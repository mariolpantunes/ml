package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DFDTTest {

  @Test
  public void test_elbow() {
    Curvature alg = new DFDT();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    System.out.println(elbow);
    assertEquals(8, elbow);
  }

  @Test
  public void test_knee() {
    Curvature alg = new DFDT();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    assertEquals(14, knee);
  }
}
