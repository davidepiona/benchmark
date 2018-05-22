package com.benchmark.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RegistryRestController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private UploadProperties props;

    @GetMapping("/movies")
    public HttpEntity<?> getMovies() {

        System.out.println("get-movies");
        return ResponseEntity.ok(createListResource(movieService.getMovies()));

    }

    @GetMapping("/movies/{id}")
    public HttpEntity<?> getMovieById(@PathVariable String id) {

        System.out.println("Get-movie"+id);
        return Optional.ofNullable(movieService.getMovieById(id))
                .map(a -> ResponseEntity.ok(createResource(a)))
                .orElseThrow(MovieNotFoundException::new);
    }

    @DeleteMapping("/movies/{id}")
    public HttpEntity<?> deleteMovie(@PathVariable String id) {

        return Optional.ofNullable(movieService.getMovieById(id))
                .map(a -> {
                    movieService.deleteMovie(id);
                    System.out.println("Delete-movie"+id);
                    File file = new File(props.getPath(),id+".mp4");
                    if(!file.delete()){
                        System.out.println("ERRORE ELIMINAZIONE FILE: "+ id+".mp4");
                    }
                    return ResponseEntity.noContent().build();          //204
                })
                .orElseThrow(MovieNotFoundException::new);              //404
    }

    @DeleteMapping("/movies/delete")
    public HttpEntity<?> deleteAll(){
        movieService.deleteAll();
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/movies")
    public HttpEntity<?> addMovie(@RequestBody Movie res) {
        res.setId(UUID.randomUUID().toString());
        Movie movieById = movieService.getMovieById(res.getId());
        if( movieById!= null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        MovieResource acc = createResource(MovieResource.create(movieService.addMovie(res)));
        URI location = URI.create(acc.getLink("self").getHref());
        System.out.println("Post-movie"+res.getTitle());
        return ResponseEntity.created(location).body(acc);

    }

    @PutMapping("/movies/{id}")
    public HttpEntity<?> editMovie(@PathVariable String id, @RequestBody Movie res) {
        Movie movieById = movieService.getMovieById(id);
        System.out.println(movieById);
        return Optional.ofNullable(movieById)
                .map(a -> {
                    movieById.editMovie(res);
                    createResource(MovieResource.create(movieService.addMovie(movieById)));
                    System.out.println("Put-movie"+res.getTitle());
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(MovieNotFoundException::new);
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
