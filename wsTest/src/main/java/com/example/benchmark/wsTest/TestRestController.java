package com.example.benchmark.wsTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TestRestController {

    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private TestProperties props;
    private HttpHeaders headers;
    private ExecutorService executor = null;
    private WSManager wsManager = null;

    public TestRestController(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
    }

    @GetMapping("/test/{type}")
    public void startTest(@PathVariable String type) {
        // get the id and pending status of all the films
        MovieList movieInfoList = getMovieInfoList();
        Long time = System.currentTimeMillis();
        ArrayList<RequestResponse> responses = null;
        System.out.println(type);
        switch (type) {
            case "getPeak":
                responses = requestPeak(movieInfoList);
                break;
            case "getCont":
                responses = requestContinuos(movieInfoList);
                break;
            case "postPeak":
                responses = postPeak();
                break;
            case "postCont":
                responses = postContinuos();
                break;
            case "upPeak":
                responses = uploadPeak(movieInfoList);
                break;
            case "upCont":
                responses = uploadContinuos(movieInfoList);
                break;
            case "upPeakWS":
                // create new wsManager: connect and subscribe to WS
                if (this.wsManager == null) {
                    wsManager = new WSManager(props);
                }
                wsManager.subscribe();
                responses = uploadPeakWS(movieInfoList);
                wsManager.unsubscribe();        // TODO: 22/05/18 penso che possa 'disiscriversi' troppo presto 
                break;
            case "upContWS":
                // create new wsManager: connect and subscribe to WS
                if (this.wsManager == null) {
                    wsManager = new WSManager(props);
                }
                wsManager.subscribe();
                responses = uploadContinuosWS(movieInfoList);
                wsManager.unsubscribe();    // TODO: 22/05/18 idem vedi su 
                break;
            default:
                System.out.println("wrong type");
        }
        System.out.println("Tempo totale:   " + (System.currentTimeMillis() - time));
        System.out.println(responses.toString());
        System.out.println("SIZEEE: " + responses.size());

    }

    @GetMapping("/deleteAll")
    public void deleteAll() {

        restTemplate.exchange("http://gateway-service/api/movies/delete",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);
    }

    @GetMapping("/runTest")
    public void runTest() {
        // get the id and pending status of all the films
        if (this.wsManager == null) {
            this.wsManager = new WSManager(props);
        }
        MovieList movieInfoList = getMovieInfoList();
        wsManager.subscribe();
        OutputStream output = null;
        try {
            output = new FileOutputStream("/home/davide/dev/benchmark/output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintStream printOut = new PrintStream(output);
//        System.setErr(printOut);
//        System.setOut(printOut);
        int waitingTime = 10000;
        Long time = null;
        ArrayList<ArrayList<RequestResponse>> responses = new ArrayList<>();

        try {
            System.err.println("GET - INIZIO TEST 1");
            time = System.currentTimeMillis();
            responses.add(postPeak());

            System.err.println("\tFINE TEST 1, impiegati " + (System.currentTimeMillis() - time) + " secondi.");
            Thread.sleep(waitingTime);
            System.err.println("INIZIO TEST 2");

            time = System.currentTimeMillis();
            responses.add(postContinuos());

            System.err.println("\tFINE TEST 2, impiegati " + (System.currentTimeMillis() - time) + " secondi.");
            Thread.sleep(waitingTime/2);
            movieInfoList = getMovieInfoList();
            Thread.sleep(waitingTime/2);
            System.err.println("POST - INIZIO TEST 3");

            time = System.currentTimeMillis();
            responses.add(requestPeak(movieInfoList));

            System.err.println("\tFINE TEST 3, impiegati " + (System.currentTimeMillis() - time) + " secondi.");
            Thread.sleep(waitingTime);
            System.err.println("INIZIO TEST 4");

            time = System.currentTimeMillis();
            responses.add(requestContinuos(movieInfoList));

            System.err.println("\tFINE TEST 4, impiegati " + (System.currentTimeMillis() - time) + " secondi.");
            Thread.sleep(waitingTime);
            System.err.println("UPLOAD - INIZIO TEST 5");

            time = System.currentTimeMillis();
            responses.add(uploadPeak(movieInfoList));

            System.err.println("\tFINE TEST 5, impiegati " + (System.currentTimeMillis() - time) + " secondi.");
            Thread.sleep(waitingTime);
            System.err.println("INIZIO TEST 6");

            time = System.currentTimeMillis();
            responses.add(uploadContinuos(movieInfoList));

            System.err.println("\tFINE TEST 6, impiegati " + (System.currentTimeMillis() - time) + " secondi.");
            Thread.sleep(waitingTime);
            System.err.println("UPLOADWS - INIZIO TEST 7");

            time = System.currentTimeMillis();
            responses.add(uploadPeakWS(movieInfoList));

            System.err.println("\tFINE TEST 7, impiegati " + (System.currentTimeMillis() - time) + " secondi.");
            Thread.sleep(waitingTime);
            System.err.println("INIZIO TEST 8");

            time = System.currentTimeMillis();
            responses.add(uploadContinuosWS(movieInfoList));

            System.err.println("\tFINE TEST 8, impiegati " + (System.currentTimeMillis() - time) + " secondi.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Tempo totale:   " + (System.currentTimeMillis() - time));

        for (int i = 0; i < responses.size(); i++) {
            int errors = 0;
            int timeSum = 0;
            float avg = 0;
            for (int j = 0; j < responses.get(i).size(); j++) {
                if (!responses.get(i).get(j).getSuccess()) {
                    errors++;
                } else {
                    timeSum += responses.get(i).get(j).getTime();
                }
            }
            if (responses.get(i).size() != 0) {
                avg = timeSum / responses.get(i).size();
            }
            wsManager.unsubscribe();
            System.err.println("\nRESPONSES" + (i + 1) + "\tLunghezza " + responses.get(i).size() + "\tErrori: " + errors + "\tTempo medio: " + avg);
            System.out.println(responses.get(i).toString());
        }


    }

    private MovieList getMovieInfoList() {
        return restTemplate.getForObject(
                "http://gateway-service/api/movies"
                , MovieList.class);
    }

    private ArrayList<RequestResponse> requestPeak(MovieList movieInfoList) {

        final ArrayList<RequestResponse> responses = new ArrayList<>();
        executor = Executors.newFixedThreadPool(200);

        int testCyclesCount = 1000;
        final CountDownLatch latch = new CountDownLatch(testCyclesCount);
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < testCyclesCount; i++) {
            final int index = i % movieInfoList.getMovieResources().size();
            executor.submit(() -> {
                final int number = counter.incrementAndGet();
                final long t = System.currentTimeMillis();

                try {
                    ResponseEntity<MovieList> res = restTemplate.exchange("http://gateway-service/api/movies/{movieId}",
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            MovieList.class,
                            movieInfoList.getMovieResources().get(index).getMovieId());

                    final long time = System.currentTimeMillis() - t;
                    RequestResponse r = new RequestResponse(number, time, res.getStatusCodeValue(), res.getStatusCodeValue()==200);
                    synchronized (responses) {
                        System.out.println("COUNT= " + number + "--- " + r);
                        responses.add(r);
                    }
                    latch.countDown();
                } catch (HttpServerErrorException ex) {
                    if (ex.getRawStatusCode() == 504) {
                        System.out.println("Gateway timeout");
                        RequestResponse e = new RequestResponse(number, 10000, 504, false);
                        System.out.println(e);
                        responses.add(e);
                    }
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Waiting for shutdown");
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responses;
    }

    private ArrayList<RequestResponse> requestContinuos(MovieList movieInfoList) {

        int index;
        ArrayList<RequestResponse> responses = new ArrayList<>();
        int testCyclesCount = 100;
        for (int i = 0; i < testCyclesCount; i++) {
            index = i % movieInfoList.getMovieResources().size();
            Long time = System.currentTimeMillis();

            try {
                ResponseEntity<MovieList> res = restTemplate.exchange("http://gateway-service/api/movies/{movieId}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        MovieList.class,
                        movieInfoList.getMovieResources().get(index).getMovieId());

                time = System.currentTimeMillis() - time;
                try {
                    if ((50 - time > 0))
                        Thread.sleep(50 - time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RequestResponse e = new RequestResponse(index, time, res.getStatusCodeValue(), res.getStatusCodeValue()==200);
                System.out.println(e);
                responses.add(e);
            } catch (HttpServerErrorException ex) {
                if (ex.getRawStatusCode() == 504) {
                    System.out.println("Gateway timeout");
                    RequestResponse e = new RequestResponse(i, time, 504, false);
                    System.out.println(e);
                    responses.add(e);
                }
            }
        }
        return responses;
    }


    private ArrayList<RequestResponse> postPeak() {
        final ArrayList<RequestResponse> responses = new ArrayList<>();
        executor = Executors.newFixedThreadPool(200);

        int testCyclesCount = 1000;
        final CountDownLatch latch = new CountDownLatch(testCyclesCount);
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < testCyclesCount; i++) {
            executor.submit(() -> {
                final int number = counter.incrementAndGet();
                final long t = System.currentTimeMillis();

                final String id = UUID.randomUUID().toString();
                final Movie movie = new Movie(id, "Harry Potter " + id, "Chris Columbus", LocalDate.of(1994, 9, 17), "English", 195, true, 1920, 1080);;
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<Movie> entity = new HttpEntity<>(movie, headers);
                try {
                    ResponseEntity<Movie> res = restTemplate.exchange(
                            "http://gateway-service/api/movies"
                            , HttpMethod.POST
                            , entity
                            , Movie.class);
                    final long time = System.currentTimeMillis() - t;
                    final RequestResponse r = new RequestResponse(number, time, res.getStatusCodeValue(), res.getStatusCodeValue()==201);
                    synchronized (responses) {
                        System.out.println("COUNT= " + number + "--- " + r);
                        responses.add(r);
                    }
                    latch.countDown();
                } catch (HttpServerErrorException ex) {
                    if (ex.getRawStatusCode() == 504) {
                        System.out.println("Gateway timeout");
                        RequestResponse e = new RequestResponse(number, 10000, 504, false);
                        System.out.println(e);
                        responses.add(e);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Waiting for shutdown");
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responses;
    }

    private ArrayList<RequestResponse> postContinuos() {

        ArrayList<RequestResponse> responses = new ArrayList<>();

        int testCyclesCount = 100;
        for (int i = 0; i < testCyclesCount; i++) {
            Long time = System.currentTimeMillis();

            String id = UUID.randomUUID().toString();
            Movie movie = new Movie(id, "Harry Potter " + i, "Chris Columbus", LocalDate.of(1994, 9, 17), "English", 195, true, 1920, 1080);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Movie> entity = new HttpEntity<>(movie, headers);
            try {
                ResponseEntity<Movie> res = restTemplate.exchange(
                        "http://gateway-service/api/movies"
                        , HttpMethod.POST
                        , entity
                        , Movie.class);

                time = System.currentTimeMillis() - time;

                try {
                    if ((50 - time > 0))
                        Thread.sleep(50 - time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RequestResponse e = new RequestResponse(i, time, res.getStatusCodeValue(), res.getStatusCodeValue()==201);
                System.out.println(e);
                responses.add(e);
            } catch (HttpServerErrorException ex) {
                if (ex.getRawStatusCode() == 504) {
                    System.out.println("Gateway timeout");
                    RequestResponse e = new RequestResponse(i, time, 504, false);
                    System.out.println(e);
                    responses.add(e);
                }
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responses;
    }

    private ArrayList<RequestResponse> uploadPeak(MovieList movieInfoList) {
        final ArrayList<RequestResponse> responses = new ArrayList<>();
        executor = Executors.newFixedThreadPool(200);

        int testCyclesCount = 5;
        final CountDownLatch latch = new CountDownLatch(testCyclesCount);
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < testCyclesCount; i++) {
            final int index = i % movieInfoList.getMovieResources().size();
            executor.submit(() -> {
                final int number = counter.incrementAndGet();
                final long t = System.currentTimeMillis();

                String id = movieInfoList.getMovieResources().get(index).getMovieId();
                LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("file", new FileSystemResource(new File(props.getPath())));
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);
                try {
                    ResponseEntity<String> res = restTemplate.exchange(
                            "http://gateway-service/api/upload/{movieId}",
                            HttpMethod.POST,
                            requestEntity,
                            String.class,
                            id);
                    final long time = System.currentTimeMillis() - t;
                    RequestResponse r = new RequestResponse(number, time, res.getStatusCodeValue(), res.getStatusCodeValue()==204);
                    synchronized (responses) {
                        System.out.println("COUNT= " + number + "--- " + r);
                        responses.add(r);
                    }
                    latch.countDown();
                } catch (HttpServerErrorException ex) {
                    if (ex.getRawStatusCode() == 504) {
                        System.out.println("Gateway timeout");
                        RequestResponse e = new RequestResponse(number, 10000, 504, false);
                        System.out.println(e);
                        responses.add(e);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Waiting for shutdown");
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responses;

    }

    private ArrayList<RequestResponse> uploadContinuos(MovieList movieInfoList) {

        ArrayList<RequestResponse> responses = new ArrayList<>();
        int testCyclesCount = 5;
        for (int i = 0; i < testCyclesCount; i++) {
            int index = i % movieInfoList.getMovieResources().size();
            Long time = System.currentTimeMillis();

            final String id = movieInfoList.getMovieResources().get(index).getMovieId();
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", new FileSystemResource(new File(props.getPath())));
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            final HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            System.out.println("Sto per inserire: "+ id);
            try {
                final ResponseEntity<String> res = restTemplate.exchange(
                        "http://upload-service/api/upload/{movieId}", // TODO: 28/05/18 FARLO PASSARE PER IL GATEWAY CREA PROBLEMI E SI BLOCCA (il problema sembra essere nello sleep) 
                        HttpMethod.POST,
                        requestEntity,
                        String.class,
                        id);

                time = System.currentTimeMillis() - time;
                try {
                    if ((1000 - time > 0))
                        Thread.sleep(1000 - time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RequestResponse e = new RequestResponse(i, time, res.getStatusCodeValue(), res.getStatusCodeValue()==204);
                System.out.println(e);
                responses.add(e);
            } catch (HttpServerErrorException ex) {
                if (ex.getRawStatusCode() == 504) {
                    System.out.println("Gateway timeout");
                    RequestResponse e = new RequestResponse(i, time, 504, false);
                    System.out.println(e);
                    responses.add(e);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responses;
    }



    @GetMapping("/unlock")
    public void unlock() {
        System.out.println("DIMENSIONE:" + getMovieInfoList().getMovieResources().size());
        String id = getMovieInfoList().getMovieResources().get(0).getMovieId();
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        map.add("file", new FileSystemResource(new File("/home/davide/dev/benchmark/wsTest/Bambino_Freddo.mp4")));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        System.out.println("Sending request for " + id);
        ResponseEntity<String> res = null;
        try {
            res = restTemplate.exchange(
                    "http://gateway-service/api/upload/{movieId}",
                    HttpMethod.POST,
                    requestEntity,
                    String.class,
                    id);
            System.out.println("Risposta: " + res);
        } catch (HttpServerErrorException ex) {
            if (ex.getRawStatusCode() == 504) {
                System.out.println("Gateway timeout");
                //false
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        System.out.println("USCITO");
    }

    private ArrayList<RequestResponse> uploadPeakWS(MovieList movieInfoList) {

        final ArrayList<RequestResponse> responses = new ArrayList<>();
        executor = Executors.newFixedThreadPool(200);

        int testCyclesCount = 5;
        final CountDownLatch latch = new CountDownLatch(testCyclesCount);
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < testCyclesCount; i++) {
            final int index = i % movieInfoList.getMovieResources().size();
            executor.submit(() -> {
                final int number = counter.incrementAndGet();

                synchronized (responses) {
                    wsManager.uploadRequest(movieInfoList.getMovieResources().get(index), props.getPath());
                    System.out.println("COUNT= " + number);
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Waiting for shutdown");
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responses;
    }

    private ArrayList<RequestResponse> uploadContinuosWS(MovieList movieInfoList) {

        final ArrayList<RequestResponse> responses = new ArrayList<>();
        int testCyclesCount = 5;
        for (int i = 0; i < testCyclesCount; i++) {
            int index = i % movieInfoList.getMovieResources().size();
            Long time = System.currentTimeMillis();

            wsManager.uploadRequest(movieInfoList.getMovieResources().get(index), props.getPath());

            time = System.currentTimeMillis() - time;
            System.out.println("TIME: " + time);

            try {
                if ((2000 - time > 0))
                    Thread.sleep(2000 - time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return responses;
    }
}
