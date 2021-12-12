package de.tobiasreich.kaiser.game

object Game {

    private lateinit var players : List<Player>
    private lateinit var currentPlayer : Player

    /** Creates a new game */
    fun setupGame(players : List<Player>){
        this.players = players
        this.currentPlayer = players[0]
    }

}