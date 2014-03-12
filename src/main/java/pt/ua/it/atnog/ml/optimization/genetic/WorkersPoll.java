package pt.ua.it.atnog.ml.optimization.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

public class WorkersPoll {
	private final BlockingQueue<Chromosome> in;
	private final BlockingQueue<Boolean> out;
	private final List<Thread> workers;
	private Thread sink, source;
	private Logger logger;

	public WorkersPoll(int nWorkers) {
		logger = Logger.getLogger("GPGA");
		in = new SynchronousQueue<Chromosome>();
		out = new ArrayBlockingQueue<Boolean>(nWorkers);
		workers = new ArrayList<Thread>(nWorkers);
		for (int i = 0; i < nWorkers; i++) {
			Thread t = new Thread(new ComputeFitness(in, out));
			workers.add(t);
			t.start();
		}
	}

	public void runParallel(List<Chromosome> population) {
		logger.info("Run Parallel");

		sink = new Thread(new Sink(population, in));
		source = new Thread(new Source(population.size(), out));

		sink.start();
		source.start();
	}

	public void done() {
		try {
			sink.join();
			source.join();
		} catch (Exception e) {

		}
	}

	public class ComputeFitness implements Runnable {
		private BlockingQueue<Chromosome> in;
		private BlockingQueue<Boolean> out;
		private boolean running;

		public ComputeFitness(BlockingQueue<Chromosome> in,
				BlockingQueue<Boolean> out) {
			this.in = in;
			this.out = out;
			running = true;
		}

		public void run() {
			while (running) {
				try {
					Chromosome c = in.take();
					c.computeFitness();
					out.put(true);
				} catch (Exception e) {
					logger.severe(e.toString());
				}
			}
		}

		public void done() {
			running = false;
		}
	}

	public class Sink implements Runnable {
		private BlockingQueue<Chromosome> in;
		private List<Chromosome> population;

		public Sink(List<Chromosome> population, BlockingQueue<Chromosome> in) {
			this.population = population;
			this.in = in;
		}

		public void run() {
			try {
				for (Chromosome c : population)
					in.put(c);
			} catch (Exception e) {
				logger.severe(e.toString());
			}
		}
	}

	public class Source implements Runnable {
		private BlockingQueue<Boolean> out;
		private int populationSize;

		public Source(int populationSize, BlockingQueue<Boolean> out) {
			this.populationSize = populationSize;
			this.out = out;
		}

		public void run() {
			int count = 0;
			while (count < populationSize) {
				try {
					out.take();
				} catch (InterruptedException e) {
					logger.severe(e.toString());
				}
				count++;
			}
		}
	}
}
