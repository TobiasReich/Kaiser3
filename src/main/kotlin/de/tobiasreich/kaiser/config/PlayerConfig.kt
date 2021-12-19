package de.tobiasreich.kaiser.config

import de.tobiasreich.kaiser.game.data.player.CountryName
import javafx.scene.paint.Color

class PlayerConfig(val id : Int, val country : CountryName, var name : String, var color : Color, var  male : Boolean, var isAI : Boolean, var active : Boolean, var difficulty : Int)
