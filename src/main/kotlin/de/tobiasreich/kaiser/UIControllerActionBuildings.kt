package de.tobiasreich.kaiser

import de.tobiasreich.kaiser.game.Game
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

    override fun initialize(p0: URL?, bundle: ResourceBundle?) {
        this.bundle = bundle!!
        updateViews()
    }



    /** This updates the views and sets the buy buttons enabled/disabled */
    fun updateViews(){
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
        println("Buy market")
    }

    fun onBuyMill(actionEvent: ActionEvent) {
        println("Buy Mill")
    }

    fun onBuyGranary(actionEvent: ActionEvent) {
        println("Buy Granary")
    }

    fun onBuyWarehouseStorage(actionEvent: ActionEvent) {
        println("Buy Storage")
    }

    fun onBuySchool(actionEvent: ActionEvent) {
        println("Buy School")
    }

    fun onBuyPalace(actionEvent: ActionEvent) {
        println("Buy Palace")
    }

    fun onBuyCathedral(actionEvent: ActionEvent) {
        println("Buy Cathedral")
    }

}