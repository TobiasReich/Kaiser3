package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.data.country.BuildingType
import javafx.event.EventHandler
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
        const val LAND_WIDTH_FIELDS  = 25
        const val LAND_HEIGHT_FIELDS = 20

        const val MIN_ZOOM_LEVEL = 20.0
        const val MAX_ZOOM_LEVEL = 50.0
    }

    @FXML
    lateinit var drawCanvas: Canvas

    private var imageBarn: Image
    private var imageMarket: Image

    private var fieldSize : Double

    init {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("view-player-land.fxml"), Game.stringsBundle)
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        try {
            fxmlLoader.load()
        } catch (exception : IOException) {
            throw RuntimeException(exception)
        }

        // loading the images for later drawing
        imageBarn = Image(javaClass.getResource("img/icon_barn.png")!!.toExternalForm())
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

        fieldSize = Game.currentPlayer.land.zoomLevelImageSize

        drawCanvas.width = LAND_WIDTH_FIELDS * fieldSize
        drawCanvas.height = LAND_HEIGHT_FIELDS * fieldSize

        updateView()
    }

    private fun zoomIn() {
        fieldSize = (Game.currentPlayer.land.zoomLevelImageSize++).coerceAtMost(MAX_ZOOM_LEVEL)
        drawCanvas.width = LAND_WIDTH_FIELDS * fieldSize
        drawCanvas.height = LAND_HEIGHT_FIELDS * fieldSize
        updateView()
    }

    private fun zoomOut() {
        fieldSize = (Game.currentPlayer.land.zoomLevelImageSize--).coerceAtLeast(MIN_ZOOM_LEVEL)
        drawCanvas.width = LAND_WIDTH_FIELDS * fieldSize
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
                    BuildingType.FREE -> { /* nothing to do for now */}
                    BuildingType.MARKET -> { drawCanvas.graphicsContext2D.drawImage(imageMarket, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                    BuildingType.BARN -> { drawCanvas.graphicsContext2D.drawImage(imageBarn, indexColumn * fieldSize, indexRow * fieldSize, fieldSize, fieldSize) }
                }
            }
        }

    }

}