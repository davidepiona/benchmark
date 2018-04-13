package com.benchmark.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MovieResource extends ResourceSupport {

    private String movieId;
    private String title;
    private String director;
    private LocalDate releaseDate;
    private String language;
    private int duration;
    private boolean pending;
    private int width;
    private int height;

    public static MovieResource create(Movie res) {
        return new MovieResource(
                res.getId(),
                res.getTitle(),
                res.getDirector(),
                res.getReleaseDate(),
                res.getLanguage(),
                res.getDuration(),
                res.isPending(),
                res.getWidth(),
                res.getHeight()
        );
    }


    public Movie toMovie() {
        Movie m = new Movie();
        m.setDuration(duration);
        m.setLanguage(language);
        m.setReleaseDate(releaseDate);
        m.setDirector(director);
        m.setTitle(title);
        m.setId(movieId);
        m.setPending(pending);
        m.setWidth(width);
        m.setHeight(height);
        return m;
    }


}
