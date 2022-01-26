/*
 * Changelog:
 *   Implemented bug pathing strategy to walk towards location
 *   with obstacles in the way
 */

package playerv2.p1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

strictfp class Pathing {

    /**
     * Any square with more than this amount of rubble is an obstacle.
     */
    private static final int ACCEPTABLE_RUBBLE = 25;

    /**
     * The direction that we are trying to use to go around the obstacle.
     * It is null if we are not trying to go around an obstacle.
     */
    private static Direction bugDirection = null;

    /**
     * Uses bug pathing strategy to move around obstacles towards target.
     */
    static void walkTowards(RobotController rc, MapLocation target) throws GameActionException {
        if (!rc.isMovementReady()) {
            // If our cooldown is too high, there is nothing we can do.
            return;
        }

        MapLocation me = rc.getLocation();
        if (me.equals(target)) {
            // We're already at our goal! Nothing to do.
            return;
        }

        Direction d = me.directionTo(target);
        if (rc.canMove(d) && !isObstacle(rc, d)) {
            // Easy case: can just move towards target
            rc.move(d);
            bugDirection = null;
        } else {
            // Hard case: obstacle in the way
            if (bugDirection == null) {
                // Set bug direction to optimal direction
                bugDirection = d;
            }

            // Keep trying every possible direction
            for (int i = 0; i < 8; i++) {
                if (rc.canMove(bugDirection) && !isObstacle(rc, bugDirection)) {
                    rc.move(bugDirection);
                    bugDirection = bugDirection.rotateLeft();
                    break;
                } else {
                    // If I can't move towards bug direction, then rotate right
                    bugDirection = bugDirection.rotateRight();
                }
            }
        }
    }

    /**
     * Checks if the square we reach by moving in direction d is an obstacle.
     */
    private static boolean isObstacle(RobotController rc, Direction d) throws GameActionException {
        MapLocation adjacentLocation = rc.getLocation().add(d);
        int rubbleOnLocation = rc.senseRubble(adjacentLocation);
        return rubbleOnLocation > ACCEPTABLE_RUBBLE;
    }

}
