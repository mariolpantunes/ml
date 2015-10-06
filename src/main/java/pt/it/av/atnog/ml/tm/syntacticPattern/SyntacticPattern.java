package pt.it.av.atnog.ml.tm.syntacticPattern;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.util.List;

/**
 * Created by mantunes on 10/5/15.
 */
public interface SyntacticPattern {
    String query();
    List<NGram> match(List<String> tokens, int n);
}
