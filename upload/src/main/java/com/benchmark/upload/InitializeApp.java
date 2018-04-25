package com.benchmark.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class InitializeApp implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
//        ProcessBuilder builder = new ProcessBuilder(
//                "/bin/bash", "-c", "chmod +x etc/nginx" +
//                "-v quiet -show_format -show_streams etc/nginx/" + path + ".mp4");
//
//        builder.redirectErrorStream(true);
//        Process p = null;
//        try {
//            p = builder.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
