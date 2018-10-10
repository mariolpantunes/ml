package pt.it.av.tnav.ml.tm.ngrams;

import pt.it.av.tnav.ml.tm.tokenizer.PlainTextTokenizer;
import pt.it.av.tnav.utils.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * NGram class, represents a contiguous sequence of n items from a given sequence of text.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class NGram implements Comparable<NGram>, Iterable<String> {
  protected final String array[];

  /**
   * NGra constructor, creates a NGram from an array of Strings.
   *
   * @param array of Strings
   */
  public NGram(String array[]) {
    this.array = array;
  }

  /**
   * NGram constructor, creates a NGram from a List of Strings.
   *
   * @param list of Strings
   */
  public NGram(List<String> tokens) {
    array = new String[tokens.size()];
    for (int i = 0; i < tokens.size(); i++) {
      array[i] = tokens.get(i);
    }
  }

  /**
   * Returns the number of items in this NGram.
   *
   * @return number of items in this NGram.
   */
  public int size() {
    return array.length;
  }

  /**
   * Returns the length of this character sequence (total number of characters).
   *
   * @return ength of this character sequence
   */
  public int length() {
    int rv = 0;
    for (String s : array)
      rv += s.length();
    return rv;
  }

  /**
   * Returns the Levenshtein distance between two NGrams.
   *
   * @param ngram the second NGram
   * @return Levenshtein distance between two NGrams
   */
  public int levenshtein(NGram ngram) {
    int rv = 0;
    String first[], second[];
    if (array.length < ngram.array.length) {
      second = array;
      first = ngram.array;
    } else {
      first = array;
      second = ngram.array;
    }

    for (int i = 0; i < second.length; i++)
      rv += StringUtils.levenshtein(first[i], second[i]);

    for (int i = second.length; i < first.length; i++)
      rv += StringUtils.levenshtein(first[i], "");

    return rv;
  }

  /**
   * @return
   */
  public NGram toLowerCase() {
    String buffer[] = new String[array.length];
    for (int i = 0; i < array.length; i++)
      buffer[i] = array[i].toLowerCase();
    return new NGram(buffer);
  }

  /**
   * @return
   */
  public NGram toUpperCase() {
    String buffer[] = new String[array.length];
    for (int i = 0; i < array.length; i++)
      buffer[i] = array[i].toUpperCase();
    return new NGram(buffer);
  }

  /**
   * @param tokens
   * @return
   */
  public boolean equals(String tokens[]) {
    boolean rv = false;
    if (tokens.length == array.length) {
      rv = true;
      for (int i = 0; i < array.length && rv; i++)
        rv = array[i].equals(tokens[i]);
    }
    return rv;
  }

  /**
   * @param tokens
   * @return
   */
  public boolean equals(List<String> tokens) {
    boolean rv = false;
    if (tokens.size() == array.length) {
      rv = true;
      for (int i = 0; i < array.length && rv; i++)
        rv = array[i].equals(tokens.get(i));
    }
    return rv;
  }

  @Override
  public boolean equals(Object o) {
    boolean rv = false;
    if (o != null) {
      if (o == this)
        rv = true;
      else if (o instanceof NGram) {
        NGram ngram = (NGram) o;
        if (ngram.array.length == this.array.length) {
          rv = true;
          for (int i = 0; i < this.array.length && rv; i++)
            rv = this.array[i].equals(ngram.array[i]);
        } else
          rv = false;
      }
    }
    return rv;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length - 1; i++)
      sb.append(array[i] + " ");
    if (array.length > 0)
      sb.append(array[array.length - 1]);
    return sb.toString();
  }

  @Override
  public int compareTo(NGram ngram) {
    int rv = 0, t = 0;
    if (array.length < ngram.array.length)
      t = array.length;
    else
      t = ngram.array.length;

    for (int i = 0; i < t && rv == 0; i++)
      rv = array[i].compareTo(ngram.array[i]);

    if (rv == 0 && array.length != ngram.array.length)
      if (array.length < ngram.array.length)
        rv = -1;
      else
        rv = 1;

    return rv;
  }

  @Override
  public int hashCode() {
    int rv = 0;
    for (int i = 0; i < this.array.length; i++)
      rv += array[i].hashCode();
    return rv;
  }

  @Override
  public Iterator<String> iterator() {
    return new NGramIterator();
  }

  /**
   *
   */
  private class NGramIterator implements Iterator<String> {
    int idx = 0;

    @Override
    public boolean hasNext() {
      return idx < array.length;
    }

    @Override
    public String next() {
      return array[idx++];
    }
  }

  /**
   * @param a
   * @param b
   * @param c
   * @return
   */
  public static NGram Trigram(String a, String b, String c) {
    String buffer[] = new String[3];
    buffer[0] = a;
    buffer[1] = b;
    buffer[2] = c;
    return new NGram(buffer);
  }

  /**
   * @param a
   * @param b
   * @return
   */
  public static NGram Bigram(String a, String b) {
    String buffer[] = new String[2];
    buffer[0] = a;
    buffer[1] = b;
    return new NGram(buffer);
  }

  /**
   * @param a
   * @return
   */
  public static NGram Unigram(String a) {
    String buffer[] = new String[1];
    buffer[0] = a;
    return new NGram(buffer);
  }

  /**
   *
   * @param txt
   * @return
   */
  public static NGram Parse(String txt) {
    return new NGram(PlainTextTokenizer.build().tokenize(txt));
  }
}
