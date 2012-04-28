import scala.util.Random

trait Recombination {
  def recombine(parentA: Individual, parentB: Individual): Individual
}

object OrderedRecombination extends Recombination {

  def recombine(parentA: Individual, parentB: Individual): Individual = {
    assert(parentA.decoder == parentB.decoder, "Genotype decoders have to be equal")
    val randomLength = Random.nextInt(parentA.genotype.length + 1)
    val partA = parentA.genotype.take(randomLength)
    val partB = parentB.genotype.diff(partA)
    val childGenotype = partA ++ partB
    new Individual(childGenotype, parentA.decoder)
  }
}
