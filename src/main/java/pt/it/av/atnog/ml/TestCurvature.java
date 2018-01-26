package pt.it.av.atnog.ml;

import pt.it.av.atnog.ml.clustering.AutoK;
import pt.it.av.atnog.ml.clustering.Cluster;
import pt.it.av.atnog.ml.clustering.Kmeans;
import pt.it.av.atnog.ml.clustering.Kmeanspp;
import pt.it.av.atnog.ml.dataset.*;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Point2D;
import pt.it.av.atnog.utils.structures.Point4D;

import java.util.List;

public class TestCurvature {
  public static void main(String[] args) {
    System.out.println("Curvature Evaluation:");
    int min = 2, max = 40, reps = 100;
    Kmeans alg = new Kmeanspp();

    // IRIS
    System.out.println("Iris dataset (3)");
    Dataset iris = new Iris();

    List<Point4D> irisDps = iris.load();

    AutoK.elbowTest(alg, irisDps, min, max, reps);

    List<Cluster<Point4D>> irisClusters = AutoK.silhouette(alg, irisDps, min, max);
    System.out.println("\tShilhoute: " + irisClusters.size());

    irisClusters = AutoK.clustering(alg, irisDps, min, max, irisDps.get(0).size());
    System.out.println("\tGAP:" + irisClusters.size());

    // Yeast Dataset
    System.out.println("Yeast dataset (10/5)");
    Dataset<Vector> yeast = new Yeast();

    List<Vector> yeastDps = yeast.load();

    AutoK.elbowTest(alg, yeastDps, min, max, reps);

    List<Cluster<Vector>> yeastClusters = AutoK.silhouette(alg, yeastDps, min, max);
    System.out.println("\tShilhoute: " + yeastClusters.size());

    yeastClusters = AutoK.clustering(alg, yeastDps, min, max, yeastDps.get(0).size());
    System.out.println("\tGAP:" + yeastClusters.size());

    // Control Dataset
    System.out.println("Control dataset (6)");
    Dataset<Vector> control = new Control();

    List<Vector> controlDps = yeast.load();

    AutoK.elbowTest(alg, controlDps, min, max, reps);

    List<Cluster<Vector>> controlClusters = AutoK.silhouette(alg, controlDps, min, max);
    System.out.println("\tShilhoute: " + controlClusters.size());

    controlClusters = AutoK.clustering(alg, controlDps, min, max, controlDps.get(0).size());
    System.out.println("\tGAP:" + controlClusters.size());

    // Wine Dataset
    System.out.println("Wine dataset (3)");
    Dataset<Vector> wine = new Wine();

    List<Vector> wineDps = yeast.load();

    AutoK.elbowTest(alg, wineDps, min, max, reps);

    List<Cluster<Vector>> wineClusters = AutoK.silhouette(alg, wineDps, min, max);
    System.out.println("\tShilhoute: " + wineClusters.size());

    wineClusters = AutoK.clustering(alg, wineDps, min, max, wineDps.get(0).size());
    System.out.println("\tGAP:" + wineClusters.size());

    // Vehicle Dataset
    System.out.println("Vehicle dataset (4)");
    Dataset<Vector> vehicle = new Wine();

    List<Vector> vehicleDps = vehicle.load();

    AutoK.elbowTest(alg, vehicleDps, min, max, reps);

    List<Cluster<Vector>> vehicleClusters = AutoK.silhouette(alg, vehicleDps, min, max);
    System.out.println("\tShilhoute: " + vehicleClusters.size());

    vehicleClusters = AutoK.clustering(alg, vehicleDps, min, max, vehicleDps.get(0).size());
    System.out.println("\tGAP:" + vehicleClusters.size());

    // S-Set S1 Dataset
    System.out.println("S-sets S1 dataset (15)");
    Dataset<Point2D> s1 = new SSetsS1();

    List<Point2D> s1Dps = s1.load();

    AutoK.elbowTest(alg, s1Dps, min, max, reps);

    List<Cluster<Point2D>> s1Clusters = AutoK.silhouette(alg, s1Dps, min, max);
    System.out.println("\tShilhoute: " + s1Clusters.size());

    s1Clusters = AutoK.clustering(alg, s1Dps, min, max, s1Dps.get(0).size());
    System.out.println("\tGAP:" + s1Clusters.size());

    // A-Set A1 Dataset
    System.out.println("A-sets A1 dataset (20)");
    Dataset<Point2D> a1 = new ASetsA1();

    List<Point2D> a1Dps = a1.load();

    AutoK.elbowTest(alg, a1Dps, min, max, reps);

    List<Cluster<Point2D>> a1Clusters = AutoK.silhouette(alg, a1Dps, min, max);
    System.out.println("\tShilhoute: " + a1Clusters.size());

    a1Clusters = AutoK.clustering(alg, a1Dps, min, max, a1Dps.get(0).size());
    System.out.println("\tGAP:" + a1Clusters.size());
  }
}