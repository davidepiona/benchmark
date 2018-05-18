package com.benchmark.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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

    @PostMapping("/upload/{id}")
    public HttpEntity<?> addImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            //todo controllare che l'id esista prima di copiare il video
            File f = new File(props.getPath(), id+".mp4");
            String path = f.getAbsolutePath();
            System.out.println("INIZIO: InputStream targetStream = FileUtils.openInputStream(initialFile);Il file al percorso"+ path+ "esiste?  |"+ f.exists());
            file.transferTo(f);
            System.out.println("FINE: Il file al percorso"+ path+ "esiste?  |"+ f.exists());

            FFmpegHandlerImpl ffmpeg = new FFmpegHandlerImpl(id, props.getEnvironment());
            System.out.println(ffmpeg.getHeight()+"x"+ffmpeg.getWidth()+"   "+ ffmpeg.getRatio()+ " Duration:"+ ffmpeg.getDuration());
            Movie res = new Movie(id, null, null, null, null, ffmpeg.getDuration(), false, ffmpeg.getWidth(), ffmpeg.getHeight());
//            Movie res = new Movie(id, null, null, null, null, 0, false, 0,0 );
            //headers.add("X-HTTP-Method-Override", "PATCH");
            HttpEntity<Movie> entity = new HttpEntity<>(res, headers);
            return restTemplate.exchange(
                    "http://registry-service/api/movies/{movieId}"
                    , HttpMethod.PUT
                    , entity
                    , Movie.class, id);

        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (HttpClientErrorException hcee) {
            return ResponseEntity.status(hcee.getStatusCode()).contentType(MediaType.APPLICATION_JSON_UTF8).body(hcee.getResponseBodyAsString());
        }
    }

}
