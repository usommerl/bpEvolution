import scala.util.Random
import scala.collection.mutable.ListBuffer

class Evolution(configuration: Configuration) extends Traversable[Population]{
  
  protected val problem = configuration.problem
  protected val initialPopulationSize = configuration.populationSize
  protected val maxGenerations = configuration.maxGenerations
  protected val genotypeDecoder = configuration.genotypeDecoder
  protected val parentSelection = configuration.parentSelection
  protected val recombination = configuration.recombination
  protected val mutations = configuration.mutations
  protected val environmentSelection = configuration.environmentSelection
  protected val qualityFunction = configuration.qualityFunction
 
  def foreach[U](f: (Population) => U): Unit = {
    var population = initializePopulation(initialPopulationSize, problem)
    var qualityWorseThanBestKnownSolution = true
    while (population.generation <= maxGenerations && qualityWorseThanBestKnownSolution) { 
      f(population)
      if (population.best.quality.toInt == problem.bestKnownSolution &&
          configuration.earlyAbort) 
        qualityWorseThanBestKnownSolution = false
      population = evolve(population)
    }
  }
  
  protected def evolve(population: Population): Population = {
    val parents = parentSelection.select(population.individuals, population.individuals.size/2)
    val children = bearAndMutateChildren(parents, 4)
    val nextGeneration = environmentSelection.select(parents ++ children, population.size)  
    new Population(population.generation + 1, nextGeneration)
  } 

  protected def initializePopulation(size: Int, problem: BinPackProblem): Population = {
    require(problem != null)
    require(size > 1, "A Population should consist of at least 2 individuals")
    val individualBuffer = new ListBuffer[Individual]
    for (i <- 1 to size) {
      val shuffledItems = Random.shuffle(problem.items)
        val individual = new Individual(shuffledItems, this.genotypeDecoder, this.qualityFunction)
        individualBuffer += individual
    }
    new Population(0, individualBuffer.toList)
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
    def applyMutations(individual: Individual, mutations: List[Mutation]): Individual = 
      mutations match { 
        case Nil        => individual
        case head::tail => head.mutate(applyMutations(individual, tail))
      }
    val mutantBuffer = new ListBuffer[Individual]
    for (individual <- individuals) mutantBuffer += applyMutations(individual, this.mutations)
    mutantBuffer.toList
  }

  protected def bearAndMutateChildren(parents: List[Individual], spawnRate: Int = 1): List[Individual] = 
    mutateIndividuals(bearChildren(parents, spawnRate))
}
