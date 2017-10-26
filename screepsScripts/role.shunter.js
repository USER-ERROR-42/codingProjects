/*
 * Module code goes here. Use 'module.exports' to export things:
 * module.exports.thing = 'a thing';
 *
 * You can import it from another modules like this:
 * var mod = require('role.shunter');
 * mod.thing == 'a thing'; // true
 */
 
// ***************************************************************************************************************************
// ****************************************************UNFINISHED*************************************************************
// ***************************************************************************************************************************

var role = {

    /** Shunter: Quickly picks up energy on the ground near its assigned source
                    Puts it in a nearby spawn, extension, container, or storage in that order
    **/
    
    highestDroppedEnergy: function(list) {
        // takes an array of dropped energy objects
        // returns the highest one
        var obj = list[0];
        for (var c = 1; c < list.length; c++) {
            if (list[c].energy > obj.energy){
                obj = list[c];
            }
        }
        return obj;
    }
    
    targetSelect: function(list) {
        // takes an array of my structures
        // returns a nonfull one among 
        // spawn, extension, container, or storage (in order)
        var target;
        for (var c = 0; c < list.length; c ++) {
            if (list[c].) {
                
            }
        }
    }

    run: function(creep) {
        
        if (creep.energy == 0) {
            creep.memory.dropoff = false;
        }
        else if (creep.energy >= creep.carryCapacity) {
            creep.memory.dropoff = true;
        }
        
        if (creep.memory.dropoff) {
            // Go drop off
            var targets = creep.memory.source.pos.findInRange(FIND_MY_STRUCTURES, 2);
            if (targets.length > 0){
                target = targetSelect();
            }
        }
        else {
            // Go pickup
            var targets = creep.memory.source.pos.findInRange(FIND_DROPPED_ENERGY, 1);
                if (targets.length == 1) {
                    if (creep.pickup(targets[0]) == ERR_NOT_IN_RANGE){
                        creep.moveTo(targets[0]);
                    }
                }
                else if (targets.length > 1) {
                    var target = highestDroppedEnergy(targets);
                    if (creep.pickup(target) == ERR_NOT_IN_RANGE){
                        creep.moveTo(target);
                    }
                }
                else {
                    targets = creep.memory.source.pos.findInRange(FIND_DROPPED_ENERGY, 2);
                    if (targets.length > 0) {
                        var target = highestDroppedEnergy(targets);
                        if (creep.pickup(target) == ERR_NOT_IN_RANGE){
                            creep.moveTo(target);
                        }
                    }
                }
        }
    
};
module.exports = role;
