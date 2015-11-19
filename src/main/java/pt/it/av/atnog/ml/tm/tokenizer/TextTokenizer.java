package pt.it.av.atnog.ml.tm.tokenizer;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.text.BreakIterator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mantunes on 11/17/15.
 */
public class TextTokenizer implements Tokenizer {
    private final Locale locale;
    private final Pattern norm, text;

    public TextTokenizer(Locale locale) {
        this.locale = locale;
        norm = Pattern.compile("[\\p{InCombiningDiacriticalMarks}]");
        text = Pattern.compile("('s|[^a-zA-Z-]+)");
    }

    public TextTokenizer() {
        this(Locale.getDefault());
    }

    private String normalize(String input) {
        String lowerCase = input.toLowerCase(locale);
        String normalize = Normalizer.normalize(lowerCase, Normalizer.Form.NFD);
        Matcher matcher = norm.matcher(normalize);
        StringBuffer sb = new StringBuffer();
        while(matcher.find())
            matcher.appendReplacement(sb, "");
        matcher.appendTail(sb);
        return sb.toString();
    }

    public Iterator<String> tokenize(String input) {
        return new TextTokenizerIteratorString(normalize(input));
    }

    public Iterator<NGram> tokenize(String input, int n) {
        return new TextTokenizerIteratorNGram(normalize(input), n);
    }

    private class TextTokenizerIteratorString implements Iterator<String> {
        private final String input;
        private final ItData d;
        private boolean hasNext = true;

        public TextTokenizerIteratorString(String input) {
            this.input = input;
            d = new ItData(BreakIterator.getWordInstance(locale), input);
            findNext(input, d);
            if(d.token == null)
                hasNext = false;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public String next() {
            String rv = d.token;
            findNext(input, d);
            if(d.token == null)
                hasNext = false;
            return rv;
        }
    }

    private class TextTokenizerIteratorNGram implements Iterator<NGram> {
        private final String input;
        private ItData d;
        private boolean hasNext = true;
        private List<String> buffer;
        private int idx = 0, n;

        public TextTokenizerIteratorNGram(String input, int n) {
            this.input = input;
            this.n = n;
            buffer = new ArrayList<>(n);
            d = new ItData(BreakIterator.getWordInstance(locale), input);
            for(int i = 0; i < n && hasNext; i++) {
                findNext(input, d);
                if(d.token != null)
                    buffer.add(d.token);
                else
                    hasNext = false;
            }

        }

        public boolean hasNext() {
            return hasNext;
        }

        public NGram next() {
            NGram rv = null;
            if(idx < n) {
                rv = new NGram(buffer.subList(0, idx+1));
                idx++;
            } else {
                idx = 0;
                findNext(input, d);
                if (d.token != null) {
                    buffer.remove(0);
                    buffer.add(d.token);
                    rv = new NGram(buffer.subList(0, idx+1));
                    idx++;
                } else
                    hasNext = false;
            }
            return rv;
        }
    }

    private void findNext(String input, ItData d) {
        d.token = null;
        boolean done = false;

        while(d.end != BreakIterator.DONE && !done) {
            String candidate = input.substring(d.start, d.end);
            Matcher matcher = text.matcher(candidate);

            StringBuffer sb = new StringBuffer();
            while(matcher.find())
                matcher.appendReplacement(sb, "");
            matcher.appendTail(sb);

            d.start = d.end;
            d.end = d.it.next();
            if(sb.length() > 0) {
                done = true;
                d.token = sb.toString();
            }
        }
    }

    private class ItData {
        protected BreakIterator it;
        protected int start, end;
        protected String token = null;

        public ItData(BreakIterator it, String input) {
            this.it = it;
            it.setText(input);
            start = it.first();
            end = it.next();
        }
    }
}
