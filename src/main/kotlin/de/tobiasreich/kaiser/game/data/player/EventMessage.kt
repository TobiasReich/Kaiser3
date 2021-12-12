package de.tobiasreich.kaiser.game.data.player

import de.tobiasreich.kaiser.game.data.country.HarvestCondition

interface EventMessage

/** Population changes (birth, death, migration...) */
class PopulationEvent(val birth : Int, val diedOfAge : Int, val diedOfHealth : Int, val immigrated : Int, val emigrated : Int) : EventMessage

/** Harvest news. Was it a good or a bad one? */
class HarvestEvent(val harvest: HarvestCondition) : EventMessage