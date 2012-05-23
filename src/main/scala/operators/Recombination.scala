import scala.util.Random
import scala.collection._

trait Recombination {
  def recombine(parentA: Individual, parentB: Individual): Individual
}

object OrderedRecombination extends Recombination {
  def recombine(parentA: Individual, parentB: Individual): Individual = {
    require(parentA.decoder == parentB.decoder, "Genotype decoders have to be equal")
    require(parentA.qualityFunction == parentB.qualityFunction, "Quality functions have to be equal")
    val randomLength = Random.nextInt(parentA.genotype.length + 1)
    val partA = parentA.genotype.take(randomLength)
    val partB = parentB.genotype.diff(partA)
    val childGenotype = partA ++ partB
    new Individual(childGenotype, parentA.decoder, parentA.qualityFunction)
  }
}

object PartiallyMappedCrossover extends Recombination {
  def recombine(parentA: Individual, parentB: Individual): Individual = {
    require(parentA.decoder == parentB.decoder, 
      "Genotype decoders have to be equal")
    require(parentA.genotype.length == parentB.genotype.length, 
      "Length of genotype A has to be equal to the length of genotype B")
    require(parentA.qualityFunction == parentB.qualityFunction, 
      "Quality functions have to be equal")
    val childGenotype = Array.ofDim[Item](parentA.genotype.length)
    val parentAGenotype = parentA.genotype.toArray
    val map = parentA.genotype.zip(parentB.genotype).toMap
    val u1 = Random.nextInt(parentA.genotype.length - 1 ) + 1
    val u2 = Random.nextInt(parentA.genotype.length - 1 ) + 1
    val range = if (u1 > u2) u2 to u1 else u1 to u2
    val slice = parentB.genotype.slice(range.start, range.end + 1) 
    var used = slice.toSet
    slice.copyToArray(childGenotype, range.start, slice.length)
    for (i <- (0 until parentA.genotype.length).diff(range) ) {
      var x = parentAGenotype(i)
      while (used.contains(x)) x = map(x)
      childGenotype(i) = x
      used = used + x
    }
    new Individual(childGenotype.toList, parentA.decoder, parentA.qualityFunction)
  }
}
