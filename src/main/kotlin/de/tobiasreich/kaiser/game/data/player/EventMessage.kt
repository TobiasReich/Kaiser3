package de.tobiasreich.kaiser.game.data.player

import de.tobiasreich.kaiser.Main
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import javafx.fxml.FXMLLoader

interface EventMessage{

    fun getView(): FXMLLoader

}

/** Population changes (birth, death, migration...) */
class PopulationEvent(val birth : Int, val diedOfAge : Int, val diedOfHealth : Int, val immigrated : Int, val emigrated : Int) : EventMessage {
    override fun getView(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-population.fxml"))
    }
}

/** Harvest news. Was it a good or a bad one? */
class HarvestEvent(val harvest: HarvestCondition) : EventMessage {
    override fun getView(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-harvest.fxml"))
    }
}