package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.TreatyType
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.TreatyOfferMessage
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
        offeredTypeLabel.text = when(message.type){
            TreatyType.PEACE -> bundle.getString("treaty_offer_message_treaty_peace")
            TreatyType.TRADE -> bundle.getString("treaty_offer_message_treaty_trade")
            TreatyType.ALLIANCE -> bundle.getString("treaty_offer_message_treaty_alliance")
        }
    }

    fun onRejectTreatyButtonClick() {
        println("reject treaty")
        //TODO sent response message to the initiating player
        proceedToNextNews()
    }

    fun onAcceptTreatyButtonClick() {
        println("accept treaty")
        //TODO sent response message to the initiating player
        proceedToNextNews()
    }

}