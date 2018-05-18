package com.example.benchmark.wsTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class TestRestController {

    @Autowired
    private final RestTemplate restTemplate;
    private HttpHeaders headers;

    public TestRestController(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
    }

    @GetMapping("/test")
    public void startTest(){
        // create new wsManager: connect and subscribe to WS
        WSManager wsManager = new WSManager();

        // get the id and pending status of all the films
        MovieList movieInfoList = getMovieInfoList();

//        wsManager.movieListUpload(movieInfoList);
        Long time = System.currentTimeMillis();
        ArrayList<RequestResponse> responses = moviePostContinuos();
        System.out.println("Tempo totale:   "+ (System.currentTimeMillis() - time));
        System.out.println(responses.toString());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(responses.toString());

    }

    private MovieList getMovieInfoList(){
        return restTemplate.getForObject(
                "http://registry-service/api/movies"
                , MovieList.class);
    }

    private ArrayList<RequestResponse> movieRequestPeak(MovieList movieInfoList) { // TODO: 18/05/18 NOT WORKING

        ArrayList<RequestResponse> responses = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        int testCyclesCount = 1000;
        final CountDownLatch latch = new CountDownLatch(testCyclesCount);
        for (int i = 0; i< testCyclesCount; i++) {
            final int index = i % movieInfoList.getMovieResources().size();
            executor.submit(() -> {
                latch.countDown();
                System.out.println("COUNT= "+(testCyclesCount-(int)latch.getCount()));
                final long t = System.currentTimeMillis();

                ResponseEntity<MovieList> forEntity = restTemplate.exchange("http://registry-service/api/movies/{movieId}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        MovieList.class,
                        movieInfoList.getMovieResources().get(testCyclesCount-(int)latch.getCount()).getMovieId());

                final long time = System.currentTimeMillis() - t;
                RequestResponse r = new RequestResponse(testCyclesCount-(int)latch.getCount(), time, forEntity.getStatusCode().compareTo(HttpStatus.OK) == 0);

                System.out.println(r);
                responses.add(r);
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responses;
    }

    private ArrayList<RequestResponse> movieRequestContinuos(MovieList movieInfoList) {

        int index;
        ArrayList<RequestResponse> responses = new ArrayList<>();
        int testCyclesCount = 100;
        for (int i=0; i<testCyclesCount; i++) {
            index = i % movieInfoList.getMovieResources().size();
            Long time = System.currentTimeMillis();
            ResponseEntity<MovieList> forEntity = restTemplate.exchange("http://registry-service/api/movies/{movieId}",
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    MovieList.class,
                    movieInfoList.getMovieResources().get(index).getMovieId());
            time = System.currentTimeMillis() - time;
            try {
                if((100-time>0))
                    Thread.sleep(100-time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RequestResponse e = new RequestResponse(index, time, forEntity.getStatusCode().compareTo(HttpStatus.OK) == 0);
            System.out.println(e);
            responses.add(e);
        }
        return responses;
    }

    private ArrayList<RequestResponse> moviePostContinuos() {

        ArrayList<RequestResponse> responses = new ArrayList<>();

        int testCyclesCount = 100;
        for (int i=0; i<testCyclesCount; i++) {
            Long time = System.currentTimeMillis();
            String id = UUID.randomUUID().toString();
            Movie res = new Movie(id, "Harry Potter "+i, "Chris Columbus",LocalDate.of(1994, 9, 17) , "English", 195, true, 1920, 1080);
            HttpEntity<Movie> entity = new HttpEntity<>(res, headers);
            ResponseEntity<Movie> response = restTemplate.exchange(
                    "http://registry-service/api/movies"
                    , HttpMethod.POST
                    , entity
                    , Movie.class);
            time = System.currentTimeMillis() - time;
            try {
                if((100-time>0))
                    Thread.sleep(100-time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RequestResponse e = new RequestResponse(i, time, response.getStatusCode().compareTo(HttpStatus.CREATED) == 0);
            System.out.println(e);
            responses.add(e);
        }
        return responses;
    }


}
