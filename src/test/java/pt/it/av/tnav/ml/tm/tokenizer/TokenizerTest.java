package pt.it.av.tnav.ml.tm.tokenizer;

import org.junit.Test;

import java.util.Iterator;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the Tokenizer class.
 */
public class TokenizerTest {
    private static final String Sample1 = "My estate goes to my husband, son, daughter-in-law, and nephew.";
    private static final String Sample1_unigram[] = {"my", "estate", "goes", "to", "my", "husband", "son", "daughter-in-law", "and", "nephew"};

    @Test
    public void test_unigram() {
        Tokenizer tk = new TextTokenizer();
        Iterator<String> it = tk.tokenizeIt(Sample1);
        int i = 0;
        while (it.hasNext())
            assertTrue(Sample1_unigram[i++].equals(it.next()));
    }
}
