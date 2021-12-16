package de.tobiasreich.kaiser.game.data.population

import de.tobiasreich.kaiser.game.data.player.PopulationReport
import de.tobiasreich.kaiser.game.data.population.Health.HEALTH_DEAD

/** Data object representing a whole country population */
class Population {

    companion object{
        //TODO Consider laws where children are counted adult earlier (e.g. for more work force but sadness/education)
        const val AGE_ADULT = 14  // When a child became adult in 1400
        const val AGE_OLD = 50    // When someone was old in 1400
        const val MAX_AGE = 70    // When most people died in around 1400

        const val FOOD_USE_PER_PERSON = 5  // How much food a person needs per year

        const val BASE_BIRTH_FACTOR = 0.05 // Default value for birth (independent of mood, health etc.)
    }

    val children = mutableListOf<Person>()
    val adults = mutableListOf<Person>()
    val old = mutableListOf<Person>()

    //TODO Implement jobs
    val farmers = mutableListOf<Person>()
    val merchants = mutableListOf<Person>()
    val soldiers = mutableListOf<Person>()
    val priests = mutableListOf<Person>()


    init {
        fillDummyPopulation() // Testing only
    }

    @Deprecated("Only used for testing!")
    fun fillDummyPopulation(){
        for (i in 0..10000){
            children.add(Person((Math.random() *  AGE_ADULT).toInt()))
        }
        for (i in 0..10000){
            adults.add(Person((Math.random() *  AGE_OLD).toInt()))
        }
        for (i in 0..10000){
            old.add(Person((Math.random() * MAX_AGE).toInt()))
        }
    }

    /** This processes the food given to the population
     *  A person needs FOOD_USE_PER_PERSON units of food per year.
     *  If gotten less their health will decline.
     *
     *  TODO: add the parameter of distributeFair determines whether all people should get the same amount of food
     *  or the adults should be fed first, then the children and at last the old ones.
     *  This will have the result of more old people dying in case of a famine.
     *
     */
    fun processFood(amountFood : Int) : Int{
        var died = 0
        var foodLeft = amountFood

        println("Food left: $foodLeft")

        if (foodLeft > adults.size){
            foodLeft -= adults.size * FOOD_USE_PER_PERSON
            println("All adults were fed!")
        } else {
            // Estimate how much food is left. All people after that starve
            // E.g. 5 food per person, 100 food left
            // -> 100 / 5 = 20 -> from index[20] on people starve this year
            val peopleStarveIndex = foodLeft / FOOD_USE_PER_PERSON
            foodLeft -= peopleStarveIndex * FOOD_USE_PER_PERSON
            println("Adults $peopleStarveIndex to ${adults.size} starve")
            died += declineHealth(adults, peopleStarveIndex, adults.size)
        }

        if (foodLeft > children.size){
            foodLeft -= children.size * FOOD_USE_PER_PERSON
            println("All children were fed!")
        } else {
            // Estimate how much food is left. All people after that starve
            // E.g. 5 food per person, 100 food left
            // -> 100 / 5 = 20 -> from index[20] on people starve this year
            val peopleStarveIndex = foodLeft / FOOD_USE_PER_PERSON
            foodLeft -= peopleStarveIndex * FOOD_USE_PER_PERSON
            println("Children $peopleStarveIndex to ${children.size} starve")
            died += declineHealth(children, peopleStarveIndex, adults.size)
        }
        if (foodLeft > old.size){
            foodLeft -= old.size * FOOD_USE_PER_PERSON
            println("All old were fed!")
        } else {
            // Estimate how much food is left. All people after that starve
            // E.g. 5 food per person, 100 food left
            // -> 100 / 5 = 20 -> from index[20] on people starve this year
            val peopleStarveIndex = foodLeft / FOOD_USE_PER_PERSON
            foodLeft -= peopleStarveIndex * FOOD_USE_PER_PERSON
            println("Old people $peopleStarveIndex to ${old.size} starve")
            died += declineHealth(old, peopleStarveIndex, adults.size)
        }

        println("$died people starved to death this year!")

        return died
    }

    /** declines the health of a list of people.
     *  This can also be a partial list of people. Use the start / end parameters for that.
     *
     *  The people getting their health reduced are automatically checked for dying
     */
    private fun declineHealth(people : MutableList<Person>, startInclusive : Int, endExclusive : Int) : Int{
        val died = mutableListOf<Person>()
        for (i in startInclusive until endExclusive){
            people[i].health = people[i].health-1
            if (people[i].health <= HEALTH_DEAD){
                died.add(people[i])
            }
        }
        println("${died.size} from this group died!")
        people.removeAll(died)
        return died.size
    }


        /** This calculates how many babies are added to the population,
     *  how many died
     *  and migration aspects
     */
    fun processPopulationChange() : PopulationReport {
        val diedOfAge = processAging()
        val born = processBirth()
        val diedOfHealth = processHealth()
        val immigrated = processImmigration()
        val emigrated = processEmigration()

        val totalChange = born + immigrated - diedOfAge - diedOfHealth - emigrated
        return PopulationReport(born, diedOfAge, diedOfHealth, immigrated, emigrated, totalChange)
    }



    /** This processes all peoples age
     *
     *  Do first the old so we don't process younger ones twice when moving to another group
     */
    private fun processAging() : Int {
        val changedPersons = mutableListOf<Person>()

        old.forEach {
            it.age++
            // TODO: This will get more influences like HEALTH etc.
            if (it.age > MAX_AGE){
                changedPersons.add(it)
            }
        }
        val diedOfAge = changedPersons.size
        old.removeAll(changedPersons)
        changedPersons.clear()

       adults.forEach{
            it.age++
            if (it.age > AGE_OLD){
                changedPersons.add(it)
            }
        }
        adults.removeAll(changedPersons)
        old.addAll(changedPersons)
        changedPersons.clear()

        children.forEach{
            it.age++
            if (it.age > AGE_ADULT){
                changedPersons.add(it)
            }
        }
        children.removeAll(changedPersons)
        adults.addAll(changedPersons)

        return diedOfAge
    }

    /** This processes the health of the population.
     *  This comes from values like:
     *  - how much wheat is given to the people
     *  - how much money is spent on health system
     *  - ...
     */
    private fun processHealth() : Int{
        //TODO: Implement
        return 0
    }

    /** Processes how many babies are born this year.
     *  TODO: This should include health, wealth etc.
     */
    private fun processBirth() : Int {
        val birthfactor = BASE_BIRTH_FACTOR //TODO add other aspects like health
        val amountNewBorn = (adults.size * birthfactor).toInt()
        for (i in 0..amountNewBorn){
            children.add(Person())
        }
        return amountNewBorn
    }


    /** This defines how many people immigrate into this country this year
     *  - Depends on immigration laws
     *  - Mood
     *  - Wheat distribution
     *  - ...
     */
    private fun processImmigration() : Int {
        //TODO: Implement this
        return 0
    }



    /** This defines how many people leave this country this year
     *  This depends on
     *  - Mood
     *  - Wheat distribution
     *  - Tax hight
     *  - ...
     */
    private fun processEmigration() : Int {
        return 0
    }
}