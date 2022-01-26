package playerv1.p4;

import battlecode.common.*;

import java.util.Arrays;
import java.util.Comparator;

strictfp class ArchonStrategy {

    static int miners = 0, soldiers = 0, builders = 0;

    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runArchon(RobotController rc) throws GameActionException {
        if (miners < 5) {
            buildTowardsLowRubble(rc, RobotType.MINER);
        } else if (soldiers < 10) {
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        } else if (builders < 1) {
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        }

        else if (miners < soldiers / 2 && rc.getTeamLeadAmount(rc.getTeam()) < 5000) {
            buildTowardsLowRubble(rc, RobotType.MINER);
        } else if (builders < soldiers / 10) {
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        } else {
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
                switch (type) {
                    case MINER: miners++; break;
                    case SOLDIER: soldiers++; break;
                    case BUILDER: builders++; break;
                    default: break;
                }
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
