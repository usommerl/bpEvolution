class Phenotype(bins: List[Bin]) extends Traversable[Bin] {
  def foreach[U](f: (Bin) ⇒ U): Unit = bins.foreach(f(_))
}

object Phenotype {
  implicit def binListToPhenotype(bins: List[Bin]): Phenotype = new Phenotype(bins)
}
