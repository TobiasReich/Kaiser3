package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.WarDeclarationMessage
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.shape.Line
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessageWarDeclaration : Initializable, IMessageController{

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
        // Estimate sabotage success
//     /*   val sabotageSuccess = message.sabotageType.difficulty < Math.random()
//
//        var messageText = ""
//        var confessionText = ""
//
//        // If the mission failed, check if the apprehended spy will talk
//        if (!sabotageSuccess){
//            messageText = bundle.getString(message.sabotageType.sabotageText)
//
//            //TODO We might want to add the internal security here (laws, perks...)
//            if(message.sabotageType.confess < Math.random()){
//                val titleString = if(message.sabotagingPlayer.isMale){
//                    bundle.getString(message.sabotagingPlayer.playerTitle.resourceNameMale)
//                } else {
//                    bundle.getString(message.sabotagingPlayer.playerTitle.resourceNameFemale)
//                }
//                val countryName = bundle.getString(message.sabotagingPlayer.country.nameResource)
//
//                confessionText = String.format(bundle.getString(message.sabotageType.confessionText), titleString,
//                    message.sabotagingPlayer.name, countryName)
//            }
//        } else {
//            messageText = bundle.getString(message.sabotageType.accidentText)
//        }
//
//        // Only execute the consequences when the sabotage was effective
//        if (sabotageSuccess) {
//            val player = Game.currentPlayer
//            when (message.sabotageType) {
//                SabotageType.STEAL_MONEY -> {
//                    player.money = (player.money.toDouble() * 0.9).toInt()
//                }
//                SabotageType.BURN_MILLS -> {
//                    player.land.buildings.destroyMill()
//                    player.land.buildings.updateUsedBuildings(player.population)
//                }
//                SabotageType.START_REVOLT -> {
//                    player.population.addMoodBonus(REVOLT_MOOD_REDUCTION_FACTOR)
//                    player.calculateMood()
//                }
//                SabotageType.DEMORALIZE_TROOPS -> {
//                    player.addMoodBonus(MILITARY_MORALE_REDUCTION_FACTOR)
//                }
//            }
//        }
//
//        sabotageTitle.text = bundle.getString(message.sabotageType.messageTitle)
//        sabotageSummary.text = messageText
//
//        sabotageSummaryConfession.style = ("-fx-text-fill: ${message.sabotagingPlayer.playerColor.toRGBCode()}; ")
//        sabotageSummaryConfession.text = confessionText*/
    }

}