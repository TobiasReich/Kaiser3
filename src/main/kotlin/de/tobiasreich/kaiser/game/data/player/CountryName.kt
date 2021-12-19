package de.tobiasreich.kaiser.game.data.player

/** This is the area of Germany in around 1400 (not really but for the game it is good enough):
 *
 *                      Holstein
 *          Westfahlen  Sachsen  Preußen
 *          Hessen      Thüringen  Böhmen
 *                  Baden      Franken
 *                      Bayern
 */
enum class CountryName(val nameResource: String) {

    HOLSTEIN("game_country_holstein"),
    WESTPHALIA("game_country_westphalia"), SAXONY("game_country_saxony"), PRUSSIA("game_country_prussia"),
    HESSE("game_country_hesse"), THURINGIA("game_country_thuringia"), BOHEMIA("game_country_bohemia"),
    BADEN("game_country_baden"), FRANCONIA("game_country_franconia"),
    BAVARIA("game_country_bavaria")
}
