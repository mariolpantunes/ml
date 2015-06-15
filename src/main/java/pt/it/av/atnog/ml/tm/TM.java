package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.parallel.Pipeline;
import pt.it.av.atnog.utils.parallel.Stop;
import pt.it.av.atnog.utils.structures.tuple.Pair;
import pt.it.av.atnog.utils.ws.search.SearchEngine;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by mantunes on 3/30/15.
 */
//TODO: STEM term
public class TM {

    public static DP learnDP(NGram term, SearchEngine se, List<String> stopWords) {
        return learnDP(term, se, new StandartTPP(3, 12, stopWords, 7, 3), new ElbowOptmizer(15));
    }

    public static DP learnDP(NGram term, SearchEngine se, TPPipeline tpp, DPOptimizer o) {
        Map<NGram, NGramStats> m = new HashMap<>();
        Pipeline p = tpp.build(term, m);
        BlockingQueue<Object> source = p.source(), sink = p.sink();
        List<String> corpus = se.snippets("what is "+term.toString());
        p.start();

        for (String s : corpus) {
            //System.out.println(s);
            sink.add(s);
        }
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

    private static List<Pair<NGram, Double>> map2DP(Map<NGram, NGramStats> m, int n) {
        System.out.println("N: "+n);
        List<Pair<NGram, Double>> profile = new ArrayList<>();
        Iterator<Map.Entry<NGram, NGramStats>> iter = m.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<NGram, NGramStats> entry = iter.next();
            NGramStats stats = entry.getValue();
            double tf = Math.log(1.0 + stats.rawFrequency),
                   idf = Math.log(n / (1.0 + stats.documentFrequency));
            System.out.println("NGram: "+entry.getKey()+" "+stats+" -> "+tf*idf);
            profile.add(new Pair<>(entry.getKey(), tf*idf));
        }
        return profile;
    }
}
