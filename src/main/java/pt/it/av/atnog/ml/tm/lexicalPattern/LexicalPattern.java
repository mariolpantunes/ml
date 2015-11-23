package pt.it.av.atnog.ml.tm.lexicalPattern;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO: finish the other lexical patterns
//TODO: for now we can set the ngram at any time, this allows me to develop a loop in the hyernym class
public abstract class LexicalPattern {
    protected NGram term, stem;
    final protected Stemmer stemmer;
    final protected  List<String> blacklist;

    public LexicalPattern(final Stemmer stemmer, final List<String> blacklist) {
        this.stemmer = stemmer;
        this.blacklist = blacklist;
    }

    public void ngram(final NGram term) {
        this.term = term;
        stem = stemmer.stem(term);
    }

    public List<String> blacklist() {
        return blacklist;
    }

    public abstract String query();

    protected abstract List<String> match(List<String> tokens, int i, int w);

    //TODO: try to remove self term
    //TODO: improve code, should use more classes and interfaces
    public List<NGram> extract(List<String> tokens, int n) {
        List<NGram> rv = new ArrayList<>();
        List<String> match = null;
        List<String> buffer = new ArrayList<>(stem.size());

        int i = 0;
        for(;i < stem.size() && i < tokens.size(); i++)
            buffer.add(stemmer.stem(tokens.get(i)));
        if (stem.equals(buffer))
            match = match(tokens, 0, n);

        for (int s = tokens.size(); i < s && match == null; i++) {
            buffer.remove(0);
            buffer.add(stemmer.stem(tokens.get(i)));
            if (stem.equals(buffer))
                match = match(tokens, i+stem.size(), n);
        }

        if(match != null) {
            match.removeIf(x -> Collections.binarySearch(blacklist, x) >= 0);
            for (i = 1; i <= n; i++)
                for (int j = 0, t = match.size() - i + 1; j < t; j++)
                    rv.add(new NGram(match.subList(j, j + i)));
        }
        return rv;
    }
}
