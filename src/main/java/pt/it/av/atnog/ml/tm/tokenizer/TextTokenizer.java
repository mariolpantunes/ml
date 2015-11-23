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
        while (matcher.find())
            matcher.appendReplacement(sb, "");
        matcher.appendTail(sb);
        return sb.toString();
    }

    public Iterator<String> tokenizeIterator(String input) {
        return new TextTokenizerIteratorString(normalize(input));
    }

    public List<String> tokenizeList(String input) {
        Iterator<String> it = this.tokenizeIterator(input);
        List<String> rv = new ArrayList<>();
        while(it.hasNext())
            rv.add(it.next());
        return rv;
    }

    public Iterator<NGram> tokenizeIterator(String input, int n) {
        return new TextTokenizerIteratorNGram(normalize(input), n);
    }

    private class TextTokenizerIteratorString implements Iterator<String> {
        private final String input;
        private final IteratorParameters p;
        private boolean hasNext = true;

        public TextTokenizerIteratorString(String input) {
            this.input = input;
            p = new IteratorParameters(BreakIterator.getWordInstance(locale), input);
            findNext(input, p);
            if (p.token == null)
                hasNext = false;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public String next() {
            String rv = p.token;
            findNext(input, p);
            if (p.token == null)
                hasNext = false;
            return rv;
        }
    }

    private class TextTokenizerIteratorNGram implements Iterator<NGram> {
        private final String input;
        private final IteratorParameters p;
        private final List<String> buffer;
        private boolean hasNext = true;
        private int idx = 0, n;

        public TextTokenizerIteratorNGram(String input, int n) {
            this.input = input;
            this.n = n;
            buffer = new ArrayList<>(n);
            p = new IteratorParameters(BreakIterator.getWordInstance(locale), input);
            for (int i = 0; i < n && hasNext; i++) {
                findNext(input, p);
                if (p.token != null)
                    buffer.add(p.token);
                else
                    hasNext = false;
            }

        }

        public boolean hasNext() {
            return hasNext;
        }

        public NGram next() {
            NGram rv = null;
            if (idx < n) {
                rv = new NGram(buffer.subList(0, idx + 1));
                idx++;
            } else {
                idx = 0;
                findNext(input, p);
                if (p.token != null) {
                    buffer.remove(0);
                    buffer.add(p.token);
                    rv = new NGram(buffer.subList(0, idx + 1));
                    idx++;
                } else
                    hasNext = false;
            }
            return rv;
        }
    }

    private void findNext(String input, IteratorParameters p) {
        p.token = null;
        boolean done = false;

        while (p.end != BreakIterator.DONE && !done) {
            String candidate = input.substring(p.start, p.end);
            Matcher matcher = text.matcher(candidate);

            StringBuffer sb = new StringBuffer();
            while (matcher.find())
                matcher.appendReplacement(sb, "");
            matcher.appendTail(sb);

            p.start = p.end;
            p.end = p.it.next();
            if (sb.length() > 0) {
                done = true;
                p.token = sb.toString();
            }
        }
    }

    private class IteratorParameters {
        protected BreakIterator it;
        protected int start, end;
        protected String token = null;

        public IteratorParameters(BreakIterator it, String input) {
            this.it = it;
            it.setText(input);
            start = it.first();
            end = it.next();
        }
    }
}
