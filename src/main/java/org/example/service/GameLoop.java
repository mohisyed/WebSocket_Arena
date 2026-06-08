package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.outgoing.PlayerSnapshot;

import org.example.dto.outgoing.StateMessage;
import org.example.model.Player;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameLoop {
    private final Map<String, WebSocketSession> sessionRegistry = new ConcurrentHashMap<>();
    private final Map<String, Player> playerRegistry = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();


    public void addSession(String Id, WebSocketSession session){
        sessionRegistry.putIfAbsent(Id, session);
    }

    public void removeSession(String Id){
        sessionRegistry.remove(Id);
    }

    public void addPlayer(String id, Player player){
        playerRegistry.putIfAbsent(id,player);
    }

    public void removePlayer(String id){
        playerRegistry.remove(id);
    }

    public Map<String, Player> getPlayers() { return playerRegistry; }

    public Map<String, WebSocketSession> getSessions() { return sessionRegistry; }

    @Scheduled(fixedRate = 33)
    public void tick() throws IOException {
        List<PlayerSnapshot> snapShotRegistry = new ArrayList<>();
        for(Map.Entry<String,Player> entry: playerRegistry.entrySet()){
            String id = entry.getKey();
            Player tempPlayer = entry.getValue();
            int speed = 5;
            if (tempPlayer.isUp())    tempPlayer.setY(tempPlayer.getY() - speed);
            if (tempPlayer.isDown())  tempPlayer.setY(tempPlayer.getY() + speed);
            if (tempPlayer.isLeft())  tempPlayer.setX(tempPlayer.getX() - speed);
            if (tempPlayer.isRight()) tempPlayer.setX(tempPlayer.getX() + speed);

            // Bounce off arena walls (800x600 canvas, 15px radius)
            if (tempPlayer.getX() <= 15) {
                tempPlayer.setX(15);
                tempPlayer.setLeft(false);
                tempPlayer.setRight(true);
            } else if (tempPlayer.getX() >= 785) {
                tempPlayer.setX(785);
                tempPlayer.setRight(false);
                tempPlayer.setLeft(true);
            }
            if (tempPlayer.getY() <= 15) {
                tempPlayer.setY(15);
                tempPlayer.setUp(false);
                tempPlayer.setDown(true);
            } else if (tempPlayer.getY() >= 585) {
                tempPlayer.setY(585);
                tempPlayer.setDown(false);
                tempPlayer.setUp(true);
            }

            PlayerSnapshot snap = new PlayerSnapshot(tempPlayer.getId(), tempPlayer.getX(), tempPlayer.getY(), tempPlayer.getColor().name());
            snapShotRegistry.add(snap);
        }

        StateMessage updates = new StateMessage("state",snapShotRegistry);
        String jsonString = objectMapper.writeValueAsString(updates);

        for(Map.Entry<String,WebSocketSession> entry: sessionRegistry.entrySet()) {
            String id = entry.getKey();
            WebSocketSession session = entry.getValue();
            session.sendMessage(new TextMessage(jsonString));
        }
    }

}
