# TICKET A2 — ADD A SPRING WEBSOCKET ENDPOINT (CONNECT / RECEIVE / ECHO)
PHASE: A
DEPENDS ON: A1
*** FIRST WEBSOCKET WIN ***

## GOAL
  Register a WebSocket endpoint at /ws and echo back any text message a client
  sends. Spring handles the RFC 6455 handshake/framing for you.

## WHAT TO DO
  - Add a class extending TextWebSocketHandler (Spring's raw handler — NOT STOMP).
  - Override the lifecycle methods:
      afterConnectionEstablished(session)  -> log "connected"
      handleTextMessage(session, message)  -> echo message.getPayload() back via
                                              session.sendMessage(new TextMessage(...))
      afterConnectionClosed(session, status)-> log "closed"
  - Register it with a WebSocketConfigurer (@EnableWebSocket), mapping the handler
    to "/ws". (Allow your localhost origin.)
  - Test in browser console: new WebSocket("ws://localhost:8080/ws"); onopen fires;
    ws.send("hi") -> onmessage receives "hi".

## CONCEPTS THIS TEACHES
  - WebSocketHandler lifecycle (the application-level analog of the raw tickets:
    connect / message / close)
  - WebSocketSession as the per-client handle (this is what Spring gives you
    instead of a raw Socket)
  - Why the handshake/framing are invisible here (Spring did Tickets 02-05 for you)

## GOTCHAS
  - You almost always need to allow the request origin (setAllowedOrigins) or the
    browser handshake fails silently — check the Network > WS tab for the 101.
  - TextWebSocketHandler gives you whole decoded messages — no partial reads to
    handle. That convenience is exactly what you'd hand-roll in the raw project.

## DEFINITION OF DONE
  - onopen fires; a sent string echoes back; connect/close log on the server.

I WRITE THE HANDLER MYSELF. (AI may write the config/registration boilerplate.)

## RESOURCES
  - Spring docs: WebSocket support (the "WebSocketHandler" section)
    https://docs.spring.io/spring-framework/reference/web/websocket.html
