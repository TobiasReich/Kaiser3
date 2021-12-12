package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class NextPlayerScreenUIController : Initializable{

    @FXML
    private lateinit var playerIntroLabel: Label


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
        val message = Game.currentPlayer.getNextNews()

        if (message == null){
            ScreenController.showScene(ScreenController.SCENE_NAME.GAME)
        } else {
            ScreenController.showView(message.getView().load())
        }
    }

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        playerIntroLabel.text = "Willkommen ${Game.currentPlayer.name}. Es ist das Jahr ${Game.getYear()}."
    }

}