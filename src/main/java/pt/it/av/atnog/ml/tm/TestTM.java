package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.ml.tm.lexicalPattern.EAndOtherC;
import pt.it.av.atnog.ml.tm.ngrams.NGram;
import pt.it.av.atnog.ml.tm.stemmer.Stemmer;
import pt.it.av.atnog.ml.tm.stemmer.PorterStemmer;
import pt.it.av.atnog.ml.tm.lexicalPattern.EIsAC;
import pt.it.av.atnog.ml.tm.lexicalPattern.LexicalPattern;
import pt.it.av.atnog.ml.tm.tokenizer.TokenizerOld;
import pt.it.av.atnog.utils.json.JSONArray;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mantunes on 6/10/15.
 */
public class TestTM {
    public static void main(String[] args) {

        Stemmer s = new PorterStemmer();

        String snippet = "The banana is an edible fruit, botanically a berry, produced by several kinds of large herbaceous flowering plants in the genus Musa. In some countries ...";

        //Test SyntaticPatterns...
        LexicalPattern eIsAC = new EIsAC(s);
        eIsAC.ngram(NGram.Unigram("banana"));
        System.out.println(eIsAC.query());
        //System.out.println(eIsAC.extract(TokenizerOld.text(snippet), 3));

        String snippet2 = "As they ripen, bananas, apples, kiwi fruit, tomatoes, figs, pears and some other fruits";
        LexicalPattern eAndOtherC = new EAndOtherC(s);
        eAndOtherC.ngram(NGram.Unigram("banana"));
        System.out.println(eAndOtherC.query());
        //System.out.println(eAndOtherC.extract(TokenizerOld.text(snippet2), 3));


        /*List<String> stopWords = null;
        try(BufferedReader br = new BufferedReader(new FileReader("stopWords.json"))) {
            stopWords = loadWordsList(JSONObject.read(br).get("stop words").asArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<SearchEngine> lse = new ArrayList<>(2);
        lse.add(new Faroo("XWyjTp6De1S0axhVD14voLV5vjk_"));
        lse.add(new Bing("ZctQ1GPgFid1k72ZUBlKmB/CWfWXiPoFZj3IlChOV1g"));
        //lse.add(new YaCy());
        //lse.add(new Searx());
        SearchEngine se = new CollectionSearchEngine(lse);

        /*DPW dp1 = TM.learnDP(new Unigram("temperature"), se, stopWords);
        System.out.println(dp1);
        DPW dp2 = TM.learnDP(new Unigram("temperature"), se, stopWords);
        System.out.println(dp2);
        System.out.println("Similarity: "+dp1.similarity(dp2));*/

    }

    private static List<String> loadWordsList(JSONArray array) throws FileNotFoundException {
        List<String> rv = new ArrayList<>(array.size());
        for(int i = 0, size = array.size(); i < size; i++)
            rv.add(array.get(i).asString());
        return rv;
    }
}
