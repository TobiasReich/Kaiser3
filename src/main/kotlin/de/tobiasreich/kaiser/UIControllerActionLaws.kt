package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.input.MouseEvent
import java.net.URL
import java.util.*


/** Controller, specific for the laws actions (taxes,immigration...) */
class UIControllerActionLaws : Initializable {

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

    private lateinit var bundle :  ResourceBundle

    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!

        incomeTaxSlider.value = Game.currentPlayer.laws.incomeTax
        taxLawEnforcementSlider.value = Game.currentPlayer.laws.lawEnforcement
        immigrationSlider.value = Game.currentPlayer.laws.immigrationStrictness
        healthSlider.value = Game.currentPlayer.laws.healthSystem
        educationSlider.value = Game.currentPlayer.laws.educationSystem

        updateMood()
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
        updateCallback.invoke()
    }

    fun lawEnforcementChanged(mouseEvent: MouseEvent) {
        Game.currentPlayer.laws.lawEnforcement = taxLawEnforcementSlider.value
        updateMood()
        updateCallback.invoke()
    }

    fun immigrationChanged(mouseEvent: MouseEvent) {
        Game.currentPlayer.laws.immigrationStrictness = immigrationSlider.value
        updateMood()
        updateCallback.invoke()
    }

    fun healthChanged(mouseEvent: MouseEvent) {
        Game.currentPlayer.laws.healthSystem = healthSlider.value
        updateMood()
        updateCallback.invoke()
    }

    fun educationChanged(mouseEvent: MouseEvent) {
        Game.currentPlayer.laws.educationSystem = educationSlider.value
        updateMood()
        updateCallback.invoke()
    }

    private fun updateMood(){
        // Calculate the mood
        Game.currentPlayer.calculateMood()
        estimatedIncomeLabel.text = Game.currentPlayer.calculateTaxBalance().toString()
    }

    // -------- Info buttons --------
    
    fun onIncomeTaxInfoClicked(mouseEvent: MouseEvent) {
        ViewController.showInfoPopUp(incomeTaxSlider, bundle.getString("laws_income_tax_title"),bundle.getString("laws_income_tax_info"))
    }

    fun onLawEnforcementInfoClicked(mouseEvent: MouseEvent) {
        ViewController.showInfoPopUp(taxLawEnforcementSlider, bundle.getString("laws_law_enforcement"),bundle.getString("laws_law_enforcement_info"))
    }

    fun onImmigrationInfoClicked(mouseEvent: MouseEvent) {
        ViewController.showInfoPopUp(immigrationSlider, bundle.getString("laws_immigration"),bundle.getString("laws_immigration_info"))
    }

    fun onHealthInfoClicked(mouseEvent: MouseEvent) {
        ViewController.showInfoPopUp(healthSlider, bundle.getString("laws_health"),bundle.getString("laws_health_info"))
    }

    fun onEducationInfoClicked(mouseEvent: MouseEvent) {
        ViewController.showInfoPopUp(educationSlider, bundle.getString("laws_education"),bundle.getString("laws_education_info"))
    }

}