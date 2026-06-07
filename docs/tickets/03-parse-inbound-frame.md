# TICKET 03 — READ EXACTLY N BYTES & PARSE ONE INBOUND (MASKED) FRAME

**PHASE:** MVP
**DEPENDS ON:** 02
***THIS IS THE CORE LEARNING TICKET — budget the most time here.***

## GOAL

After the handshake the browser sends WebSocket FRAMES, not HTTP. Parse one text frame and print its decoded payload.

## WHAT TO DO (frame layout, RFC 6455 §5.2)

- Byte 0: FIN (bit 7) + RSV1-3 (bits 6-4, must be 0) + opcode (bits 3-0)
- Byte 1: MASK bit (bit 7) + 7-bit payload length
- Payload length:
  - 0-125 => that's the length
  - 126 => next 2 bytes are a 16-bit unsigned length
  - 127 => next 8 bytes are a 64-bit length (top bit must be 0)
- Client->server frames are ALWAYS masked: a 4-byte masking key follows the length. Recover plaintext by XOR: `payload[i] ^ maskKey[i % 4]`.
- Decode opcode 0x1 (text) payload as UTF-8 and print it.

## CONCEPTS THIS TEACHES

- Bit masking/shifting in Java; SIGN EXTENSION (byte is signed — use `& 0xFF`)
- Variable-length integer encoding
- A read-fully helper that LOOPS until N bytes are read, because `InputStream.read()` may return fewer bytes than asked, or split a frame across TCP segments.

## GOTCHAS

- **#1 burn:** TCP is a byte STREAM, not a message stream. One `read()` may give you half a frame, one frame, or 2.5 frames. Read the 2-byte minimum header, figure out how many more bytes you need, then read EXACTLY that many.
- Java signed bytes: `(length & 0x7F)` and `(b & 0xFF)` are mandatory.
- Per §5.1 a server MUST close the connection on an UNMASKED client frame.

## DEFINITION OF DONE

- Browser runs `ws.send("hello")`; server prints: `hello`
- Send a string > 125 chars; confirm the 16-bit length path works.

I WRITE THIS MYSELF. (The parser, the unmask loop, and the read-exactly-N-bytes helper are the whole point of the project. Do NOT let the AI write them. AI may review my parser against a failing frame and explain what's wrong.)

## RESOURCES

- [RFC 6455 §5.2 (frame format) and §5.3 (masking)](https://www.rfc-editor.org/rfc/rfc6455)
- [Jenkov: Java NIO Non-blocking Server — the "Message Reader" section](https://jenkov.com/tutorials/java-nio/non-blocking-server.html)

## DAY-JOB TRANSFER

"Read the length prefix, then read exactly that many payload bytes, handling short reads" IS the ISO 8583-over-TCP loop. The masking XOR is the only new part; the framing discipline is identical.
