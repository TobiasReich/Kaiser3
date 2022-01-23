package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.TreatyType
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.TreatyRumorsMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*

/** This shows the message that another player is requesting an offer. */
class UIControllerMessageTreatyRumor : Initializable, IMessageController{

    @FXML
    lateinit var tratyRumorLabel: Label

    @FXML
    lateinit var tratyCountry2: Label

    @FXML
    lateinit var tratyCountry1: Label




    /** The Message to be shown to the user */
    private lateinit var message: TreatyRumorsMessage

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
        this.message = message as TreatyRumorsMessage
        updateView()
    }


    private fun updateView() {
        val player1 = message.treaty.receiver
        val player2 = message.treaty.initiator

        tratyCountry1.text = bundle.getString(player1.country.nameResource)
        tratyCountry1.style = ("-fx-text-fill: ${player1.playerColor.toRGBCode()}; ")

        tratyCountry2.text = bundle.getString(player2.country.nameResource)
        tratyCountry2.style = ("-fx-text-fill: ${player2.playerColor.toRGBCode()}; ")

        tratyRumorLabel.text = when (message.treaty.type){
            TreatyType.PEACE -> {
                if (message.wasStarted){
                    bundle.getString("treaty_rumor_treaty_message_peace_start")
                } else {
                    bundle.getString("treaty_rumor_treaty_message_peace_end")
                }
            }
            TreatyType.TRADE -> {
                if (message.wasStarted){
                    bundle.getString("treaty_rumor_treaty_message_trade_start")
                } else {
                    bundle.getString("treaty_rumor_treaty_message_trade_end")
                }
            }
            TreatyType.ALLIANCE -> {
                if (message.wasStarted){
                    bundle.getString("treaty_rumor_treaty_message_alliance_start")
                } else {
                    bundle.getString("treaty_rumor_treaty_message_alliance_end")
                }
            }
        }
    }

}