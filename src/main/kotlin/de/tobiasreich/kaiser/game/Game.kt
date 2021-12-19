package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.ViewController
import de.tobiasreich.kaiser.config.PlayerConfig
import java.util.*

object Game {

    /** Language Bundle for resources */
    lateinit var stringsBundle : ResourceBundle

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
            Player(it.name, it.male, it.country, it.color)
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
            currentYear++
            println("----- NEW YEAR -----")
        }

        currentPlayer = players[nextPlayerIndex]
        ViewController.showScene(ViewController.SCENE_NAME.NEXT_PLAYER)
        //NextPlayerScreenUIController.showPlayerNews()
    }

    // The year is basically the turn number
    fun getYear(): Int{
        return currentYear
    }

}