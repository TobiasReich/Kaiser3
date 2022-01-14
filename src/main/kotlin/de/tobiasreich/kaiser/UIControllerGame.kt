package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.utils.FXUtils.FxUtils.toRGBCode
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
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import java.net.URL
import java.util.*


class UIControllerGame : Initializable {

    @FXML
    lateinit var gameHeaderBox: HBox

    @FXML
    lateinit var gameHeaderYearLabel: Label

    @FXML
    lateinit var gameHeaderPlayerNameLabel: Label

    // ------------------------------- Action Buttons (Left panel) -------------------------------
    @FXML
    lateinit var gameFoodButton: ImageView


    // ------------------------------- Statistics -------------------------------
    @FXML
    lateinit var gameSummaryMoneyPossessionLabel: Label

    @FXML
    lateinit var gameSummaryInhabitantsLabel: Label

    @FXML
    lateinit var gameSummaryAdultInhabitantsLabel: Label

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
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-food.fxml"), Game.resourcesBundle)
        val foodScene = SubScene(fxmlLoader.load(), 630.0, 300.0)
        rootBorderPane.center = foodScene
    }

    fun onLandButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-land.fxml"), Game.resourcesBundle)
        val landScene = SubScene(fxmlLoader.load(), 300.0, 200.0)
        rootBorderPane.center = landScene
    }

    fun onTaxButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-laws.fxml"), Game.resourcesBundle)
        val taxScene = SubScene(fxmlLoader.load(), 800.0, 600.0)
        val controller = fxmlLoader.getController<UIControllerActionLaws>()
        controller.setCallback{
            //Update the view so the user sees the available money
            updateViews()
        }
        rootBorderPane.center = taxScene
    }

    fun onBuildingsButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-buildings.fxml"), Game.resourcesBundle)
        val buildingsScene = SubScene(fxmlLoader.load(), 750.0, 650.0)
        val controller = fxmlLoader.getController<UIControllerActionBuildings>()
        controller.setCallback{
            //Update the view so the user sees the available money
            updateViews()
        }
        rootBorderPane.center = buildingsScene
    }


    fun onDonateButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-donation.fxml"), Game.resourcesBundle)
        val donationScene = SubScene(fxmlLoader.load(), 750.0, 650.0)
        val controller = fxmlLoader.getController<UIControllerActionDonations>()
        controller.setCallback{
            //Update the view so the user sees the available money
            updateViews()
            showLandView() //The callback is only called when a donation was made. Show the "land" again
        }
        rootBorderPane.center = donationScene
    }

    /** When the player clicks on the sabotage button */
    fun onSabotageButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-sabotage.fxml"), Game.resourcesBundle)
        val donationScene = SubScene(fxmlLoader.load(), 750.0, 650.0)
        val controller = fxmlLoader.getController<UIControllerActionSabotage>()
        controller.setCallback{
            //Update the view so the user sees the available money
            updateViews()
            showLandView() //The callback is only called when a donation was made. Show the "land" again
        }
        rootBorderPane.center = donationScene
    }

    /** Shows the military action when the user clicks on the military-button */
    fun onMilitaryButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-military.fxml"), Game.resourcesBundle)
        val militaryScene = SubScene(fxmlLoader.load(), 1100.0, 800.0)
        val controller = fxmlLoader.getController<UIControllerActionMilitary>()
        controller.setCallback{
            //Update the view so the user sees the available money
            updateViews()
        }
        rootBorderPane.center = militaryScene
    }


    fun onDiplomacyButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-diplomacy.fxml"), Game.resourcesBundle)
        val diplomacyScene = SubScene(fxmlLoader.load(), 1100.0, 800.0)
        val controller = fxmlLoader.getController<UIControllerActionDiplomacy>()
        controller.setCallback{
            //Update the view so the user sees the available money
            showLandView()
            updateViews()
        }
        rootBorderPane.center = diplomacyScene
    }


    /** War view */
    fun onWarButtonClick(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("game-view-war.fxml"), Game.resourcesBundle)
        val warScene = SubScene(fxmlLoader.load(), 1100.0, 800.0)
        val controller = fxmlLoader.getController<UIControllerActionWar>()
        controller.setCallback{
            //Update the view so the user sees the available money
            showLandView()
            updateViews()
        }
        rootBorderPane.center = warScene
    }

    /** Shows the "map" of the own country */
    fun onShowMapButtonClick(actionEvent: ActionEvent) {
        showLandView()
    }


    /********************************************
     *
     *             View Updates
     *
     *******************************************/

    private lateinit var bundle: ResourceBundle

    @FXML
    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!
        updateViews()
        //TODO Consider showing the food action instead. After all this is what the game loop requires. The map is just fancy stuff.
        showLandView()
    }


    /** This updates the statistics (on the right side) of the game view.
     *  Including:
     *  - The population Graph
     *  - The statistics "table"
     *  - ...
     */
    private fun updateViews(){
        // updateLandView()
        updateHeaderView()
        updatePopulationGraph()
        updateUnemploymentGraph()

        // Updating statistics "table"
        gameSummaryMoneyPossessionLabel.text = "${Game.currentPlayer.money} ${Game.resourcesBundle.getString("general_currency")}"
        gameSummaryFoodPossessionLabel.text = "${Game.currentPlayer.storedFood} ${Game.resourcesBundle.getString("general_food")}"
        gameSummaryInhabitantsLabel.text = "${Game.currentPlayer.population.getAmountPeople()} ${Game.resourcesBundle.getString("general_persons")}"
        gameSummaryAdultInhabitantsLabel.text = Game.currentPlayer.population.adults.size.toString()
        gameSummaryHappinessLabel.text = "${Game.currentPlayer.population.mood} %"
        gameSummaryLandPossessionLabel.text = "${Game.currentPlayer.land.landSize} ${Game.resourcesBundle.getString("general_hectars")}"
    }


    private fun showLandView() {
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
            PieChart.Data("${bundle.getString("game_summary_mills")} ($millWorkers)", millWorkers.toDouble()),
            PieChart.Data("${bundle.getString("game_summary_granaries")} ($granaryWorkers)", granaryWorkers.toDouble()),
            PieChart.Data("${bundle.getString("game_summary_markets")} ($marketWorkers)", marketWorkers.toDouble()),
            PieChart.Data("${bundle.getString("game_summary_unemployment")} ($unemployed)", unemployed.toDouble())
        )

        employmentChart.data = pieChartData
    }


    //TODO Show the real name and title
    //TODO Might also more info like a "profile picture", the current rank in the game etc.
    /** Updates the title view on top showing the current player and the current year */
    private fun updateHeaderView() {
        val player = Game.currentPlayer
        gameHeaderPlayerNameLabel.text = String.format(bundle.getString("game_header_player_name_country"), player.getGenderTitle(bundle), player.name, bundle.getString(player.country.nameResource))
        gameHeaderBox.style= ("-fx-background-color: ${Game.currentPlayer.playerColor.toRGBCode()}; ")
        //gameHeaderPlayerNameLabel.style = ("-fx-text-fill: ${Game.currentPlayer.playerColor.toRGBCode()}; ")
        gameHeaderYearLabel.text = Game.currentYear.toString()
    }


    private fun updatePopulationGraph(){
        populationChart.data.clear()

        val population = Game.currentPlayer.population

        val amountChildren = population.children.size
        val amountAdult = population.adults.size
        val amountOld = population.old.size

        val series = Series<String, Int>()
        series.data.add(XYChart.Data(bundle.getString("game_summary_population_category_children"), amountChildren))
        series.data.add(XYChart.Data(bundle.getString("game_summary_population_category_adult"), amountAdult))
        series.data.add(XYChart.Data(bundle.getString("game_summary_population_category_old"), amountOld))
        populationChart.data.add(series)
    }

    fun onEmploymentChartClick(mouseEvent: MouseEvent) {
        ViewController.showInfoPopUp(employmentChart, bundle.getString("game_summary_worker_distribution_title"),bundle.getString("game_summary_info"))
    }

    fun onPopulationChartClicked(mouseEvent: MouseEvent) {
        ViewController.showInfoPopUp(populationChart, bundle.getString("game_summary_population_title"),bundle.getString("game_summary_population_info"))
    }



}