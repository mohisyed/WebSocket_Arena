# TICKET 02 — HTTP UPGRADE HANDSHAKE (101 SWITCHING PROTOCOLS)

**PHASE:** MVP
**DEPENDS ON:** 01

## GOAL

When a browser connects, complete the WebSocket opening handshake so the browser's WebSocket onopen event fires. THIS IS YOUR FIRST WIN.

## WHAT TO DO

- The browser sends a normal HTTP/1.1 GET request with headers including:
  ```
  Upgrade: websocket
  Connection: Upgrade
  Sec-WebSocket-Key: <base64>
  Sec-WebSocket-Version: 13
  ```
- Read the request lines up to the blank line (`\r\n\r\n`).
- Extract Sec-WebSocket-Key.
- Compute the accept token:
  ```
  base64( SHA-1( <the exact key string> + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11" ) )
  ```
  The GUID is a fixed RFC 6455 constant. Concatenate it to the key string AS SENT — do NOT base64-decode the key first.
- Respond with:
  ```
  HTTP/1.1 101 Switching Protocols
  Upgrade: websocket
  Connection: Upgrade
  Sec-WebSocket-Accept: <accept token>
  ```
  ...followed by a blank line (`\r\n\r\n`).

## CONCEPTS THIS TEACHES

- Parsing HTTP off a raw byte stream; case-insensitive headers
- MessageDigest (SHA-1) + Base64 from the JDK
- What 101 Switching Protocols means
- Purpose of the magic GUID: a protocol-correctness check so non-WebSocket servers/proxies don't accidentally complete the handshake. It is NOT auth.

## GOTCHAS

- Headers are case-insensitive — lowercase before matching.
- Lines end in `\r\n`; the header block ends with a blank `\r\n\r\n`.
- No trailing whitespace when concatenating the key + GUID.
- The response also ends with a blank line.
- Get the hash wrong and the browser SILENTLY refuses — onopen never fires, no error. Check DevTools > Network > WS for the 101.

## DEFINITION OF DONE

- In a browser console: `new WebSocket("ws://localhost:8080")` fires onopen.
- DevTools > Network > WS shows "101 Switching Protocols", connected.

I WRITE THIS MYSELF. (This is load-bearing protocol logic — do not let the AI write the accept-token computation or the request parsing.)

## RESOURCES

- [RFC 6455 §1.3 (Opening Handshake) and §4.2.2 (Server's Opening Handshake)](https://www.rfc-editor.org/rfc/rfc6455)
- [MDN: Writing a WebSocket server in Java](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_a_WebSocket_server_in_Java)

## DAY-JOB TRANSFER

Session negotiation + handshake validation — like a sign-on/logon exchange and key-derivation step in payment / Tandem protocols.
