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
    }


    override fun start(stage: Stage) {
        //val fxmlLoader = FXMLLoader(Main::class.java.getResource("scene-game-view.fxml"))
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("scene-start-screen.fxml"))
        val startGameScene = Scene(fxmlLoader.load(), 800.0, 600.0)

        // Set the language so all Controllers can use it
        //val locale = Locale("en", "US")
        val locale = Locale("de", "DE")
        //val locale = Locale.getDefault() // Use this one in the end
        val resources = ResourceBundle.getBundle("strings", locale)
        Game.stringsBundle = resources

        //val root = scene.lookup("#rootBorderPane") as BorderPane

        //val menuBar = prepareMenu()

        //root.top = menuBar
        ViewController.main = startGameScene

        stage.title = "Kaiser III"
        stage.scene = startGameScene
        stage.show()
   }

}
        