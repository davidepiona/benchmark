package com.example.benchmark.wsTest;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MySessionHandler extends StompSessionHandlerAdapter {

    private HashMap<String, Long> timerMap = new HashMap<>();

    public MySessionHandler() {
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

        log.info("New session: " + session.getSessionId());

    }


    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return WSResponse.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        WSResponse wsResponse= (WSResponse) payload;
        if(wsResponse.getSuccess()){
            System.out.println("Upload completed! Overall time spent: "+ stopTimer(wsResponse.getId())+ " ms");
        }
    }

    public void startTimer(String id){
        timerMap.put(id, System.currentTimeMillis());
    }

    public long stopTimer(String id){
        return System.currentTimeMillis() - timerMap.get(id);
    }
}