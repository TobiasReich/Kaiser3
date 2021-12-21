package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
import de.tobiasreich.kaiser.game.data.country.BuildingType
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import java.net.URL
import java.util.*

/** Controller, specific for the Buildings actions */
class UIControllerActionBuildings : Initializable {

    @FXML
    private lateinit var rootBorderPane: BorderPane

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

    private lateinit var bundle: ResourceBundle

    /** Notifies the view about a purchase so the statistics can be updated */
    private lateinit var callback : () -> Unit

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
        println("update Views")
        val buildings = Game.currentPlayer.land.buildings

        marketsBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.markets)
        millsBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.mills)
        granariesBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.granaries)
        warehousesBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.warehouses)
        schoolsBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.schools)
        palacesBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.palacePieces)
        cathedralsBuildLabel.text = String.format(bundle.getString("buildings_amount_built"), buildings.cathedralPieces)
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
        Game.currentPlayer.buyBuilding(building)
        updateViews()
        callback.invoke()
    }

}
