package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane

/** Manager for setting views to the stage */
object ViewController {

    @Suppress("ClassName")
    enum class SCENE_NAME {
        START_SCREEN,
        GAME,
        NEXT_PLAYER
    }

    var main: Scene? = null

    /** This shows a certain screen*/
    fun showScene(sceneName: SCENE_NAME) {
        main!!.root = when (sceneName){
            SCENE_NAME.START_SCREEN -> { FXMLLoader.load(javaClass.getResource("scene-start-screen.fxml"), Game.stringsBundle) }
            SCENE_NAME.GAME -> { FXMLLoader.load(javaClass.getResource("scene-game-view.fxml"), Game.stringsBundle) }
            SCENE_NAME.NEXT_PLAYER -> { FXMLLoader.load(javaClass.getResource("scene-next-player-screen.fxml"), Game.stringsBundle) }
        }
    }

    /** This shows a certain screen (used for the news) */
    fun showView(view: Pane) {
        main!!.root = view
    }
}