package com.benchmark.registry;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.Optional;

@Table("movie_informations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @PrimaryKey
    private String id;

//    @SASI(analyzed = true, indexMode = CONTAINS)
    private String title;
    private String director;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private String language;
    private int duration;
    private boolean pending;
    private int width;
    private int height;

    public void editMovie(Movie newMovie) {
        Optional.ofNullable(newMovie.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(newMovie.getDirector()).ifPresent(this::setDirector);
        Optional.ofNullable(newMovie.getReleaseDate()).ifPresent(this::setReleaseDate);
        Optional.ofNullable(newMovie.getLanguage()).ifPresent(this::setLanguage);
        if(newMovie.duration!=0) this.setDuration(newMovie.duration);
        if(newMovie.width!=0) this.setWidth(newMovie.width);
        if(newMovie.height!=0) this.setHeight(newMovie.height);
        this.setPending(newMovie.isPending());
    }

}


