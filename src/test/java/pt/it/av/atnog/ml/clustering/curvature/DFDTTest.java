package pt.it.av.atnog.ml.clustering.curvature;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DFDTTest {

  @Test
  public void test_elbow() {
    Curvature alg = new DFDT();
    int elbow = alg.elbow(CurvatureTest.elbow_x, CurvatureTest.elbow_y);
    assertEquals(8, elbow);
  }

  @Test
  public void test_elbow_slope_zero() {
    Curvature alg = new DFDT();
    final double[] x = new double[]{1, 2, 3};
    final double[] y = new double[]{0, 0, 0};
    int elbow = alg.elbow(x, y);
    assertEquals(-1, elbow);
  }

  @Test
  public void test_elbow_0() {
    Curvature alg = new DFDT();
    final double[] x = new double[]{1, 2, 3};
    final double[] y = new double[]{0, 0.1, 0.1};
    int elbow = alg.elbow(x, y);
    assertEquals(1, elbow);
  }

  @Test
  public void test_knee() {
    Curvature alg = new DFDT();
    int knee = alg.knee(CurvatureTest.knee_x, CurvatureTest.knee_y);
    assertEquals(14, knee);
  }

  @Test
  public void test_knee_slope_zero() {
    Curvature alg = new DFDT();
    final double[] x = new double[]{1, 2, 3};
    final double[] y = new double[]{0, 0, 0};
    int elbow = alg.knee(x, y);
    assertEquals(-1, elbow);
  }

  @Test
  public void test_knee_minimal() {
    Curvature alg = new DFDT();
    final double[] x = new double[]{1, 2, 3};
    final double[] y = new double[]{0, 0.1, 0.1};
    int knee = alg.knee(x, y);
    assertEquals(1, knee);
  }

  @Test
  public void test_knee_few() {
    Curvature alg = new DFDT();
    final double[] x = new double[]{1, 2};
    final double[] y = new double[]{0.1934, 0.193};
    int knee = alg.knee(x, y);
    assertEquals(-1, knee);
  }
}
