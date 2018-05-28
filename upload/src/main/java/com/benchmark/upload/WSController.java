package com.benchmark.upload;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;

@Controller
public class WSController {

    @Autowired
    private UploadProperties props;

    private final RestTemplate restTemplate;

    public WSController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @MessageMapping("/echo")
    public boolean echo(String message) {
        return StringUtils.containsIgnoreCase(message, "hello");
    }

    @MessageMapping("/upload")
    public WSResponse upload(UploadRequest req) {
        StringWriter w = new StringWriter();
        w.append(req.getFileContent());
        try {
            FileUtils.writeByteArrayToFile(new File(props.getPath(), "file" + req.getPartId() + req.getId() + ".tmp"), Base64.getDecoder().decode(w.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("part: " + req.getPartId() + "  count: " + req.getPartCount());
        if (req.getPartId() == req.getPartCount()) {
            File dest = new File(props.getPath(), "file0" + req.getId() + ".tmp");
            int partId;
            for (partId=1; partId < req.getPartCount(); partId++) {
                byte[] buffer = null;
                try {
                    File file = new File(props.getPath(), "file" + partId + req.getId() + ".tmp");
                    buffer = IOUtils.toByteArray(file.toURI());
                    FileUtils.writeByteArrayToFile(dest, buffer, true);
                    if(!file.delete()) {
                        System.out.println("File temporaneo non eliminato: file" + partId + req.getId() + ".tmp");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File file = new File(props.getPath(), "file" + partId + req.getId() + ".tmp");
            if(!file.delete()) {
                System.out.println("File temporaneo non eliminato: file" + partId + req.getId() + ".tmp");
            }
            dest.renameTo(new File(props.getPath(), req.getId() + ".mp4"));
            if (!postToRegistry(req)) {
                return new WSResponse(req.getId(), false);
            }
            return new WSResponse(req.getId(), true);
        }

        return new WSResponse(req.getId(), false);
    }

    private boolean postToRegistry(UploadRequest req) {
        FFmpegHandlerImpl ffmpeg = null;
        try {
            ffmpeg = new FFmpegHandlerImpl(req.getId(), props.getEnvironment());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ffmpeg.getHeight() + "x" + ffmpeg.getWidth() + "   " + ffmpeg.getRatio() + " Duration:" + ffmpeg.getDuration());
        Movie res = new Movie(req.getId(), null, null, null, null, ffmpeg.getDuration(), false, ffmpeg.getWidth(), ffmpeg.getHeight());
        restTemplate.exchange(
                "http://registry-service/api/movies/{movieId}"
                , HttpMethod.PUT
                , new HttpEntity<>(res)
                , Movie.class, req.getId());
        return true;
    }

}
