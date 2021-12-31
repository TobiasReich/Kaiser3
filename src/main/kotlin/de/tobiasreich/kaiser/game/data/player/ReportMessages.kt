package de.tobiasreich.kaiser.game.data.player

import de.tobiasreich.kaiser.Main
import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.ResourceType
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import de.tobiasreich.kaiser.game.data.country.HarvestEvent
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.SabotageType
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
class WarDeclarationMessage(val declaringPlayer: Player, val units : Map<MilitaryUnit, Int>) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-war-declaration.fxml"), Game.resourcesBundle)
    }
}

/** A message about a reaction to a war declaration (i.e. a peace treaty)
 *  Note: this message includes the returning units. These are needed so the player - should the peace treaty be accepted -
 *  can re-include the own units again.
 */
class WarDeclarationReactionMessage(val reactingPlayer: Player, val peaceOfferAmount : Int, val returningUnits : Map<MilitaryUnit, Int>) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-war-declaration-reaction.fxml"), Game.resourcesBundle)
    }
}

/** A message about a troops coming back from the battlefield  */
class ReturningTroopsMessage(val originPlayer: Player, val returningUnits : Map<MilitaryUnit, Int>) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-returning-troops.fxml"), Game.resourcesBundle)
    }
}