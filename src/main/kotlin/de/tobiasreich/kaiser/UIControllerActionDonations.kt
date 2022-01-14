package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Game.MAX_DONATION_AMOUNT
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.ResourceType
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import de.tobiasreich.kaiser.game.utils.FxDialogs
import java.net.URL
import java.util.*


/** Controller, specific for the Buildings actions */
class UIControllerActionDonations : Initializable {


    @FXML
    lateinit var donateButton: Button

    @FXML
    lateinit var donationTypeLabel: Label

    @FXML
    lateinit var donationAmountSlider: Slider

    @FXML
    lateinit var donationAmountTextField: TextField

    @FXML
    lateinit var donationTypeCB: ComboBox<String>

    @FXML
    lateinit var playerSelectionCB: ComboBox<String>


    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    private lateinit var bundle: ResourceBundle

    private val players = Game.getAllOtherPlayers()
    private var selectedPlayer : Player? = null
    private var selectedResource : ResourceType? = null
    private var donationAmount: Int = 0


    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!

        val playerNames = FXCollections.observableArrayList<String>()
        players.forEach {
            playerNames.add("${it.name} (${bundle.getString(it.country.nameResource)})")
        }

        playerSelectionCB.items = playerNames

        // Listener to changes of the selected player
        playerSelectionCB.valueProperty().addListener { _, _, _ ->
            val selectedIndex = playerSelectionCB.selectionModel.selectedIndex
            selectedPlayer = players[selectedIndex]
            println("Index: $selectedIndex / $selectedPlayer")
            updateDonateButtonStatus()
        }

        val resourceNames = FXCollections.observableArrayList<String>()
        ResourceType.values().forEach {
            val string = when(it){
                ResourceType.MONEY -> { "resource_money" }
                ResourceType.LAND -> { "resource_land" }
                ResourceType.POPULATION -> { "resource_population" }
                ResourceType.FOOD -> { "resource_food" }
            }
            resourceNames.add(bundle.getString(string))
        }
        donationTypeCB.items = resourceNames

        // Listener to changes of the Resource type
        donationTypeCB.valueProperty().addListener { _, _, _ ->
            val selectedIndex = donationTypeCB.selectionModel.selectedIndex
            selectedResource =  ResourceType.values()[selectedIndex]

            updateDonationTypeText() //Sets the label to the right resource (e.g. population = "slaves")

            // Reset the donated amount so players don't accidentally donate too much of a wrong resource
            donationAmount = 0
            donationAmountSlider.value = 0.0
            donationAmountTextField.text = "0"

            donationAmountSlider.max = when(selectedResource){
                ResourceType.MONEY -> Game.currentPlayer.money * MAX_DONATION_AMOUNT
                ResourceType.LAND -> Game.currentPlayer.land.landSize * MAX_DONATION_AMOUNT
                ResourceType.POPULATION -> Game.currentPlayer.population.adults.size * MAX_DONATION_AMOUNT
                ResourceType.FOOD -> Game.currentPlayer.storedFood * MAX_DONATION_AMOUNT
                null -> 0.0
            }

            println("Selected Resource: $selectedResource")
            updateDonationAmountText()
            updateDonateButtonStatus()
        }

        // Listener that ignores the input of the TextField when it's not a number
        donationAmountTextField.textProperty().addListener { _, oldValue, newValue ->
            println("Text field updated: $oldValue -> $newValue")

            if (newValue.isEmpty()){
                // Nothing to do
            } else if (!newValue.matches("\\d*?".toRegex())) {
                // Set to former value (= ignore this input)
                donationAmountTextField.text = oldValue
            } else {
                //TODO What about negative money? We need a minimum of 0!!!
                val maxAmount =  when(selectedResource){
                    ResourceType.MONEY -> Game.currentPlayer.money * MAX_DONATION_AMOUNT
                    ResourceType.LAND -> Game.currentPlayer.land.landSize * MAX_DONATION_AMOUNT
                    ResourceType.POPULATION -> Game.currentPlayer.population.adults.size * MAX_DONATION_AMOUNT
                    ResourceType.FOOD -> Game.currentPlayer.storedFood * MAX_DONATION_AMOUNT
                    null -> 0.0
                }
                if (newValue.toInt() > maxAmount){
                    println("$newValue is too high. Setting to max: $maxAmount")
                    // Set to max value if user entered a higher one
                    donationAmountTextField.text = maxAmount.toInt().toString()
                    donationAmountSlider.value = maxAmount
                    donationAmount = maxAmount.toInt()
                } else {
                    donationAmountSlider.value = newValue.toDouble()
                    donationAmount = newValue.toInt()
                }
            }
        }

        // update view at start so there are no "empty" values
        updateDonationTypeText()
        updateDonationAmountText()
        updateDonateButtonStatus()
    }


    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }


    fun onDonationAmountChanged(mouseEvent: MouseEvent) {
        println("Slider value changed to ${donationAmountSlider.value.toInt()}")
        donationAmount = donationAmountSlider.value.toInt()
        updateDonationAmountText()
    }

    private fun updateDonationTypeText() {
        val amountString = when(selectedResource){
            ResourceType.MONEY -> { bundle.getString("donation_summary_resource_money") }
            ResourceType.LAND -> {  bundle.getString("donation_summary_resource_land")}
            ResourceType.POPULATION -> { bundle.getString("donation_summary_resource_population")}
            ResourceType.FOOD -> {  bundle.getString("donation_summary_resource_food")}
            null -> { "---" }
        }
        donationTypeLabel.text = amountString
    }

    private fun updateDonationAmountText() {
        donationAmountTextField.text = donationAmount.toString()
    }

    private fun updateDonateButtonStatus() {
        donateButton.isDisable = selectedPlayer == null || selectedResource == null
    }

    fun onSendDonationButtonClick(actionEvent: ActionEvent) {
        val result = ViewController.showModalDialog()

        if (result == FxDialogs.DialogResult.OK){
            println("Donation made")
            Game.currentPlayer.donateResource(selectedPlayer!!, selectedResource!!, donationAmount)
            updateCallback()
            //TODO We might want to block the donation screen for the rest of the turn.
        } else {
            println("Donation NOT made")
            //Nothing to do for now
        }
    }

}
