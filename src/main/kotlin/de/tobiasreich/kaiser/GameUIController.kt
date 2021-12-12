package de.tobiasreich.kaiser

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.chart.BarChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage


class GameUIController {

    @FXML
    private val wheatButton: Button? = null

    @FXML
    private lateinit var rootBorderPane: BorderPane

    // Chat showing the age and the population amount in this category
    @FXML
    private lateinit var populationChart: BarChart<String, Float>

    /********************************************
     *
     *             Game Menu items
     *
     *******************************************/

    @FXML
    private fun onMenuNewGameClick() {
    }

    @FXML
    private fun onMenuLoadGameClick() {
    }

    @FXML
    private fun onMenuSaveGameClick() {
    }


    /********************************************
     *
     *             Settings Menu items
     *
     *******************************************/


    @FXML
    private fun onMenuPreferencesClick() {
    }


    /********************************************
     *
     *             Help Menu items
     *
     *******************************************/


    @FXML
    private fun onMenuHelpClick() {
    }


    @FXML
    private fun onMenuAboutClick() {
    }

    /********************************************
     *
     *             Game Buttons Updates
     *
     *******************************************/

    @FXML
    private fun onEndTurnClick() {
        // Game.End Turn
        // Update Views
        updateAllViews()
    }


    /********************************************
     *
     *             View Updates
     *
     *******************************************/

    private fun updateAllViews(){
        populationChart.data.clear()

        val amountChildren = (Math.random() * 20).toFloat()
        val amountAdult = (Math.random() * 80).toFloat()
        val amountOld = 100f - amountChildren - amountAdult

        val series = Series<String?, Float?>()
        series.data.add(XYChart.Data<String?, Float?>("Children", amountChildren))
        series.data.add(XYChart.Data<String?, Float?>("Adult", amountAdult))
        series.data.add(XYChart.Data<String?, Float?>("Old", amountOld))
        populationChart.data.add(series)
    }

    fun onWheatButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-wheat.fxml"))
        val taxScene = Scene(fxmlLoader.load(), 300.0, 200.0)

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Steuern"
        stage.scene = taxScene
        stage.show()
    }

    fun onLandButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-land.fxml"))
        val taxScene = Scene(fxmlLoader.load(), 300.0, 200.0)

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Steuern"
        stage.scene = taxScene
        stage.show()
    }

    fun onTaxButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-tax.fxml"))
        val taxScene = Scene(fxmlLoader.load(), 300.0, 200.0)

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Steuern"
        stage.scene = taxScene
        stage.show()
    }

}