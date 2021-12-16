package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.ViewController
import java.util.*

object Game {

    /** Language Bundle for resources */
    lateinit var stringsBundle : ResourceBundle

    private lateinit var players : List<Player>
    lateinit var currentPlayer : Player

    var currentYear = 1400

    /** Creates a new game */
    fun setupGame(players : List<Player>){
        this.players = players
        this.currentPlayer = players[0]
    }


    /** Finishes the turn of the current player, proceeds with the next player and shows the News Screen
     *
     */
    fun endTurn(){
        // Process Food distribution
        currentPlayer.processFood()

        // Switch to the next player!

        val nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size
        if (nextPlayerIndex == 0){
            currentYear++
        }

        currentPlayer = players[nextPlayerIndex]
        currentPlayer.startNewTurn()
        ViewController.showScene(ViewController.SCENE_NAME.NEXT_PLAYER)
        //NextPlayerScreenUIController.showPlayerNews()
    }

    // The year is basically the turn number
    fun getYear(): Int{
        return currentYear
    }

}