package pt.it.av.atnog.ml.tm.tokenizer;

import java.text.BreakIterator;
import java.text.Normalizer;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mantunes on 11/17/15.
 */
public class TextTokenizer implements Tokenizer {
    private final Locale locale;
    private final Pattern pattern;

    public TextTokenizer(Locale locale) {
        this.locale = locale;
        pattern = Pattern.compile("('s|[^a-zA-Z-]+)");
    }

    public TextTokenizer() {
        this(Locale.getDefault());
    }

    public Iterator<String> tokenize(String input) {
        return new TextTokenizerIterator(Normalizer.normalize(input.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", ""));
    }

    private class TextTokenizerIterator implements Iterator<String> {
        private final String input;
        private final BreakIterator it = BreakIterator.getWordInstance(locale);
        private int start, end;
        private boolean hasNext = true;
        private String token;

        public TextTokenizerIterator(String input) {
            this.input = input;
            it.setText(input);
            start = it.first();
            end = it.next();
            process();
        }

        public boolean hasNext() {
            return hasNext;
        }

        public String next() {
            String rv = token;
            process();
            return rv;
        }

        private void process() {
            boolean done = false;

            while(end != BreakIterator.DONE && !done) {
                String word = input.substring(start, end);
                Matcher matcher = pattern.matcher(word);

                StringBuffer sb = new StringBuffer();
                while(matcher.find()) {
                    matcher.appendReplacement(sb, "");
                }
                matcher.appendTail(sb);

                start = end;
                end = it.next();
                if(sb.length() > 0) {
                    done = true;
                    token = sb.toString();
                }
            }

            if(!done)
                hasNext = false;
        }
    }
}
