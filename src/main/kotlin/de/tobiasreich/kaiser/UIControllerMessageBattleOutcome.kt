package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.data.military.WarGoal
import de.tobiasreich.kaiser.game.data.player.BattleOutcomeMessage
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessageBattleOutcome : Initializable, IMessageController{

    @FXML
    lateinit var civilLossesLabel: Label

    @FXML
    lateinit var battleAttackerLabel: Label

    @FXML
    lateinit var militaryLossesLabel: Label

    @FXML
    lateinit var battleOutcomeLabel: Label

    /** The Message to generate the info shown to the user */
    private lateinit var message: BattleOutcomeMessage


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
        this.message = message as BattleOutcomeMessage
        updateView()
    }

    private fun updateView() {
        println("Show War outcome")

        battleAttackerLabel.text = message.attackingPlayer.name
        battleAttackerLabel.style = ("-fx-text-fill: ${message.attackingPlayer.playerColor.toRGBCode()}; ")

        if (message.attackerVictory){
            // Attacker won -> this user was defeated
            battleOutcomeLabel.text = bundle.getString("battle_outcome_summary_defeat")
        } else {
            // Attacker lost -> this user is victorious
            battleOutcomeLabel.text = bundle.getString("battle_outcome_summary_victory")
        }


        // Different message depending on how many troops survived
        if (message.remainingPowerFraction > 0.7) {
            militaryLossesLabel.text = bundle.getString("battle_outcome_losses_few")
        } else if (message.remainingPowerFraction > 0.4) {
            militaryLossesLabel.text = bundle.getString("battle_outcome_losses_mediocre")
        } else {
            militaryLossesLabel.text = bundle.getString("battle_outcome_losses_hard")
        }

        when (message.warGoal){
            WarGoal.KILL_UNITS -> {
                civilLossesLabel.text = bundle.getString("battle_outcome_civil_losses_none")
            }
            WarGoal.STEAL_MONEY -> {
                civilLossesLabel.text = String.format(bundle.getString("battle_outcome_civil_losses_money"), message.victoryValue)
            }
            WarGoal.GET_SLAVES -> {
                civilLossesLabel.text = String.format(bundle.getString("battle_outcome_civil_losses_slaves"), message.victoryValue)
            }
            WarGoal.CONQUER -> {
                civilLossesLabel.text = String.format(bundle.getString("battle_outcome_civil_losses_conquer"), message.victoryValue)
            }
            WarGoal.BURN_BUILDINGS -> {
                civilLossesLabel.text = String.format(bundle.getString("battle_outcome_civil_losses_buildings"), message.victoryValue)
            }
        }
    }

}
