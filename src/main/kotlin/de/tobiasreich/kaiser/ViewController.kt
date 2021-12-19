package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane

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
            SCENE_NAME.START_SCREEN -> { FXMLLoader.load(javaClass.getResource("scene-game-config-screen.fxml"), Game.stringsBundle) }
            SCENE_NAME.GAME -> { FXMLLoader.load(javaClass.getResource("scene-game-view.fxml"), Game.stringsBundle) }
            SCENE_NAME.NEXT_PLAYER -> { FXMLLoader.load(javaClass.getResource("scene-next-player-screen.fxml"), Game.stringsBundle) }
        }

        rootPane.center = viewToShow
    }

    /** This shows a certain screen (used for the news) */
    fun showView(view: Pane) {
        rootPane.center = view
        //main!!.root = view
    }
}