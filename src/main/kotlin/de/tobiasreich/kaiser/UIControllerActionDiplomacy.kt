package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.*
import de.tobiasreich.kaiser.game.DiplomacyManager.TREATY_EXPIRATION_TIME_YEARS
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*


/** Controller, specific for the Diplomacy actions
 *  In this view the player can make:
 *  - Peace treaties
 *    - Does not get enforced (You can still declare war) but a breaking of this will alarm all other leaders and make a bad reputation
 *  - Trade agreements
 *    - Allows sharing produced goods and therefore make money (for both countries)
 *  - Declare Alliances
 *    - Let every other leader know that these two countries are allies. Can be a warning for others - but also an invitation for war!
 */
class UIControllerActionDiplomacy : Initializable {

    @FXML
    lateinit var playerTreatyProposalsVBox: VBox

    @FXML
    lateinit var playerTreatiesVBox: VBox

    @FXML
    lateinit var offerTreatyButton: Button

    @FXML
    lateinit var treatyTypeCB: ComboBox<String>

    @FXML
    lateinit var playerSelectionCB: ComboBox<String>


    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    private lateinit var bundle: ResourceBundle

    private val currentPlayer = Game.currentPlayer
    private val players = Game.getAllButCurrentPlayer()
    private var selectedPlayer : Player? = null
    private var treatyType : TreatyType? = null


    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }


    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!

        // ----------- Players -----------
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
            updateMakeTreatyButtonViews()
        }


        // ----------- Treaty type -----------
        val treatyTypeNames = FXCollections.observableArrayList<String>()
        TreatyType.values().forEach {
            treatyTypeNames.add(bundle.getString(it.stringResource))
        }
        treatyTypeCB.items = treatyTypeNames

        // Listener to changes of the Resource type
        treatyTypeCB.valueProperty().addListener { _, _, _ ->
            val selectedIndex = treatyTypeCB.selectionModel.selectedIndex
            treatyType = TreatyType.values()[selectedIndex]
            updateMakeTreatyButtonViews()
        }

        updateTreatyList()
        updateMakeTreatyButtonViews()
    }


    private fun updateTreatyList() {
        println("adding treaty views")
        playerTreatiesVBox.children.clear()
        DiplomacyManager.getAllCurrentTreatiesForPlayer(currentPlayer, null).forEach {
            // Add a Treaty View
            println("Adding TreatyView for: $it")
            val treatyView = UIControllerViewTreaty(it, currentPlayer)
            playerTreatiesVBox.children.add(treatyView)
        }

        playerTreatyProposalsVBox.children.clear()
        DiplomacyManager.getAllProposalsForPlayer(currentPlayer, null).forEach {
            // Add a Treaty View
            println("Adding TreatyView for: $it")
            val treatyView = UIControllerViewTreaty(it, currentPlayer)
            playerTreatyProposalsVBox.children.add(treatyView)
        }

    }


    /** Enables / Disables the Button to offer a treaty */
    private fun updateMakeTreatyButtonViews() {
        offerTreatyButton.isDisable = selectedPlayer == null || treatyType == null
    }


    fun onOfferTreatyButtonClick() {
        val dialogTitle = bundle.getString("diplomacy_treaty_offer_dialog_title")
        val dialogMessage = bundle.getString("diplomacy_treaty_offer_dialog_message")
        val dialogAccepted = ViewController.showModalDialog(dialogTitle, dialogMessage)

        if (dialogAccepted){
            val treaty = Treaty(treatyType!!, currentPlayer, selectedPlayer!!, Game.currentYear + TREATY_EXPIRATION_TIME_YEARS)
            println("Offering a new treaty: $treaty")
            DiplomacyManager.addProposal(treaty)
//            println("Adding new Treaty $treaty")
//            DiplomacyManager.acceptProposal(treaty)
            updateTreatyList() // Update list for showing pending treaties?
        } else {
            println("No treaty offered")
            //Nothing to do for now
        }



    }

    fun onDiplomacyHelpClicked(mouseEvent: MouseEvent) {
        ViewController.showInfoPopUp(treatyTypeCB, bundle.getString("diplomacy_treaty_treaty_type_help_title"), bundle.getString("diplomacy_treaty_treaty_type_help_help"))
    }

}
