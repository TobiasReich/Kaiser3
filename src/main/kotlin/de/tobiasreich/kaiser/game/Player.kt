package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.config.PlayerConfig
import de.tobiasreich.kaiser.game.data.country.BuildingType
import de.tobiasreich.kaiser.game.data.country.Buildings.Companion.GRAIN_PER_GRANARY
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import de.tobiasreich.kaiser.game.data.country.HarvestEvent
import de.tobiasreich.kaiser.game.data.country.Land
import de.tobiasreich.kaiser.game.data.country.Land.Companion.FOOD_HARVESTED_BY_FARMER
import de.tobiasreich.kaiser.game.data.country.Land.Companion.LAND_USED_PER_FARMER
import de.tobiasreich.kaiser.game.data.player.Country
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import de.tobiasreich.kaiser.game.data.player.HarvestReport
import de.tobiasreich.kaiser.game.data.player.Title
import de.tobiasreich.kaiser.game.data.population.Population
import de.tobiasreich.kaiser.game.data.population.Population.Companion.FOOD_USE_PER_PERSON
import javafx.scene.paint.Color
import java.util.*
import kotlin.math.min

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

    var money = 1000                // The amount of money the player has

    var playerTitle = Title.MISTER  // We automatically start with the lowest title Mr/Mrs
    val population = Population()   // The standard population at start
    val land = Land()               // How many ha the user possesses

    var storedFood = 1000           // The resources (how much wheat is in the granaries)
    var foodForDistribution = 1000  // Amount of food to be distributed (i.e. how much do they get for that year)
    var foodPrice = 50              // The current wheat price for this player this year


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
        val harvestEvent = HarvestEvent.values().random()

        // Now get how many adults work on the field
        // TODO: Once jobs are implemented, we want to select only farmers
        val farmer = player.population.adults.size

        val processableLandSlots = player.land.available / LAND_USED_PER_FARMER

        // This determines how many farm land "parcels" will be processed
        val processedSlots = min(processableLandSlots, farmer)

        val harvestedFood = (FOOD_HARVESTED_BY_FARMER * processedSlots * harvestCondition.harvestRatio).toInt()

        player.storedFood += harvestedFood

        // ----- Work on the HARVEST EFFECTS -----
        player.storedFood = (player.storedFood * harvestEvent.effect).toInt()

        return HarvestReport(harvestCondition, harvestedFood, harvestEvent)
    }

    /** Convenience method determining how much grain can be stored in
     *  the countries granaries (in total)
     *  This is defined as:
     *    buildings.granaries * GRAIN_PER_GRANARY
     */
    fun getMaxWheatStorage() : Int {
        return land.buildings.granaries * GRAIN_PER_GRANARY
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
        messageList.clear()

        // Only show the update messages if the flag is not true
        // This is important in order to not show the update to a players first turn (or later when loading a saved game etc.)
        if (firstTurn){
            // Set this flag to false so the next turn everything goes as normal
            firstTurn = false
        } else {
            // Since this is not the first turn, calculate updates and show the messages
            messageList.add(population.processPopulationChange(this))
            messageList.add(processHarvest(this))
            //...
        }
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
        money -= building.price
    }

    //</editor-fold>
}