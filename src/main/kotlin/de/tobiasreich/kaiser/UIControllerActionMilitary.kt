package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionMilitary : Initializable {

    @FXML
    private lateinit var unitList: VBox

    @FXML
    fun onButtonSomethingClick(actionEvent: ActionEvent) {
        //Some button clicked
    }

    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    override fun initialize(p0: URL?, p1: ResourceBundle?) {

        val newUnitCallback = object : (MilitaryUnit, Boolean) -> Boolean {
            override fun invoke(unit: MilitaryUnit, wantsToPurchase: Boolean) : Boolean {
                if(wantsToPurchase){
                    Game.currentPlayer.money -= unit.mercCost
                    Game.currentPlayer.addMilitaryUnit(unit)
                    updateCallback()
                    return true
                } else {
                    val population = Game.currentPlayer.population
                    if (population.adults.size > unit.popCost) {
                        Game.currentPlayer.money -= unit.recruitCost
                        population.removeAdults(unit.popCost)
                        Game.currentPlayer.land.buildings.updateUsedBuildings(population)
                        Game.currentPlayer.addMilitaryUnit(unit)
                        updateCallback()
                        return true
                    }
                    updateCallback()
                    return false
                }
            }
        }

        MilitaryUnit.values().forEach {
            val unitView = UIControllerViewMilitaryRecruitUnit(it, Game.currentPlayer.population, Game.currentPlayer.miliarty, newUnitCallback)
            unitList.children.add(unitView)
        }
    }

    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }
}