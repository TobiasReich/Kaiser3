package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.input.DragEvent
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionTaxes : Initializable {

    @FXML
    private lateinit var incomeTaxSlider: Slider


    override fun initialize(p0: URL?, p1: ResourceBundle?) {
    }

    fun incomeTaxChanged() {
        println("Income tax changed! ${incomeTaxSlider.value}.")
    }

    /** When the user clicks the "done" button. Close this window */
    fun onDoneButtonPressed(actionEvent: ActionEvent) {
        val source: Node = actionEvent.source as Node
        val stage: Stage = source.scene.window as Stage
        stage.close()
    }
}