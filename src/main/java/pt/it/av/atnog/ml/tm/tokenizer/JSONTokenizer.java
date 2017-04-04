package pt.it.av.atnog.ml.tm.tokenizer;

import pt.it.av.atnog.utils.json.JSONObject;
import pt.it.av.atnog.utils.json.JSONValue;

import java.io.IOException;
import java.util.*;

/**
 * Implements a tokenizer specific for JSON documents.
 * The current implementation does not take full advantage of the iterator method.
 * Also, it relies heavily on the {@link TextTokenizer}.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class JSONTokenizer implements Tokenizer{
    private final Tokenizer t;

    /**
     *
     * @param t
     */
    public JSONTokenizer(final Tokenizer t) {
        this.t = t;
    }

    /**
     *
     * @param locale
     */
    public JSONTokenizer(Locale locale) {
        this(new TextTokenizer(locale));
    }

    /**
     *
     */
    public JSONTokenizer() {
        this(new TextTokenizer());
    }

    @Override
    public Iterator<String> tokenizeIt(String input) {
        return tokenize(input).iterator();
    }

    /**
     *
     * @param json
     * @return
     */
    public Iterator<String> tokenizeIt(JSONObject json) { return tokenize(json).iterator(); }

    @Override
    public List<String> tokenize(String input) {
        List<String> rv = null;
        JSONObject json = null;

        try {
            json = JSONObject.read(input);
        } catch (IOException e) {
            e.printStackTrace();
            json = null;
            rv = new ArrayList<>(0);
        }

        if(json != null)
            rv = tokenize(json);

        return rv;
    }

    /**
     *
     * @param json
     * @return
     */
    public List<String> tokenize(JSONObject json) {
        List<String> rv = new ArrayList<>();

        Deque<JSONValue> stack = new ArrayDeque();
        stack.push(json);

        while(!stack.isEmpty()) {
            JSONValue values = stack.pop();
            if(values.isArray()) {
                for(JSONValue v : values.asArray()) {
                    if(v.isString())
                        rv.addAll(t.tokenize(v.asString()));
                    else if(v.isObject() || v.isArray())
                        stack.add(v);
                }
            } else if(values.isObject()) {
                for(Map.Entry<String, JSONValue> e : values.asObject().entrySet()) {
                    rv.addAll(t.tokenize(e.getKey()));
                    JSONValue v = e.getValue();
                    if (v.isString())
                        rv.addAll(t.tokenize(v.asString()));
                    else if(v.isObject() || v.isArray())
                        stack.add(v);
                }
            }
        }

        return rv;
    }
}
