# TICKET A6 — BROWSER CLIENT: NATIVE WEBSOCKET + CANVAS (GAME COMPLETE)
PHASE: A — HEADLINE DELIVERABLE
DEPENDS ON: A5

## GOAL
  A tiny HTML page: connect, send input, render all players on a canvas.
  Two tabs -> two dots -> each sees the other move live.

## WHAT TO DO (conceptually)
  - new WebSocket("ws://localhost:8080/ws"); on open -> send join.
  - on message -> JSON.parse, store the latest state snapshot.
  - keydown/keyup (arrows/WASD) -> send input messages.
  - requestAnimationFrame loop -> clear <canvas>, draw each player as a circle at
    (x,y) with its color.
  - DUMB CLIENT: all game logic stays on the server; client sends input + draws.

## CONCEPTS THIS TEACHES
  - Native WebSocket API (onopen/onmessage/onclose/onerror, send())
  - Canvas 2D + requestAnimationFrame
  - Decoupling render rate (~60fps) from snapshot rate (20-30 Hz)

## GOTCHAS
  - rAF ~60fps vs snapshots 20-30 Hz -> motion looks steppy without smoothing.
    Snapping is OK for now (hook for C1 interpolation).
  - send() is async + buffers (bufferedAmount) — send input on change / fixed
    rate, don't flood.
  - Serve the page from somewhere (a Spring static resource under
    src/main/resources/static works) so it's same-origin with /ws.

## DEFINITION OF DONE
  *** Two tabs, each a keyboard-driven dot, each seeing the other move live on a
      shared canvas. ***

AI MAY WRITE THIS CLIENT — it's a test harness, not the learning target. Let it
do the HTML/Canvas/JS so your focus stays on the server.

## RESOURCES
  - MDN: WebSocket API  https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API
  - MDN: Canvas tutorial https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API/Tutorial
  - victorzhou.com .io series (client render loop)
