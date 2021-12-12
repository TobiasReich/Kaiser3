package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.game.data.player.Country
import de.tobiasreich.kaiser.game.data.player.Title
import de.tobiasreich.kaiser.game.data.population.Population

/** This is the complete configuration and setup of once specific player
 *  A player consists of
 *  - a name (without title) -> The name shown in the game
 *  - a gender represented as "isMale" -> We have only 2 genders here, this is a game in 1400
 *  - a population -> Representing all the people living in the players reign
 *  - wheat -> The amount of wheat that is stored in the players granary
 */
class Player(val name : String, val isMale : Boolean, val country : Country) {

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

    var title = Title.MISTER // We automatically start with the lowest title Mr/Mrs

    val population = Population() // The standard population at start

    val wheat = 1000



}