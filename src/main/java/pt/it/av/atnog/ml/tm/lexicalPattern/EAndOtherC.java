package pt.it.av.atnog.ml.tm.lexicalPattern;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mantunes on 11/13/15.
 */
public class EAndOtherC extends LexicalPattern {

    private static final int DEFAULT_LIMIT = 5;
    private final int limit;

    public EAndOtherC(final NGram term, final Stemmer stemmer, int limit) {
        super(term, stemmer, new ArrayList<>());
        blacklist.add("and");
        blacklist.add("other");
        this.limit = limit;
    }

    public EAndOtherC(final NGram term, final Stemmer stemmer) {
        this(term, stemmer, DEFAULT_LIMIT);
    }

    @Override
    public String query() {
        return term.toString()+" and other";
    }

    @Override
    public List<String> match(List<String> tokens, int i, int w) {
        List<String> rv = null;
        //int limit;
        //for(int j = i+term.size(); j < )
        if(i+term.size() < tokens.size()
                && (tokens.get(i + term.size()).equals("is")
                || tokens.get(i + term.size()).equals("are"))) {
            int total = i + term.size() + 1 + w < tokens.size() ? i + term.size() + 1 + w : tokens.size();
            rv = tokens.subList(i + term.size() + 1, total);
        }
        return rv;
    }
}
