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
    private lateinit var newsMessageStarvedToDeathLabel: Label

    @FXML
    private lateinit var newsMessageTotalChange: Label


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
        proceedToNextNews()
    }

    @FXML
    override fun initialize(p0: URL?, resources: ResourceBundle?) { }

    override fun setMessage(message: ReportMessage) {
        this.messge = message
        val popMsg = messge as PopulationReport

        newsMessageBornLabel.text = String.format(Game.resourcesBundle.getString("message_born_children"), popMsg.birth)
        newsMessageDiedAgeLabel.text = String.format(Game.resourcesBundle.getString("message_died_age"), popMsg.diedOfAge)
        newsMessageDiedBadHealthLabel.text = String.format(Game.resourcesBundle.getString("message_died_bad_health"), popMsg.diedOfHealth)
        newsMessageImmimigratedLabel.text = String.format(Game.resourcesBundle.getString("message_immigrated"), popMsg.immigrated)
        newsMessageEmigratedLabel.text = String.format(Game.resourcesBundle.getString("message_emigrated"), popMsg.emigrated)
        newsMessageStarvedToDeathLabel.text = String.format(Game.resourcesBundle.getString("message_starved_to_death"), popMsg.starvedToDeath)

        if (popMsg.totalChange > 0){
            populationMessageTitle.text = Game.resourcesBundle.getString("message_title_population_grows")
            newsMessageTotalChange.text = String.format(Game.resourcesBundle.getString("message_population_difference_grows"), popMsg.totalChange)

        } else if (popMsg.totalChange < 0){
            populationMessageTitle.text = Game.resourcesBundle.getString("message_title_population_shrinks")
            newsMessageTotalChange.text = String.format(Game.resourcesBundle.getString("message_population_difference_shrinks"), popMsg.totalChange * -1)

        } else {
            populationMessageTitle.text = Game.resourcesBundle.getString("message_title_population_stays_constant")
            newsMessageTotalChange.text = Game.resourcesBundle.getString("message_population_difference_stays_constant")
        }
    }

}