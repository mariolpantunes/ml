package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.json.JSONArray;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by mantunes on 3/31/15.
 */
public class Eval {
    public static void main(String[] args) {
        //Load EN-MC-30
        List<Pair<String, String>> pairs = new ArrayList<>();
        double data[] = new double[30];
        try(BufferedReader br = new BufferedReader(new FileReader("en-mc-30.csv"))) {
            int idx = 0;
            String line = br.readLine();
            while(line != null) {
                String split[] = line.split(",");
                pairs.add(new Pair<>(split[0], split[1]));
                data[idx++] = Double.parseDouble(split[2]);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector GT = new Vector(data);

        //Utils.printList(pairs);
        //System.out.println(GT);

        //Load stop words
        List<String> stopWords = null;
        try(BufferedReader br = new BufferedReader(new FileReader("stopwords.json"))) {
            stopWords = loadWordsList(JSONObject.read(br).get("stop words").asArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*for(int n = 1; n < 7; n+=2) {
            for (int docs = 50; docs < 600; docs += 50) {
                Vector H = new Vector(30);
                int idx = 0;
                for (Pair<String, String> p : pairs) {
                    double similarity = TM.tdp_snippet_stemmer(p.a, p.b, stopWords, 3, 15, n,
                            new CachedSearchEngine(new Bing("ZctQ1GPgFid1k72ZUBlKmB/CWfWXiPoFZj3IlChOV1g")));
                    H.set(idx++, similarity);
                    //System.out.println(GT.get(idx - 1) + " <-> " + H.get(idx - 1));
                }
                //System.out.println("Parameters ("+n+"; "+docs+") -> "+GT.corr(H));
                System.out.println(GT.corr(H));
                //System.out.println(GT);
                //System.out.println(H);
                //System.out.println();
            }
            System.out.println();
        }*/
    }

    private static List<String> loadWordsList(JSONArray array) throws FileNotFoundException {
        List<String> rv = new ArrayList<>(array.size());
        for(int i = 0, size = array.size(); i < size; i++)
            rv.add(array.get(i).asString());
        return rv;
    }
}

