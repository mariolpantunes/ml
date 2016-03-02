package pt.it.av.atnog.ml.tm.tokenizer;

import org.junit.Test;
import pt.it.av.atnog.ml.tm.tokenizer.TokenizerOld;

import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Created by mantunes on 8/21/15.
 */

//TODO: Finish this test
public class TokenizerTest {

    @Test
    public void test_text() {
        Tokenizer tk = new TextTokenizer();
        String s = "My estate goes to my husband, son, daughter-in-law, and nephew.";
        Iterator it = tk.tokenizeIt(s,2);
        int i = 0;
        while(it.hasNext()) {
            System.out.println(i+":"+it.next());
            i++;
        }
    }
}
