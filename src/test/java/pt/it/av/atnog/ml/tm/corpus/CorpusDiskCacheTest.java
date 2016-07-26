package pt.it.av.atnog.ml.tm.corpus;

import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.utils.ws.search.SearchEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for CorpusDiskCache class.
 */
public class CorpusDiskCacheTest {
    /**
     * Tests the caching mechanism of the CorpusDiskCache class.
     */
    @org.junit.Test
    public void test() {
        List<String> l = new ArrayList<>(3);
        l.add("banana");
        l.add("apple");
        l.add("peach");

        String tmp = System.getProperty("java.io.tmpdir");
        Corpus c = new CorpusDiskCache(new CorpusSnippet(new MockSearchEngine()), Paths.get(tmp));

        Iterator<String> it = c.iterator(NGram.Unigram("UNIT_TEST_CORPUS_DISK_CACHE"));

        // Writes the cache
        int idx = 0;
        while (it.hasNext())
            assertTrue(it.next().equals(l.get(idx++)));

        // Reads the cache
        idx = 0;
        while (it.hasNext())
            assertTrue(it.next().equals(l.get(idx++)));

        try {
            Files.delete(Paths.get(tmp).resolve("UNIT_TEST_CORPUS_DISK_CACHE.gz"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * A mockup of the SearchEngine class. Used to test the CorpusDiskCache.
     */
    private class MockSearchEngine extends SearchEngine {
        List<Result> l = new ArrayList<>(3);

        /**
         * MockSearchEngine constructor.
         */
        public MockSearchEngine() {
            l.add(new Result("banana", "banana", "banana"));
            l.add(new Result("apple", "apple", "apple"));
            l.add(new Result("peach", "peach", "peach"));
        }

        @Override
        public Iterator<Result> searchIt(String s) {
            return l.iterator();
        }
    }
}
