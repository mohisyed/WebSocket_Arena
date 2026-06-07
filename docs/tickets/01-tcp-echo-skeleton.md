# TICKET 01 — TCP ECHO SKELETON

**PHASE:** MVP (do first)
**DEPENDS ON:** nothing

## GOAL

Stand up a raw TCP server on java.net.ServerSocket. Accept one connection, read bytes, print them, write bytes back. No WebSocket yet — just prove you can accept a TCP connection and move bytes both ways.

## WHAT TO DO

- Bind a ServerSocket to a port (use 8080+, not 80 — avoids privilege issues).
- accept() a connection (it blocks until a client connects).
- Read from the socket's InputStream, log what you got, echo it back via the OutputStream, and flush().
- Test with: `nc localhost 8080` (type text, see it echoed)

## CONCEPTS THIS TEACHES

- ServerSocket.accept(), Socket.getInputStream()/getOutputStream()
- Blocking I/O mental model
- The one-thread-per-connection scaffold you'll grow later
- Clean startup/shutdown

## GOTCHAS

- Set SO_REUSEADDR so rapid restarts during testing don't hit "address already in use."
- accept() BLOCKS — that's expected.
- Don't forget to flush() the output stream or you'll see nothing.

## DEFINITION OF DONE

- `nc localhost 8080`, type "hello", see "hello" echoed back.
- Server logs "client connected."

I WRITE THIS MYSELF. (AI may scaffold the Gradle/Maven project + .gitignore.)

## RESOURCES

- [Oracle Java Tutorials: All About Sockets / Writing the Server Side of a Socket](https://docs.oracle.com/javase/tutorial/networking/sockets/)
- [Jenkov: Java Networking: ServerSocket](https://jenkov.com/tutorials/java-networking/server-socket.html)

## DAY-JOB TRANSFER

Same accept-loop + per-connection handler shape as any socket server you'd see fronting an ISO 8583 / Tandem link.
