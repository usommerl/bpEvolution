import scala.collection.mutable.ListBuffer
import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION
import java.lang.AssertionError

abstract class GenotypeDecoder(problem: BinPackProblem){

  def decode(genotype: Genotype): Phenotype

  @elidable(ASSERTION)
  protected def assertPhenotypeIsValid(phenotype: List[Bin], problem: BinPackProblem){
    val numberOfItems = phenotype.flatMap(_.items).size;
    assert(numberOfItems == problem.size, "Phenotype is not valid (numberOfItems="+numberOfItems+", problem.size="+problem.size+")")
    val itemsInBins = phenotype.flatMap(_.items) 
    for (item <- problem.items) 
      if (!itemsInBins.contains(item)) throw new AssertionError(item+" in Problem but not in phenotype")
    }

  protected val sumItemSizesInBuffer = (itemBuffer: ListBuffer[Item]) => (0.0 /: itemBuffer)(_+_.size);

}

case class SimpleDecoder(problem: BinPackProblem) extends GenotypeDecoder(problem) {

  def decode(genotype: Genotype): Phenotype = {
    val binBuffer = new ListBuffer[Bin]
    val itemBuffer = new ListBuffer[Item]
    for (item <- genotype) { 
      if (sumItemSizesInBuffer(itemBuffer) + item.size > this.problem.binCapacity) {
        binBuffer += new Bin(this.problem.binCapacity, itemBuffer.toList)
        itemBuffer.clear()
      } 
      itemBuffer += item
    }
    if (!itemBuffer.isEmpty) binBuffer += new Bin(this.problem.binCapacity, itemBuffer.toList)
    assertPhenotypeIsValid(binBuffer.toList, this.problem)
    binBuffer.toList
  }
}

case class FirstFitDecoder(problem: BinPackProblem) extends GenotypeDecoder(problem) {

  def decode(genotype: Genotype): Phenotype = {
    val binBuffer = new ListBuffer[Bin]
    val itemBuffer = new ListBuffer[Item]
    for (item <- genotype) {
      itemBuffer += item
      if (sumItemSizesInBuffer(itemBuffer) > this.problem.binCapacity) {
        itemBuffer -= item
        binBuffer += new Bin(this.problem.binCapacity, itemBuffer.toList)
        itemBuffer.clear()
        val capableBins = for (bin <- binBuffer if bin.remainingCapacity >= item.size) yield bin
        if (capableBins.isEmpty) { 
          itemBuffer += item 
        } else {
            val existingBin = capableBins.head
            binBuffer -= existingBin
            binBuffer += new Bin(this.problem.binCapacity, existingBin.items ++ List(item))
        }
      }
    }
    if (!itemBuffer.isEmpty) binBuffer += new Bin(this.problem.binCapacity, itemBuffer.toList)
    assertPhenotypeIsValid(binBuffer.toList, this.problem)
    binBuffer.toList
  }
}
