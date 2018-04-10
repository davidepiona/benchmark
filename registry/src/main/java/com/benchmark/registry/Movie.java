package com.benchmark.registry;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.SASI;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.Date;

import static org.springframework.data.cassandra.core.mapping.SASI.IndexMode.CONTAINS;

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

}


