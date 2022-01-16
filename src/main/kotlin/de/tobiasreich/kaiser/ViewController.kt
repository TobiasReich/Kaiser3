package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.stage.Modality
import org.controlsfx.control.PopOver
import de.tobiasreich.kaiser.game.utils.FxDialogs


/** Manager for setting views to the stage */
object ViewController {

    lateinit var rootPane: BorderPane

    /** This shows the "NEXT PLAYER" screen*/
    fun showNextPlayerScene() {
        val viewToShow : Pane = FXMLLoader.load(javaClass.getResource("scene-next-player-screen.fxml"), Game.resourcesBundle)
        showView(viewToShow)
    }

    /** This shows the START SCREEN screen*/
    fun showStartScreenScene() {
        val viewToShow : Pane =  FXMLLoader.load(javaClass.getResource("scene-game-config-screen.fxml"), Game.resourcesBundle)
        showView(viewToShow)
    }

    /** This shows the GAME screen*/
    fun showGameScene() {
        val viewToShow : Pane = FXMLLoader.load(javaClass.getResource("scene-game-view.fxml"), Game.resourcesBundle)
        showView(viewToShow)
    }

    /** This shows a certain screen (used for the news) */
    fun showView(view: Pane) {
        rootPane.center = view
        //main!!.root = view
    }

    /** Shows an (info) popup at a specified view - used for help texts and infos. */
    fun showInfoPopUp(view : Node, title : String, infoText : String){
        val popOver = PopOver()
        popOver.isDetachable = false
        popOver.isDetached = false
        popOver.arrowSize = 10.0
        popOver.arrowIndent = 5.0
        popOver.arrowLocation = PopOver.ArrowLocation.BOTTOM_CENTER
        popOver.cornerRadius = 5.0
        popOver.isHeaderAlwaysVisible = true
        popOver.isAnimated = true
        popOver.title = title
        popOver.isCloseButtonEnabled = false
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("info_popup_layout.fxml"), Game.resourcesBundle)
        val node = fxmlLoader.load() as Node
        val label = node.lookup("#infoTextLabel") as Label
        label.text = infoText
        popOver.contentNode = node // Sets the "help" node as content of that popOver
        popOver.show(view)
    }

    fun showModalDialog(title : String, message : String) : FxDialogs.DialogResult {
        val result = FxDialogs.showConfirm(title,message)
        println("Result from Dialog was $result")
        return result
    }

    private fun showDialog(type: AlertType, modality : Modality) {
        FxDialogs.showConfirm("Title","Message")
    }
}