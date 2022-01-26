package playerv0.p1;

import battlecode.common.*;

strictfp class Archon {

    static void runArchon(RobotController rc) throws GameActionException {
        // Pick a direction to build in.
        int visionRadius = rc.getType().visionRadiusSquared;
        MapLocation me = rc.getLocation();
        MapLocation[] nearby = rc.getAllLocationsWithinRadiusSquared(me, visionRadius);
        MapLocation targetLoc = null;
        int distance = Integer.MAX_VALUE;
        for (MapLocation ml : nearby) {

            if (rc.senseLead(ml) > 0 || rc.senseGold(ml) > 0) {
                int thisDist = me.distanceSquaredTo(ml);
                if (thisDist < distance) {
                    targetLoc = ml;
                    distance = thisDist;
                }
            }
        }
        if (targetLoc != null) {
            Direction toBuild = me.directionTo(targetLoc);
            while (rc.canBuildRobot(RobotType.MINER, toBuild))
            {
                rc.buildRobot(RobotType.MINER, toBuild);
                System.out.println("Just build a miner");
            }
        }
        else
        {
            Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
            while (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                rc.buildRobot(RobotType.SOLDIER, dir);
                dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
            }
        }

    }
}
