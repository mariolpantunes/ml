package pt.it.av.atnog.ml.tm.syntacticPattern;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mantunes on 10/5/15.
 */
public abstract class SyntacticPattern {
    final protected NGram term, stem;
    final protected Stemmer stemmer;
    protected final List<String> blacklist;

    public SyntacticPattern(final NGram term, final Stemmer stemmer, final List<String> blacklist) {
        this.term = term;
        this.stemmer = stemmer;
        this.blacklist = blacklist;
        stem = stemmer.stem(term);
    }

    public abstract String query();

    protected abstract List<String> match(List<String> tokens, int i, int w);

    //TODO: remove self term
    public List<NGram> extract(List<String> tokens, int n) {
        List<NGram> rv = new ArrayList<>();
        List<String> match = null;
        String buffer[] = new String[term.size()];
        for (int i = 0, s = tokens.size() - term.size(); i < s && match == null; i++) {
            for (int j = 0; j < term.size(); j++)
                buffer[j] = stemmer.stem(tokens.get(i + j));
            if (stem.equals(buffer))
                match = match(tokens, i, n);
        }
        match.removeIf(x -> Collections.binarySearch(blacklist, x) >= 0);
        for(int i = 1; i <= n; i++) {
            for(int j = 0, t = tokens.size() - i + 1; j < t; j++) {
                rv.add(new NGram(tokens.subList(j,j+i)));
            }
        }
        return rv;
    }
}
