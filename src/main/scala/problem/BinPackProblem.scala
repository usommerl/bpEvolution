import scala.math.ceil

class BinPackProblem(val id: String, val binCapacity: Double, val bestKnownSolution: Int, val items: List[Item]) {
    require(id != null)
    require(id.length > 0)
    require(items.size > 0)
    require(bestKnownSolution > 0)

    lazy val size = this.items.size
    private lazy val sumItemSizes = (BigDecimal("0.0") /: items)( (s,i) => s + BigDecimal(i.size.toString))
    lazy val theoreticalOptimum = ceil(sumItemSizes.toDouble/binCapacity).toInt

    override def toString() = {
      "[BinPackProblem id="+id+" binCapacity="+binCapacity+" theoreticalOptimum="+theoreticalOptimum+" bestKnownSolution="+bestKnownSolution+"]" 
    }
} 
