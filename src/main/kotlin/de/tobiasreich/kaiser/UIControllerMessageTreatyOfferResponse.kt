package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.TreatyType
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.TreatyOfferResponseMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.shape.Line
import java.net.URL
import java.util.*

/** This shows the message that another player is requesting an offer. */
class UIControllerMessageTreatyOfferResponse : Initializable, IMessageController{

    @FXML
    lateinit var responseLabel: Label

    @FXML
    lateinit var respondingPlayerLabel: Label

    @FXML
    lateinit var playerTopLine: Line

    @FXML
    lateinit var playerBottomLine: Line


    /** The Message to be shown to the user */
    private lateinit var message: TreatyOfferResponseMessage

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
        this.message = message as TreatyOfferResponseMessage
        updateView()
    }


    private fun updateView() {
        val respondingPlayer = message.treaty.receiver
        // Requesting player
        val requestingPlayerTitle = if (respondingPlayer.isMale){
            bundle.getString(respondingPlayer.playerTitle.resourceNameMale)
        } else {
            bundle.getString(respondingPlayer.playerTitle.resourceNameFemale)
        }
        val playerCountry = bundle.getString(respondingPlayer.country.nameResource)
        respondingPlayerLabel.style = ("-fx-text-fill: ${respondingPlayer.playerColor.toRGBCode()}; ")
        playerTopLine.stroke = respondingPlayer.playerColor
        playerBottomLine.stroke = respondingPlayer.playerColor
        respondingPlayerLabel.text = String.format(bundle.getString("treaty_offer_response_message_message"), requestingPlayerTitle, respondingPlayer.name, playerCountry)

        // Type of treaty
        if (message.accepted){
            responseLabel.text = when (message.treaty.type) {
                TreatyType.PEACE -> bundle.getString("treaty_offer_response_message_peace_accepted")
                TreatyType.TRADE -> bundle.getString("treaty_offer_response_message_trade_accepted")
                TreatyType.ALLIANCE -> bundle.getString("treaty_offer_response_message_alliance_accepted")
            }
        } else {
            responseLabel.text = when (message.treaty.type) {
                TreatyType.PEACE -> bundle.getString("treaty_offer_response_message_peace_rejected")
                TreatyType.TRADE -> bundle.getString("treaty_offer_response_message_trade_rejected")
                TreatyType.ALLIANCE -> bundle.getString("treaty_offer_response_message_alliance_rejected")
            }
        }
    }

}