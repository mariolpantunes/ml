package pt.it.av.atnog.ml.utils.workers;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sink implements Runnable {
	private final AtomicBoolean running;
	private Thread thread;
	protected BlockingQueue<Parallel> out;
	protected List<? extends Parallel> elements;

	public Sink() {
		running = new AtomicBoolean(false);
	}

	public void connect(BlockingQueue<Parallel> out) {
		this.out = out;
	}

	public void start(List<? extends Parallel> elements) {
		if (!running.getAndSet(true)) {
			this.elements = elements;
			thread = new Thread(this);
			thread.start();
		}
	}

	public void join() throws InterruptedException {
		if (running.get()) {
			try {
				thread.join();
				running.set(false);
			} catch (InterruptedException e) {
				throw e;
			} finally {
				running.set(false);
			}
		}
	}

	public void run() {
		try {
			for (Parallel p : elements)
				out.put(p);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
