package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.config.PlayerConfig
import de.tobiasreich.kaiser.game.data.country.BuildingType
import de.tobiasreich.kaiser.game.data.country.Buildings.Companion.FOOD_PRODUCED_PER_MILL
import de.tobiasreich.kaiser.game.data.country.Buildings.Companion.GRAIN_STORED_PER_GRANARY
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import de.tobiasreich.kaiser.game.data.country.HarvestEvent
import de.tobiasreich.kaiser.game.data.country.Land
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
import de.tobiasreich.kaiser.game.data.military.MilitaryUnitType
import de.tobiasreich.kaiser.game.data.military.SabotageType
import de.tobiasreich.kaiser.game.data.player.*
import de.tobiasreich.kaiser.game.data.population.Laws
import de.tobiasreich.kaiser.game.data.population.Person
import de.tobiasreich.kaiser.game.data.population.Population
import de.tobiasreich.kaiser.game.data.population.Population.Companion.BASE_EXPENSE_EDUCATION_SYSTEM
import de.tobiasreich.kaiser.game.data.population.Population.Companion.BASE_EXPENSE_HEALTH_SYSTEM
import de.tobiasreich.kaiser.game.data.population.Population.Companion.FOOD_USE_PER_PERSON
import javafx.scene.paint.Color
import java.util.*

/** This is the complete configuration and setup of once specific player
 *  A player consists of
 *  - a name (without title) -> The name shown in the game
 *  - a gender represented as "isMale" -> We have only 2 genders here, this is a game in 1400
 *  - a population -> Representing all the people living in the players reign
 *  - wheat -> The amount of wheat that is stored in the players granary
 */
class Player{

    constructor(playerConfig: PlayerConfig){
        this.name = playerConfig.name
        this.isMale = playerConfig.male
        this.country = playerConfig.country
        this.playerColor = playerConfig.color
        this.isAI = playerConfig.isAI
        this.difficulty = playerConfig.difficulty
        this.firstTurn = true
    }

    val name :String            // The players name without title
    val isMale: Boolean         // Gender of the player (only used for the title, no game difference)
    val country: Country        // Country ruled by the player
    val playerColor : Color     // Player's color for better UX
    val isAI : Boolean          // Defines whether it is an AI player (no visible turn is made by that player)
    val difficulty : Int        // Unused for now. Could define harvest and events
    var firstTurn : Boolean    // Skips update flow. Used for the first time a player is playing (So there is no harvest etc. at the first turn made)


    /** The probability that this player gets a (negative) HarvestEvent
     *  TODO Players might have a treat of good / bad luck
     */
    private var harvestEventProbability = 0.5


    /** The price multiplier for buildings. This is by default 1.0 but can change due to events.
     * E.g. wood shortage etc. */
    private var playerBuildingPriceMultiplier = 1.0

    /** The actual money the player possesses. This can also be negative.
     *  According to the classical game, making debts is of no direct concern.
     *  So purchases can be made, even if it leads to negative money.
     *  This however might lead to side effects (like population unrest etc.)
     *  TODO: Think of potential consequences. Like public expenses not being paid (e.g. health, schools...)
     *  The original game and Kaiser II lead to skipping a turn as worst case, having the AI taking over. */
    var money = 10000               // The amount of money the player has

    var playerTitle = Title.MISTER  // We automatically start with the lowest title Mr/Mrs
    val population = Population()   // The standard population at start
    val land = Land()               // How many ha the user possesses
    val laws = Laws()               // Player / Land specific rules like taxes ...

    var storedFood = 1000           // The resources (how much wheat is in the granaries)
    var foodForDistribution = 1000  // Amount of food to be distributed (i.e. how much do they get for that year)
    var foodPrice = 50              // The current wheat price for this player this year


    /* The units the player owns, grouped by unit */
    var military = mutableMapOf<MilitaryUnitType, MutableList<MilitaryUnit>>()

    /* The bonus / malus for troop morale. Higher values make the units less likely to desert. */
    var militaryMoodModifier = 0.0


    init {
        calculateMood()
        land.buildings.updateUsedBuildings(population)
    }


    /** A list of messages arriving at the beginning of a year (turn) */
    private val messageList = mutableListOf<ReportMessage>()


    // ---------------------------- FOOD ----------------------------

    // ------------------------------------------------------------------------
    //<editor-fold desc="Food Management">
    // ------------------------------------------------------------------------

    /** Returns how much food is required in order to feed every person */
    fun getNeededFoodAmount() : Int {
        return population.getAmountPeople() * FOOD_USE_PER_PERSON
    }

    /** Returns the MAXIMUM amount of food that is required in order to feed every person.
     *  Excessive food beyond that won't give a bonus. */
    fun getMaxFoodAmount() : Int {
        return (population.getAmountPeople() * FOOD_USE_PER_PERSON * Population.MAX_FOOD_USE_PER_PERSON_FACTOR).toInt()
    }

    /** Returns the MINIMUM amount of food that is required in order to feed every person */
    fun getMinFoodAmount() : Int {
        return (population.getAmountPeople() * FOOD_USE_PER_PERSON * Population.MIN_FOOD_USE_PER_PERSON_FACTOR).toInt()
    }

    /** Calculates the harvest for this year. */
    private fun processHarvest(player : Player) : HarvestReport {
        // Defines the current harvest condition this year -> Defining the wheat price
        val harvestCondition = HarvestCondition.values().random()
        val harvestEvent = if (Math.random() > harvestEventProbability){
            HarvestEvent.values().random()
        } else { null }

        println("HarvestCondition: ${harvestCondition.name}, HarvestEvent? : ${harvestEvent?.name}")

        val harvestedFood = (FOOD_PRODUCED_PER_MILL * player.land.buildings.usedMills * harvestCondition.harvestRatio).toInt()

        println("Harvest this year brought: $harvestedFood")

        player.storedFood += harvestedFood

        println("Total Food (before events): ${player.storedFood}")

        // ----- Work on the HARVEST EFFECTS -----
        if (harvestEvent != null) {
            player.storedFood = (player.storedFood * harvestEvent.effect).toInt()
        }
        println("Total Food (after potential events): ${player.storedFood}")

        return HarvestReport(harvestCondition, harvestedFood, player.storedFood, harvestEvent)
    }

    /** Convenience method determining how much grain can be stored in
     *  the countries granaries (in total)
     *  This is defined as:
     *    buildings.granaries * GRAIN_PER_GRANARY
     */
    fun getMaxWheatStorage() : Int {
        return land.buildings.granaries * GRAIN_STORED_PER_GRANARY
    }

    /** This returns the title of the player depending on the set gender.
     *  E.g. "King" (male) or "Queen" (female) */
    fun getGenderTitle(bundle: ResourceBundle) : String {
        return if(isMale){
            bundle.getString(playerTitle.resourceNameMale)
        } else {
            bundle.getString(playerTitle.resourceNameMale)
        }
    }

    /** This adds / subtracts wheat
     *  It returns a boolean determining whether the purchase was valid
     *  A player can not buy more wheat than there is capacitiy in the granaries
     *  A player can not sell more wheat than all that's left in the granary
     */
    fun addFood(amount : Int) : Boolean{
        if (storedFood + amount > getMaxWheatStorage()){
            return false
        }
        if (storedFood + amount < 0){
            return false
        }

        storedFood += amount
        return true
    }


    /** Processes the food for the upcoming year
     *  (This has to be done before the turn ends since the new turn would change the harvest and granary states)  */
    fun processFood() {
        // TODO: Implement this!
        // ...
    }


    //</editor-fold>


    // ------------------------------------------------------------------------
    //<editor-fold desc="Turn logic">
    // ------------------------------------------------------------------------

    /** This starts a new turn for the given player.
     *  That includes:
     *  - Set a new harvest condition
     *  ...
     *
     *  NOTE: This is all done in order to create the "news" messages.
     *  For everything that is done after that (i.e. when the player finally starts making the turn)
     *  the onNewTurnStarted() call is executed. */
    fun beforeNewTurnStart() {
        // Clear old modifiers before "processing" the news messages (so they affect only this single turn)
        clearMoodModifier()
        population.clearMoodModifier()

        // Check for troops returning from the battlefield
        WarManager.getAllReturningTroops(this).forEach{
            messageList.add(ReturningTroopsMessage(it.origin, it.units))
        }

        // Only show the update messages if the flag is not true
        // This is important in order to not show the update to a players first turn (or later when loading a saved game etc.)
        if (firstTurn){
            // Set this flag to false so the next turn everything goes as normal
            firstTurn = false
        } else {
            // NOT the first turn: calculate updates and show the messages!
            messageList.add(population.processPopulationChange(this))
            //land.buildings.updateUsedBuildings(population)  // Updating employment (since people changed) Needed here??
            messageList.add(processHarvest(this)) // Harvest
            //...
        }
    }


    /** This is called directly when the player has finished reading all news and is now starting the turn.
     *  Use this in order to update player values (e.g. mood, used buildings, morale...)
     *
     *  NOTE: This is executed after the player has read all the messages at the start of the turn.
     *  Doing anything regarding that is too late here. Consider executing it in beforeNewTurnStart()! */
    fun onNewTurnStarted() {
        calculateMood()                                 // Updating mood (In case something has changed - e.g. events)
        land.buildings.updateUsedBuildings(population)  // Updating employment for the first turn, showing correct predictions
        this.money -= calculateTroopPayment()
        // ...
    }



        //</editor-fold>

    // ------------------------------------------------------------------------
    //<editor-fold desc="Message Management">
    // ------------------------------------------------------------------------


    /** Returns the next News for this player (EventMessage)
     *  and removes it from the list of outstanding messages
     *  That way you can easily pull them without the need
     *  to care about deleting or counting them. */
    fun getNextMessage() : ReportMessage?{
        val message = messageList.firstOrNull()
        messageList.remove(message)
        return message
    }


    /** Allows other players to send messages to this player (e.g. diplomacy, war, donations...) */
    fun addMessage(messsage : ReportMessage){
        messageList.add(messsage)
    }

    /** Sets a message to the "front" of the message queue. This comes handy when one message should trigger another
     * (e.g. a war declaration reaction has a direct new message for a battle) */
    fun addMessageToFrontOfList(messsage : ReportMessage){
        messageList.add(0, messsage)
    }


    /** Buys/builds a specific building for this user */
    fun buyBuilding(building: BuildingType) {
        //println("Buying $building for a price of ${building.price}.")
        //TODO Palace and Cathedral can only be purchased one piece per turn
        when(building){
            BuildingType.MARKET -> { land.buildings.markets ++ }
            BuildingType.MILL -> { land.buildings.mills ++}
            BuildingType.GRANARY -> { land.buildings.granaries ++}
            BuildingType.WAREHOUSE -> { land.buildings.warehouses ++}
            BuildingType.SCHOOL -> { land.buildings.schools ++}
            BuildingType.PALACE -> { land.buildings.palacePieces ++}
            BuildingType.CATHEDRAL -> { land.buildings.cathedralPieces ++}
        }

        //TODO: Add pricing discount. This could come from random messages (e.g. wood shortage increases the price)
        money -= building.price
        land.buildings.updateUsedBuildings(population)
    }


    /** Returns the local price for a certain building.
     *  This is important since building prices might vary from turn to turn.
     *  E.g. when resources are cheap or work force is scarce. */
    fun getPriceForBuilding(building : BuildingType):Int{
        return (building.price * playerBuildingPriceMultiplier).toInt()
    }

    /** Calculates the mood of the population defined by the player's laws */
    fun calculateMood() {
        population.calculateMood(laws)
    }

    /** Calculates the tax calculation. This can be used
     *  to estimate tax income for the current turn and
     *  also to calculate the real income at the beginning of a turn */
    fun calculateTaxBalance() : Int {
        land.buildings.updateUsedBuildings(population) // Update building usage

        val taxMills = land.buildings.usedMills * BuildingType.MILL.income
        val taxGranaries = land.buildings.usedGranaries * BuildingType.GRANARY.income
        val taxMarkets = land.buildings.usedMarkets * BuildingType.MARKET.income
        val totalPotentialTax = taxMills + taxGranaries + taxMarkets

//        println("Income: Mills: $taxMills, Granaries: $taxGranaries, Markets: $taxMarkets -> $totalPotentialTax")

        val incomeTax = totalPotentialTax * laws.incomeTax * laws.lawEnforcement
        val healthExpenses = population.getAmountPeople() * BASE_EXPENSE_HEALTH_SYSTEM * laws.healthSystem
        val educationExpenses = population.children.size * BASE_EXPENSE_EDUCATION_SYSTEM * laws.educationSystem

//        println("Adults: ${population.adults.size}, Total: ${population.getAmountPeople()}, Children: ${population.children.size}")
//        println("Tax income (pure): $incomeTax")
//        println("Health expenses: $healthExpenses")
//        println("Education expenses: $educationExpenses")

        return (incomeTax - healthExpenses - educationExpenses).toInt()
    }


    //</editor-fold>

    override fun toString(): String {
        return "Player $name ($country)"
    }

    /** This donates a resource to a given player.
     *  In Detail:
     *  - Reduce the resource by the given amount
     *  - Send the selected player a donation message containing this resource.
     *  -> The selected player then can either accept or reject this donation at the beginning of the next turn
     *  //TODO It might come handy to allow only 1 donation per turn (per player) so players don't donate too much / cheat
     */
    fun donateResource(selectedPlayer: Player, selectedResource: ResourceType, donationAmount: Int) {
        var people : List<Person>? = null

        when(selectedResource){
            ResourceType.MONEY -> { this.money -= donationAmount }
            ResourceType.LAND -> {
                // Removes the land and updates the used buildings
                land.removeLand(donationAmount, population)
            }
            ResourceType.POPULATION -> {
                people = population.removeAdults(donationAmount)
                land.buildings.updateUsedBuildings(population)
            }
            ResourceType.FOOD -> {
                this.storedFood -= donationAmount
                // If there was food selected for distribution, this might be reduced since there was not enough available
                this.foodForDistribution = foodForDistribution.coerceAtMost(storedFood)
            }
        }

        selectedPlayer.addMessage(DonationMessage(this, selectedResource, donationAmount, people))
    }


    /** Adds a military unit to the players troops */
    fun addMilitaryUnit(unit : MilitaryUnit){
        if(military[unit.type] == null){
            println("Adding a new list for the unit type")
            military[unit.type] = mutableListOf()
        }
        military[unit.type]!!.add(unit)
        println("Added ${unit}- (currently ${military[unit.type]} units)")
    }


    /** Removes a military unit from the players troops.
     *  This can happen due to dying, selling, donating etc. */
    fun removeMilitaryUnit(unit : MilitaryUnit){
        println("Removing unit ${unit} (from ${military[unit.type]} units)")
        military[unit.type]!!.remove(unit)
    }


    /** Calculates how much pay the military units require per year */
    fun calculateTroopPayment() : Int {
        var sum = 0
        military.keys.forEach { type ->
            // The pay of the unit multiplied by the amount of unity in this category
            sum += military[type]!!.sumOf{ it.pay }
        }
        println("Calculated pay for troops: $sum")
        return sum
    }



    /** Issues a sabotage action
     *  //TODO It might come handy to allow only 1 spy action per turn (per player) so players don't donate too much / cheat
     */
    fun sabotagePlayer(selectedPlayer: Player, sabotageType: SabotageType, cost : Int) {
        this.money -= cost

        when(sabotageType){
            SabotageType.STEAL_MONEY -> {}
            SabotageType.BURN_MILLS -> {}
            SabotageType.START_REVOLT -> {}
            SabotageType.DEMORALIZE_TROOPS -> {}
        }

        selectedPlayer.addMessage(SabotageMessage(this, sabotageType))
    }


    /** adds / subtracts a mood bonus for the troops */
    fun addMoodBonus(amount : Double) {
        this.militaryMoodModifier += amount
        println("Miliarty Mood Modifer: $militaryMoodModifier")
    }

    /** Clears the mood bonus/malus for the troops */
    private fun clearMoodModifier(){
        this.militaryMoodModifier = 0.0
    }

}