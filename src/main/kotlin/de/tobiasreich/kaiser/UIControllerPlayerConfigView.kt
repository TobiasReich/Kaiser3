package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.config.IPlayerConfigChange
import de.tobiasreich.kaiser.config.PlayerConfig
import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import java.io.IOException


class UIControllerPlayerConfigView(private val playerConfig : PlayerConfig, private val callback : IPlayerConfigChange) : VBox() {

    @FXML
    lateinit var playerConfigCountryLabel: Label

    @FXML
    lateinit var playerConfigNameTF: TextField

    @FXML
    lateinit var playerConfigColorPicker: ColorPicker

    @FXML
    lateinit var playerConfigMaleToggleButton: ToggleButton

    @FXML
    lateinit var playerConfigAIPlayerCB: CheckBox

    @FXML
    lateinit var playerConfigActiveCB: CheckBox

    /** This is called by all config views but the controller is set programmatically so the usage is not shown
     *  TODO: check if this could be defined in the XMl again instead */
    @FXML
    fun onConfigurationChange(e: ActionEvent){
        updatePlayer(playerConfigActiveCB.isSelected) // Update player object
        updateView()                                  // Update the view representing the player
        callback.onUpdateActiveState(playerConfig)    // Notify the outer controller to update the whole view
    }


    init {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("view-player-config.fxml"), Game.stringsBundle)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception : IOException) {
            throw RuntimeException(exception)
        }

        val tabListener: EventHandler<KeyEvent> = EventHandler<KeyEvent> { evt ->
            evt.consume()
            updatePlayer(playerConfigActiveCB.isSelected) // Update player object
            updateView()                                  // Update the view representing the player
            callback.onUpdateActiveState(playerConfig)    // Notify the outer controller to update the whole view
            /* Since the UpdateView sets the "new" text the cursor would still be at the beginning of the view
             * Thus writing another character would lead to adding it at the start.
             * We therefore set the cursor to the end of the view */
            playerConfigNameTF.end()
        }

        playerConfigNameTF.addEventHandler(KeyEvent.ANY, tabListener)

        updateView()
    }


    /** Updates the game configuration for this player */
    private fun updatePlayer(active : Boolean){
        playerConfig.active = active
        playerConfig.name = playerConfigNameTF.text
        playerConfig.color = playerConfigColorPicker.value
        playerConfig.male = playerConfigMaleToggleButton.isSelected
        playerConfig.isAI = playerConfigAIPlayerCB.isSelected
        playerConfig.active = playerConfigActiveCB.isSelected
    }

    private fun updateView(){
        playerConfigCountryLabel.text = Game.stringsBundle.getString(playerConfig.country.nameResource)
        playerConfigNameTF.text = playerConfig.name
        playerConfigColorPicker.value = playerConfig.color
        playerConfigMaleToggleButton.isSelected = playerConfig.male
        playerConfigAIPlayerCB.isSelected = playerConfig.isAI
        playerConfigActiveCB.isSelected = playerConfig.active

        setViewsEnabled(playerConfig.active)
    }

    private fun setViewsEnabled(enabled : Boolean){
        playerConfigCountryLabel.isDisable = ! enabled
        playerConfigNameTF.isDisable = !enabled
        playerConfigColorPicker.isDisable = !enabled
        playerConfigMaleToggleButton.isDisable = !enabled
        playerConfigAIPlayerCB.isDisable = !enabled
    }

}