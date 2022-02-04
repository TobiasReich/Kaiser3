package de.tobiasreich.kaiser

import javafx.fxml.Initializable
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionDemographics : Initializable {

    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    private lateinit var bundle: ResourceBundle

    override fun initialize(p0: URL?, bundle: ResourceBundle) {
        this.bundle = bundle
    }

    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }


}