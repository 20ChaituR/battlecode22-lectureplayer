package submittedPlayer;

import battlecode.common.*;

public class Soldier {
    static Direction general_enemy_dir = null;
    static Direction moving_direction = null;
    static void runSoldier(RobotController rc) throws GameActionException {
        // Try to attack someone

        MapLocation me = rc.getLocation();
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        if (enemies.length > 0) {
            general_enemy_dir = me.directionTo(enemies[0].location);
            MapLocation toAttack = enemies[0].location;
            while (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        }

        // Also try to move randomly.
        if(general_enemy_dir == null)
        {

            moving_direction = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];

            while(!rc.canMove(moving_direction))
            {
                moving_direction = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
            }
            while(rc.canMove(moving_direction))
            {
                rc.move(moving_direction);
                enemies = rc.senseNearbyRobots(radius, opponent);
                if (enemies.length > 0) {
                    general_enemy_dir = me.directionTo(enemies[0].location);
                    MapLocation toAttack = enemies[0].location;
                    while (rc.canAttack(toAttack)) {
                        rc.attack(toAttack);
                    }
                }
            }
        }
        else {
            while (rc.canMove(general_enemy_dir)) {
                rc.move(general_enemy_dir);
                enemies = rc.senseNearbyRobots(radius, opponent);
                if (enemies.length > 0) {
                    MapLocation toAttack = enemies[0].location;
                    if (rc.canAttack(toAttack)) {
                        rc.attack(toAttack);
                    }
                }
            }
        }
    }
}
