package de.tobiasreich.kaiser.game.data.country

import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_HEIGHT_FIELDS
import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_WIDTH_FIELDS

/** Defines the country and it's buildings. */
class Land {

    companion object{

        const val LAND_USED_PER_FARMER = 5 // ha that can be worked on by 1 single farmer (person)
        const val FOOD_HARVESTED_BY_FARMER = 5.0 // How much food can be harvested optimally per farmer
    }

    val available : Int = 10000     // How many ha the user possesses

    val buildings = Buildings()     // The standard population at start

    fun getLandViewMatrix() : Array<Array<BuildingType>>{
        val matrix = Array(LAND_WIDTH_FIELDS) { Array(LAND_HEIGHT_FIELDS) { BuildingType.FREE } }
        // 1st parameter is column, 2nd parameter is the row
        matrix[0][0] = BuildingType.MARKET
        matrix[1][1] = BuildingType.BARN
        matrix[1][2] = BuildingType.BARN
        matrix[14][3] = BuildingType.MARKET
        return matrix
    }
}