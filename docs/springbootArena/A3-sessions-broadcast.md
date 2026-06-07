# TICKET A3 — TRACK SESSIONS + BROADCAST TO ALL CLIENTS
PHASE: A
DEPENDS ON: A2

## GOAL
  Keep a registry of connected sessions and broadcast a message to all of them.
  Prove it as a tiny chat: one client's message reaches every client.

## WHAT TO DO
  - Maintain a thread-safe collection of WebSocketSession (e.g. a
    ConcurrentHashMap<String, WebSocketSession> keyed by session id, or a
    Collections.newSetFromMap / CopyOnWriteArraySet).
  - Add on connect, remove on close.
  - broadcast(text): iterate the registry, call session.sendMessage(...) on each.

## CONCEPTS THIS TEACHES
  - Managing many concurrent sessions (the Spring analog of the raw "client
    registry" ticket)
  - Thread-safety: Spring may call your handler on different threads; a single
    WebSocketSession is NOT safe for concurrent sends.

## GOTCHAS
  - WebSocketSession.sendMessage is NOT thread-safe for concurrent calls on the
    same session. When you later broadcast from a game-loop thread AND handle
    inbound messages, guard each session's sends (synchronize per session, or
    wrap with ConcurrentWebSocketSessionDecorator).
  - Remove dead/closed sessions or broadcasts will throw on them.

## DEFINITION OF DONE
  - Three browser tabs; a message from one appears in all three; closing one tab
    removes it cleanly.

I WRITE THIS MYSELF.

## RESOURCES
  - Spring docs: WebSocket support (sessions)
    https://docs.spring.io/spring-framework/reference/web/websocket.html
  - Oracle: Concurrent Collections
    https://docs.oracle.com/javase/tutorial/essential/concurrency/collections.html
