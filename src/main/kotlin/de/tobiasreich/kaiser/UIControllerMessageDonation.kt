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
class UIControllerMessageDonation : Initializable, IMessageController{

    @FXML
    lateinit var playerTopLine: Line

    @FXML
    lateinit var playerBottomLine: Line

    @FXML
    lateinit var donationSummaryPlayerLabel: Label

    @FXML
    lateinit var donationSummaryAmountLabel: Label


    /** The Message to be shown to the user */
    private lateinit var message: DonationMessage


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle?) { }

    override fun setMessage(message: ReportMessage) {
        println("set donation Message")
        this.message = message as DonationMessage
        updateView()
    }

    private fun updateView() {
        println("updateView")

        val donatingPlayer = message.donatingPlayer
        //donation_message_message=%s %s von %s bietet euch eine noble Spende
        //donation_message_message_donation=Wollt ihr die %d %s annehmen?
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

        donationSummaryPlayerLabel.style = ("-fx-text-fill: ${donatingPlayer.playerColor.toRGBCode()}; ")
        playerTopLine.stroke = donatingPlayer.playerColor
        playerBottomLine.stroke = donatingPlayer.playerColor

        donationSummaryPlayerLabel.text = String.format(Game.resourcesBundle.getString("donation_message_message"), playerTitle, donatingPlayer.name, playerCountry)
        donationSummaryAmountLabel.text = String.format(Game.resourcesBundle.getString("donation_message_message_donation"),  message.donationAmount, selectedResource)
    }

    fun onDonationRejectButtonClick(actionEvent: ActionEvent) {
        //Send a donation reaction message to other placer (REJECTED) -> Refund donation
        message.donatingPlayer.addMessage(DonationReactionMessage(Game.currentPlayer, message.selectedResource, message.donationAmount, message.people,false))
        proceedToNextNews()
    }

    fun onDonationAcceptButtonClick(actionEvent: ActionEvent) {
        //Send a donation reaction message to other placer (ACCEPTED)
        val player = Game.currentPlayer
        println("Current player: $player")
        message.donatingPlayer.addMessage(DonationReactionMessage(player, message.selectedResource, message.donationAmount, message.people,true))

        when (message.selectedResource){
            ResourceType.MONEY -> { player.money += message.donationAmount }
            ResourceType.LAND -> { player.land.landSize += message.donationAmount}
            ResourceType.POPULATION -> {
                player.population.addAdults(message.people!!)
                player.land.buildings.updateUsedBuildings(player.population)
            }
            ResourceType.FOOD -> { player.addFood(message.donationAmount) }
        }

        proceedToNextNews()
    }

}