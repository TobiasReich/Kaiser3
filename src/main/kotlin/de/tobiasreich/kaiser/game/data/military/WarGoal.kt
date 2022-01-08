package de.tobiasreich.kaiser.game.data.military

/** Types of war the war starting player might select:
 *  - Kill units: "Classic" war where it's all about killing units.
 *  - Steal money: This is more a plunder raid where units try to steal money from the enemy
 *  - Get Slaves: This is stealing people from the invaded country
 *  - Conquer: This is trying to conquer some land of the enemy
 *  - Burn Buildings: Trying to destroy buildings instead of units
 */
enum class WarGoal(val stringResource : String) {

    KILL_UNITS("war_view_action_war_goal_kill"),
    STEAL_MONEY("war_view_action_war_goal_steal"),
    GET_SLAVES("war_view_action_war_goal_slaves"),
    CONQUER("war_view_action_war_goal_conquer"),
    BURN_BUILDINGS("war_view_action_war_goal_burn")

}