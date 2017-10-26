/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.collector');
 * mod.thing == 'a thing'; // true
 */

    /** Collector: Takes energy from sources (prioritize dropped energy)
     * to spawners, extendions, and storage
     * Stores its destination in .dest as an id #
     * role # is 2
     * .memory has
     * .dropoff, .dest
    **/
    
    var otherFunctions = require('other.functions');
    



var roleCollector = {


    run: function(creep) {
        
        if (! Game.getObjectById(creep.memory.dest) || Memory.tickCounter % 25 == 0) {
            creep.memory.dest = null;
        }
        if (!creep.memory.dropoff && creep.carryCapacity - _.sum(creep.carry) <= 10) {
        
        
            creep.memory.dropoff = true;
            creep.memory.dest = null;
        }
        else if (creep.memory.dropoff && _.sum(creep.carry) < 15) {

        
            creep.memory.dropoff = false;
            creep.memory.dest = null;
        }

        //********************************************************************************************************************************************************************
        
        if (! creep.memory.dropoff) { // Go pickup more
        

        
            if (Game.getObjectById(creep.memory.dest)) {  // Has a destination in mind
        

        
                if (creep.pos.isNearTo(Game.getObjectById(creep.memory.dest))) {  // is next to destination

        
                    var droppedEn = creep.pos.findInRange(FIND_DROPPED_ENERGY, 1);
                    if(droppedEn.length > 0) { // Some dropped energy is near
        

        
                        var target = otherFunctions.highestEnergy(droppedEn);
                        creep.pickup(target);
                        creep.memory.dest = null;
                    }
                    if(_.sum(creep.carry) < creep.carryCapacity) {
        
   
        
                        var containers = creep.pos.findInRange(FIND_STRUCTURES, 1, {filter: { structureType: STRUCTURE_CONTAINER }});
                    }
                    if (containers.length > 0) { // containers are near, try containers
        

        
                        var target = otherFunctions.highestEnergyStorage(containers);
                        creep.withdraw(target, RESOURCE_ENERGY);
                        creep.memory.dest = null;
                    }
                    else { // Nothing here, find another dest
        

        
                        creep.memory.dest = null;
                    }
                    if (_.sum(creep.carry) == creep.carryCapacity) {
        

        
                        creep.memory.dropoff = true;
                    }
                }
                else {  // Gotta keep going
        

        
					if (creep.moveTo(Game.getObjectById(creep.memory.dest)) != 0) { // If destination invalid, get a new one
        

        
						creep.memory.dest = null;
                    }
                }
                var blah = creep.pickup(Game.getObjectById(creep.memory.dest));
                var blah = creep.withdraw(Game.getObjectById(creep.memory.dest), RESOURCE_ENERGY);
            }
            if (! Game.getObjectById(creep.memory.dest) && _.sum(creep.carry) < creep.carryCapacity) { // Needs a dest to pickup from
        

        
                var target = null;
                var checkAgain = true;
                if (.25 < Math.random()) { // Fourth of the time go for dropped energy
        

        
                    target = otherFunctions.findDroppedEnergyNearSources(creep.room.memory.allSources, creep.pos); 
                    checkAgain = false;
                }
				if (target) { // there is a dropped energy object
        

        
					creep.memory.dest = target.id;
					creep.moveTo(target);
					var blah = creep.pickup(target);
				}
				else { // no dropped energy, check containers
        
        
					var target = otherFunctions.findContainer(creep.room.memory.containers, creep.pos, creep.room);
					if (target) { // there is a dropped energy object
        
        
						creep.memory.dest = target.id;
						creep.moveTo(target);
                        var blah = creep.withdraw(Game.getObjectById(target), RESOURCE_ENERGY);
                        
                        if (creep.room.storage &&  (!target || .15 < Math.random())) {
                            target = creep.room.storage;
                        }
					}
				}
				
                
                if (! target && checkAgain) { // Go back and check for dropped energy
        
        
                    target = otherFunctions.findDroppedEnergyNearSources(creep.room.memory.allSources, creep.pos); 
                    if (target) {
					    creep.memory.dest = target.id;
                    }
					creep.moveTo(target);
					creep.pickup(target);
                }
                
            }
            
        }
        
        //********************************************************************************************************************************************************************
        
		if (creep.memory.dropoff) { // Go drop off
        

        
			if(creep.memory.dest) { // Knows where to go
        

        
                if (creep.pos.isNearTo(Game.getObjectById(creep.memory.dest))) {  // is next to destination
        

        
                    if(creep.transfer(Game.getObjectById(creep.memory.dest), RESOURCE_ENERGY) != 0) {
        

        
                        creep.memory.dest = null;
                    }
                    else { // Transfer failed, check if there's anything else to transfer to
        
                        var targets = creep.pos.findInRange(FIND_STRUCTURES, 1, { filter: (structure) => { return (structure.structureType == STRUCTURE_EXTENSION || structure.structureType == STRUCTURE_SPAWN ||
                                structure.structureType == STRUCTURE_TOWER || structure.structureType == STRUCTURE_STORAGE) && structure.energy < structure.energyCapacity; } });
                        creep.transfer(targets[0], RESOURCE_ENERGY);
                        
                        if (Game.getObjectById(creep.memory.dest).structureType == STRUCTURE_SPAWN && creep.room.storage) {
                            creep.memory.dest = creep.room.storage.id;
                            creep.moveTo(Game.getObjectById(creep.memory.dest));
                        }
                        else {
                            creep.memory.dest = null;
                        }
                    }
                }
                else { // Gotta keep going
        

        
                    creep.moveTo(Game.getObjectById(creep.memory.dest));  // Move on

                    if (creep.pos.isNearTo(Game.getObjectById(creep.memory.dest))) { // If moved try trasferring afterwards
        

        
                        creep.transfer(Game.getObjectById(creep.memory.dest), RESOURCE_ENERGY);
                    }
                }
			}
			if(! creep.memory.dest) { // Needs to know where to go
        
		
                var target = null; 
                if (0.15 < Math.random()) { // 85% of the time go for spawn/ extension

    
                    target = otherFunctions.findDropOff(creep.room.memory.spawns, creep.room.memory.extensions, creep.pos, creep.room);
                    var checkTowers = true;
                }
				else { // 15% of the time go straight for towers with a spawn/extension check

    
					target = otherFunctions.findTowerDropOff(creep.room.memory.towers, creep.pos, creep.room, true);

    
					var checkTowers = false;
				}
				if (target == null && checkTowers) {	// Try towers with no check
        
        
					target = otherFunctions.findTowerDropOff(creep.room.memory.towers, creep.pos, creep.room, false);
				}
				if (target == null && creep.room.storage) { // Go drop it off at storage
				    if (creep.room.storage.store[RESOURCE_ENERGY] < 500000) {
				        target = creep.room.storage;
				    }
				}
				if (target == null) {	// Go head towards the closest extension
        
	
					var nearExt = creep.pos.findInRange(FIND_STRUCTURES, 1, { filter: (structure) => { return (structure.structureType == STRUCTURE_EXTENSION)} });

					if (nearExt.length > 0) { // if next to an extension
        
        
						target = null; // No need to go anywhere
					}
					else { // Otherwise move to the closest extension
        
						var rand = _.random(0,creep.room.memory.extensions.length - 1);
						creep.moveTo(Game.getObjectById(creep.room.memory.extensions[rand]));
					}
				}
				creep.moveTo(target);
				creep.transfer(target,RESOURCE_ENERGY);
				if (target) {
        
        
					creep.memory.dest = target.id;
				}
				
			}
		}
		if(creep.room.memory.constructionSites.length < 10) {
			creep.pos.createConstructionSite(STRUCTURE_ROAD);
		}
    }
    
};
module.exports = roleCollector;
