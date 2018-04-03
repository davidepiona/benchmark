package com.benchmark.registry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    private String id;
    private String title;
    private String director;
    private Date releaseDate;
    private String language;
    private int duration;
}
