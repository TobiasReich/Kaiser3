package de.tobiasreich.kaiser

import javafx.scene.Scene
import javafx.scene.layout.Pane


object ScreenController {

        var main: Scene? = null

        private val screenMap = HashMap<String, Pane>()

        fun addScreen(name: String, pane: Pane) {
            screenMap[name] = pane
        }

        fun removeScreen(name: String) {
            screenMap.remove(name)
        }

        fun activate(name: String) {
            main!!.root = screenMap[name]
        }
}