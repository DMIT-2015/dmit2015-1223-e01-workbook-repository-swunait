package dmit2015.listener;

import dmit2015.entity.Movie;
import dmit2015.persistence.MovieRepository;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;

@WebListener
public class MoviesApplicationStartupListener implements ServletContextListener {

    @Inject
    MovieRepository _movieRepository;

    public MoviesApplicationStartupListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */

        if (_movieRepository.count() == 0) {
            try {
                try (var reader = new BufferedReader(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream("/data/csv/movies.csv")))) ) {
                    String line;
//                    final var delimiter = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    // Skip the first line as it is containing column headings
                    reader.readLine();
                    while ((line = reader.readLine()) != null) {
                        Optional<Movie> optionalMovie = Movie.parseCsv(line);
                        if (optionalMovie.isPresent()) {
                            Movie csvMovie = optionalMovie.orElseThrow();
                            _movieRepository.add(csvMovie);
                        }
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

}
