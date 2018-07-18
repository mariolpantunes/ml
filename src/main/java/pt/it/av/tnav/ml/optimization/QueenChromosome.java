package pt.it.av.tnav.ml.optimization;

import pt.it.av.tnav.ml.optimization.genetic.Chromosome;

import java.util.Random;

public class QueenChromosome extends Chromosome {
	private int[] genes;
    private int ChessBoradLenght;
	private Random random;

	public QueenChromosome(int ChessBoradLenght) {
        this.ChessBoradLenght = ChessBoradLenght;
        genes = new int[ChessBoradLenght];
		random =  new Random();
		for(int i = 0; i < ChessBoradLenght; i++)
			genes[i] = random.nextInt(ChessBoradLenght);
	}

	public void setGene(int index, Object gene) {
		genes[index] = (Integer)gene;
	}

	public Object getGene(int index) {
		return genes[index];
	}

	public int numberGenes() {
		return genes.length;
	}

	public String toString() {
		String rv = "Fitness = " + fitness() + " : ";

		for(int i = 0; i < numberGenes(); i++) {
			rv += "" + genes[i] + ";";
		}

		return rv;
	}

    public Chromosome crossover(Chromosome parent) {
            QueenChromosome child = new QueenChromosome(numberGenes());
            QueenChromosome parentConverted = (QueenChromosome)parent;
            for (int i = 0; i < this.numberGenes(); i++) {
                    boolean secondParent = random.nextFloat() >= 0.5;
                    if (secondParent) {
                            child.genes[i] = parentConverted.genes[i];
                    } else {
                            child.genes[i] = genes[i];
                    }
            }
            return child;
    }

    public void computeMutation() {
            boolean mutate = random.nextFloat() >= (1.0-(0.1));
            if (mutate) {
                    int index = random.nextInt(genes.length);
                    if(random.nextBoolean())
                    {
                        genes[index]++;
                        if(genes[index] >= genes.length)
                        {
                            genes[index] = 0;
                        }
                    } else {
                        genes[index]--;
                        if(genes[index] < 0)
                        {
                            genes[index] = genes.length - 1;
                        }
                    }
            } 
    }

	protected double computeFitness() {
        int cost = 0;

        // verifica as linhas
        for(int i = 0;  i < ChessBoradLenght-1; i++)
            for(int j = i+1; j< ChessBoradLenght; j++)
                if(genes[i] == genes[j])
                    cost++;

        // verifica as diagonais
        for(int i = 0;  i < ChessBoradLenght-1; i++)
            for(int j = i+1; j< ChessBoradLenght; j++)
                if(Math.abs(i-j) == Math.abs(genes[i] - genes[j]))
                    cost ++;

		return 1.0/(1.0 + cost);
	}
}
