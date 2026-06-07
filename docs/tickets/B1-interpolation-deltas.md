# TICKET B1 — ENTITY INTERPOLATION, THEN DELTAS & INTEREST MANAGEMENT

**PHASE:** B (beyond MVP — only after you've PLAYED the MVP)
**DEPENDS ON:** 09

## GOAL

Fix jerky motion (client interpolation) and cut bandwidth (server deltas + interest management).

## WHAT TO DO

- **CLIENT entity interpolation:** buffer the last two snapshots and render other players ~100ms in the past, interpolating between them for smooth motion.
- **SERVER delta compression:** send only what changed vs a baseline the client acknowledged.
- **SERVER interest management:** only send a client the entities near it.

## CONCEPTS / GOTCHAS

- Interpolation adds deliberate latency. At 10 snap/s the practical min delay is ~150ms; raising snapshot rate shrinks it (30/s -> ~150ms with loss protection; 60/s -> ~85ms).
- Delta compression needs per-client acked-baseline tracking; a newly visible entity must be sent in full until acked.
- DON'T optimize bandwidth before you've measured it.

## DEFINITION OF DONE

- Other players move smoothly (not in 20 Hz steps).
- Snapshot bandwidth drops when little is changing; clients get only nearby entities.

## RESOURCES

- [Gambetta: Entity Interpolation](https://www.gabrielgambetta.com/entity-interpolation.html)
- [Fiedler: Snapshot Interpolation / Snapshot Compression](https://gafferongames.com/post/snapshot_interpolation/)
  - [Snapshot Compression](https://gafferongames.com/post/snapshot_compression/)
- [Valve: Source Multiplayer Networking](https://developer.valvesoftware.com/wiki/Source_Multiplayer_Networking)
