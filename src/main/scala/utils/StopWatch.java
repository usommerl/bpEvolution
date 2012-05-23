public class StopWatch {

	private final Precision precision;
	private long start = 0;
	
	public StopWatch() {
		this.precision = Precision.NANOSECONDS;
	}
	
	public StopWatch(Precision precision){
		this.precision = precision;
	}
	
	
	public void start(){
		start = System.nanoTime();
	}
	
	public long stop() {
		return (System.nanoTime()-start)/precision.nanoSecondsConversionQuotient;
	}
	
	public enum Precision {
		SECONDS (1000000000),
		MILLISECONDS(1000000),
		NANOSECONDS(1);
		
		public final long nanoSecondsConversionQuotient;
		Precision(long nanoSecondsConversionQuotient) {
			this.nanoSecondsConversionQuotient = nanoSecondsConversionQuotient;
		}
	}
}
