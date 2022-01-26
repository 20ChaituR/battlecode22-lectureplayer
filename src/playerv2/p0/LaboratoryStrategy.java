package playerv2.p0;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

strictfp class LaboratoryStrategy {

    /**
     * Run a single turn for a Laboratory.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runLaboratory(RobotController rc) throws GameActionException {
        if (rc.getTeamLeadAmount(rc.getTeam()) > 5000 && rc.canTransmute()) {
            rc.transmute();
        }
    }

}
