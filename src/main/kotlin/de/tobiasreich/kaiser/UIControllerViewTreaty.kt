package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.Treaty
import de.tobiasreich.kaiser.game.TreatyType
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import java.io.IOException


class UIControllerViewTreaty(private val treaty: Treaty, private val currentPlayer: Player) : HBox() {

    @FXML
    lateinit var treatyTypeImageView: ImageView

    @FXML
    lateinit var treatyTypeLabel: Label

    @FXML
    lateinit var treatyInitiatorLabel: Label

    @FXML
    lateinit var treatyReceiverLabel: Label



    init {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("view-treaty.fxml"), Game.resourcesBundle)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception : IOException) {
            throw RuntimeException(exception)
        }

        // Initiator of the treaty (if its the current player, show only "you" instead of full name)
        treatyInitiatorLabel.text = if (treaty.initiator == currentPlayer) {
            Game.resourcesBundle.getString("diplomacy_treaty_player_yourself")
        } else {
            "${treaty.initiator.name} (${Game.resourcesBundle.getString(treaty.initiator.country.nameResource)})"
        }
        treatyInitiatorLabel.style = ("-fx-text-fill: ${treaty.initiator.playerColor.toRGBCode()}; ")

        // Receiver of the treaty (if its the current player, show only "you" instead of full name)
        treatyReceiverLabel.text = if (treaty.receiver == currentPlayer) {
            Game.resourcesBundle.getString("diplomacy_treaty_player_yourself")
        } else {
            "${treaty.receiver.name} (${Game.resourcesBundle.getString(treaty.receiver.country.nameResource)})"
        }
        treatyReceiverLabel.style = ("-fx-text-fill: ${treaty.receiver.playerColor.toRGBCode()}; ")

        treatyTypeLabel.text = Game.resourcesBundle.getString(treaty.type.stringResource)
        treatyTypeImageView.image = when(treaty.type){
            TreatyType.PEACE -> GameImageCache.treatyPeace
            TreatyType.TRADE -> GameImageCache.treatyTrade
            TreatyType.ALLIANCE -> GameImageCache.treatyAlliance
        }
    }

}