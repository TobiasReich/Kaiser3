package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import org.controlsfx.control.PopOver

/** Manager for setting views to the stage */
object ViewController {

    @Suppress("ClassName")
    enum class SCENE_NAME {
        START_SCREEN,
        GAME,
        NEXT_PLAYER
    }

    //var main: Scene? = null
    lateinit var rootPane: BorderPane

    /** This shows a certain screen*/
    fun showScene(sceneName: SCENE_NAME) {
        //main!!.root = when (sceneName){
        val viewToShow : Parent = when (sceneName){
            SCENE_NAME.START_SCREEN -> { FXMLLoader.load(javaClass.getResource("scene-game-config-screen.fxml"), Game.resourcesBundle) }
            SCENE_NAME.GAME -> { FXMLLoader.load(javaClass.getResource("scene-game-view.fxml"), Game.resourcesBundle) }
            SCENE_NAME.NEXT_PLAYER -> { FXMLLoader.load(javaClass.getResource("scene-next-player-screen.fxml"), Game.resourcesBundle) }
        }

        rootPane.center = viewToShow
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

}