package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DKmethodTest {
  @Test
  public void test_elbow() {
    Curvature alg = new DKmethod();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    //System.out.println(elbow);
    assertTrue(elbow == 8);
  }

  @Test
  public void test_knee() {
    Curvature alg = new DKmethod();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    //System.out.println(knee);
    assertTrue(knee == 3);
  }
}
