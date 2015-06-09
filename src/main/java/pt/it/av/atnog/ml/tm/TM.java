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
public class TM {
    /*public static double tdp_snippet_stemmer(String term1, String term2, List<String> stopWords, int minWord, int maxWord, int k, SearchEngine engine) {
        Locale locale = new Locale("en", "US");
        Stemmer stemmer = new Stemmer();
        //Stemm terms
        String stemmt1 = stem(stemmer, term1), stemmt2 = stem(stemmer, term2);
        //System.out.println("Gathering corpus");
        List<String> corpusT1 = engine.snippets(term1),
                corpusT2 = engine.snippets(term2);//,corpusT1T2 = engine.snippets(term1 + " " + term2);
        Pipeline pipeline = new Pipeline();
        BlockingQueue<Object> source = pipeline.source(), sink = pipeline.sink();
        //System.out.println("Process corpus");
        buildBeginPipeline(pipeline, locale, stopWords, minWord, maxWord);
        stemmerPipeline(pipeline, stemmer);
        Map<String, Integer> m1 = new HashMap<>(), m2 = new HashMap<>();
        dpPipeline(pipeline, stemmt1, stemmt2, k, m1, m2);

        pipeline.start();
        for (String s : corpusT1)
            sink.add(s);
        for (String s : corpusT2)
            sink.add(s);
        //for (String s : corpusT1T2)
        //    sink.add(s);
        try {
            pipeline.join();
            //Consume elements in pipeline result...
            boolean done = false;
            while (!source.isEmpty()) {
                Object o = source.take();
                if ((o instanceof Stop))
                    done = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("Term = " + term1);
        //Utils.printMap(m1);
        //System.out.println("Term = " + term2);
        //Utils.printMap(m2);
        DP dp1 = new DP(term1, map2DP(m1)), dp2 = new DP(term2, map2DP(m2));
        //System.out.println(dp1);
        //System.out.println(dp2);
        //dp1.optimize_elbow();
        //dp2.optimize_elbow();
        return dp1.similarity(dp2);
    }

    public static double tdp_snippet(String term1, String term2, List<String> stopWords, int minWord, int maxWord, int k, SearchEngine engine) {
        Locale locale = new Locale("en", "US");
        //System.out.println("Gathering corpus");
        List<String> corpusT1 = engine.snippets(term1),
                corpusT2 = engine.snippets(term2);//,corpusT1T2 = engine.snippets(term1 + " " + term2);
        Pipeline pipeline = new Pipeline();
        BlockingQueue<Object> source = pipeline.source(), sink = pipeline.sink();
        //System.out.println("Process corpus");
        buildBeginPipeline(pipeline, locale, stopWords, minWord, maxWord);
        Map<String, Integer> m1 = new HashMap<>(), m2 = new HashMap<>();
        dpPipeline(pipeline, term1, term2, k, m1, m2);
        pipeline.start();
        for (String s : corpusT1)
            sink.add(s);
        for (String s : corpusT2)
            sink.add(s);
        //for (String s : corpusT1T2)
        //    sink.add(s);
        try {
            pipeline.join();
            //Consume elements in pipeline result...
            boolean done = false;
            while (!source.isEmpty()) {
                Object o = source.take();
                if ((o instanceof Stop))
                    done = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DP dp1 = new DP(term1, map2DP(m1)), dp2 = new DP(term2, map2DP(m2));
        //System.out.println(dp1);
        //System.out.println(dp2);
        //dp1.optimize_elbow();
        //dp2.optimize_elbow();
        return dp1.similarity(dp2);
    }

    public static double tdp_search_stemmer(String term1, String term2, List<String> stopWords, int minWord, int maxWord, int k, SearchEngine engine) {
        Locale locale = new Locale("en", "US");
        Stemmer stemmer = new Stemmer();
        //Stemm terms
        String stemmt1 = stem(stemmer, term1), stemmt2 = stem(stemmer, term2);
        //System.out.println("Gathering corpus");
        List<String> corpusT1 = engine.search(term1),
                corpusT2 = engine.search(term2);//,corpusT1T2 = engine.search(term1 + " " + term2);
        Pipeline pipeline = new Pipeline();
        BlockingQueue<Object> source = pipeline.source(), sink = pipeline.sink();
        //System.out.println("Process corpus");
        buildBeginPipeline(pipeline, locale, stopWords, minWord, maxWord);
        stemmerPipeline(pipeline, stemmer);
        Map<String, Integer> m1 = new HashMap<>(), m2 = new HashMap<>();
        dpPipeline(pipeline, stemmt1, stemmt2, k, m1, m2);
        pipeline.start();
        for (String s : corpusT1)
            sink.add(s);
        for (String s : corpusT2)
            sink.add(s);
        //for (String s : corpusT1T2)
        //    sink.add(s);
        try {
            pipeline.join();
            //Consume elements in pipeline result...
            boolean done = false;
            while (!source.isEmpty()) {
                Object o = source.take();
                if ((o instanceof Stop))
                    done = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("Term = " + term1);
        //Utils.printMap(m1);
        //System.out.println("Term = " + term2);
        //Utils.printMap(m2);
        DP dp1 = new DP(term1, map2DP(m1)), dp2 = new DP(term2, map2DP(m2));
        //System.out.println(dp1);
        //System.out.println(dp2);
        return dp1.similarity(dp2);
    }

    public static double tdp_search(String term1, String term2, List<String> stopWords, int minWord, int maxWord, int k, SearchEngine engine) {
        Locale locale = new Locale("en", "US");
        //System.out.println("Gathering corpus");
        List<String> corpusT1 = engine.search(term1),
                corpusT2 = engine.search(term2);//,corpusT1T2 = engine.search(term1 + " " + term2);
        Pipeline pipeline = new Pipeline();
        BlockingQueue<Object> source = pipeline.source(), sink = pipeline.sink();
        //System.out.println("Process corpus");
        buildBeginPipeline(pipeline, locale, stopWords, minWord, maxWord);
        Map<String, Integer> m1 = new HashMap<>(), m2 = new HashMap<>();
        dpPipeline(pipeline, term1, term2, k, m1, m2);
        pipeline.start();
        for (String s : corpusT1)
            sink.add(s);
        for (String s : corpusT2)
            sink.add(s);
        //for (String s : corpusT1T2)
        //    sink.add(s);
        try {
            pipeline.join();
            //Consume elements in pipeline result...
            boolean done = false;
            while (!source.isEmpty()) {
                Object o = source.take();
                if ((o instanceof Stop))
                    done = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DP dp1 = new DP(term1, map2DP(m1)), dp2 = new DP(term2, map2DP(m2));
        //System.out.println(dp1);
        //System.out.println(dp2);
        dp1.optimize_elbow();
        dp2.optimize_elbow();
        return dp1.similarity(dp2);
    }

    private static void findCloseTokens(List<String> tokens, int pos, int k, Map<String, Integer> m) {
        int min = (pos - k >= 0) ? pos - k : 0, max = (pos + k + 1 <= tokens.size()) ? pos + k + 1 : tokens.size();
        for (int i = min; i < pos; i++)
            updateValue(m, tokens.get(i));
        for (int i = pos + 1; i < max; i++)
            updateValue(m, tokens.get(i));
    }

    private static void updateValue(Map<String, Integer> m, String key) {
        int v = 1;
        if (m.containsKey(key))
            v += m.get(key);
        m.put(key, v);
    }

    private static List<Pair<String, Double>> map2DP(Map<String, Integer> map) {
        List<Pair<String, Double>> p = new ArrayList<>();
        Iterator<Map.Entry<String, Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
            double v = entry.getValue();
            p.add(new Pair<>(entry.getKey(), v));
        }
        return p;
    }

    private static void buildBeginPipeline(Pipeline pipeline, Locale locale, List<String> stopWords, int minWord, int maxWord) {
        //Divide intput into setences.
        pipeline.add((Object o, List<Object> l) -> {
            String input = (String) o;
            l.addAll(Tokenizer.setences(input, locale));
        });

        //Clean setence and convert it to tokens
        pipeline.add((Object o, List<Object> l) -> {
            String input = (String) o;
            List<String> tokens = Tokenizer.text(input, locale);
            //System.out.println("Input: "+input);
            //Utils.printList(tokens);
            //System.out.println();
            if (!tokens.isEmpty())
                l.add(tokens);
        });

        //Remove stop words
        pipeline.add((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            List<String> found = tokens.parallelStream()
                    .filter(w -> Collections.binarySearch(stopWords, w) >= 0)
                    .collect(Collectors.toList());
            tokens.removeAll(found);
            if (tokens.size() > 0)
                l.add(tokens);
        });

        //Remove tokens that are too small or too big
        pipeline.add((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            List<String> found = tokens.parallelStream()
                    .filter(w -> w.length() < minWord || w.length() > maxWord)
                    .collect(Collectors.toList());
            tokens.removeAll(found);
            if (tokens.size() > 0)
                l.add(tokens);
        });
    }

    private static void stemmerPipeline(Pipeline pipeline, Stemmer stemmer) {
        // Stemmer
        pipeline.add((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o, sl = new ArrayList<String>();
            for (String s : tokens)
                sl.add(stem(stemmer, s));
            l.add(sl);
            //Utils.printList(tokens);
            //Utils.printList(sl);
        });
    }

    private static void dpPipeline(Pipeline pipeline, String term1, String term2, int k, Map<String, Integer> m1, Map<String, Integer> m2) {
        //DP extraction
        pipeline.add((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            for (int i = 0, t = tokens.size(); i < t; i++) {
                String token = tokens.get(i);
                if (token.equals(term1)) {
                    findCloseTokens(tokens, i, k, m1);
                } else if (token.equals(term2)) {
                    findCloseTokens(tokens, i, k, m2);
                }
            }
        });
    }

    private static String stem(Stemmer stemmer, String word) {
        char st[] = word.toCharArray();
        stemmer.add(st, st.length);
        stemmer.stem();
        return stemmer.toString();
    }*/
}
