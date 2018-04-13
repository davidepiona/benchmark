package com.benchmark.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    public Movie(String id) {
        this.id = id;
    }
}


