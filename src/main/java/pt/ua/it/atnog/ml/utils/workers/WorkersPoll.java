package pt.ua.it.atnog.ml.utils.workers;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WorkersPoll {
	private final BlockingQueue<Parallel> in;
	private final BlockingQueue<Parallel> out;
	private final Worker[] workers;
	private final Sink sink;
	private final Source source;

	public WorkersPoll(Worker worker, int nWorkers) {
		this(new Sink(), new Source(), worker, nWorkers);
	}
	
	public WorkersPoll(Sink sink, Source source, Worker worker, int nWorkers) {
		in = new ArrayBlockingQueue<Parallel>(nWorkers);
		out = new ArrayBlockingQueue<Parallel>(nWorkers);

		this.sink = sink;
		this.source = source;

		workers = new Worker[nWorkers];
		for (int i = 0; i < nWorkers; i++) {
			Worker w = worker.clone();
			w.start(in, out);
			workers[i] = w;
		}

		this.sink.connect(in);
		this.source.connect(out);
	}

	public void runParallel(List<? extends Parallel> tasks) {
		sink.start(tasks);
		source.start(tasks.size());
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
		try {
			sink.join();
			source.join();
			for (int i = 0; i < workers.length; i++)
				in.put(new Parallel(true));

			for (int i = 0; i < workers.length; i++)
				workers[i].join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
