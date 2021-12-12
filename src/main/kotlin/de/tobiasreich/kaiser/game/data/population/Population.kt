package de.tobiasreich.kaiser.game.data.population

/** Data object representing a whole country population */
class Population {

    companion object{
        val AGE_ADULT = 14  // When a child became adult in 1400
        val AGE_OLD = 50    // When someone was old in 1400
        val MAX_AGE = 70    // When most people died in around 1400

        val BIRTH_FACTOR = 0.05 // Default value for birth (independent of mood, health etc.)
    }

    val children = mutableListOf<Person>()
    val adults = mutableListOf<Person>()
    val old = mutableListOf<Person>()



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
            old.add(Person((Math.random() *  MAX_AGE).toInt()))
        }
    }

    /** This calculates how many babies are added to the population
     *
     */
    fun processPopulationChange() {
        processAging()
        processBirth()
        processHealth()
        processImmigration()
        processEmigration()
    }



    /** This processes all peoples age
     *
     *  Do first the old so we don't process younger ones twice when moving to another group
     */
    private fun processAging() {
        val changedPersons = mutableListOf<Person>()

        old.forEach {
            it.age++
            // TODO: This will get more influences like HEALTH etc.
            if (it.age > MAX_AGE){
                changedPersons.add(it)
            }
        }
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
    }

    /** This processes the health of the population.
     *  This comes from values like:
     *  - how much wheat is given to the people
     *  - how much money is spent on health system
     *  - ...
     */
    private fun processHealth() {
        //TODO: Implement
    }

    /** Processes how many babies are born this year.
     *  TODO: This should include health, wealth etc.
     */
    private fun processBirth() {
        val birthfactor = BIRTH_FACTOR
        val amountNewBorn = (adults.size * birthfactor).toInt()
        for (i in 0..amountNewBorn){
            children.add(Person())
        }
    }


    /** This defines how many people immigrate into this country this year
     *  - Depends on immigration laws
     *  - Mood
     *  - Wheat distribution
     *  - ...
     */
    private fun processImmigration() {
        //TODO: Implement this
    }



    /** This defines how many people leave this country this year
     *  This depends on
     *  - Mood
     *  - Wheat distribution
     *  - Tax hight
     *  - ...
     */
    private fun processEmigration() {


    }
}