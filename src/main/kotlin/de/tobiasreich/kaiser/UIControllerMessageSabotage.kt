package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.military.SabotageType
import de.tobiasreich.kaiser.game.data.military.SabotageType.Companion.MILITARY_MORALE_REDUCTION_FACTOR
import de.tobiasreich.kaiser.game.data.military.SabotageType.Companion.REVOLT_MOOD_REDUCTION_FACTOR
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.SabotageMessage
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import utils.FXUtils.FxUtils.toRGBCode
import java.net.URL
import java.util.*

/** This basically shows the empty screen where the news are presented, once the player starts the turn */
class UIControllerMessageSabotage : Initializable, IMessageController{


    @FXML
    private lateinit var sabotageTitle: Label

    @FXML
    private lateinit var sabotageSummary: Label

    @FXML
    private lateinit var sabotageSummaryConfession: Label

    /** The Message to generate the info shown to the user */
    private lateinit var message: SabotageMessage


    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/


    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
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

    private lateinit var bundle : ResourceBundle

    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!
    }

    override fun setMessage(message: ReportMessage) {
        this.message = message as SabotageMessage
        updateView()
    }

    private fun updateView() {
        // Estimate sabotage success
        val sabotageSuccess = message.sabotageType.difficulty < Math.random()

        var messageText = ""
        var confessionText = ""

        // If the mission failed, check if the apprehended spy will talk
        if (!sabotageSuccess){
            messageText = bundle.getString(message.sabotageType.sabotageText)

            //TODO We might want to add the internal security here (laws, perks...)
            if(message.sabotageType.confess < Math.random()){
                val titleString = if(message.sabotagingPlayer.isMale){
                    bundle.getString(message.sabotagingPlayer.playerTitle.resourceNameMale)
                } else {
                    bundle.getString(message.sabotagingPlayer.playerTitle.resourceNameFemale)
                }
                val countryName = bundle.getString(message.sabotagingPlayer.country.nameResource)

                confessionText = String.format(bundle.getString(message.sabotageType.confessionText), titleString,
                    message.sabotagingPlayer.name, countryName)
            }
        } else {
            messageText = bundle.getString(message.sabotageType.accidentText)
        }

        // Only execute the consequences when the sabotage was effective
        if (sabotageSuccess) {
            val player = Game.currentPlayer
            when (message.sabotageType) {
                SabotageType.STEAL_MONEY -> {
                    player.money = (player.money.toDouble() * 0.9).toInt()
                }
                SabotageType.BURN_MILLS -> {
                    player.land.buildings.destroyMill()
                    player.land.buildings.updateUsedBuildings(player.population)
                }
                SabotageType.START_REVOLT -> {
                    player.population.addMoodBonus(REVOLT_MOOD_REDUCTION_FACTOR)
                    player.calculateMood()
                }
                SabotageType.DEMORALIZE_TROOPS -> {
                    player.addMoodBonus(MILITARY_MORALE_REDUCTION_FACTOR)
                }
            }
        }

        sabotageTitle.text = bundle.getString(message.sabotageType.messageTitle)
        sabotageSummary.text = messageText

        sabotageSummaryConfession.style = ("-fx-text-fill: ${message.sabotagingPlayer.playerColor.toRGBCode()}; ")
        sabotageSummaryConfession.text = confessionText
    }

}