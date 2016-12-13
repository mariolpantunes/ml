package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.ml.tm.StopWords.StopWords;
import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;
import pt.it.av.atnog.ml.tm.tokenizer.Tokenizer;
import pt.it.av.atnog.utils.StringUtils;
import pt.it.av.atnog.utils.parallel.Pipeline;
import pt.it.av.atnog.utils.structures.CircularQueue;
import pt.it.av.atnog.utils.structures.mutableNumber.MutableDouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Not sure about this class. For now holds text mining useful functions.
 * Should be divided into several classes that better express the functionality
 */
public class TM {
    /*
    private static List<Pair<NGram, Double>> map2DP(Map<NGram, Count> m, int n) {
        List<Pair<NGram, Double>> profile = new ArrayList<>();
        Iterator<Map.Entry<NGram, Count>> iter = m.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<NGram, Count> entry = iter.next();
            Count stats = entry.getValue();
            double tf = Math.log(1.0 + stats.rawFreq),
                   idf = Math.log(n / (1.0 + stats.invFreq));
            profile.add(new Pair<>(entry.getKey(), tf*idf));
        }
        return profile;
    }*/

    /**
     * Returns the possible ngrams (until n) from the tokens.
     *
     * @param tokens list of tokens
     * @param n      maximum number of tokens in a ngram
     * @return list of ngrams extracted from the tokens
     */
    public static List<NGram> extractNGrams(List<String> tokens, int n) {
        List<NGram> rv = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < tokens.size() - i + 1; j++) {
                String buffer[] = new String[i];
                for (int k = 0; k < i; k++)
                    buffer[k] = tokens.get(j + k);
                rv.add(new NGram(buffer));
            }
        }
        return rv;
    }

    /**
     * Returns the possible ngrams (until n) from the tokens.
     *
     * @param tokens array of tokens
     * @param begin  the index of the first element (inclusive)
     * @param end    the index of the last element (exclusive)
     * @param n      maximum number of tokens in a ngram
     * @return list of ngrams extracted from the tokens
     */
    public static List<NGram> extractNGrams(String[] tokens, int begin, int end, int n) {
        List<NGram> rv = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            for (int j = begin; j < end - i + 1; j++) {
                String buffer[] = new String[i];
                for (int k = 0; k < i; k++)
                    buffer[k] = tokens[j + k];
                rv.add(new NGram(buffer));
            }
        }
        return rv;
    }

    /**
     * Returns a Pipeline ready to process text and extract the neighborhood of some term.
     *
     * @param term the term used to extract the neighborhood
     * @param t    tokenizer to convert text into valid tokens
     * @param sw   stop words filter, used to remove unwanted tokens
     * @param min  minimal size of token
     * @param max  maximal size of token
     * @param n    size of the neighborhood
     * @param nn   size of the extracted n-grams
     * @param s    Stemmer used to match tokens with the term
     * @param m    Map used to store the term's neighborhood
     * @return a Pipeline ready to process text and find the neighborhood.
     * @see pt.it.av.atnog.utils.parallel.Pipeline
     * @see pt.it.av.atnog.ml.tm.tokenizer.Tokenizer
     * @see pt.it.av.atnog.ml.tm.StopWords.StopWords
     * @see pt.it.av.atnog.ml.tm.stemmer.Stemmer
     * @see java.util.Map
     */
    public static Pipeline DPPipeline(NGram term, Tokenizer t, StopWords sw, int min, int max,
                                      int n, int nn, Stemmer s, Map<NGram, MutableDouble> m) {
        Pipeline pipeline = new Pipeline();
        CircularQueue<String> q = new CircularQueue<>(n * 2 + term.size());
        NGram stemm = s.stem(term);
        String array[] = new String[term.size()];

        // Setences
        pipeline.addLast((Object o, List<Object> l) -> {
            String input = (String) o;
            Iterator<String> setences = StringUtils.splitSetences(input);
            while (setences.hasNext())
                l.add(setences.next());
        });

        // Clean the text  and convert it to tokens
        pipeline.addLast((Object o, List<Object> l) -> {
            String setence = (String) o;
            Iterator<String> tokens = t.tokenizeIt(setence);
            while (tokens.hasNext())
                l.add(tokens.next());
        });

        // Remove stop words and words that are too small or too big
        pipeline.addLast((Object o, List<Object> l) -> {
            String token = (String) o;
            if (!sw.isStopWord(token)) {
                if (token.length() >= min && token.length() <= max)
                    l.add(token);
            }
        });

        // Extract the neighborhoods of the token
        pipeline.addLast((Object o, List<Object> l) -> {
            String token = (String) o;
            q.add(token);
            if (q.isFull()) {
                q.middle(array);
                //System.err.print("Array: "+ PrintUtils.array(array));
                if (stemm.equals(s.stem(array))) {
                    //System.err.print(" -> Match");
                    String[] buffer = q.toArray(new String[n * 2 + term.size()]);
                    l.add(TM.extractNGrams(buffer, 0, n, nn));
                    //System.err.print(" "+PrintUtils.list(TM.extractNGrams(buffer, 0, n, nn)));
                    l.add(TM.extractNGrams(buffer, n + term.size(), n * 2 + term.size(), nn));
                    //System.err.println(" "+PrintUtils.list(TM.extractNGrams(buffer, n + term.size(), n * 2 + term.size(), nn)));
                }
            }
        });

        // Count Raw Frequency
        pipeline.addLast((Object o, List<Object> l) -> {
            List<NGram> candidates = (List<NGram>) o;
            for (NGram ngram : candidates) {
                if (!m.containsKey(ngram))
                    m.put(ngram, new MutableDouble());
                m.get(ngram).increment();
            }
        });

        return pipeline;
    }
}