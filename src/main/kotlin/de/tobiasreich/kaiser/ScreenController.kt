package de.tobiasreich.kaiser

import javafx.scene.Scene
import javafx.scene.layout.Pane


object ScreenController {

    enum class SCREENS {
        START_SCREEN,
        GAME,
        NEXT_PLAYER
    }

    var main: Scene? = null

    private val screenMap = HashMap<SCREENS, Pane>()

    fun addScreen(name: SCREENS, pane: Pane) {
        screenMap[name] = pane
    }

    fun removeScreen(name: SCREENS) {
        screenMap.remove(name)
    }

    fun activate(name: SCREENS) {
        main!!.root = screenMap[name]
    }
}