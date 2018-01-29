package pt.it.av.atnog.ml;

import pt.it.av.atnog.ml.clustering.AutoK;
import pt.it.av.atnog.ml.clustering.Cluster;
import pt.it.av.atnog.ml.clustering.Kmeans;
import pt.it.av.atnog.ml.clustering.Kmeanspp;
import pt.it.av.atnog.ml.clustering.curvature.Curvature;
import pt.it.av.atnog.ml.clustering.curvature.DFDT;
import pt.it.av.atnog.ml.clustering.curvature.Kneedle;
import pt.it.av.atnog.ml.clustering.curvature.Lmethod;
import pt.it.av.atnog.ml.clustering.curvature.MengerCurvature;
import pt.it.av.atnog.ml.dataset.ASetsA1;
import pt.it.av.atnog.ml.dataset.Control;
import pt.it.av.atnog.ml.dataset.Dataset;
import pt.it.av.atnog.ml.dataset.Iris;
import pt.it.av.atnog.ml.dataset.SSetsS1;
import pt.it.av.atnog.ml.dataset.Wine;
import pt.it.av.atnog.ml.dataset.Yeast;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Point2D;
import pt.it.av.atnog.utils.structures.Point4D;

import java.util.List;

public class TestCurvature {
  public static void main(String[] args) {
    System.out.println("Curvature Evaluation:");
    int min = 2, max = 100;
    Kmeans alg = new Kmeanspp();
    Curvature kneedle = new Kneedle(),
        lmethod = new Lmethod(),
        menger = new MengerCurvature(),
        dfde = new DFDT();

    // IRIS
    /*System.out.println("Iris dataset (4)");
    Dataset iris = new Iris();

    List<Point4D> irisDps = iris.load();

    List<Cluster<Point4D>> irisClusters = AutoK.elbow(alg, irisDps, min, max, kneedle);
    System.out.println("\tKneedle: " + irisClusters.size());

    irisClusters = AutoK.elbow(alg, irisDps, min, max, lmethod);
    System.out.println("\tLmethod: " + irisClusters.size());

    irisClusters = AutoK.elbow(alg, irisDps, min, max, menger);
    System.out.println("\tMenger: " + irisClusters.size());

    irisClusters = AutoK.elbow(alg, irisDps, min, max, dfde);
    System.out.println("\tDFDE: " + irisClusters.size());

    irisClusters = AutoK.silhouette(alg, irisDps, min, max);
    System.out.println("\tShilhoute: " + irisClusters.size());

    irisClusters = AutoK.clustering(alg, irisDps, min, max, irisDps.get(0).size());
    System.out.println("\tGAP:" + irisClusters.size());

    // Yeast Dataset
    System.out.println("Yeast dataset (10/5)");
    Dataset<Vector> yeast = new Yeast();

    List<Vector> yeastDps = yeast.load();

    List<Cluster<Vector>> yeastClusters = AutoK.elbow(alg, yeastDps, min, max, kneedle);
    System.out.println("\tKneedle: " + yeastClusters.size());

    yeastClusters = AutoK.elbow(alg, yeastDps, min, max, lmethod);
    System.out.println("\tLmethod: " + yeastClusters.size());

    yeastClusters = AutoK.elbow(alg, yeastDps, min, max, menger);
    System.out.println("\tMenger: " + yeastClusters.size());

    yeastClusters = AutoK.elbow(alg, yeastDps, min, max, dfde);
    System.out.println("\tDFDE: " + yeastClusters.size());

    yeastClusters = AutoK.silhouette(alg, yeastDps, min, max);
    System.out.println("\tShilhoute: " + yeastClusters.size());

    yeastClusters = AutoK.clustering(alg, yeastDps, min, max, yeastDps.get(0).size());
    System.out.println("\tGAP:" + yeastClusters.size());

    // Control Dataset
    System.out.println("Control dataset (6)");
    Dataset<Vector> control = new Control();

    List<Vector> controlDps = yeast.load();

    List<Cluster<Vector>> controlClusters = AutoK.elbow(alg, controlDps, min, max, kneedle);
    System.out.println("\tKneedle: " + controlClusters.size());

    controlClusters = AutoK.elbow(alg, controlDps, min, max, lmethod);
    System.out.println("\tLmethod: " + controlClusters.size());

    controlClusters = AutoK.elbow(alg, controlDps, min, max, menger);
    System.out.println("\tMenger: " + controlClusters.size());

    controlClusters = AutoK.elbow(alg, controlDps, min, max, dfde);
    System.out.println("\tDFDE: " + controlClusters.size());

    controlClusters = AutoK.silhouette(alg, controlDps, min, max);
    System.out.println("\tShilhoute: " + controlClusters.size());

    controlClusters = AutoK.clustering(alg, controlDps, min, max, controlDps.get(0).size());
    System.out.println("\tGAP:" + controlClusters.size());

    // Wine Dataset
    System.out.println("Wine dataset (3)");
    Dataset<Vector> wine = new Wine();

    List<Vector> wineDps = yeast.load();

    List<Cluster<Vector>> wineClusters = AutoK.elbow(alg, wineDps, min, max, kneedle);
    System.out.println("\tKneedle: " + wineClusters.size());

    wineClusters = AutoK.elbow(alg, wineDps, min, max, lmethod);
    System.out.println("\tLmethod: " + wineClusters.size());

    wineClusters = AutoK.elbow(alg, wineDps, min, max, menger);
    System.out.println("\tMenger: " + wineClusters.size());

    wineClusters = AutoK.elbow(alg, wineDps, min, max, dfde);
    System.out.println("\tDFDE: " + wineClusters.size());

    wineClusters = AutoK.silhouette(alg, wineDps, min, max);
    System.out.println("\tShilhoute: " + wineClusters.size());

    wineClusters = AutoK.clustering(alg, wineDps, min, max, wineDps.get(0).size());
    System.out.println("\tGAP:" + wineClusters.size());

    // Vehicle Dataset
    System.out.println("Vehicle dataset (4)");
    Dataset<Vector> vehicle = new Wine();

    List<Vector> vehicleDps = yeast.load();

    List<Cluster<Vector>> vehicleClusters = AutoK.elbow(alg, vehicleDps, min, max, kneedle);
    System.out.println("\tKneedle: " + vehicleClusters.size());

    vehicleClusters = AutoK.elbow(alg, vehicleDps, min, max, lmethod);
    System.out.println("\tLmethod: " + vehicleClusters.size());

    vehicleClusters = AutoK.elbow(alg, vehicleDps, min, max, menger);
    System.out.println("\tMenger: " + vehicleClusters.size());

    vehicleClusters = AutoK.elbow(alg, vehicleDps, min, max, dfde);
    System.out.println("\tDFDE: " + vehicleClusters.size());

    vehicleClusters = AutoK.silhouette(alg, vehicleDps, min, max);
    System.out.println("\tShilhoute: " + vehicleClusters.size());

    vehicleClusters = AutoK.clustering(alg, vehicleDps, min, max, vehicleDps.get(0).size());
    System.out.println("\tGAP:" + vehicleClusters.size());

    // S-Set S1 Dataset
    System.out.println("S-sets S1 dataset (15)");
    Dataset<Point2D> s1 = new SSetsS1();

    List<Point2D> s1Dps = s1.load();

    List<Cluster<Point2D>> s1Clusters = AutoK.elbow(alg, s1Dps, min, max, kneedle);
    System.out.println("\tKneedle: " + s1Clusters.size());

    s1Clusters = AutoK.elbow(alg, s1Dps, min, max, lmethod);
    System.out.println("\tLmethod: " + s1Clusters.size());

    s1Clusters = AutoK.elbow(alg, s1Dps, min, max, menger);
    System.out.println("\tMenger: " + s1Clusters.size());

    s1Clusters = AutoK.elbow(alg, s1Dps, min, max, dfde);
    System.out.println("\tDFDE: " + s1Clusters.size());

    s1Clusters = AutoK.silhouette(alg, s1Dps, min, max);
    System.out.println("\tShilhoute: " + s1Clusters.size());

    s1Clusters = AutoK.clustering(alg, s1Dps, min, max, s1Dps.get(0).size());
    System.out.println("\tGAP:" + s1Clusters.size());*/

    // A-Set A1 Dataset
    System.out.println("A-sets A1 dataset (20)");
    Dataset<Point2D> a1 = new ASetsA1();

    List<Point2D> a1Dps = a1.load();

    /*List<Cluster<Point2D>> a1Clusters = AutoK.elbow(alg, a1Dps, min, max, kneedle);
    System.out.println("\tKneedle: " + a1Clusters.size());

    a1Clusters = AutoK.elbow(alg, a1Dps, min, max, lmethod);
    System.out.println("\tLmethod: " + a1Clusters.size());

    a1Clusters = AutoK.elbow(alg, a1Dps, min, max, menger);
    System.out.println("\tMenger: " + a1Clusters.size());*/

    List<Cluster<Point2D>> a1Clusters = AutoK.elbow(alg, a1Dps, min, max, dfde);
    System.out.println("\tDFDE: " + a1Clusters.size());

    /*a1Clusters = AutoK.silhouette(alg, a1Dps, min, max);
    System.out.println("\tShilhoute: " + a1Clusters.size());

    a1Clusters = AutoK.clustering(alg, a1Dps, min, max, a1Dps.get(0).size());
    System.out.println("\tGAP:" + a1Clusters.size());*/
  }
}