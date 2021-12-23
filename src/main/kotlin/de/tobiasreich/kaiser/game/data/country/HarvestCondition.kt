package de.tobiasreich.kaiser.game.data.country

/** This defines how good the harvest was.
 *  NOTE: This does not have any influence on the random effects like a blight or rats plague */
enum class HarvestCondition(val harvestRatio : Float) {

    FANTASTIC_HARVEST(1.2f),
    GOOD_HARVEST(1.1f),
    NORMAL_HARVEST(1.0f),
    BAD_HARVEST(0.8f),
    TERRIBLE_HARVEST(0.5f)
}

/** Events that happened after the harvest.
 *  If it is anything else aside from "NOTHING" some of the food is lost */
enum class HarvestEvent(val effect : Float){

    RATS_PLAGUE(0.7f),        // Rats were eating a lot of the food this year
    ROTTEN_FOOD(0.8f),        // Some food is rotten
    THEFT(0.9f),              // Someone stole a good amount of food from the granary

    //... TODO: Think of more events
}