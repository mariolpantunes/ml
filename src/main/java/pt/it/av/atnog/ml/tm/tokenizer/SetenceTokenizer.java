package pt.it.av.atnog.ml.tm.tokenizer;

import pt.it.av.atnog.ml.tm.ngrams.NGram;

import java.text.BreakIterator;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by mantunes on 11/18/15.
 */
public class SetenceTokenizer implements Tokenizer {
    private final Locale locale;

    public SetenceTokenizer(Locale locale) {
        this.locale = locale;
    }

    public SetenceTokenizer() {
        this(Locale.getDefault());
    }

    public Iterator<String> tokenize(String input) {
        return new SetenceTokenizerIterator(input);
    }

    @Override
    public Iterator<NGram> tokenize(String input, int n) {
        return null;
    }

    private class SetenceTokenizerIterator implements Iterator<String> {
        private final String input;
        private final BreakIterator it = BreakIterator.getSentenceInstance(locale);
        private int start, end;

        public SetenceTokenizerIterator(String input) {
            this.input = input;
            it.setText(input);
            start = it.first();
            end = it.next();
        }

        public boolean hasNext() {
            return end != BreakIterator.DONE;
        }

        public String next() {
            String rv = input.substring(start, end);
            start = end;
            end = it.next();
            return rv;
        }
    }
}
