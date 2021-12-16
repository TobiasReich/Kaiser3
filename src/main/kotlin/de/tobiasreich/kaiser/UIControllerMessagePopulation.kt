package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.player.PopulationReport
import de.tobiasreich.kaiser.game.data.player.ReportMessage
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

    private var bundle: ResourceBundle? = null

    @FXML
    override fun initialize(p0: URL?, resources: ResourceBundle?) {
        bundle = resources
        println(bundle)
        println(bundle?.getString("key1"))
    }

    override fun setMessage(message: ReportMessage) {
        this.messge = message
        val popMsg = messge as PopulationReport

        newsMessageBornLabel.text = String.format(Game.stringsBundle.getString("message_born_children"), popMsg.birth)
        newsMessageDiedAgeLabel.text = String.format(Game.stringsBundle.getString("message_died_age"), popMsg.diedOfAge)
        newsMessageDiedBadHealthLabel.text = String.format(Game.stringsBundle.getString("message_died_bad_health"), popMsg.diedOfHealth)
        newsMessageImmimigratedLabel.text = String.format(Game.stringsBundle.getString("message_immigrated"), popMsg.immigrated)
        newsMessageEmigratedLabel.text = String.format(Game.stringsBundle.getString("message_emigrated"), popMsg.emigrated)

        if (popMsg.totalChange > 0){
            populationMessageTitle.text = Game.stringsBundle.getString("message_title_population_grows")
            newsMessageTotalChange.text = String.format(Game.stringsBundle.getString("message_population_difference_grows"), popMsg.totalChange)

        } else if (popMsg.totalChange < 0){
            populationMessageTitle.text = Game.stringsBundle.getString("message_title_population_shrinks")
            newsMessageTotalChange.text = String.format(Game.stringsBundle.getString("message_population_difference_shrinks"), popMsg.totalChange * -1)

        } else {
            populationMessageTitle.text = Game.stringsBundle.getString("message_title_population_stays_constant")
            newsMessageTotalChange.text = Game.stringsBundle.getString("message_population_difference_stays_constant")
        }
    }

}