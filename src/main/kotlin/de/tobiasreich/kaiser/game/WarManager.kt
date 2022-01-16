package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.MilitaryUnitType
import de.tobiasreich.kaiser.game.data.military.WarGoal
import de.tobiasreich.kaiser.game.data.player.ReturningTroopsMessage
import de.tobiasreich.kaiser.game.data.player.WarDeclarationMessage
import de.tobiasreich.kaiser.game.data.population.Person
import java.lang.Math.min

/** Object for tracking war declarations.
 *  Every player that declares war to another player will add the war declarations here.
 *  They get resolved the next turn so
 *  A) the target player has a turn to prepare (e.g. recruit units) or
 *  B) make a peace treaty (e.g. "I give you money if you retreat your units")
 *  - This then, when accepted, automatically adds a peace treaty with that player.
 *
 *  That way we add more diplomacy among the players. */
object WarManager {

    private val warDeclarations = mutableListOf<WarDeclaration>()

    private val returningTroops = mutableListOf<TroopMovement>()

    /** This declares a war.
     *  - store that in the war declarations (so the next time this player starts the turn, we can solve it)
     *  - send a WarDeclarationMessage to the target of the war declaration
     *  TODO If the target of the war declaration has a peace treaty with the initiating player, warn all other players about the breach of peacy treaty!
     *  */
    fun declareWar(initiator : Player, target : Player, units : Map<MilitaryUnitType, MutableList<MilitaryUnit>>, warGoal: WarGoal){
        warDeclarations.add(WarDeclaration(initiator, target, units, warGoal))
        target.addMessage(WarDeclarationMessage(initiator, units, warGoal))
    }

    /** Gets the next war declaration FOR the given player. */
    fun getNextWarDeclarationForPlayer(player : Player) : WarDeclaration? {
        return warDeclarations.firstOrNull{it.target == player}
    }

    /** Gets the next war declaration FROM the given player. */
    fun getNextWarDeclarationFromPlayer(player : Player) : WarDeclaration? {
        return warDeclarations.firstOrNull{it.initiator == player}
    }

    /** This calculates the ROUGH chances of victory for the attacker.
     *  (For defense chances just use the opposite values)
     *  A value EASY_VICTORY or LIKELY_VICTORY suggests the attacker might win. But there is NO GUARANTE!
     *  This is just a guess by the "power" of all units.
     *  A good combination of units might still win (e.g. when they have a higher initiative and "shoot first")
     *
     *  A value of INDECISIVE suggests a "draw" meaning the outcome is insecure and both are equally likely to win.
     *
     *  POTENTIAL_LOSS and SURE_LOSS values however suggest, the attacker might lose the battle. */
    fun estimateBattleOutcome(ownMilitary : Map<MilitaryUnitType, MutableList<MilitaryUnit>>, otherMilitary : Map<MilitaryUnitType, MutableList<MilitaryUnit>>) : BattleOutcome {
        val ownPower = getTotalAttackPower(ownMilitary)
        val otherPower = getTotalAttackPower(otherMilitary)

        val estimate = when{
            ownPower > otherPower * 5 -> { BattleOutcome.EASY_VICTORY }    // Having more than 5 times the other's troops
            ownPower > otherPower * 2 -> { BattleOutcome.LIKELY_VICTORY }  // Having more than twice the other's troops
            ownPower * 5 < otherPower -> { BattleOutcome.SURE_LOSS }       // Having less than a 5th of the other's troops
            ownPower * 2 < otherPower -> { BattleOutcome.POTENTIAL_LOSS }  // Having less than half the other's troops
            else -> { BattleOutcome.INDECISIVE }                           // All else is indecisive
        }

        return estimate
    }

    /** Returns the attack power of an army */
    fun getTotalAttackPower(units: Map<MilitaryUnitType, MutableList<MilitaryUnit>>): Double {
        return units.keys.sumOf { type -> units[type]!!.sumOf { it.power } }
    }

    /** Returns the attack power of an army */
    fun getAttackPowerByType(melee: Boolean, units: Map<MilitaryUnitType, MutableList<MilitaryUnit>>): Double {
        val selectedUnits = if (melee) {
            units.keys.filter { it.melee }
        } else {
            units.keys.filter { it.ranged }
        }
        return selectedUnits.sumOf { type -> units[type]!!.sumOf { it.power } }
    }

    /** This "kills" units depending on the damage power they receive.
     *  This returns a Pair of two values.
     *  FIRST: The new units (survivors)
     *  SECOND: The amount of killed soldiers */
    fun takeDamage(attackedUnits : MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>, power : Double) : Pair<MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>, Int> {
        var attackPower = power
        var killedUnitsCounter = 0
        // For every unity type (sorted, beginning at the lowest)
        attackedUnits.keys.sorted().forEach { type ->
            val killedUnits = mutableListOf<MilitaryUnit>()
            val units = attackedUnits[type]!!
            // if there is still attack power left, reduce it from the unity health
            units.forEach { unit ->
                if (attackPower > 0){
                    val damageTagen = min(unit.health, attackPower)
                    unit.health -= damageTagen
                    attackPower -= damageTagen
                    if(unit.health <= 0){
                        println("Unit got killed $unit")
                        killedUnits.add(unit)
                    }
                }
            }
            units.removeAll(killedUnits)
            attackedUnits[type] = units

            // If there is no attack power left, we can just return the wounded (but surviving) units
            if (attackPower <= 0){
                return Pair(attackedUnits, killedUnits.size)
            } else {
                killedUnitsCounter += killedUnits.size
            }
        }
        return Pair(attackedUnits, killedUnitsCounter)
    }


    /** Adds a military group of troops to the returning troops.
     *  These are fetched at the beginning of a player's turn. */
    fun addTroopMovement(troops : TroopMovement){
        returningTroops.add(troops)
    }

    /** Returns a list of returning troops so the player can integrate them in their troops again
     *  NOTE: The returning troops are deleted afterwards! */
    fun getAllReturningTroops(destination: Player) : List<TroopMovement>{
        val troopMovements = returningTroops.filter{ it.destination == destination }
        returningTroops.removeAll(troopMovements) // Delete them, so they don't come home every turn!
        return troopMovements
    }


    /** This calculates troops potentially deserting the battlefield */
    fun calculateDesertingTroops(troops : MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>) : Pair<MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>, Int> {
        var desertedUnitsCounter = 0

        troops.keys.forEach { type ->
            val desertedUnits = mutableListOf<MilitaryUnit>()
            val units = troops[type]!!

            units.forEach { unit ->
                if (Math.random() > unit.loyalty){
                    desertedUnits.add(unit)
                }
            }

            troops[type]!!.removeAll(desertedUnits)
            desertedUnitsCounter += desertedUnits.size
        }

        return Pair(troops, desertedUnitsCounter)
    }

}


enum class BattleOutcome{
    EASY_VICTORY,
    LIKELY_VICTORY,
    INDECISIVE,
    POTENTIAL_LOSS,
    SURE_LOSS
}

/** Data object for tracking war declarations */
data class WarDeclaration(val initiator : Player, val target : Player, val units : Map<MilitaryUnitType, MutableList<MilitaryUnit>>, val warGoal: WarGoal)

/** Data object for troop movement.
 *  This is a bundle of units that will arrive at the destination (player) the next time it becomes the player's turn
 *  They can bring goods from the battlefield
 *  - Money & conquered Land will be integrated by the "victoryAmount"
 *  - Slaves will be the list of persons that gets added
 *
 *  The war goal defines what they were targeting. NULL means, they went home without any battle (e.g. peace was accepted)
 *
 *  NOTE: The reason for that is, so that on a peace treaty, the player does not receive the troops immediately.
 *  That gives another layer of diplomacy since other players might use the situation for their own goals and attack a
 *  player that is at war with another country. */
data class TroopMovement(val origin : Player, val destination : Player, val units : Map<MilitaryUnitType, MutableList<MilitaryUnit>>,
                         val warGoal: WarGoal?, val victoryAmount : Int, val slaves : List<Person>?){

    /** Creates a ReturningTroopsMessage out of a Troop Movement.
     *  This is needed since the troop movement, when arriving one turn later, becomes a Message for the turn start. */
    fun toReturningTroopsMessage() : ReturningTroopsMessage {
        return ReturningTroopsMessage(origin, units, warGoal, victoryAmount, slaves)
    }

}