package com.benchmark.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RegistryRestController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public HttpEntity<?> getMovies() {

        System.out.println("Entrato");
        return ResponseEntity.ok(createListResource(movieService.getMovies()));
    }

    @GetMapping("/movies/{id}")
    public HttpEntity<?> getMovieById(@PathVariable String id) {

        return Optional.ofNullable(movieService.getMovieById(id))
                .map(a -> ResponseEntity.ok(createResource(a)))
                .orElseThrow(MovieNotFoundException::new);
    }

    @DeleteMapping("/movies/{id}")
    public HttpEntity<?> deleteMovie(@PathVariable String id) {

        return Optional.ofNullable(movieService.getMovieById(id))
                .map(a -> {
                    movieService.deleteMovie(id);
                    return ResponseEntity.noContent().build();          //204
                })
                .orElseThrow(MovieNotFoundException::new);              //404
    }

    //todo non funziona , sistemare!
    @GetMapping("/movies/like/{part}")
    public HttpEntity<?> movieUserNameLike(@PathVariable String part) {

        return ResponseEntity.ok(createListResource(movieService.getMovieLike(part)));
    }

    @PostMapping("/movies")
    public HttpEntity<?> addMovie(@RequestBody Movie res) {
        Movie movieById = movieService.getMovieById(res.getId());
        if( movieById!= null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        MovieResource acc = createResource(MovieResource.create(movieService.addMovie(res)));
        URI location = URI.create(acc.getLink("self").getHref());
        return ResponseEntity.created(location).body(acc);

    }

    private MovieResource createResource(MovieResource res) {
        res.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder.methodOn(getClass()).getMovieById(res.getMovieId())
                ).withSelfRel());

        return res;
    }

    private MovieResource createResource(Movie res) {
        MovieResource movieResource = MovieResource.create(res);
        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder.methodOn(getClass()).getMovieById(res.getId())
                ).withSelfRel());

        return movieResource;
    }

    private MovieList createListResource(List<Movie> movies) {

        MovieList movieList = new MovieList(movies.stream()
                .map(a -> createResource(MovieResource.create(a)))
                .collect(Collectors.toList()));
        return movieList;
    }
}
