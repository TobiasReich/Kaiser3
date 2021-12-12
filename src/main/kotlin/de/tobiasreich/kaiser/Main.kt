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

        //val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view.fxml"))
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("start-screen.fxml"))
        val startGameScene = Scene(fxmlLoader.load(), 800.0, 600.0)

        //val root = scene.lookup("#rootBorderPane") as BorderPane

        //val menuBar = prepareMenu()

        //root.top = menuBar
        ScreenController.main = startGameScene
        ScreenController.addScreen("start", FXMLLoader.load(javaClass.getResource("start-screen.fxml")))
        ScreenController.addScreen("game", FXMLLoader.load(javaClass.getResource("game-view.fxml")))
        ScreenController.activate("start")

        stage.title = "Kaiser III"
        stage.scene = startGameScene
        stage.show()
   }

}
        