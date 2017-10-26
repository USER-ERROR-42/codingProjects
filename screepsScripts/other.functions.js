/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('other.functions');
 * mod.thing == 'a thing'; // true
 */
 



var otherFunctions = {
    
    // A support Module that contains some useful functions
    // 
    // 
    
    run: function() {
        
    },


	
	createFlagName: function (str) {
	    //takes a string and adds an unused number
	    var taken = true;
	    while(taken) {
	        var num = _.random(0,9999)
			var fullName = str + num;
			taken = false;
			for (var n in Game.flags) {
				if (fullName == n) {
					taken = true;
					break;
				}
			}
	    }
	    return fullName;
	},
	
	
    highestEnergy: function (list) {
        // Takes a list of objects and returns the one with the most energy
        if (list.length == 0) {
            return null;
        }
        var obj = list[0];
        for(var c = 1; c < list.length; c++) {
            if(list[c].energy > obj.energy) {
                obj = list[c];
            }
        }
        return obj;
    },
        
    
    highestEnergyStorage: function (list) {
        // Takes a list of objects and returns the one with the most energy in storage
        if (list.length == 0) {
            return null;
        }
        var obj = list[0];
        for(var c = 1; c < list.length; c++) {
            if(list[c].store[RESOURCE_ENERGY] > obj.store[RESOURCE_ENERGY]) {
                obj = list[c];
            }
        }
        return obj;
    },
    
    findDroppedEnergyNearSources: function (list, position) {
        // Takes a list of source ID's
		// Returns a nearby dropped energy object to go to
		// Prioritizes larger piles and closer ones in that order
        var target = null;
		var ls = [];
        for (var ID in list) { // Iterate over every source in the room
            var source = Game.getObjectById(list[ID]);
			ls = source.pos.findInRange(FIND_DROPPED_ENERGY, 1);
			if (ls.length > 0) {
				for(var c = 0; c < ls.length; c++) {
					if (target) {
						if (ls[c].energy/ ls[c].pos.getRangeTo(position) > target.energy/ target.pos.getRangeTo(position)) {
							target = ls[c];
						}
					}
					else {
						target = ls[c];
					}
				}
			}
        }
		return target;
    },
	
	findContainer: function (list, position, rom) {
    
	//console.log('findContainer')
	
        // Takes a list of container ID's
		// Returns a nearby container to go to
		// Prioritizes larger energy stores and closer ones in that order
		var ENERGY_WEIGHT_FACTOR = 1.0033;
        var target = null;
        for (var ID in list) { // Iterate over every container in the room
            var contain = Game.getObjectById(list[ID]);
			
	//console.log('FindContainer ID: ' + contain.id + typeof(contain));
			
			if (contain) {
			
	//console.log('is a container ' + contain.id);
			
				var containScore = Math.pow(ENERGY_WEIGHT_FACTOR, contain.store[RESOURCE_ENERGY])/ contain.pos.getRangeTo(position);
				if (target) {   // Takes FACTOR^Energy/distance to determine order to pickup
					if (containScore > targetScore) {
						target = contain;
						var targetScore = containScore;
					}
				}
				else { // If no target yet, this container is target
					target = contain;
					var targetScore = containScore;
				}
			}
			else if (rom){
			
	//console.log('Was removed ' + contain.id);
			
                var blah = rom.memory.containers.splice(ID, 1);
			}
        }
		return target;
		
	},
	
	 findDropOff: function (listSpawns, listExtensions, position, rom) {
	    // Takes a list of spawn id's and extension id's and a creep's position object (and optional room object)
	    // Returns the object to dropoff at
	    // Prioritizes closeness and space remaining in that order
		var CLOSENESS_WEIGHT_FACTOR = 1.2;
		var target = null;
		var list = listSpawns.concat(listExtensions);
		for (var ID in list) {
			var spawn = Game.getObjectById(list[ID]); // Uses var spawn, but also includes extensions
			if (! spawn && rom) { // If that object no longer exists and we have the room object
				if (ID <= listSpawns.length - 1) { // ID is in the listSpawns list
					rom.memory.spawns.splice(ID, 1); // Remove that ID and continue
					continue;
				}
				else { // ID is in the listExtensions list
					rom.memory.extensions.splice(ID - listSpawns.length, 1); // Remove that ID and continue
					continue;
				}
			}
			
			if (spawn.energy < spawn.energyCapacity) { // spawn is not full
				if (! target) { // if no initial target
					target = spawn;
					var targetSpace = target.energyCapacity - target.energy;
					if (targetSpace <= 1) {
						targetSpace = 2;
					}
					var targetDist = position.getRangeTo(target);
					var targetScore = ( Math.log(targetSpace)/ Math.pow(targetDist,CLOSENESS_WEIGHT_FACTOR) );
				}
				else { 	
					// compare current target and potential one
					// Algorithm for determining which is a better dropoff point
				    // ln (space remaining) / (distance)^(CLOSENESS_WEIGHT_FACTOR)
					var spawnSpace = spawn.energyCapacity - spawn.energy;
					if (spawnSpace <= 1) { // Can't have this be <= 1
						spawnSpace = 2;    // We're gonna take its log in a bit
					}
					var spawnDist = position.getRangeTo(spawn);
					var spawnScore = ( Math.log(spawnSpace)/ Math.pow(spawnDist,CLOSENESS_WEIGHT_FACTOR) );
					if ( spawnScore > targetScore ) {
						target = spawn;
						var targetScore = spawnScore;
					}
				}
			}
		}
		return target;
	    
	},
	
	findTowerDropOff: function (list, position, rom, tryOthers) {
	    // Takes a list of tower id's and a creep's position object (and optional room object and optional bool)
	    // Returns the object to dropoff at
	    // Prioritizes space remaining and closeness in that order
		// If no towers need filling, and the bool is true calls findDropOff to check for spawns and extensions
		var SPACE_WEIGHT_FACTOR = 1.001;
		var target = null;
		for (var ID in list) {
			var tower = Game.getObjectById(list[ID]);
			if (! tower && rom) { // If that object no longer exists and we have the room object
				rom.memory.towers.splice(ID, 1); // Remove that ID and continue
				continue;
			}
			if ( tower.energyCapacity - tower.energy >= 200) { // tower is not full
				if (! target) { // if no initial target
					target = tower;
					var targetSpace = target.energyCapacity - target.energy;
					var targetDist = position.getRangeTo(target);
					if (targetDist <= 1) {
						targetDist = 1.1;
					}
					var targetScore = ( Math.pow(targetSpace, SPACE_WEIGHT_FACTOR)/ Math.log(targetDist) );
				}
				else { 	
					// compare current target and potential one
					// Algorithm for determining which is a better dropoff point
				    // (space remaining)^(SPACE_WEIGHT_FACTOR) / ln(distance)
					var towerSpace = tower.energyCapacity - tower.energy;
					var towerDist = position.GetRangeTo(tower);
					if (towerDist <= 1) {
						towerDist = 1.1;
					}
					var towerScore = ( Math.pow(towerSpace, SPACE_WEIGHT_FACTOR)/ Math.log(towerDist) );
					if ( towerScore > targetScore ) {
						target = tower;
						var targetScore = towerScore;
					}
				}
			}
		}
		if (tryOthers && rom && ! target) { // if check more, have a room object , and still no target
			target = otherFunctions.findDropOff(rom.memory.spawns, rom.memory.extensions, position, rom);
		}
		return target;
	    
	},
    
    
    chooseRandomDirection: function () {
        var rand = _.random(1,8);
        switch (rand) {
            case 1:
                return TOP;
            case 2:
                return TOP_RIGHT;
            case 3:
                return RIGHT;
            case 4:
                return BOTTOM_RIGHT;
            case 5:
                return BOTTOM;
            case 6:
                return BOTTOM_LEFT;
            case 7:
                return LEFT;
            case 8:
                return TOP_LEFT;
            default:
                return RIGHT;
        }
    },
    
    getPositionInDirection: function (room, position, direction) {
        // Takes a current position and a direction
        // Returns the position one square in the direction from the current position
        
        switch (direction) {
            case 1:
                return room.getPositionAt(position.x    , position.y - 1);
            case 2:
                return room.getPositionAt(position.x + 1, position.y - 1);
            case 3:
                return room.getPositionAt(position.x + 1, position.y    );
            case 4:
                return room.getPositionAt(position.x + 1, position.y + 1);
            case 5:
                return room.getPositionAt(position.x    , position.y + 1);
            case 6:
                return room.getPositionAt(position.x - 1, position.y + 1);
            case 7:
                return room.getPositionAt(position.x - 1, position.y    );
            case 8:
                return room.getPositionAt(position.x - 1, position.y - 1);
            default:
                return null;
        }
    },
    
    getThreePositionsInDirection: function (room, position, direction) {
        // Takes a current position and a direction
        // Returns an array of the 3 position squares in the direction from the current position
        var dir1;
        var dir2;
        var dir3;
        
        dir2 = direction;
        if (dir2 == 1) {
            dir1 = 8;
            dir3 = 2;
        }
        else if (dir2 == 8) {
            dir1 = 7;
            dir3 = 1;
        }
        else {
            dir1 = dir2 - 1;
            dir3 = dir2 + 1;
        }
        
        var pos1 = otherFunctions.getPositionInDirection(room, position, dir1) ;
        var pos2 = otherFunctions.getPositionInDirection(room, position, dir2) ;
        var pos3 = otherFunctions.getPositionInDirection(room, position, dir3) ;
        
        var list = [pos1, pos2, pos3];
        return list;
    },
    
    //************************************************NEW CODE**********************************************
				
    getDirectionThisWayNotThatWay: function (desiredDirection, forbiddenDirection) {
        // Takes a desired direction and a forbiddenDirection
        // Returns a direction toward desired, but not toward forbidden
        var difference = desiredDirection - forbiddenDirection;
        var absDist = Math.abs(difference);
        if ((absDist == 1) || absDist == 7) {
            // Conflict
            
            if (absDist == 7) {
                // Edge case
				if (desiredDirection == 1) {
					return 2;
				}
				else if (desiredDirection == 8) {
					return 7;
				}
            }
            
            else {
                // simple case
                var try1 = desiredDirection - 1;
                var try2 = desiredDirection + 1;
                var no1 = forbiddenDirection - 1;
                var no2 = forbiddenDirection + 1;
                
				// Standardize to 1-8
				
				try1 = ifZeroMakeEight(try1);
				try2 = ifNineMakeOne(try2);
				no1 = ifZeroMakeEight(no1);
				no2 = ifNineMakeOne(no2);
				
				
                if (try1 != no1 && try1 != no2) { // Try adjacent directions
                    return try1;
                }
                else {
                    return try2;
                }
                
                
            }
            
        } else if (absDist == 0) {
            // Direct Conflict go -2 for tickCounter % 25 < 13, +2 otherwise
                //*********************************************************************
			
			if (Memory.tickCounter % 25 < 13) {
				var newDir = desiredDirection - 2;
				newDir = ifNegOneMakeSeven(newDir);
				newDir = ifZeroMakeEight(newDir);
				return newDir;
			}
			else {
				var newDir = desiredDirection + 2;
				newDir = ifNineMakeOne(newDir);
				newDir = ifTenMakeTwo(newDir);
				return newDir;
			}
			
        }
        else {
            // No conflict
            return desiredDirection;
        }
        
    },
    
    ifZeroMakeEight: function (num) {
		if(num == 0) {
			return 8;
		}
		else {
			return num;
		}
	},
    
	ifNineMakeOne: function (num) {
		if(num == 9) {
			return 1;
		}
		else {
			return num;
		}
	}
    
    ifNegOneMakeSeven: function (num) {
		if(num == -1) {
			return 7;
		}
		else {
			return num;
		}
	},
    
	ifTenMakeTwo: function (num) {
		if(num == 10) {
			return 2;
		}
		else {
			return num;
		}
	}
	
    //************************************************NEW CODE**********************************************
	
};


module.exports = otherFunctions;
