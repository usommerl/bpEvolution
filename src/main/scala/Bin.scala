class Bin(val initialCapacity: Double, val items: Set[Item]) {
    require(initialCapacity > 0)
    require(remainingCapacity >= 0, "Bin is above capacity")
    def remainingCapacity = initialCapacity - fillLevel 
    lazy val fillLevel = (0.0 /: items)(_+_.size)
}
