class Bin(val capacity: Double, val items: List[Item]) {
    require(capacity > 0)
    require(remainingCapacity >= 0, "Bin is above capacity")
    val fillLevel = (0.0 /: items)(_+_.size)
    def remainingCapacity = capacity - fillLevel 
}
