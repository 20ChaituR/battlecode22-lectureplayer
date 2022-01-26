/*
 * Changelog:
 *   Used Pathing for Miner movement
 */

package playerv2.p0;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.nio.file.Path;

strictfp class MinerStrategy {

    private static Direction exploreDir = null;

    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runMiner(RobotController rc) throws GameActionException {
        if (exploreDir == null) {
            RobotPlayer.rng.setSeed(rc.getID());
            int directionIndex = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
            exploreDir = RobotPlayer.directions[directionIndex];
        }
        rc.setIndicatorString(exploreDir.toString());

        // Try to mine on squares around us.
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                }
                // Lead can regrow! So only mine lead down to 1.
                while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation) > 1) {
                    rc.mineLead(mineLocation);
                }
            }
        }

        int visionRadius = rc.getType().visionRadiusSquared;
        MapLocation[] nearbyLocations = rc.getAllLocationsWithinRadiusSquared(me, visionRadius);

        MapLocation targetLocation = null;
        int minDistance = Integer.MAX_VALUE;

        // For each nearby location
        for (MapLocation tryLocation : nearbyLocations) {
            // Is there any resources there?
            if (rc.senseLead(tryLocation) > 1 || rc.senseGold(tryLocation) > 0) {
                // Yes! We should consider going here.
                int distanceTo = me.distanceSquaredTo(tryLocation);
                if (distanceTo < minDistance) {
                    targetLocation = tryLocation;
                    minDistance = distanceTo;
                }
            }
        }

        // If we have a target location, move towards it.
        if (targetLocation != null) {
            Pathing.walkTowards(rc, targetLocation);

        // Otherwise, move in the explore direction.
        } else {
            if (rc.canMove(exploreDir)) {
                rc.move(exploreDir);

            // Bounce off walls
            } else if (!rc.onTheMap(me.add(exploreDir))) {
                exploreDir = exploreDir.opposite();
                if (rc.canMove(exploreDir)) {
                    rc.move(exploreDir);
                }
            }

            // Also try to move randomly
            int directionIndex = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
            Direction dir = RobotPlayer.directions[directionIndex];
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }
    }

}
