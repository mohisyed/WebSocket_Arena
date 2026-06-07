# TICKET C2 — CLIENT-SIDE PREDICTION + SERVER RECONCILIATION
PHASE: C (optional)
DEPENDS ON: C1

## GOAL
  Make YOUR own dot respond instantly while the server stays authoritative.

## WHAT TO DO
  - Prediction: client applies its own input immediately (don't wait for the
    round trip).
  - Reconciliation: tag each input with a sequence number; on each authoritative
    snapshot, re-apply inputs the server hasn't acknowledged yet to correct drift.

## CONCEPTS / GOTCHAS
  - Movement logic must be identical (deterministic) on client and server.
  - This is the difference between "feels laggy" and "feels instant."

## DEFINITION OF DONE
  - Your dot responds with zero perceived delay, yet snaps to server truth under
    induced latency. You can explain prediction vs reconciliation vs interpolation.

I WRITE THIS MYSELF.

## RESOURCES
  - Gambetta: Client-Side Prediction and Server Reconciliation (+ live demo)
    https://www.gabrielgambetta.com/client-side-prediction-server-reconciliation.html
