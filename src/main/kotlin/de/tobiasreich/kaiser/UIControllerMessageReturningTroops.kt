package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.military.WarGoal
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.ReturningTroopsMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.shape.Line
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessageReturningTroops : Initializable, IMessageController{

    @FXML
    lateinit var playerTopLine: Line

    @FXML
    lateinit var playerBottomLine: Line

    @FXML
    lateinit var originPlayerLabel: Label

    @FXML
    lateinit var warSpoilsSummary: Label


    /** The Message to generate the info shown to the user */
    private lateinit var message: ReturningTroopsMessage


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
        proceedToNextNews()
    }

    private lateinit var bundle : ResourceBundle

    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!
    }

    override fun setMessage(message: ReportMessage) {
        this.message = message as ReturningTroopsMessage
        updateView()
    }

    private fun updateView() {
        val originPlayer = message.originPlayer
        val playerTitle = if (originPlayer.isMale){
            Game.resourcesBundle.getString(originPlayer.playerTitle.resourceNameMale)
        } else {
            Game.resourcesBundle.getString(originPlayer.playerTitle.resourceNameFemale)
        }
        val playerCountry = Game.resourcesBundle.getString(originPlayer.country.nameResource)
        originPlayerLabel.style = ("-fx-text-fill: ${originPlayer.playerColor.toRGBCode()}; ")
        playerTopLine.stroke = originPlayer.playerColor
        playerBottomLine.stroke = originPlayer.playerColor
        originPlayerLabel.text = String.format(bundle.getString("returning_troops_message_origin_message"), playerTitle, originPlayer.name, playerCountry)

        // Add resources from war spoils
        val spoilsMessage = when(message.warGoal){
            WarGoal.KILL_UNITS -> {
                "" /* No resources to add. */
            }
            WarGoal.STEAL_MONEY -> {
                Game.currentPlayer.money += message.victoryAmount
                String.format(bundle.getString("returning_troops_message_war_spoils_money"), message.victoryAmount)
            }
            WarGoal.GET_SLAVES -> {
                Game.currentPlayer.population.addAdults(message.slaves ?: mutableListOf())
                String.format(bundle.getString("returning_troops_message_war_spoils_slaves"), message.victoryAmount)
            }
            WarGoal.CONQUER -> {
                Game.currentPlayer.land.addLand(message.victoryAmount)
                String.format(bundle.getString("returning_troops_message_war_spoils_conquer"), message.victoryAmount)
            }
            WarGoal.BURN_BUILDINGS -> {
                "" /* No resources to add. */
            }
            null -> {
                "" /* Troops did not go to war, nothing to add.*/
            }
        }
        warSpoilsSummary.text = spoilsMessage
    }


    /** Integrates the home coming troops in the local military and adds their acquired resources. */
    fun integrateTroopsButtonClick() {
        // Integrate military
        val military = Game.currentPlayer.military
        message.returningUnits.keys.forEach {
            if (military[it] == null){
                military[it] = message.returningUnits[it]!!
            } else {
                military[it]!!.addAll(message.returningUnits[it]!!)
            }
        }

        proceedToNextNews()
    }
}
