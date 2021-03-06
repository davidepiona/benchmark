package com.example.benchmark.wsTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfo {
    private String movieId;
    private boolean pending;
}