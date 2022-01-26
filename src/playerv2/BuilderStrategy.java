/*
 * Changelog:
 *   Used Pathing for Builder movement
 */

package playerv2;

import battlecode.common.*;

strictfp class BuilderStrategy {

    static int turnsSinceWatchTower = 0;
    static int turnsSinceLaboratory = 0;

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
            Pathing.walkTowards(rc, targetLocation);

        // Otherwise, try to move randomly
        } else {
            int directionIndex = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
            Direction dir = RobotPlayer.directions[directionIndex];
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }

        // Try to build a laboratory every 150 turns
        turnsSinceLaboratory++;
        if (rc.getTeamLeadAmount(rc.getTeam()) > 10000 && turnsSinceLaboratory >= 150) {
            int directionIndex = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
            Direction dir = RobotPlayer.directions[directionIndex];

            if (rc.canBuildRobot(RobotType.LABORATORY, dir)) {
                rc.buildRobot(RobotType.LABORATORY, dir);
                turnsSinceLaboratory = 0;
            }
        }

        // Try to build a watchtower every 100 turns
        turnsSinceWatchTower++;
        if (rc.getTeamLeadAmount(rc.getTeam()) > 7000 && turnsSinceWatchTower >= 100) {
            int directionIndex = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
            Direction dir = RobotPlayer.directions[directionIndex];

            if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {
                rc.buildRobot(RobotType.WATCHTOWER, dir);
                turnsSinceWatchTower = 0;
            }
        }
    }

}
