package de.tobiasreich.kaiser.game.data.country

/** Configuration of buildings existing in a country */
class Buildings {

    companion object{
        const val GRAIN_PER_GRANARY = 10000
    }

    var markets = 0
    var mills = 0
    var granaries = 1
    var palacePieces = 0
    var cathedralPieces = 0
    var storage = 0

    // Other buildings:

    // churches
    // hospitals / infirmary
    // prisons
    // ...
}