package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.population.Population
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.layout.VBox
import java.io.IOException

/** This view shows the "purchase" option or one military unit.
 *  - name
 *  - pay per year
 *  - price as mercenary
 *  - button to hire as mercenary (expensive but no population cost)
 *  - price as soldier
 *  - population cost as soldier
 *  - button to "recruit" as soldier (cheaper but population cost)
 */
class UIControllerViewMilitaryRecruitUnit(private val unit : MilitaryUnit, private val population: Population) : VBox() {

    @FXML
    lateinit var unitTypeLabel: Label
    @FXML
    lateinit var payPerYearLabel: Label
    @FXML
    lateinit var hireCostLabel: Label
    @FXML
    lateinit var hireMercenaryButton: Button
    @FXML
    lateinit var recruitCostLabel: Label
    @FXML
    lateinit var populationCostLabel: Label
    @FXML
    lateinit var recruitSoldierButton: Button
    @FXML
    lateinit var meleeCB: CheckBox
    @FXML
    lateinit var rangedCB: CheckBox
    @FXML
    lateinit var powerProgressbar: ProgressBar
    @FXML
    lateinit var healthProgressbar: ProgressBar
    @FXML
    lateinit var prestigeProgressbar: ProgressBar
    @FXML
    lateinit var loyaltyProgressbar: ProgressBar



    /** This is called by all config views but the controller is set programmatically so the usage is not shown
     *  TODO: check if this could be defined in the XMl again instead */
    @FXML
    fun onConfigurationChange(e: ActionEvent){
//        updatePlayer(playerConfigActiveCB.isSelected) // Update player object
//        updateView()                                  // Update the view representing the player
//        callback.onUpdateActiveState(playerConfig)    // Notify the outer controller to update the whole view
    }


    init {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("view-military-recruit-unit.fxml"), Game.resourcesBundle)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception : IOException) {
            throw RuntimeException(exception)
        }

        unitTypeLabel.text = Game.resourcesBundle.getString(unit.nameRes)
        payPerYearLabel.text = "${unit.pay} ${Game.resourcesBundle.getString("general_currency")}"
        hireCostLabel.text = "${unit.mercCost} ${Game.resourcesBundle.getString("general_currency")}"
        recruitCostLabel.text = "${unit.recruitCost} ${Game.resourcesBundle.getString("general_currency")}"
        populationCostLabel.text = "${unit.popCost} ${Game.resourcesBundle.getString("general_persons_male")}"

        meleeCB.isSelected = unit.melee
        rangedCB.isSelected = unit.ranged

        powerProgressbar.progress = 0.5
        healthProgressbar.progress = 0.5
        prestigeProgressbar.progress = 0.5
        loyaltyProgressbar.progress = unit.loyalty

    }

}