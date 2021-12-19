package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionLand : Initializable {

    @FXML
    private lateinit var rootBorderPane: BorderPane

    @FXML
    private lateinit var foodAmountLabel: Label


    @FXML
    fun onButtonSomethingClick(actionEvent: ActionEvent) {
        //Some button clicked
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        foodAmountLabel.text = Game.currentPlayer.storedFood.toString()
    }

}