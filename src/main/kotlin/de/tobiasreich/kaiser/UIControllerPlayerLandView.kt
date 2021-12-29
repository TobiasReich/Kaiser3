package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.data.country.BuildingType
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
    private var imageBarn: Image
    private var imageMarket: Image

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
        imageBarn = Image(javaClass.getResource("img/icon_granary.png")!!.toExternalForm())
        imageMarket = Image(javaClass.getResource("img/icon_market.png")!!.toExternalForm())

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

        val matrix = Game.currentPlayer.land.getLandViewMatrix()
        matrix.forEachIndexed { indexColumn, column ->
            column.forEachIndexed { indexRow, fieldType ->
                when(fieldType){
                    BuildingType.MILL -> { drawCanvas.graphicsContext2D.drawImage(imageMill, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingType.MARKET -> { drawCanvas.graphicsContext2D.drawImage(imageMarket, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingType.GRANARY -> { drawCanvas.graphicsContext2D.drawImage(imageBarn, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    //TODO Add the other buildings
                    else -> { /* nothing to do for now for null values but we might draw them with a texture, too*/}
                }
            }
        }

    }

}