package de.tobiasreich.kaiser

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
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
        ScreenController.activate("game")
    }

}