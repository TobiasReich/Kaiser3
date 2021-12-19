package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.config.IPlayerConfigChange
import de.tobiasreich.kaiser.config.PlayerConfig
import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.data.player.CountryName
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import java.net.URL
import java.util.*


class UIControllerStartScreen : Initializable, IPlayerConfigChange {

    @FXML
    private lateinit var rootBorderPane: BorderPane

    @FXML
    private lateinit var playerConfigs: VBox

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/



    @FXML
    fun onStartGameClick(actionEvent: ActionEvent) {
        // TODO: Get this from the config instead of hard coded
        val player0 = Player("Player0", true, CountryName.BAVARIA)
        Game.setupGame(mutableListOf(player0))

        ViewController.showScene(ViewController.SCENE_NAME.GAME)
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        val player0 = PlayerConfig(0, CountryName.HOLSTEIN  , "", Color.RED         ,true, false, true, 0)
        val player1 = PlayerConfig(1, CountryName.WESTPHALIA, "", Color.YELLOW      ,false, true, false,0)
        val player2 = PlayerConfig(2, CountryName.SAXONY    , "", Color.ORANGE      ,false, true, false,0)
        val player3 = PlayerConfig(3, CountryName.PRUSSIA   , "", Color.GREEN       ,false, true, false,0)
        val player4 = PlayerConfig(4, CountryName.HESSE     , "", Color.DARKCYAN    ,false, true, false,0)
        val player5 = PlayerConfig(5, CountryName.THURINGIA , "", Color.TURQUOISE   ,false, true, false,0)
        val player6 = PlayerConfig(6, CountryName.BOHEMIA   , "", Color.LIGHTBLUE   ,false, true, false,0)
        val player7 = PlayerConfig(7, CountryName.BADEN     , "", Color.BLUE        ,false, true, false,0)
        val player8 = PlayerConfig(8, CountryName.FRANCONIA , "", Color.BLUEVIOLET  ,false, true, false,0)
        val player9 = PlayerConfig(9, CountryName.BAVARIA   , "", Color.VIOLET      ,false, true, false,0)

        val player0ConfigView = PlayerConfigView(player0, this)
        val player1ConfigView = PlayerConfigView(player1, this)
        val player2ConfigView = PlayerConfigView(player2, this)
        val player3ConfigView = PlayerConfigView(player3, this)
        val player4ConfigView = PlayerConfigView(player4, this)
        val player5ConfigView = PlayerConfigView(player5, this)
        val player6ConfigView = PlayerConfigView(player6, this)
        val player7ConfigView = PlayerConfigView(player7, this)
        val player8ConfigView = PlayerConfigView(player8, this)
        val player9ConfigView = PlayerConfigView(player9, this)
        playerConfigs.children.addAll(
            player0ConfigView,
            player1ConfigView,
            player2ConfigView,
            player3ConfigView,
            player4ConfigView,
            player5ConfigView,
            player6ConfigView,
            player7ConfigView,
            player8ConfigView,
            player9ConfigView,
        )
    }


    override fun onUpdateActiveState() {
        //TODO Check if we have a valid configuration and enable/disable the start-game button depending on it
    }

}