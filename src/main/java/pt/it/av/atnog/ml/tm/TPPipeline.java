package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.parallel.Pipeline;

import java.util.Map;

/**
 * Created by mantunes on 6/15/15.
 */
public interface TPPipeline {
    Pipeline build(NGram term, Map<NGram, NGramStats> m);
}
