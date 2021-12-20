package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
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

        // This loads the root view where there is a MENU on top.
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("scene-root-menu.fxml"), Game.stringsBundle)
        val rootMenuScene = Scene(fxmlLoader.load(), WIDTH, HEIGHT)
        // Load the Border pane which is the root of that view. There in the Center we want to show all the views
        // but keep the menu
        val borderPane = rootMenuScene.root as BorderPane

        // Set the border pane the root pane in the ViewController in order to set other views there to the Center
        ViewController.rootPane = borderPane

        // Now load a subScene view which will be shown in the Center
        val subViewLoader = FXMLLoader(Main::class.java.getResource("scene-game-config-screen.fxml"), Game.stringsBundle)
        val subScene = Scene(subViewLoader.load(), WIDTH, HEIGHT)

        // Set this view as the "start screen"
        //TODO We need a game config screen before anything else. Add one here as the first view
        ViewController.showView(subScene.root as Pane) //borderPane.center = subScene.root

        stage.title = resources.getString("game_title")
        stage.scene = rootMenuScene
        stage.isResizable = false
        stage.show()
    }

}
        