package pt.ua.it.atnog.ml.utils.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Filter implements Runnable {
	private AtomicBoolean running;
	private BlockingQueue<Event> in, out;
	private Thread thread;

	public Filter() {
		running = new AtomicBoolean(false);
	}

	public void connect(BlockingQueue<Event> in, BlockingQueue<Event> out) {
		if (!running.get()) {
			this.in = in;
			this.out = out;
		}
	}

	public void start() {
		if (!running.getAndSet(true)) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {
		boolean done = false;
		List<Event> eOut = new ArrayList<Event>();
		while (!done) {
			try {
				Event eIn = in.take();
				if (!eIn.endOfStream()) {
					processEvent(eIn, eOut);
					if (eOut.size() > 0) {
						for (Event e : eOut)
							out.put(e);
						eOut.clear();
					}
				} else {
					out.put(new Event(true));
					done = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public void stop() {
		in.add(new Event(true));
	}

	protected abstract void processEvent(Event eIn, List<Event> eOut);
}
