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


enum class HarvestEvent(val effect : Float){

    NOTHING(1.0f),            // Nothing happens this year
    RATS_PLAGUE(0.7f),        // Rats were eating a lot of the food this year
    GREAT_WEATHER(1.2f),      // There was great weather and the harvest was better
    THEFT(0.9f),              // Someone stole a good amount of food from the granary

    //... TODO: Think of more events
}