import scala.math.ceil

class BinPackProblem(val id: String, val binCapacity: Double, val bestKnownSolution: Int, val items: List[Item]) {
    require(id != null)
    require(id.length > 0)
    require(items.size > 0)
    require(bestKnownSolution > 0)
    
    lazy val size = this.items.size
    lazy val theoreticalOptimum = ceil((0.0 /: items)(_+_.size) / this.binCapacity).toInt

    override def toString() = {
      "[BinPackProblem id="+id+" binCapacity="+binCapacity+" bestKnownSolution="+bestKnownSolution+"]" 
    }
} 
