package pt.it.av.tnav.ml.clustering;

import org.junit.Test;

import pt.it.av.tnav.ml.clustering.cluster.Cluster;
import pt.it.av.tnav.utils.structures.point.Point1D;
import pt.it.av.tnav.utils.structures.point.Point2D;

import static org.junit.Assert.assertTrue;


/**
 * Unit tests for Cluster.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class ClusterTest {
  public Point2D p0,p1,p2,p3,p4;

  @org.junit.Before
  public void setup() {
    p0 = new Point2D(0,0);
    p1 = new Point2D(1,0);
    p2 = new Point2D(0,1);
    p3 = new Point2D(-1,0);
    p4 = new Point2D(0,-1);
  }

  @Test
  public void test_center_zero() {
    Cluster<Point1D> cluster = new Cluster<Point1D>();
    assertTrue(cluster.center() == null);
  }

  @Test
  public void test_center_one() {
    Cluster<Point2D> cluster = new Cluster<Point2D>(p0);
    assertTrue(cluster.center().equals(p0));
  }

  @Test
  public void test_center_multiple() {
    Cluster<Point2D> cluster = new Cluster<>(p0);
    assertTrue(cluster.center() == p0);
    cluster.add(p1);
    cluster.add(p2);
    cluster.add(p3);
    cluster.add(p4);
    assertTrue(cluster.center() == p0);

    cluster = new Cluster<>(p1);
    assertTrue(cluster.center() == p1);
    cluster.add(p0);
    cluster.add(p2);
    cluster.add(p3);
    cluster.add(p4);
    assertTrue(cluster.center() == p0);
  }

  @Test
  public void test_distortion() {
    Cluster<Point2D> cluster = new Cluster<>(p0);
    cluster.add(p1);
    cluster.add(p2);
    cluster.add(p3);
    cluster.add(p4);
    assertTrue(cluster.distortion() == 4.0);

    cluster = new Cluster<>(p0);
    cluster.add(p1);
    cluster.add(p2);
    assertTrue(cluster.distortion() == 2.0);
  }

  @Test
  public void test_maxRadius() {
    Cluster<Point2D> cluster = new Cluster<>(p0);
    cluster.add(p1);
    cluster.add(p2);
    cluster.add(p3);
    cluster.add(p4);
    assertTrue(cluster.maxRadius() == 1.0);
  }

  @Test
  public void test_avgRadius() {
    Cluster<Point2D> cluster = new Cluster<>(p0);
    cluster.add(p1);
    cluster.add(p2);
    cluster.add(p3);
    cluster.add(p4);
    assertTrue(cluster.avgRadius() == 4.0/5.0);
  }
}
