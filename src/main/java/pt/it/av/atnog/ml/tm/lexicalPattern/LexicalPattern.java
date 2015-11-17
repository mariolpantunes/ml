package pt.it.av.atnog.ml.tm.lexicalPattern;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO: finish the other lexical patterns
public abstract class LexicalPattern {
    final protected NGram term, stem;
    final protected Stemmer stemmer;
    protected final List<String> blacklist;

    public LexicalPattern(final NGram term, final Stemmer stemmer, final List<String> blacklist) {
        this.term = term;
        this.stemmer = stemmer;
        this.blacklist = blacklist;
        stem = stemmer.stem(term);
    }

    public abstract String query();

    protected abstract List<String> match(List<String> tokens, int i, int w);

    //TODO: try to remove self term
    //TODO: improve code, should use more classes and interfaces
    public List<NGram> extract(List<String> tokens, int n) {
        List<NGram> rv = new ArrayList<>();
        List<String> match = null;
        String buffer[] = new String[stem.size()];
        for (int i = 0, s = tokens.size() - stem.size(); i < s && match == null; i++) {
            for(int j = 0; j < stem.size(); j++)
                buffer[j] = stemmer.stem(tokens.get(i+j));
            if (stem.equals(buffer))
                match = match(tokens, i, n);
        }
        if(match != null) {
            match.removeIf(x -> Collections.binarySearch(blacklist, x) >= 0);
            for (int i = 1; i <= n; i++)
                for (int j = 0, t = match.size() - i + 1; j < t; j++)
                    rv.add(new NGram(match.subList(j, j + i)));
        }
        return rv;
    }
}
