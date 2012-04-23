import scala.collection.mutable.ListBuffer
import scala.util.Random

class Population(val individuals: Set[Individual]){

  def size: Int = individuals.size

  override def toString(): String = {"[Population size="+this.size+"]"}

}

object Population {

  def initialize(size: Int, problem: BinPackProblem): Population = {
    require(problem != null)
    require(size > 1, "A Population should consist of at least 2 individuals")
    val individualBuffer = new ListBuffer[Individual]
    for (i <- 1 to size) {
      val shuffledItems = Random.shuffle(problem.items)
      val individual = createIndividual(shuffledItems, problem.binCapacity)
      val amountMatches = (0 /: individual.bins)(_+_.items.size) == problem.items.size
      assert(amountMatches, "Amount of items in problem and individual does not match")
      individualBuffer += individual
    }
    new Population(individualBuffer.toSet)
  }

  private def createIndividual(items:List[Item], binCapacity:Double): Individual = {
    val binBuffer = new ListBuffer[Bin]
    val itemBuffer = new ListBuffer[Item]
    def sumItemSizesInBuffer: Double = (0.0 /: itemBuffer)(_+_.size) 
    for (item <- items) { 
      if (sumItemSizesInBuffer + item.size > binCapacity) {
        assert(!itemBuffer.isEmpty, "Item buffer should not be empty at this point")
        binBuffer += new Bin(binCapacity, itemBuffer.toSet)
        itemBuffer.clear()
      } 
      itemBuffer += item
    }
    binBuffer += new Bin(binCapacity, itemBuffer.toSet)
    new Individual(binBuffer.toSet)
  }
}
