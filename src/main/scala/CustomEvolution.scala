class CustomEvolution(configuration: Configuration) extends Evolution(configuration) {

  def evolve(population: Population): Population = {
    val parents = parentSelection.select(population.individuals, population.individuals.size/4)
    val children = bearAndMutateChildren(parents, 4)
    val nextGeneration = environmentSelection.select(parents ++: children, population.size)  
    new Population(population.generation + 1, nextGeneration)
  }

}
