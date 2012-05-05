import scala.util.Random

trait Mutation {
  def mutate(individual: Individual): Individual

  protected def cloneGenotype(genotype: Genotype): (Array[Item], Array[Item]) = {
    val a = genotype.toArray; val b = a.clone(); (a,b)
  }
  protected def generateRandomNumbers(max: Int): (Int,Int) = (Random.nextInt(max), Random.nextInt(max))
}

object ShiftingMutation extends Mutation {
  def mutate(individual: Individual): Individual = {
    val (a,b) = cloneGenotype(individual.genotype)
    val (u1,u2) = generateRandomNumbers(a.size)
    b(u2) = a(u1)
    if (u1 > u2) 
      for (j <- u2 until u1) b(j+1) = a(j)
    else 
      for (j <- u1+1 to u2) b(j-1) = a(j)
    new Individual(b.toList, individual.decoder)
  }
}

object ExchangeMutation extends Mutation {
  def mutate(individual: Individual): Individual = {
    val (a,b) = cloneGenotype(individual.genotype)
    val (u1,u2) = generateRandomNumbers(a.size)
    b(u1) = a(u2)
    b(u2) = a(u1) 
    new Individual(b.toList, individual.decoder)
  }
}

object InversionMutation extends Mutation {
  def mutate(individual: Individual): Individual = {
    val (a,b) = cloneGenotype(individual.genotype)
    var (u1,u2) = generateRandomNumbers(a.size)
    if (u1 > u2) {val tmp = u1; u1 = u2; u2 = tmp}
    for (j <- u1 to u2) b(u2+u1-j) = a(j)
    new Individual(b.toList, individual.decoder)
  }
}
