# TICKET A5 — GAME LOOP: FIXED-TIMESTEP AUTHORITATIVE TICK + BROADCAST
PHASE: A
DEPENDS ON: A4
(Transport-agnostic — same concept as the raw project's game-loop ticket.)

## GOAL
  A single dedicated game-loop thread that simulates the world at a fixed rate and
  broadcasts a snapshot to all sessions each tick.

## WHAT TO DO — each tick (start 20 Hz = 50ms, or 30 Hz ~= 33ms)
  1. Drain queued client inputs.
  2. Advance world state (move each player by input * dt).
  3. Serialize a world snapshot (state message).
  4. Broadcast it to all sessions (reuse A3's broadcast).
  - Use an accumulator so the sim steps in fixed dt regardless of timing jitter;
    sleep the remainder.
  - Start the loop from an ApplicationRunner / @PostConstruct, or a
    ScheduledExecutorService at fixed rate.

## CONCEPTS THIS TEACHES
  - Fixed-timestep simulation; decoupling sim rate from message I/O
  - The accumulator pattern; the "spiral of death" (cap steps per frame)
  - Server authority over world state

## GOTCHAS
  - Handler threads ENQUEUE inputs; the loop thread DRAINS them — use a
    thread-safe queue (ConcurrentLinkedQueue). Don't mutate world state from
    handler threads; funnel all mutations through the loop thread.
  - Broadcasting from the loop thread while handlers also send -> mind the
    per-session send safety from A3.
  - Thread.sleep is imprecise — compute the next deadline from elapsed time.

## DEFINITION OF DONE
  - With nobody moving, ~20-30 snapshots/sec go out (log tick/sec).
  - A queued input visibly moves that player next snapshot.

I WRITE THIS MYSELF.

## RESOURCES
  - Glenn Fiedler: Fix Your Timestep!  https://gafferongames.com/post/fix_your_timestep/
  - The Cherno (video): Timesteps and Delta Time
    https://www.youtube.com/watch?v=pctGOMDW-HQ
