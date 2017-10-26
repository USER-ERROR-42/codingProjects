/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.harvester2');
 * mod.thing == 'a thing'; // true
 */

var roleHarvester2 = {



    /** @param {Creep} creep **/

    run: function(creep) {

        if(creep.carry.energy < creep.carryCapacity) {

            if(creep.harvest(Game.getObjectById('70f27f0718fcf1bc4ac7a445')) == ERR_NOT_IN_RANGE) {

                creep.moveTo(Game.getObjectById('70f27f0718fcf1bc4ac7a445'));

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



module.exports = roleHarvester2;