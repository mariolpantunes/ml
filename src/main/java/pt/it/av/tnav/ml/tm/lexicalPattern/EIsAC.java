package pt.it.av.tnav.ml.tm.lexicalPattern;

import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.ml.tm.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.List;


public class EIsAC extends LexicalPattern {

    public EIsAC(final Stemmer stemmer) {
        super(stemmer, new ArrayList<>());
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
        if(i < tokens.size()
                        && (tokens.get(i).equals("is")
                        || tokens.get(i).equals("are"))) {
                    int total = i + 1 + w < tokens.size() ? i + 1 + w : tokens.size();
                    rv = tokens.subList(i + 1, total);
                }
        return rv;
    }
}
