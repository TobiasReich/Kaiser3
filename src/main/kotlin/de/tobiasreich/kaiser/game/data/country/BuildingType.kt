package de.tobiasreich.kaiser.game.data.country

enum class BuildingType(val price : Int) {

    MARKET(1000),
    MILL(2000),
    GRANARY(4000),
    WAREHOUSE(5000),
    SCHOOL(6000),
    PALACE(8000),
    CATHEDRAL(10000),

    //TODO Add more buildings

}