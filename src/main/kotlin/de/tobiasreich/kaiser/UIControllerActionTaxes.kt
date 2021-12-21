package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Slider
import javafx.scene.input.MouseEvent
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionTaxes : Initializable {

    @FXML
    private lateinit var educationSlider: Slider

    @FXML
    private lateinit var healthSlider: Slider

    @FXML
    private lateinit var immigrationSlider: Slider

    @FXML
    private lateinit var taxLawEnforcementSlider: Slider

    @FXML
    private lateinit var incomeTaxSlider: Slider

    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        incomeTaxSlider.value = Game.currentPlayer.incomeTax
        taxLawEnforcementSlider.value = Game.currentPlayer.lawEnforcement
        immigrationSlider.value = Game.currentPlayer.immigrationStrictness
        healthSlider.value = Game.currentPlayer.healthSystem
        educationSlider.value = Game.currentPlayer.educationSystem
    }

    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }

    fun incomeTaxChanged() {
        println("Income tax changed! ${incomeTaxSlider.value}.")
        Game.currentPlayer.incomeTax = incomeTaxSlider.value
        updateCallback.invoke()
    }

    fun lawEnforcementChanged(mouseEvent: MouseEvent) {
        println("Tax law enforcement tax changed! ${taxLawEnforcementSlider.value}.")
        Game.currentPlayer.lawEnforcement = taxLawEnforcementSlider.value
        updateCallback.invoke()
    }

    fun immigrationChanged(mouseEvent: MouseEvent) {
        println("Immigration law tax changed! ${immigrationSlider.value}.")
        Game.currentPlayer.immigrationStrictness = immigrationSlider.value
        updateCallback.invoke()
    }

    fun healthChanged(mouseEvent: MouseEvent) {
        println("Health system changed! ${healthSlider.value}.")
        Game.currentPlayer.healthSystem = healthSlider.value
        updateCallback.invoke()
    }

    fun educationChanged(mouseEvent: MouseEvent) {
        println("Education expenses changed! ${educationSlider.value}.")
        Game.currentPlayer.educationSystem = educationSlider.value
        updateCallback.invoke()
    }


}