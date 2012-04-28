import scala.util.Random

trait Selection {
    def select(population: Population): List[Individual]
}

object SimpleSelection extends Selection {

    def select(population: Population): List[Individual] = {
      val numberOfParents = population.size / 2 
      Random.shuffle(population.individuals.sorted.take(numberOfParents))
    }
}
