package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class MessagePopulationUIController : Initializable{


    @FXML
    private lateinit var newsMessageBornLabel: Label

    @FXML
    private lateinit var newsMessageDiedAgeLabel: Label

    @FXML
    private lateinit var newsMessageDiedBadHealthLabel: Label

    @FXML
    private lateinit var newsMessageImmimigratedLabel: Label

    @FXML
    private lateinit var newsMessageEmigratedLabel: Label


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
        val message = Game.currentPlayer.getNextNews()

        if (message == null){
            ScreenController.showScene(ScreenController.SCENE_NAME.GAME)
        } else {
            ScreenController.showView(message.getView().load())
        }
    }


    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {

    }

//    private lateinit var playerIntroLabel: Label
//    private lateinit var newsMessageBornLabel: Label
//    private lateinit var newsMessageDiedAgeLabel: Label
//    private lateinit var newsMessageDiedBadHealthLabel: Label
//    private lateinit var newsMessageImmimigratedLabel: Label
//    private lateinit var newsMessageEmigratedLabel: Label

}