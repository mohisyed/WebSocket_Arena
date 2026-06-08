package org.example.bot;

/**
 * A bot that chases the nearest player.
 * Slows down drastically when very close (caught), returns to normal speed
 * when the target gets far enough away. This gives prey a chance to escape.
 */
public class ChaseBot extends BotClient {

    private static final int CAUGHT_DISTANCE = 50;
    private static final int RESUME_DISTANCE = 150;

    private volatile boolean stunned = false;
    private int tickCount = 0;

    public ChaseBot(String name) {
        super(name);
    }

    @Override
    protected long getThinkIntervalMs() { return 50; }

    @Override
    protected void think() {
        tickCount++;

        PlayerState me = getMyState();
        if (me == null) return;

        // Find nearest player that isn't me
        PlayerState target = null;
        double closestDist = Double.MAX_VALUE;

        for (PlayerState p : getPlayers()) {
            if (p.id().equals(getMyId())) continue;
            double dist = Math.hypot(p.x() - me.x(), p.y() - me.y());
            if (dist < closestDist) {
                closestDist = dist;
                target = p;
            }
        }

        if (target == null) return;

        // Update stun state
        if (closestDist < CAUGHT_DISTANCE) {
            stunned = true;
        } else if (closestDist > RESUME_DISTANCE) {
            stunned = false;
        }

        // When stunned, only act every 8th tick (~400ms). Normal is every 4th tick (~200ms).
        if (stunned && tickCount % 8 != 0) return;
        if (!stunned && tickCount % 4 != 0) return;

        // Move toward the target
        boolean up = target.y() < me.y();
        boolean down = target.y() > me.y();
        boolean left = target.x() < me.x();
        boolean right = target.x() > me.x();

        sendInput(up, down, left, right);
    }
}
