package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game.battleSpeed
import de.tobiasreich.kaiser.game.TroopMovement
import de.tobiasreich.kaiser.game.WarManager
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.MilitaryUnitType
import de.tobiasreich.kaiser.game.data.military.WarGoal
import de.tobiasreich.kaiser.game.data.player.BattleMessage
import de.tobiasreich.kaiser.game.data.player.BattleOutcomeMessage
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
import kotlin.math.min


/** A battle view showing a battle against another player */
class UIControllerMessageBattle : Initializable, IMessageController{

    @FXML
    lateinit var startBattleButton: Button

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
    private var attackPowerAtStart = 0.0
    private lateinit var attackerColor : Color
    private lateinit var defendingUnits : MutableMap<MilitaryUnitType, MutableList<MilitaryUnit>>
    private var defendingPowerAtStart = 0.0
    private lateinit var defenderColor : Color

    var attackPhase = 0
    var battleEnded = false

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
        attackPowerAtStart = WarManager.getTotalAttackPower(attackingUnits)
        attackerColor = message.attackingPlayer.playerColor

        defendingUnits = message.defendingPlayer.military
        defendingPowerAtStart = WarManager.getTotalAttackPower(defendingUnits)
        defenderColor = message.defendingPlayer.playerColor
        updateView()
        updateBattleStatistics(attackPowerAtStart, defendingPowerAtStart)
    }

    private fun updateView() {
        attackerNameLabel.text = message.attackingPlayer.name
        attackerNameLabel.style = ("-fx-text-fill: ${attackerColor.toRGBCode()}; ")
        attackerRectangle.fill = attackerColor

        defenderNameLabel.text = message.defendingPlayer.name
        defenderNameLabel.style = ("-fx-text-fill: ${defenderColor.toRGBCode()}; ")
        defenderRectangle.fill = defenderColor

    }


    /** Shows the Attack Power of both sides and updates the attack power graph */
    private fun updateBattleStatistics(attackPower: Double, defensePower: Double) {
        attackerAmountUnitsLabel.text = String.format(bundle.getString("battle_view_attack_power"), attackPower)
        defenderAmountUnitsLabel.text = String.format(bundle.getString("battle_view_attack_power"), defensePower)

        val totalPower = attackPower + defensePower
        val fraction = 500 / totalPower // 1 unit of Power is represented by that amount pixels
        attackerRectangle.width = attackPower * fraction // Defender is always "the rest" so no need for calculation
    }


    fun onBeginBattleButtonClick(actionEvent: ActionEvent) {
        startBattleButton.isDisable = true
        battleEndButton.isDisable = false
        battleTimeline.play() // Start the timeline which executes a "frame" every second
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
     *  TODO: Units fleeing might come back after the battle so the looser is not punished that hard as if all units were killed for nothing.
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
        solveDeserting()


        val attackPower = WarManager.getTotalAttackPower(attackingUnits)
        val defensePower = WarManager.getTotalAttackPower(defendingUnits)

        // Step 3.2: update view
        updateBattleStatistics(attackPower, defensePower)

        // Step 3.3: Check victory conditions

        // War goal was not killing all units -> Battles go until half the units died
        if (message.warGoal != WarGoal.KILL_UNITS){

            if (attackPower <= attackPowerAtStart / 2.0  || attackPower <= 0){
                // Attackers lost more than half their power -> Attackers RETREAT
                endWarLogic(attackPower, defensePower,false)
                logBattleMsg(bundle.getString("battle_view_battle_is_over_attacker_retreat"), attackerColor) // Your troops retreat
                logBattleMsg(String.format(bundle.getString("battle_view_attacker_lost_other_goal"), attackPhase+1), attackerColor)
                return

            } else if (defensePower <= defendingPowerAtStart / 2.0 || defensePower <= 0){
                // Defenders have lost more than half their power -> Defenders SURRENDER
                // Attackers return with their goal achieved
                endWarLogic(attackPower, defensePower,true)
                logBattleMsg(bundle.getString("battle_view_battle_is_over_defender_retreat"), defenderColor) // Defender troops retreat
                logBattleMsg(String.format(bundle.getString("battle_view_attacker_won_other_goal"), attackPhase+1), attackerColor)
                return
            }

        } else {
            // WarGoal is "ordinary war" (WarGoal.KILL_UNITS)
            if (attackPower <= 0){
                endWarLogic(attackPower, defensePower,false)
                logBattleMsg(String.format(bundle.getString("battle_view_attacker_lost"), attackPhase+1), attackerColor)
                return

            } else if (defensePower <= 0){
                endWarLogic(attackPower, defensePower,true)
                logBattleMsg(String.format(bundle.getString("battle_view_attacker_won"), attackPhase+1), defenderColor)
                return
            }
        }

        attackPhase++ // The battle continues: increment battle phase
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


    /** Calculates how many units of the military were deserting, updating the view and the military
     *  NOTE: Units only desert if the other troops are more than twice as powerful! */
    private fun solveDeserting() {
        val attackerPower = WarManager.getTotalAttackPower(attackingUnits)
        val defenderPower = WarManager.getTotalAttackPower(defendingUnits)

        // Units only desert if the other troops are more than twice as powerful
        if (attackerPower > defenderPower * 2.0){
            // defender units deserting
            val defenderDeserting = WarManager.calculateDesertingTroops(defendingUnits)
            defendingUnits = defenderDeserting.first
            if (defenderDeserting.second > 0) {
                logBattleMsg(String.format(bundle.getString("battle_view_units_deserted"), defenderDeserting.second), defenderColor)
            }

        } else if (defenderPower > attackerPower * 2.0){
            // attacking units deserting
            val attackerDeserting = WarManager.calculateDesertingTroops(attackingUnits)
            attackingUnits = attackerDeserting.first
            if (attackerDeserting.second > 0) {
                logBattleMsg(String.format(bundle.getString("battle_view_units_deserted"), attackerDeserting.second), attackerColor)
            }
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


    /** Write the log message
     *  Stops the battle timeLine.
     *  If the attack units are not completely eradicated -> send the troops back home.
     *  Enables the button to end the view */
    private fun endWarLogic(attackPower : Double, defenderPower : Double, victory: Boolean){
        battleTimeline.stop()

        // Calculate the lost military power of the defender for the BOM
        val remainingDefenderMilitaryPowerFraction = defenderPower / defendingPowerAtStart

        logBattleMsg(bundle.getString("battle_view_battle_is_over"))

        // Get the BattleOutcomeMessage that can be sent to the defender
        val bom = if (victory) {

            // Depends on how many troops the attacker has lost.
            // E.g. At start: 10, now: 4 -> 4 / 10 = 0.4
            // Their goal is therefore reached by 40% only
            val warSuccessFactor = attackPower / attackPowerAtStart
            println("War Success Factor: $warSuccessFactor")

            // The numeric value the attacker has won (e.g. stolen money, conquered land...)
            var victoryValue = 0

            when (message.warGoal) {
                WarGoal.KILL_UNITS -> {
                    /* Nothing to do */
                }
                WarGoal.STEAL_MONEY -> {
                    val stolenMoney = if (message.defendingPlayer.money > 0){
                        (message.defendingPlayer.money.toDouble() * warSuccessFactor).toInt()
                    } else { 0 }
                    message.defendingPlayer.money -= stolenMoney // Directly remove the money in case others attack, too!
                    victoryValue = stolenMoney
                    logBattleMsg(String.format(bundle.getString("battle_view_outcome_steal_money"), stolenMoney), attackerColor)
                }
                WarGoal.GET_SLAVES -> {
                    val kidnappedPopulation = (message.defendingPlayer.population.adults.size.toDouble() * warSuccessFactor).toInt()
                    message.defendingPlayer.population.removeAdults(kidnappedPopulation) // Directly remove the money in case others attack, too!
                    message.defendingPlayer.land.buildings.updateUsedBuildings(message.defendingPlayer.population) // Update buildings in use
                    victoryValue = kidnappedPopulation
                    logBattleMsg(String.format(bundle.getString("battle_view_outcome_slaves"), kidnappedPopulation), attackerColor)
                }
                WarGoal.CONQUER -> {
                    // For better balancing: max 10 % of the owned land can be stolen
                    val conqueredLand = (message.defendingPlayer.land.landSize.toDouble() * warSuccessFactor * 0.1).toInt()
                    message.defendingPlayer.land.removeLand(conqueredLand, message.defendingPlayer.population) // Update land and building usage directly
                    victoryValue = conqueredLand
                    logBattleMsg(String.format(bundle.getString("battle_view_outcome_conquer"), conqueredLand), attackerColor)
                }
                WarGoal.BURN_BUILDINGS -> {
                    val buildings = message.defendingPlayer.land.buildings

                    // For better balancing: max 10 % of all the buildings could be burned
                    val destroyedMills = (buildings.mills * warSuccessFactor * 0.1).toInt()
                    val destroyedGranaries = (buildings.granaries * warSuccessFactor * 0.1).toInt()
                    val destroyedMarkets = (buildings.markets * warSuccessFactor * 0.1).toInt()

                    // Palace and Cathedral can destroy a maximum of 1 piece per battle, on chances depending on their success factor
                    val destroyedPalace = if (warSuccessFactor > Math.random()) { 1 } else { 0 }
                    val destroyedCathedral = if (warSuccessFactor > Math.random()) { 1 } else { 0 }

                    buildings.destroyMill(destroyedMills)
                    buildings.destroyGranaries(destroyedGranaries)
                    buildings.destroyMarkets(destroyedMarkets)
                    buildings.destroyPalace(destroyedPalace)
                    buildings.destroyCathedral(destroyedCathedral)

                    val totalBuildingsBurned = destroyedGranaries + destroyedMills + destroyedMarkets + destroyedPalace + destroyedCathedral
                    victoryValue = totalBuildingsBurned

                    logBattleMsg(String.format(bundle.getString("battle_view_outcome_burn"), totalBuildingsBurned), attackerColor)
                }
            }

            BattleOutcomeMessage(message.attackingPlayer, remainingDefenderMilitaryPowerFraction, message.warGoal, victory, victoryValue)

        } else {
            // Defending player had won this battle (victory value is 0 since nothing is stolen etc.). However killed units are killed.
            BattleOutcomeMessage(message.attackingPlayer, remainingDefenderMilitaryPowerFraction, message.warGoal, victory, 0)
        }

        // Message the defending player about the battle outcome
        message.defendingPlayer.addMessageToFrontOfList(bom)


        // If they are not completely eradicated, the leftover units return
        // No need for troop movement of the defenders, they are home already anyway
        if (attackPower > 0) {
            WarManager.addTroopMovement(TroopMovement(message.defendingPlayer, message.attackingPlayer, attackingUnits))
        }

        battleEndButton.isDisable = false
    }


    /** Click on "End Battle" Button.
     *  This is only available after the battle has finished */
    fun onBattleOutcomeButtonClick(actionEvent: ActionEvent) {
        proceedToNextNews()
    }

}