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
class UIControllerPlayerLandView(private val player : Player) : Canvas() {

    companion object{
        const val FIELD_SIZE = 50.0
        const val LAND_WIDTH_FIELDS = 15  //15*50 = 750
        const val LAND_HEIGHT_FIELDS = 10 //10*50 = 500
    }

    @FXML
    lateinit var playerConfigCountryLabel: Label

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

        updateView()
    }


    private fun updateView(){
        graphicsContext2D.fill = Color.GREEN
        graphicsContext2D.fillRect(0.0, 0.0, this.width, this.height)

        val matrix = Game.currentPlayer.land.getLandViewMatrix()
        matrix.forEachIndexed { indexColumn, column ->
            column.forEachIndexed { indexRow, fieldType ->
                when(fieldType){
                    BuildingType.FREE -> { /* nothing to do for now */}
                    BuildingType.MARKET -> { graphicsContext2D.drawImage(imageMarket, indexColumn * FIELD_SIZE, indexRow * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE) }
                    BuildingType.BARN -> { graphicsContext2D.drawImage(imageBarn, indexColumn * FIELD_SIZE, indexRow * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE) }
                }
            }
        }



    }

}