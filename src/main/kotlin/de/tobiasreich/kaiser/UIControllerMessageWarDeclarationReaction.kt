package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.BattleOutcome
import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.TroopMovement
import de.tobiasreich.kaiser.game.WarManager
import de.tobiasreich.kaiser.game.data.player.BattleMessage
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.WarDeclarationReactionMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.shape.Line
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessageWarDeclarationReaction : Initializable, IMessageController{

    @FXML
    lateinit var peaceOfferLabel: Label

    @FXML
    lateinit var warEstimationLabel: Label

    @FXML
    lateinit var playerTopLine: Line

    @FXML
    lateinit var playerBottomLine: Line

    @FXML
    lateinit var reactingPlayerLabel: Label


    /** The Message to generate the info shown to the user */
    private lateinit var message: WarDeclarationReactionMessage


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
        this.message = message as WarDeclarationReactionMessage
        updateView()
    }

    private fun updateView() {
        println("Show War declaration")

        // Who is declaring war?
        val reactingPlayer = message.reactingPlayer
        val playerTitle = if (reactingPlayer.isMale){
            Game.resourcesBundle.getString(reactingPlayer.playerTitle.resourceNameMale)
        } else {
            Game.resourcesBundle.getString(reactingPlayer.playerTitle.resourceNameFemale)
        }
        val playerCountry = Game.resourcesBundle.getString(reactingPlayer.country.nameResource)
        reactingPlayerLabel.style = ("-fx-text-fill: ${reactingPlayer.playerColor.toRGBCode()}; ")
        playerTopLine.stroke = reactingPlayer.playerColor
        playerBottomLine.stroke = reactingPlayer.playerColor
        reactingPlayerLabel.text = String.format(bundle.getString("war_declaration_reaction_message_initiator_message"), playerTitle, reactingPlayer.name, playerCountry)

        // War estimation for the defender
        val outcomeStringResource = when (WarManager.estimateBattleOutcome(Game.currentPlayer.military, message.reactingPlayer.military)){
            BattleOutcome.EASY_VICTORY -> { "war_declaration_message_estimation_easy_victory" }
            BattleOutcome.LIKELY_VICTORY -> { "war_declaration_message_estimation_potential_victory" }
            BattleOutcome.INDECISIVE -> {"war_declaration_message_estimation_indecisive"}
            BattleOutcome.POTENTIAL_LOSS -> { "war_declaration_message_estimation_potential_loss" }
            BattleOutcome.SURE_LOSS -> { "war_declaration_message_estimation_sure_loss" }
        }
        warEstimationLabel.text = bundle.getString(outcomeStringResource)

        // War estimation for the defender
        peaceOfferLabel.text = String.format(bundle.getString("war_declaration_reaction_peace_offer_text"), message.peaceOfferAmount)
    }

    /** The player does not accept the peace offer.
     *  Starts the battle! */
    fun startWarButtonClick() {
        Game.currentPlayer.addMessageToFrontOfList(BattleMessage(Game.currentPlayer, message.returningUnits, message.reactingPlayer, message.warGoal))
        proceedToNextNews()
    }

    /** Player accepts peace offer.
     *  Troops are coming home in the next turn. */
    fun acceptPeaceOfferButtonClick() {
        //TODO Add peace treaty between both players

        // Military is coming home from the battlefield
        WarManager.addTroopMovement(TroopMovement(message.reactingPlayer, Game.currentPlayer, message.returningUnits, null, 0, null))
        proceedToNextNews()
    }
}
