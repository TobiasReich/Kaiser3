package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import utils.FXUtils.FxUtils.toRGBCode
import java.net.URL
import java.util.*


/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerNextPlayerScreen : Initializable {

    @FXML
    private lateinit var nextPlayerAddress: Label

    @FXML
    private lateinit var nextPlayerInfo: Label

    @FXML
    private lateinit var nextPlayerNextTurnButton: Button

    @FXML
    private lateinit var nextPlayerTopLine: Line

    @FXML
    private lateinit var nextPlayerBottomLine: Line


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNextPlayerButtonClick(actionEvent: ActionEvent) {
        // !!! This starts a new turn for the player!!!s
        Game.currentPlayer.startNewTurn()

        // --------------------------------------------

        val message = Game.currentPlayer.getNextMessage()

        if (message == null){
            ViewController.showGameScene()
        } else {
            val loader = message.getViewLoader()
            val view = loader.load() as Pane
            val nextSceneController = loader.getController() as IMessageController
            nextSceneController.setMessage(message)
            ViewController.showView(view)
        }
    }

    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle) {
        val player = Game.currentPlayer
        nextPlayerAddress.text = String.format(bundle.getString("next_player_address"), player.getGenderTitle(bundle), player.name, bundle.getString(player.country.nameResource))
        nextPlayerInfo.text = String.format(bundle.getString("next_player_info"), Game.getYear())

        //Colorize the "next turn" button in the color of the player for better UX
        nextPlayerTopLine.stroke = player.playerColor
        nextPlayerBottomLine.stroke = player.playerColor
        nextPlayerNextTurnButton.style = ("-fx-background-color: ${player.playerColor.toRGBCode()}; ")
    }

}