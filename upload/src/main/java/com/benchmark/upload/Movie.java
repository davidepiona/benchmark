package com.benchmark.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie{


    private String id;

//    @SASI(analyzed = true, indexMode = CONTAINS)
    private String title;
    private String director;

    private LocalDate releaseDate;
    private String language;
    private int duration;
    private boolean pending;
    private int width;
    private int height;

    public Movie(String id) {
        this.id = id;
    }

    public Movie(String id, int duration, boolean pending, int width, int height) {
        this.id = id;
        this.duration = duration;
        this.pending = pending;
        this.width = width;
        this.height = height;
    }
}


