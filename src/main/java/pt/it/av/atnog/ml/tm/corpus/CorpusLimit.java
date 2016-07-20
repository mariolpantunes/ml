package pt.it.av.atnog.ml.tm.corpus;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.structures.iterator.LimitIterator;

import java.util.Iterator;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class CorpusLimit implements Corpus{
    private final Corpus c;
    private final int limit;

    public CorpusLimit(final Corpus c, final int limit) {
        this.c = c;
        this.limit = limit;
    }

    @Override
    public Iterator<String> iterator(NGram ngram) {
        return new LimitIterator(c.iterator(ngram), limit);
    }
}
