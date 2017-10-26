/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.upgrader');
 * mod.thing == 'a thing'; // true
 */

var roleUpgrader = {

    //Line 53 sources[1] instead of sources[0]
    

    /** @param {Creep} creep **/

    run: function(creep) {



        if(creep.memory.upgrading && creep.carry.energy == 0) {

            creep.memory.upgrading = false;

            creep.say('harvesting');

        }

        if(!creep.memory.upgrading && creep.carry.energy == creep.carryCapacity) {

            creep.memory.upgrading = true;

            creep.say('upgrading');

        }



        if(creep.memory.upgrading) {

            if(creep.upgradeController(creep.room.controller) == ERR_NOT_IN_RANGE) {

                creep.moveTo(creep.room.controller);

            }

        }

        else {


            if(creep.harvest(Game.getObjectById('cd3f6eb55d5a28e569cbf18b')) == ERR_NOT_IN_RANGE) {

                creep.moveTo(Game.getObjectById('cd3f6eb55d5a28e569cbf18b'));

            }

        }

    }

};



module.exports = roleUpgrader;