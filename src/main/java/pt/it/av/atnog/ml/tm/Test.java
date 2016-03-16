package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.ml.tm.lexicalPattern.EIsAC;
import pt.it.av.atnog.ml.tm.lexicalPattern.LexicalPattern;
import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.PorterStemmer;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;
import pt.it.av.atnog.utils.ws.search.Bing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mantunes on 16/02/2015.
 */
public class Test {
    public static void main(String[] args) {
        Stemmer s = new PorterStemmer();
        List<LexicalPattern> patterns = new ArrayList<>();
        patterns.add(new EIsAC(s));
        Hypernyms h = new Hypernyms(new Bing("ZctQ1GPgFid1k72ZUBlKmB/CWfWXiPoFZj3IlChOV1g"), patterns);
        h.hypernym(NGram.Unigram("linux"));
    }
}
