# WEBSOCKET ARENA — SPRING-FIRST EDITION

## WHAT THIS IS
  A real-time multiplayer "dots on a shared canvas" arena game, built to LEARN
  WebSockets. This edition uses Spring Boot + Spring's WebSocket support so you
  get a playable game fast and learn the Spring patterns you'll also see at work.
  Browser is the client, using the native WebSocket API.

  This is PROJECT 1 (learn WebSockets, ship a game). A separate Tandem-flavored
  project comes LATER — do not try to make this game mirror the Tandem API.

## GOAL ORDERING (this edition)
  Phase A — Build the game on Spring WebSocket. Ship it. (Tickets A1-A6)
  Phase C — Make it feel good: interpolation, prediction, etc. (optional)
  (There is no raw-socket phase in THIS project. If you ever want to peel back
   the layer and hand-roll RFC 6455 yourself, that's the separate raw-tickets
   set — see ../../tickets/ — but that's explicitly a different project.)

## CONTRACT FOR ANY AI ASSISTANT (Claude Code) WORKING IN THIS REPO
I am learning WebSockets and Spring's real-time support by BUILDING this game.
The point is for ME to understand the moving parts, not to have it written for me.

DO NOT write, for me, the core learning pieces — I write these by hand:
  - The Spring WebSocket handler logic (handleMessage / afterConnectionEstablished
    / afterConnectionClosed) and how sessions are tracked
  - The game message protocol (the JSON message types and how they're parsed)
  - The game tick loop and authoritative state update
  - The broadcast logic (iterating sessions and sending)

YOU MAY (and should, to keep my focus on the learning parts):
  - Scaffold the Spring Boot project (Gradle/Maven, dependencies, app entrypoint)
  - Write the HTML/Canvas browser TEST client (it's a harness, not my target)
  - Write trivial boilerplate (config classes, DTO getters, a JSON helper, logging)
  - EXPLAIN concepts, and REVIEW / DEBUG code I have already written
  - QUIZ me before answering, when I ask

HOW TO HELP BEST
  - When I'm stuck, ask to see my actual code + the actual error/output, then walk
    me through what's wrong rather than pasting a finished version.
  - Restate this contract at the start of each session (you won't remember it).

## NOTE ON ABSTRACTION
  Spring WebSocket HIDES the RFC 6455 handshake, framing, and masking on purpose.
  That's fine for this project — the goal here is to understand WebSockets at the
  application level (sessions, messages, broadcast, lifecycle) and to ship a game.
  The byte-level protocol internals are a SEPARATE, later project, not this one.

## TICKET ORDER
  A1  Spring Boot project + health/REST endpoint (the API layer)
  A2  Add a Spring WebSocket endpoint (connect / receive / echo)
  A3  Track sessions + broadcast to all clients
  A4  Game message protocol (join / input / welcome / state, as JSON)
  A5  Game loop: fixed-timestep authoritative tick + snapshot broadcast
  A6  Browser client: native WebSocket + Canvas  <-- headline: two dots, live

  STOP HERE and actually play it.

  PHASE C (optional, after you've played it):
  C1  Entity interpolation (smooth motion) + lighter updates
  C2  Client-side prediction + server reconciliation

STACK: Java 21+, Spring Boot 3.x. Use Spring's raw WebSocketHandler (TextMessage)
rather than STOMP — it keeps you closer to "messages in/out" and is the better
mental model for a game. STOMP is a fine thing to learn later, but it adds a
pub/sub abstraction you don't need here.
