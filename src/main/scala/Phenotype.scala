class Phenotype(bins: List[Bin]) extends Traversable[Bin] {
  def foreach[U](f: (Bin) ⇒ U): Unit = bins.foreach(f(_))
}
