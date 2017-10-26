/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.miner');
 * mod.thing == 'a thing'; // true
 */

    /** Mines its assigned source
     *      Sits on container, which collects the energy
     *      if not on a container it moves to one near its source
    **/ 
    
    var otherFunctions = require('other.functions');
    
    
    

    function runMiner2 (creep) {

        if (creep.memory.happy) {
            if (creep.harvest(Game.getObjectById(creep.memory.source)) == ERR_NOT_IN_RANGE) {
                creep.memory.happy = false;
            }
            else if(Memory.tickCounter == 0) {
                if (! creep.memory.container || ! creep.pos.isEqualTo(Game.getObjectById(creep.memory.container))) {
                    creep.memory.happy = false;
                    creep.moveTo(Game.getObjectById(creep.memory.container));
                }
            }
        }
        
        if (!creep.memory.happy) {   // ie Not happy, move to your container
            if (creep.memory.container) {
                if (creep.pos.isEqualTo(Game.getObjectById(creep.memory.container))) {
                    creep.memory.happy = true;
                    var blah = creep.harvest(Game.getObjectById(creep.memory.source));
                }
                else {
                    if (creep.pos.isNearTo(Game.getObjectById(creep.memory.container))) {
                        var otherCreep = (creep.room.lookForAt(LOOK_CREEPS, (Game.getObjectById(creep.memory.container)).pos));
                        if (otherCreep.length != 0) {
                            if (otherCreep[0].memory.role == 1) {
                                var targets = (Game.getObjectById(creep.memory.source)).pos.findInRange(FIND_STRUCTURES, 1, {filter: { structureType: STRUCTURE_CONTAINER }});
                                if (targets[0].id == creep.memory.container && targets.length > 1) {
                                    creep.memory.container = targets[1].id;
                                }
                                else {
                                    creep.memory.container = null; // No container for you
                                    creep.memory.type = 0; // Act like there's no container
                                    creep.moveTo(Game.getObjectById(creep.memory.source));
                                }
                            }
                        }
                    }
                }
            }
            else {
                var targets = (Game.getObjectById(creep.memory.source)).pos.findInRange(FIND_STRUCTURES, 1, {filter: { structureType: STRUCTURE_CONTAINER }});
                if (targets.length > 0) {
                    creep.memory.container = targets[0].id;
                }
            }
            if (!creep.memory.happy) {
                creep.moveTo(Game.getObjectById(creep.memory.container));
				var creeps = (Game.getObjectById(creep.memory.source)).pos.findInRange(FIND_MY_CREEPS, 2);
				var miners = 0;
				for (var name in creeps) {
					if (creeps[name].memory.role == 1) {
						miners ++;
					}
				}
				if (miners >= 4) {
					creep.suicide();
				}
            }
			
        }
        
        creep.harvest(Game.getObjectById(creep.memory.source))

    };
    
    function runMiner1 (creep) {
        runMiner2(creep);
    };
    
    function runMiner0 (creep) {
        if (creep.memory.happy) {
            if (creep.harvest(Game.getObjectById(creep.memory.source)) == ERR_NOT_IN_RANGE); {
                creep.memory.happy = false;
            }
        }
        
        if (!creep.memory.happy) {
            if (creep.pos.isNearTo(Game.getObjectById(creep.memory.source))) {
                creep.memory.happy = true;
                creep.harvest(Game.getObjectById(creep.memory.source))
            }
            else {
                creep.moveTo(Game.getObjectById(creep.memory.source));
            }
        }
        
        // Attempt to start a container construction site after several hundred ticks
        if (creep.ticksToLive <= 600 && !creep.memory.startedConstruction && creep.memory.happy) {
            var targets = creep.pos.findInRange(FIND_STRUCTURES, 1, {filter: { structureType: STRUCTURE_CONTAINER }});
            if (targets.length == 0) {
                var sites = creep.pos.findInRange(FIND_CONSTRUCTION_SITES, 1, {filter: { structureType: STRUCTURE_CONTAINER }});
                if (sites.length == 0) {
                    var blah = creep.pos.createConstructionSite(STRUCTURE_CONTAINER);
                    if (blah == 0) {
                        creep.memory.startedConstruction = true;
                    }
                    if (blah == ERR_INVALID_TARGET || blah == ERR_INVALID_ARGS) {
                        if (creep.move(otherFunctions.chooseRandomDirection()) == 0) {
                            creep.memory.happy = false;
                        }
                    }
                }
            }
        }
        
    };


    function createPickupFlags (creep, room, source) {
        // Takes a creep, room, and source objects
        // creates mining flags on the side of the miner opposite to the source
        // They are brown primary & red secondary for no container, blue secondary for yes container
        var flagDirection = source.pos.getDirectionTo(creep);
        var list = otherFunctions.getThreePositionsInDirection(room, creep.pos, flagDirection);
        var pos1 = list[0];
        var pos2 = list[1];
        var pos3 = list[2];
        var isFlag1 = false;
        var isFlag2 = false;
        var isFlag3 = false;
        if(creep.pos.isEqualTo(Game.getObjectById(creep.memory.container))) {
            // Are you on your container?
            var secColor = COLOR_BLUE;
        }
        
        else {
            var secColor = COLOR_RED;
        }
            
            var list1 = room.lookForAt(LOOK_FLAGS, pos1);
            var list2 = room.lookForAt(LOOK_FLAGS, pos2);
            var list3 = room.lookForAt(LOOK_FLAGS, pos3);
            
            for (var i in list1) {
                var flag1 = list1[i];
                if (flag1.color == COLOR_BROWN && flag1.secondaryColor == secColor) {
                    isFlag1 = true;
                    break;
                }
                if (flag1.color == COLOR_BROWN && flag1.secondaryColor == COLOR_RED) {
                    flag1.setColor(COLOR_BROWN, secColor);
                    isFlag1 = true;
                    break;
                }
            }
            
            for (var i in list2) {
                var flag2 = list2[i];
                if (flag2.color == COLOR_BROWN && flag2.secondaryColor == secColor) {
                    isFlag2 = true;
                    break;
                }
                if (flag2.color == COLOR_BROWN && flag2.secondaryColor == COLOR_RED) {
                    flag2.setColor(COLOR_BROWN, secColor);
                    isFlag2 = true;
                    break;
                }
            }
            
            for (var i in list3) {
                var flag3 = list3[i];
                if (flag3.color == COLOR_BROWN && flag3.secondaryColor == secColor) {
                    isFlag3 = true;
                    break;
                }
                if (flag3.color == COLOR_BROWN && flag3.secondaryColor == COLOR_RED) {
                    flag3.setColor(COLOR_BROWN, secColor);
                    isFlag3 = true;
                    break;
                }
            }
            
            if(! flag1 && Game.map.getTerrainAt(pos1) != "wall") {
                pos1.createFlag(otherFunctions.createFlagName("M"), COLOR_BROWN, secColor);
            }
            if(! flag2 && Game.map.getTerrainAt(pos2) != "wall") {
                pos2.createFlag(otherFunctions.createFlagName("M"), COLOR_BROWN, secColor);
            }
            if(! flag3 && Game.map.getTerrainAt(pos3) != "wall") {
                pos3.createFlag(otherFunctions.createFlagName("M"), COLOR_BROWN, secColor);
            }
            
            
        creep.memory.hasFlags = true;

    };




var roleMiner = {



    
    run: function (creep) {
        
        var type = creep.memory.type;
        switch (type) {
            case 2: 
                runMiner2(creep);
                break;
            case 1: 
                runMiner1(creep);
                break;
            case 0: 
                runMiner0(creep);
                break;
        }
        
        if (! creep.memory.hasFlags && creep.memory.happy) { // If there are no pickup flags and you're happy
            createPickupFlags(creep, creep.room, Game.getObjectById(creep.memory.source));
        }
    }
    
};



module.exports = roleMiner;