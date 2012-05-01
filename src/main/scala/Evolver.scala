import scala.collection.mutable.ListBuffer
import scala.util.Random

class Evolver(val problem: BinPackProblem){

  val initialPopulationSize = 500
  val maxRuns = 200
  val genotypeDecoder = SimpleDecoder(problem)
  val parentSelection = BestSelection
  val environmentSelection = TournamentSelection()
  val recombination = OrderedRecombination
  val mutations: List[Mutation] = List(InversionMutation, ShiftingMutation)
  var population = initializePopulation(initialPopulationSize, problem)

  def run() {
    println(problem)
    var i = 0
    while (population.best.quality > problem.bestKnownSolution && i <= maxRuns) { 
      println(population)
      i += 1
      val parents = parentSelection.select(population.individuals, population.individuals.size/4)
      val children = mutateIndividuals(bearChildren(parents, 4))
      val nextGeneration = environmentSelection.select(parents ++: children, population.size)  
      this.population = new Population(i, nextGeneration)
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
    require(parents.size > 1, "Need at least two parents to bear children")
    val children = new ListBuffer[Individual]
    for (_ <- 1 to reproductionFactor) { 
      val shuffledParents = Random.shuffle(parents)
      (shuffledParents :+ shuffledParents.head).sliding(2).foreach{ 
        case parentA::parentB::Nil => children += this.recombination.recombine(parentA, parentB)
        case _ =>
      }
    }
    children.toList
  }

  private def mutateIndividuals(individuals: List[Individual]): List[Individual] = {
    val mutantBuffer = new ListBuffer[Individual]
    def applyMutations(individual: Individual, mutations: List[Mutation]): Individual = {
      mutations match { 
        case Nil        => individual
        case head::tail => head.mutate(applyMutations(individual, tail))
      }
    }
    for (individual <- individuals) mutantBuffer += applyMutations(individual, this.mutations)
    mutantBuffer.toList
  }
}
