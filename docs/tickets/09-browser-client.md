# TICKET 09 — MINIMAL BROWSER CLIENT: NATIVE WEBSOCKET + CANVAS (MVP COMPLETE)

**PHASE:** MVP — HEADLINE DELIVERABLE
**DEPENDS ON:** 08

## GOAL

A tiny HTML page that connects, sends input, and renders all players on a canvas. Two tabs -> two dots -> each sees the other move live.

## WHAT TO DO (conceptually)

- `new WebSocket("ws://localhost:8080")`; on open, send join.
- on message: parse the state snapshot, store it.
- Capture arrow/WASD key events; send input messages.
- `requestAnimationFrame` render loop: clear the `<canvas>`, draw each player as a circle/square at (x,y) with its color.
- DUMB CLIENT: all game logic stays on the server. The client only sends input and draws what it's told.

## CONCEPTS THIS TEACHES

- Native WebSocket API (onopen/onmessage/onclose/onerror, send())
- Canvas 2D; requestAnimationFrame
- Decoupling render rate (~60fps) from snapshot rate (20-30 Hz)

## GOTCHAS

- rAF runs ~60fps but snapshots arrive at 20-30 Hz — without smoothing motion looks jerky. Snapping is acceptable for the MVP (this is the hook for B1 interpolation later).
- `WebSocket.send` is async and buffers (`bufferedAmount`) — send input on change or at a fixed input rate, don't spam.
- https pages require `wss://` (mixed content) — fine on localhost over `ws://`.

## DEFINITION OF DONE

**Two browser tabs each show a dot you drive with the keyboard, and each sees the other dot move in real time on a shared canvas.**

THE AI MAY WRITE THIS CLIENT. It's a test harness, not the learning target — let the AI scaffold the HTML/Canvas/JS so your attention stays on the server.

## RESOURCES

- [MDN: WebSocket API](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API)
- [MDN: Canvas API tutorial](https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API/Tutorial)
- [victorzhou.com .io series (client render loop)](https://victorzhou.com/blog/build-an-io-game-part-1/)
