package pt.it.av.atnog.ml.tm.lexicalPattern;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.List;


public class EIsAC extends LexicalPattern {

    public EIsAC(final NGram term, final Stemmer stemmer) {
        super(term, stemmer, new ArrayList<>());
        blacklist.add("are");
        blacklist.add("is");
    }

    @Override
    public String query() {
        return term.toString()+" is a";
    }

    @Override
    public List<String> match(List<String> tokens, int i, int w) {
        List<String> rv = null;
        if(i+term.size() < tokens.size()
                        && (tokens.get(i + term.size()).equals("is")
                        || tokens.get(i + term.size()).equals("are"))) {
                    int total = i + term.size() + 1 + w < tokens.size() ? i + term.size() + 1 + w : tokens.size();
                    rv = tokens.subList(i + term.size() + 1, total);
                }
        return rv;
    }
}
