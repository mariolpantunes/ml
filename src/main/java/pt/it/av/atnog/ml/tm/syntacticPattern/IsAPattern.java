package pt.it.av.atnog.ml.tm.syntacticPattern;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mantunes on 10/6/15.
 */
public class IsAPattern implements SyntacticPattern {
    final private NGram term, stem;
    final Stemmer stemmer;
    final List<String> blacklist;

    public IsAPattern(final NGram term, final Stemmer stemmer) {
        this.term = term;
        stem = stemmer.stem(term);
        this.stemmer = stemmer;
        blacklist = new ArrayList<String>(2);
        blacklist.add("is");
        blacklist.add("are");
    }

    public String query() {
        return term.toString()+" is a";
    }

    //int total = i + n + MAX < tokens.size() ? i + n + MAX : tokens.size();
    //for (int j = i + n; j < total && !done; j++) {
    //if (tokens.get(j).equals("is") || tokens.get(j).equals("are")) {
    //    int size = j + 1 + NN < tokens.size() ? j + 1 + NN : tokens.size();
    //        List<String> output = new ArrayList<String>(tokens.subList(j + 1, size));
    //        output.removeIf(x -> Collections.binarySearch(blacklist, x) >= 0);
    //        if (output.size() > 0)
    //            l.add(output);
    //}
    //}

    public List<NGram> match(List<String> tokens, int n) {
        String buffer[] = new String[term.size()];
        List<NGram> rv = new ArrayList<>();
        for (int i = 0, s = tokens.size() - term.size(); i < s; i++) {
            for (int j = 0; j < term.size(); j++)
                buffer[j] = stemmer.stem(tokens.get(i + j));
            if (stem.equals(buffer)) {
                if(i+term.size() < tokens.size()
                        && (tokens.get(i + term.size()).equals("is")
                        || tokens.get(i + term.size()).equals("are"))) {
                    int total = i + term.size() + 1 + n < tokens.size() ? i + term.size() + 1 + n : tokens.size();
                    List<String> data = new ArrayList<>(n);
                    for(int j = i + term.size() + 1; j < total; j++) {
                        if(!blacklist.contains(tokens.get(j)))
                           data.add(tokens.get(j));
                        rv.add(new NGram(data));
                    }
                }
            }
        }
        return rv;
    }
}
