import scala.math.pow

class Individual(val genotype: List[Item], val decoder: GenotypeDecoder) extends Ordered[Individual]{

  lazy val quality = this.phenotype.size
  lazy val phenotype: List[Bin] = this.decoder.decode(genotype)
  private val timeOfBirth = System.currentTimeMillis
  def age = System.currentTimeMillis - timeOfBirth
  def compare(that: Individual) = this.quality.compare(that.quality)
  override def toString() = "[Individual quality="+quality+" age="+this.age+"]"
}

