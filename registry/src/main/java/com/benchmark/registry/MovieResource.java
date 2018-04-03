package com.benchmark.registry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.Date;
import java.util.Optional;

@Data
@AllArgsConstructor
public class MovieResource extends ResourceSupport{

    private String movieId;
    private String title;
    private String director;
    private Date releaseDate;
    private String language;
    private int duration;

    public Movie toMovie() {
        Movie m = new Movie();
        m.setId(movieId);
        m.setTitle(title);
        m.setDirector(director);
        m.setReleaseDate(releaseDate);
        m.setLanguage(language);
        m.setDuration(duration);
        return m;
    }


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


}
