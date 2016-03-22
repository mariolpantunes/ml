package pt.it.av.atnog.ml.clustering;


import static org.junit.Assert.assertTrue;

/**
 * Unit tests for K-medoid cluster.
 */
public class KmedoidClusterTest {
    public Element<Point2D> p0,p1,p2,p3,p4;

    @org.junit.Before
    public void setup() {
        p0 = new Element<>(new Point2D(0,0));
        p1 = new Element<>(new Point2D(1,0));
        p2 = new Element<>(new Point2D(0,1));
        p3 = new Element<>(new Point2D(-1,0));
        p4 = new Element<>(new Point2D(0,-1));
    }

    @org.junit.Test
    public void test_distortion() {
        KmedoidCluster<Element<Point2D>> cluster = new KmedoidCluster<>(p0);
        cluster.add(p1);
        cluster.add(p2);
        cluster.add(p3);
        cluster.add(p4);
        assertTrue(cluster.distortion() == 4.0);

        cluster = new KmedoidCluster<>(p1);
        cluster.add(p0);
        cluster.add(p2);
        cluster.add(p3);
        cluster.add(p4);
        assertTrue(cluster.distortion() == 9.0);
    }

    @org.junit.Test
    public void test_updateMedoid() {
        KmedoidCluster<Element<Point2D>> cluster = new KmedoidCluster<>(p0);
        assertTrue(cluster.medoid() == p0);
        cluster.add(p1);
        cluster.add(p2);
        cluster.add(p3);
        cluster.add(p4);
        cluster.updateMedoid();
        assertTrue(cluster.medoid() == p0);

        cluster = new KmedoidCluster<>(p1);
        assertTrue(cluster.medoid() == p1);
        cluster.add(p0);
        cluster.add(p2);
        cluster.add(p3);
        cluster.add(p4);
        cluster.updateMedoid();
        assertTrue(cluster.medoid() == p0);
    }
}
