package playerv0.p1;

import battlecode.common.*;


strictfp class Miner {
    static void runMiner(RobotController rc) throws GameActionException {
        // Try to mine on squares around us.
        System.out.println("rerun");
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
            Direction toMove = me.directionTo(targetLoc);
            while(!rc.canMineLead(targetLoc) && rc.senseLead(targetLoc) > 0) {
                if (rc.canMove(toMove)) {
                    System.out.println("moving to mine!");
                    rc.move(toMove);
                    toMove = me.directionTo(targetLoc);
                }
            }
            System.out.println("can mine now");

            while (rc.canMineLead(targetLoc))
            {
                rc.mineLead(targetLoc);
            }
            while (rc.canMineGold(targetLoc))
            {
                rc.mineGold(targetLoc);
            }
        }
        else//search for mines
        {
            Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
            while(!rc.canMove(dir))
            {
                dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
            }
            while(rc.canMove(dir))
            {
                rc.move(dir);
            }
            System.out.println("search for mines");


        }
    }
    //void dfs()
}