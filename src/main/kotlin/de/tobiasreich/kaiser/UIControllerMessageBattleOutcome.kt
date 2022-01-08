package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.BattleOutcome
import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.WarManager
import de.tobiasreich.kaiser.game.data.player.BattleMessage
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.WarDeclarationMessage
import de.tobiasreich.kaiser.game.data.player.WarDeclarationReactionMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.shape.Line
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessageBattleOutcome : Initializable, IMessageController{

    @FXML
    lateinit var warEstimationLabel: Label

    @FXML
    lateinit var peaceOfferTextField: TextField

    @FXML
    lateinit var playerTopLine: Line

    @FXML
    lateinit var playerBottomLine: Line

    @FXML
    lateinit var declaringPlayerLabel: Label

    private var peaceOfferAmount = 0

    /** The Message to generate the info shown to the user */
    private lateinit var message: WarDeclarationMessage


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
        proceedToNextNews()
    }

    private lateinit var bundle : ResourceBundle

    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!
    }

    override fun setMessage(message: ReportMessage) {
        this.message = message as WarDeclarationMessage
        updateView()
    }

    private fun updateView() {
        println("Show War declaration")

        // Who is declaring war?
        val declaringPlayer = message.declaringPlayer
        val playerTitle = if (declaringPlayer.isMale){
            Game.resourcesBundle.getString(declaringPlayer.playerTitle.resourceNameMale)
        } else {
            Game.resourcesBundle.getString(declaringPlayer.playerTitle.resourceNameFemale)
        }
        val playerCountry = Game.resourcesBundle.getString(declaringPlayer.country.nameResource)
        declaringPlayerLabel.style = ("-fx-text-fill: ${declaringPlayer.playerColor.toRGBCode()}; ")
        playerTopLine.stroke = declaringPlayer.playerColor
        playerBottomLine.stroke = declaringPlayer.playerColor
        declaringPlayerLabel.text = String.format(bundle.getString("war_declaration_message_initiator_message"), playerTitle, declaringPlayer.name, playerCountry)

        // War estimation for the defender
        val outcomeStringResource = when (WarManager.estimateBattleOutcome(Game.currentPlayer.military, message.units)){
            BattleOutcome.EASY_VICTORY -> { "war_declaration_message_estimation_easy_victory" }
            BattleOutcome.LIKELY_VICTORY -> { "war_declaration_message_estimation_potential_victory" }
            BattleOutcome.INDECISIVE -> {"war_declaration_message_estimation_indecisive"}
            BattleOutcome.POTENTIAL_LOSS -> { "war_declaration_message_estimation_potential_loss" }
            BattleOutcome.SURE_LOSS -> { "war_declaration_message_estimation_sure_loss" }
        }
        warEstimationLabel.text = bundle.getString(outcomeStringResource)

        // Peace offer
        peaceOfferTextField.textProperty().addListener { _, oldValue, newValue ->
            println("Text field updated: $oldValue -> $newValue")

            if (newValue.isEmpty()){
                // Nothing to do
            } else if (!newValue.matches("\\d*?".toRegex())) {
                // Set to former value (= ignore this input)
                peaceOfferTextField.text = oldValue
            } else {
                val maxAmount = Game.currentPlayer.money
                if (newValue.toInt() < 0) {
                    peaceOfferAmount = 0
                } else if (newValue.toInt() > maxAmount){
                    println("$newValue is too high. Setting to max: $maxAmount")
                    // Set to max value if user entered a higher one
                    peaceOfferTextField.text = maxAmount.toString()
                    peaceOfferAmount = maxAmount
                } else {
                    peaceOfferAmount = newValue.toInt()
                }
            }
        }
    }

    /** Player clicked on the button to accept war.
     *  Just proceed with the next messages */
    fun acceptWarButtonClick(actionEvent: ActionEvent) {
        // Add a battle message to the attacking player. The battle starts with the attacker's turn
        message.declaringPlayer.addMessage(BattleMessage(message.declaringPlayer, message.units, Game.currentPlayer, message.warGoal))
        proceedToNextNews()
    }


    /** Player wants to make a peace offer by offering [peaceOfferAmount] amount of money */
    fun makePeaceOfferButtonClick(actionEvent: ActionEvent) {
        message.declaringPlayer.addMessage(WarDeclarationReactionMessage(Game.currentPlayer, peaceOfferAmount, message.units, message.warGoal))
        proceedToNextNews()
    }
}
