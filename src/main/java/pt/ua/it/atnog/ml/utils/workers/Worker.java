package pt.ua.it.atnog.ml.utils.workers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Worker implements Runnable {
	private final AtomicBoolean running;
	private BlockingQueue<Parallel> in, out;
	private Thread thread;

	public Worker() {
		this.running = new AtomicBoolean(false);
	}

	public void start(BlockingQueue<Parallel> in, BlockingQueue<Parallel> out) {
		if (!running.getAndSet(true)) {
			this.in = in;
			this.out = out;
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
		boolean done = false;
		while (!done) {
			try {
				Parallel pIn = in.take();
				if (pIn.endOfStream())
					done = true;
				else {
					Parallel pOut = process(pIn);
					out.put(pOut);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public abstract Parallel process(Parallel in);

	public abstract Worker clone();
}
