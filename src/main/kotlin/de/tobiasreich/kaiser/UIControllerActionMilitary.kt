package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.MilitaryUnitType
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionMilitary : Initializable {

    @FXML
    lateinit var unitVisualization: HBox

    @FXML
    private lateinit var unitList: VBox

    @FXML
    fun onButtonSomethingClick(actionEvent: ActionEvent) {
        //Some button clicked
    }

    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    override fun initialize(p0: URL?, p1: ResourceBundle?) {

        val newUnitCallback = object : (MilitaryUnitType, Boolean) -> Boolean {
            override fun invoke(unit: MilitaryUnitType, wantsToPurchase: Boolean) : Boolean {
                var success = false
                if(wantsToPurchase){
                    Game.currentPlayer.money -= unit.mercCost
                    Game.currentPlayer.addMilitaryUnit(MilitaryUnit(unit, unit.pay, unit.melee, unit.ranged, unit.initiative, unit.power, unit.health, unit.prestige, unit.loyalty))
                    updateCallback()
                    success = true
                } else {
                    val population = Game.currentPlayer.population
                    if (population.adults.size > unit.popCost) {
                        Game.currentPlayer.money -= unit.recruitCost
                        population.removeAdults(unit.popCost)
                        Game.currentPlayer.land.buildings.updateUsedBuildings(population)
                        Game.currentPlayer.addMilitaryUnit(MilitaryUnit(unit, unit.pay, unit.melee, unit.ranged, unit.initiative, unit.power, unit.health, unit.prestige, unit.loyalty))
                        updateCallback()
                        success = true
                    }
                    updateCallback()
                }
                drawUnits()
                return success
            }
        }

        // Draw military units
        MilitaryUnitType.values().forEach {
            val unitView = UIControllerViewMilitaryRecruitUnit(it, Game.currentPlayer.population, Game.currentPlayer.military, newUnitCallback)
            unitList.children.add(unitView)
        }

        drawUnits()

    }

    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }

    private fun drawUnits(){
        unitVisualization.children.clear()
        val military = Game.currentPlayer.military

        military.keys.sorted().forEach { unitType ->

            for (unit in 0 until military[unitType]!!.size){
                println("Unit: $unitType")
                val imageView = ImageView()
                imageView.fitWidth = 40.0
                imageView.fitHeight = 40.0
                imageView.image = when(unitType) {
                    MilitaryUnitType.WARRIOR -> { GameImageCache.warrior }
                    MilitaryUnitType.ARCHER -> { GameImageCache.archer }
                    MilitaryUnitType.SPEARMAN -> { GameImageCache.spearman}
                    MilitaryUnitType.CAVALRY -> { GameImageCache.cavalry}
                    MilitaryUnitType.CROSSBOW -> { GameImageCache.warrior}
                    MilitaryUnitType.PIKE -> {GameImageCache.warrior}
                    MilitaryUnitType.LANCER -> {GameImageCache.lancer}
                    MilitaryUnitType.LONGSWORD -> {GameImageCache.warrior}
                    MilitaryUnitType.CANNON -> {GameImageCache.cannon}
                    MilitaryUnitType.KNIGHT -> {GameImageCache.warrior}
                    MilitaryUnitType.CRUSADER -> {GameImageCache.warrior}
                    MilitaryUnitType.MUSKETEER -> {GameImageCache.warrior}
                    MilitaryUnitType.ARTILLERY -> {GameImageCache.warrior}
                }

                unitVisualization.children.add(imageView)
            }

        }
    }

    companion object{

    }

}