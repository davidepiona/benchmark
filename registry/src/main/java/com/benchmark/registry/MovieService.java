package com.benchmark.registry;

import java.util.List;

public interface MovieService {

    List<Movie> getMovies();

    Movie getMovieById(String id);

    List<Movie> getMovieLike(String name);

    void deleteMovie(String id);

    Movie addMovie(Movie res);

}
