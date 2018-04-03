package com.benchmark.registry;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieById(String id) {
        return movieRepository.findById(id)
                .orElseThrow(MovieNotFoundException::new);
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
