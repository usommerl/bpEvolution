class Population(val generation: Int, val individuals: List[Individual]){

  def size: Int = individuals.size
  def best: Individual = individuals.sorted.min
  def worst: Individual = individuals.sorted.max
  override def toString(): String = {"[Population generation="+this.generation+" size="+this.size+" best="+best.quality+" worst="+worst.quality+"]"}
}
