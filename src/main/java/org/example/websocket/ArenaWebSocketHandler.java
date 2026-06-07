package org.example.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ArenaWebSocketHandler extends TextWebSocketHandler {
    Map<String,WebSocketSession> webSocketRegistry = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ArenaWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        webSocketRegistry.putIfAbsent(session.getId(), session);
        logger.info("Connected");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
//        we need to
        for(Map.Entry<String,WebSocketSession> entry: webSocketRegistry.entrySet()){
            String key = entry.getKey();
            WebSocketSession tempSession = entry.getValue();
            TextMessage newMessage = new TextMessage((String) message.getPayload());
            tempSession.sendMessage(newMessage);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        webSocketRegistry.remove(session.getId(), session);
        logger.info("Closed");
    }


}
