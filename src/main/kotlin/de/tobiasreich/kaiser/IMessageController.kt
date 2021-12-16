package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.data.player.ReportMessage

/** Interface for all Controllers that are sharing the ability to process EventMessages (turn info)
 *  e.g.
 *  - Population updates
 *  - harvest updates
 *  ...
 */
interface IMessageController {

    fun setMessage(message : ReportMessage)

}