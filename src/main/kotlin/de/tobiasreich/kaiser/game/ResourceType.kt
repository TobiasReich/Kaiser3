package de.tobiasreich.kaiser.game

/** Types of resources that are used thoughout the game.
 *  E.g. for Donations, asking for tributes, war... */
enum class ResourceType(val nameResource: String) {

    MONEY("resource_money"),
    LAND("resource_land"),
    POPULATION("resource_population"),
    FOOD("resource_food")

}