package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RmethodTest {

  @Test
  public void test_elbow() {
    Curvature alg = new Rmethod();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    assertEquals(9, elbow);
  }

  @Test
  public void test_knee() {
    Curvature alg = new Rmethod();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    assertEquals(2, knee);
  }
}
