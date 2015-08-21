package pt.it.av.atnog.ml.tm;

import org.junit.Test;
import pt.it.av.atnog.utils.PrintUtils;

import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Created by mantunes on 8/21/15.
 */
public class TokenizerTest {

    @Test
    public void test_clauses() {
        String setence1 = "My estate goes to my husband, son, daughter-in-law, and nephew.";
        List<String> clauses1 = Tokenizer.clauses(setence1);
        assertTrue(clauses1.size() == 1);
        assertTrue(clauses1.get(0).equals(setence1));

        String setence2 = "After he walked all the way home, he shut the door.";
        List<String> clauses2 = Tokenizer.clauses(setence2);
        assertTrue(clauses2.size() == 2);
        assertTrue(clauses2.get(0).equals("After he walked all the way home"));
        assertTrue(clauses2.get(1).equals("he shut the door."));

        String setence3 = "I am, by the way, very nervous about this.";
        List<String> clauses3 = Tokenizer.clauses(setence3);
        assertTrue(clauses3.size() == 2);
        assertTrue(clauses3.get(0).equals("I am very nervous about this."));
        assertTrue(clauses3.get(1).equals("by the way"));
    }
}
