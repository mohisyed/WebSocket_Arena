# TICKET B6 — ENABLE wss:// (TLS) — AND DO NOT HAND-ROLL TLS

**PHASE:** B
**DEPENDS ON:** 09 (do near the end / before any deployment)

## GOAL

Serve the game over `wss://` with a valid certificate.

## WHAT TO DO — STRONGLY PREFERRED: terminate TLS at nginx (reverse proxy)

- nginx handles `wss://` + certs; your Java server stays a plaintext `ws://` server on localhost.
- Free auto-renewing cert via Let's Encrypt / Certbot.
- Required nginx directives for WS proxying:
  ```nginx
  proxy_set_header Upgrade $http_upgrade;
  proxy_set_header Connection "upgrade";
  proxy_http_version 1.1;
  ```
  plus a raised `proxy_read_timeout` for long-lived game connections.
- Alternative (in-JVM): SSLServerSocket (blocking, simpler). AVOID hand-driving SSLEngine for non-blocking TLS unless you specifically want that pain.

## HARD RULE

**NEVER implement TLS or any crypto primitive yourself.** Correct TLS needs constant-time code, cert-chain + hostname validation, downgrade resistance — each a documented foot-gun. Use a vetted implementation; prefer the proxy.

## GOTCHAS

- nginx does NOT forward the hop-by-hop Upgrade/Connection headers by default — you must set them explicitly (above).
- nginx closes a proxied connection after 60s of silence by default — raise `proxy_read_timeout` or send periodic WebSocket pings.

## DEFINITION OF DONE

- Reachable over `wss://yourdomain` from an `https://` page with a valid cert; your Java server never touches TLS bytes.

## RESOURCES

- [nginx: WebSocket proxying](https://nginx.org/en/docs/http/websocket.html)
- [Certbot](https://certbot.eff.org/)
- [OWASP: Use Cryptography to Protect Data (don't roll your own)](https://top10proactive.owasp.org/the-top-10/c2-crypto/)
