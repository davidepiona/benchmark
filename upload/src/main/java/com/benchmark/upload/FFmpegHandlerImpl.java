package com.benchmark.upload;


import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFmpegHandlerImpl implements FFmpegHandler {

    private String path;
    private int width;
    private int height;
    private String ratio;
    private int duration;

    @Autowired
    private UploadProperties props;

    public FFmpegHandlerImpl(String path, String env) throws IOException {
        this.path = path;
        ProcessBuilder builder = null;
        if (env.equals("local")) {
//            builder= new ProcessBuilder(
//                    "cmd.exe", "/c", "C:\\Users\\Davide\\ffmpeg-3.4.2-win64-static\\bin\\ffprobe " +
//                    "-v quiet -show_format -show_streams C:\\Users\\Davide\\IdeaProjects\\benchmark\\media\\"+path+".mp4");
            builder = new ProcessBuilder(
                    "/bin/bash", "-c", "/home/davide/dev/benchmark/upload/ffprobe " +
                    "-v quiet -show_format -show_streams /home/davide/dev/benchmark/media/" + path + ".mp4");
        } else if (env.equals("docker")) {
            builder = new ProcessBuilder(
                    "/bin/bash", "-c", "/opt/ffprobe " +
                    "-v quiet -show_format -show_streams /opt/media/" + path + ".mp4");
        } else {
            builder = new ProcessBuilder(
                    "/bin/bash", "-c", "/opt/ffprobe " +
                    "-v quiet -show_format -show_streams /opt/media/" + path + ".mp4");
        }

        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader r = null;
        if (p != null) {
            r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        }
        String line = "";
        int read = 0;
        while (read < 4) {
            try {
                if (r != null) {
                    line = r.readLine();
                }
                String[] split = line.split("=");
                if (split[0].equals("coded_width")) {
                    this.width = Integer.parseInt(split[1]);
//                    System.out.println("TROVATO!!! -->"+this.width);
                    read++;
                }
                if (split[0].equals("coded_height")) {
                    this.height = Integer.parseInt(split[1]);
//                    System.out.println("TROVATO!!! -->"+this.height);
                    read++;
                }
                if (split[0].equals("display_aspect_ratio")) {
                    this.ratio = split[1];
//                    System.out.println("TROVATO!!! -->"+this.ratio);
                    read++;
                }
                if (split[0].equals("duration")) {
                    this.duration = Integer.parseInt(split[1].split("\\.")[0]);
//                    System.out.println("TROVATO!!! -->"+this.duration);
                    read++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) {
                break;
            }
//            System.out.println(line);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getRatio() {
        return ratio;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
