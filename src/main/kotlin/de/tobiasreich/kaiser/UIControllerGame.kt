package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.chart.BarChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.stage.Modality
import javafx.stage.Stage
import java.net.URL
import java.util.*


class UIControllerGame : Initializable {

    // ------------------------------- Action Buttons (Left panel) -------------------------------
    @FXML
    lateinit var gameFoodButton: ImageView


    // ------------------------------- Statistics -------------------------------
    @FXML
    lateinit var gameSummaryMoneyPossessionLabel: Label

    @FXML
    lateinit var gameSummaryInhabitantsLabel: Label
    @FXML
    lateinit var gameSummaryFoodPossessionLabel: Label

    @FXML
    lateinit var gameSummaryHappinessLabel: Label

    @FXML
    lateinit var gameSummaryLandPossessionLabel: Label


    @FXML
    lateinit var rootBorderPane: BorderPane

    // Chat showing the age and the population amount in this category
    @FXML
    lateinit var populationChart: BarChart<String, Int>

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
        //Game.processPlayer()
        // Update Views
        // updateAllViews()

        // Store player values but do not process. This comes at the beginning of the year

        Game.endTurn()
    }


    /********************************************
     *
     *             View Updates
     *
     *******************************************/

    private fun updatePopulationGraph(){
        populationChart.data.clear()

        val population = Game.currentPlayer.population

        val amountChildren = population.children.size
        val amountAdult = population.adults.size
        val amountOld = population.old.size

        val series = Series<String, Int>()
        series.data.add(XYChart.Data<String, Int>("Children", amountChildren))
        series.data.add(XYChart.Data<String, Int>("Adult", amountAdult))
        series.data.add(XYChart.Data<String, Int>("Old", amountOld))
        populationChart.data.add(series)
    }


    /********************************************
     *
     *             Button Clicks
     *
     *******************************************/


    fun onWheatButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-food.fxml"), Game.stringsBundle)
        val wheatScene = Scene(fxmlLoader.load(), 630.0, 300.0)

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Getreide"
        stage.scene = wheatScene
        stage.show()
    }

    fun onLandButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-land.fxml"), Game.stringsBundle)
        val taxScene = Scene(fxmlLoader.load(), 300.0, 200.0)

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Steuern"
        stage.scene = taxScene
        stage.show()
    }

    fun onTaxButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-tax.fxml"), Game.stringsBundle)
        val taxScene = Scene(fxmlLoader.load(), 300.0, 200.0)

        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Steuern"
        stage.scene = taxScene
        stage.show()
    }


    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        // println("init Game Controller")
        updateStatisticsView()
    }

    /** This updates the statistics (on the right side) of the game view.
     *  Including:
     *  - The population Graph
     *  - The statistics "table"
     *  - ...
     */
    private fun updateStatisticsView(){
        // Update population graph
        updatePopulationGraph()

        // Updating statistics "table"
        gameSummaryMoneyPossessionLabel.text = "1000 Taler"
        gameSummaryFoodPossessionLabel.text = Game.currentPlayer.storedFood.toString()
        gameSummaryInhabitantsLabel.text = Game.currentPlayer.population.getAmountPeople().toString()
        gameSummaryHappinessLabel.text = "75%"
        gameSummaryLandPossessionLabel.text = "${Game.currentPlayer.land.available} ha"
    }

}