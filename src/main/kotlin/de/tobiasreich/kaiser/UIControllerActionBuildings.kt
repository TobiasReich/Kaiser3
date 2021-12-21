package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.country.BuildingType
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.stage.Stage
import java.net.URL
import java.util.*

/** Controller, specific for the Buildings actions */
class UIControllerActionBuildings : Initializable {

    // Amount of build buildings
    @FXML
    private lateinit var marketsBuildLabel: Label
    @FXML
    private lateinit var millsBuildLabel: Label
    @FXML
    private lateinit var granariesBuildLabel: Label
    @FXML
    private lateinit var warehousesBuildLabel: Label
    @FXML
    private lateinit var schoolsBuildLabel: Label
    @FXML
    private lateinit var palacesBuildLabel: Label
    @FXML
    private lateinit var cathedralsBuildLabel: Label

    // Prices
    @FXML
    private lateinit var millsPriceLabel: Label
    @FXML
    private lateinit var marketsPriceLabel: Label
    @FXML
    private lateinit var granariesPriceLabel: Label
    @FXML
    private lateinit var warehousesPriceLabel: Label
    @FXML
    private lateinit var schoolsPriceLabel: Label
    @FXML
    private lateinit var palacePriceLabel: Label
    @FXML
    private lateinit var cathedralPriceLabel: Label

    // Space Available
    @FXML
    private lateinit var marketsSpaceAvailableLabel: Label
    @FXML
    private lateinit var millsSpaceAvailableLabel: Label
    @FXML
    private lateinit var granariesSpaceAvailableLabel: Label
    @FXML
    private lateinit var warehousesSpaceAvailableLabel: Label
    @FXML
    private lateinit var schoolsSpaceAvailableLabel: Label
    @FXML
    private lateinit var palaceSpaceAvailableLabel: Label
    @FXML
    private lateinit var cathedralSpaceAvailableLabel: Label

    // Buy Buttons
    @FXML
    private lateinit var buyMarketButton: Button
    @FXML
    private lateinit var buyMillButton: Button
    @FXML
    private lateinit var buyGranaryButton: Button
    @FXML
    private lateinit var buyWarehouseButton: Button
    @FXML
    private lateinit var buySchoolButton: Button
    @FXML
    private lateinit var buyPalaceButton: Button
    @FXML
    private lateinit var buyCathedralButton: Button

    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var callback : () -> Unit

    private lateinit var bundle: ResourceBundle

    private val player = Game.currentPlayer
    private val land = player.land
    private val buildings = land.buildings

    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!
        updateViews()
    }


    /** Sets the callback for the view to update on purchases
     *  NOTE: Since this is just a notification I made the easy way of not creating
     *  an interface but just store the lambda instead. */
    fun setCallback(callback: () -> Unit){
        this.callback = callback
    }


    /** This updates the views and sets the buy buttons enabled/disabled */
    private fun updateViews(){
        // Amount of built buildings
        marketsBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.markets)
        millsBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.mills)
        granariesBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.granaries)
        warehousesBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.warehouses)
        schoolsBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.schools)
        palacesBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.palacePieces)
        cathedralsBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.cathedralPieces)

        // Prices
        val currencyString = bundle.getString("general_currency")
        marketsPriceLabel.text = "${player.getPriceForBuilding(BuildingType.MARKET)} $currencyString"
        millsPriceLabel.text = "${player.getPriceForBuilding(BuildingType.MILL)} $currencyString"
        granariesPriceLabel.text = "${player.getPriceForBuilding(BuildingType.GRANARY)} $currencyString"
        warehousesPriceLabel.text = "${player.getPriceForBuilding(BuildingType.WAREHOUSE)} $currencyString"
        schoolsPriceLabel.text = "${player.getPriceForBuilding(BuildingType.SCHOOL)} $currencyString"
        palacePriceLabel.text = "${player.getPriceForBuilding(BuildingType.PALACE)} $currencyString"
        cathedralPriceLabel.text = "${player.getPriceForBuilding(BuildingType.CATHEDRAL)} $currencyString"

        // Available Space
        marketsSpaceAvailableLabel.text = land.getAvailableSpaceForBuilding(BuildingType.MARKET).toString()
        millsSpaceAvailableLabel.text = land.getAvailableSpaceForBuilding(BuildingType.MILL).toString()
        granariesSpaceAvailableLabel.text = land.getAvailableSpaceForBuilding(BuildingType.GRANARY).toString()
        warehousesSpaceAvailableLabel.text = land.getAvailableSpaceForBuilding(BuildingType.WAREHOUSE).toString()
        schoolsSpaceAvailableLabel.text = land.getAvailableSpaceForBuilding(BuildingType.SCHOOL).toString()
        palaceSpaceAvailableLabel.text = land.getAvailableSpaceForBuilding(BuildingType.PALACE).toString()
        cathedralSpaceAvailableLabel.text = land.getAvailableSpaceForBuilding(BuildingType.CATHEDRAL).toString()

        setButtonStates()
    }

    fun onBuyMarket(actionEvent: ActionEvent) {
        buyBuilding(BuildingType.MARKET)
    }

    fun onBuyMill(actionEvent: ActionEvent) {
        buyBuilding(BuildingType.MILL)
    }

    fun onBuyGranary(actionEvent: ActionEvent) {
        buyBuilding(BuildingType.GRANARY)
    }

    fun onBuyWarehouse(actionEvent: ActionEvent) {
        buyBuilding(BuildingType.WAREHOUSE)
    }

    fun onBuySchool(actionEvent: ActionEvent) {
        buyBuilding(BuildingType.SCHOOL)
    }

    fun onBuyPalace(actionEvent: ActionEvent) {
        buyBuilding(BuildingType.PALACE)
    }

    fun onBuyCathedral(actionEvent: ActionEvent) {
        buyBuilding(BuildingType.CATHEDRAL)
    }

    private fun buyBuilding(building : BuildingType){
        if (Game.currentPlayer.land.getAvailableSpaceForBuilding(building) > 0) {
            Game.currentPlayer.buyBuilding(building)
            callback.invoke() // Update "outside" view for updating the money there, too
        }
        updateViews()
    }

    /** This updates the state of the purchase buttons. If the land is already filled with the maximum amount
     *  of buildings of a type (e.g. available space == 0) the button will be disabled. */
    private fun setButtonStates(){
        buyMarketButton.isDisable = land.getAvailableSpaceForBuilding(BuildingType.MARKET) == 0
        buyMillButton.isDisable = land.getAvailableSpaceForBuilding(BuildingType.MILL) == 0
        buyGranaryButton.isDisable = land.getAvailableSpaceForBuilding(BuildingType.GRANARY) == 0
        buyWarehouseButton.isDisable = land.getAvailableSpaceForBuilding(BuildingType.WAREHOUSE) == 0
        buySchoolButton.isDisable = land.getAvailableSpaceForBuilding(BuildingType.SCHOOL) == 0
        buyPalaceButton.isDisable = land.getAvailableSpaceForBuilding(BuildingType.PALACE) == 0
        buyCathedralButton.isDisable = land.getAvailableSpaceForBuilding(BuildingType.CATHEDRAL) == 0
    }

    /** When the user clicks the "done" button. Close this window */
    fun onDoneButtonPressed(actionEvent: ActionEvent) {
//        val source: Node = actionEvent.source as Node
//        val stage: Stage = source.scene.window as Stage
//        stage.close()
    }
}
