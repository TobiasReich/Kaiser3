package de.tobiasreich.kaiser.game.data.country

/** Types of buildings that can be build in the building menu
 *  - price is the amount of money spend when acquiring one
 *  - landNeeded is the amount of land that is used per built one */
enum class BuildingType(val price : Int, val landNeeded : Int) {

    MARKET(1000, 500),
    MILL(2000, 1000),
    GRANARY(4000, 5000),
    WAREHOUSE(5000, 10000),
    SCHOOL(6000, 8000),
    PALACE(8000, 10000),
    CATHEDRAL(10000, 10000),

    //TODO Add more buildings

}