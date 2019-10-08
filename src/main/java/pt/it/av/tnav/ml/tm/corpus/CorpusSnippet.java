package pt.it.av.tnav.ml.tm.corpus;

import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.ws.search.SearchEngine;

import java.util.Iterator;

/**
 * Implement a corpus based on snippets from a search engine.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class CorpusSnippet implements Corpus {
  private final SearchEngine se;

  /**
   * CorpusSnippet constructor.
   *
   * @param se search engine used to find relevant snippets of text
   */
  public CorpusSnippet(final SearchEngine se) {
    this.se = se;
  }

  @Override
  public Iterator<String> iterator(final NGram ngram) {
    return new CorpusIterator(se.searchIt(ngram.toString()));
  }

  /**
   * Iterator that wraps a search engine iterator and extracts the relevant snippets.
   */
  private class CorpusIterator implements Iterator<String> {
    private final Iterator<SearchEngine.Result> it;

    /**
     * CorpusIterator constructor.
     *
     * @param it
     */
    public CorpusIterator(final Iterator<SearchEngine.Result> it) {
      this.it = it;
    }

    @Override
    public boolean hasNext() {
      return it.hasNext();
    }

    @Override
    public String next() {
      SearchEngine.Result r = it.next();
      return r.snippet;
    }
  }
}
