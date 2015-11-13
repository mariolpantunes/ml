package pt.it.av.atnog.ml.tm.ngrams;

import pt.it.av.atnog.ml.tm.stemmer.Stemmer;
import pt.it.av.atnog.utils.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by mantunes on 6/1/15.
 */
public class NGram implements Comparable<NGram>, Iterable<String> {
    protected final String array[];

    public NGram(String array[]) {
        this.array = array;
    }

    public NGram(int size) {
        this.array = new String[size];
    }

    public NGram(List<String> list) {
        array = new String[list.size()];
        int i = 0;
        for(String s : list)
            array[i++] = s;
    }

    public int size() {
        return array.length;
    }

    public int length() {
        int rv = 0;
        for (String s : array)
            rv += s.length();
        return rv;
    }

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

    public boolean equals(String tokens[]) {
        boolean rv = false;

        if (tokens.length == this.array.length) {
            rv = true;
            for (int i = 0; i < this.array.length && rv; i++)
                rv = this.array[i].equals(tokens[i]);
        }

        return rv;
    }

    public boolean equals(List<String> tokens, int b, Stemmer stemmer) {
        boolean rv = false;

        if(b + size() <= tokens.size()) {
            rv = true;
            for(int i = 0, t = size(); i < t && rv; i++)
                rv = array[i].equals(stemmer.stem(tokens.get(i+b)));
        }

        return rv;
    }

    //TODO: add locale, add toUpperCase...
    public NGram toLowerCase() {
        NGram rv = new NGram(array.length);
        for(int i = 0; i < array.length; i++)
            rv.array[i] = array[i].toLowerCase();
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

        if (rv == 0) {
            if (array.length < ngram.array.length)
                rv = 1;
            else
                rv = -1;
        }
        return 0;
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
}
