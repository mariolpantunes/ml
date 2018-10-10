package pt.it.av.tnav.ml.tm.lexicalPattern;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.it.av.tnav.ml.tm.ngrams.NGram;
import pt.it.av.tnav.ml.tm.stemmer.PorterStemmer;
import pt.it.av.tnav.ml.tm.stemmer.Stemmer;
import pt.it.av.tnav.ml.tm.tokenizer.PlainTextTokenizer;
import pt.it.av.tnav.ml.tm.tokenizer.Tokenizer;
import pt.it.av.tnav.utils.PrintUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Mário Antunes
 * @version 1.0
 */
public class EIsACTest {
  private static Stemmer stemmer;
  private static Tokenizer tokenizer;

  @BeforeClass
  public static void setup() {
    stemmer = new PorterStemmer();
    tokenizer = new PlainTextTokenizer();
  }

  @Test
  public void test_query() {
    LexicalPattern eIsAC = new EIsAC(stemmer);

    eIsAC.ngram(NGram.Unigram("banana"));
    assertTrue(eIsAC.query().equals("banana is a"));

    eIsAC.ngram(NGram.Unigram("linux"));
    assertTrue(eIsAC.query().equals("linux is a"));
  }

  @Test
  public void test_extract() {
    String snippet = "The banana is an edible fruit, botanically a berry, produced by several kinds of large " +
        "herbaceous flowering plants in the genus Musa. In some countries ...";
    LexicalPattern eIsAC = new EIsAC(stemmer);
    eIsAC.ngram(NGram.Unigram("banana"));
    List<NGram> rv = new ArrayList<>();
    rv.add(NGram.Unigram("an"));
    rv.add(NGram.Unigram("edible"));
    rv.add(NGram.Unigram("fruit"));
    rv.add(NGram.Bigram("an", "edible"));
    rv.add(NGram.Bigram("edible", "fruit"));
    rv.add(NGram.Trigram("an", "edible", "fruit"));
    assertTrue(eIsAC.extract(tokenizer.tokenize(snippet), 3).equals(rv));

    snippet = "linux is an operating system.";
    eIsAC.ngram(NGram.Unigram("linux"));
    rv.clear();
    rv.add(NGram.Unigram("an"));
    rv.add(NGram.Unigram("operating"));
    rv.add(NGram.Unigram("system"));
    rv.add(NGram.Bigram("an", "operating"));
    rv.add(NGram.Bigram("operating", "system"));
    rv.add(NGram.Trigram("an", "operating", "system"));
    assertTrue(eIsAC.extract(tokenizer.tokenize(snippet), 3).equals(rv));
  }
}
