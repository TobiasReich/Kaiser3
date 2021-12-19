package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import java.net.URL
import java.util.*


/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerNextPlayerScreen : Initializable {

    @FXML
    private lateinit var nextPlayerAddress: Label

    @FXML
    private lateinit var nextPlayerInfo: Label


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNextPlayerButtonClick(actionEvent: ActionEvent) {
        Game.currentPlayer.startNewTurn()

        val message = Game.currentPlayer.getNextMessage()

        if (message == null){
            ViewController.showScene(ViewController.SCENE_NAME.GAME)
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
        nextPlayerAddress.text = String.format(bundle.getString("next_player_address"), player.getGenderTitle(bundle), player.name, player.getCountryName(bundle))
        nextPlayerInfo.text = String.format(bundle.getString("next_player_info"), Game.getYear())
    }

}