/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.worker');
 * mod.thing == 'a thing'; // true
 */
var otherFunctions = require('other.functions');

    /** Worker: takes energy from containers or storage, does 1 of several things with it
     * 6 Subtasks: (mem.task)
	 *  0. Collecting		(default when empty)
	 * 	1. Upgrading		(2 max) 
     *  2. Road Repairing   (1 max)
     *  3. Repairing        (2 max)
	 *  4. Build/Upgrade	(2 max)
     *  5. Building/Repair  (the rest)
	 *  ...
	 *  10.FULL-NEEDS NEW TASK [flag to get new task]
		   ORDER: 1, 2, 3, 4, 5, 3, 4, 1, 5, 1, 3, 5...
    **/
	
	function chooseNewTask(rm) {
    
	//console.log('11')
	
		// takes a room object 
		// returns a worker task number
		// based on how many other workers had that task last turn
		// ORDER: 1, 2, 3, 4, 5, 3, 4, 1, 5, 1, 3, 5...
		
		if (rm.memory.upgradeWorkersLast == 0 && rm.memory.upgradeWorkers == 0) {
			// If no upgraders, don't let the controller downgrade
			return 1;
		}
		else if (rm.memory.repairRoadWorkersLast == 0 && rm.memory.repairRoadWorkers == 0) {
			// If no road work, put our tax dollars to work
			return 2;
		}
		else if (rm.memory.repairWorkersLast == 0 && rm.memory.repairWorkers == 0) {
			// If no handymen, call Hank
			return 3;
		}
		else if (rm.memory.buildUpgradeWorkersLast == 0 && rm.memory.buildUpgradeWorkers == 0) {
			// If no builders, who will catcall the women?
			return 4;
		}
		else if (rm.memory.buildRepairWorkersLast == 0 && rm.memory.buildRepairWorkers == 0) {
			// If no builders, who will catcall the women?
			return 5;
		}
		else if (rm.memory.repairWorkersLast <= 1 && rm.memory.repairWorkers <= 1) {
			// Only 1 handyman? better call Larry, Ya idiot!
			return 3;
		}
		else if (rm.memory.buildUpgradeWorkersLast <= 1 && rm.memory.buildUpgradeWorkers <= 1) {
			// Only 1 builder? You need at least 2 to cat call
			return 4;
		}
		else if (rm.memory.upgradeWorkersLast <= 1 && rm.memory.upgradeWorkers <= 1) {
			// Upgrading takes forever, give him some help
			return 1;
		}
		else if (rm.memory.buildRepairWorkersLast == 1 && rm.memory.buildRepairWorkers == 1) {
			// If no builders, who will catcall the women?
			return 5;
		}
		else if (rm.memory.upgradeWorkersLast <= 2 && rm.memory.upgradeWorkers <= 2) {
			// Upgrading takes forever, give him some help
			return 1;
		}
		else if (rm.memory.repairWorkersLast <= 2 && rm.memory.repairWorkers <= 2) {
			// Only 1 handyman? better call Larry, Ya idiot!
			return 3;
		}
		else {
			// Put everything on Builders, Daddy needs a new power spawner
			return 5;
		}
	};

	function addWorkerCounter(room, task) { 
    
	//console.log('12')
	
		// increments the room's worker task counter
		switch (task) {
			case 1:
				room.memory.upgradeWorkers ++;
				break;
			case 2:
				room.memory.repairRoadWorkers ++;
				break;
			case 3:
				room.memory.repairWorkers ++;
				break;
			case 4:
				room.memory.buildUpgradeWorkers ++;
				break;
			case 5:
				room.memory.buildRepairWorkers ++;
				break;
		}
	}
	
	function removeWorkerCounter(room, task) {
    
	//console.log('13')
	
		// decrements the room's worker task counter
		switch (task) {
			case 1:
				room.memory.upgradeWorkers --;
				break;
			case 2:
				room.memory.repairRoadWorkers --;
				break;
			case 3:
				room.memory.repairWorkers --;
				break;
			case 4:
				room.memory.buildUpgradeWorkers --;
				break;
			case 5:
				room.memory.buildRepairWorkers --;
				break;
		}
	}
	
	function getRoadId (creep, room) {
    
	//console.log('14')
	
		// takes a creep and room
		// returns a road id to be repaired
		var target = null;
		var list = room.memory.repairSitesRoads;
		for (var nam in list) {
			var struct = list[nam];
			if (struct.hitsMax - struct.hits >= 100) {
				target = struct.id;
			}
		}
		if (! target) {
		    target = getRepairSiteId(creep, room);
		}
		return target;
	}
	
	function getRepairSiteId (creep, room) {
    
	//console.log('15')
	
		// takes a creep and room
		// returns a structure id to be repaired
		var target = null;
		var list = room.memory.repairSites;
		if (list.length == 0) {
			return target;
		}
		else if (list.length <= 4) {
			return list[0];
		}
		else {
			var rand = _.random(0,3);
			return list[rand];
		}
		return target;
	}
	
	function getConstructionSiteId (creep, room) {
    
	//console.log('16')
	
		// takes a creep and room
		// returns a construction site id 
		// prioritizes walls and ramparts, otherwise FIFO
		// returns null if none
		var target = null;
		if (room.memory.constructionSitesWalls.length > 0) {
			target = room.memory.constructionSitesWalls[0];
		}
		else if (room.memory.constructionSites.length > 0) {
			target = room.memory.constructionSites[0];
		}
		return target;
	}

	function runRepair(creep) {
    
	//console.log('17')
	
		// Runs the repair/ move to repair script
		var struct = Game.getObjectById(creep.memory.dest);
		if (struct && creep.pos.getRangeTo(struct) <= 3) {
			if (struct.hitsMax - struct.hits < 300 || Memory.tickCounter % 25 == 0) {
				creep.memory.dest = null;
			}
		}
		else {
			creep.moveTo(struct);
		    creep.repair(struct);
		}
		creep.repair(struct);
	}
	
	function runBuild(creep, room) {
    
	//console.log('18')
	
		// Runs the build/ move to construction site script
		var site = Game.getObjectById(creep.memory.dest);
		var blah = creep.build(site);
		if (blah == ERR_NOT_IN_RANGE) {
			// not close enough, get there
			creep.moveTo(site);
			creep.build(site);
		}
		else if (blah != 0) {
			// if there was any other error
			creep.memory.dest = getConstructionSiteId (creep, room);
		}
	}
	
	
var roleWorker = {

    run: function(creep) {
    
	//console.log(creep.name)
	
		if (creep.memory.task >= 1) {
    
	//console.log('19')
	
			// increment the Worker Counter
			addWorkerCounter(creep.room, creep.memory.task);
		}
		
		if(creep.memory.task >= 1 && _.sum(creep.carry) < 10) { 
    
	//console.log('20')
	
			// if almost empty go get more
			removeWorkerCounter(creep.room, creep.memory.task);
			creep.memory.task = 0;
			creep.memory.dest = null;
		}
		else if (creep.memory.task == 0 && creep.carryCapacity - _.sum(creep.carry) < 10) {
    
	//console.log('21')
	
			// if almost full go drop off
			creep.memory.task = 10;
			creep.memory.dest = null;
		}
		else if (! Game.getObjectById(creep.memory.dest )) {
    
	//console.log('22')
	
			// if dest no longer exists, get a new one
			creep.memory.dest = null;
			if (creep.memory.task == undefined) {
    
	//console.log('22.5')
	
			    creep.memory.task = 0
			}
		}
		
		if(creep.memory.task == 0) {
    
	//console.log('23')
	
			// Go pick-up
			
			if(creep.memory.dest) {
    
	//console.log('24')
	
				// Knows where to go
				
				var destination = Game.getObjectById(creep.memory.dest);
				if (creep.pos.isNearTo(destination)) {
    
	//console.log('25')
	
					// We're here
					creep.withdraw (destination, RESOURCE_ENERGY);
					creep.pickup(destination);
					creep.memory.dest = null;
					
                    var droppedEn = creep.pos.findInRange(FIND_DROPPED_ENERGY, 1);
                    if(droppedEn.length > 0) { // Some dropped energy is near
        

        
                        var target = otherFunctions.highestEnergy(droppedEn);
                        creep.pickup(target);
                        creep.memory.dest = null;
                    }
				}
				else { // Gotta keep moving
    
	//console.log('26')
	
					
					if (creep.moveTo(destination) != 0) { 
    
	//console.log('27')
	
						// Destination nonexistant, get a new one
						creep.memory.dest = null;
					}
					else { // Moved successfully, try picking up
    
	//console.log('28')
	
						var picked = creep.pickup(destination);
						var withdrawn = creep.withdraw (destination, RESOURCE_ENERGY);
						
						if (withdrawn == 0 || picked == 0 ) {
    
	//console.log('29')
	
							// if either worked, get a new dest
							creep.memory.dest = null;
						}
					}
				}
				var withdrawn = creep.withdraw (destination, RESOURCE_ENERGY);
			}
			
			if(!creep.memory.dest) {
    
	//console.log('30')
	
				// Needs to get a new dest
				var target = otherFunctions.findContainer(creep.room.memory.containers, creep.pos, creep.room);
				
				if(! target) {
				    target = otherFunctions.findDroppedEnergyNearSources(creep.room.memory.allSources, creep.pos);
				    creep.memory.noContainers = true;
				}
				else {
				    creep.memory.noContainers = false;
				}
    
	//console.log('Target ID: ' + target.id + typeof(target))
	
				if (creep.room.storage) { // if this room has storage
    
	//console.log('31')
	
					if (! target || target.store[RESOURCE_ENERGY] < 100 || .15 < Math.random()) {
    
	//console.log('32')
	
						target = creep.room.storage
					}
				}
				creep.moveTo(target);
				creep.withdraw (target, RESOURCE_ENERGY);
				if (target) {
				    creep.memory.dest = target.id;
				}
			}
		}
		
		
		if (creep.memory.task == 10) {
    
	//console.log('33')
	
			// Needs a new work task
			creep.memory.dest = null;
			creep.memory.task = chooseNewTask(creep.room);
			addWorkerCounter(creep.memory.task);
		}
		
		if (creep.memory.task != 0 && !creep.memory.dest) {
    
	//console.log('34')
	
			// Needs to get a new dest
			switch (creep.memory.task) {
				
				case 1: // go get controller as dest
    
	//console.log('35')
	
					creep.memory.dest = (creep.room.controller.id);
					break;
				
				case 2: // go get road as dest
    
	//console.log('36')
	
					creep.memory.dest = getRoadId(creep, creep.room);
					if (creep.memory.dest == null) {
						creep.memory.task = 5;
					}
					break;
				
				case 3: // go get repair site as dest
    
	//console.log('37')
	
					creep.memory.dest = getRepairSiteId(creep, creep.room);
					if (creep.memory.dest == null) {
						creep.memory.task = 4;
					}
					break;
				
				case 4: // go get construction site as dest (or upgrade if room.memory.constructionSites.length == 0)
    
	//console.log('38')
	
					creep.memory.dest = getConstructionSiteId(creep, creep.room);
					if (creep.memory.dest == null) {
    
	//console.log('39')
	
						creep.memory.dest = creep.room.controller.id;
					}
					break;
				
				case 5: // go get construction site as dest (or repair if room.memory.constructionSites.length == 0)
    
	//console.log('40')
	
					creep.memory.dest = getConstructionSiteId(creep, creep.room);
					if (creep.memory.dest == null) {
    
	//console.log('41')
	
						creep.memory.dest = getRepairSiteId(creep, creep.room);
					}
					break;
				
			}
			
		}
		
		switch(creep.memory.task) { // What are we gonna do today?
									// Whatch'ya doin' ?
									
			case 1: 
    
	//console.log('42')
	
				// Upgrade
				if (creep.upgradeController(creep.room.controller) == ERR_NOT_IN_RANGE) {
					creep.moveTo(creep.room.controller);
					creep.upgradeController(creep.room.controller);
				}
				break;
			
			case 2: 
    
	//console.log('43')
	
				// Road Repair
				var road = Game.getObjectById(creep.memory.dest);
				if (! road || road.structureType != STRUCTURE_ROAD) {
					creep.memory.dest = null;
					creep.memory.task = 4;
					break;
				}
				if (road && creep.pos.getRangeTo(road) <= 3) {
					if (road.hitsMax - road.hits < 300) {
						creep.memory.dest = null;
					}
					creep.repair(road);
				}
				else {
					var targets = creep.pos.findInRange(FIND_STRUCTURES, 2, { filter: (structure) => { return (structure.structureType == STRUCTURE_ROAD && 
						structure.hitsMax - structure.hits >= 400)} });
					if (targets.length > 0) {
						creep.repair(targets[0]);
					} 
					creep.moveTo(road);
				}
				break;
			
			case 3: 
    
	//console.log('44')
	
				// Repair
				runRepair(creep);
				break;
			
			case 4: 
    
	//console.log('45')
	
				// Build-Upgrade
				if (creep.memory.dest != creep.room.controller.id) {
					// if dest is not the controller, then build
					runBuild(creep, creep.room);
				}
				else { // are doin' the controller thing
					if (Memory.tickCounter % 25 == 2 && creep.room.memory.constructionSites.length + creep.room.memory.constructionSitesWalls.length > 0) {
						// Check if there's some new construction sites every once and a while
						creep.memory.dest = getConstructionSiteId(creep, creep.room);
						creep.moveTo(Game.getObjectById(creep.memory.dest));
					}
					if (creep.upgradeController(creep.room.controller) == ERR_NOT_IN_RANGE) {
						creep.moveTo(Game.getObjectById(creep.memory.dest));
						creep.upgradeController(creep.room.controller);
					}
					else {
						creep.moveTo(Game.getObjectById(creep.memory.dest));
						creep.upgradeController(creep.room.controller);
					}
				}
				break;
			
			case 5: 
    
	//console.log('46')
	
				// Build-Repair
				var struct = (Game.getObjectById(creep.memory.dest))
				if (struct && struct.progressTotal) {
					// If it's a construction site, then build
					runBuild(creep, creep.room);
				}
				else {
					runRepair(creep);
					if (Memory.tickCounter % 25 == 6 && creep.room.memory.constructionSites.length + creep.room.memory.constructionSitesWalls.length > 0) {
						// Check if there's some new construction sites every once and a while
						creep.memory.dest = getConstructionSiteId(creep, creep.room);
						creep.moveTo(Game.getObjectById(creep.memory.dest));
					}
				}
				break;
			
			case 10: 
    
	//console.log('47')
	
				// somehow needs another task despite above lines
				creep.memory.dest = null;
				creep.memory.task = chooseNewTask(creep.room);
				addWorkerCounter(creep.memory.task);
				
				break;
			
			case 0: 
    
	//console.log('48')
	
				// Check if still empty, does it need to switch tasks?
				if (creep.carryCapacity - _.sum(creep.carry) <= 15){
					creep.memory.task = 10;
					creep.memory.dest = null;
				}
			
				break;
		}

    }
};
module.exports = roleWorker;
