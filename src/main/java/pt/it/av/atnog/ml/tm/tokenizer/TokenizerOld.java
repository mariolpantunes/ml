package pt.it.av.atnog.ml.tm.tokenizer;

import pt.it.av.atnog.utils.StringUtils;

import java.text.BreakIterator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by mantunes on 2/2/15.
 */
//TODO: Locale as a parameter
public class TokenizerOld {
    public static List<String> twitter(String input) {
        List<String> rv = new ArrayList<>();
        input = Normalizer.normalize(input.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        String words[] = input.split("\\s+");
        for (String w : words) {
            if (!w.startsWith("#") && !w.startsWith("@") && !w.startsWith("http"))
                rv.addAll(Arrays.asList(w.replaceAll("[^a-zA-Z\\s]", " ")
                        .replaceAll("\\s+", " ").split("\\s+")));
        }
        return rv;
    }
}
