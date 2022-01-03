package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game.battleSpeed
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
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.*
import javafx.util.Duration
import java.net.URL
import java.util.*


/** A battle view showing a battle against another player */
class UIControllerMessageBattle : Initializable, IMessageController{

    @FXML
    lateinit var battleUpdateVBox: VBox

    @FXML
    lateinit var battleTitle: Label

    @FXML
    lateinit var battleEndButton: Button

    @FXML
    lateinit var attackerRectangle: Rectangle

    @FXML
    lateinit var defenderRectangle: Rectangle

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
        KeyFrame(Duration.seconds(battleSpeed), {
            executeBattlePhase()
        })
    )

    private lateinit var attackingUnits : MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>
    private lateinit var attackerColor : Color
    private lateinit var defendingUnits : MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>
    private lateinit var defenderColor : Color

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
        attackerColor = message.attackingPlayer.playerColor
        defendingUnits = message.defendingPlayer.military
        defenderColor = message.defendingPlayer.playerColor
        updateView()
    }

    private fun updateView() {
        attackerNameLabel.text = message.attackingPlayer.name
        attackerNameLabel.style = ("-fx-text-fill: ${attackerColor.toRGBCode()}; ")
        attackerRectangle.fill = attackerColor

        defenderNameLabel.text = message.defendingPlayer.name
        defenderNameLabel.style = ("-fx-text-fill: ${defenderColor.toRGBCode()}; ")
        defenderRectangle.fill = defenderColor

    }

    private fun updateBattleStatistics() {
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

    var attackPhase = 0

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
        logBattleMsg(String.format(bundle.getString("battle_view_new_phase"), attackPhase+1))

        /** Ranged and Melee are alternating with the ranged weapons shooting first */
        if (attackPhase %2 == 0){
            executeRangedAttackPhase()
        } else {
            executeMeleeAttacks()
        }


        // Check deserting units

        // +++ Write info to the log +++

        // Step 3.2: update view
        updateBattleStatistics()

        // Step 3.3: Check for victory

        if (WarManager.getTotalAttackPower(defendingUnits) <= 0){
            logBattleMsg(bundle.getString("battle_view_battle_is_over"))
            logBattleMsg(String.format(bundle.getString("battle_view_attacker_won"), attackPhase+1), attackerColor)
            battleTimeline.stop()
            // TODO: Make a battle outcome message to the defending player (this is technically still the attacker's turn)
            battleEndButton.isDisable = false
            // TODO: Send the attacker's units back home

        } else if (WarManager.getTotalAttackPower(attackingUnits) <= 0){
            logBattleMsg(bundle.getString("battle_view_battle_is_over"))
            logBattleMsg(String.format(bundle.getString("battle_view_defender_won"), attackPhase+1), defenderColor)
            battleTimeline.stop()
            // TODO: Make a battle outcome message to the defending player (this is technically still the attacker's turn)
            battleEndButton.isDisable = false
            // No units required to send home (the attacker lost!)
        }

        // in any case, increment the phase so next time the other "group" is fighting
        attackPhase++
    }


    /** Executes the RANGED attacks */
    private fun executeRangedAttackPhase(){
        // 1) First shot goes to the DEFENDER (in order to make defense a bit easier, attack is always a bit harder)
        // This helps to prevent strong players to bash weaker ones since they require more troops to win.
        // War is only one option for solving conflicts, but it shouldn't be too profitable fighting.
        val defenderPowerRanged = WarManager.getAttackPowerByType(false, defendingUnits)
        //println("Ranged Defender Power: $defenderPowerRanged")

        // >>> LOG: "Defender is shooting"
        // logBattleMsg(bundle.getString("battle_view_defender_range_attacking"))

        // Attacking units receive damage.
        val attackDamage = WarManager.tageDamage(attackingUnits, defenderPowerRanged)
        attackingUnits = attackDamage.first
        // >>> Log: "Attacker received damage. x units killed"
        if (attackDamage.second > 1) {
            logBattleMsg(String.format(bundle.getString("battle_view_units_died_many"), attackDamage.second), attackerColor )
        } else {
            logBattleMsg(String.format(bundle.getString("battle_view_units_died_one"), attackDamage.second), attackerColor )
        }

        // 2) Ranged attacking units attack now:
        val attackerPowerRanged = WarManager.getAttackPowerByType(false, attackingUnits)
        // >>> LOG: "Attacker is shooting"
        //logBattleMsg(bundle.getString("battle_view_attacker_range_attacking"))

        //println("Ranged Attacker Power: $attackerPowerRanged")
        val defenderDamage = WarManager.tageDamage(defendingUnits, attackerPowerRanged)
        defendingUnits = defenderDamage.first

        // >>> Log: "Defender got damage. x units killed"
        if (defenderDamage.second > 1) {
            logBattleMsg(String.format(bundle.getString("battle_view_units_died_many"), defenderDamage.second), defenderColor )
        } else {
            logBattleMsg(String.format(bundle.getString("battle_view_units_died_one"), defenderDamage.second), defenderColor )
        }
    }


    /** Executes the MELEE attacks */
    private fun executeMeleeAttacks(){
        // Melee units attack (simultaneously) - this is a pure 1 vs 1 fight so no bonus for defense

        // >>> Log: "Melee units attacking..."
        //logBattleMsg(bundle.getString("battle_view_melee_attacking"))

        val defenderPowerMelee = WarManager.getAttackPowerByType(true, defendingUnits)
        val attackerPowerMelee = WarManager.getAttackPowerByType(true, attackingUnits)
        //println("Melee DefenderPower: $defenderPowerMelee")
        //println("Melee AttackerPower: $attackerPowerMelee")
        val meleeDamageAttacker = WarManager.tageDamage(attackingUnits, defenderPowerMelee)
        attackingUnits = meleeDamageAttacker.first

        val meleeDamageDefender = WarManager.tageDamage(defendingUnits, attackerPowerMelee)
        defendingUnits = meleeDamageDefender.first

        // >>> Log: "Attacker got damage. x units killed"
        if (meleeDamageAttacker.second > 1) {
            logBattleMsg(String.format(bundle.getString("battle_view_units_died_many"), meleeDamageAttacker.second), attackerColor )
        } else {
            logBattleMsg(String.format(bundle.getString("battle_view_units_died_one"), meleeDamageAttacker.second), attackerColor )
        }

        // >>> Log: "Defender got damage. x units killed"
        if (meleeDamageDefender.second > 1) {
            logBattleMsg( String.format(bundle.getString("battle_view_units_died_many"), meleeDamageDefender.second), defenderColor )
        } else {
            logBattleMsg( String.format(bundle.getString("battle_view_units_died_one"), meleeDamageDefender.second), defenderColor )
        }
    }


    /** Adds a message to the "battle log" scroll view */
    private fun logBattleMsg(message : String, color : Color? = null){
        val messageText = Text(message)
        color?.let {
            messageText.fill = it
        }
        println("Battle log: $message")
        battleUpdateVBox.children.add(0, messageText)
    }


}