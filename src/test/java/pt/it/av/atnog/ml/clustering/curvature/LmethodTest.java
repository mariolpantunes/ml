package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

public class LmethodTest {
  @Test
  public void test_elbow() {
    Curvature alg = new Lmethod();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    System.out.println(elbow);
    //assertTrue(elbow == 4);
  }

  @Test
  public void test_knee() {
    Curvature alg = new Lmethod();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    System.out.println(knee);
    //assertTrue(knee == 6);
  }
}
