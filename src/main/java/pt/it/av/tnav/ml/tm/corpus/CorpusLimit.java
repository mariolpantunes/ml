package pt.it.av.tnav.ml.tm.corpus;

import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.structures.iterator.LimitIterator;

import java.util.Iterator;

/**
 * Wraps a Corpus class and provides a limit to the result returned.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class CorpusLimit implements Corpus {
  private final Corpus c;
  private final int limit;

  public CorpusLimit(final Corpus c, final int limit) {
    this.c = c;
    this.limit = limit;
  }

  @Override
  public Iterator<String> iterator(NGram ngram) {
    return new LimitIterator<>(c.iterator(ngram), limit);
  }
}
