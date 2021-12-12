package de.tobiasreich.kaiser.game.data.population

/** Data object representing a whole country population */
class Population {

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
            children.add(Person())
        }
        for (i in 0..10000){
            adults.add(Person())
        }
        for (i in 0..10000){
            old.add(Person())
        }
    }

}