import scala.collection.mutable.ListBuffer
import scala.util.Random

class Evolver(val problem: BinPackProblem){
  
  val initialPopulationSize = 1000
  val genotypeDecoder = new SimpleDecoder(problem)
  val selection = SimpleSelection
  val recombination = OrderedRecombination
  var population = initializePopulation(initialPopulationSize, problem)

  def run() {
    println(problem)
    for (i <- 1 to 20) { 
      printShit()
      val parents = selection.select(population)
      val children = bearChildren(parents)
      this.population = new Population(i, (parents ++: children))
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

  private def bearChildren(parents: List[Individual]): List[Individual] = {
    val children = new ListBuffer[Individual]
    val coupleIterator = (parents :+ parents.head).sliding(2)
    while(coupleIterator.hasNext){
      val couple = coupleIterator.next
      couple match { 
        case parentA::parentB::Nil => children += this.recombination.recombine(parentA, parentB)
        case _ => throw new Exception("Sorry, this won't work")
      }
    }
    children.toList
  }

  private def printShit() {
    println(population)
    //population.individuals.sorted.reverse.foreach(println)
  }
}
