package de.tobiasreich.kaiser.game.data.player

import de.tobiasreich.kaiser.Main
import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.ResourceType
import de.tobiasreich.kaiser.game.TreatyType
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import de.tobiasreich.kaiser.game.data.country.HarvestEvent
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.MilitaryUnitType
import de.tobiasreich.kaiser.game.data.military.SabotageType
import de.tobiasreich.kaiser.game.data.military.WarGoal
import de.tobiasreich.kaiser.game.data.population.Person
import javafx.fxml.FXMLLoader

interface ReportMessage{
    fun getViewLoader(): FXMLLoader
}

/** Population changes (birth, death, migration...) */
class PopulationReport(val birth : Int, val diedOfAge : Int, val diedOfHealth : Int, val immigrated : Int,
                       val emigrated : Int, val starvedToDeath : Int, val totalChange : Int) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-population.fxml"), Game.resourcesBundle)
    }
}

/** Harvest news. Was it a good or a bad one? */
class HarvestReport(val harvest: HarvestCondition, val harvestedFood : Int, val totalFood : Int, val harvestEvent : HarvestEvent?) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-harvest.fxml"), Game.resourcesBundle)
    }
}

/** A donation from another player. */
class DonationMessage(val donatingPlayer: Player, val selectedResource: ResourceType, val donationAmount: Int, val people : List<Person>?) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-donation.fxml"), Game.resourcesBundle)
    }
}

/** A reaction to your donation. Can be accepted or rejected. */
class DonationReactionMessage(val respondingPlayer: Player, val selectedResource: ResourceType, val donationAmount: Int, val people : List<Person>?, val accepted : Boolean) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-donation-reaction.fxml"), Game.resourcesBundle)
    }
}

/** A message about "sabotage"
 *  TODO: Might be more interesting when this could occur randomly, too so it is not always obvious that this was sabotage
 */
class SabotageMessage(val sabotagingPlayer: Player, val sabotageType: SabotageType) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-sabotage.fxml"), Game.resourcesBundle)
    }
}

/** A message about a war declaration */
class WarDeclarationMessage(val declaringPlayer: Player, val units : Map<MilitaryUnitType, MutableList<MilitaryUnit>>, val warGoal: WarGoal) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-war-declaration.fxml"), Game.resourcesBundle)
    }
}

/** A message about a reaction to a war declaration (i.e. a peace treaty)
 *  Note: this message includes the returning units. These are needed so the player - should the peace treaty be accepted -
 *  can re-include the own units again.
 */
class WarDeclarationReactionMessage(val reactingPlayer: Player, val peaceOfferAmount : Int, val returningUnits : Map<MilitaryUnitType, MutableList<MilitaryUnit>>, val warGoal: WarGoal) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-war-declaration-reaction.fxml"), Game.resourcesBundle)
    }
}

/** A message about a troops coming back from the battlefield  */
class ReturningTroopsMessage(val originPlayer: Player, val returningUnits : Map<MilitaryUnitType, MutableList<MilitaryUnit>>,
                             val warGoal: WarGoal?, val victoryAmount : Int, val slaves: List<Person>?) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-returning-troops.fxml"), Game.resourcesBundle)
    }
}

/** A message about a troops coming back from the battlefield  */
class BattleMessage(val attackingPlayer: Player, val attackingUnits : Map<MilitaryUnitType, MutableList<MilitaryUnit>>,
                    val defendingPlayer: Player, val warGoal: WarGoal) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-battle.fxml"), Game.resourcesBundle)
    }
}

/** A message about an outcome of a battle, sent do the defender (the attacker sees the battle directly in the turn)
 *  @param attackingPlayer - the player who was leading the attack
 *  @param remainingPowerFraction - the fraction of the units coming back (e.g. 0.5 means only 50% of the units came back from war)
 *  @param warGoal - the goal of the attacker in order to present what happened
 *  @param attackerVictory - whether the attacker won the battle or not (thus true means the message means a loss to the defender)
 *  @param victoryValue - the amount of "resources" robbed by the attacker
 */
class BattleOutcomeMessage(val attackingPlayer: Player, val remainingPowerFraction : Double,
                           val warGoal : WarGoal, val attackerVictory : Boolean, val victoryValue: Int, val slaves : List<Person>?) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-battle-outcome.fxml"), Game.resourcesBundle)
    }
}

/** A message about a treaty offer (any type) */
class TreatyOfferMessage(val requestingPlayer: Player, val type : TreatyType) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-treaty-offer.fxml"), Game.resourcesBundle)
    }
}