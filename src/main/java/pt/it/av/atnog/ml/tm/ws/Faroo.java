package pt.it.av.atnog.ml.tm.ws;

import com.eclipsesource.json.JsonObject;
import pt.it.av.atnog.utils.HTTP;
import java.lang.Exception;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.System;

public class Faroo {
    private final String key;

    public Faroo(final String key) {
        this.key = key;
    }

    public void search(String q) {
        String url = url(q);
        try {
            JsonObject json = JsonObject.readFrom(HTTP.get(url));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String url(String q) {
        return url(q, 1, 10, 20, Src.WEB, true, false);
    }

    private String url(String q, int start, int lenght, int rlength,
                       Src src, boolean kwic, boolean i) {
        StringBuilder sb = new StringBuilder("http://www.faroo.com/api?");
        sb.append("q=" + q + "&");
        sb.append("start=" + start + "&");
        sb.append("length=" + lenght + "&");
        sb.append("rlength=" + rlength + "&");
        sb.append("src=" + enum2Src(src) + "&");
        sb.append("kwic=" + kwic + "&");
        sb.append("i=" + i + "&");
        sb.append("l=en&f=json&key=" + key);

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
