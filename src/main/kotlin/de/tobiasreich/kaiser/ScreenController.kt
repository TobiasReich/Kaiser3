package de.tobiasreich.kaiser

import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.Pane

object ScreenController {

    @Suppress("ClassName")
    enum class SCREEN_NAME {
        START_SCREEN,
        GAME,
        NEXT_PLAYER
    }

    var main: Scene? = null

    private val screenMap = HashMap<SCREEN_NAME, Pane>()

    fun addScreen(name: SCREEN_NAME, pane: Pane) {
        screenMap[name] = pane
    }

    fun removeScreen(name: SCREEN_NAME) {
        screenMap.remove(name)
    }

    /** This shows a certain screen*/
    fun activate(name: SCREEN_NAME) {
        val screen = screenMap[name]
        main!!.root = screen
    }

    /** This shows a certain screen (used for the news) */
    fun showView(view: Pane) {
        main!!.root = view
    }
}