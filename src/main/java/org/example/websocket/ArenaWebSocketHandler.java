package org.example.websocket;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.outgoing.WelcomeMessage;
import org.example.model.Player;
import org.example.model.PlayerColor;
import org.example.service.GameLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;


public class ArenaWebSocketHandler extends TextWebSocketHandler {

    // injected game loop service
    private final GameLoop gameLoop;

    // object mapper for jackson
    private final ObjectMapper objectMapper = new ObjectMapper();

    // logging solution
    private static final Logger logger = LoggerFactory.getLogger(ArenaWebSocketHandler.class);

    public ArenaWebSocketHandler(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        gameLoop.addSession(session.getId(), session);
        logger.info("Connected");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        // jsonNode is just the message payload
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());

        // we look for "type" (same as contains)
        String type = jsonNode.get("type").asText();

        // when new player joins
        // we need to create a new player then send a welcome message announcing they have joined and log it

        if (Objects.equals(type, "join")) {
            String playerSessionId = session.getId();
            String name = jsonNode.get("name").asText();
            PlayerColor color = PlayerColor.random();
            gameLoop.addPlayer(playerSessionId, new Player(playerSessionId, name, 100, 100, color));

            WelcomeMessage newPlayerWelcome = new WelcomeMessage("welcome", playerSessionId);
            String jsonString = objectMapper.writeValueAsString(newPlayerWelcome);
            session.sendMessage(new TextMessage(jsonString));

            logger.info(String.valueOf(jsonString));

        // if the player inputs some commands to move or such
        // we log and update the player object
        } else if (Objects.equals(type, "input")) {
            Player player = gameLoop.getPlayers().get(session.getId());
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
        gameLoop.removeSession(session.getId());
        gameLoop.removePlayer(session.getId());
        logger.info("Closed");
    }
}
