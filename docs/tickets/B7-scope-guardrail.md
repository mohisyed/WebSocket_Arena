# TICKET B7 — SCOPE GUARDRAIL: DO NOT CHASE FULL RFC 6455 / AUTOBAHN COMPLIANCE

**PHASE:** B (read + internalize; mostly a decision, not code)
**DEPENDS ON:** none (attitude for the whole project)

## THE POINT

The Autobahn test suite has 500+ cases (fragmentation, UTF-8 edge cases, reserved bits, every close-code, length-boundary fuzzing...). Production libs aim for 100%. YOU SHOULD NOT.

## WHAT TO DO

- For a single-page learning game with a browser client you control, implement only the HAPPY PATH: v13 handshake, single (unfragmented) text frames, the close handshake, ping/pong.
- SKIP: message fragmentation/continuation, permessage-deflate, subprotocol negotiation, exhaustive UTF-8 validation, obscure close codes — unless you hit a concrete need.

## GOTCHA

- The browser MAY eventually send a fragmented or larger frame. If you ignore fragmentation, at least be aware the continuation opcode 0x0 exists and handle it only if you actually observe it.
- Don't let "completeness" stall the project — the MVP doesn't need it.

## DEFINITION OF DONE

- A short written list: which RFC 6455 features you implemented vs deliberately skipped, and why ("controlled client, happy path only").

## RESOURCES

- [crossbario/autobahn-testsuite (to see the scope you're declining)](https://github.com/crossbario/autobahn-testsuite)
- [Pusher: WebSockets From Scratch (models happy-path scoping)](https://pusher.com/blog/websockets-from-scratch/)
