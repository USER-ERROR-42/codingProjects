/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.harvester');
 * mod.thing == 'a thing'; // true
 */

var roleHarvester = {



    /** @param {Creep} creep **/

    run: function(creep) {

        if(creep.carry.energy < creep.carryCapacity) {


            if(creep.harvest(Game.getObjectById('d1f6a90b959a24b19b33c55b')) == ERR_NOT_IN_RANGE) {

                creep.moveTo(Game.getObjectById('d1f6a90b959a24b19b33c55b'));

            }

        }

        else {

            var targets = creep.room.find(FIND_STRUCTURES, {

                    filter: (structure) => {

                        return (structure.structureType == STRUCTURE_EXTENSION ||

                                structure.structureType == STRUCTURE_SPAWN ||

                                structure.structureType == STRUCTURE_TOWER) && structure.energy < structure.energyCapacity;

                    }

            });

            if(targets.length > 0) {
                var target = creep.pos.findClosestByRange(targets);
                if(creep.transfer(target, 'energy') == ERR_NOT_IN_RANGE) {

                    creep.moveTo(target);

                }

            }

        }

    }

};



module.exports = roleHarvester;