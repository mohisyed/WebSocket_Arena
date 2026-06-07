# TICKET A1 — SPRING BOOT PROJECT + HEALTH / REST ENDPOINT
PHASE: A (do first)
DEPENDS ON: nothing

## GOAL
  Stand up a Spring Boot app with one plain REST endpoint. Prove the app runs and
  serves HTTP before any WebSocket work. This is your "API layer."

## WHAT TO DO
  - Generate a Spring Boot 3.x project (start.spring.io) with dependencies:
    Spring Web, Spring WebSocket. Java 21.
  - Add one REST controller with GET /health that returns a small JSON body
    (e.g. {"status":"ok"}).
  - Run it; hit http://localhost:8080/health in a browser or curl.

## CONCEPTS THIS TEACHES
  - Spring Boot project layout, the @SpringBootApplication entrypoint
  - @RestController / @GetMapping; how Spring serves HTTP on an embedded Tomcat
  - The mental split you'll see at work: REST = request/response control plane

## GOTCHAS
  - Default port is 8080. Note it; the browser WebSocket will target the same host.
  - Returning a POJO auto-serializes to JSON via Jackson — no manual JSON here.

## DEFINITION OF DONE
  - GET /health returns 200 with your JSON body.

AI MAY scaffold the project + write the trivial controller. (This part is not the
learning target — it's setup. Read what it generated so you know the layout.)

## RESOURCES
  - Spring Boot REST guide  https://spring.io/guides/gs/rest-service/
  - start.spring.io
