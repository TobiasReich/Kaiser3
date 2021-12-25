package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.config.GameConfiguration
import de.tobiasreich.kaiser.config.IPlayerConfigChange
import de.tobiasreich.kaiser.config.PlayerConfig
import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.player.Country
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
            PlayerConfig(0, Country.HOLSTEIN  , "Archibald Buttle"      , Color.RED         ,true, false, true, 0),
            PlayerConfig(1, Country.WESTPHALIA, "Bella Bellington"      , Color.YELLOW      ,true, true, false,0),
            PlayerConfig(2, Country.SAXONY    , "Charles Le Crobboiyent", Color.ORANGE      ,true, true, false,0),
            PlayerConfig(3, Country.PRUSSIA   , "Dorothea die Gerechte" , Color.GREEN       ,false, true, false,0),
            PlayerConfig(4, Country.HESSE     , "Erwin der Erlöser"     , Color.DARKCYAN    ,false, true, false,0),
            PlayerConfig(5, Country.THURINGIA , "Frieda die Fromme"     , Color.TURQUOISE   ,false, true, false,0),
            PlayerConfig(6, Country.BOHEMIA   , "Gunther the Penguin"   , Color.LIGHTBLUE   ,true, true, false,0),
            PlayerConfig(7, Country.BADEN     , "Hellena Hillarious"    , Color.BLUE        ,false, true, false,0),
            PlayerConfig(8, Country.FRANCONIA , "Iwan der Franke"       , Color.BLUEVIOLET  ,true, true, false,0),
            PlayerConfig(9, Country.BAVARIA   , "Juliane aus Bayern"    , Color.VIOLET      ,false, true, false,0),
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
        Game.setupGame(getGameConfiguration().players)
        ViewController.showScene(ViewController.SCENE_NAME.NEXT_PLAYER)
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