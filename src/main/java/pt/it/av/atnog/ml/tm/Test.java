package pt.it.av.atnog.ml.tm;

import java.util.List;

/**
 * Created by mantunes on 2/2/15.
 */
public class Test {
    public static void main(String[] args) {
        List<String> bow = Tokenizer.twitter("#acendalhas @Paulão mete fogo aí: http://www.google.com....");
        for(String w : bow) {
            System.out.print(w+";");
        }
    }
}
