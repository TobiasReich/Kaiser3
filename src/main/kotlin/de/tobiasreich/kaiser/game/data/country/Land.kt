package de.tobiasreich.kaiser.game.data.country

import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_FIELD_WIDTH
import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_HEIGHT_FIELDS
import de.tobiasreich.kaiser.game.data.population.Population
import java.lang.Math.min
import kotlin.math.floor

/** Defines the country and it's buildings. */
class Land {

    companion object{

        const val LAND_USED_PER_FARMER = 5 // ha that can be worked on by 1 single farmer (person)
        const val FOOD_HARVESTED_BY_FARMER = 5.0 // How much food can be harvested optimally per farmer

        const val HEIGHT_MILL = 4
        const val HEIGHT_GRANARY = 5
        const val HEIGHT_MARKET = 7
        const val HEIGHT_WAREHOUSE = 8
        const val HEIGHT_SCHOOL = 9

        // The "draw width" for a city (border width
        const val CITY_WIDTH = 6

        const val AMOUNT_PEOPLE_PER_BUILDING = 50
        const val AMOUNT_BUILDINGS_PER_CITY = 5

    }

    // This is the size of one tile in the map view, larger values lead to a bigger image
    // This is a per player setting
    var zoomLevelImageSize = 40.0

    var landSize : Int = 10000      // How many ha the user possesses

    val buildings = Buildings()     // The standard population at start


    /** This returns a matrix of the land to draw */
    //TODO That algorithm is still ugly. Find a better way. This is just a proof of concept!
    fun getLandViewMatrix(population : Population) : Array<Array<BuildingImage?>>{
        val width = landSize / LAND_FIELD_WIDTH
        val matrix = Array(width) { arrayOfNulls<BuildingImage>(LAND_HEIGHT_FIELDS) }

        // First draw the buildings and city borders before drawing special buildings
        setupCities(matrix, population, buildings.markets)

        drawMills(matrix)
        drawMarkets(matrix)
        drawGranaries(matrix)
        drawWarehouses(matrix)
        drawSchools(matrix)

        return matrix
    }

    private fun drawSchools(matrix: Array<Array<BuildingImage?>>) {
        val fieldsPerSchool = BuildingType.SCHOOL.landNeeded / LAND_FIELD_WIDTH
        for (school in 0 until buildings.schools) {
            // 1st parameter is column (width), 2nd parameter is the row (height)
            matrix[school * fieldsPerSchool + 5][HEIGHT_SCHOOL] = BuildingImage.SCHOOL
        }
    }

    private fun drawWarehouses(matrix: Array<Array<BuildingImage?>>) {
        val fieldsPerWarehouse = BuildingType.WAREHOUSE.landNeeded / LAND_FIELD_WIDTH
        for (warehouse in 0 until buildings.warehouses) {
            // 1st parameter is column (width), 2nd parameter is the row (height)
            matrix[warehouse * fieldsPerWarehouse + 5][HEIGHT_WAREHOUSE] = BuildingImage.WAREHOUSE
        }
    }

    private fun drawGranaries(matrix: Array<Array<BuildingImage?>>) {
        val fieldsPerGranary = BuildingType.GRANARY.landNeeded / LAND_FIELD_WIDTH
        for (granary in 0 until buildings.granaries) {
            // 1st parameter is column (width), 2nd parameter is the row (height)
            matrix[granary * fieldsPerGranary + 3][HEIGHT_GRANARY] = BuildingImage.GRANARY
        }
    }

    private fun drawMarkets(matrix: Array<Array<BuildingImage?>>) {
        val fieldsPerMarket = BuildingType.MARKET.landNeeded / LAND_FIELD_WIDTH
        for (market in 0 until buildings.markets) {
            // 1st parameter is column (width), 2nd parameter is the row (height)
            matrix[market * fieldsPerMarket + 3][HEIGHT_MARKET] = BuildingImage.MARKET
        }
    }


    /**
     * For every market
     * We can have 1 market / 1000 ha land
     * That means we can draw every market on every 1000/LAND_FIELD_WIDTH fields
     * E.g. LAND_FIELD_WIDTH = 100 -> every MARKET.landNeeded/100 = 10 fields.
     * Thus: 2 Markets -> 1st market at field 0, 2nd market at field 10
     * 1 2 3 4 5 6 7
     */
    private fun drawMills(matrix: Array<Array<BuildingImage?>>) {
        val fieldsPerMill = BuildingType.MILL.landNeeded / LAND_FIELD_WIDTH
        for (mill in 0 until buildings.mills) {
            // 1st parameter is column (width), 2nd parameter is the row (height)
            matrix[mill * fieldsPerMill + 3][HEIGHT_MILL] = BuildingImage.MILL
        }
    }

    /** This draws cities depending on the buildings
     *
     */
    private fun setupCities(matrix: Array<Array<BuildingImage?>>, population: Population, amountMarkets : Int) {
        // The amount of people determine how many "houses" are build
        // This also defines how many "cities" are "drawn"
        val amountBuildings = population.getAmountPeople() / AMOUNT_PEOPLE_PER_BUILDING
        val amountCities = min(amountBuildings / AMOUNT_BUILDINGS_PER_CITY, 3)

        for (city in 0 until amountCities){
            /* For each city draw the borders
             * ######
             * #    #
             * #    #
             *
             * #    #
             * #    #
             * ######
             *
             * Each city is 10 fields apart
             */

            // 1st parameter is column (width), 2nd parameter is the row (height)
            val startWidth = (city * 10) + 2
            matrix[startWidth  ][3] = BuildingImage.WALL
            matrix[startWidth+1][3] = BuildingImage.WALL
            matrix[startWidth+2][3] = BuildingImage.WALL
            matrix[startWidth+3][3] = BuildingImage.WALL
            matrix[startWidth+4][3] = BuildingImage.WALL
            matrix[startWidth+5][3] = BuildingImage.WALL

            matrix[startWidth  ][4] = BuildingImage.WALL
            matrix[startWidth+5][4] = BuildingImage.WALL

            matrix[startWidth  ][5] = BuildingImage.WALL
            matrix[startWidth+5][5] = BuildingImage.WALL

            matrix[startWidth  ][7] = BuildingImage.WALL
            matrix[startWidth+5][7] = BuildingImage.WALL

            matrix[startWidth  ][8] = BuildingImage.WALL
            matrix[startWidth+5][8] = BuildingImage.WALL

            matrix[startWidth  ][9] = BuildingImage.WALL
            matrix[startWidth+5][9] = BuildingImage.WALL

            matrix[startWidth  ][10] = BuildingImage.WALL
            matrix[startWidth+1][10] = BuildingImage.WALL
            matrix[startWidth+2][10] = BuildingImage.WALL
            matrix[startWidth+3][10] = BuildingImage.WALL
            matrix[startWidth+4][10] = BuildingImage.WALL
            matrix[startWidth+5][10] = BuildingImage.WALL



            matrix[startWidth+1][9] = BuildingImage.HOUSE
            matrix[startWidth+2][9] = BuildingImage.HOUSE
            matrix[startWidth+3][9] = BuildingImage.HOUSE
            matrix[startWidth+4][9] = BuildingImage.HOUSE
        }
    }

    /** States how much buildings of this type can still be bought.
     *  NOTE: This is not the absolute amount but basically
     *  Total - Already Build
     *  A player can't build more buildings of this type when 0 is reached. New land has to be acquired. */
    fun getAvailableSpaceForBuilding(building: BuildingType): Int {
        val amountBuildAlready = when(building){
            BuildingType.MARKET -> buildings.markets
            BuildingType.MILL ->  buildings.mills
            BuildingType.GRANARY ->  buildings.granaries
            BuildingType.WAREHOUSE -> buildings.warehouses
            BuildingType.SCHOOL -> buildings.schools
            BuildingType.PALACE -> buildings.palacePieces
            BuildingType.CATHEDRAL -> buildings.cathedralPieces
        }
        val landAvailable = landSize - (amountBuildAlready * building.landNeeded)
        //println("Land available: $landAvailable, $building land needed: ${building.landNeeded}: Result: ${floor(landAvailable.toDouble() / building.landNeeded.toDouble()).toInt()}")
        return floor(landAvailable.toDouble() / building.landNeeded.toDouble()).toInt()
    }


    fun removeLand(amount: Int, population : Population) {
        this.landSize -= amount

        val millsAvailable = getAvailableSpaceForBuilding(BuildingType.MILL)
        val granariesAvailable = getAvailableSpaceForBuilding(BuildingType.GRANARY)
        val warehousesAvailable = getAvailableSpaceForBuilding(BuildingType.WAREHOUSE)
        val marketsAvailable = getAvailableSpaceForBuilding(BuildingType.MARKET)

        if(millsAvailable < 0){
            buildings.mills += millsAvailable
            println("Mills destroyed: $millsAvailable")
        }
        if(granariesAvailable < 0){
            buildings.granaries += granariesAvailable
            println("Granaries destroyed: $granariesAvailable")
        }
        if(warehousesAvailable < 0){
            buildings.warehouses += warehousesAvailable
            println("Warehouses destroyed: $warehousesAvailable")
        }
        if(marketsAvailable < 0){
            buildings.markets += marketsAvailable
            println("Markets destroyed: $marketsAvailable")
        }

        buildings.updateUsedBuildings(population)
    }
}