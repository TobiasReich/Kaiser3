package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.data.country.BuildingImage
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
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

        println("Road: $GameImageCache")

        val matrix = Game.currentPlayer.land.getLandViewMatrix(Game.currentPlayer.population)
        matrix.forEachIndexed { indexColumn, column ->
            column.forEachIndexed { indexRow, fieldType ->
                when(fieldType){
                    BuildingImage.MILL -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageMill, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.MARKET -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageMarket, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.GRANARY -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageGranary, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WAREHOUSE -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageWarehouse, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.SCHOOL -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageSchool, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.TREE -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageTree, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.TREE2 -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageTree2, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.HOUSE -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageHouse, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.BUSHES -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageBushes, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.ROAD -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageRoad, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_H -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageWallH, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_V -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageWallV, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_CORNER_NE -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageWallNE, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_CORNER_NW -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageWallNW, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_CORNER_SE -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageWallSE, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingImage.WALL_CORNER_SW -> { drawCanvas.graphicsContext2D.drawImage(GameImageCache.imageWallSW, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    //TODO Add the other buildings
                    else -> { /* nothing to do for now for null values but we might draw them with a texture, too*/}
                }
            }
        }
    }

}