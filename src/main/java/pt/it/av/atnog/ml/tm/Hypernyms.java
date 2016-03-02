package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.ml.tm.StopWords.BlacklistStopWords;
import pt.it.av.atnog.ml.tm.StopWords.EnglishStopWords;
import pt.it.av.atnog.ml.tm.StopWords.StopWords;
import pt.it.av.atnog.ml.tm.lexicalPattern.LexicalPattern;
import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.PorterStemmer;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;
import pt.it.av.atnog.ml.tm.tokenizer.TextTokenizer;
import pt.it.av.atnog.ml.tm.tokenizer.Tokenizer;
import pt.it.av.atnog.utils.MathUtils;
import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.StringUtils;
import pt.it.av.atnog.utils.parallel.Pipeline;
import pt.it.av.atnog.utils.parallel.Stop;
import pt.it.av.atnog.utils.structures.tuple.Pair;
import pt.it.av.atnog.utils.ws.search.SearchEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by mantunes on 7/28/15.
 */
public class Hypernyms {
    private static final int MAX = 5, N = 2, NN = 7, MIN_SIZE = 100;
    private final SearchEngine s;
    private final List<LexicalPattern> patterns;

    public Hypernyms(SearchEngine s, List<LexicalPattern> patterns) {
        this.s = s;
        this.patterns = patterns;
    }

    public NGram hypernym(NGram term) {
        NGram t = term.toLowerCase();
        Map<NGram, Count> m = new HashMap();
        Parameters param = new Parameters();
        Stemmer stemmer = new PorterStemmer();

        int min = 3, max = 7;
        Tokenizer tokenizer = new TextTokenizer();
        List<String> sw = new EnglishStopWords().stopWords();

        for (LexicalPattern p : patterns) {
            p.ngram(t);
            List<String> blacklist = p.blacklist();
            Pipeline pipeline = new Pipeline();

            // Setences
            pipeline.addLast((Object o, List<Object> l) -> {
                String input = (String) o;
                Iterator<String> setences = StringUtils.splitSetences(input);
                while(setences.hasNext())
                    l.add(setences.next());
            });

            // Clean the text  and convert it to tokens
            pipeline.addLast((Object o, List<Object> l) -> {
                String setence = (String) o;
                List<String> tokens = tokenizer.tokenize(setence);
                l.add(tokens);
            });

            // Remove stop words and words that are too small or too big
            pipeline.addLast((Object o, List<Object> l) -> {
                List<String> tokens = (List<String>) o;

                tokens.removeIf(x -> Collections.binarySearch(sw, x) >= 0);
                tokens.removeIf(x -> x.length() < min || x.length() > max);
                if (tokens.size() > 0)
                        l.add(tokens);
            });

            // Use Lexical Pattern p to extract relevant candidate Classe c
            pipeline.addLast((Object o, List<Object> l) -> {
                List<String> tokens = (List<String>) o;
                List<NGram> c = p.extract(tokens, N);
                l.add(c);
            });

            // Count candidate Classe c
            pipeline.addLast((Object o, List<Object> l) -> {
                List<NGram> candidates = (List<NGram>) o;
                for(NGram c : candidates) {
                    NGram stemmed = stemmer.stem(c);
                    if (!m.containsKey(stemmed)) {
                        m.put(stemmed, new Count());
                        //if(stemmed.size() == 1)
                            param.vocabulary += 1.0;
                    }
                    m.get(stemmed).rawFreq += 1;
                }
                param.partitions += 1.0;
            });

            BlockingQueue<Object> source = pipeline.source(),
                    sink = pipeline.sink();
            List<SearchEngine.Result> corpus = s.search(term + " is a");
            pipeline.start();
            int size = corpus.size() > MIN_SIZE ? (int) (corpus.size() * 0.3) : corpus.size();
            System.err.println("Corpus size (" + corpus.size() + ";" + size + ")");

            for (int i = 0; i < size; i++)
                sink.add(corpus.get(i));

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
        }

        // System.out.println(PrintUtils.map(m));
        //p.vocabulary = (int)(p.vocabulary / NN);
        //p.partitions *= NN;
        List<Pair<NGram, Double>> v = map2Vector(m, param);
        Comparator<Pair<NGram, Double>> c = (Pair<NGram, Double> a, Pair<NGram, Double> b) -> (Double.compare(a.b, b.b));
        Collections.sort(v, c);
        printStats(v, m, param, 10);
        System.out.println("Vector: " + PrintUtils.list(v));
        if(!v.isEmpty() && v.get(0).b < 0.05)
            return v.get(0).a;
        else
            return null;
    }

    private void printStats(List<Pair<NGram, Double>> v, Map<NGram, Count> m, Parameters p, int k) {
        int total = k < v.size() ? k : v.size();
        for (int i = 0; i < total; i++)
            System.err.println(v.get(i).a + " (" + m.get(v.get(i).a).rawFreq + "; " + p.partitions + "; " + p.vocabulary + ") -> Pr = " + probs(m.get(v.get(i).a).rawFreq,p.partitions,p.vocabulary));
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
            profile.add(new Pair<>(entry.getKey(), probs(entry.getValue().rawFreq, p.partitions, p.vocabulary)));
            //profile.add(new Pair<>(entry.getKey(), new Double(entry.getValue().rawFreq)));
        }
        return profile;
    }

    private double probs(int freq, int partitions, int vocabulary) {
        double pr = 0.0;
        for(int i = 0; i < freq; i++)
            pr += tprobs(i,partitions,vocabulary);
        return 1.0 - pr;
    }

    private double tprobs(int freq, int partitions, int vocabulary) {
        //System.err.println("("+freq+"; "+partitions+"; "+vocabulary+")");
        BigDecimal C = new BigDecimal(MathUtils.combination(partitions, freq));
        //System.err.println("C = "+C);

        BigDecimal bd1 = new BigDecimal(vocabulary-1);
        bd1 = bd1.pow(partitions-freq);
        //System.err.println("(V-1)^(P-F) = "+bd1);

        BigDecimal bd2 = new BigDecimal(vocabulary);
        bd2 = bd2.pow(partitions);
        //System.err.println("V^P = "+bd2);

        bd1 = bd1.divide(bd2, 100, RoundingMode.HALF_UP);
        //System.err.println("(V-1)^(P-F) / V^P = "+bd1);

        double pr = C.multiply(bd1).doubleValue();
        //System.err.println("Pr = "+pr);
        return pr;
    }

    private class Parameters {
        public int partitions, vocabulary;
    }

    private class Count {
        public int rawFreq = 0;
    }
}
