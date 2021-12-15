package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.PopulationReport
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessagePopulation : Initializable, IMessageController{

    /** The Message to be shown to the user */
    private lateinit var messge: ReportMessage

    @FXML
    private lateinit var populationMessageTitle: Label

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

    @FXML
    private lateinit var newsMessageTotalChange: Label


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
        val message = Game.currentPlayer.getNextMessage()

        if (message == null){
            ViewController.showScene(ViewController.SCENE_NAME.GAME)
        } else {
            val loader = message.getViewLoader()
            val view = loader.load() as Pane
            val nextSceneController = loader.getController() as IMessageController
            nextSceneController.setMessage(message)
            ViewController.showView(view)
        }
    }


    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {

    }

    override fun setMessage(message: ReportMessage) {
        this.messge = message
        val popMsg = messge as PopulationReport


        newsMessageBornLabel.text = "Dieses Jahr sind ${popMsg.birth} Kinder geboren."
        newsMessageDiedAgeLabel.text = "Dieses Jahr sind ${popMsg.diedOfAge} alte Menschen gestorben."
        newsMessageDiedBadHealthLabel.text = "Dieses Jahr sind ${popMsg.diedOfHealth} Menschen an schlechter Gesundheit gestorben."
        newsMessageImmimigratedLabel.text = "${popMsg.immigrated} Einwanderer kamen ins Land."
        newsMessageEmigratedLabel.text = "${popMsg.emigrated} eurer Bürger sind ausgewandert."
        if (popMsg.totalChange > 0){
            populationMessageTitle.text = "Die Bevölkerung wächst!"
            newsMessageTotalChange.text = "Damit leben jetzt ${popMsg.totalChange} mehr Bürger in eurem Land."
        } else if (popMsg.totalChange < 0){
            populationMessageTitle.text = "Keine Veränderung!"
            newsMessageTotalChange.text = "Damit ist die Bevölkerung um ${popMsg.totalChange*-1} Personen geschrumpft."
        } else {
            populationMessageTitle.text = "Die Bevölkerung schrumpft!"
            newsMessageTotalChange.text = "Damit bleibt die Bevölkerung stabil."
        }
    }


//    private lateinit var playerIntroLabel: Label
//    private lateinit var newsMessageBornLabel: Label
//    private lateinit var newsMessageDiedAgeLabel: Label
//    private lateinit var newsMessageDiedBadHealthLabel: Label
//    private lateinit var newsMessageImmimigratedLabel: Label
//    private lateinit var newsMessageEmigratedLabel: Label

}