package de.tobiasreich.kaiser.game.data.player

/** All possible titles a player can obtain
 *  Is this the order?
 *  https://de.wikipedia.org/wiki/Adelstitel
 */
enum class Title(val resourceNameMale : String, val resourceNameFemale : String) {
    MISTER("player_title_mister_male","player_title_mister_female"),       // Herr / Frau
    BARON("player_title_baron_male","player_title_baron_female"),          // Freiherr
    COUNT("player_title_count_male","player_title_count_female"),          // Graf
    FIRST("player_title_first_male","player_title_first_female"),          // Fürst
    MARGRAVE("player_title_margrave_male","player_title_margrave_female"), // Markgraf
    COUNT_PALATINE("player_title_count_palatine_male","player_title_count_palatine_female"), // Pfalzgraf
    LANDGRAVE("player_title_landgrave_male","player_title_landgrave_female"), // LANDGRAF
    DUKE("player_title_duke_male","player_title_duke_female"),            // Herzog
    PRINCE_ELECTOR("player_title_prince_elector_male","player_title_prince_elector_female"), // Kurfürst
    GRAND_DUKE("player_title_grand_duke_male","player_title_grand_duke_female"), // Großherzog
    ARCHDUKE("player_title_archduke_male","player_title_archduke_female"),// Erzherzog
    KING("player_title_king_male","player_title_king_female"),            // König/Königin
    EMPEROR("player_title_emperor_male","player_title_emperor_female"),   // Kaiser

}