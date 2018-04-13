package com.benchmark.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.*;
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
    @PostMapping("/upload/{id}")
    public HttpEntity<?> addImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            File f = new File(props.getPath(), id+".mp4");
            String path = f.getAbsolutePath();
            System.out.println("INIZIO: Il file al percorso"+ path+ "esiste?  |"+ f.exists());
            file.transferTo(f);
            System.out.println("FINE: Il file al percorso"+ path+ "esiste?  |"+ f.exists());

            FFmpegHandlerImpl FFmpeg = new FFmpegHandlerImpl(id);
            System.out.println(FFmpeg.getHeight()+"x"+FFmpeg.getWidth()+"   "+ FFmpeg.getRatio()+ " Duration:"+ FFmpeg.getDuration());
            Movie res = new Movie(id, null, null, null, null, 0, false);
            //headers.add("X-HTTP-Method-Override", "PATCH");
            HttpEntity<Movie> entity = new HttpEntity<>(res, headers);
            return restTemplate.exchange(
                    "http://registry-service/api/movies/edit"
                    , HttpMethod.POST
                    , entity
                    , Movie.class);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
