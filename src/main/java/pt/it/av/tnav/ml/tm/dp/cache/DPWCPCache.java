package pt.it.av.tnav.ml.tm.dp.cache;

import pt.it.av.tnav.ml.tm.dp.DPWC;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.utils.structures.cache.AbstractPCache;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public class DPWCPCache extends AbstractPCache<NGram, DPWC> {
  private final DPWPCache dpwpCache;

  public DPWCPCache(final Path pCache, final DPWPCache dpwpCache) {
    super(pCache);
    this.dpwpCache = dpwpCache;
  }

  @Override
  protected DPWC load(Reader in) throws IOException {
    return DPWC.load(in);
  }

  @Override
  protected DPWC build(NGram key) {
    return DPWC.learn(key, dpwpCache);
  }

  @Override
  protected void store(DPWC dpwc, Writer out) throws IOException {
    DPWC.store(dpwc, out);
  }
}
