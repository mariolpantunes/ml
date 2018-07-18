package pt.it.av.tnav.ml.regression;

import org.junit.Test;
import pt.it.av.tnav.utils.ArrayUtils;
import pt.it.av.tnav.utils.MathUtils;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnivariateRegressionTest {
  @Test
  public void test_lr_slope_positive() {
    double x[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
        y[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    UnivariateRegression.LR lr = UnivariateRegression.lr(x, y);
    assertEquals(lr.a(), 1.0, 0.01);
    assertEquals(lr.b(), 0.0, 0.01);
    assertEquals(lr.r2(), 1.0, 0.01);
  }

  @Test
  public void test_lr_slope_negative() {
    double x[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
        y[] = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    UnivariateRegression.LR lr = UnivariateRegression.lr(x, y);
    assertEquals(lr.a(), -1.0, 0.01);
    assertEquals(lr.b(), 9.0, 0.01);
    assertEquals(lr.r2(), 1.0, 0.01);
  }

  @Test
  public void test_pr_positive() {
    double x[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        y[] = {2, 8, 18, 32, 50, 72, 98, 128, 162, 200};
    UnivariateRegression.PR pr = UnivariateRegression.pr(x, y);
    assertTrue(MathUtils.equals(pr.a(), 2, 0.01));
    assertTrue(MathUtils.equals(pr.b(), 2, 0.01));
    assertTrue(MathUtils.equals(pr.r2(), 1, 0.01));
  }

  @Test
  public void test_pr_negative() {
    double x[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        y[] = {2, 0.5, 0.222222222222222, 0.125, 0.08, 0.055555555555556, 0.040816326530612,
            0.03125, 0.024691358024691, 0.02};
    UnivariateRegression.PR pr = UnivariateRegression.pr(x, y);
    assertTrue(MathUtils.equals(pr.a(), 2, 0.01));
    assertTrue(MathUtils.equals(pr.b(), -2, 0.01));
    assertTrue(MathUtils.equals(pr.r2(), 1, 0.01));
  }

  @Test
  public void test_er_positive() {
    double x[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        y[] = {5.436563657, 14.7781122, 40.17107385, 109.1963001, 296.8263182, 806.857587,
            2193.266317, 5961.915974, 16206.16786, 44052.93159};
    UnivariateRegression.ER er = UnivariateRegression.er(x, y);
    assertTrue(MathUtils.equals(er.a(), 2, 0.01));
    assertTrue(MathUtils.equals(er.b(), 1, 0.01));
    assertTrue(MathUtils.equals(er.r2(), 1, 0.01));
  }

  @Test
  public void test_er_negative() {
    double x[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        y[] = {0.735758882, 0.270670566, 0.099574137, 0.036631278, 0.013475894, 0.004957504,
            0.001823764, 0.000670925, 0.00024682, 9.07999E-05};
    UnivariateRegression.ER er = UnivariateRegression.er(x, y);
    assertTrue(MathUtils.equals(er.a(), 2, 0.01));
    assertTrue(MathUtils.equals(er.b(), -1, 0.01));
    assertTrue(MathUtils.equals(er.r2(), 1, 0.01));
  }

  @Test
  public void test_lnr_positive() {
    double x[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        y[] = {2, 3.386294361, 4.197224577, 4.772588722, 5.218875825, 5.583518938, 5.891820298,
            6.158883083, 6.394449155, 6.605170186};
    UnivariateRegression.LNR lnr = UnivariateRegression.lnr(x, y);
    assertTrue(MathUtils.equals(lnr.a(), 2, 0.01));
    assertTrue(MathUtils.equals(lnr.b(), 2, 0.01));
    assertTrue(MathUtils.equals(lnr.r2(), 1, 0.01));
  }

  @Test
  public void test_lnr_negative() {
    double x[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        y[] = {2, 0.613705639, -0.197224577, -0.772588722, -1.218875825, -1.583518938, -1.891820298,
            -2.158883083, -2.394449155, -2.605170186};
    UnivariateRegression.LNR lnr = UnivariateRegression.lnr(x, y);
    assertTrue(MathUtils.equals(lnr.a(), -2, 0.01));
    assertTrue(MathUtils.equals(lnr.b(), 2, 0.01));
    assertTrue(MathUtils.equals(lnr.r2(), 1, 0.01));
  }
}
