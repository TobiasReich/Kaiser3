package de.tobiasreich.kaiser.game.data.military


/** Different types of sabotage consist of these parameters:
 * - nameRes: I18N name resource
 * - cost: the costs per sabotage action (higher with higher enemy state title)
 * - difficulty: how difficult the task is. A higher value = higher chance of failing
 * - catchRisk: if the sabotage fails, this indicates the chance of the agent to confess
 */
enum class SabotageType(val nameRes : String, val cost : Int, val difficulty : Double, val catchRisk : Double) {

    STEAL_MONEY("sabotage_type_steal",1000, 0.3, 0.2),
    BURN_MILLS("sabotage_type_burn",2000, 0.5, 0.4),
    START_REVOLT("sabotage_type_revolt",5000, 0.7, 0.6),
    DEMORALIZE_TROOPS("sabotage_type_demoralize_troops",10000, 0.8, 0.8),

}
