package pt.it.av.tnav.ml.tm.dp.cache;

import pt.it.av.tnav.ml.tm.corpus.Corpus;
import pt.it.av.tnav.ml.tm.dp.DPW;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.structures.cache.AbstractPCache;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public class DPWPCache extends AbstractPCache<NGram, DPW> {
  private final Corpus corpus;

  public DPWPCache(final Path pCache, final Corpus corpus) {
    super(pCache);
    this.corpus = corpus;
  }

  @Override
  protected DPW load(Reader in) throws IOException {
    return DPW.load(in);
  }

  @Override
  protected DPW build(NGram key) {
    return DPW.learn(key, corpus);
  }

  @Override
  protected void store(DPW dpw, Writer out) throws IOException {
    DPW.store(dpw, out);
  }
}
