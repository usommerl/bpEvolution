class BinPackProblem(val identifier: String, val containerCapacity: Int, val numberOfItems: Int, val bestKnownSolution: Int, val items: List[Int]) {
    require(identifier != null)
    require(identifier.length > 0)
} 
