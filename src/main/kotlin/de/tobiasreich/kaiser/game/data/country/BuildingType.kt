package de.tobiasreich.kaiser.game.data.country

/** Types of buildings that can be build in the building menu
 *  - price is the amount of money spend when acquiring one
 *  - landNeeded is the amount of land that is used per built one */
enum class BuildingType(val price : Int, val landNeeded : Int, val income : Int) {

    /* Produce food and mediocre income */
    MILL(2000, 1000, 400),

    /* Can store food but bad income */
    GRANARY(4000, 5000, 200),

    /* Produce no food but high income */
    MARKET(1000, 1000, 800),

    /* These buildings do not produce food nor income */
    WAREHOUSE(5000, 10000, 0),
    SCHOOL(6000, 8000, 0),
    PALACE(8000, 10000, 0),
    CATHEDRAL(10000, 10000, 0),

    //TODO Add more buildings

}