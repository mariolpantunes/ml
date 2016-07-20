package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.ml.tm.tokenizer.JSONTokenizer;
import pt.it.av.atnog.ml.tm.tokenizer.TextTokenizer;
import pt.it.av.atnog.ml.tm.tokenizer.Tokenizer;
import pt.it.av.atnog.utils.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

/**
 * Represent a bag of words.
 * It eases the use of several text mining methods.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class BagWords {
    private Tokenizer plainT = new TextTokenizer();
    JSONTokenizer jsonT = new JSONTokenizer(plainT);


    public BagWords(final String payload) {
        JSONObject json = null;
        boolean isJSON = true;

        try {
            json = JSONObject.read(payload);
        } catch (IOException e) {
            isJSON = false;
        }

        Iterator<String> tokensIt;

        if(isJSON)
            tokensIt = jsonT.tokenizeIt(json);
        else
            tokensIt = plainT.tokenizeIt(payload);


    }
}