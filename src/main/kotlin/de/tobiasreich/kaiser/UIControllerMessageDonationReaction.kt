package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.ResourceType
import de.tobiasreich.kaiser.game.data.player.DonationMessage
import de.tobiasreich.kaiser.game.data.player.DonationReactionMessage
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import utils.FXUtils.FxUtils.toRGBCode
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessageDonationReaction : Initializable, IMessageController{

    @FXML
    lateinit var playerTopLine: Line

    @FXML
    lateinit var playerBottomLine: Line

    @FXML
    lateinit var reactingPlayerLabel: Label

    @FXML
    lateinit var reactionLabel: Label


    /** The Message to be shown to the user */
    private lateinit var message: DonationReactionMessage


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    private fun proceedToNextNews() {
        val message = Game.currentPlayer.getNextMessage()

        if (message == null){
            ViewController.showGameScene()
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
        println("set donation Message")
        this.message = message as DonationReactionMessage
        updateView()
    }

    private fun updateView() {
        println("updateView")

        val donatingPlayer = message.respondingPlayer

        val playerTitle = if (donatingPlayer.isMale){
            Game.resourcesBundle.getString(donatingPlayer.playerTitle.resourceNameMale)
        } else {
            Game.resourcesBundle.getString(donatingPlayer.playerTitle.resourceNameFemale)
        }
        val playerCountry = Game.resourcesBundle.getString(donatingPlayer.country.nameResource)

        val selectedResource = when(message.selectedResource){
            ResourceType.MONEY -> { Game.resourcesBundle.getString("donation_summary_resource_money") }
            ResourceType.LAND -> {  Game.resourcesBundle.getString("donation_summary_resource_land")}
            ResourceType.POPULATION -> { Game.resourcesBundle.getString("donation_summary_resource_population")}
            ResourceType.FOOD -> {  Game.resourcesBundle.getString("donation_summary_resource_food")}
        }

        reactingPlayerLabel.style = ("-fx-text-fill: ${donatingPlayer.playerColor.toRGBCode()}; ")
        playerTopLine.stroke = donatingPlayer.playerColor
        playerBottomLine.stroke = donatingPlayer.playerColor

        reactingPlayerLabel.text = String.format(Game.resourcesBundle.getString("donation_reaction_message_sender"), playerTitle, donatingPlayer.name, playerCountry)

        if (message.accepted) {
            reactionLabel.text = Game.resourcesBundle.getString("donation_message_message_reaction_accepted")
        } else {
            reactionLabel.text = Game.resourcesBundle.getString("donation_message_message_reaction_rejected")
        }
    }

    fun onNewsButtonClick(actionEvent: ActionEvent) {
        if (!message.accepted) {
            val player = Game.currentPlayer
            when (message.selectedResource){
                ResourceType.MONEY -> { player.money += message.donationAmount }
                ResourceType.LAND -> { player.land.landSize += message.donationAmount}
                ResourceType.POPULATION -> { player.population.addAdults(message.people!!) }
                ResourceType.FOOD -> { player.addFood(message.donationAmount) }
            }
        }
        proceedToNextNews()
    }

}