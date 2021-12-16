package de.tobiasreich.kaiser.game.data.country

/** Defines the country and it's buildings. */
class Land {

    companion object{

        const val LAND_USED_PER_FARMER = 5 // ha that can be worked on by 1 single farmer (person)
        const val FOOD_HARVESTED_BY_FARMER = 5.0 // How much food can be harvested optimally per farmer
    }

    val available : Int = 10000     // How many ha the user possesses

    val buildings = Buildings()     // The standard population at start
}