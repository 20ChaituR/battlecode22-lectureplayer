/*
 * Changelog:
 *   Archon now builds in direction of lowest rubble (instead of random direction)
 */

package playerv1.p3;

import battlecode.common.*;

import java.util.Arrays;
import java.util.Comparator;

strictfp class ArchonStrategy {

    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runArchon(RobotController rc) throws GameActionException {
        if (RobotPlayer.rng.nextBoolean()) {
            // Let's try to build a miner.
            rc.setIndicatorString("Trying to build a miner");
            buildTowardsLowRubble(rc, RobotType.MINER);
        } else {
            // Let's try to build a soldier.
            rc.setIndicatorString("Trying to build a soldier");
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        }
    }

    /**
     * Try to build robot of given type in each direction from lowest to highest rubble.
     */
    private static void buildTowardsLowRubble(RobotController rc, RobotType type) throws GameActionException {
        // Sort directions by rubble amount
        Direction[] dirs = Arrays.copyOf(RobotPlayer.directions, RobotPlayer.directions.length);
        Arrays.sort(dirs, Comparator.comparingInt(o -> getRubble(rc, o)));

        // Build in first possible location with lowest rubble
        for (Direction dir : dirs) {
            if (rc.canBuildRobot(type, dir)) {
                rc.buildRobot(type, dir);
            }
        }
    }

    /**
     * Gets the amount of rubble in given direction from robot.
     */
    private static int getRubble(RobotController rc, Direction dir) {
        try {
            MapLocation loc = rc.getLocation().add(dir);
            return rc.senseRubble(loc);
        } catch (GameActionException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
