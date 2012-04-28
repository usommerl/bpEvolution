import scala.collection.mutable.ListBuffer
import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION

abstract class GenotypeDecoder(problem: BinPackProblem){

  def decode(genotype: List[Item]): List[Bin]

  @elidable(ASSERTION)
  protected def evaluatePhenotype(phenotype: List[Bin], problem: BinPackProblem){
    val itemsInBins = (0 /: phenotype)(_+_.items.toSet.size);
    assert(itemsInBins == problem.size, "Phenotype is not valid (itemsInBins="+itemsInBins+", problem.size="+problem.size+")")
    val itemBuffer = new ListBuffer[Item]
    phenotype.foreach(itemBuffer ++= _.items)
    for (item <- problem.items) 
      if (!itemBuffer.contains(item)) throw new java.lang.AssertionError("Phenotype is not valid")
    }

}

class SimpleDecoder(problem: BinPackProblem) extends GenotypeDecoder(problem) {

  def decode(genotype: List[Item]): List[Bin] = {
    val binBuffer = new ListBuffer[Bin]
    val itemBuffer = new ListBuffer[Item]
    def sumItemSizesInBuffer: Double = (0.0 /: itemBuffer)(_+_.size); 
    for (item <- genotype) { 
      if (sumItemSizesInBuffer + item.size > this.problem.binCapacity) {
        assert(!itemBuffer.isEmpty, "Item buffer should not be empty at this point")
        binBuffer += new Bin(this.problem.binCapacity, itemBuffer.toList)
        itemBuffer.clear()
      } 
      itemBuffer += item
    }
    binBuffer += new Bin(this.problem.binCapacity, itemBuffer.toList)
    evaluatePhenotype(binBuffer.toList, this.problem)
    binBuffer.toList
  }
}
