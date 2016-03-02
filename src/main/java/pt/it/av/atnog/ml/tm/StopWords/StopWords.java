package pt.it.av.atnog.ml.tm.StopWords;

import java.util.List;

/**
 * Created by mantunes on 8/5/15.
 */
public interface StopWords {
    List<String> stopWords();
    boolean isStopWord(String token);
}
