package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Game.MAX_DONATION_AMOUNT
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.ResourceType
import de.tobiasreich.kaiser.game.data.military.SabotageType
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import utils.FxDialogs
import java.net.URL
import java.util.*


/** Controller, specific for the Buildings actions */
class UIControllerActionSabotage : Initializable {

    @FXML
    lateinit var startSabotageButton: Button

    @FXML
    lateinit var sabotageCostLabel: Label

    @FXML
    lateinit var sabotageTypeCB: ComboBox<String>

    @FXML
    lateinit var playerSelectionCB: ComboBox<String>


    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    private lateinit var bundle: ResourceBundle

    private val players = Game.getAllOtherPlayers()
    private var selectedPlayer : Player? = null
    private var selectedSabotage : SabotageType? = null


    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!

        // ----- PLAYER SELECTION -----
        val playerNames = FXCollections.observableArrayList<String>()
        Game.getAllOtherPlayers().forEach {
            playerNames.add("${it.name} (${bundle.getString(it.country.nameResource)})")
        }

        playerSelectionCB.items = playerNames

        // Listener to changes of the selected player
        playerSelectionCB.valueProperty().addListener { _, _, _ ->
            val selectedIndex = playerSelectionCB.selectionModel.selectedIndex
            selectedPlayer = players[selectedIndex]
            println("Index: $selectedIndex / $selectedPlayer")
            updatePriceCalculation()
        }

        // ----- SABOTAGE TYPE -----

        val sabotageOptions = FXCollections.observableArrayList<String>()
        SabotageType.values().forEach {
            sabotageOptions.add(bundle.getString(it.nameRes))
        }
        sabotageTypeCB.items = sabotageOptions

        // Listener to changes of the sabotage type
        sabotageTypeCB.valueProperty().addListener { _, _, _ ->
            val selectedIndex = sabotageTypeCB.selectionModel.selectedIndex
            selectedSabotage = SabotageType.values()[selectedIndex]
            println("Index: $selectedIndex / $selectedSabotage")
            updatePriceCalculation()
        }

        updatePriceCalculation()
    }

    /** Updates the price calculation for the planned sabotage */
    private fun updatePriceCalculation() {
        val player = selectedPlayer
        val sabotage = selectedSabotage
        if (player == null || sabotage == null){
            // Selection not complete
            sabotageCostLabel.text = "---"
            startSabotageButton.isDisable = true
            return
        }
        startSabotageButton.isDisable = false

        val price = getSabotageCost(player, sabotage)
        println("Sabotage costs: $price")
        sabotageCostLabel.text = "$price ${Game.resourcesBundle.getString("general_currency")}"
    }


    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }


    fun onStartSabotageButtonClick(actionEvent: ActionEvent) {
        val targetPlayer = selectedPlayer!!
        val sabotage = selectedSabotage!!

        val result = ViewController.showModalDialog()

        if (result == FxDialogs.DialogResult.OK){
            println("Donation made")
            Game.currentPlayer.sabotagePlayer(targetPlayer, sabotage, getSabotageCost(targetPlayer, sabotage))
            updateCallback()
            //TODO We might want to block the donation screen for the rest of the turn.
        } else {
            println("Donation NOT made")
            //Nothing to do for now
        }
    }


    /** Calculates the cost of the sabotage, depending on the action and level of the player */
    private fun getSabotageCost(player: Player, sabotageType: SabotageType) : Int {
        return sabotageType.cost * (player.playerTitle.ordinal + 1)
    }

}
