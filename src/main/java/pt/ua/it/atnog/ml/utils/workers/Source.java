package pt.ua.it.atnog.ml.utils.workers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Source implements Runnable {
	private final AtomicBoolean running;
	private BlockingQueue<Parallel> in;
	private int numberTasks;
	private Thread thread;

	public Source() {
		running = new AtomicBoolean(false);
	}

	public void connect(BlockingQueue<Parallel> in) {
		this.in = in;
	}

	public void start(int numberTasks) {
		if (!running.getAndSet(true)) {
			this.numberTasks = numberTasks;
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
		int size = 0;
		while (size < numberTasks) {
			try {
				in.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
			size++;
		}
	}
}
