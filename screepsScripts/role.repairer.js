/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.repairer');
 * mod.thing == 'a thing'; // true
 */

var roleRepairer = {

    //Line 61 sources[1]

    /** @param {Creep} creep **/

    run: function(creep) {



        if(creep.memory.repairing && creep.carry.energy == 0) {

            creep.memory.repairing = false;

            creep.say('harvesting');

        }

        if(!creep.memory.repairing && creep.carry.energy == creep.carryCapacity) {

            creep.memory.repairing = true;

            creep.say('repairing');

        }



        if(creep.memory.repairing) {
            var targets = creep.room.find(FIND_STRUCTURES, {
            filter: object => object.hits < object.hitsMax});

            targets.sort((a,b) => a.hits - b.hits);


            if(targets.length > 0) {

                if(creep.repair(targets[0]) == ERR_NOT_IN_RANGE) {

                    creep.moveTo(targets[0]);

                }

            }

        }

        else {

            if(creep.harvest(Game.getObjectById('cd3f6eb55d5a28e569cbf18b')) == ERR_NOT_IN_RANGE) {

                creep.moveTo(Game.getObjectById('cd3f6eb55d5a28e569cbf18b'));

            }

        }

    }

};



module.exports = roleRepairer;
