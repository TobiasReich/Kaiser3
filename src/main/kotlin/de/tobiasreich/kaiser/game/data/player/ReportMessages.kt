package de.tobiasreich.kaiser.game.data.player

import de.tobiasreich.kaiser.Main
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import javafx.fxml.FXMLLoader

interface ReportMessage{
    fun getViewLoader(): FXMLLoader
}

/** Population changes (birth, death, migration...) */
class PopulationReport(val birth : Int, val diedOfAge : Int, val diedOfHealth : Int, val immigrated : Int, val emigrated : Int, val totalChange : Int) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-population.fxml"))
    }
}

/** Harvest news. Was it a good or a bad one? */
class HarvestReport(val harvest: HarvestCondition) : ReportMessage {
    override fun getViewLoader(): FXMLLoader {
        return FXMLLoader(Main::class.java.getResource("news-harvest.fxml"))
    }
}