package de.tobiasreich.kaiser.config

import de.tobiasreich.kaiser.game.data.player.Country
import javafx.scene.paint.Color

class PlayerConfig(val id : Int, // Might not be needed since we have an order
                   val country : Country, // Enum defining the country the player rules
                   var name : String,  // Player name without title
                   var color : Color,  // Player color for better UX
                   var male : Boolean, // Player gender (only used for propper titles like "King" vs "Queen")
                   var isAI : Boolean, // Defines whether it is an AI player (no visible turn is made by that player)
                   var active : Boolean, // This player slot is enabled or not. Inactive players practically don't exist in gameplay
                   var difficulty : Int, // Unused for now. Could define harvest and events
                   )
