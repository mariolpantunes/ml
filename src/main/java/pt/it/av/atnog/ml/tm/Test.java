package pt.it.av.atnog.ml.tm;

import pt.it.av.atnog.utils.bla.Matrix;

/**
 * Created by mantunes on 16/02/2015.
 */
public class Test {
    public static void main(String[] args) {
        double data[] = {0,0,1,1.0/2.0,0,0,1.0/2.0,1,0};
        Matrix M = new Matrix(3,3,data);
        PageRank.pageRank(M, 0.7);
    }
}
