package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.structures.tuple.Pair;
import pt.it.av.atnog.utils.ws.SearchEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mantunes on 3/30/15.
 */
public class Corpus {
    private List<SearchEngine> engines;

    public Corpus(List<SearchEngine> engines){
        this.engines = engines;
    }

    //TODO: store results in a file for further evaluation
    public Pair<TDP, TDP> tdp(String term1, String term2) {
        List<String> corpusT1 = new ArrayList<>(), corpusT2 = new ArrayList<>();
        for(SearchEngine engine: engines) {
            corpusT1.addAll(engine.snippets(term1));
            corpusT2.addAll(engine.snippets(term2));
        }
        return new Pair<>(TDP.learn(corpusT1), TDP.learn(corpusT2));
    }
}
