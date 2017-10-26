

var getName = require('names');
var otherFunctions = require('other.functions');

var roleHarvester = require('role.harvester');
var roleHarvester2 = require('role.harvester2');

var roleUpgrader = require('role.upgrader');

var roleBuilder = require('role.builder');

var roleRepairer = require('role.repairer');

var roleMiner = require('role.miner');

var roleCollector = require('role.collector');
var roleCollectorV2 = require('role.collectorV2');

var roleWorker = require('role.worker');

var runTowers = require('role.tower');



/*
    Role Numbers:
    1 *Miner
    2 *Collector
    3 *Worker
    4  Patroller
    5  Defender (ranged def)
    6  Melee (melee def)
    7  Healer (healing def)
    8  
    9  
    10 
    11 
    12 
    13 
*/

/*
    Flag Colors and numbers:
    Primary         Secondary
    1  Red      
    2  Purple   
    3  Blue     
    4  Cyan     
    5  Green    
    6  Yellow   
    7  Orange   
    8  Brown    MinePickupFlags
                    1 Red, no Container
                    3 Blue, Container
    9  Grey     
    10 White    
*/

// other functions

	
	function createName (role) {
	    var letter = 'Z';
	    
	    switch (role) {
	        
	        case 1:
	            letter = "M";
	            break;
	        
	        case 2:
	            letter = 'C';
	            break;
	        
	        case 3:
	            letter = 'W';
	            break;
	        
	        case 4:
	            letter = 'P';
	            break;
	        
	        case 5:
	            letter = "D";
	            break;
	        
	        case 6:
	            letter = 'M';
	            break;
	        
	        case 7:
	            letter = 'H';
	            break;
	        
	    }
		
		if (.5 > Math.random()) {
			var space = ' ';
		}
		else {
			var space = '_';
		}
		
		var taken = true;
		while (taken) { // Find a name that's not taken
			var name = getName.getName();
			var fullName = letter + space + name;
			taken = false;
			for (var n in Game.creeps) {
				if (fullName == n) {
					taken = true;
					break;
				}
			}
			
		}
	    return fullName;

	};
	
	
	function destinationReset (creep) {
	    creep.dest = null;
	};
	
	
	function requestMiner (room, ID, type) {  
	    
		// Adds a miner request
		// Takes, the room, source ID, and miner type (0, 1, 2) [number of containers]
		
		if (! room.memory.queue) { // Queue is empty or nonexistant
			room.memory.queue = [];
		}
		room.memory.queue.push([1, ID, type]);
		
		if (! room.memory.sourceQueue[ID]) {
			room.memory.sourceQueue[ID] = 1;
		}
		else {
			room.memory.sourceQueue[ID] ++;
		}
		
	};
	
	
	function creepRequester () { 
		// Checks every room to see if creeps need to be requested
		for (var nam in Memory.myRooms) {
		    var room = Game.rooms[Memory.myRooms[nam]];
		    if (! room.memory.sourceQueue) {
			    room.memory.sourceQueue = {};
		    }
		    if (!room.memory.collectorsQueue) {
		        room.memory.collectorsQueue = 0;
		    }
		    if (!room.memory.workersQueue) {
		        room.memory.workersQueue = 0;
		    }
		    
		    var count = 0;
		    // Check to spawn miners 
		    for (var num in room.memory.sources) {
		        var ID = room.memory.sources[num];
		        count ++;
		        if(! room.memory.sourceQueue[ID]) {
		            room.memory.sourceQueue[ID] = 0
		        }
		        if (room.memory.sourceQueue[ID] + room.memory.sourceMiners[ID] < 2) {
		            requestMiner(room, ID, 2);
		        }
		    }
		    // Big Miners for sources with 1 container
		    for (var num in room.memory.sourcesBig) {
		        var ID = room.memory.sourcesBig[num];
		        count ++;
		        if(! room.memory.sourceQueue[ID]) {
		            room.memory.sourceQueue[ID] = 0
		        }
		        if (room.memory.sourceQueue[ID] + room.memory.sourceMiners[ID] < 2) {
		            requestMiner(room, ID, 1);
		        }
		    }
		    // Lonely Miners for sources with no containers
		    for (var num in room.memory.sourcesNone) {
		        var ID = room.memory.sourcesNone[num];
		        count ++;
		        if(! room.memory.sourceQueue[ID]) {
		            room.memory.sourceQueue[ID] = 0
		        }
		        if (room.memory.sourceQueue[ID] + room.memory.sourceMiners[ID] < 2) {
		            requestMiner(room, ID, 0);
		        }
		    }
		    
		    
		    // Check to spawn collectors
		    // Max # = 2 + 2 * sources 
    
	//console.log('8')
	
		    if (room.memory.collectors + room.memory.collectorsQueue < (1 + 1 * count) ) {
		        room.memory.queue.push([2]);
                room.memory.collectorsQueue ++;
		    }
		    
		    // Check to spawn Workers
		    // Max # = 6 + 1.34 * sources 
		    if (room.memory.workers + room.memory.workersQueue < (5 + 1 * count) ) {
    
	//console.log('9')
	
		        room.memory.queue.push([3]);
                room.memory.workersQueue ++;
		    }
			
			// ****************************************************************************************************
			// **************************************ADD DEFENSIVE SPAWNS******************************************
			// **************************************ADD OFFENSIVE SPAWNS******************************************
			// ****************************************************************************************************
			
		}
		
	};
    
	// console.log('11')
	
	
	function creepSpawner () { 
		// Looks at every room's queue and attempts to spawn each request
		for (var nam in Memory.myRooms) {
		    var room = Game.rooms[Memory.myRooms[nam]];
		    if (!room.memory.queue) {
    
	//console.log('11')
	
		        room.memory.queue = [];
		    }
		    if (room.memory.queue.length > 0) { // If there's anything in the queue for this room
    
	//console.log('13')
	
		        for (var num in room.memory.spawns) {
    
	//console.log('13.5')
	
		            var spawn = Game.getObjectById(room.memory.spawns[num]);
		            if (! spawn) { // If spawn no longer exists, remove it's id
    
	//console.log('14')
	
		                room.memory.spawns.splice(num, 1);
		                continue;
		            }
		            else if (! spawn.spawning) { // if the spawn is not busy
    
	//console.log('15')
	
		            
		                if (room.memory.miners < 1 || (room.memory.miners < 2 && room.memory.collectors != 0)) {
    
	//console.log('16')
	
		                    for (var i in room.memory.queue) {
		                        var order = room.memory.queue[i];
		                        var roleNumber = order[0];
		                        if (roleNumber == 1) {
    
	//console.log('17')
	
		                            if ((spawn.canCreateCreep( bigMinerBody, createName(roleNumber), {role: roleNumber, happy: false, source: order[1], type: order[2]}) == 0) ){
    
	//console.log('18')
	
		                                // If it worked
										spawn.createCreep( minerBody, createName(roleNumber), {role: roleNumber, happy: false, source: order[1], type: order[2], bigMiner: true});
		                                room.memory.queue.splice(i, 1);
		                                room.memory.sourceQueue[order[1]] --;
		                                continue;
		                            }
		                            if ((spawn.canCreateCreep( minerBody, createName(roleNumber), {role: roleNumber, happy: false, source: order[1], type: order[2]}) == 0) ){
    
	//console.log('18')
	
		                                // If it worked
										spawn.createCreep( minerBody, createName(roleNumber), {role: roleNumber, happy: false, source: order[1], type: order[2]});
		                                room.memory.queue.splice(i, 1);
		                                room.memory.sourceQueue[order[1]] --;
		                                continue;
		                            }
		                            else if ((spawn.canCreateCreep( smallMinerBody, createName(roleNumber), {role: roleNumber, happy: false, source: order[1], type: order[2]}) == 0) ){
    
	//console.log('19')
	
		                                // If it worked
										spawn.createCreep( smallMinerBody, createName(roleNumber), {role: roleNumber, happy: false, source: order[1], type: order[2]});
		                                room.memory.queue.splice(i, 1);
		                                room.memory.sourceQueue[order[1]] --;
		                                continue;
		                            }
		                        }
		                    }
		                }
		                else if (room.memory.collectors < 3) {
    
	//console.log('20')
	
		                    for (var i in room.memory.queue) {
		                        var order = room.memory.queue[i];
		                        var roleNumber = order[0];
		                        if (roleNumber == 2) {
    
	//console.log('21')
	
		                            if ((spawn.canCreateCreep(collectorBody, createName(roleNumber), {role: roleNumber, dropoff: false}) == 0) ){
    
	//console.log('22')
	
		                                // If it worked
										spawn.createCreep(collectorBody, createName(roleNumber), {role: roleNumber, dropoff: false});
		                                room.memory.queue.splice(i, 1);
		                                room.memory.collectorsQueue --;
		                                continue;
		                            }
		                        }
		                    }
		                }
		                else if (room.memory.workers < 3) {
    
	//console.log('23')
	
		                    for (var i in room.memory.queue) {
		                        var order = room.memory.queue[i];
		                        var roleNumber = order[0];
		                        if (roleNumber == 3) {
    
	//console.log('24')
	
		                            if ((spawn.canCreateCreep(smallWorkerBody, createName(roleNumber), {role: roleNumber, task: 0}) == 0) ){
    
	//console.log('25')
	
		                                // If it worked
										spawn.createCreep(smallWorkerBody, createName(roleNumber), {role: roleNumber, task: 0});
		                                room.memory.queue.splice(i, 1);
		                                room.memory.workersQueue --;
    
	//console.log('25.5')
	
		                                continue;
		                            }
		                        }
		                    }
		                }
		                else if (room.memory.alertLevel) { // if the room is on alert
    
	//console.log('26')
	
		                    for (var i in room.memory.queue) {
		                        var order = room.memory.queue[i];
		                        var roleNumber = order[0];
		                        var worked = false;
		                        switch (roleNumber) {
		                            
		                            case 4:
		                                if ((spawn.canCreateCreep(patrollerBody, createName(roleNumber), {role: roleNumber}) == 0) ){
		                                // If it worked
											spawn.createCreep(patrollerBody, createName(roleNumber), {role: roleNumber});
		                                    room.memory.queue.splice(i, 1);
		                                    room.memory.patollersQueue --;
		                                    worked = true;
		                                }
		                                break;
		                            
		                            case 5:
		                                if ((spawn.canCreateCreep(defenderBody, createName(roleNumber), {role: roleNumber}) == 0) ){
		                                // If it worked
											spawn.createCreep(defenderBody, createName(roleNumber), {role: roleNumber});;
		                                    room.memory.queue.splice(i, 1);
		                                    room.memory.defendersQueue --;
		                                    worked = true;
		                                }
		                                break;
		                            
		                            case 6:
		                                if ((spawn.canCreateCreep(meleeBody, createName(roleNumber), {role: roleNumber}) == 0) ){
		                                // If it worked
											spawn.createCreep(meleeBody, createName(roleNumber), {role: roleNumber});
		                                    room.memory.queue.splice(i, 1);
		                                    room.memory.meleeQueue --;
		                                    worked = true;
		                                }
		                                break;
		                            
		                            case 7:
		                                if ((spawn.canCreateCreep(healerBody, createName(roleNumber), {role: roleNumber}) == 0) ){
		                                // If it worked
											spawn.createCreep(healerBody, createName(roleNumber), {role: roleNumber});
		                                    room.memory.queue.splice(i, 1);
		                                    room.memory.healersQueue --;
		                                    worked = true;
		                                }
		                                break;
		                            
		                        }
		                        if (worked) {
		                            continue;
		                        }
		                    }
		                }
		                else { // Otherwise first in, first out
    
	//console.log('27')
	
		                    var order = room.memory.queue[0];
		                    var roleNumber = order[0];
		                    switch (roleNumber) {
		                        
		                        case 1: 
    
	//console.log('28')
	
		                            if (room.energyAvailable >= 700) {
		                            var thisBody = bigMinerBody;
		                            var isBigMiner = true;
		                            }
		                            else if (room.energyAvailable >= 550) {
		                            var thisBody = minerBody;
		                            var isBigMiner = false;
		                            }
		                            else {
		                                thisBody = smallMinerBody;
		                                   var isBigMiner = false;
		                            }

		                            if ((spawn.canCreateCreep( thisBody, createName(roleNumber), {role: roleNumber, happy: false, source: order[1], type: order[2], bigMiner: isBigMiner}) == 0)){
    
	//console.log('29')
	
		                                // If it worked
										spawn.createCreep( thisBody, createName(roleNumber), {role: roleNumber, happy: false, source: order[1], type: order[2], bigMiner: isBigMiner });
		                                room.memory.queue.splice(0, 1);
		                                room.memory.sourceQueue[order[1]] --;
		                            }
		                            break;
		                        
		                        case 2: 
    
	//console.log('30')
	
		                            if ((spawn.canCreateCreep(collectorBody, createName(roleNumber), {role: roleNumber}) == 0) ){
    
	//console.log('31')
	
		                                // If it worked
										spawn.createCreep(collectorBody, createName(roleNumber), {role: roleNumber});
	                                    room.memory.queue.splice(0, 1);
		                                room.memory.collectorsQueue --;
		                            }
		                            break;
		                        
		                        case 3: 
    
	//console.log('32')
	
		                            if (room.memory.extensions.length < 8 ) {
    
	//console.log('33')
	
		                                var thisBody = smallWorkerBody;
		                            }
		                            else {
    
	//console.log('34')
	
		                                var thisBody = workerBody;
		                            }
		                            if ((spawn.canCreateCreep(thisBody, createName(roleNumber), {role: roleNumber, task: 0}) == 0) ){
    
	//console.log('35')
	
		                                // If it worked
										spawn.createCreep(thisBody, createName(roleNumber), {role: roleNumber, task: 0});
	                                    room.memory.queue.splice(0, 1);
		                                room.memory.workersQueue --;
		                            }
		                            break;
		                        
		                        case 4: 
		                            if ((spawn.canCreateCreep(patrollerBody, createName(roleNumber), {role: roleNumber}) == 0) ){
		                                // If it worked
										spawn.createCreep(patrollerBody, createName(roleNumber), {role: roleNumber});
	                                    room.memory.queue.splice(0, 1);
		                                room.memory.patrollersQueue --;
		                            }
		                            break;
		                        
		                        case 5: 
		                            if ((spawn.canCreateCreep(defenderBody, createName(roleNumber), {role: roleNumber}) == 0) ){
		                                // If it worked
										spawn.createCreep(defenderBody, createName(roleNumber), {role: roleNumber});
	                                    room.memory.queue.splice(0, 1);
		                                room.memory.defendersQueue --;
		                            }
		                            break;
		                        
		                        case 6: 
		                            if ((spawn.canCreateCreep(meleeBody, createName(roleNumber), {role: roleNumber}) == 0) ){
		                                // If it worked
										spawn.createCreep(meleeBody, createName(roleNumber), {role: roleNumber});
	                                    room.memory.queue.splice(0, 1);
		                                room.memory.meleeQueue --;
		                            }
		                            break;
		                        
		                        case 7: 
		                            if ((spawn.canCreateCreep(healerBody, createName(roleNumber), {role: roleNumber}) == 0) ){
		                                // If it worked
										spawn.createCreep(healerBody, createName(roleNumber), {role: roleNumber});
	                                    room.memory.queue.splice(0, 1);
		                                room.memory.healersQueue --;
		                            }
		                            break;
		                        
		                    }
		                    
		                }
		            }
		        }
		    }
		}
	};
	
	
	function findConstructionSites (room) {
		// Finds a room's construction sites
		// returns nothing, changes room.memory.constructionSites
		// puts walls and ramparts at the back of the list
		var list = room.find(FIND_CONSTRUCTION_SITES);
		var wallList = [];
		var otherList = [];
		for (var nam in list) {
			var site = list[nam];
			if (site.structureType == STRUCTURE_WALL || site.structureType == STRUCTURE_RAMPART) {
				wallList.push(site.id);
			}
			else {
				otherList.push(site.id)
			}
		}
		room.memory.constructionSites = otherList;
		room.memory.constructionSitesWalls = wallList;
	};
	    
	
	function findRepairSites (room) {
		// Finds a room's structures needing repair (more than 1000 hp missing)
		// returns nothing, changes room.memory.repairSites
		// sorts the list based on how much hp they have
		var list = room.find(FIND_STRUCTURES, {
        filter: object => (object.hitsMax - object.hits) >= 100 });
        list.sort((a,b) => a.hits - b.hits);
		var newList = [];
		var roadList = [];
		for (var nam in list) {
			var site = list[nam];
			
			if (site.hitsMax - site.hits > 500) {
			newList.push(site.id);
			}
			if (site.structureType == STRUCTURE_ROAD)
			    roadList.push(site);
		}
		room.memory.repairSites = newList;
		room.memory.repairSitesRoads = roadList;
	};
	    
	
	function findHostiles (room) {
		// Checks the room for hostiles and sets the alert level
		var hostiles = room.find(FIND_HOSTILE_CREEPS);
		if (hostiles.length > 0) {
			room.memory.alert = 1 
		}
		else {
			room.memory.alert = 0 
		}
		room.memory.hostiles = hostiles;
	}
	
	
	function cleanCreepMemory (){
	    // Removes dead creeps from memory, must run at beginning of code, before creep creation
        for(var i in Memory.creeps) {
            if(!Game.creeps[i]) {
                delete Memory.creeps[i];
            }
        }
	}
	
	function cleanRoomMemory () {
	    // Removes lost rooms from memory, must run at beginning of code, before creep creation
	    for (var i in Memory.rooms) {
	        if (!Game.rooms[i]) {
	            delete Memory.rooms[i];
	        }
	    }
	}
	
	
	// Constants

	
	var runChecksEveryTurn = true;
	
	
	var basicBody = [WORK,WORK,CARRY,MOVE,CARRY,MOVE];
	var minerBody = [WORK, WORK, WORK, MOVE, MOVE];
	var smallMinerBody = [WORK, WORK, MOVE];
	var bigMinerBody = [WORK, WORK, WORK, WORK, WORK, WORK, MOVE, MOVE, MOVE];
	var collectorBody = [CARRY,CARRY,CARRY,CARRY,MOVE,MOVE];
	var smallWorkerBody = [WORK,CARRY,CARRY,MOVE,MOVE];
	var workerBody = [WORK,WORK,WORK,CARRY,CARRY,CARRY,MOVE,MOVE,MOVE];
	var bigWorkerBody = [WORK,WORK,WORK,WORK,CARRY,CARRY,CARRY,CARRY,MOVE,MOVE,MOVE,MOVE];
	var patrollerBody = [];
	var bigPatrollerBody = [];
	var defenderBody = [];
	var bigDefenderBody = [];
	var meleeBody = [];
	var bigmeleeBody = [];
	var healerBody = [];
	var bigHealerBody = [];
	
	
	



module.exports.loop = function () {

    console.log("Game Tick: " + Game.time);
    
    // Loop Counters
    
    
    // My Game Time counter
    if (Memory.tickCounter >= 99) {
        Memory.tickCounter = 0;
    }
    else {
        Memory.tickCounter ++;
    }
    
    console.log("My Tick: " + Memory.tickCounter);
    
    // Room Counters
    
    if (Memory.tickCounter == 6) {
        var destResetThisTurn = true;
    }
    else {
        var destResetThisTurn = false;
    }
    
    
    // Removes dead creeps and lost rooms from memory every 100 turns
    if (Memory.tickCounter == 7) {
        cleanCreepMemory();
        cleanRoomMemory();
    }
    
    
    // Adds list of spawns ids to each room's memory
    // calculated every 50 turns, on the twos
    
    if (runChecksEveryTurn || Memory.tickCounter % 50 == 2) {
	       
        Memory.myRooms = [];
        for (var nam in Game.spawns) {
	        var spawn = Game.spawns[nam];
	        
	        if (! spawn.room.memory.spawns) { // adds spawn id's to the room memory
	            spawn.room.memory.spawns = [spawn.id];
	        }
	        else if (! spawn.room.memory.spawns.includes(spawn.id)) {
	            spawn.room.memory.spawns.push(spawn.id)
	        }
	        if (! Memory.myRooms.includes(spawn.room.name)) {
	            Memory.myRooms.push(spawn.room.name);
	        }
	    }
    }
	
	
	
	// Source checker
	if (runChecksEveryTurn || Memory.tickCounter % 50 == 3) {
		for (var n in Memory.myRooms) {
			var rom = Game.rooms[Memory.myRooms[n]];
            if (! rom.memory.containers) {
                rom.memory.containers = [];
            }
            if (! rom.memory.sourceExtensions) {
                rom.memory.sourceExtensions = {};
            }
            if (! rom.memory.sourceFlagsBlue) {
                rom.memory.sourceFlagsBlue = {};
            }
            if (! rom.memory.sourceFlagsRed) {
                rom.memory.sourceFlagsRed = {};
            }
			
			// Adds sources to mem.sources for 2 containers, mem.sourcesBig for 1, and mem.sourcesNone for none
	        rom.memory.sources = [];
	        rom.memory.sourcesBig = [];
	        rom.memory.sourcesNone = [];
	        rom.memory.allSources = [];
			var newContainers = []
			

			var list = rom.find(FIND_SOURCES)
			for (var nam in list) {
    
			    var s = list[nam];
			    rom.memory.allSources.push(s.id)
				var containers = s.pos.findInRange(FIND_STRUCTURES, 1, {filter: { structureType: STRUCTURE_CONTAINER }});
    
				if (containers.length == 2) {
					rom.memory.sources.push(s.id);
				}
				else if (containers.length == 1) {
					rom.memory.sourcesBig.push(s.id);
				}
				else if (containers.length == 0) {
					rom.memory.sourcesNone.push(s.id);
				}
				
				for (var contID in containers) {
	
					var contain = containers[contID];
	
					newContainers.push(contain.id);
	
					rom.memory.containers = newContainers;
					
				}
                if (! rom.memory.sourceExtensions[s.id]) {
                    rom.memory.sourceExtensions[s.id] = [];
                }
                
                
                //Flag finder
				
                rom.memory.sourceFlagsBlue[s.id] = [];
                rom.memory.sourceFlagsRed[s.id] = [];
				var flagList = s.pos.findInRange(FIND_FLAGS,2);
				for (var x in flagList) {
				    var flag = flagList[x];
				    if(flag.color == COLOR_BROWN) {
				        if (flag.secondaryColor == COLOR_BLUE) {
				            rom.memory.sourceFlagsBlue[s.id].push(flag.name);
				        }
				        else if (flag.secondaryColor == COLOR_RED) {
				            rom.memory.sourceFlagsRed[s.id].push(flag.name);
				        }
				    }
				}
			}
			
			
		}
	}
	
    if (runChecksEveryTurn || Memory.tickCounter % 50 == 4) {
        // Adds & Checks each room's mem list of containers (near sources), extensions, and towers (as id's)
        
        for (var nam in Memory.myRooms) { // For each room
            var rom = Game.rooms[Memory.myRooms[nam]];
            
            // Find the containers near sources
			// Prempted by Source checker function
			
            // if (rom.memory.containers != 5) {
            //    var sourceList = rom.memory.allSources;
            //    for( var ID in sourceList) {
            //        var sourc = Game.getObjectById(sourceList[ID]);
            //        var list = sourc.pos.findInRange(FIND_STRUCTURES, 1, {filter: { structureType: STRUCTURE_CONTAINER }});
            //        for (var nam2 in list) {
            //            var contain = list[nam2];
            //            if (! rom.memory.containers.includes(contain.id)) {
            //                rom.memory.containers.push(contain.id);
            //            }
            //        }
            //    }    
            //}
            
            if (! rom.memory.towers) {
                rom.memory.towers = [];
            }
            if (! rom.memory.extensions) {
                rom.memory.extensions = [];
            }
            if (! rom.memory.extensionsWithLocalSource) {
                rom.memory.extensionsWithLocalSource = [];
            }

            
            // Find extensions and towers
            var list2 = rom.find(FIND_STRUCTURES, {filter: (structure) => { return (structure.structureType == STRUCTURE_EXTENSION ||
                                structure.structureType == STRUCTURE_TOWER)}});
            for (var name3 in list2) {
                var struct = list2[name3];
                
                    // Checks whether the structure is on the list
                if (struct.structureType == STRUCTURE_TOWER) {
                    if(! rom.memory.towers.includes(struct.id)) {
                        rom.memory.towers.push(struct.id)
                    }
                }
                
                if (struct.structureType == STRUCTURE_EXTENSION) {
                    if(! rom.memory.extensions.includes(struct.id)) {
                        rom.memory.extensions.push(struct.id)
                    }
                    if(! rom.memory.extensionsWithLocalSource.includes(struct.id)) {
                        var mySource = struct.pos.findClosestByPath(FIND_SOURCES);
                        console.log(mySource);
                        
                        rom.memory.extensionsWithLocalSource.push(struct.id);
                        rom.memory.sourceExtensions[mySource.id].push(struct.id);
                    }
                    
                }
            }
            
        }
    }
    
	
    
	// ****************************************************************************************************
	// ROOM BY ROOM CHECKER/ COUNTER RESET
    
        // room.memory.workers has # of workers in each room
	for (var nam in Memory.myRooms) {
	    var rm = Game.rooms[Memory.myRooms[nam]];
	    rm.memory.miners = 0;
	    rm.memory.workers = 0;
	    rm.memory.collectors = 0;
	    rm.memory.upgradeWorkersLast = rm.memory.upgradeWorkers;
	    rm.memory.repairRoadWorkersLast = rm.memory.repairRoadWorkers;
	    rm.memory.repairWorkersLast = rm.memory.repairWorkers;
	    rm.memory.buildUpgradeWorkersLast = rm.memory.buildUpgradeWorkers;
	    rm.memory.buildRepairWorkersLast = rm.memory.buildRepairWorkers;
	    rm.memory.upgradeWorkers = 0;
	    rm.memory.repairRoadWorkers = 0;
	    rm.memory.repairWorkers = 0;
	    rm.memory.buildUpgradeWorkers = 0;
	    rm.memory.buildRepairWorkers = 0;
		
		if (! rm.memory.sourceMiners) {
			rm.memory.sourceMiners = {};
		}
		if (!rm.memory.sourceCollectors)
			rm.memory.sourceCollectors = {};
		if (!rm.memory.sourceWorkers)
			rm.memory.sourceWorkers = {};
		
		var sourceList = rm.memory.allSources;
		for (var ID in sourceList) {
			rm.memory.sourceMiners[sourceList[ID]] = 0;
			rm.memory.sourceCollectors[sourceList[ID]] = 0;
			if (rm.memory.sourceWorkers[ID] == undefined) {
			    rm.memory.sourceWorkers[ID] = 0;
			}
		}
		
		
		// Construction site check
		findConstructionSites(rm);
		
		// Repair site check
		findRepairSites(rm);
		
		// Hostiles/ Alert update
		findHostiles(rm);
		
		// Tower AI (make sure it's after hostiles)
		runTowers.run(rm);
	}
    
    

    
    /*
    var upgraders = _.filter(Game.creeps, (creep) => creep.memory.role == 'upgrader');

    //console.log('Upgraders: ' + upgraders.length);



    if(upgraders.length < 3 && (Game.spawns['Spawn1'].canCreateCreep(basicBody, undefined, {role: 'upgrader'}) === 0)) {
        
        var newName = Game.spawns['Spawn1'].createCreep(basicBody, undefined, {role: 'upgrader'});
        console.log('Spawning new upgrader: ' + newName);
        

    }


    var harvesters = _.filter(Game.creeps, (creep) => creep.memory.role == 'harvester');

    //console.log('Harvesters: ' + harvesters.length);



    if(harvesters.length < 3 && (Game.spawns['Spawn1'].canCreateCreep(basicBody, undefined, {role: 'harvester'}) === 0)) {

        var newName = Game.spawns['Spawn1'].createCreep(basicBody, undefined, {role: 'harvester'});
        if (newName === 0) {
            console.log('Spawning new harvester: ' + newName);
        }

    }
    
    var harvesters2 = _.filter(Game.creeps, (creep) => creep.memory.role == 'harvester2');

    //console.log('Harvesters2: ' + harvesters.length);



    if(harvesters2.length < 3 && (Game.spawns['Spawn1'].canCreateCreep(basicBody, undefined, {role: 'harvester2'}) === 0)) {

        var newName = Game.spawns['Spawn1'].createCreep(basicBody, undefined, {role: 'harvester2'});
        if (newName === 0) {
            console.log('Spawning new harvester2: ' + newName);
        }

    }
    
        var builders = _.filter(Game.creeps, (creep) => creep.memory.role == 'builder');

    //console.log('Builders: ' + builders.length);



    if(builders.length < 1 && (Game.spawns['Spawn1'].canCreateCreep(basicBody, undefined, {role: 'builder'}) === 0)) {

        var newName = Game.spawns['Spawn1'].createCreep(basicBody, undefined, {role: 'builder'});
        if (newName === 0) {
            console.log('Spawning new builder: ' + newName);
        }

    }
    
        var repairers = _.filter(Game.creeps, (creep) => creep.memory.role == 'repairer');

    //console.log('Repairers: ' + repairers.length);



    if(repairers.length < 2 && (Game.spawns['Spawn1'].canCreateCreep(basicBody, undefined, {role: 'repairer'}) === 0)) {

        var newName = Game.spawns['Spawn1'].createCreep(basicBody, undefined, {role: 'repairer'});
        if (newName === 0) {
            console.log('Spawning new repairer: ' + newName);
        }

    }



    var tower = Game.getObjectById('585644c979ebbad12bf05827');

    if(tower) {
        if (tower.energy > 350) {
            var targets = tower.room.find(FIND_STRUCTURES, {
            filter: object => (object.hitsMax - object.hits) > 800 });

            targets.sort((a,b) => a.hits - b.hits);


            if(targets.length > 0) {

                tower.repair(targets[0]);


                

            }
        }



        var closestHostile = tower.pos.findClosestByRange(FIND_HOSTILE_CREEPS);

        if(closestHostile) {

            tower.attack(closestHostile);

        }

    }
    */

    // Creep runner & counter

    for(var nam in Game.creeps) {
        var creep = Game.creeps[nam];
        var role = creep.memory.role;
        
        if (destResetThisTurn) {
            destinationReset(creep);
        }
        
		switch(role) {
		
			case 1:
			    if(creep.memory.bigMiner == true) {
				creep.room.memory.sourceMiners[creep.memory.source] += 2;
				creep.room.memory.miners += 2;
			    }
			    else {
			    	creep.room.memory.sourceMiners[creep.memory.source] ++;
				    creep.room.memory.miners ++;
			    }
				roleMiner.run(creep);
			    break;
		
			case 2:
				creep.room.memory.collectors ++;
				roleCollector.run(creep);
			    break;
			    
			case 3:
				creep.room.memory.workers ++;
				roleWorker.run(creep);
			    break;
			    
			case 'harvester':
				roleHarvester.run(creep);
			    break;
			    
			case 'harvester2':
				roleHarvester2.run(creep);
			    break;
		
			case 'upgrader':
				roleUpgrader.run(creep);
			    break;
		
			case 'builder':
				roleBuilder.run(creep);
			    break;
		
			case 'repairer':
				roleRepairer.run(creep);
			    break;
			
		}
		
		// If not a miner and on container, get off
		if (creep.memory.role != 1) { 
		    var onContainer = creep.pos.findInRange(FIND_STRUCTURES, 0, {filter: { structureType: STRUCTURE_CONTAINER }});
		    if (onContainer.length > 0) {
		        creep.move(otherFunctions.chooseRandomDirection);
		    }
		}
        

    }
    
    // Log units counts
    // if neccessary
    
    if (Game.time > 8) {
	    // Creep Requester
	    creepRequester();
    
	    // Creep Spawner
	    creepSpawner();
	
    }
	

};
