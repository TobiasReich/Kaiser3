package de.tobiasreich.kaiser.config

interface IPlayerConfigChange {

    /** Called when ever the configuration of players changed.
     *  We then can check whether a valid configuration is given
     *  (e.g. at least 1 human player...) */
    fun onUpdateActiveState()

}