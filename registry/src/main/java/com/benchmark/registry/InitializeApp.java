package com.benchmark.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
class InitializeApp implements ApplicationRunner {

    @Autowired
    private MovieService movieService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        movieService.addMovie(
                new Movie(UUID.randomUUID().toString(), "Titanic", "James Cameron",LocalDate.of(1997, 11, 01) , "English", 195, false));
        movieService.addMovie(
                new Movie(UUID.randomUUID().toString(), "Gone with the Wind", "Victor Fleming", LocalDate.of(1939, 12,15), "English", 221, true));

        System.out.println(movieService.getMovies());
    }

}
