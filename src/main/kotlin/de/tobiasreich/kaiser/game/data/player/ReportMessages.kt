package de.tobiasreich.kaiser.game.data.player

import de.tobiasreich.kaiser.Main
import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.ResourceType
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import de.tobiasreich.kaiser.game.data.country.HarvestEvent
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