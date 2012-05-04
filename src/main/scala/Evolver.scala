import scala.collection.mutable.ListBuffer
import scala.util.Random

abstract class Evolver(configuration: Configuration){

  protected val problem = configuration.problem
  protected val initialPopulationSize = configuration.populationSize
  protected val maxGenerations = configuration.maxGenerations
  protected val genotypeDecoder = configuration.genotypeDecoder
  protected val parentSelection = configuration.parentSelection
  protected val recombination = configuration.recombination
  protected val mutations = configuration.mutations
  protected val environmentSelection = configuration.environmentSelection
  private var population = initializePopulation(initialPopulationSize, problem)

  def run() {
    while (population.best.quality > problem.bestKnownSolution && population.generation <= maxGenerations) { 
       population = evolve(population)
    }
  }

  def evolve(population: Population): Population

  protected def initializePopulation(size: Int, problem: BinPackProblem): Population = {
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

  protected def bearAndMutateChildren(parents: List[Individual], spawnRate: Int = 1): List[Individual] = {
    mutateIndividuals(bearChildren(parents, spawnRate))
  }

  protected def bearChildren(parents: List[Individual], spawnRate: Int = 1): List[Individual] = {
    require(parents.size > 1, "Need at least two parents to bear children")
    val children = new ListBuffer[Individual]
    for (_ <- 1 to spawnRate) { 
      val shuffledParents = Random.shuffle(parents)
      (shuffledParents :+ shuffledParents.head).sliding(2).foreach{ 
        case parentA::parentB::Nil => children += this.recombination.recombine(parentA, parentB)
        case _ =>
      }
    }
    children.toList
  }

  protected def mutateIndividuals(individuals: List[Individual]): List[Individual] = {
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
