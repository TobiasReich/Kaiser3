package de.tobiasreich.kaiser.game.data.country

import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_HEIGHT_FIELDS
import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_WIDTH_FIELDS
import de.tobiasreich.kaiser.game.data.population.Population
import kotlin.math.floor

/** Defines the country and it's buildings. */
class Land {

    companion object{

        const val LAND_USED_PER_FARMER = 5 // ha that can be worked on by 1 single farmer (person)
        const val FOOD_HARVESTED_BY_FARMER = 5.0 // How much food can be harvested optimally per farmer
    }

    // This is the size of one tile in the map view, larger values lead to a bigger image
    // This is a per player setting
    var zoomLevelImageSize = 40.0

    var landSize : Int = 10000      // How many ha the user possesses

    val buildings = Buildings()     // The standard population at start

    fun getLandViewMatrix() : Array<Array<BuildingType?>>{
        val matrix = Array(LAND_WIDTH_FIELDS) { arrayOfNulls<BuildingType>(LAND_HEIGHT_FIELDS) }
        // 1st parameter is column, 2nd parameter is the row
        matrix[0][0] = BuildingType.MARKET
        matrix[1][1] = BuildingType.GRANARY
        matrix[1][2] = BuildingType.GRANARY
        matrix[14][3] = BuildingType.MARKET
        matrix[11][12] = BuildingType.MARKET
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