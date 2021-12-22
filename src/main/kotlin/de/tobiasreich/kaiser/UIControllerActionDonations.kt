package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ComboBox
import java.net.URL
import java.util.*


/** Controller, specific for the Buildings actions */
class UIControllerActionDonations : Initializable {

    @FXML
    lateinit var playerSelectionCB: ComboBox<String>


    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    private lateinit var bundle: ResourceBundle

    private val player = Game.currentPlayer

    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!
        val options = FXCollections.observableArrayList<String>()
        Game.getAllOtherPlayers().forEach {
            options.add("${bundle.getString(it.country.nameResource)} (${it.name})")
        }

        playerSelectionCB.items = options
    }


    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }


}
