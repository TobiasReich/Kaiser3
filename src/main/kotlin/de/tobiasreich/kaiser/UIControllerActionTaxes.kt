package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
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

    @FXML
    private lateinit var estimatedIncomeLabel: Label


    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        incomeTaxSlider.value = Game.currentPlayer.laws.incomeTax
        taxLawEnforcementSlider.value = Game.currentPlayer.laws.lawEnforcement
        immigrationSlider.value = Game.currentPlayer.laws.immigrationStrictness
        healthSlider.value = Game.currentPlayer.laws.healthSystem
        educationSlider.value = Game.currentPlayer.laws.educationSystem
    }

    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }

    fun incomeTaxChanged() {
        Game.currentPlayer.laws.incomeTax = incomeTaxSlider.value
        updateMood()
    }

    fun lawEnforcementChanged(mouseEvent: MouseEvent) {
        Game.currentPlayer.laws.lawEnforcement = taxLawEnforcementSlider.value
        updateMood()
    }

    fun immigrationChanged(mouseEvent: MouseEvent) {
        Game.currentPlayer.laws.immigrationStrictness = immigrationSlider.value
        updateMood()
    }

    fun healthChanged(mouseEvent: MouseEvent) {
        Game.currentPlayer.laws.healthSystem = healthSlider.value
        updateMood()
    }

    fun educationChanged(mouseEvent: MouseEvent) {
        Game.currentPlayer.laws.educationSystem = educationSlider.value
        updateMood()
    }

    private fun updateMood(){
        // Calculate the mood
        Game.currentPlayer.calculateMood()
        estimatedIncomeLabel.text = Game.currentPlayer.calculateTaxBalance().toString()
        updateCallback.invoke()
    }


}