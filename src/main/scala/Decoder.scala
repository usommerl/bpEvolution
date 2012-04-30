import scala.collection.mutable.ListBuffer
import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION

abstract class GenotypeDecoder(problem: BinPackProblem){

  def decode(genotype: List[Item]): List[Bin]

  @elidable(ASSERTION)
  protected def assertPhenotypeIsValid(phenotype: List[Bin], problem: BinPackProblem){
    val numberOfItems = phenotype.flatMap(_.items).size;
    assert(numberOfItems == problem.size, "Phenotype is not valid (numberOfItems="+numberOfItems+", problem.size="+problem.size+")")
    val itemsInBins = phenotype.flatMap(_.items) 
    for (item <- problem.items) 
      if (!itemsInBins.contains(item)) throw new java.lang.AssertionError(item+" in Problem but not in phenotype")
    }

}

case class SimpleDecoder(problem: BinPackProblem) extends GenotypeDecoder(problem) {

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
    assertPhenotypeIsValid(binBuffer.toList, this.problem)
    binBuffer.toList
  }
}
