package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.WarManager
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import de.tobiasreich.kaiser.game.utils.FxDialogs
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionWar : Initializable {

    @FXML
    lateinit var toWarButton: Button

    @FXML
    lateinit var playerSelectionCB: ComboBox<String>

    @FXML
    lateinit var unitsToWarVisualization: HBox

    @FXML
    lateinit var unitsAtHomeVisualization: HBox

    @FXML
    fun onButtonSomethingClick(actionEvent: ActionEvent) {
        //Some button clicked
    }

    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    private var miliartyAtHome = mutableMapOf<MilitaryUnit, Int>()
    private var miliartyAtWar = mutableMapOf<MilitaryUnit, Int>()

    private val players = Game.getAllOtherPlayers()
    private var targetPlayer : Player? = null


    override fun initialize(p0: URL?, bundle: ResourceBundle) {
        val playerNames = FXCollections.observableArrayList<String>()
        Game.getAllOtherPlayers().forEach {
            playerNames.add("${it.name} (${bundle.getString(it.country.nameResource)})")
        }

        playerSelectionCB.items = playerNames

        // Listener to changes of the selected player
        playerSelectionCB.valueProperty().addListener { _, _, _ ->
            val selectedIndex = playerSelectionCB.selectionModel.selectedIndex
            targetPlayer = players[selectedIndex]
            println("Index: $selectedIndex / $targetPlayer")
            updateToWarButton()
        }

        //Copy all military units to the "at home" category
        miliartyAtHome = HashMap(Game.currentPlayer.miliarty)

        drawUnitsAtHome()
        drawUnitsToWar()
    }

    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.updateCallback = callback
    }

    private fun drawUnitsAtHome(){
        unitsAtHomeVisualization.children.clear()

        miliartyAtHome.keys.sorted().forEach { unitType ->

            for (unit in 0 until (miliartyAtHome[unitType] ?: 0)){
                println("Unit: $unitType")
                val imageView = ImageView()
                imageView.fitWidth = 40.0
                imageView.fitHeight = 40.0
                imageView.image = when(unitType) {
                    MilitaryUnit.WARRIOR -> { GameImageCache.warrior }
                    MilitaryUnit.ARCHER -> { GameImageCache.archer }
                    MilitaryUnit.SPEARMAN -> { GameImageCache.spearman}
                    MilitaryUnit.CAVALRY -> { GameImageCache.cavalry}
                    MilitaryUnit.CROSSBOW -> { GameImageCache.warrior}
                    MilitaryUnit.PIKE -> {GameImageCache.warrior}
                    MilitaryUnit.LANCER -> {GameImageCache.lancer}
                    MilitaryUnit.LONGSWORD -> {GameImageCache.warrior}
                    MilitaryUnit.CANNON -> {GameImageCache.cannon}
                    MilitaryUnit.KNIGHT -> {GameImageCache.warrior}
                    MilitaryUnit.CRUSADER -> {GameImageCache.warrior}
                    MilitaryUnit.MUSKETEER -> {GameImageCache.warrior}
                    MilitaryUnit.ARTILLERY -> {GameImageCache.warrior}
                }
                imageView.setOnMouseClicked { event ->
                    moveToBattlefield(unitType)
                }
                unitsAtHomeVisualization.children.add(imageView)
            }
        }
    }


    private fun drawUnitsToWar(){
        unitsToWarVisualization.children.clear()

        miliartyAtWar.keys.sorted().forEach { unitType ->

            for (unit in 0 until (miliartyAtWar[unitType] ?: 0)){
                println("Unit: $unitType")
                val imageView = ImageView()
                imageView.fitWidth = 40.0
                imageView.fitHeight = 40.0
                imageView.image = when(unitType) {
                    MilitaryUnit.WARRIOR -> { GameImageCache.warrior }
                    MilitaryUnit.ARCHER -> { GameImageCache.archer }
                    MilitaryUnit.SPEARMAN -> { GameImageCache.spearman}
                    MilitaryUnit.CAVALRY -> { GameImageCache.cavalry}
                    MilitaryUnit.CROSSBOW -> { GameImageCache.warrior}
                    MilitaryUnit.PIKE -> {GameImageCache.warrior}
                    MilitaryUnit.LANCER -> {GameImageCache.lancer}
                    MilitaryUnit.LONGSWORD -> {GameImageCache.warrior}
                    MilitaryUnit.CANNON -> {GameImageCache.cannon}
                    MilitaryUnit.KNIGHT -> {GameImageCache.warrior}
                    MilitaryUnit.CRUSADER -> {GameImageCache.warrior}
                    MilitaryUnit.MUSKETEER -> {GameImageCache.warrior}
                    MilitaryUnit.ARTILLERY -> {GameImageCache.warrior}
                }
                imageView.setOnMouseClicked { event ->
                    moveToHome(unitType)
                }
                unitsToWarVisualization.children.add(imageView)
            }
        }
    }

    private fun moveToBattlefield(unitType: MilitaryUnit) {
        miliartyAtHome[unitType] = miliartyAtHome[unitType]!! - 1
        miliartyAtWar[unitType] = (miliartyAtWar[unitType]?: 0) + 1
        drawUnitsAtHome()
        drawUnitsToWar()
        updateToWarButton()
    }

    private fun moveToHome(unitType: MilitaryUnit) {
        miliartyAtWar[unitType] = miliartyAtWar[unitType]!! - 1
        miliartyAtHome[unitType] = (miliartyAtHome[unitType]?: 0) + 1
        drawUnitsAtHome()
        drawUnitsToWar()
        updateToWarButton()
    }

    /** Sets the "to War" button enabled when a target player is selected
     * AND at least one unit is selected to go to war (one can't declare war
     * without sending at least one unit to the battle field) */
    private fun updateToWarButton(){
        toWarButton.isDisable = targetPlayer == null && miliartyAtWar.values.sum() > 0
    }

    /** Declares war to the target player */
    fun startWar(actionEvent: ActionEvent) {
        val result = ViewController.showModalDialog()

        if (result == FxDialogs.DialogResult.OK){
            println("Declaring war")
            //Set the player's military to the ones remaining "at home"
            Game.currentPlayer.miliarty = miliartyAtHome
            WarManager.declareWar(Game.currentPlayer, targetPlayer!!, miliartyAtWar)
            updateCallback()
            // Close the view (show the
        } else {
            println("No war declared")
            //Nothing to do for now
        }
    }

}