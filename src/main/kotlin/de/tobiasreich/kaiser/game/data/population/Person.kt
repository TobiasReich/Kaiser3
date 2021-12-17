package de.tobiasreich.kaiser.game.data.population

import de.tobiasreich.kaiser.game.data.population.Health.HEALT_GOOD

/** Simple data representation of one specific person having
 * - age
 * - education
 * - health
 * - mood // Ranging from 100 (perfect) to 0 (will riot for sure)
 * ...
 */
class Person(var age: Int = 0,
             var education: Education = Education.BAD,
             var health: Int = HEALT_GOOD,
             var mood : Int = 100 // Ranging from 100 (perfect) to 0 (will riot for sure)
)

