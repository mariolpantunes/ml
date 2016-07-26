package pt.it.av.atnog.ml.tm.corpus;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Wraps a Corpus class and provides a cache system based on disk files.
 */
public class CorpusDiskCache implements Corpus {
    private final Corpus c;
    private final Path cache;

    /**
     * Corpus disk cache constructor
     *
     * @param c     another corpus class
     * @param cache directory where the cache is written/read
     */
    public CorpusDiskCache(Corpus c, Path cache) {
        this.c = c;
        this.cache = cache;
    }

    @Override
    public Iterator<String> iterator(NGram ngram) {
        Path file = cache.resolve(ngram + ".gz");
        if (Files.isReadable(file))
            return new CorpusCacheIterator(file);
        else
            return new CorpusIterator(c.iterator(ngram), file);
    }

    /**
     * Implements a corpus iterator that reads the corpus from the cache (disk file).
     */
    private class CorpusCacheIterator implements Iterator<String> {
        private BufferedReader in;
        private String line = null;

        /**
         * Corpus cache iterator constructor.
         *
         * @param file the file that contains the corpus content
         */
        public CorpusCacheIterator(Path file) {
            try {
                this.in = new BufferedReader(new InputStreamReader(new GZIPInputStream(
                        Files.newInputStream(file)), "UTF-8"));
                line = in.readLine();
            } catch (IOException e) {
                //should not happen
                e.printStackTrace();
            }
        }

        @Override
        public boolean hasNext() {
            boolean rv = true;
            if (line == null) {
                rv = false;
                if (in != null) {
                    try {
                        in.close();
                        in = null;
                    } catch (IOException e) {
                        //should not happen
                        e.printStackTrace();
                    }
                }
            }
            return rv;
        }

        @Override
        public String next() {
            String rv = line;
            try {
                line = in.readLine();
            } catch (IOException e) {
                //should not happen
                e.printStackTrace();
                line = null;
            }
            return rv;
        }
    }

    /**
     * Implements a corpus iterator that consomes content from the original corpus and store it in the cache.
     */
    private class CorpusIterator implements Iterator<String> {
        private final Iterator<String> it;
        private BufferedWriter out = null;


        /**
         * Corpus iterator constructor.
         *
         * @param it   iterator from the original corpus with the relevant content
         * @param file file in the cache that will hold the content
         */
        public CorpusIterator(Iterator<String> it, Path file) {
            this.it = it;
            if (it.hasNext()) {
                try {
                    this.out = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream
                            (Files.newOutputStream(file)), "UTF-8"));
                } catch (IOException e) {
                    //should not happen
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean hasNext() {
            boolean rv = true;

            if (!it.hasNext()) {
                if (out != null) {
                    try {
                        out.close();
                        out = null;
                    } catch (IOException e) {
                        //should not happen
                        e.printStackTrace();
                    }
                }
                rv = false;
            }
            return rv;
        }

        @Override
        public String next() {
            String rv = it.next();
            try {
                out.write(rv);
                out.newLine();
            } catch (IOException e) {
                //should not happen
                e.printStackTrace();
            }
            return rv;
        }
    }
}
