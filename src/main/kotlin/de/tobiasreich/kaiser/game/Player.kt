package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.config.PlayerConfig
import de.tobiasreich.kaiser.game.data.country.BuildingType
import de.tobiasreich.kaiser.game.data.country.Buildings.Companion.FOOD_PRODUCED_PER_MILL
import de.tobiasreich.kaiser.game.data.country.Buildings.Companion.GRAIN_STORED_PER_GRANARY
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import de.tobiasreich.kaiser.game.data.country.HarvestEvent
import de.tobiasreich.kaiser.game.data.country.Land
import de.tobiasreich.kaiser.game.data.military.MilitaryUnit
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
    var miliarty = mutableMapOf<MilitaryUnit, Int>()


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
     *  Then processes the decisions of the player of the last year
     *  This includes
     *  - calculate population growth / shrinking
     *  - calculate health
     *  - calculate work output
     *  - calculate taxes
     *  - calculate educational changes
     *  - calculate mood
     *  - ...
     */
    fun startNewTurn() {
        //messageList.clear() //Don't cleat this list or messages from other players will be lost!!!

        // Only show the update messages if the flag is not true
        // This is important in order to not show the update to a players first turn (or later when loading a saved game etc.)
        if (firstTurn){
            // Set this flag to false so the next turn everything goes as normal
            firstTurn = false
            land.buildings.updateUsedBuildings(population)  // Updating employment for the first turn, showing correct predictions
        } else {
            // NOT the first turn: calculate updates and show the messages!
            messageList.add(population.processPopulationChange(this))
            land.buildings.updateUsedBuildings(population)  // Updating employment (since people changed)
            messageList.add(processHarvest(this))
            //...
        }
        // ------- Update player statistics -------

        calculateMood()                                 // Updating mood (In case something has changed - e.g. events)
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

        println("Income: Mills: $taxMills, Granaries: $taxGranaries, Markets: $taxMarkets -> $totalPotentialTax")

        val incomeTax = totalPotentialTax * laws.incomeTax * laws.lawEnforcement
        val healthExpenses = population.getAmountPeople() * BASE_EXPENSE_HEALTH_SYSTEM * laws.healthSystem
        val educationExpenses = population.children.size * BASE_EXPENSE_EDUCATION_SYSTEM * laws.educationSystem

        println("Adults: ${population.adults.size}, Total: ${population.getAmountPeople()}, Children: ${population.children.size}")
        println("Tax income (pure): $incomeTax")
        println("Health expenses: $healthExpenses")
        println("Education expenses: $educationExpenses")

        return (incomeTax - healthExpenses - educationExpenses).toInt()
    }

    /** Returns the percentage of employment.
     *  A higher value means higher employment (so 100% is the desired value) */
    fun getEmploymentRate() : Int {
        return 50
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
        if(miliarty[unit] == null){
            println("Adding first ${unit.name} to the troops")
            miliarty[unit] = 1
        } else {
            miliarty[unit] = miliarty[unit]!! + 1
            println("Added ${unit.name}- (currently ${miliarty[unit]} units)")
        }
    }


    /** Removes a military unit from the players troops.
     *  This can happen due to dying, selling, donating etc. */
    fun removeMilitaryUnit(unit : MilitaryUnit){
        println("Removing unit ${unit.name} (from ${miliarty[unit]} units)")
        miliarty[unit] = miliarty[unit]!! - 1
    }

}