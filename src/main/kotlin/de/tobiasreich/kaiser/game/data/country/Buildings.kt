package de.tobiasreich.kaiser.game.data.country

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.population.Population

/** Configuration of buildings existing in a country */
class Buildings {

    companion object{
        const val GRAIN_STORED_PER_GRANARY = 10000
        const val FOOD_PRODUCED_PER_MILL = 1000
    }

    var markets = 1
    var mills = 1
    var granaries = 1
    var palacePieces = 0
    var cathedralPieces = 0
    var warehouses = 0
    var schools = 0

    var usedMills = 0
    var usedGranaries = 0
    var usedMarkets = 0

    /** Call this when ever the population changes.
     *  This calculates how many buildings are used by the population.
     *  This can be used for calculating taxes as well as unemployment rates */
    fun updateUsedBuildings(population: Population){
        // Final income / expenses
        var availableWorkers = population.adults.size

        val amountMills = mills
        val amountGranaries = granaries
        val amountMarkets = markets
        //TODO: Add a weight for jobs. E.g. 50% of people work in farms if possible

        // Calculate how many buildings are fully occupied. Then reduce workers used by them.
        // e.g. 550 workers will result in: 550 / 100 = 5

        println("Workers available: $availableWorkers")

        // --- Mills ---
        usedMills = if (availableWorkers / Game.WORKERS_PER_BUILDING > amountMills){
            amountMills
        } else {
            availableWorkers / Game.WORKERS_PER_BUILDING
        }
        availableWorkers-= usedMills * Game.WORKERS_PER_BUILDING // Reduce total workers by the amount of mill workers

        //println("Workers available: $availableWorkers")

        // --- Granaries ---
        usedGranaries = if (availableWorkers / Game.WORKERS_PER_BUILDING > amountGranaries){
            amountGranaries
        } else {
            amountGranaries / Game.WORKERS_PER_BUILDING
        }
        availableWorkers-= usedGranaries * Game.WORKERS_PER_BUILDING // Reduce total workers by the amount of mill workers

        //println("Workers available: $availableWorkers")

        // --- Markets ---
        usedMarkets = if (availableWorkers / Game.WORKERS_PER_BUILDING > amountMarkets){
            amountMarkets
        } else {
            availableWorkers / Game.WORKERS_PER_BUILDING
        }
        availableWorkers-= usedMarkets * Game.WORKERS_PER_BUILDING // Reduce total workers by the amount of mill workers

        //println("Workers available: $availableWorkers")

        // Now we calculate the tax variation factors
        println("Used buildings: Mills: $usedMills, Granaries: $usedGranaries, Markets: $usedMarkets")
    }

    // Other buildings:

    // churches
    // hospitals / infirmary
    // prisons
    // ...
}