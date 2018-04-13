package com.benchmark.upload;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFmpegHandlerImpl implements FFmpegHandler{

    private String path;
    private String width;
    private String height;
    private String ratio;
    private String duration;

    public FFmpegHandlerImpl(String path) {
        this.path = path;
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "C:\\Users\\Davide\\ffmpeg-3.4.2-win64-static\\bin\\ffprobe " +
                "-v quiet -show_format -show_streams C:\\Users\\Davide\\IdeaProjects\\benchmark\\media\\"+path+".mp4");

        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line="";
        int read =0;
        while (read<4) {
            try {
                line = r.readLine();
                String[] split = line.split("=");
                if(split[0].equals("coded_width")){
                    this.width=split[1];
//                    System.out.println("TROVATO!!! -->"+this.width);
                    read++;
                }
                if(split[0].equals("coded_height")){
                    this.height=split[1];
//                    System.out.println("TROVATO!!! -->"+this.height);
                    read++;
                }
                if(split[0].equals("display_aspect_ratio")){
                    this.ratio=split[1];
//                    System.out.println("TROVATO!!! -->"+this.ratio);
                    read++;
                }
                if(split[0].equals("duration")){
                    this.duration=split[1];
//                    System.out.println("TROVATO!!! -->"+this.duration);
                    read++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) { break; }
//            System.out.println(line);
        }
    }

    @Override
    public String getWidth() {
        return width;
    }

    @Override
    public String getHeight() {
        return height;
    }

    @Override
    public String getRatio() {
        return ratio;
    }

    @Override
    public String getDuration() {
        return duration;
    }
}
