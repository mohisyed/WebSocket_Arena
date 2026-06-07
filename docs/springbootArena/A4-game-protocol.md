# TICKET A4 — GAME MESSAGE PROTOCOL (JSON)
PHASE: A
DEPENDS ON: A3
(Transport-agnostic — identical to the raw project's protocol ticket.)

## GOAL
  Design the application messages carried inside WebSocket text frames.

## WHAT TO DO — minimum set
  client -> server:
    join  { type:"join",  name }
    input { type:"input", up, down, left, right }   (or a movement vector)
  server -> client:
    welcome { type:"welcome", yourId }
    state   { type:"state", players:[ {id,x,y,color} ] }
  - Use JSON (Jackson is already on the classpath). Map to small DTO classes or
    parse to a tree — your choice.
  - Include a "type" discriminator on every message (versioning + routing).
  - SERVER IS AUTHORITATIVE: clients send INTENT (inputs), never positions.

## CONCEPTS THIS TEACHES
  - Two layers: transport (WebSocket) vs application protocol (your messages)
  - Server authority; JSON vs binary tradeoffs (JSON is fine here)

## GOTCHAS
  - Validate/clamp inputs server-side. Never trust the client.
  - JSON snapshots of N players at 30 Hz are heavy — fine for the MVP; it's the
    motivation for lighter updates in Phase C.

## DEFINITION OF DONE
  - Documented schema; server parses join/input and emits welcome/state; you can
    watch the JSON in DevTools > Network > WS frames.

I WRITE THIS MYSELF. (AI may generate trivial DTO getters.)

## RESOURCES
  - victorzhou.com: Build a Multiplayer (.io) Web Game
    https://victorzhou.com/blog/build-an-io-game-part-1/
