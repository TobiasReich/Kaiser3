package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Modality
import javafx.stage.Stage

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class NextPlayerScreenUIController {


    @FXML
    private lateinit var rootBorderPane: BorderPane

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    fun onStartTurnClick(actionEvent: ActionEvent) {
        // Show all upcoming events and results
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-land.fxml"))
        val taxScene = Scene(fxmlLoader.load(), 300.0, 200.0)

        // For all Messages of the player, show a message
        Game.currentPlayer.messageList.forEachIndexed { index, message ->

            val stage = Stage()
            stage.initModality(Modality.APPLICATION_MODAL)
            stage.title = "Ereigniss!"
            stage.scene = taxScene
            stage.show()
        }

        // Once done, show the game screen
        //TODO: This should not be shown directly but only when the last message is read.
        ScreenController.activate(ScreenController.SCREENS.GAME)
    }
}