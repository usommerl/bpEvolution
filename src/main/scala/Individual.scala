import Individual._

class Individual(val bins: Set[Bin]) {

    def quality(f: Individual => Double = QualityFuncCapacity): Double = f(this)

    override def toString() = "[Individual numberOfBins="+bins.size+" quality="+this.quality()+"]"
}

object Individual {
   val QualityFuncBins = (x:Individual) => -x.bins.size.toDouble
   val QualityFuncCapacity = (x:Individual) => -x.bins.foldLeft(0.0){_+_.remainingCapacity}
}
