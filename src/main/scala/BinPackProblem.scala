class BinPackProblem(val id: String, val binCapacity: Double, val bestKnownSolution: Int, val items: List[Item]) {
    require(id != null)
    require(id.length > 0)
    require(items.size > 0)
    require(bestKnownSolution > 0)
    
    lazy val size = this.items.size
    override def toString() = {
      "[BinPackProblem id="+id+" binCapacity="+binCapacity+" bestKnownSolution="+bestKnownSolution+"]" 
    }
} 
