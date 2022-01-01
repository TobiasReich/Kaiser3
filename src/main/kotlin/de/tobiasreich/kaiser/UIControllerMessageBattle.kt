package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.WarManager
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.MilitaryUnitType
import de.tobiasreich.kaiser.game.data.player.BattleMessage
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextFlow
import javafx.util.Duration
import java.net.URL
import java.util.*


/** A battle view showing a battle against another player */
class UIControllerMessageBattle : Initializable, IMessageController{

    @FXML
    lateinit var battleTitle: Label

    @FXML
    lateinit var battleEndButton: Button

    @FXML
    lateinit var attackerRectangle: Rectangle

    @FXML
    lateinit var defenderRectangle: Rectangle


    @FXML
    lateinit var battleUpdateTextFlow: TextFlow

    @FXML
    lateinit var defenderAmountUnitsLabel: Label

    @FXML
    lateinit var defenderNameLabel: Label

    @FXML
    lateinit var attackerAmountUnitsLabel: Label

    @FXML
    lateinit var attackerNameLabel: Label

    private lateinit var bundle: ResourceBundle

    /** The Message to be shown to the user */
    private lateinit var message: BattleMessage

    /** Basically like a handler that gets "executed" on fixed intervalls
     *  This will simulate the battle where the users see an update very second
     *  until the battle is over. */
    private val battleTimeline = Timeline(
        KeyFrame(Duration.seconds(1.0), {
            executeBattlePhase()
        })
    )

    private lateinit var attackingUnits : MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>
    private lateinit var defendingUnits : MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/

    @FXML
    fun onNewsButtonClick(actionEvent: ActionEvent) {
        proceedToNextNews()
    }

    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle) {
        this.bundle = bundle
        battleTimeline.cycleCount = Timeline.INDEFINITE
    }

    override fun setMessage(message: ReportMessage) {
        this.message = message as BattleMessage
        attackingUnits = message.attackingUnits.toMutableMap()
        defendingUnits = message.defendingPlayer.military
        updateView()
    }

    private fun updateView() {
        attackerNameLabel.text = message.attackingPlayer.name
        attackerNameLabel.style = ("-fx-text-fill: ${message.attackingPlayer.playerColor.toRGBCode()}; ")
        attackerRectangle.fill = message.attackingPlayer.playerColor

        defenderNameLabel.text = message.defendingPlayer.name
        defenderNameLabel.style = ("-fx-text-fill: ${message.defendingPlayer.playerColor.toRGBCode()}; ")
        defenderRectangle.fill =  message.defendingPlayer.playerColor

    }

    private fun updateBattleStatistics() {
        println("updateBattle stats")

        val attackPower = WarManager.getBattlePower(attackingUnits)
        attackerAmountUnitsLabel.text = String.format(bundle.getString("battle_view_attack_power"), attackPower)

        val defensePower = WarManager.getBattlePower(defendingUnits)
        defenderAmountUnitsLabel.text = String.format(bundle.getString("battle_view_attack_power"), defensePower)

        val totalPower = attackPower + defensePower
        val fraction = 500 / totalPower // 1 unit of Power is represented by that amount pixels
        attackerRectangle.width = attackPower * fraction // Defender is always the rest so no need for calculation
    }


    fun onBeginBattleButtonClick(actionEvent: ActionEvent) {
        battleEndButton.isDisable = false
        battleTimeline.play() // Start the timeline which executes a "frame" every second
    }

    fun onBattleOutcomeButtonClick(actionEvent: ActionEvent) {
        // TODO if battle is over, end the view
        // -> Make a BattleOutcomeMessage message to the defender (attacker not needed)
        // -> send remaining attacker Troops home -> WarManager.addTroopMovement()

        battleTimeline.stop() //TODO for now we stop when pressing the button. This will happen at the battle phases in the future

        proceedToNextNews()
    }

    /*************************************************************************************
     *
     *                                      BATTLE LOGIC
     *
     *************************************************************************************/

    /** This will be one "phase" of the battle.
     *  A battle can have multiple battles in case the goal is not reached
     *  (e.g. if units are still alive)
     *
     *  TODO: Add different goals and their termination condition
     *  (E.g. if a player only wants to burn some buildings instead of killing all enemies,
     *  it could end when half it's own troops are slaughtered already. No clear victory would
     *  be needed)
     *
     *  TODO: Consider units fleeing when the battle looks bad!
     *   They might come back after the battle so the looser is not punished that hard as if all units were killed for nothing.
     */
    private fun executeBattlePhase(){
        // STEP 1: Ranged units attack first

        // 1.1: First shot goes to the DEFENDER (in order to make defense a bit easier, attack is always a bit harder)
        // This helps preventing strong players to bash weaker ones since they require more troops to win.
        // War is only one option for solving conflicts, but it shouldn't be too profitable fighting.
        val defenderPowerRanged = WarManager.getAttackPowerByType(false, defendingUnits)
        println("Ranged Defender Power: $defenderPowerRanged")

        // Attacking units receive damage.
        attackingUnits = WarManager.tageDamage(attackingUnits, defenderPowerRanged)

        // 1.2 Ranged attacking units attack now:
        val attackerPowerRanged = WarManager.getAttackPowerByType(false, attackingUnits)
        println("Ranged Attacker Power: $attackerPowerRanged")
        defendingUnits = WarManager.tageDamage(defendingUnits, attackerPowerRanged)

        // +++ Write info to the log +++


        // STEP 2: Melee units attack (simultaneously) - this is a pure 1 vs 1 fight so no bonus for defense
        val defenderPowerMelee = WarManager.getAttackPowerByType(true, defendingUnits)
        val attackerPowerMelee = WarManager.getAttackPowerByType(true, attackingUnits)
        println("Melee DefenderPower: $defenderPowerMelee")
        println("Melee AttackerPower: $attackerPowerMelee")
        attackingUnits = WarManager.tageDamage(attackingUnits, defenderPowerMelee)
        defendingUnits = WarManager.tageDamage(defendingUnits, attackerPowerMelee)


        // +++ Write info to the log +++

        // Step 3: Battle outcome

        // Step 3.1: Check deserting units

        // +++ Write info to the log +++

        // Step 3.2: update view
        updateBattleStatistics()

        // Step 3.3: Check for victory

        // +++ Write info to the log +++ (e.g. "next phase" or "victory" etc.)

        // If battle is over?
        // -> battleTimeline.stop()
        // solve consequences (e.g. units return to player etc.)
        // -> set the "End Battle" button enabled

        if (WarManager.getAttackPowerByType(false, defendingUnits) <= 0){
            println("Defender got eliminated")
            battleTimeline.stop()
        } else if (WarManager.getAttackPowerByType(false, attackingUnits) <= 0){
            println("Attacker got eliminated")
            battleTimeline.stop()

        }

        println(" ----- END OF BATTLE PHASE ----- ")

    }

    private fun executeRangedAttackPhase(){

    }

}