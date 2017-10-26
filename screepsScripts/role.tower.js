/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.tower');
 * mod.thing == 'a thing'; // true
 */

    // Runs all of a room's towers
    // Based on alert level and hostile lists

module.exports = {
    
    run: function (room) {
		
        var alert = room.memory.alert;
		var hostiles = room.memory.hostiles;
		var towers =room.memory.towers;
		if (alert == 0) {
			var targets = room.memory.repairSites
			// No alert, repair stuff
			if (targets.length >= towers.length) {
				var list = [];
				var listIds = [];
				for (var c = 0; c < towers.length; c ++) {
					list.push(Game.getObjectById(targets[c]));
					listIds.push(targets[c]);
					
				}
				
				
				for (var nam in towers) {
					var tower = Game.getObjectById(towers[nam]);
					var closest = tower.pos.findClosestByRange(list);
					if( tower.energy > 350) {
					    tower.repair(closest);
					    var index = listIds.indexOf(closest.id);
					    if (index > -1) {
						    list.splice(index,1);
						    listIds.splice(index,1);
					    }
					}
				}
			}
			else if (targets.length > 0) {
				for (var nam in towers) {
					var tower = Game.getObjectById(towers[nam]);
					if( tower.energy > 350) {
					    tower.repair(targets[0]);
					}
				}
			}
			
		}
		else {
			// *******************************************************************
			// Think of better target selection, account for alert levels
			
			for (var nam in towers) {
				var tower = Game.getObjectById(towers[nam]);
				var closest = tower.pos.findClosestByRange(hostiles);
				tower.attack(closest);
			}
		}
    }
    

};