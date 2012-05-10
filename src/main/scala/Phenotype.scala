class Phenotype(bins: List[Bin]) extends Traversable[Bin] with Iterable[Bin] {
  override def foreach[U](f: (Bin) ⇒ U): Unit = bins.foreach(f(_))
  def iterator: Iterator[Bin] = bins.iterator
}
