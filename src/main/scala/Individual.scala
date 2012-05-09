import scala.math.pow

class Individual(val genotype: Genotype, val decoder: GenotypeDecoder, val qualityFunction: Phenotype => Double) extends Ordered[Individual]{
  
  lazy val phenotype: Phenotype = this.decoder.decode(genotype)
  lazy val quality = qualityFunction(this.phenotype)
  def compare(that: Individual) = this.quality.compare(that.quality)
  override def toString() = "[Individual quality="+this.quality+"]"
}

