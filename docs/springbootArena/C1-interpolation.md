# TICKET C1 — ENTITY INTERPOLATION (SMOOTH MOTION) + LIGHTER UPDATES
PHASE: C (optional — after you've PLAYED the game)
DEPENDS ON: A6

## GOAL
  Kill the steppy motion and trim bandwidth.

## WHAT TO DO
  - CLIENT interpolation: buffer the last two snapshots, render OTHER players
    ~100ms in the past, interpolating between the two for smooth movement.
  - (Optional) SERVER: send only changed fields (deltas) and/or only nearby
    players (interest management).

## CONCEPTS / GOTCHAS
  - Interpolation trades a little latency for smoothness; higher snapshot rate
    shrinks the needed buffer.
  - Don't optimize bandwidth before you measure it.

## DEFINITION OF DONE
  - Other dots glide instead of stepping at 20-30 Hz.

I WRITE THIS MYSELF.

## RESOURCES
  - Gambetta: Entity Interpolation
    https://www.gabrielgambetta.com/entity-interpolation.html
  - Fiedler: Snapshot Interpolation
    https://gafferongames.com/post/snapshot_interpolation/
