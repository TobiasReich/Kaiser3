package de.tobiasreich.kaiser

import javafx.scene.image.Image

/** Container class for static images that are required in multiple occasions
 *
 */
object GameImageCache {

    // loading the images for later drawing
    val imageMill: Image = Image(javaClass.getResource("img/icon_windmill.png")!!.toExternalForm())
    val imageGranary: Image = Image(javaClass.getResource("img/icon_granary.png")!!.toExternalForm())
    val imageMarket: Image = Image(javaClass.getResource("img/icon_market.png")!!.toExternalForm())
    val imageWarehouse: Image = Image(javaClass.getResource("img/icon_warehouse.png")!!.toExternalForm())
    val imageSchool: Image = Image(javaClass.getResource("img/icon_school.png")!!.toExternalForm())
    val imageHouse: Image = Image(javaClass.getResource("img/icon_houses.png")!!.toExternalForm())
    val imageTree: Image = Image(javaClass.getResource("img/icon_tree.png")!!.toExternalForm())
    val imageTree2: Image = Image(javaClass.getResource("img/icon_tree2.png")!!.toExternalForm())
    val imageBushes: Image = Image(javaClass.getResource("img/icon_bushes.png")!!.toExternalForm())
    val imageRoad: Image = Image(javaClass.getResource("img/icon_road.png")!!.toExternalForm())

    val imageWallH: Image = Image(javaClass.getResource("img/icon_wall_horizontal.png")!!.toExternalForm())
    val imageWallV: Image = Image(javaClass.getResource("img/icon_wall_vertical.png")!!.toExternalForm())
    val imageWallNE: Image = Image(javaClass.getResource("img/icon_wall_corner_NE.png")!!.toExternalForm())
    val imageWallNW: Image = Image(javaClass.getResource("img/icon_wall_corner_NW.png")!!.toExternalForm())
    val imageWallSE: Image = Image(javaClass.getResource("img/icon_wall_corner_SE.png")!!.toExternalForm())
    val imageWallSW: Image = Image(javaClass.getResource("img/icon_wall_corner_SW.png")!!.toExternalForm())


    val warrior: Image = Image(javaClass.getResource("img/icon_unit_warrior.png")!!.toExternalForm())
    val archer: Image = Image(javaClass.getResource("img/icon_unit_archer.png")!!.toExternalForm())
    val spearman: Image = Image(javaClass.getResource("img/icon_unit_spearman.png")!!.toExternalForm())
    val cannon: Image = Image(javaClass.getResource("img/icon_unit_cannon.png")!!.toExternalForm())
    val lancer: Image = Image(javaClass.getResource("img/icon_unit_lancer.png")!!.toExternalForm())
    val cavalry: Image = Image(javaClass.getResource("img/icon_unit_cavalry.png")!!.toExternalForm())

}