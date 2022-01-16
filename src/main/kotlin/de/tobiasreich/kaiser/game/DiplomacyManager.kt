package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.MilitaryUnitType
import de.tobiasreich.kaiser.game.data.military.WarGoal
import de.tobiasreich.kaiser.game.data.player.ReturningTroopsMessage
import de.tobiasreich.kaiser.game.data.player.TreatyOfferMessage
import de.tobiasreich.kaiser.game.data.player.WarDeclarationMessage
import de.tobiasreich.kaiser.game.data.population.Person
import java.lang.Math.min

/** Object for tracking diplomatic relationships.
 *
 *  Players have the option for:
 *  - Peace treaties
 *    - Does not get enforced (You can still declare war) but a breaking of this will alarm all other leaders and make a bad reputation
 *  - Trade agreements
 *    - Allows sharing produced goods and therefore make money (for both countries)
 *  - Declare Alliances
 *    - Let every other leader know that these two countries are allies. Can be a warning for others - but also an invitation for war!
 *    //TODO If one country is target of a war declaration that has an alliance with others, the alliance partners should get notified as well.
 */
object DiplomacyManager {

    // List of Treaties (can be of all type). Use a filter if you want specific ones.
    private val acceptedTreaties = mutableListOf<Treaty>()

    // A list of proposals that still need to be accepted by a player
    private val treatyProposals = mutableListOf<Treaty>()

    /** This makes a treaty proposal sent to the receiver of that treaty as a "message".
     *  That player then can accept or decline it. */
    fun addProposal(treaty : Treaty){
        treaty.receiver.addMessage(TreatyOfferMessage(treaty.initiator, treaty.type))
        treatyProposals.add(treaty)
    }

    /** Accepts a proposal that was made by another player. */
    fun acceptProposal(treaty: Treaty){
        //TODO send a "Proposal Accepted Message" to the initiator

        // Add the treaty to the list
        acceptedTreaties.add(treaty)
    }

    /** Returns a list of all treaties the player is involved in (initiator and receiver) */
    fun getAllProposalsForPlayer(player: Player, type: TreatyType?) : List<Treaty> {
        return if (type != null){
            treatyProposals.filter { it.type == type && (it.initiator == player || it.receiver == player) }
        } else {
            treatyProposals.filter { it.initiator == player || it.receiver == player }
        }
    }

    /** Returns a list of all treaties that are proposed by / from this player */
    fun getAllCurrentTreatiesForPlayer(player: Player, type: TreatyType?) : List<Treaty> {
        return if (type != null){
            acceptedTreaties.filter { it.type == type && (it.initiator == player || it.receiver == player) }
        } else {
            acceptedTreaties.filter { it.initiator == player || it.receiver == player }
        }
    }

    /** Cancels a treaty with another player
     *
     *  TODO Think about consequences and treaty default durations
     *  E.g. Treaties could always last for 10 years and are not cancellable.
     *  This could give it another layer of strategic depth since a player is
     *  bound to it (or must break it) instead of cancelling it directly before war.  */
    fun cancelTreaty(treaty: Treaty){
        //TODO think about it!
    }

}


data class Treaty(val type : TreatyType, val initiator : Player, val receiver : Player)

enum class TreatyType(val stringResource : String){
    PEACE("treaty_type_peace"),
    TRADE("treaty_type_trade"),
    ALLIANCE("treaty_type_alliance")
}
