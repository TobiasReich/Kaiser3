package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.WarManager
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.MilitaryUnitType
import de.tobiasreich.kaiser.game.data.military.WarGoal
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import java.net.URL
import java.util.*

/** Controller, specific for the Land actions */
class UIControllerActionWar : Initializable {

    @FXML
    lateinit var warGoalSelectionCB: ComboBox<String>

    @FXML
    lateinit var toWarButton: Button

    @FXML
    lateinit var playerSelectionCB: ComboBox<String>

    @FXML
    lateinit var unitsToWarVisualization: HBox

    @FXML
    lateinit var unitsAtHomeVisualization: HBox


    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var updateCallback : () -> Unit

    private var miliartyAtHome = mutableMapOf<MilitaryUnitType, MutableList<MilitaryUnit>>()
    private var miliartyAtWar = mutableMapOf<MilitaryUnitType, MutableList<MilitaryUnit>>()

    private val players = Game.getAllButCurrentPlayer()
    private var targetPlayer : Player? = null
    private var warGoal : WarGoal = WarGoal.KILL_UNITS

    private lateinit var bundle: ResourceBundle

    override fun initialize(p0: URL?, bundle: ResourceBundle) {
        this.bundle = bundle

        // Target Player ComboBox
        val playerNames = FXCollections.observableArrayList<String>()
        Game.getAllButCurrentPlayer().forEach {
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

        // War Goal ComboBox
        val warGoals = FXCollections.observableArrayList<String>()

        WarGoal.values().forEach {
            warGoals.add(bundle.getString(it.stringResource))
        }

        warGoalSelectionCB.items = warGoals
        warGoalSelectionCB.selectionModel.select(0)

        // Listener to changes of the selected player
        warGoalSelectionCB.valueProperty().addListener { _, _, _ ->
            val selectedIndex = warGoalSelectionCB.selectionModel.selectedIndex
            warGoal = WarGoal.values()[selectedIndex]
            println("War Goal Index: $selectedIndex / $targetPlayer")
        }


        //TODO Add "War goal" option for different types of wars
        // E.g. steal land, rob money, burn buildings, kill people.
        // That might give more strategic options for the attacker!

        //Copy all military units to the "at home" category
        Game.currentPlayer.military.keys.forEach {
            // For every key create a new ArrayList with the same elements.
            // This is needed since we don't want to move on the original map.
            // In case the user changes something we don't want the original
            // list messed up! (e.g. units moved to "to sent")
            miliartyAtHome[it] = Game.currentPlayer.military[it]!!.toMutableList()
        }

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

            for (unit in 0 until miliartyAtHome[unitType]!!.size){
                println("Unit: $unitType")
                val imageView = ImageView()
                imageView.fitWidth = 40.0
                imageView.fitHeight = 40.0
                imageView.image = when(unitType) {
                    MilitaryUnitType.WARRIOR -> { GameImageCache.warrior }
                    MilitaryUnitType.ARCHER -> { GameImageCache.archer }
                    MilitaryUnitType.SPEARMAN -> { GameImageCache.spearman}
                    MilitaryUnitType.CAVALRY -> { GameImageCache.cavalry}
                    MilitaryUnitType.CROSSBOW -> { GameImageCache.warrior}
                    MilitaryUnitType.PIKE -> {GameImageCache.warrior}
                    MilitaryUnitType.LANCER -> {GameImageCache.lancer}
                    MilitaryUnitType.LONGSWORD -> {GameImageCache.warrior}
                    MilitaryUnitType.CANNON -> {GameImageCache.cannon}
                    MilitaryUnitType.KNIGHT -> {GameImageCache.warrior}
                    MilitaryUnitType.CRUSADER -> {GameImageCache.warrior}
                    MilitaryUnitType.MUSKETEER -> {GameImageCache.warrior}
                    MilitaryUnitType.ARTILLERY -> {GameImageCache.warrior}
                }
                imageView.setOnMouseClicked {
                    moveToBattlefield(miliartyAtHome[unitType]!!.first())
                }
                unitsAtHomeVisualization.children.add(imageView)
            }
        }
    }


    private fun drawUnitsToWar(){
        unitsToWarVisualization.children.clear()

        miliartyAtWar.keys.sorted().forEach { unitType ->

            for (unit in 0 until miliartyAtWar[unitType]!!.size){
                val imageView = ImageView()
                imageView.fitWidth = 40.0
                imageView.fitHeight = 40.0
                imageView.image = when(unitType) {
                    MilitaryUnitType.WARRIOR -> { GameImageCache.warrior }
                    MilitaryUnitType.ARCHER -> { GameImageCache.archer }
                    MilitaryUnitType.SPEARMAN -> { GameImageCache.spearman}
                    MilitaryUnitType.CAVALRY -> { GameImageCache.cavalry}
                    MilitaryUnitType.CROSSBOW -> { GameImageCache.warrior}
                    MilitaryUnitType.PIKE -> {GameImageCache.warrior}
                    MilitaryUnitType.LANCER -> {GameImageCache.lancer}
                    MilitaryUnitType.LONGSWORD -> {GameImageCache.warrior}
                    MilitaryUnitType.CANNON -> {GameImageCache.cannon}
                    MilitaryUnitType.KNIGHT -> {GameImageCache.warrior}
                    MilitaryUnitType.CRUSADER -> {GameImageCache.warrior}
                    MilitaryUnitType.MUSKETEER -> {GameImageCache.warrior}
                    MilitaryUnitType.ARTILLERY -> {GameImageCache.warrior}
                }
                imageView.setOnMouseClicked {
                    moveToHome(miliartyAtWar[unitType]!!.first())
                }
                unitsToWarVisualization.children.add(imageView)
            }
        }
    }

    private fun moveToBattlefield(unit: MilitaryUnit) {
        miliartyAtHome[unit.type]!!.remove(unit)
        if (miliartyAtWar[unit.type] == null){
            miliartyAtWar[unit.type] = mutableListOf()
        }
        miliartyAtWar[unit.type]!!.add(unit)
        drawUnitsAtHome()
        drawUnitsToWar()
        updateToWarButton()
    }

    private fun moveToHome(unit: MilitaryUnit) {
        miliartyAtWar[unit.type]!!.remove(unit)
        if (miliartyAtHome[unit.type] == null){
            miliartyAtHome[unit.type] = mutableListOf()
        }
        miliartyAtHome[unit.type]!!.add(unit)
        drawUnitsAtHome()
        drawUnitsToWar()
        updateToWarButton()
    }

    /** Sets the "to War" button enabled when a target player is selected
     * AND at least one unit is selected to go to war (one can't declare war
     * without sending at least one unit to the battle field) */
    private fun updateToWarButton(){
        toWarButton.isDisable = targetPlayer == null || miliartyAtWar.values.none{ it.size > 0}
    }

    /** Declares war to the target player */
    fun startWar(actionEvent: ActionEvent) {
        val dialogTitle = bundle.getString("war_view_offer_dialog_title")
        val dialogMessage = bundle.getString("war_view_offer_dialog_message")
        val dialogAccepted = ViewController.showConfirmationDialog(dialogTitle, dialogMessage)

        if (dialogAccepted){
            println("Declaring war")
            //Set the player's military to the ones remaining "at home"
            Game.currentPlayer.military = miliartyAtHome
            WarManager.declareWar(Game.currentPlayer, targetPlayer!!, miliartyAtWar, warGoal)
            updateCallback()
            // Close the view?
        } else {
            println("No war declared")
            //Nothing to do for now
        }
    }

    fun onWarHelpClicked() {
        ViewController.showInfoPopUp(warGoalSelectionCB, bundle.getString("war_view_help_war_goal_title"), bundle.getString("war_view_help_war_goal_help"))
        ViewController.showInfoPopUp(toWarButton, bundle.getString("war_view_help_war_declaration_title"), bundle.getString("war_view_help_war_declaration_help"))
    }

}