package de.tobiasreich.kaiser.game

import de.tobiasreich.kaiser.ViewController

object Game {

    private lateinit var players : List<Player>
    lateinit var currentPlayer : Player

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

        // Switch to the next player

        val nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size
        currentPlayer = players[nextPlayerIndex]
        currentPlayer.startNewTurn()
        ViewController.showScene(ViewController.SCENE_NAME.NEXT_PLAYER)
        //NextPlayerScreenUIController.showPlayerNews()
    }

    // The year is basically the turn number
    fun getYear(): Int{
        return 1400
    }

}