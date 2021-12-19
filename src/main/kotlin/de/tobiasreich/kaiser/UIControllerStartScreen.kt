package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.config.GameConfiguration
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

    companion object{

        // This is the config at start of the game.
        val defaultPlayerList = listOf(
            PlayerConfig(0, CountryName.HOLSTEIN  , "", Color.RED         ,true, false, true, 0),
            PlayerConfig(1, CountryName.WESTPHALIA, "", Color.YELLOW      ,false, true, false,0),
            PlayerConfig(2, CountryName.SAXONY    , "", Color.ORANGE      ,false, true, false,0),
            PlayerConfig(3, CountryName.PRUSSIA   , "", Color.GREEN       ,false, true, false,0),
            PlayerConfig(4, CountryName.HESSE     , "", Color.DARKCYAN    ,false, true, false,0),
            PlayerConfig(5, CountryName.THURINGIA , "", Color.TURQUOISE   ,false, true, false,0),
            PlayerConfig(6, CountryName.BOHEMIA   , "", Color.LIGHTBLUE   ,false, true, false,0),
            PlayerConfig(7, CountryName.BADEN     , "", Color.BLUE        ,false, true, false,0),
            PlayerConfig(8, CountryName.FRANCONIA , "", Color.BLUEVIOLET  ,false, true, false,0),
            PlayerConfig(9, CountryName.BAVARIA   , "", Color.VIOLET      ,false, true, false,0),
        )

    }


    @FXML
    private lateinit var rootBorderPane: BorderPane

    @FXML
    private lateinit var playerConfigs: VBox

    private lateinit var playersList : MutableList<PlayerConfig>

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onStartGameClick(actionEvent: ActionEvent) {
        // TODO: Get this from the config instead of hard coded
        //val player0 = Player("Player0", true, CountryName.BAVARIA)

        Game.setupGame(getGameConfiguration().players)

        ViewController.showScene(ViewController.SCENE_NAME.GAME)
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        playersList = defaultPlayerList.toMutableList()
        playersList.forEach {
            val playerConfigView = UIControllerPlayerConfigView(it, this)
            playerConfigs.children.add(playerConfigView)
        }
    }


    override fun onUpdateActiveState(playerConfig : PlayerConfig) {
        //TODO Check if we have a valid configuration and enable/disable the start-game button depending on it
    }

    /** Returns a GameConfiguration object depending on the settings made in this screen */
    fun getGameConfiguration() : GameConfiguration{
        val selectedPlayers = playersList.filter { it.active }
        return(GameConfiguration(selectedPlayers))
    }
}