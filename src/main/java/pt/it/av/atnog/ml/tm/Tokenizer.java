package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.Utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mantunes on 2/2/15.
 */
public class Tokenizer {

    public static List<String> twitter(String txt) {
        List<String> rv = new ArrayList<String>();
        txt = Normalizer.normalize(txt.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        String words[] = txt.split("\\s+");
        for(String w : words) {
            if(!w.startsWith("#") && !w.startsWith("@") && !w.startsWith("http"))
                rv.addAll(Arrays.asList(w.replaceAll("[^a-zA-Z\\s]", " ")
                        .replaceAll("\\s+", " ").split("\\s+")));
        }
        return rv;
    }
}
