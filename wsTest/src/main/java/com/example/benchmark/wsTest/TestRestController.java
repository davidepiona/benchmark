package com.example.benchmark.wsTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class TestRestController {

    @Autowired
    private final RestTemplate restTemplate=new RestTemplate();

    public List<MovieList> getMovieId(){

        MovieList forObject = restTemplate.getForObject(
                "http://registry-service/api/movies/8a6ac739-b654-47a7-9e77-384c068c5acb"
                , MovieList.class);
        System.out.println(forObject);
        return null;
    }

    @GetMapping("/test")
    public void startTest(){
        MovieList movieList = restTemplate.getForObject(
                "http://registry-service/api/movies"
                , MovieList.class);
        System.out.println(movieList);
        System.out.println("SIZE: "+ movieList.getMovieResources().size());
        WSManager wsManager = new WSManager();
        wsManager.runWS(movieList);

    }

}
