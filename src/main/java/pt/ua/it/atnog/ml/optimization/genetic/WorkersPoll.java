package pt.ua.it.atnog.ml.optimization.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkersPoll {
	private final BlockingQueue<Chromosome> in;
	private final BlockingQueue<Object> out;
	private final List<ComputeFitness> workers;
	private Thread sink, source;

	public WorkersPoll(int nWorkers) {
		in = new SynchronousQueue<Chromosome>();
		out = new ArrayBlockingQueue<Object>(nWorkers);
		workers = new ArrayList<ComputeFitness>(nWorkers);
		for (int i = 0; i < nWorkers; i++) {
			ComputeFitness w = new ComputeFitness(in, out);
			w.start();
			workers.add(w);
		}
	}

	public void runParallel(List<? extends Chromosome> population) {
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
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void close() {
		List<Chromosome> population = new ArrayList<Chromosome>(workers.size());
		for (int i = 0; i < workers.size(); i++) {
			workers.get(i).stop();
			population.add(new DummyChromosome());
		}
		runParallel(population);
		done();
		for (ComputeFitness t : workers)
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
	}

	public class DummyChromosome extends Chromosome {
		protected double computeFitness() {
			return 0;
		}

		public Chromosome crossover(Chromosome parent) {
			return null;
		}

		public void computeMutation() {
		}
	}

	public class ComputeFitness implements Runnable {
		private BlockingQueue<? extends Chromosome> in;
		private BlockingQueue<Object> out;
		private Thread t;
		private AtomicBoolean running;

		public ComputeFitness(BlockingQueue<? extends Chromosome> in,
				BlockingQueue<Object> out) {
			this.in = in;
			this.out = out;
			running = new AtomicBoolean(false);
		}

		public void start() {
			if (!running.getAndSet(true)) {
				t = new Thread(this);
				t.start();
			}
		}

		public void stop() {
			if (running.get()) {
				running.set(false);
			}
		}

		public void join() throws InterruptedException {
			t.join();
		}

		public void run() {
			Object dummy = new Object();
			while (running.get()) {
				try {
					Chromosome c = in.take();
					c.fitness();
					out.put(dummy);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
	}

	public class Sink implements Runnable {
		private BlockingQueue<Chromosome> in;
		private List<? extends Chromosome> population;

		public Sink(List<? extends Chromosome> population,
				BlockingQueue<Chromosome> in) {
			this.population = population;
			this.in = in;
		}

		public void run() {
			try {
				for (Chromosome c : population)
					in.put(c);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public class Source implements Runnable {
		private BlockingQueue<Object> out;
		private int populationSize;

		public Source(int populationSize, BlockingQueue<Object> out) {
			this.populationSize = populationSize;
			this.out = out;
		}

		public void run() {
			int count = 0;
			while (count < populationSize) {
				try {
					out.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(0);
				}
				count++;
			}
		}
	}
}
