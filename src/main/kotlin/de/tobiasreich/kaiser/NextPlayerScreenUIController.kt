package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class NextPlayerScreenUIController {


    @FXML
    private lateinit var messageView: BorderPane

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/

    // Ugly but this keeps track of which message was shown to the user
    private var newsNumber = 0

    fun onNewsButtonClick(actionEvent: ActionEvent) {
        println("onNewsButtonClick")
        showNextPlayerNews()
        newsNumber++
    }

    private fun showNextPlayerNews() {
        println("showNextPlayerNews")
        println("playerNewsNumber: $newsNumber")

        val messages = Game.currentPlayer.messageList

        if (newsNumber >= messages.size){
            println("This was the last news message. Showing game")

            // This was the last news message. Show the game view now
            newsNumber = 0 // reset the news counter before showing the game
            ScreenController.activate(ScreenController.SCREEN_NAME.GAME)

        } else {
            println("Showing next message" )
            val message = messages[newsNumber]
            println("Showing next message: $message in messageView: $messageView" )
            val fxmlLoader = message.getView()
            val view : Node = fxmlLoader.load()
            println("View to show: $view in MessageView: $messageView" )
            messageView.center = view
            println("View got added. State of the MessageView: $messageView" )
        }
    }


}