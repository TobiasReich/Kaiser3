package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import de.tobiasreich.kaiser.game.data.country.HarvestEvent
import de.tobiasreich.kaiser.game.data.player.HarvestReport
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessageHarvest : Initializable, IMessageController{

    @FXML
    private lateinit var harvestTitle: Label

    @FXML
    private lateinit var harvestSummaryLabel: Label

    /** The Message to be shown to the user */
    private lateinit var message: ReportMessage


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
    override fun initialize(p0: URL?, bundle: ResourceBundle?) { }

    override fun setMessage(message: ReportMessage) {
        println("setMessage")
        this.message = message
        updateView()
    }

    private fun updateView() {
        println("updateView")

        val harvestMessage = this.message as HarvestReport

        val messageTitleResource = when(harvestMessage.harvest) {
            HarvestCondition.FANTASTIC_HARVEST -> { "message_harvest_type_fantastic_harvest" }
            HarvestCondition.GOOD_HARVEST -> { "message_harvest_type_good_harvest" }
            HarvestCondition.NORMAL_HARVEST -> { "message_harvest_type_normal_harvest" }
            HarvestCondition.BAD_HARVEST -> { "message_harvest_type_bad_harvest" }
            HarvestCondition.TERRIBLE_HARVEST -> { "message_harvest_type_terrible_harvest" }
        }

        // val harvest: HarvestCondition, val harvestedFood : Int, val harvestEvent : HarvestEven
        val eventMessage = when (harvestMessage.harvestEvent){
            null -> { "message_harvest_event_nothing" }
            HarvestEvent.RATS_PLAGUE -> { "message_harvest_event_rats" }
            HarvestEvent.ROTTEN_FOOD -> { "message_harvest_event_rotten_food" }
            HarvestEvent.THEFT -> { "message_harvest_event_theft" }
        }

        harvestTitle.text = Game.resourcesBundle.getString(messageTitleResource)
        harvestSummaryLabel.text = String.format(Game.resourcesBundle.getString("message_harvest_summary"),
            harvestMessage.harvestedFood,  Game.resourcesBundle.getString(eventMessage), harvestMessage.totalFood)
    }

}