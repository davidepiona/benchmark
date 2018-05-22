package com.example.benchmark.wsTest;

import org.apache.commons.io.FileUtils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

@Component
public class WSManager {

    private static final int BUFFER_SIZE = 8192;
    private StompSession stompSession;
    private MySessionHandler sessionHandler;
    private TestProperties props;

    private StompSession.Subscription subscribe;

    /**
     * Constructor that create the session, establishes the connection and subscribes it
     */

    public WSManager(TestProperties props) {
        this.props = props;
        String url = this.props.getWs();    //String url = "ws://127.0.0.1:8020/events/websocket";
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        this.sessionHandler = new MySessionHandler();
        this.stompSession = null;
        try {
            stompSession = stompClient.connect(url, sessionHandler).get();
            System.out.println("Creata ws");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void subscribe() {
//        System.out.println("<--subscribe-->");
        this.subscribe = stompSession.subscribe("/topic/upload", sessionHandler);
    }

    public void unsubscribe() {
//        System.out.println("<--unsubscribe-->");
        subscribe.unsubscribe();
    }

    public void movieListUpload(MovieList movieList) {


        for (int i = 0; i < movieList.getMovieResources().size(); i++) {
            MovieInfo movieInfo = movieList.getMovieResources().get(i);
            uploadRequest(movieInfo, props.getPath());
        }
    }

    public void echoRequest() {

        stompSession.subscribe("/topic/echo", sessionHandler);
        stompSession.send("/app/echo", "{\"name\":\"Helloooo\"}".getBytes());
        stompSession.disconnect();
    }

    public void uploadRequest(MovieInfo movieInfo, String uploadPath) {

        final String id = movieInfo.getMovieId();
        final File f = new File(uploadPath);
        sessionHandler.startTimer(id);

        final String path = f.getAbsolutePath();
        byte[] buffer = new byte[BUFFER_SIZE];

        try (InputStream input = FileUtils.openInputStream(f)) {
            final long totalBytesCount = input.available();
            int partCount = Math.round(totalBytesCount / BUFFER_SIZE);
            System.out.println("partcount= " + partCount + " totalBytes= " + totalBytesCount);
            UploadRequest uploadRequest = null;

            int partId = 0;
            while (partId <= partCount) {
                final int readBytesCount = input.read(buffer, 0, BUFFER_SIZE);
                String s = Base64.getEncoder().encodeToString(buffer);
                uploadRequest = new UploadRequest(id, s, partId, partCount);
                stompSession.send("/app/upload", uploadRequest);
                partId++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

