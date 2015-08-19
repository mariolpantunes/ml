package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.bla.Matrix;
import pt.it.av.atnog.utils.ws.search.Bing;

/**
 * Created by mantunes on 16/02/2015.
 */
public class Test {
    public static void main(String[] args) {
        Hypernyms h = new Hypernyms(new Bing("ZctQ1GPgFid1k72ZUBlKmB/CWfWXiPoFZj3IlChOV1g"));
        h.hypernym(new Unigram("computer"));
    }
}
