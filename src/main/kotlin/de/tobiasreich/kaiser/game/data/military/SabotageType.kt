package de.tobiasreich.kaiser.game.data.military


/** Different types of sabotage consist of these parameters:
 * - nameRes: I18N name resource
 * - cost: the costs per sabotage action (higher with higher enemy state title)
 * - difficulty: how difficult the task is. A higher value = higher chance of failing
 * - catchRisk: if the sabotage fails, this indicates the chance of the agent to confess
 */
enum class SabotageType(val nameRes : String, val cost : Int, val difficulty : Double, val confess : Double,
                        val messageTitle : String, val accidentText : String, val sabotageText : String, val confessionText : String) {

    STEAL_MONEY("sabotage_type_steal",1000, 0.3, 0.2, "sabotage_type_steal_messsage_title", "sabotage_type_steal_accident_text", "sabotage_type_steal_sabotage_text", "sabotage_type_steal_confession"),
    BURN_MILLS("sabotage_type_burn",2000, 0.5, 0.4, "sabotage_type_burn_messsage_title","sabotage_type_burn_accident_text", "sabotage_type_burn_sabotage_text", "sabotage_type_burn_confession"),
    START_REVOLT("sabotage_type_revolt",5000, 0.7, 0.6, "sabotage_type_revolt_messsage_title", "sabotage_type_revolt_accident_text", "sabotage_type_revolt_sabotage_text", "sabotage_type_revolt_confession"),
    DEMORALIZE_TROOPS("sabotage_type_demoralize_troops",10000, 0.8, 0.8, "sabotage_type_demoralize_troops_messsage_title", "sabotage_type_demoralize_troops_accident_text", "sabotage_type_demoralize_troops_sabotage_text", "sabotage_type_demoralize_troops_confession");

    companion object{
        const val REVOLT_MOOD_REDUCTION_FACTOR = -10 // Mood reduction per revolt "sabotage"
        const val MILITARY_MORALE_REDUCTION_FACTOR = -0.1 // Morale change per "sabotage"
    }




}
