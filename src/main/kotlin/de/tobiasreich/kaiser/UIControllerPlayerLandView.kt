package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.Player
import de.tobiasreich.kaiser.game.data.country.BuildingType
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.image.Image
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
        const val FIELD_SIZE = 35.0
        const val LAND_WIDTH_FIELDS  = 25
        const val LAND_HEIGHT_FIELDS = 20
    }

    @FXML
    lateinit var drawCanvas: Canvas

    private var imageBarn: Image
    private var imageMarket: Image

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

        drawCanvas.width = LAND_WIDTH_FIELDS * FIELD_SIZE
        drawCanvas.height = LAND_HEIGHT_FIELDS * FIELD_SIZE

        // Can be scaled via
        //drawCanvas.scaleX = value
        //drawCanvas.scaleY = value

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
                    BuildingType.MARKET -> { drawCanvas.graphicsContext2D.drawImage(imageMarket, indexColumn * FIELD_SIZE, indexRow * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE) }
                    BuildingType.BARN -> { drawCanvas.graphicsContext2D.drawImage(imageBarn, indexColumn * FIELD_SIZE, indexRow * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE) }
                }
            }
        }

    }

}