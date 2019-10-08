package pt.it.av.tnav.ml.tm.ngrams;

import org.junit.BeforeClass;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit testing for the NGram class
 */
public class NGramsTest {
    private static NGram n1,n2,n3;
    private static List<String> l1, l2, l3;

    @BeforeClass
    public static void setup() {
        l1 = new ArrayList<>(1);
        l1.add("banana");
        l2 = new ArrayList<>(2);
        l2.add("banana");
        l2.add("apple");
        l3 = new ArrayList<>(3);
        l3.add("banana");
        l3.add("apple");
        l3.add("peach");
        n1 = NGram.Unigram("banana");
        n2 = NGram.Bigram("banana", "apple");
        n3 = NGram.Trigram("banana", "apple", "peach");
    }

    @Test
    public void test_size_length() {
        assertTrue(n1.size() == 1);
        assertTrue(n1.length() == 6);

        assertTrue(n2.size() == 2);
        assertTrue(n2.length() == 11);

        assertTrue(n3.size() == 3);
        assertTrue(n3.length() == 16);
    }

    @Test
    public void test_equals() {
        assertTrue(n1.equals(l1));
        assertTrue(!n1.equals(l2));
        assertTrue(!n1.equals(l3));

        assertTrue(!n2.equals(l1));
        assertTrue(n2.equals(l2));
        assertTrue(!n2.equals(l3));

        assertTrue(!n3.equals(l1));
        assertTrue(!n3.equals(l2));
        assertTrue(n3.equals(l3));
    }

    @Test
    public void test_compareTo() {
        assertTrue(n1.compareTo(n1) == 0);
        assertTrue(n1.compareTo(n2) == -1);
        assertTrue(n2.compareTo(n1) == 1);

        assertTrue(n2.compareTo(n2) == 0);
        assertTrue(n2.compareTo(n3) == -1);
        assertTrue(n3.compareTo(n2) == 1);
    }

    @Test
    public void test_sort() {
        List<NGram> sorted = new ArrayList<>(), l = new ArrayList<>();
        sorted.add(n1);
        sorted.add(n2);
        sorted.add(n3);
        l.add(n3);
        l.add(n2);
        l.add(n1);
        Collections.sort(l);
        assertTrue(sorted.equals(l));
    }

}
