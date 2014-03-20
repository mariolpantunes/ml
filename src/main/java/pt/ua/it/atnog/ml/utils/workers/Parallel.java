package pt.ua.it.atnog.ml.utils.workers;

public class Parallel {
	private boolean endOfStream;

	public Parallel(boolean endOfStream) {
		this.endOfStream = endOfStream;
	}

	public Parallel() {
		endOfStream = false;
	}

	public boolean endOfStream() {
		return endOfStream;
	}
}
