package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.population.Population
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.layout.HBox
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
class UIControllerViewMilitaryRecruitUnit(private val unit : MilitaryUnit, private val population: Population) : HBox() {

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

        unitTypeLabel.text = "Soldat"
//        val tabListener: EventHandler<KeyEvent> = EventHandler<KeyEvent> { evt ->
//            evt.consume()
//            updatePlayer(playerConfigActiveCB.isSelected) // Update player object
//            updateView()                                  // Update the view representing the player
//            callback.onUpdateActiveState(playerConfig)    // Notify the outer controller to update the whole view
//            /* Since the UpdateView sets the "new" text the cursor would still be at the beginning of the view
//             * Thus writing another character would lead to adding it at the start.
//             * We therefore set the cursor to the end of the view */
//            playerConfigNameTF.end()
//        }
//
//        playerConfigNameTF.addEventHandler(KeyEvent.ANY, tabListener)
//
//        updateView()
    }

}