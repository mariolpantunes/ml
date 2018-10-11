package pt.it.av.tnav.ml.tm.corpus;

import pt.it.av.tnav.ml.tm.ngrams.NGram;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Mockup Corpus implementation.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class MockCorpus implements Corpus {
  private final HashMap<String, List<String>> map;

  /**
   *
   * @param map
   */
  public MockCorpus(final HashMap<String, List<String>> map) {
    this.map = map;
  }

  @Override
  public Iterator<String> iterator(NGram ngram) {
    List<String> l = map.get(ngram.toString());
    return l.iterator();
  }
}
