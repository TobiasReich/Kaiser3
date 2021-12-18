package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.PlayerConfig
import de.tobiasreich.kaiser.game.data.player.CountryName
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import java.net.URL
import java.util.*


class UIControllerStartScreen : Initializable {

    @FXML
    private lateinit var rootBorderPane: BorderPane

    @FXML
    private lateinit var configTable: TableView<PlayerConfig>

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/



    @FXML
    fun onStartGameClick(actionEvent: ActionEvent) {
        val player0 = Player("Player0", true, CountryName.BAVARIA)
        val player1 = Player("Player1", false, CountryName.HESSE)
        //...
        Game.setupGame(mutableListOf(player0, player1))

        ViewController.showScene(ViewController.SCENE_NAME.GAME)
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
//        val player0 = PlayerConfig(0, CountryName.HOLSTEIN , "Alice", Color.RED,true, true, 0)
//        val player1 = PlayerConfig(2, CountryName.BAVARIA , "Bob", Color.RED,false, true, 0)
//
//        val indexColumn = TableColumn<PlayerConfig, Int>("Index!!!")
//        indexColumn.cellValueFactory = PropertyValueFactory("id")
//
//        val nameColumn = TableColumn<PlayerConfig, String>("Spieler Name!!!")
//        nameColumn.cellValueFactory = PropertyValueFactory("name")
//
//        configTable.columns.add(indexColumn)
//        configTable.columns.add(nameColumn)
//
//        configTable.items.add(player0)
//        configTable.items.add(player1)
    }

    /*
    *  <columns>
            <TableColumn prefWidth="27.0" text="#" />
          <TableColumn prefWidth="71.0" text="Land" />
          <TableColumn prefWidth="127.0" text="Name" />
            <TableColumn prefWidth="75.0" text="Farbe" />
            <TableColumn prefWidth="75.0" text="Männlich" />
            <TableColumn prefWidth="75.0" text="Aktiv" />
            <TableColumn prefWidth="102.0" text="Schwierigkeit" />
        </columns>
    * */
}