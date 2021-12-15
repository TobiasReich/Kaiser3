package de.tobiasreich.kaiser

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

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

        //val root = scene.lookup("#rootBorderPane") as BorderPane

        //val menuBar = prepareMenu()

        //root.top = menuBar
        ViewController.main = startGameScene

        stage.title = "Kaiser III"
        stage.scene = startGameScene
        stage.show()
   }

}
        