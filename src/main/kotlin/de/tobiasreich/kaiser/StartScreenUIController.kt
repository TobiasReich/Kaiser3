package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.data.player.CountryName
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane


class StartScreenUIController {

    @FXML
    private lateinit var rootBorderPane: BorderPane

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/

    @FXML
    fun onStartGameClick(actionEvent: ActionEvent) {
        val player1 = Player("Player1", true, CountryName.BAVARIA)
        val player2 = Player("Player2", false, CountryName.HESSE)
        //...
        Game.setupGame(mutableListOf(player1, player2))

        ScreenController.activate("game")
    }

}