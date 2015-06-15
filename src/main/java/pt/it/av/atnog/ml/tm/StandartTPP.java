package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.parallel.Pipeline;

import java.util.*;

/**
 * Created by mantunes on 6/15/15.
 */
public class StandartTPP implements TPPipeline{
    private final List<String> stopWords;
    private final int min, max, k, n;

    public StandartTPP(final int min, final int max, final List<String> stopWords, final int k, final int n) {
        this.min = min;
        this.max = max;
        this.stopWords = stopWords;
        this.k = k;
        this.n = n;
    }

    @Override
    public Pipeline build(NGram term, Map<NGram, NGramStats> m) {
        Stemmer stemmer = new Stemmer();
        NGram termStemmed = stemmer.stem(term);
        Pipeline pipeline = new Pipeline();
        Locale locale = new Locale("en", "US");

        //Divide intput into setences.
        //pipeline.add((Object o, List<Object> l) -> {
        //    String input = (String) o;
        //    l.addAll(Tokenizer.setences(input, locale));
        //});

        //Clean snippet and convert it to tokens
        pipeline.add((Object o, List<Object> l) -> {
            String input = (String) o;
            List<String> tokens = Tokenizer.text(input, locale);
            if (!tokens.isEmpty())
                l.add(tokens);
        });

        //Remove stop words
        pipeline.add((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            tokens.removeIf(x -> Collections.binarySearch(stopWords, x) >= 0);
            if (tokens.size() > 0)
                l.add(tokens);
        });

        //Remove tokens that are too small or too big
        pipeline.add((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;
            tokens.removeIf(x -> x.length() < min || x.length() > max);
            if (tokens.size() > 0)
                l.add(tokens);
        });

        pipeline.add((Object o, List<Object> l) -> {
            List<String> tokens = (List<String>) o;

            int t = tokens.size() - term.size() + 1;
            String buffer[] = new String[term.size()];

            List<NGram> used = new ArrayList<>();
            for (int i = 0; i < t; i++) {
                for(int j = 0; j < term.size(); j++)
                    buffer[j] = stemmer.stem(tokens.get(i + j));
                if(termStemmed.equals(buffer)) {
                    int min = (i - k >= 0) ? i - k : 0,
                        max = (i + k + term.size() <= tokens.size()) ? i + k + term.size() : tokens.size();
                    findCloseNGrams(tokens, min, i, n, m, used);
                    findCloseNGrams(tokens, i+term.size(), max, n, m, used);
                }
            }
        });

        return pipeline;
    }

    private void findCloseNGrams(List<String> tokens, int left, int right, int n,
                                 Map<NGram, NGramStats> m, List<NGram> used) {
        //System.out.println("Left: " + left + " Right: " + right);
        for(int i = 1; i <= n; i++) {
            //System.out.println("NGRAM: "+i);
            for (int j = left; j < right - i + 1; j++) {
                String buffer[] = new String[i];
                for(int k = 0; k < i; k++)
                    buffer[k] = tokens.get(j+k);
                NGram ngram = new NGram(buffer);
                //System.out.println(PrintUtils.array(buffer)+" -> "+ngram);
                if(!used.contains(ngram)) {
                    if(!m.containsKey(ngram))
                        m.put(ngram, new NGramStats());
                    m.get(ngram).documentFrequency += 1;
                    used.add(ngram);
                }
                try {
                    m.get(ngram).rawFrequency += 1;
                } catch(Exception e) {
                    System.err.println("MAP: "+m);
                    System.err.println("NGRAM: "+ngram.size()+" "+ngram.length()+" "+ngram);
                }
            }
        }
    }
}
