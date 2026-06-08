package org.example.bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Base WebSocket bot client that connects to the Arena server and acts as an automated player.
 *
 * Lifecycle:
 *   1. connect(url)  — opens a WebSocket to the server
 *   2. On open       — sends a "join" message with the bot's name
 *   3. On message    — parses "welcome" (stores bot's ID) and "state" (updates all player positions)
 *   4. think()       — called at ~10Hz by a scheduler; subclasses override this to implement AI behavior
 *   5. disconnect()  — shuts down the scheduler and closes the connection
 *
 * To create a bot with custom behavior:
 *   - Extend this class
 *   - Override think() with your AI logic
 *   - Use getMyState() to find this bot's position
 *   - Use getPlayers() to see all players
 *   - Call sendInput(up, down, left, right) to move
 *
 * Example:
 *   public class ChaserBot extends BotClient {
 *       public ChaserBot(String name) { super(name); }
 *
 *       @Override
 *       protected void think() {
 *           // find nearest player, move toward them
 *           sendInput(up, down, left, right);
 *       }
 *   }
 */
public class BotClient extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(BotClient.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String name;

    private WebSocketSession session;
    private String myId;
    private List<PlayerState> players = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public BotClient(String name) {
        this.name = name;
    }

    /**
     * Connects to the Arena server at the given WebSocket URL.
     * Once connected, afterConnectionEstablished sends the join message automatically.
     */
    public void connect(String serverUrl) throws Exception {
        StandardWebSocketClient client = new StandardWebSocketClient();
        session = client.execute(this, new WebSocketHttpHeaders(), URI.create(serverUrl)).get();
    }

    /**
     * Called by Spring when the WebSocket connection opens.
     * Sends a join message and starts the AI decision loop at ~10Hz.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        String joinJson = objectMapper.writeValueAsString(new JoinMsg("join", name));
        session.sendMessage(new TextMessage(joinJson));
        logger.info("Bot '{}' connected and sent join", name);

        scheduler.scheduleAtFixedRate(this::think, getThinkIntervalMs(), getThinkIntervalMs(), TimeUnit.MILLISECONDS);
    }

    /**
     * Called by Spring when a message arrives from the server.
     * Handles "welcome" (stores this bot's ID) and "state" (updates player positions).
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode node = objectMapper.readTree(message.getPayload());
        String type = node.get("type").asText();

        if ("welcome".equals(type)) {
            myId = node.get("yourId").asText();
            logger.info("Bot '{}' received id: {}", name, myId);

        } else if ("state".equals(type)) {
            List<PlayerState> updated = new ArrayList<>();
            for (JsonNode p : node.get("players")) {
                updated.add(new PlayerState(
                        p.get("id").asText(),
                        p.get("x").asInt(),
                        p.get("y").asInt(),
                        p.get("color").asText()
                ));
            }
            this.players = updated;
        }
    }

    /**
     * AI decision method — override in subclasses to define bot behavior.
     * Called ~10 times per second by the scheduler.
     *
     * Available helpers:
     *   getMyId()    — this bot's player ID
     *   getMyState() — this bot's current position/color, or null if not yet in state
     *   getPlayers() — list of all players (each has id, x, y, color)
     *   sendInput(up, down, left, right) — send movement input to the server
     */
    protected void think() {
        // Override in subclasses
    }

    /** Override to change how often think() is called. Default 100ms (10Hz). */
    protected long getThinkIntervalMs() {
        return 100;
    }

    /**
     * Sends an input message to the server with the given directional booleans.
     */
    protected void sendInput(boolean up, boolean down, boolean left, boolean right) {
        try {
            if (session != null && session.isOpen()) {
                String json = objectMapper.writeValueAsString(new InputMsg("input", up, down, left, right));
                session.sendMessage(new TextMessage(json));
            }
        } catch (Exception e) {
            logger.error("Bot '{}' failed to send input", name, e);
        }
    }

    /** Returns this bot's current state from the latest snapshot, or null if not found. */
    protected PlayerState getMyState() {
        for (PlayerState p : players) {
            if (p.id().equals(myId)) return p;
        }
        return null;
    }

    /** Returns the list of all players from the latest state snapshot. */
    protected List<PlayerState> getPlayers() {
        return players;
    }

    /** Returns this bot's player ID (assigned by the server on join). */
    protected String getMyId() {
        return myId;
    }

    /** Shuts down the AI scheduler and closes the WebSocket connection. */
    public void disconnect() throws Exception {
        scheduler.shutdown();
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    // Simple DTOs for sending messages
    private record JoinMsg(String type, String name) {}
    private record InputMsg(String type, boolean up, boolean down, boolean left, boolean right) {}

    /** Represents a player's state as received from the server's state broadcast. */
    public record PlayerState(String id, int x, int y, String color) {}

    /** Entry point — spawns one ChaseBot and one FleeBot that connect to the server. */
    public static void main(String[] args) throws Exception {
        String url = "ws://localhost:8080/ws";

        ChaseBot chaser = new ChaseBot("Hunter");
        chaser.connect(url);
        logger.info("Started ChaseBot 'Hunter'");

        FleeBot runner = new FleeBot("Runner");
        runner.connect(url);
        logger.info("Started FleeBot 'Runner'");
    }
}
