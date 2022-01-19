package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.DiplomacyManager
import de.tobiasreich.kaiser.game.TreatyType
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.TreatyOfferMessage
import de.tobiasreich.kaiser.game.data.player.TreatyOfferResponseMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.shape.Line
import java.net.URL
import java.util.*

/** This shows the message that another player is requesting an offer. */
class UIControllerMessageTreatyOffer : Initializable, IMessageController{

    @FXML
    lateinit var rejectTreatyButton: Button

    @FXML
    lateinit var acceptTreatyButton: Button

    @FXML
    lateinit var offeredTypeLabel: Label

    @FXML
    lateinit var offeringPlayerLabel: Label

    @FXML
    lateinit var playerTopLine: Line

    @FXML
    lateinit var playerBottomLine: Line


    /** The Message to be shown to the user */
    private lateinit var message: TreatyOfferMessage

    private lateinit var bundle: ResourceBundle

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle) {
        this.bundle = bundle
    }


    override fun setMessage(message: ReportMessage) {
        println("set treaty offer Message")
        this.message = message as TreatyOfferMessage
        updateView()
    }


    private fun updateView() {
        val messageSender = message.treaty.initiator
        // Requesting player
        val requestingPlayerTitle = if (messageSender.isMale){
            bundle.getString(messageSender.playerTitle.resourceNameMale)
        } else {
            bundle.getString(messageSender.playerTitle.resourceNameFemale)
        }
        val playerCountry = bundle.getString(messageSender.country.nameResource)
        offeringPlayerLabel.style = ("-fx-text-fill: ${messageSender.playerColor.toRGBCode()}; ")
        playerTopLine.stroke = messageSender.playerColor
        playerBottomLine.stroke = messageSender.playerColor
        offeringPlayerLabel.text = String.format(bundle.getString("treaty_offer_message_message"), requestingPlayerTitle, messageSender.name, playerCountry)

        // Type of treaty
        offeredTypeLabel.text = when(message.treaty.type){
            TreatyType.PEACE -> bundle.getString("treaty_offer_message_treaty_peace")
            TreatyType.TRADE -> bundle.getString("treaty_offer_message_treaty_trade")
            TreatyType.ALLIANCE -> bundle.getString("treaty_offer_message_treaty_alliance")
        }
    }


    fun onRejectTreatyButtonClick() {
        DiplomacyManager.rejectProposal(message.treaty)
        proceedToNextNews()
    }


    fun onAcceptTreatyButtonClick() {
        DiplomacyManager.acceptProposal(message.treaty)
        proceedToNextNews()
    }

}