package pt.it.av.atnog.ml.tm.lexicalPattern;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mantunes on 11/13/15.
 */
public class CSuchAsE extends LexicalPattern {

    public CSuchAsE(Stemmer stemmer, List<String> blacklist) {
        super(stemmer, new ArrayList<>());
        blacklist.add("such");
    }

    @Override
    public String query() {
        return null;
    }

    @Override
    protected List<String> match(List<String> tokens, int i, int w) {
        return null;
    }
}
