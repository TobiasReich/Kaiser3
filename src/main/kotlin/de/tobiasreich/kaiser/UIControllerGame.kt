package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.SubScene
import javafx.scene.chart.BarChart
import javafx.scene.chart.PieChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
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

    // Chat showing the age and the population amount in this category
    @FXML
    lateinit var employmentChart: PieChart

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
     *             Button Clicks
     *
     *******************************************/


    fun onFoodButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-food.fxml"), Game.stringsBundle)
        val foodScene = SubScene(fxmlLoader.load(), 630.0, 300.0)
        rootBorderPane.center = foodScene

//        val stage = Stage()
//        stage.initModality(Modality.APPLICATION_MODAL)
//        stage.title = "Getreide"
//        stage.scene = wheatScene
//
//        // When this dialog is closed, update the views!
//        // For testing use this: adding 1000 to the users money.
//        //TODO: Remove this once testing works fine. This is just so we see the view gets updated again
//        Game.currentPlayer.money += 1000
//        stage.onCloseRequest = EventHandler { updateViews() }
//        stage.show()
    }

    fun onLandButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-land.fxml"), Game.stringsBundle)
        val landScene = SubScene(fxmlLoader.load(), 300.0, 200.0)
        rootBorderPane.center = landScene

//        val stage = Stage()
//        stage.initModality(Modality.APPLICATION_MODAL)
//        stage.title = "Steuern"
//        stage.scene = landScene
//        stage.onCloseRequest = EventHandler { updateViews() }
//        stage.show()
    }

    fun onTaxButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-laws.fxml"), Game.stringsBundle)
        val taxScene = SubScene(fxmlLoader.load(), 800.0, 600.0)
        val controller = fxmlLoader.getController<UIControllerActionLaws>()
        controller.setCallback{
            //Update the view so the user sees the available money
            updateViews()
        }
        rootBorderPane.center = taxScene
    }

    fun onBuildingsButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("dialog-buildings.fxml"), Game.stringsBundle)
        val buildingsScene = SubScene(fxmlLoader.load(), 750.0, 650.0)
        val controller = fxmlLoader.getController<UIControllerActionBuildings>()
        controller.setCallback{
            //Update the view so the user sees the available money
            updateViews()
        }
        rootBorderPane.center = buildingsScene
    }


    /********************************************
     *
     *             View Updates
     *
     *******************************************/

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        updateViews()
        updateLandView()
    }


    /** This updates the statistics (on the right side) of the game view.
     *  Including:
     *  - The population Graph
     *  - The statistics "table"
     *  - ...
     */
    private fun updateViews(){
        // Update population graph
       // updateLandView()
        updatePopulationGraph()
        updateUnemploymentGraph()


        // Updating statistics "table"
        gameSummaryMoneyPossessionLabel.text = "${Game.currentPlayer.money} ${Game.stringsBundle.getString("general_currency")}"
        gameSummaryFoodPossessionLabel.text = "${Game.currentPlayer.storedFood} ${Game.stringsBundle.getString("general_food")}"
        gameSummaryInhabitantsLabel.text = "${Game.currentPlayer.population.getAmountPeople()} ${Game.stringsBundle.getString("general_persons")}"
        gameSummaryHappinessLabel.text = "${Game.currentPlayer.population.mood} %"
        gameSummaryLandPossessionLabel.text = "${Game.currentPlayer.land.landSize} ${Game.stringsBundle.getString("general_hectars")}"
    }


    private fun updateLandView() {
        val playerLandView = UIControllerPlayerLandView(Game.currentPlayer)
        rootBorderPane.center = playerLandView
    }


    /** Renders a pie chart showing employment in 4 categories:
     *  - mill workers
     *  - granary workers
     *  - market workers
     *  - unemployed */
    private fun updateUnemploymentGraph() {
        val millWorkers = Game.currentPlayer.land.buildings.usedMills * Game.WORKERS_PER_BUILDING
        val granaryWorkers = Game.currentPlayer.land.buildings.usedGranaries * Game.WORKERS_PER_BUILDING
        val marketWorkers = Game.currentPlayer.land.buildings.usedMarkets * Game.WORKERS_PER_BUILDING
        val unemployed = Game.currentPlayer.population.adults.size - millWorkers - granaryWorkers - marketWorkers

        println("Millworkers: $millWorkers, Granary: $granaryWorkers, Market: $marketWorkers, Unemployed: $unemployed")

        val pieChartData = FXCollections.observableArrayList(
            PieChart.Data("Mühlen", millWorkers.toDouble()),
            PieChart.Data("Kornspeicher", granaryWorkers.toDouble()),
            PieChart.Data("Märkte", marketWorkers.toDouble()),
            PieChart.Data("Arbeitslos", unemployed.toDouble())
        )

        employmentChart.title = "Arbeiter"
        employmentChart.data = pieChartData
    }

    private fun updatePopulationGraph(){
        populationChart.data.clear()

        val population = Game.currentPlayer.population

        val amountChildren = population.children.size
        val amountAdult = population.adults.size
        val amountOld = population.old.size

        val series = Series<String, Int>()
        series.data.add(XYChart.Data(Game.stringsBundle.getString("game_summary_table_cat_children"), amountChildren))
        series.data.add(XYChart.Data(Game.stringsBundle.getString("game_summary_table_cat_adult"), amountAdult))
        series.data.add(XYChart.Data(Game.stringsBundle.getString("game_summary_table_cat_old"), amountOld))
        populationChart.data.add(series)
    }
}