package org.example.websocket;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.outgoing.WelcomeMessage;
import org.example.model.Player;
import org.example.model.PlayerColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class ArenaWebSocketHandler extends TextWebSocketHandler {
    Map<String, WebSocketSession> webSocketRegistry = new ConcurrentHashMap<>();
    Map<String, Player> playerRegistry = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(ArenaWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketRegistry.putIfAbsent(session.getId(), session);
        logger.info("Connected");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        // jsonNode is just the message payload
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());

        // we look for "type" (same as contains)
        String Type = jsonNode.get("type").asText();

        // when new player joins
        // we need to create a new player then send a welcome message announcing they have joined and log it

        if (Objects.equals(Type, "join")) {
            String playerSessionId = session.getId();
            String name = jsonNode.get("name").asText();
            PlayerColor color = PlayerColor.random();
            playerRegistry.putIfAbsent(playerSessionId, new Player(playerSessionId, name, 100, 100, color));

            WelcomeMessage newPlayerWelcome = new WelcomeMessage("welcome",playerSessionId);
            String jsonString = objectMapper.writeValueAsString(newPlayerWelcome);
            session.sendMessage(new TextMessage(jsonString));

            logger.info(String.valueOf(jsonString));


        // if the player inputs some commands to move or such
        // we log and update the player object/
        } else if (Objects.equals(Type, "input")) {
            Player player = playerRegistry.get(session.getId());
            if (player != null) {
                player.setUp(jsonNode.get("up").asBoolean());
                player.setDown(jsonNode.get("down").asBoolean());
                player.setLeft(jsonNode.get("left").asBoolean());
                player.setRight(jsonNode.get("right").asBoolean());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketRegistry.remove(session.getId(), session);
        playerRegistry.remove(session.getId());
        logger.info("Closed");
    }


}
