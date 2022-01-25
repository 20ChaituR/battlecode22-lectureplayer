/*
 * Changelog:
 *
 */

package playerv1.p4;

import battlecode.common.*;

strictfp class BuilderStrategy {

    /**
     * Run a single turn for a Builder.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runBuilder(RobotController rc) throws GameActionException {
        // Try to repair buildings around us.
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation repLocation = new MapLocation(me.x + dx, me.y + dy);
                while (rc.canRepair(repLocation)) {
                    rc.repair(repLocation);
                }
            }
        }

        // Go to closest damaged building
        RobotInfo[] robots = rc.senseNearbyRobots();
        MapLocation targetLocation = null;
        int minDistance = Integer.MAX_VALUE;

        for (RobotInfo robot : robots) {
            if (robot.getTeam().equals(rc.getTeam()) && robot.type.isBuilding() &&
                    robot.health < robot.type.getMaxHealth(robot.level)) {

                int distanceTo = me.distanceSquaredTo(robot.getLocation());
                if (distanceTo < minDistance) {
                    targetLocation = robot.getLocation();
                    minDistance = distanceTo;
                }
            }
        }

        // If we have a target location, move towards it
        if (targetLocation != null) {
            Direction toMove = me.directionTo(targetLocation);
            if (rc.canMove(toMove)) {
                rc.move(toMove);
                System.out.println("I moved to " + targetLocation);
            }

        // Otherwise, try to move randomly
        } else {

            int directionIndex = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
            Direction dir = RobotPlayer.directions[directionIndex];
            if (rc.canMove(dir)) {
                rc.move(dir);
                System.out.println("I moved randomly!");
            }
        }
    }

}
