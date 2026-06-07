# TICKET B5 — LOBBY / MATCHMAKING VIA AN HTTP REST LAYER (SAME PROCESS)

**PHASE:** B
**DEPENDS ON:** 09
***THIS IS WHERE SPRING BOOT IS APPROPRIATE — do NOT hand-roll HTTP here.***

## GOAL

Add a small HTTP layer for create-room / list-rooms / join-room, running in the same JVM as your raw WebSocket game server.

## WHAT TO DO

- Expose REST endpoints: create room, list rooms, join room -> returns a WebSocket URL / room token.
- Run HTTP and the raw WebSocket server in ONE process (different ports, or route by path).
- Browser hits REST to pick a room, then opens `ws://` carrying the room token.

## CONCEPTS / GOTCHAS

- Separating control-plane (REST, request/response) from data-plane (WebSocket, streaming).
- THIS is where using Spring / an embedded HTTP server is correct — contrast with the WebSocket layer where hand-rolling was the point.
- Share the room registry safely between REST threads and the game thread.
- Decide how the WS upgrade reads the room token (path or a header you parse in Ticket 02).

## DEFINITION OF DONE

- Two players create/join the same named room via REST, then land in the same shared arena over WebSocket; a third player in another room is isolated.

## RESOURCES

- [Spring Boot REST guide](https://spring.io/guides/gs/rest-service/)
