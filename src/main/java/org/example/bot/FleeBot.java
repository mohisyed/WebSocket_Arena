package org.example.bot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A bot that runs away from the nearest player.
 * Gets faster when a threat is close (within 100px) and returns to normal speed
 * when safe (over 200px away). When cornered near a wall, slides along it
 * instead of getting stuck.
 */
public class FleeBot extends BotClient {

    private static final int PANIC_DISTANCE = 100;
    private static final int SAFE_DISTANCE = 200;
    private static final int WALL_MARGIN = 30;

    private volatile boolean panicking = false;
    private int tickCount = 0;

    public FleeBot(String name) {
        super(name);
    }

    @Override
    protected long getThinkIntervalMs() { return 50; }

    @Override
    protected void think() {
        // When calm, only act every other tick (effectively 100ms).
        // When panicking, act every tick (50ms) for double speed.
        tickCount++;
        if (!panicking && tickCount % 2 != 0) return;

        PlayerState me = getMyState();
        if (me == null) return;

        // Find nearest player that isn't me
        PlayerState threat = null;
        double closestDist = Double.MAX_VALUE;

        for (PlayerState p : getPlayers()) {
            if (p.id().equals(getMyId())) continue;
            double dist = Math.hypot(p.x() - me.x(), p.y() - me.y());
            if (dist < closestDist) {
                closestDist = dist;
                threat = p;
            }
        }

        if (threat == null) return;

        // Update panic state
        if (closestDist < PANIC_DISTANCE) {
            panicking = true;
        } else if (closestDist > SAFE_DISTANCE) {
            panicking = false;
        }

        // Base flee direction (away from threat)
        boolean up = threat.y() > me.y();
        boolean down = threat.y() < me.y();
        boolean left = threat.x() > me.x();
        boolean right = threat.x() < me.x();

        // Wall avoidance — if near a wall, slide along it instead of into it
        boolean nearLeft = me.x() <= WALL_MARGIN;
        boolean nearRight = me.x() >= 800 - WALL_MARGIN;
        boolean nearTop = me.y() <= WALL_MARGIN;
        boolean nearBottom = me.y() >= 600 - WALL_MARGIN;

        if (nearLeft && left) {
            left = false;
            // Slide vertically away from threat
            up = threat.y() > me.y();
            down = threat.y() < me.y();
        }
        if (nearRight && right) {
            right = false;
            up = threat.y() > me.y();
            down = threat.y() < me.y();
        }
        if (nearTop && up) {
            up = false;
            left = threat.x() > me.x();
            right = threat.x() < me.x();
        }
        if (nearBottom && down) {
            down = false;
            left = threat.x() > me.x();
            right = threat.x() < me.x();
        }

        sendInput(up, down, left, right);
    }
}
