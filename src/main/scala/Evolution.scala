import scala.util.Random
import scala.collection.mutable.ListBuffer
import scala.math.ceil

class Evolution(configuration: Configuration) extends Traversable[Population]{
  
  private val problem = configuration.problem
  private val initialPopulationSize = configuration.populationSize
  private val maxGenerations = configuration.maxGenerations
  private val genotypeDecoder = configuration.genotypeDecoder
  private val parentSelection = configuration.parentSelection
  private val parentSelectionImpact = configuration.parentSelectionImpact
  private val recombination = configuration.recombination
  private val mutations = configuration.mutations
  private val environmentSelection = configuration.environmentSelection
  private val qualityFunction = configuration.qualityFunction
  
  def foreach[U](f: (Population) => U): Unit = {
    var population = initializePopulation(initialPopulationSize, problem)
    f(population)
    while (population.generation <= maxGenerations &&
           population.best.quality.toInt > problem.theoreticalOptimum ) { 
      population = evolve(population)
      f(population)
    }
  }
  
  private def evolve(population: Population): Population = {
    val parents = parentSelection.select(population.individuals, population.size/3)
    val children = bearAndMutateChildren(parents, 3)
    val nextGeneration = environmentSelection.select(population.individuals ++ children, population.size)  
    new Population(population.generation + 1, nextGeneration)
  } 

  private def initializePopulation(size: Int, problem: BinPackProblem): Population = {
    require(size > 1, "A Population should consist of at least 2 individuals")
    val individualBuffer = new ListBuffer[Individual]
    for (i <- 1 to size) {
      val shuffledItems = Random.shuffle(problem.items)
        val individual = new Individual(shuffledItems, this.genotypeDecoder, this.qualityFunction)
        individualBuffer += individual
    }
    new Population(0, individualBuffer.toList)
  }

  private def bearChildren(parents: List[Individual], spawnRate: Int = 1): List[Individual] = {
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

  private def mutateIndividuals(individuals: List[Individual]): List[Individual] = {
    def applyMutations(individual: Individual, mutations: List[Mutation]): Individual = 
      mutations match { 
        case Nil        => individual
        case head::tail => head.mutate(applyMutations(individual, tail))
      }
    val mutantBuffer = new ListBuffer[Individual]
    for (individual <- individuals) mutantBuffer += applyMutations(individual, this.mutations)
    mutantBuffer.toList
  }

  private def bearAndMutateChildren(parents: List[Individual], spawnRate: Int = 1): List[Individual] = 
    mutateIndividuals(bearChildren(parents, spawnRate))
}
