package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.ViewController
import de.tobiasreich.kaiser.config.PlayerConfig
import java.util.*

object Game {

    // The maximum fraction of resources that a player is allowed to donate
    const val MAX_DONATION_AMOUNT = 0.1

    // Duration (in seconds) how long each phase (day) of the battle field lasts. Default = 1 second
    var battleSpeed = 1.0

    /** Language Bundle for resources */
    lateinit var resourcesBundle : ResourceBundle

    private lateinit var players : List<Player>
    lateinit var currentPlayer : Player

    var currentYear = 1400

    /** Creates a new game */
    fun setupGame(players : List<PlayerConfig>){
        this.players = createPlayers(players)
        this.currentPlayer = this.players[0]
    }

    /** This creates the player objects from the PlayerConfig when setupGame is called */
    private fun createPlayers(players : List<PlayerConfig>) : List<Player>{
        return players.map {
            Player(it)
        }
    }

    /** Finishes the turn of the current player, proceeds with the next player and shows the News Screen
     *
     */
    fun endTurn(){
        // Process Food distribution
        currentPlayer.processFood()

        // Switch to the next player!

        val nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size
        println("----- NEXT PLAYER (#$nextPlayerIndex) -----")

        if (nextPlayerIndex == 0){
            println("----- NEW YEAR -----")
            currentYear++
            DiplomacyManager.cleanupTreaties(currentYear)
        }

        currentPlayer = players[nextPlayerIndex]
        ViewController.showNextPlayerScene()
    }

    // The year is basically the turn number
    fun getYear(): Int{
        return currentYear
    }


    /** Returning a list of players that are not the current player */
    fun getAllOtherPlayers() : List<Player>{
        return players.filter { it != currentPlayer }
    }

    // ---------------------------------------------------------


    /** Determines how many workers are used per building.
     *  Currently, that is 100 people per building.
     *  That means:
     *  - having 100 people but 2 buildings, the second one does not generate profit.
     *  - having 200 people but 1 building, the 100 excess people do not work nor pay taxes */
    const val WORKERS_PER_BUILDING = 100

}