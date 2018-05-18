package com.benchmark.registry;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getMovies() {
        return Lists.newArrayList(movieRepository.findAll());
    }

    @Override
    public Movie getMovieById(String id) {
        return movieRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Movie> getMovieLike(String part) {
        return movieRepository.findByTitleLike(part);
    }

    @Override
    public void deleteMovie(String id) {
        movieRepository.deleteById(id);
    }

    @Override
    public Movie addMovie(Movie res) {
        return movieRepository.save(res);
    }
}
