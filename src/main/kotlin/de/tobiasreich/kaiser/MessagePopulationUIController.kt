package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class MessagePopulationUIController : Initializable{

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
            ScreenController.activate(ScreenController.SCREEN_NAME.GAME)
        } else {
            ScreenController.showView(message.getView().load())
        }
    }


    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        //playerIntroLabel.text = "Willkommen ${Game.currentPlayer.name}. Es ist das Jahr ${Game.getYear()}."
        playerIntroLabel.text = "Willkommen. Es ist das Jahr ${Game.getYear()}."
    }

}