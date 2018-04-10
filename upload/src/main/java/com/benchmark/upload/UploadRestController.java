package com.benchmark.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UploadRestController {

    @Autowired
    private UploadProperties props;

    private final RestTemplate restTemplate;

    private HttpHeaders headers;

    public UploadRestController(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        headers = new HttpHeaders();
        headers.add("X-Forwarded-Host", "localhost:8020");
    }
    //todo potrebbe tornare un boolean con lo stato della richiesta
    @PostMapping("/upload")
    public HttpEntity<?> addImage(@RequestParam("file") MultipartFile file) {
        try {
            String id = UUID.randomUUID().toString();
            File f = new File(props.getPath(), UUID.randomUUID().toString()+".mp4");
            String path = f.getAbsolutePath();
            System.out.println("INIZIO: Il file al percorso"+ path+ "esiste?  |"+ f.exists());
            file.transferTo(f);
            System.out.println("FINE: Il file al percorso"+ path+ "esiste?  |"+ f.exists());


            Movie res = new Movie(id, file.getOriginalFilename(), "James Cameron", LocalDate.of(1997, 11, 01) , "English", 195);
            HttpEntity<Movie> entity = new HttpEntity<>(res, headers);
            restTemplate.postForEntity(
                    "http://registry-service/api/movies"
                    , entity
                    , Movie.class);

            return ResponseEntity.ok().build();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
