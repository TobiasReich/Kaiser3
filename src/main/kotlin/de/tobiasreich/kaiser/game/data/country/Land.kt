package de.tobiasreich.kaiser.game.data.country

import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_FIELD_WIDTH
import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_HEIGHT_FIELDS
import de.tobiasreich.kaiser.game.data.population.Population
import kotlin.math.floor

/** Defines the country and it's buildings. */
class Land {

    companion object{

        const val LAND_USED_PER_FARMER = 5 // ha that can be worked on by 1 single farmer (person)
        const val FOOD_HARVESTED_BY_FARMER = 5.0 // How much food can be harvested optimally per farmer

        const val HEIGHT_MILL = 4
        const val HEIGHT_GRANARY = 5
        const val HEIGHT_MARKET = 6
        const val HEIGHT_WAREHOUSE = 7

    }

    // This is the size of one tile in the map view, larger values lead to a bigger image
    // This is a per player setting
    var zoomLevelImageSize = 40.0

    var landSize : Int = 10000      // How many ha the user possesses

    val buildings = Buildings()     // The standard population at start


    /** This returns a matrix of the land to draw */
    fun getLandViewMatrix() : Array<Array<BuildingType?>>{
        val width = landSize / LAND_FIELD_WIDTH
        val matrix = Array(width) { arrayOfNulls<BuildingType>(LAND_HEIGHT_FIELDS) }

        // For every market
        // We can have 1 market / 1000 ha land
        // That means we can draw every market on every 1000/LAND_FIELD_WIDTH fields
        // E.g. LAND_FIELD_WIDTH = 100 -> every MARKET.landNeeded/100 = 10 fields.
        // Thus: 2 Markets -> 1st market at field 0, 2nd market at field 10
        val fieldsPerMill = BuildingType.MILL.landNeeded / LAND_FIELD_WIDTH
        for (mill in 0 until buildings.mills){
            // 1st parameter is column (width), 2nd parameter is the row (height)
            matrix[mill * fieldsPerMill + 3][HEIGHT_MILL] = BuildingType.MILL
        }

        val fieldsPerMarket = BuildingType.MARKET.landNeeded / LAND_FIELD_WIDTH
        for (market in 0 until buildings.markets){
            // 1st parameter is column (width), 2nd parameter is the row (height)
            matrix[market * fieldsPerMarket + 3][HEIGHT_MARKET] = BuildingType.MARKET
        }

        val fieldsPerGranary = BuildingType.GRANARY.landNeeded / LAND_FIELD_WIDTH
        for (granary in 0 until buildings.granaries){
            // 1st parameter is column (width), 2nd parameter is the row (height)
            matrix[granary * fieldsPerGranary + 3][HEIGHT_GRANARY] = BuildingType.GRANARY
        }


       // matrix[0][0] = BuildingType.MARKET
        //matrix[0][5] = BuildingType.GRANARY
//        matrix[1][2] = BuildingType.GRANARY
//        matrix[14][3] = BuildingType.MARKET
//        matrix[11][12] = BuildingType.MARKET
        return matrix
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