package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.data.country.BuildingImage
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.input.ScrollEvent
import javafx.scene.paint.Color
import java.io.IOException


/** Visualization of the players land.
 *  TODO: Decide what to show.
 *  - Size of the land (basically the more land is bought the more ... to be shown?
 *  - Buildings
 *  - Troops
 *  - Population?
 */
class UIControllerPlayerLandView(private val player : Player) : ScrollPane() {

    companion object{

        // The height is fixed. When the land grows, it grows in width
        const val LAND_HEIGHT_FIELDS = 20
        const val LAND_FIELD_WIDTH = 200 // How many ha is one field in the graphical representation

        const val MIN_ZOOM_LEVEL = 20.0
        const val MAX_ZOOM_LEVEL = 50.0
    }

    @FXML
    lateinit var drawCanvas: Canvas

    private var imageMill: Image
    private var imageGranary: Image
    private var imageMarket: Image
    private var imageWarehouse: Image
    private var imageSchool: Image
    private var imageHouse: Image
    private var imageTree: Image
    private var imageTree2: Image
    private var imageBushes: Image
    private var imageRoad: Image

    private var imageWallH: Image
    private var imageWallV: Image
    private var imageWallNE: Image
    private var imageWallNW: Image
    private var imageWallSE: Image
    private var imageWallSW: Image

    private var fieldSize : Double
    private var tilesWidth  : Int

    init {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("view-player-land.fxml"), Game.resourcesBundle)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception : IOException) {
            throw RuntimeException(exception)
        }

        // loading the images for later drawing
        imageMill = Image(javaClass.getResource("img/icon_windmill.png")!!.toExternalForm())
        imageGranary = Image(javaClass.getResource("img/icon_granary.png")!!.toExternalForm())
        imageMarket = Image(javaClass.getResource("img/icon_market.png")!!.toExternalForm())
        imageWarehouse = Image(javaClass.getResource("img/icon_warehouse.png")!!.toExternalForm())
        imageSchool = Image(javaClass.getResource("img/icon_school.png")!!.toExternalForm())
        imageHouse = Image(javaClass.getResource("img/icon_houses.png")!!.toExternalForm())
        imageTree = Image(javaClass.getResource("img/icon_tree.png")!!.toExternalForm())
        imageTree2 = Image(javaClass.getResource("img/icon_tree2.png")!!.toExternalForm())
        imageBushes = Image(javaClass.getResource("img/icon_bushes.png")!!.toExternalForm())
        imageRoad = Image(javaClass.getResource("img/icon_road.png")!!.toExternalForm())

        imageWallH = Image(javaClass.getResource("img/icon_wall_horizontal.png")!!.toExternalForm())
        imageWallV = Image(javaClass.getResource("img/icon_wall_vertical.png")!!.toExternalForm())
        imageWallNE = Image(javaClass.getResource("img/icon_wall_corner_NE.png")!!.toExternalForm())
        imageWallNW = Image(javaClass.getResource("img/icon_wall_corner_NW.png")!!.toExternalForm())
        imageWallSE = Image(javaClass.getResource("img/icon_wall_corner_SE.png")!!.toExternalForm())
        imageWallSW = Image(javaClass.getResource("img/icon_wall_corner_SW.png")!!.toExternalForm())

        addEventFilter(ScrollEvent.ANY) { event ->
            if (event.deltaY > 0) {
                zoomIn()
                //println("Zoom in (${event.deltaY})")
            } else if (event.deltaY < 0) {
                zoomOut()
                //println("Zoom out (${event.deltaY})")
            }
            event.consume()
        }

        val land = Game.currentPlayer.land
        fieldSize = land.zoomLevelImageSize

        // The land
        tilesWidth  = land.landSize / LAND_FIELD_WIDTH

        drawCanvas.width = tilesWidth * fieldSize
        drawCanvas.height = LAND_HEIGHT_FIELDS * fieldSize

        updateView()
    }

    private fun zoomIn() {
        fieldSize = (Game.currentPlayer.land.zoomLevelImageSize++).coerceAtMost(MAX_ZOOM_LEVEL)
        drawCanvas.width = tilesWidth * fieldSize
        drawCanvas.height = LAND_HEIGHT_FIELDS * fieldSize
        updateView()
    }

    private fun zoomOut() {
        fieldSize = (Game.currentPlayer.land.zoomLevelImageSize--).coerceAtLeast(MIN_ZOOM_LEVEL)
        drawCanvas.width = tilesWidth * fieldSize
        drawCanvas.height = LAND_HEIGHT_FIELDS * fieldSize
        updateView()
    }


    private fun updateView(){
        drawCanvas.graphicsContext2D.fill = Color.GREEN
        drawCanvas.graphicsContext2D.fillRect(0.0, 0.0, drawCanvas.width, drawCanvas.height)

        val matrix = Game.currentPlayer.land.getLandViewMatrix(Game.currentPlayer.population)
        matrix.forEachIndexed { indexColumn, column ->
            column.forEachIndexed { indexRow, fieldType ->
                when(fieldType){
                    BuildingImage.MILL -> { drawCanvas.graphicsContext2D.drawImage(imageMill, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.MARKET -> { drawCanvas.graphicsContext2D.drawImage(imageMarket, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.GRANARY -> { drawCanvas.graphicsContext2D.drawImage(imageGranary, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WAREHOUSE -> { drawCanvas.graphicsContext2D.drawImage(imageWarehouse, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.SCHOOL -> { drawCanvas.graphicsContext2D.drawImage(imageSchool, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.TREE -> { drawCanvas.graphicsContext2D.drawImage(imageTree, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.TREE2 -> { drawCanvas.graphicsContext2D.drawImage(imageTree2, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.HOUSE -> { drawCanvas.graphicsContext2D.drawImage(imageHouse, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.BUSHES -> { drawCanvas.graphicsContext2D.drawImage(imageBushes, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.ROAD -> { drawCanvas.graphicsContext2D.drawImage(imageRoad, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_H -> { drawCanvas.graphicsContext2D.drawImage(imageWallH, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_V -> { drawCanvas.graphicsContext2D.drawImage(imageWallV, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_CORNER_NE -> { drawCanvas.graphicsContext2D.drawImage(imageWallNE, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_CORNER_NW -> { drawCanvas.graphicsContext2D.drawImage(imageWallNW, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_CORNER_SE -> { drawCanvas.graphicsContext2D.drawImage(imageWallSE, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_CORNER_SW -> { drawCanvas.graphicsContext2D.drawImage(imageWallSW, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    //TODO Add the other buildings
                    else -> { /* nothing to do for now for null values but we might draw them with a texture, too*/}
                }
            }
        }
    }

}