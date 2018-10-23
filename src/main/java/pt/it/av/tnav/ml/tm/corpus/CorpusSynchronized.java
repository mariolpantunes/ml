package pt.it.av.tnav.ml.tm.corpus;

import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.CollectionsUtils;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class CorpusSynchronized implements Corpus {
  private final Corpus c;

  /**
   *
   * @param c
   */
  public CorpusSynchronized(final Corpus c) {
    this.c = c;
  }

  @Override
  public Iterator<String> iterator(NGram ngram) {
    Iterator<String> rv;
    synchronized(ngram.toString().intern()) {
      List<String> l = CollectionsUtils.iterator2List(c.iterator(ngram));
      rv = l.iterator();
    }
    return rv;
  }
}
