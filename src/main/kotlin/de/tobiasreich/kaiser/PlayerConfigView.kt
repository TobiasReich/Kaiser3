package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.config.IPlayerConfigChange
import de.tobiasreich.kaiser.config.PlayerConfig
import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import java.io.IOException

class PlayerConfigView(val playerConfig : PlayerConfig, val callback : IPlayerConfigChange) : VBox() {

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

    @FXML
    fun changeActive(e: ActionEvent){
        playerConfig.active = playerConfigActiveCB.isSelected
        updateView()
        callback.onUpdateActiveState()
    }

    init {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("scene-start-screen-player-config.fxml"), Game.stringsBundle)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception : IOException) {
            throw RuntimeException(exception)
        }
        updateView()
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