import scala.collection.mutable.ListBuffer
import scala.util.Random

class Evolver(val problem: BinPackProblem){

  val initialPopulationSize = 1000
  val genotypeDecoder = SimpleDecoder(problem)
  val parentSelection = BestSelection
  val environmentSelection = TournamentSelection()
  val recombination = OrderedRecombination
  var population = initializePopulation(initialPopulationSize, problem)

  def run() {
    println(problem)
    var i = 0
    var abortCounter = 0
    while (abortCounter < 5) { 
      println(population)
      if (population.best == population.worst) abortCounter += 1 else abortCounter = 0 
      val parents = parentSelection.select(population.individuals, population.individuals.size/2)
      val children = bearChildren(parents)
      val nextGeneration = environmentSelection.select(parents ++: children, population.size)  
      this.population = new Population(i, nextGeneration)
      i += 1
    }

  }

  private def initializePopulation(size: Int, problem: BinPackProblem): Population = {
    require(problem != null)
    require(size > 1, "A Population should consist of at least 2 individuals")
    val individualBuffer = new ListBuffer[Individual]
    for (i <- 1 to size) {
      val shuffledItems = Random.shuffle(problem.items)
        val individual = new Individual(shuffledItems, this.genotypeDecoder)
        individualBuffer += individual
    }
    new Population(0, individualBuffer.toList)
  }

  private def bearChildren(parents: List[Individual], reproductionFactor: Int = 1): List[Individual] = {
    val children = new ListBuffer[Individual]
    (1 to reproductionFactor).foreach{ i =>
      val shuffledParents = Random.shuffle(parents)
      (shuffledParents :+ shuffledParents.head).sliding(2).foreach{ 
        case parentA::parentB::Nil => children += this.recombination.recombine(parentA, parentB)
        case _ =>
      }
    }
    children.toList
  }
}
