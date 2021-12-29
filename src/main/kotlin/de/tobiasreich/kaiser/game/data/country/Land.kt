package de.tobiasreich.kaiser.game.data.country

import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_FIELD_WIDTH
import de.tobiasreich.kaiser.UIControllerPlayerLandView.Companion.LAND_HEIGHT_FIELDS
import de.tobiasreich.kaiser.game.data.population.Population
import kotlin.math.floor

/** Defines the country and it's buildings. */
class Land {

    companion object{

        // The "draw width" for a city (border width
        const val CITY_WIDTH = 10

        const val COMPLETE_CITY_POPULATION = 5000
        const val COMPLETE_CITY_MILLS = 4
        const val COMPLETE_CITY_MARKETS = 3
        const val CITY_GRANARIES = 1
        const val CITY_WAREHOUSES = 1
        const val CITY_SCHOOLS = 1

    }

    // This is the size of one tile in the map view, larger values lead to a bigger image
    // This is a per player setting
    var zoomLevelImageSize = 40.0

    var landSize : Int = 10000      // How many ha the user possesses

    val buildings = Buildings()     // The standard population at start


    /** This returns a matrix of the land to draw */
    //TODO That algorithm is still ugly. Find a better way. This is just a proof of concept!
    fun getLandViewMatrix(population : Population) : Array<Array<BuildingImage?>>{
        val widthFields = landSize / LAND_FIELD_WIDTH
        val matrix = Array(widthFields) { arrayOfNulls<BuildingImage>(LAND_HEIGHT_FIELDS) }

        val amountCities = widthFields / CITY_WIDTH

        var amountPeople = population.getAmountPeople()
        var amountMills = buildings.mills
        var amountMarkets = buildings.markets
        var amountGranaries = buildings.granaries
        var amountWarehouses = buildings.warehouses
        var schoolsToDraw = buildings.schools
        //TODO: Add other buildings like palace and cathedral

        for (cityIndex in 0 until amountCities){
            drawCity(matrix, 10 * cityIndex, amountPeople, amountMills, amountMarkets, amountGranaries, amountWarehouses, schoolsToDraw)
            // Reduce maximum values a city can show so the next potential city shows only what it can actually have.
            amountPeople -= COMPLETE_CITY_POPULATION
            amountMills -= COMPLETE_CITY_MILLS
            amountMarkets -= COMPLETE_CITY_MARKETS
            amountGranaries -= CITY_GRANARIES
            amountWarehouses -= CITY_WAREHOUSES
            schoolsToDraw -= CITY_SCHOOLS
        }

        return matrix
    }

    /** This draws one city beginning at the given StartWidth.
     *  The caller can simply define a city (by the properties) and where to draw it (startWitdh)
     *  Excessive values are ignored (e.g. if markets is a too high value, only the max amounts of markets / city are drawn)
     *  That way the caller can always reduce it's amount of objects by a fixed number and doesn't have to worry about dawing
     *  wrong values. Negative values are also ignored
     *
     *  A full city looks like that
     *  <- 10 fields ->
     *
     *     0123456789
     *  0  ..t....T..
     *  1  ..b..t....
     *  2  ..######..
     *  3  ..#m.M.#.t
     *  4  .t#hSmW#.b
     *  5  ..#.MPh#..
     *  6  ====C=====
     *  7  ..#.M.h#t.
     *  8  t.#hGM.#b.
     *  9  ..#m.h.#.t
     *  10 ..######..
     *  11 ..t..b.t..
     *  12 ....T.....
     *  13 .t.....t..
     *
     *  # = wall
     *
     *  M = Mill 4x
     *  m = market 3x
     *  G = Granary 1x
     *  W = Warehouse 1x
     *  h = House 5x
     *  S = School 1x
     *  ... other buildings added when needed
     *  P = Palace (1x)
     *  C = Cathedral (1x)
     *
     *  = = Road (through City)
     *  t = Tree 10x
     *  T = Tree2 3x
     *  b = bush 4x
     */
    private fun drawCity(matrix: Array<Array<BuildingImage?>>, startWidth : Int,  population: Int, mills : Int,
                         markets : Int, granaries: Int, warehouses : Int, schools : Int){
        val housesToDraw = population.coerceIn(0, COMPLETE_CITY_POPULATION) / 1000 // 0..5 houses
        val millsToDraw = mills.coerceIn(0, COMPLETE_CITY_MILLS)
        val marketsToDraw = markets.coerceIn(0, COMPLETE_CITY_MARKETS)
        val granariesToDraw = granaries.coerceIn(0, CITY_GRANARIES)
        val warehousesToDraw = warehouses.coerceIn(0, CITY_WAREHOUSES)
        val schoolsToDraw = schools.coerceIn(0, CITY_SCHOOLS)

        // Only "complete" cities have walls.
        val hasWalls = population > COMPLETE_CITY_POPULATION && mills > COMPLETE_CITY_MILLS && markets > COMPLETE_CITY_MARKETS
        if (hasWalls){
            drawWalls(matrix, startWidth)
        }

        drawRoad(matrix, startWidth)
        drawNature(matrix, startWidth)
        drawHouses(matrix, startWidth, housesToDraw)
        drawMills(matrix, startWidth, millsToDraw)
        drawMarkets(matrix, startWidth, marketsToDraw)
        drawGranaries(matrix, startWidth, granariesToDraw)
        drawWarehouses(matrix, startWidth, warehousesToDraw)
        drawSchools(matrix, startWidth, schoolsToDraw)
    }


    /** This draws the road through the city at height 6 */
    private fun drawRoad(matrix: Array<Array<BuildingImage?>>, startX: Int) {
        println("City complete, drawing walls")

        // 1st parameter is column (width), 2nd parameter is the row (height)
        matrix[startX+0][6] = BuildingImage.ROAD
        matrix[startX+1][6] = BuildingImage.ROAD
        matrix[startX+2][6] = BuildingImage.ROAD
        matrix[startX+3][6] = BuildingImage.ROAD
        matrix[startX+4][6] = BuildingImage.ROAD
        matrix[startX+5][6] = BuildingImage.ROAD
        matrix[startX+6][6] = BuildingImage.ROAD
        matrix[startX+7][6] = BuildingImage.ROAD
        matrix[startX+8][6] = BuildingImage.ROAD
        matrix[startX+9][6] = BuildingImage.ROAD
    }

    /** This draws the wall around a city
     *
     *     0123456789
     *  0  ..t....T..
     *  1  ..b..t....
     *  2  ..######..
     *  3  ..#....#.t
     *  4  .t#....#.b
     *  5  ..#....#..
     *  6  ..........
     *  7  ..#....#t.
     *  8  t.#....#b.
     *  9  ..#....#.T
     *  10 ..######..
     *  11 ..t..b.t..
     *  12 ....T.....
     *  13 .t.....t..
     * */
    private fun drawNature(matrix: Array<Array<BuildingImage?>>, startX: Int) {
        println("City complete, drawing walls")

        // 1st parameter is column (width), 2nd parameter is the row (height)
        matrix[startX+2][0] = BuildingImage.TREE
        matrix[startX+5][1] = BuildingImage.TREE
        matrix[startX+9][3] = BuildingImage.TREE
        matrix[startX+1][4] = BuildingImage.TREE
        matrix[startX+8][7] = BuildingImage.TREE
        matrix[startX+0][8] = BuildingImage.TREE
        matrix[startX+2][11] = BuildingImage.TREE
        matrix[startX+7][11] = BuildingImage.TREE
        matrix[startX+1][13] = BuildingImage.TREE
        matrix[startX+7][13] = BuildingImage.TREE

        matrix[startX+7][0] = BuildingImage.TREE2
        matrix[startX+9][9] = BuildingImage.TREE2
        matrix[startX+4][12] = BuildingImage.TREE2

        matrix[startX+2][1] = BuildingImage.BUSHES
        matrix[startX+9][4] = BuildingImage.BUSHES
        matrix[startX+8][8] = BuildingImage.BUSHES
        matrix[startX+5][11] = BuildingImage.BUSHES
    }


    /** This draws the wall around a city
     *
     *   0123456789
     *  0 ..........
     *  1 ..........
     *  2 ..######..
     *  3 ..#....#..
     *  4 ..#....#..
     *  5 ..#....#..
     *  6 ..........
     *  7 ..#....#..
     *  8 ..#....#..
     *  9 ..#....#..
     *  10..######..
     *  11 ..........
     * */
    private fun drawWalls(matrix: Array<Array<BuildingImage?>>, wallStart: Int) {
        println("City complete, drawing walls")

        // 1st parameter is column (width), 2nd parameter is the row (height)
        matrix[wallStart+2][2] = BuildingImage.WALL_CORNER_NW
        matrix[wallStart+3][2] = BuildingImage.WALL_H
        matrix[wallStart+4][2] = BuildingImage.WALL_H
        matrix[wallStart+5][2] = BuildingImage.WALL_H
        matrix[wallStart+6][2] = BuildingImage.WALL_H
        matrix[wallStart+7][2] = BuildingImage.WALL_CORNER_NE

        matrix[wallStart+2][3] = BuildingImage.WALL_V
        matrix[wallStart+7][3] = BuildingImage.WALL_V

        matrix[wallStart+2][4] = BuildingImage.WALL_V
        matrix[wallStart+7][4] = BuildingImage.WALL_V

        matrix[wallStart+2][5] = BuildingImage.WALL_V
        matrix[wallStart+7][5] = BuildingImage.WALL_V

        matrix[wallStart+2 ][7] = BuildingImage.WALL_V
        matrix[wallStart+7][7] = BuildingImage.WALL_V

        matrix[wallStart+2][8] = BuildingImage.WALL_V
        matrix[wallStart+7][8] = BuildingImage.WALL_V

        matrix[wallStart+2][9] = BuildingImage.WALL_V
        matrix[wallStart+7][9] = BuildingImage.WALL_V

        matrix[wallStart+2][10] = BuildingImage.WALL_CORNER_SW
        matrix[wallStart+3][10] = BuildingImage.WALL_H
        matrix[wallStart+4][10] = BuildingImage.WALL_H
        matrix[wallStart+5][10] = BuildingImage.WALL_H
        matrix[wallStart+6][10] = BuildingImage.WALL_H
        matrix[wallStart+7][10] = BuildingImage.WALL_CORNER_SE
    }


    /** This draws the wall around a city
     *     0123456789
     *  0  ..........
     *  1  ..........
     *  2  ..######..
     *  3  ..#....#..
     *  4  ..#h...#..
     *  5  ..#...h#..
     *  6  ..........
     *  7  ..#...h#..
     *  8  ..#h...#..
     *  9  ..#..h.#.
     *  10 ..######..
     *  11 ..........
     * */
    private fun drawHouses(matrix: Array<Array<BuildingImage?>>, startWidth: Int, amountHouses: Int) {
        println("Drawing $amountHouses houses")
        // 1st parameter is column (width), 2nd parameter is the row (height)
        if (amountHouses >= 5) {
            matrix[startWidth+3][4] = BuildingImage.HOUSE
        }
        if (amountHouses >= 4) {
            matrix[startWidth+6][5] = BuildingImage.HOUSE
        }
        if (amountHouses >= 3) {
            matrix[startWidth+6][7] = BuildingImage.HOUSE
        }
        if (amountHouses >= 2) {
            matrix[startWidth + 3][8] = BuildingImage.HOUSE
        }
        if (amountHouses >= 1) {
            matrix[startWidth + 6][9] = BuildingImage.HOUSE
        }
    }


    /**
     *     0123456789
     *  0  ..........
     *  1  ..........
     *  2  ..######..
     *  3  ..#..M.#..
     *  4  ..#....#..
     *  5  ..#.M..#..
     *  6  ..........
     *  7  ..#.M..#..
     *  8  ..#..M.#..
     *  9  ..#....#.
     *  10 ..######..
     *  11 ..........
     *
     */
    private fun drawMills(matrix: Array<Array<BuildingImage?>>, startWidth: Int, millsToDraw: Int) {
        println("Drawing $millsToDraw mills")
        // 1st parameter is column (width), 2nd parameter is the row (height)
        if (millsToDraw >= 4) {
            matrix[startWidth+5][3] = BuildingImage.MILL
        }
        if (millsToDraw >= 3) {
            matrix[startWidth+4][5] = BuildingImage.MILL
        }
        if (millsToDraw >= 2) {
            matrix[startWidth + 4][7] = BuildingImage.MILL
        }
        if (millsToDraw >= 1) {
            matrix[startWidth + 5][8] = BuildingImage.MILL
        }
    }


    /**
     *     0123456789
     *  0  ..........
     *  1  ..........
     *  2  ..######..
     *  3  ..#m...#..
     *  4  ..#..m.#..
     *  5  ..#....#..
     *  6  ..........
     *  7  ..#....#..
     *  8  ..#....#..
     *  9  ..#m...#.
     *  10 ..######..
     *  11 ..........
     */
    private fun drawMarkets(matrix: Array<Array<BuildingImage?>>, startWidth: Int, marketsToDraw: Int) {
        println("Drawing $marketsToDraw markets")
        // 1st parameter is column (width), 2nd parameter is the row (height)
        if (marketsToDraw >= 3) {
            matrix[startWidth+3][9] = BuildingImage.MARKET
        }
        if (marketsToDraw >= 2) {
            matrix[startWidth+5][4] = BuildingImage.MARKET
        }
        if (marketsToDraw >= 1) {
            matrix[startWidth+3][3] = BuildingImage.MARKET
        }
    }


    /** Draws the Granary at [8:4] s*/
    private fun drawGranaries(matrix: Array<Array<BuildingImage?>>, startWidth: Int, granariesToDraw: Int) {
        println("Drawing $granariesToDraw granaries")
        // 1st parameter is column (width), 2nd parameter is the row (height)
        if (granariesToDraw >= 1) {
            matrix[startWidth+4][8] = BuildingImage.GRANARY
        }
    }

    /** Draws the Warehouse at [4:6] s*/
    private fun drawWarehouses(matrix: Array<Array<BuildingImage?>>, startWidth: Int, warehouses: Int) {
        println("Drawing $warehouses warehouses")
        // 1st parameter is column (width), 2nd parameter is the row (height)
        if (warehouses >= 1) {
            matrix[startWidth+6][4] = BuildingImage.GRANARY
        }
    }

    /** Draws the School at [4:4] s*/
    private fun drawSchools(matrix: Array<Array<BuildingImage?>>, startWidth: Int, schools: Int) {
        println("Drawing $schools schools")
        // 1st parameter is column (width), 2nd parameter is the row (height)
        if (schools >= 1) {
            matrix[startWidth+4][4] = BuildingImage.SCHOOL
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