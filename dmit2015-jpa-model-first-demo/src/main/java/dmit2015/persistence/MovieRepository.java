package dmit2015.persistence;

import dmit2015.entity.Movie;
import dmit2015.security.Security;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovieRepository {

    @PersistenceContext
    private EntityManager em;

//    @Inject
//    private SecurityContext _securityContext;

    @Inject
    private Security _security;

    @Transactional
    public void add(Movie newMovie) {
//        if (_securityContext.isCallerInRole("Sales")) {
//            throw new RuntimeException("Access denied. You do not have permission to execute this method.");
//        }
//        final String username = _securityContext.getCallerPrincipal().getName();
        if (! _security.isInAnyRole("Sales")) {
            throw new RuntimeException("Access denied. You do not have permission to execute this method.");
        }
        final String username = _security.getUsername();
        newMovie.setUsername(username);

        em.persist(newMovie);
    }

    public Optional<Movie> findById(Long movieId) {
        Optional<Movie> optionalMovie = Optional.empty();
        try {
            Movie querySingleResult = em.find(Movie.class, movieId);
            if (querySingleResult != null) {
                optionalMovie = Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return optionalMovie;
    }

    public List<Movie> findAll() {
        if ( !_security.isAuthenticated()) {
            throw new RuntimeException("Access denied. Anonymous users are not allowed to view movies");
        }
        // Return all movies if user is unauthenticated or has the IT Role
        if (!_security.isAuthenticated() || _security.isInAnyRole("IT")) {
            return em.createQuery("SELECT o FROM Movie o ", Movie.class)
                    .getResultList();
        } else { // Return movies created by the current user
            String username = _security.getUsername();
            return em.createQuery("SELECT o FROM Movie o where o.username = :usernameParam "
                            , Movie.class)
                    .setParameter("usernameParam", username)
                    .getResultList();
        }
    }

    @Transactional
    public Movie update(Long id, Movie updatedMovie) {
        if (!_security.isInAnyRole("Sales","IT")) {
            throw new RuntimeException("Access denied. You do not have permission to execute this method.");
        }

        Movie existingMovie = null;

        Optional<Movie> optionalMovie = findById(id);
        if (optionalMovie.isPresent()) {
            // Update only properties that is editable by the end user
            existingMovie = optionalMovie.orElseThrow();
            existingMovie.setTitle(updatedMovie.getTitle());
            existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
            existingMovie.setRating(updatedMovie.getRating());
            existingMovie.setGenre(updatedMovie.getGenre());
            existingMovie.setPrice(updatedMovie.getPrice());

            existingMovie = em.merge(existingMovie);
        }

        return existingMovie;
    }

    @Transactional
    public void delete(Movie existingMovie) {
        if (!_security.isInAnyRole("Sales","IT")) {
            throw new RuntimeException("Access denied. You do not have permission to execute this method.");
        }

        if (em.contains(existingMovie)) {
            em.remove(existingMovie);
        } else {
            em.remove(em.merge(existingMovie));
        }
    }

    @Transactional
    public void deleteById(Long movieId) {
        if (!_security.isInAnyRole("Sales")) {
            throw new RuntimeException("Access denied. You do not have permission to execute this method.");
        }

        Optional<Movie> optionalMovie = findById(movieId);
        if (optionalMovie.isPresent()) {
            Movie existingMovie = optionalMovie.orElseThrow();
            em.remove(existingMovie);
        }
    }

    public long count() {
        return em.createQuery("SELECT COUNT(o) FROM Movie o", Long.class).getSingleResult().longValue();
    }

    @Transactional
    public void deleteAll() {
        em.flush();
        em.clear();
        em.createQuery("DELETE FROM Movie").executeUpdate();
    }

}