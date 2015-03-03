package pt.it.av.atnog.ml.tm.ws;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pt.it.av.atnog.utils.HTTP;

import java.util.ArrayList;
import java.util.List;

public class Faroo {
    private static final int LENGTH = 10;
    private static final long SLEEP = 100;
    private final String key;

    public Faroo(final String key) {
        this.key = key;
    }

    public List<String> search(final String q) {
        List<String> rv = new ArrayList<>();
        boolean done = false;
        int start = 1;
        while (!done) {
            try {
                JsonObject json = JsonObject.readFrom(HTTP.get(url(q, start)));
                System.out.println("Count " + json.get("count").asInt() + " total " + (start * LENGTH));
                if (start * LENGTH >= json.get("count").asInt())
                    done = true;
                else
                    start++;
                JsonArray results = json.get("results").asArray();
                for (JsonValue jv : results) {
                    System.out.println(jv.asObject().get("url").asString());
                    try {
                        Document doc = Jsoup.parse(HTTP.get(jv.asObject().get("url").asString()));
                        rv.add(doc.body().text());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                done = true;
            }
        }
        return rv;
    }

    public List<String> snippets(final String q) {
        List<String> rv = new ArrayList<>();
        boolean done = false;
        int start = 1;
        while (!done) {
            try {
                JsonObject json = JsonObject.readFrom(HTTP.get(url(q, start)));
                JsonArray results = json.get("results").asArray();
                for (JsonValue jv : results) {
                    rv.add(jv.asObject().get("kwic").asString());
                }
                if (start * LENGTH >= json.get("count").asInt())
                    done = true;
                else
                    start++;
                Thread.sleep(SLEEP);
            } catch (Exception e) {
                e.printStackTrace();
                done = true;
            }
        }
        return rv;
    }

    private String url(String q, int start) {
        return url(q, start, Src.WEB, true, false);
    }

    private String url(String q) {
        return url(q, 1, Src.WEB, true, false);
    }

    private String url(String q, int start,
                       Src src, boolean kwic, boolean i) {
        StringBuilder sb = new StringBuilder("http://www.faroo.com/api?");
        sb.append("q=" + q + "&");
        sb.append("start=" + start + "&");
        sb.append("src=" + enum2Src(src) + "&");
        sb.append("kwic=" + kwic + "&");
        sb.append("i=" + i + "&");
        sb.append("length=10&rlength=20&l=en&f=json&key=" + key);
        return sb.toString();
    }

    private String enum2Src(Src src) {
        String rv = null;

        switch (src) {
            case WEB:
                rv = "web";
                break;
            case NEWS:
                rv = "news";
                break;
            case TOPICS:
                rv = "topics";
                break;
            case TRENDS:
                rv = "trends";
                break;
            case SUGGEST:
                rv = "suggest";
                break;
        }

        return rv;
    }


    public enum Src {
        WEB, NEWS, TOPICS, TRENDS, SUGGEST
    }
}
