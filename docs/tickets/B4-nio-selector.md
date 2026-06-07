# TICKET B4 — REWRITE I/O AS A SINGLE-THREADED NIO SELECTOR / EVENT LOOP

**PHASE:** B (advanced / OPTIONAL — do it to learn the reactor model)
**DEPENDS ON:** 09 (independent of B1-B3)

## GOAL

Re-implement the server's I/O with java.nio: a non-blocking ServerSocketChannel + SocketChannels registered with a Selector, driven by one event-loop thread.

## WHAT TO DO

- Process OP_ACCEPT / OP_READ / OP_WRITE readiness in a `select()` loop.
- Handle partial reads/writes EXPLICITLY: a per-connection "message reader" that accumulates bytes until a full frame is present; per-connection write buffers/queues.

## CONCEPTS / GOTCHAS

- Readiness-based non-blocking I/O; the reactor pattern (how Netty / nginx / Node.js / Redis work under the hood).
- Non-blocking `read()` can return 0; `write()` may not write everything — buffer the remainder and re-arm OP_WRITE (backpressure).
- Store partial-frame state PER connection — a frame can span many reads.
- This is meaningfully harder than thread-per-connection. With Java 21 virtual threads, the blocking model is now a strong default — so do NIO to LEARN the reactor, not because the MVP needs it.

## DEFINITION OF DONE

- The full game runs on a single I/O thread (or a small fixed set), no thread-per-connection; you can explain why high-connection servers historically used this and how virtual threads change the calculus.

## RESOURCES

- [Jenkov: Java NIO Selector / Non-blocking Server](https://jenkov.com/tutorials/java-nio/selectors.html)
  - [Non-blocking Server](https://jenkov.com/tutorials/java-nio/non-blocking-server.html)
  - [GitHub: jjenkov/java-nio-server](https://github.com/jjenkov/java-nio-server)
