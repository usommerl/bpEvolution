class Population(val generation: Int, val individuals: List[Individual]){
  
  private val genotypeFingerprints = individuals.map(_.genotype.hashCode).toSet
  val diversity = genotypeFingerprints.size.toDouble / this.size
  def size: Int = individuals.size
  def best: Individual = individuals.sorted.min
  def worst: Individual = individuals.sorted.max
  override def toString(): String = {"[Population generation="+this.generation+" size="+this.size+" diversity="+this.diversity+" best=%.14f worst=%.14f]".format(best.quality,worst.quality)}
}
