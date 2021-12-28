package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.player.ReportMessage
import javafx.scene.layout.Pane

/** Interface for all Controllers that are sharing the ability to process EventMessages (turn info)
 *  e.g.
 *  - Population updates
 *  - harvest updates
 *  ...
 */
interface IMessageController {

    /** This is the same logic for all message Controllers.
     *  It is checked whether the player has any additional messages.
     *  If so, the next message controller is shown.
     *  Else, the game view gets started and the player can make the turn. */
    fun proceedToNextNews() {
        println("Proceed with next message!")
        val message = Game.currentPlayer.getNextMessage()

        if (message == null){
            Game.currentPlayer.onNewTurnStarted()
            ViewController.showGameScene()
        } else {
            val loader = message.getViewLoader()
            val view = loader.load() as Pane
            val nextSceneController = loader.getController() as IMessageController
            nextSceneController.setMessage(message)
            ViewController.showView(view)
        }
    }

    fun setMessage(message : ReportMessage)

}