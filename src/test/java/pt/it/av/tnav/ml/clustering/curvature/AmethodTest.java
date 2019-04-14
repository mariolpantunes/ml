package pt.it.av.tnav.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AmethodTest {
  @Test
  public void test_elbow() {
    Curvature alg = new Amethod();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    assertEquals(13, elbow);
  }

  @Test
  public void test_knee() {
    Curvature alg = new Amethod();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    assertEquals(1, knee);
  }
}
