package de.tobiasreich.kaiser.game.utils

import javafx.scene.paint.Color

class FXUtils {

    object FxUtils {

        /** Converts a Color to the Hex values needed for setting as CSS style
         * (e.g. when changing background colors programmatically) */
        fun Color.toRGBCode(): String {
            return String.format("#%02X%02X%02X",
                (red * 255).toInt(),
                (green * 255).toInt(),
                (blue * 255).toInt()
            )
        }
    }
}