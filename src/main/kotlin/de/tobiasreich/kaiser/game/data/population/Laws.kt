package de.tobiasreich.kaiser.game.data.population

/** Collection class for all set laws of the given player
 *  (E.g. taxes, immigration law, education expenses...) */
data class Laws(var incomeTax : Double = 0.1, var lawEnforcement : Double = 0.5,
           var immigrationStrictness : Double = 0.9, var healthSystem : Double = 0.5,
           var educationSystem : Double = 0.5)