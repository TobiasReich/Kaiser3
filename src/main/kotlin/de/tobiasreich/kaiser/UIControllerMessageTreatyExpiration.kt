package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.TreatyType
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.TreatyExpirationMessage
import de.tobiasreich.kaiser.game.data.player.TreatyOfferResponseMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.shape.Line
import java.net.URL
import java.util.*

/** This shows the message that another player is requesting an offer. */
class UIControllerMessageTreatyExpiration : Initializable, IMessageController{

    @FXML
    lateinit var respondingPlayerLabel: Label

    @FXML
    lateinit var playerTopLine: Line

    @FXML
    lateinit var playerBottomLine: Line


    /** The Message to be shown to the user */
    private lateinit var message: TreatyExpirationMessage

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
        println("set treaty expiration Message")
        this.message = message as TreatyExpirationMessage
        updateView()
    }


    private fun updateView() {
        val player = Game.currentPlayer
        val otherPlayer = if (message.treaty.initiator == player){
            message.treaty.receiver
        } else {
            message.treaty.initiator
        }

        // Requesting player
        val requestingPlayerTitle = if (otherPlayer.isMale){
            bundle.getString(otherPlayer.playerTitle.resourceNameMale)
        } else {
            bundle.getString(otherPlayer.playerTitle.resourceNameFemale)
        }
        val playerCountry = bundle.getString(otherPlayer.country.nameResource)
        respondingPlayerLabel.style = ("-fx-text-fill: ${otherPlayer.playerColor.toRGBCode()}; ")
        playerTopLine.stroke = otherPlayer.playerColor
        playerBottomLine.stroke = otherPlayer.playerColor
        respondingPlayerLabel.text = String.format(bundle.getString("treaty_offer_expiration_message_message"),
            bundle.getString(message.treaty.type.stringResource), requestingPlayerTitle, otherPlayer.name, playerCountry)

    }

}