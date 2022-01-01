package de.tobiasreich.kaiser.game.data.military


/** a military unit consists of the following propertiers:
 * - nameRes: I18N name resource
 * - techLevel: the players "technology" level (Coming from educating the people)
 * - pay: the costs per year for this unit
 * - mercCost: the price when purchasing as mercenaries
 * - recruitCost: the price when recruting as a soldier
 * - pooCost: the amount of population needed for recruting this unit
 * - melee: is this a melee fighter (e.g. knight)
 * - ranged: is this a ranged fighter (e.g. archer)
 * - initiative: how fast the unit is attacking. Units attack earlier in a battle with a higher value.
 * - power: the attack strength (higher is better)
 * - health: the health (higher is better)
 * - prestige: the prestige points given for owning such a unit (higher is better)
 * - loyalty: the default mood of this unit (higher is better), decides about deserting when making war
 */
enum class MilitaryUnitType(val nameRes : String, val techLevel : Int,
                            val pay : Int, val mercCost : Int, val recruitCost : Int,
                            val popCost : Int, val melee : Boolean, val ranged : Boolean,
                            val initiative : Double, val power : Double, val health : Double, val prestige : Double, val loyalty : Double) {

    WARRIOR("military_unit_warrior"     ,0     ,10  ,100    ,20     ,10 , true  , false ,0.80,0.10,0.20,0.10,0.9),
    ARCHER("military_unit_archer"       ,10    ,20  ,200    ,40     ,20 , false , true  ,0.95,0.15,0.10,0.20,0.7),
    SPEARMAN("military_unit_spearman"   ,25    ,10  ,250    ,50     ,40 , true  , false ,0.65,0.15,0.15,0.15,0.8),
    CAVALRY("military_unit_cavalry"     ,100   ,50  ,500    ,100    ,100, true  , false ,0.55,0.20,0.20,0.20,0.9),
    CROSSBOW("military_unit_crossbow"   ,200   ,100 ,1500   ,300    ,200, false , true  ,0.45,0.15,0.15,0.25,0.7),
    PIKE("military_unit_pike"           ,300   ,50  ,800    ,150    ,80 , true  , false ,0.40,0.25,0.25,0.20,0.8),
    LANCER("military_unit_lancer"       ,500   ,100 ,1200   ,200    ,200, true  , false ,0.50,0.30,0.25,0.30,0.9),
    LONGSWORD("military_unit_longsword" ,1000  ,500 ,2000   ,450    ,100, true  , false ,0.35,0.40,0.35,0.50,0.9),
    CANNON("military_unit_cannon"       ,2000  ,850 ,3000   ,1000   ,100, false , true  ,0.25,0.50,0.20,0.35,0.7),
    KNIGHT("military_unit_knight"       ,3000  ,1500,3000   ,1000   ,200, true  , false ,0.70,0.75,0.50,0.50,0.9),
    CRUSADER("military_unit_crusader"   ,5000  ,2000,5000   ,2000   ,200, true  , false ,0.75,0.80,0.60,0.75,1.0), //never surrender!
    MUSKETEER("military_unit_musketeer" ,10000 ,3000,7000   ,4000   ,500, true  , true  ,0.60,0.85,0.55,0.60,0.9), //only melee and ranged unint
    ARTILLERY("military_unit_artillery" ,20000 ,4000,10000  ,5000   ,500, false , true  ,0.30,0.70,0.25,0.50,0.7),

}

/** Simulating a single military unit. This is used for estimating specific changing properties of units.
 *  E.g. when during a battle units are hurt but not killed, their health shrings. Or when they went to a boot camp
 *  their morale might increase etc.
 *  This then is a unit specific property (i.e. a newly hired unit of same type has a lower power) */
data class MilitaryUnit(val type : MilitaryUnitType, var pay : Int, var melee : Boolean, var ranged : Boolean, var initiative : Double, var power : Double, var health : Double, var prestige : Double, var loyalty : Double)