package pt.it.av.tnav.ml.optimization;

import pt.it.av.tnav.ml.optimization.genetic.Chromosome;

import java.util.Random;

public class DummyChromossome extends Chromosome {

    private int[] genes;
    private Random random;

    public DummyChromossome(int numberGenes) {
        genes = new int[numberGenes];
        random = new Random();
        for (int i = 0; i < numberGenes; i++) {
            genes[i] = random.nextInt(numberGenes);
        }
    }

    public void setGene(int index, Object gene) {
        genes[index] = (Integer) gene;
    }

    public Object getGene(int index) {
        return genes[index];
    }

    public int numberGenes() {
        return genes.length;
    }

    public String toString() {
        String rv = "Fitness = " + fitness() + " : ";

        for (int i = 0; i < numberGenes(); i++) {
            rv += "" + genes[i] + ";";
        }

        return rv;
    }

    public Chromosome crossover(Chromosome parent) {
        DummyChromossome child = new DummyChromossome(numberGenes());
        DummyChromossome parentConverted = (DummyChromossome) parent;

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
        int index = random.nextInt(genes.length);
        if (random.nextBoolean()) {
            genes[index]++;
            if (genes[index] >= genes.length)
                genes[index] = 0;

        } else {
            genes[index]--;
            if (genes[index] < 0)
                genes[index] = genes.length - 1;
        }
    }

    protected double computeFitness() {
        double sum = 0;
        for (int i = 0; i < genes.length; i++)
            sum += Math.pow(Math.abs(i - genes[i]), 2.0);
        return 1.0 / (sum + 1);
    }
}
