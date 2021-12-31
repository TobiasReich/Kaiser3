package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.player.WarDeclarationMessage

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

    fun declareWar(initiator : Player, target : Player, units : Map<MilitaryUnit, Int>){
        warDeclarations.add(WarDeclaration(initiator, target, units))
        target.addMessage(WarDeclarationMessage(initiator, units))
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
    fun estimateBattleOutcome(ownMilitary : Map<MilitaryUnit, Int>, otherMilitary : Map<MilitaryUnit, Int>) : BattleOutcome {
        var ownPower = 0.0
        var otherPower = 0.0

        ownMilitary.keys.forEach {
            val unitPower = it.power * (ownMilitary[it] ?: 0).toDouble()
//            println("$it power: $unitPower")
            ownPower +=  unitPower
        }

        otherMilitary.keys.forEach {
            val unitPower = it.power * (otherMilitary[it] ?: 0).toDouble()
//            println("$it power: $unitPower")
            otherPower +=  unitPower
        }

//        println("Own Power: $ownPower")
//        println("Other Power: $otherPower")

        val estimate = when{
            ownPower > otherPower * 5 -> { BattleOutcome.EASY_VICTORY }    // Having more than 5 times the other's troops
            ownPower > otherPower * 2 -> { BattleOutcome.LIKELY_VICTORY }  // Having more than twice the other's troops
            ownPower * 5 < otherPower -> { BattleOutcome.SURE_LOSS }       // Having less than a 5th of the other's troops
            ownPower * 2 < otherPower -> { BattleOutcome.POTENTIAL_LOSS }  // Having less than half the other's troops
            else -> { BattleOutcome.INDECISIVE }                           // All else is indecisive
        }

        return estimate
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
data class WarDeclaration(val initiator : Player, val target : Player, val units : Map<MilitaryUnit, Int>)