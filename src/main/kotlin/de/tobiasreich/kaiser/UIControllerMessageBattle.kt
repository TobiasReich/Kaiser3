package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.data.player.BattleMessage
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextFlow
import java.net.URL
import java.util.*

/** A battle view showing a battle against another player */
class UIControllerMessageBattle : Initializable, IMessageController{

    @FXML
    lateinit var battleEndButton: Button

    @FXML
    lateinit var attackerRectangle: Rectangle

    @FXML
    lateinit var defenderRectangle: Rectangle


    @FXML
    lateinit var battleUpdateTextFlow: TextFlow

    @FXML
    lateinit var defenderAmountUnitsLabel: Label

    @FXML
    lateinit var defenderNameLabel: Label

    @FXML
    lateinit var attackerAmountUnitsLabel: Label

    @FXML
    lateinit var attackerNameLabel: Label


    /** The Message to be shown to the user */
    private lateinit var message: BattleMessage


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/

    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
        proceedToNextNews()
    }


    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle?) { }

    override fun setMessage(message: ReportMessage) {
        println("setMessage")
        this.message = message as BattleMessage
        updateView()
    }

    private fun updateView() {
        println("updateView")
        //attackerRectangle.width = 450.0
    }

    fun onBeginBattleButtonClick(actionEvent: ActionEvent) {
        // TODO if Battle has not started yet: Starts the Battle
        battleEndButton.isDisable = false
    }

    fun onBattleOutcomeButtonClick(actionEvent: ActionEvent) {
        // TODO if battle is over, end the view
        // -> Make a BattleOutcomeMessage message to the defender (attacker not needed)
        // -> send remaining attacker Troops home -> WarManager.addTroopMovement()
        proceedToNextNews()
    }

}