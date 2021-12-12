package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.game.data.country.Buildings
import de.tobiasreich.kaiser.game.data.country.Buildings.Companion.GRAIN_PER_GRANARY
import de.tobiasreich.kaiser.game.data.country.HarvestCondition
import de.tobiasreich.kaiser.game.data.player.CountryName
import de.tobiasreich.kaiser.game.data.player.Title
import de.tobiasreich.kaiser.game.data.population.Population

/** This is the complete configuration and setup of once specific player
 *  A player consists of
 *  - a name (without title) -> The name shown in the game
 *  - a gender represented as "isMale" -> We have only 2 genders here, this is a game in 1400
 *  - a population -> Representing all the people living in the players reign
 *  - wheat -> The amount of wheat that is stored in the players granary
 */
class Player(val name : String, val isMale : Boolean, val countryName : CountryName) {

    /** This processes the decisions of the player for the upcoming year
     *  This includes
     *  - calculate population growth / shrinking
     *  - calculate health
     *  - calculate work output
     *  - calculate taxes
     *  - calculate educational changes
     *  - calculate mood
     *  - ...
     */
    fun processPlayer() {
        population.processPopulationChange()
    }

    var playerTitle = Title.MISTER  // We automatically start with the lowest title Mr/Mrs
    val population = Population()   // The standard population at start
    val buildings = Buildings()     // The standard population at start

    var wheat = 1000                // the resources (how much wheat is in the granaries)
        get(){
            return field
        }
        private set(value){
            field = value
        }

    var wheatPrice = 50             // the current wheat price for this player
        get(){
            return field
        }
        private set(value){
            field = value
        }

    /** Defines the current harvest condition this year -> Defining the wheat price */
    var harvestCondition = HarvestCondition.NORMAL_HARVEST

    fun setNewHarvestCondition(){
        harvestCondition = HarvestCondition.values().random()
    }

    // ------------------------------------------------------------------------
    //<editor-fold desc="Wheat Management">
    // ------------------------------------------------------------------------

    /** Convenience method determining how much grain can be stored in
     *  the countries granaries (in total)
     *  This is defined as:
     *    buildings.granaries * GRAIN_PER_GRANARY
     */
    fun getMaxWheatStorage() : Int {
        return buildings.granaries * GRAIN_PER_GRANARY
    }

    /** This adds / subtracts wheat
     *  It returns a boolean determining whether the purchase was valid
     *  A player can not buy more wheat than there is capacitiy in the granaries
     *  A player can not sell more wheat than all that's left in the granary
     */
    fun addWheat(amount : Int) : Boolean{
        if (wheat + amount > getMaxWheatStorage()){
            return false
        }
        if (wheat + amount < 0){
            return false
        }

        wheat += amount
        return true
    }




    //</editor-fold>
}