package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.util.*


class Main : Application() {

    companion object {
        @JvmStatic fun main(args : Array<String>) {
            launch(Main::class.java)
        }

        const val WIDTH = 1280.0
        const val HEIGHT = 720.0
    }


    override fun start(stage: Stage) {
        // Before anything else (e.g. inflating): set the language so all Controllers can use it
        //val locale = Locale("en", "US")
        val locale = Locale("de", "DE")
        //val locale = Locale.getDefault() // Use this one in the end
        val resources = ResourceBundle.getBundle("strings", locale)
        Game.stringsBundle = resources

        //TODO We need a game config screen before anything else. Add one here as the first view
        //val fxmlLoader = FXMLLoader(Main::class.java.getResource("scene-game-view.fxml"))
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("scene-start-screen.fxml"), Game.stringsBundle)
        val startGameScene = Scene(fxmlLoader.load(), WIDTH, HEIGHT)


        //val menuBar = prepareMenu()

        //root.top = menuBar
        ViewController.main = startGameScene

        stage.title = resources.getString("game_title")
        stage.scene = startGameScene
        stage.show()
   }

}
        