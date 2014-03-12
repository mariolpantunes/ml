package pt.ua.it.atnog.ml.optimization.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import pt.ua.it.atnog.ml.optimization.genetic.selection.Selection;
import pt.ua.it.atnog.ml.optimization.genetic.termination.Termination;

public class GPGA {
	public static List<Chromosome> optimize(List<Chromosome> population,
			Selection selection, Termination termination) {

		Logger logger = Logger.getLogger("GPGA");
		logger.setUseParentHandlers(false);
		SimpleFormater formatter = new SimpleFormater();
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(formatter);
		logger.addHandler(handler);

		WorkersPoll workers = new WorkersPoll(4);

		workers.runParallel(population);
		workers.done();
		
		List<Chromosome> offspring = new ArrayList<Chromosome>(population.size());
		List<Chromosome> newGeneration = new ArrayList<Chromosome>(population.size());

		while (!termination.termination(population)) {
			selection.select(population, offspring);
			workers.runParallel(offspring);
			workers.done();
			
			int steadyState = population.size() - offspring.size();
			
			if (steadyState > 0) {
				Collections.sort(population);
				newGeneration.addAll(offspring);
				for(int i = 0; i < steadyState; i++)
					newGeneration.add(population.get(i));
			} else {
				newGeneration.addAll(offspring);
			}

			population.clear();
			population.addAll(newGeneration);
			offspring.clear();
			newGeneration.clear();
		}
		return population;
	}

	public static class SimpleFormater extends Formatter {
		public String format(LogRecord record) {
			StringBuilder builder = new StringBuilder();
			builder.append("[").append(record.getLevel()).append("] ");
			builder.append(formatMessage(record));
			builder.append("\n");
			return builder.toString();
		}

		public String getHead(Handler h) {
			return super.getHead(h);
		}

		public String getTail(Handler h) {
			return super.getTail(h);
		}
	}
}
