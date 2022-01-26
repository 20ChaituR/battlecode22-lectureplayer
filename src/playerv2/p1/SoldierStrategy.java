/*
 * Changelog:
 *   Targets enemy with lowest health and attacks them
 */

package playerv2.p1;

import battlecode.common.*;

public class SoldierStrategy {

    /**
     * Run a single turn for a Soldier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runSoldier(RobotController rc) throws GameActionException {
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        MapLocation me = rc.getLocation();

        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        if (enemies.length > 0) {
            // Try to attack closest enemy
            MapLocation toAttack = null;
            int minDistance = Integer.MAX_VALUE;

            for (RobotInfo enemy : enemies) {
                int distanceTo = me.distanceSquaredTo(enemy.location);
                if (distanceTo < minDistance) {
                    toAttack = enemy.location;
                    minDistance = distanceTo;
                }
            }

            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }

        // Go towards enemy with lowest health
        RobotInfo[] sensedEnemies = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, opponent);
        MapLocation targetLocation = null;
        int minDistance = Integer.MAX_VALUE;

        for (RobotInfo enemy : sensedEnemies) {
            int distanceTo = me.distanceSquaredTo(enemy.location);
            if (distanceTo < minDistance) {
                targetLocation = enemy.location;
                minDistance = distanceTo;
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
    }

}
