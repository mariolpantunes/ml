package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MengerCurvatureTest {
  @Test
  public void test_elbow() {
    Curvature alg = new MengerCurvature();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    assertTrue(elbow == 2);
    //System.out.println(CurvatureTest.elbow_x[elbow]);
  }
}
