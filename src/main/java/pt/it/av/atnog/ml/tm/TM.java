package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.tokenizer.TokenizerOld;
import pt.it.av.atnog.utils.parallel.Pipeline;
import pt.it.av.atnog.utils.parallel.Stop;
import pt.it.av.atnog.utils.structures.tuple.Pair;
import pt.it.av.atnog.utils.ws.search.SearchEngine;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by mantunes on 3/30/15.
 */
//TODO: STEM term
public class TM {

    /*public static DP learnDP(NGram term, SearchEngine se, List<String> stopWords) {
        return learnDP(term, se, new StandartTPP(3, 15, stopWords, 5, 3), new ElbowOptmizer(35));
    }

    public static DP learnDP(NGram term, SearchEngine se, List<String> stopWords, int min, int max, int k, int n) {
        return learnDP(term, se, new StandartTPP(min, max, stopWords, k, n), new ElbowOptmizer(35));
    }*/

    public static DP learnDP(NGram term, SearchEngine se, DPOptimizer o) {
        Map<NGram, Count> m = new HashMap<>();
        Pipeline p = standartTPP(new EnglishStopWords(), 3, 15);
        BlockingQueue<Object> source = p.source(), sink = p.sink();
        List<String> corpus = se.snippets(term.toString());
        p.start();

        for (String s : corpus)
            sink.add(s);

        try {
            p.join();
            boolean done = false;
            while (!source.isEmpty()) {
                Object ob = source.take();
                if ((ob instanceof Stop))
                    done = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Pair<NGram,Double>> profile = map2DP(m, corpus.size());
        profile = o.optimize(profile);

        return new DP(term, profile);
    }

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
    }

    public static List<NGram> extractNGrams(List<String> tokens, int idx, int n) {
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

    public static Pipeline standartTPP(StopWords sw, int min, int max) {
        return standartTPP(new Locale("en", "US"), sw, min, max);
    }

    public static Pipeline standartTPP(Locale locale, StopWords sw, int min, int max) {
        Pipeline pipeline = new Pipeline();
        List<String> stopWords = sw.stopWords();

        // Setences
        pipeline.addLast((Object o, List<Object> l) -> {
            String input = (String) o;
            List<String> tokens = TokenizerOld.sentences(input);
            if (!tokens.isEmpty())
                l.add(tokens);
        });

        // Clauses
        pipeline.addLast((Object o, List<Object> l) -> {
            List<String> setences = (List<String>) o;
            for(String s : setences) {
                List<String> tokens = TokenizerOld.clauses(s);
                if (!tokens.isEmpty())
                    l.add(tokens);
            }
        });

        // Clean the text  and convert it to tokens
        pipeline.addLast((Object o, List<Object> l) -> {
            List<String> clauses = (List<String>) o;
            for(String c : clauses) {
                List<String> tokens = TokenizerOld.text(c, locale);
                if (!tokens.isEmpty())
                    l.add(tokens);
            }
        });

        // Remove stop words
        pipeline.addLast((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            tokens.removeIf(x -> Collections.binarySearch(stopWords, x) >= 0);
            if (tokens.size() > 0)
                l.add(tokens);
        });

        // Remove tokens that are too small or too big
        pipeline.addLast((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            tokens.removeIf(x -> x.length() < min || x.length() > max);
            if (tokens.size() > 0)
                l.add(tokens);
        });

        return pipeline;
    }

    private class Count {
        public int rawFreq = 0, invFreq = 0;
    }
}
