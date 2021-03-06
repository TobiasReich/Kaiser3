package de.tobiasreich.kaiser.game.utils

import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.stage.Modality
import javafx.stage.StageStyle
import java.io.PrintWriter
import java.io.StringWriter


object FxDialogs {



    fun showWarning(title: String?, message: String?) {
        val alert = Alert(Alert.AlertType.WARNING)
        alert.initStyle(StageStyle.UTILITY)
        alert.title = "Warning"
        alert.headerText = title
        alert.contentText = message
        alert.showAndWait()
    }

    fun showError(title: String?, message: String?) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.initStyle(StageStyle.UTILITY)
        alert.title = "Error"
        alert.headerText = title
        alert.contentText = message
        alert.showAndWait()
    }

    fun showException(title: String?, message: String?, exception: Exception) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.initStyle(StageStyle.UTILITY)
        alert.title = "Exception"
        alert.headerText = title
        alert.contentText = message
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        exception.printStackTrace(pw)
        val exceptionText = sw.toString()
        val label = Label("Details:")
        val textArea = TextArea(exceptionText)
        textArea.isEditable = false
        textArea.isWrapText = true
        textArea.maxWidth = Double.MAX_VALUE
        textArea.maxHeight = Double.MAX_VALUE
        GridPane.setVgrow(textArea, Priority.ALWAYS)
        GridPane.setHgrow(textArea, Priority.ALWAYS)
        val expContent = GridPane()
        expContent.maxWidth = Double.MAX_VALUE
        expContent.add(label, 0, 0)
        expContent.add(textArea, 0, 1)
        alert.dialogPane.expandableContent = expContent
        alert.showAndWait()
    }


    /** Shows an information Dialog.
     *  The user has only one button. This can only be accepted.
     *  No return value is specified (unlike the confirm, there is no YES/NO option) s*/
    fun showInformation(title: String?, message: String?) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.initModality(Modality.APPLICATION_MODAL)
        //alert.initStyle(StageStyle.UTILITY)
        alert.title = ""
        alert.headerText = title
        alert.contentText = message
        alert.showAndWait()
    }

    /** Shows a dialog that prints a title and a context String with a question mark icon (CONFIRMATION).
     *  Allowing the user to either ACCEPT or  REJECT the dialog question.
     *  NOTE: This has always 2 choices. Use another method if you only want to present something without choice! */
    fun showConfirm(title: String, message: String): Boolean {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.initModality(Modality.APPLICATION_MODAL)
        alert.title = ""
        alert.headerText = title
        alert.contentText = message

        alert.buttonTypes.clear()
        alert.buttonTypes.addAll(ButtonType.YES, ButtonType.NO)
        //If wanted, deactivate default behavior for a Button like this
        //val yesButton = alert.dialogPane.lookupButton(ButtonType.YES) as Button
        //yesButton.isDefaultButton = false
        // Change the text of the buttons like this:
        //val okButton = alert.dialogPane.lookupButton(ButtonType.OK) as Button
        //okButton.text = "Accept."

        val result = alert.showAndWait()
        return result.isPresent && result.get() == ButtonType.YES
    }

    fun showTextInput(title: String?, message: String?, defaultValue: String?): String? {
        val dialog = TextInputDialog(defaultValue)
        dialog.initStyle(StageStyle.UTILITY)
        dialog.title = "Input"
        dialog.headerText = title
        dialog.contentText = message
        val result = dialog.showAndWait()
        return if (result.isPresent) {
            result.get()
        } else {
            null
        }
    }
}
