package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.ml.tm.StopWords.EnglishStopWords;
import pt.it.av.atnog.ml.tm.StopWords.StopWords;
import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.tokenizer.TextTokenizer;
import pt.it.av.atnog.ml.tm.tokenizer.Tokenizer;
import pt.it.av.atnog.utils.StringUtils;
import pt.it.av.atnog.utils.parallel.Pipeline;
import pt.it.av.atnog.utils.parallel.Stop;
import pt.it.av.atnog.utils.structures.tuple.Pair;
import pt.it.av.atnog.utils.ws.search.SearchEngine;

import java.util.*;
import java.util.concurrent.BlockingQueue;

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
     * @param tokens list of tokens
     * @param n maximum number of tokens in a ngram
     * @return list of ngrams extracted from the tokens
     */
    public static List<NGram> extractNGrams(List<String> tokens, int n) {
        List<NGram> rv = new ArrayList<>();
        for(int i = 1; i <= n; i++) {
            for (int j = 0; j < tokens.size() - i + 1; j++) {
                String buffer[] = new String[i];
                for(int k = 0; k < i; k++)
                    buffer[k] = tokens.get(j+k);
                rv.add(new NGram(buffer));
            }
        }
        return rv;
    }

    /**
     * Returns the possible ngrams (until n) from the tokens.
     * @param tokens array of tokens
     * @param begin the index of the first element (inclusive)
     * @param end the index of the last element (exclusive)
     * @param n maximum number of tokens in a ngram
     * @return list of ngrams extracted from the tokens
     */
    public static List<NGram> extractNGrams(String[] tokens, int begin, int end, int n) {
        List<NGram> rv = new ArrayList<>();
        for(int i = 1; i <= n; i++) {
            for (int j = begin; j < end - i + 1; j++) {
                String buffer[] = new String[i];
                for(int k = 0; k < i; k++)
                    buffer[k] = tokens[j+k];
                rv.add(new NGram(buffer));
            }
        }
        return rv;
    }
}