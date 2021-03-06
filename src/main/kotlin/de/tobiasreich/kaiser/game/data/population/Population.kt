package de.tobiasreich.kaiser.game.data.population

import de.tobiasreich.kaiser.game.Player
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

        /* This is the factor for the MAXIMUM food that can be distributed.
         * It is useful since excessive food distribution increases the reproduction in the country
         * TODO: And also the happiness */
        const val MAX_FOOD_USE_PER_PERSON_FACTOR = 1.1f

        const val BASE_EXPENSE_HEALTH_SYSTEM = 1.0
        const val BASE_EXPENSE_EDUCATION_SYSTEM = 1.0


        /* This is the factor for the MINIMUM food that can be distributed.
         * The rule is that at least 20% of the wheat has to stay as seed for the next year. Thus it is not possible to
         * give less.
         *
         * TODO: This rule was already obsolete in the original Kaiser which also copied it from another game without
         * stating the reason. I don't see this as a need. It might however make more sense to allow actually all of
         * it to be distributed but then warn that wheat is required for planting in the next year. So the user has a
         * greater freedom in strategy (sacrifice all now and speculate for cheap corn prices) */
        const val MIN_FOOD_USE_PER_PERSON_FACTOR = 0.2f

        const val BASE_BIRTH_FACTOR = 0.05f // Default value for birth (independent of mood, health etc.)
    }

    val children = mutableListOf<Person>()
    val adults = mutableListOf<Person>()
    val old = mutableListOf<Person>()

    //TODO Implement jobs
    val farmers = mutableListOf<Person>()
    val merchants = mutableListOf<Person>()
    val soldiers = mutableListOf<Person>()
    val priests = mutableListOf<Person>()

    /** The default mood of the population.
      * TODO: Decide specific traits for each player (might be "happy population" etc. like in Kaiser II)
      */
    var defaultMood = 100

    /** A modifier that can be applied temporarily. E.g. by sabotage */
    var moodModifier = 0

    var mood = 50


    init {
        createStartPopulation()
    }

    private fun createStartPopulation(){
        for (i in 0 until 1000){
            children.add(Person((Math.random() *  AGE_ADULT).toInt()))
        }
        for (i in 0 until 200){
            adults.add(Person((Math.random() *  AGE_OLD).toInt()))
        }
        for (i in 0 until 200){
            old.add(Person((Math.random() * MAX_AGE).toInt()))
        }
    }

    /** Helper Method returning how many people live in this country */
    fun getAmountPeople() : Int {
        return adults.size + children.size + old.size
    }

    /** This processes the food given to the population
     *  A person needs FOOD_USE_PER_PERSON units of food per year.
     *  If gotten less their health will decline.
     *
     *  TODO: We might want to add the parameter "DistributeFair" if we want to change priorities.
     *
     *  Calculation of how many people are starving:
     *  Estimate how much food is left. All people after that starve
     *  E.g. 5 food per person, 100 food left
     *  -> 100 / 5 = 20 -> from index[20] on people starve this year */
    fun processFood(player: Player) : Int{
        var died = 0 // Counter of how many people died
        var foodLeft = player.storedFood

        println("Food left: $foodLeft")

        val foodNeededAdults    = adults.size * FOOD_USE_PER_PERSON
        val foodNeededChildren  = children.size * FOOD_USE_PER_PERSON
        val foodNeededOld       = old.size * FOOD_USE_PER_PERSON

        if (foodLeft >= foodNeededAdults){
            foodLeft -= foodNeededAdults
        } else {
            val peopleStarveIndex = foodLeft / FOOD_USE_PER_PERSON
            foodLeft = 0 //All food is used
            died += declineHealth(adults, peopleStarveIndex, adults.size)
        }

        if (foodLeft > foodNeededChildren){
            foodLeft -= foodNeededChildren
        } else {
            val peopleStarveIndex = foodLeft / FOOD_USE_PER_PERSON
            foodLeft = 0 //All food is used
            died += declineHealth(children, peopleStarveIndex, children.size)
        }

        if (foodLeft > foodNeededOld){
            foodLeft -= foodNeededOld
        } else {
            val peopleStarveIndex = foodLeft / FOOD_USE_PER_PERSON
            foodLeft = 0 //All food is used
            died += declineHealth(old, peopleStarveIndex, old.size)
        }

        player.storedFood = foodLeft // Write the leftover food back to the player

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
        //println("${died.size} from this group died!")
        people.removeAll(died)
        return died.size
    }


        /** This calculates how many babies are added to the population,
     *  how many died
     *  and migration aspects
     */
    fun processPopulationChange(player : Player) : PopulationReport {
        val starvedToDeath = processFood(player)
        val diedOfAge = processAging()
        val born = processBirth()
        val diedOfHealth = processHealth()
        val immigrated = processImmigration()
        val emigrated = processEmigration()

        val totalChange = born + immigrated - diedOfAge - diedOfHealth - emigrated - starvedToDeath
        return PopulationReport(born, diedOfAge, diedOfHealth, immigrated, emigrated, starvedToDeath, totalChange)
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

    /** This calculates the mood for the population. */
    fun calculateMood(laws: Laws) {
        // MoodReduction is 1 - tax - lawEndorcement - (1-immigration)
        // E.g. tax=0, law=0, immigration=1 -> mood reduction is 0
        val moodReduction = laws.incomeTax + laws.lawEnforcement + (1.0 - laws.immigrationStrictness)

        // Mood raises by "(health + educational expenses) * 0.1"
        // E.g. health = 1, education = 1 -> Total mood gain = 0.2 (20%)
        val moodAddition = (laws.healthSystem + laws.educationSystem) * 0.1

        // Mood is calculated by the default mood - reduction factor + mood addition
        val calculatedMood = (defaultMood + moodModifier - (moodReduction * 100.0) + (moodAddition * 100.0)).toInt()

        //        println("moodReductionFactor: $moodReductionFactor")
        //        println("moodAddition: $moodAddition")
        //        println("Calculated Mood: $calculatedMood")
        mood = calculatedMood.coerceAtMost(100).coerceAtLeast(0)
    }

    /** Removes amount adults */
    fun removeAdults(amount: Int) : List<Person>{
        val slaves = mutableListOf<Person>()
        slaves.addAll(adults.subList(0,amount))
        println("Adults 'lost'': ${slaves.size}")
        adults.removeAll(slaves)
        return slaves
    }

    /** Removes random amount people equally distributed (if possible) */
    fun addAdults(slaves : List<Person>) {
        println("Slaves added to population: ${slaves.size}")
        adults.addAll(slaves)
    }

    /** adds / subtracts a mood bonus for this population! */
    fun addMoodBonus(amount : Int) {
        this.moodModifier += amount
        println("MoodModifier: $moodModifier")
    }

    /** Clears the mood bonus/malus for this population! */
    fun clearMoodModifier(){
        this.moodModifier = 0
    }
}