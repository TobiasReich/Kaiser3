package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionWar : Initializable {

    @FXML
    lateinit var unitsToWarVisualization: HBox

    @FXML
    lateinit var unitsAtHomeVisualization: HBox

    @FXML
    fun onButtonSomethingClick(actionEvent: ActionEvent) {
        //Some button clicked
    }

    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    private var miliartyAtHome = mutableMapOf<MilitaryUnit, Int>()
    private var miliartyAtWar = mutableMapOf<MilitaryUnit, Int>()


    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        //Copy all
        miliartyAtHome = HashMap(Game.currentPlayer.miliarty)

        drawUnitsAtHome()
        drawUnitsToWar()
    }

    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }

    private fun drawUnitsAtHome(){
        unitsAtHomeVisualization.children.clear()

        miliartyAtHome.keys.forEach { unitType ->

            for (unit in 0 until (miliartyAtHome[unitType] ?: 0)){
                println("Unit: $unitType")
                val imageView = ImageView()
                imageView.fitWidth = 40.0
                imageView.fitHeight = 40.0
                imageView.image = when(unitType) {
                    MilitaryUnit.WARRIOR -> { GameImageCache.warrior }
                    MilitaryUnit.ARCHER -> { GameImageCache.archer }
                    MilitaryUnit.SPEARMAN -> { GameImageCache.spearman}
                    MilitaryUnit.CAVALRY -> { GameImageCache.cavalry}
                    MilitaryUnit.CROSSBOW -> { GameImageCache.warrior}
                    MilitaryUnit.PIKE -> {GameImageCache.warrior}
                    MilitaryUnit.LANCER -> {GameImageCache.lancer}
                    MilitaryUnit.LONGSWORD -> {GameImageCache.warrior}
                    MilitaryUnit.CANNON -> {GameImageCache.cannon}
                    MilitaryUnit.KNIGHT -> {GameImageCache.warrior}
                    MilitaryUnit.CRUSADER -> {GameImageCache.warrior}
                    MilitaryUnit.MUSKETEER -> {GameImageCache.warrior}
                    MilitaryUnit.ARTILLERY -> {GameImageCache.warrior}
                }

                unitsAtHomeVisualization.children.add(imageView)
            }
        }
    }


    private fun drawUnitsToWar(){
        unitsToWarVisualization.children.clear()

        miliartyAtWar.keys.forEach { unitType ->

            for (unit in 0 until (miliartyAtWar[unitType] ?: 0)){
                println("Unit: $unitType")
                val imageView = ImageView()
                imageView.fitWidth = 40.0
                imageView.fitHeight = 40.0
                imageView.image = when(unitType) {
                    MilitaryUnit.WARRIOR -> { GameImageCache.warrior }
                    MilitaryUnit.ARCHER -> { GameImageCache.archer }
                    MilitaryUnit.SPEARMAN -> { GameImageCache.spearman}
                    MilitaryUnit.CAVALRY -> { GameImageCache.cavalry}
                    MilitaryUnit.CROSSBOW -> { GameImageCache.warrior}
                    MilitaryUnit.PIKE -> {GameImageCache.warrior}
                    MilitaryUnit.LANCER -> {GameImageCache.lancer}
                    MilitaryUnit.LONGSWORD -> {GameImageCache.warrior}
                    MilitaryUnit.CANNON -> {GameImageCache.cannon}
                    MilitaryUnit.KNIGHT -> {GameImageCache.warrior}
                    MilitaryUnit.CRUSADER -> {GameImageCache.warrior}
                    MilitaryUnit.MUSKETEER -> {GameImageCache.warrior}
                    MilitaryUnit.ARTILLERY -> {GameImageCache.warrior}
                }

                unitsToWarVisualization.children.add(imageView)
            }
        }
    }

    companion object{

    }

}