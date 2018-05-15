package com.example.benchmark.wsTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieList {

    private List<MovieInfo> MovieResources;
}
