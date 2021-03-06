package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.game.data.player.TreatyExpirationMessage
import de.tobiasreich.kaiser.game.data.player.TreatyOfferMessage
import de.tobiasreich.kaiser.game.data.player.TreatyOfferResponseMessage
import de.tobiasreich.kaiser.game.data.player.TreatyRumorsMessage

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

    // Treaties are valid for 10 years (for now)
    const val TREATY_EXPIRATION_TIME_YEARS = 10

    // List of Treaties (can be of all type). Use a filter if you want specific ones.
    private val acceptedTreaties = mutableListOf<Treaty>()

    // A list of proposals that still need to be accepted by a player
    private val treatyProposals = mutableListOf<Treaty>()

    /** This makes a treaty proposal sent to the receiver of that treaty as a "message".
     *  That player then can accept or decline it. */
    fun addProposal(treaty : Treaty){
        treaty.receiver.addMessage(TreatyOfferMessage(treaty))
        treatyProposals.add(treaty)
    }


    /** Accepts a proposal that was made by another player.
     *  This automatically deletes the proposal and sends a message to the initiator of the proposal. */
    fun acceptProposal(treaty: Treaty){
        // Add the treaty to the list
        println("accept treaty")
        acceptedTreaties.add(treaty)

        // Inform proposing player
        treaty.initiator.addMessage(TreatyOfferResponseMessage(treaty, true))

        // Inform all other players
        // TODO Consider not informing about all treaties. (maybe depending on the espionage level towards these players?)
        //if (treaty.type == TreatyType.PEACE || treaty.type == TreatyType.ALLIANCE) {
            val otherPlayers = Game.getAllOtherPlayers(setOf(treaty.initiator, treaty.receiver))
            otherPlayers.forEach {
                it.addMessage(TreatyRumorsMessage(treaty, true))
            }
        //}

        treatyProposals.remove(treaty)
    }


    /** Rejects a proposal that was made by another player.
     *  This means basically just deleting the proposal and sending an info message back to the initiator. */
    fun rejectProposal(treaty: Treaty){
        println("reject treaty")
        treatyProposals.remove(treaty)
        treaty.initiator.addMessage(TreatyOfferResponseMessage(treaty, false))
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

    /** Call this when ever a year passes.
     *  This will cleanup all treaties that have an expiration year equal or smaller the given one */
    fun cleanupTreaties(year : Int){
        val expiredTreaties = acceptedTreaties.filter { it.expirationYear <= year }

        expiredTreaties.forEach { treaty ->
            //1. Remove it from the acceptedTreaties
            acceptedTreaties.remove(treaty)
            //2. Send message to Initiator
            treaty.initiator.addMessage(TreatyExpirationMessage(treaty))
            //3. Send message to Receiver
            treaty.receiver.addMessage(TreatyExpirationMessage(treaty))

            // Inform all other players
            // TODO Consider not informing about all treaties. (maybe depending on the espionage level towards these players?)
            //if (treaty.type == TreatyType.PEACE || treaty.type == TreatyType.ALLIANCE) {
            val otherPlayers = Game.getAllOtherPlayers(setOf(treaty.initiator, treaty.receiver))
            otherPlayers.forEach {
                it.addMessage(TreatyRumorsMessage(treaty, false))
            }
            //}
        }
    }

    /** Checks if there is already a treaty of that type.
     *  This is defined as
     *  - both players must be involved
     *  - treaty type has to be the same
     *
     *  If includeProposals is true, it will also check proposed (but not yet accepted) treaties.
     *  Use this flag in order to avoid multiple identical treaty proposals */
    fun hasTreaty(player1 : Player, player2: Player, type : TreatyType, includeProposals : Boolean) : Boolean {
        if (acceptedTreaties.any {(it.initiator == player1 || it.receiver == player1) && (it.initiator == player2 || it.receiver == player2) && it.type == type } ){
            return true
        }
        if (includeProposals){
            if (treatyProposals.any {(it.initiator == player1 || it.receiver == player1) && (it.initiator == player2 || it.receiver == player2) && it.type == type } ){
                return true
            }
        }
        return false
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


data class Treaty(val type : TreatyType, val initiator : Player, val receiver : Player, val expirationYear : Int)

enum class TreatyType(val stringResource : String){
    PEACE("treaty_type_peace"),
    TRADE("treaty_type_trade"),
    ALLIANCE("treaty_type_alliance")
}
