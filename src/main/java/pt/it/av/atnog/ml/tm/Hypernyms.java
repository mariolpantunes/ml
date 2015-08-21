package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.MathUtils;
import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.parallel.Pipeline;
import pt.it.av.atnog.utils.parallel.Stop;
import pt.it.av.atnog.utils.structures.tuple.Pair;
import pt.it.av.atnog.utils.ws.search.SearchEngine;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by mantunes on 7/28/15.
 */
public class Hypernyms {
    private static final int MAX = 5, N = 2, NN = 7;
    private final SearchEngine s;
    private List<String> blacklist = new ArrayList<>();

    public Hypernyms(SearchEngine s) {
        this.s = s;
        blacklist.add("are");
        blacklist.add("is");
    }

    public NGram hypernym(NGram term) {
        NGram t = term.toLowerCase();
        int n = t.size();
        String buffer[] = new String[t.size()];
        Parameters p = new Parameters();

        HashMap<NGram, Count> m = new HashMap();
        Pipeline pipeline = TM.standartTPP(new BlacklistStopWords(new EnglishStopWords(), blacklist), 2, 15);

        //
        pipeline.addLast((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            for (int i = 0, s = tokens.size() - n; i < s; i++) {
                for (int j = 0; j < n; j++)
                    buffer[j] = tokens.get(i + j);
                if (t.equals(buffer)) {
                    boolean done = false;
                    int total = i + n + MAX < tokens.size() ? i + n + MAX : tokens.size();
                    for (int j = i + n; j < total && !done; j++) {
                        if (tokens.get(j).equals("is") || tokens.get(j).equals("are")) {
                            //if (tokens.get(j).equals("is")) {
                            int size = j + 1 + NN < tokens.size() ? j + 1 + NN : tokens.size();
                            List<String> output = new ArrayList<String>(tokens.subList(j + 1, size));
                            output.removeIf(x -> Collections.binarySearch(blacklist, x) >= 0);
                            if (output.size() > 0)
                                l.add(output);
                            done = true;
                        }
                    }
                }
            }
        });

        //
        pipeline.addLast((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            boolean used[] = new boolean[N];
            for (int i = 0; i < tokens.size(); i++) {
                for (int j = 1; j <= N && i < tokens.size() - j + 1; j++) {
                    String ngramBuffer[] = new String[j];
                    for (int k = 0; k < j; k++)
                        ngramBuffer[k] = tokens.get(i + k);
                    if (!term.equals(ngramBuffer)) {
                        NGram ngram = new NGram(ngramBuffer);
                        if (!m.containsKey(ngram)) {
                            m.put(ngram, new Count());
                            p.vocabulary[ngram.size() - 1] += 1.0;
                        }
                        m.get(ngram).rawFreq += 1;
                    }
                }
            }
            p.partitions[0] += 1.0;
        });

        BlockingQueue<Object> source = pipeline.source(),
                sink = pipeline.sink();
        List<String> corpus1 = s.snippets(term + " is a");
        pipeline.start();

        for (String s : corpus1)
            sink.add(s);

        try {
            pipeline.join();
            boolean done = false;
            while (!source.isEmpty()) {
                Object ob = source.take();
                if ((ob instanceof Stop))
                    done = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println(PrintUtils.map(m));
        // p.vocabulary /= NN;
        List<Pair<NGram, Double>> vector = map2Vector(m, p);
        Comparator<Pair<NGram, Double>> c = (Pair<NGram, Double> a, Pair<NGram, Double> b) -> (Double.compare(a.b, b.b));
        Collections.sort(vector, c);
        System.out.println("Vector: " + PrintUtils.list(vector));
        System.out.println("Parameters (" + PrintUtils.array(p.vocabulary) + "/" + PrintUtils.array(p.partitions)+ ")");

        return null;
    }

    private int countNGram(Map<NGram, Count> m, int n) {
        int rv = 0;
        Iterator<Map.Entry<NGram, Count>> iter = m.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<NGram, Count> entry = iter.next();
            NGram ngram = entry.getKey();
            if (ngram.size() == n)
                rv += 1;
        }
        return rv;
    }

    private List<Pair<NGram, Double>> map2Vector(Map<NGram, Count> m, Parameters p) {
        List<Pair<NGram, Double>> profile = new ArrayList<>();
        Iterator<Map.Entry<NGram, Count>> iter = m.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<NGram, Count> entry = iter.next();
            System.err.print(entry.getKey()+" -> prob("+entry.getValue().rawFreq+"; "+p.partitions[entry.getKey().size() - 1]+"; "+p.vocabulary[entry.getKey().size()-1]+") = ");
            profile.add(new Pair<>(entry.getKey(), probs(entry.getValue().rawFreq,
                    p.partitions[0], p.vocabulary[entry.getKey().size()-1])));
        }
        return profile;
    }

    private double probs(int freq, double partitions, double vocabulary) {
        double C = MathUtils.combination(partitions, freq);
        double D = Math.pow(vocabulary, freq);
        System.err.println("C("+partitions+" "+freq+") = "+C+" D("+vocabulary+"^"+freq+") = "+D+" C/D = "+C/D);
        return C/D;
    }

    private class Parameters {
        public double partitions[] = new double[N], vocabulary[] = new double[N];
    }

    private class Count {
        public int rawFreq = 0;
    }
}
