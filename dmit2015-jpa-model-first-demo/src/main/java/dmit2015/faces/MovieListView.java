package dmit2015.faces;

import dmit2015.entity.Movie;
import dmit2015.persistence.MovieRepository;
import dmit2015.security.Security;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.omnifaces.util.Messages;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Named("currentMovieListView")
@ViewScoped
public class MovieListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private MovieRepository _movieRepository;

    @Getter
    private List<Movie> movieList;

//    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            movieList = _movieRepository.findAll();
        } catch (RuntimeException ex) {
            Messages.addGlobalWarn(ex.getMessage());
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }

    @Inject
    private Security _security;

    public void onImportData() {
        if ( ! _security.isAuthenticated()) {
            throw new RuntimeException("Access denied. You do not have permission to execute this method.");
        }
        //if (_movieRepository.count() == 0) {
            try {
                final String username = _security.getUsername();
                try ( InputStream csvInputStream = getClass().getResourceAsStream("/data/csv/movies.csv");
                      InputStreamReader csvInputStreamReader = new InputStreamReader(Objects.requireNonNull(csvInputStream));
                      var reader = new BufferedReader(csvInputStreamReader) ) {
                    String line;
                    // Skip the first line as it is containing column headings
                    reader.readLine();
                    while ((line = reader.readLine()) != null) {
                        Optional<Movie> optionalMovie = Movie.parseCsv(line);
                        if (optionalMovie.isPresent()) {
                            Movie csvMovie = optionalMovie.orElseThrow();
                            csvMovie.setUsername(username);
                            try {
                                _movieRepository.add(csvMovie);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                Messages.addGlobalInfo("Finished importing data.");
                movieList = _movieRepository.findAll();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        //}

    }
}