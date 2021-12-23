package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Modality
import javafx.stage.Stage
import java.net.URL
import java.util.*

/** This is the controller for the game menu
 *
 */
class UIControllerGameMenu : Initializable {


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/

    @FXML
    private fun onMenuNewGameClick() {
    }

    @FXML
    private fun onMenuLoadGameClick() {
    }

    @FXML
    private fun onMenuSaveGameClick() {
    }


    /********************************************
     *
     *             Settings Menu items
     *
     *******************************************/


    @FXML
    private fun onMenuPreferencesClick() {
    }


    /********************************************
     *
     *             Help Menu items
     *
     *******************************************/


    @FXML
    private fun onMenuHelpClick() {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("menu-help-webview.fxml"), Game.resourcesBundle)
        val helpScene = Scene(fxmlLoader.load())

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Hilfe"
        stage.scene = helpScene
        stage.show()

        val helpWindowWebview = helpScene.lookup("#helpWindowWebview") as WebView
        //TODO This should also be affected by I18n so we have to create translated help html pages
        val url = javaClass.getResource("help_index.html")
        helpWindowWebview.engine.load(url.toExternalForm())
    }


    @FXML
    private fun onMenuAboutClick() {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("menu-help-about.fxml"), Game.resourcesBundle)
        val helpScene = Scene(fxmlLoader.load(), 640.0, 480.0)

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = Game.resourcesBundle.getString("menu_about_title")
        stage.scene = helpScene
        stage.show()
    }


    /********************************************
     *
     *             View Updates
     *
     *******************************************/

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {

    }


}