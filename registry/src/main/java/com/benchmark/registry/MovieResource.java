package com.benchmark.registry;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class MovieResource extends ResourceSupport{

    private String movieId;
    private String title;
    private String director;
    private LocalDate releaseDate;
    private String language;
    private int duration;

    public static MovieResource create(Movie res) {
        return new MovieResource(
                res.getId(),
                res.getTitle(),
                res.getDirector(),
                res.getReleaseDate(),
                res.getLanguage(),
                res.getDuration()
        );
    }
    //        return m;
    //        m.setDuration(duration);
    //        m.setLanguage(language);
    //        m.setReleaseDate(releaseDate);
    //        m.setDirector(director);
    //        m.setTitle(title);
    //        m.setId(movieId);
    //        Movie m = new Movie();
//    public Movie toMovie() {

//    }



}
