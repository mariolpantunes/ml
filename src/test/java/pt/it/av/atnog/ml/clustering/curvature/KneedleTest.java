package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class KneedleTest {
  @Test
  public void test_elbow() {
    Curvature alg = new Kneedle();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    assertTrue(elbow == 4);
    //System.out.println(CurvatureTest.elbow_x[elbow]);
  }

  @Test
  public void test_knee() {
    Curvature alg = new Kneedle();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    assertTrue(knee == 6);
    //System.out.println(CurvatureTest.knee_x[knee]);
  }
}
