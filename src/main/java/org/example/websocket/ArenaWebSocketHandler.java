package org.example.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class ArenaWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ArenaWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        logger.info("Connected");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        // creating the message object
        TextMessage newMessage = new TextMessage((String) message.getPayload());

        // sending message to the websocket
        session.sendMessage(newMessage);
        logger.info(String.valueOf(message));

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        logger.info("Closed");
    }
}
