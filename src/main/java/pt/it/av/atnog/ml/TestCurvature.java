package pt.it.av.atnog.ml;

import pt.it.av.atnog.ml.clustering.Kmeans;
import pt.it.av.atnog.ml.clustering.Kmeanspp;
import pt.it.av.atnog.ml.clustering.cluster.Cluster;
import pt.it.av.atnog.ml.clustering.cluster.ClusterUtils;
import pt.it.av.atnog.ml.clustering.curvature.*;
import pt.it.av.atnog.ml.clustering.density.DBSCAN;
import pt.it.av.atnog.ml.clustering.hierarchical.Hierarchical;
import pt.it.av.atnog.ml.clustering.hierarchical.SLINK;
import pt.it.av.atnog.ml.dataset.*;
import pt.it.av.atnog.utils.ArrayUtils;
import pt.it.av.atnog.utils.MathUtils;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Distance;
import pt.it.av.atnog.utils.structures.Point2D;
import pt.it.av.atnog.utils.structures.Point4D;

import java.util.ArrayList;
import java.util.List;

public class TestCurvature {
  public static void main(String[] args) {
    System.out.println("Curvature Evaluation:");
    int min = 2, max = 20, reps = 100,
        dmin = 2, dmax = 5;
    Kmeans kmeans = new Kmeanspp();
    SLINK slink = new SLINK();

    // IRIS
    System.out.println("Iris dataset (3)");
    Dataset iris = new Iris();

    List<Point4D> irisDps = iris.load();

    System.out.println("K-means++");
    elbowTest(kmeans, irisDps, (int) (Math.round(iris.classes() / 2.0)), iris.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, irisDps);
    //System.out.println();
    //System.out.println("DBSCAN");
    //dbscanElbowTest(irisDps,dmin,dmax);

    // Yeast Dataset
    System.out.println();
    System.out.println("Yeast dataset (10/5)");
    Dataset<Vector> yeast = new Yeast();

    List<Vector> yeastDps = yeast.load();

    System.out.println("K-means++");
    elbowTest(kmeans, yeastDps, min, max, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, yeastDps);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(yeastDps,dmin,dmax);

    // Control Dataset
    System.out.println();
    System.out.println("Control dataset (6)");
    Dataset<Vector> control = new Control();

    List<Vector> controlDps = control.load();

    System.out.println("K-means++");
    elbowTest(kmeans, controlDps, min, max, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, controlDps);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(controlDps,dmin,dmax);

    // Wine Dataset
    System.out.println();
    System.out.println("Wine dataset (3)");
    Dataset<Vector> wine = new Wine();

    List<Vector> wineDps = wine.load();

    System.out.println("K-means++");
    elbowTest(kmeans, wineDps, min, max, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, wineDps);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(wineDps,dmin,dmax);

    // Vehicle Dataset
    System.out.println();
    System.out.println("Vehicle dataset (4)");
    Dataset<Vector> vehicle = new Vehicle();

    List<Vector> vehicleDps = vehicle.load();

    System.out.println("K-means++");
    elbowTest(kmeans, vehicleDps, min, max, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, vehicleDps);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(vehicleDps,dmin,dmax);

    // A-Set A1 Dataset
    System.out.println();
    System.out.println("A-sets A1 dataset (20)");
    Dataset<Point2D> a1 = new ASetsA1();

    List<Point2D> a1Dps = a1.load();

    System.out.println("K-means++");
    elbowTest(kmeans, a1Dps, min, max, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, a1Dps);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(a1Dps,dmin,dmax);

    // A-Set A2 Dataset
    System.out.println();
    System.out.println("A-sets A2 dataset (35)");
    Dataset<Point2D> a2 = new ASetsA2();

    List<Point2D> a2Dps = a2.load();

    System.out.println("K-means++");
    elbowTest(kmeans, a2Dps, min, max, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, a2Dps);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(a2Dps,dmin,dmax);

    // A-Set A3 Dataset
    System.out.println();
    System.out.println("A-sets A3 dataset (50)");
    Dataset<Point2D> a3 = new ASetsA3();

    List<Point2D> a3Dps = a3.load();

    System.out.println("K-means++");
    elbowTest(kmeans, a3Dps, min, max, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, a3Dps);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(a3Dps,dmin,dmax);
  }

  public static <D extends Distance<D>> void elbowTest(final Kmeans alg, final List<D> dps,
                                                       final int min, final int max, final int reps) {
    final int kmax = (max < dps.size()) ? max : dps.size() - 1;
    double wsss[] = new double[(kmax - min) + 1],
        x[] = new double[(kmax - min) + 1];

    List<String> header = new ArrayList<>();
    header.add("Kneedle");
    header.add("L-method");
    header.add("S-method");
    header.add("Menger Curvature");
    header.add("DFDT");
    header.add("DSDT");
    header.add("R-Method");
    header.add("DK-method");

    List<Curvature> curv = new ArrayList<>();
    curv.add(new Kneedle());
    curv.add(new Lmethod());
    curv.add(new Smethod());
    curv.add(new MengerCurvature());
    curv.add(new DFDT());
    curv.add(new DSDT());
    curv.add(new Rmethod());
    curv.add(new DKmethod());

    int i = 0;
    for (int k = min; k <= kmax; k++, i++) {
      x[i] = k;
      List<Cluster<D>> clusters = alg.clustering(dps, k);
      double wss = ClusterUtils.avgDistortion(clusters);
      for (int j = 1; j < reps; j++) {
        List<Cluster<D>> currentClusters = alg.clustering(dps, k);
        double cwss = ClusterUtils.avgDistortion(currentClusters);
        if (cwss > wss && !ClusterUtils.emptyClusters(currentClusters)) {
          wss = cwss;
        }
      }
      wsss[i] = wss;
    }


    for (i = 0; i < curv.size(); i++) {
      int idx = curv.get(i).elbow(x, wsss);
      System.out.println("\t" + header.get(i) + " -> " + x[idx]);
    }
  }

  public static <D extends Distance<D>> void hiearchicalElbowTest(Hierarchical alg, final List<D> dps) {
    List<String> header = new ArrayList<>();
    header.add("Kneedle");
    header.add("L-method");
    header.add("S-method");
    header.add("Menger Curvature");
    header.add("DFDT");
    header.add("DSDT");
    header.add("R-Method");
    header.add("DK-method");

    List<Curvature> curv = new ArrayList<>();
    curv.add(new Kneedle());
    curv.add(new Lmethod());
    curv.add(new Smethod());
    curv.add(new MengerCurvature());
    curv.add(new DFDT());
    curv.add(new DSDT());
    curv.add(new Rmethod());
    curv.add(new DKmethod());

    int d[][] = alg.clustering(dps);
    double x[] = new double[dps.size() - 1], y[] = new double[dps.size() - 1];

    // Create individual clusters
    List<Cluster<D>> clusters = new ArrayList<>(dps.size());
    for (int i = 0; i < dps.size(); i++) {
      clusters.add(new Cluster<D>(dps.get(i)));
    }

    clusters.get(d[0][1]).addAll(clusters.get(d[0][0]));
    clusters.set(d[0][0], null);
    x[dps.size() - 2] = dps.size() - 1;
    y[dps.size() - 2] = ClusterUtils.avgDistortion(clusters);

    // Merge clusters
    for (int i = 1; i < d.length; i++) {
      clusters.get(d[i][1]).addAll(clusters.get(d[i][0]));
      clusters.set(d[i][0], null);
      x[dps.size() - i - 2] = dps.size() - i - 1;
      y[dps.size() - i - 2] = ClusterUtils.avgDistortion(clusters);
    }

    ArrayUtils.replace(y, 0, MathUtils.eps());
    //System.out.println(PrintUtils.array(x));
    //System.out.println(PrintUtils.array(y));

    for (int i = 0; i < curv.size(); i++) {
      int idx = curv.get(i).elbow(x, y);
      System.out.println("\t" + header.get(i) + " -> " + x[idx]);
    }
  }

  public static <D extends Distance<D>> void dbscanElbowTest(final List<D> dps, final int min,
                                                             final int max) {
    DBSCAN alg = new DBSCAN();

    List<String> header = new ArrayList<>();
    header.add("Kneedle");
    header.add("L-method");
    header.add("S-method");
    header.add("Menger Curvature");
    header.add("DFDT");
    header.add("DSDT");
    header.add("R-Method");
    header.add("DK-method");

    List<Curvature> curv = new ArrayList<>();
    curv.add(new Kneedle());
    curv.add(new Lmethod());
    curv.add(new Smethod());
    curv.add(new MengerCurvature());
    curv.add(new DFDT());
    curv.add(new DSDT());
    curv.add(new Rmethod());
    curv.add(new DKmethod());

    for (int i = 0; i < curv.size(); i++) {
      Curvature c = curv.get(i);
      List<Cluster<D>> bClusters = alg.clustering(dps, min, c);
      double bAvgDistortion = ClusterUtils.avgDistortion(bClusters);
      for (int j = min + 1; j <= max; j++) {
        List<Cluster<D>> clusters = alg.clustering(dps, j, c);
        double avgDistortion = ClusterUtils.avgDistortion(clusters);
        if (avgDistortion < bAvgDistortion) {
          bClusters = clusters;
          bAvgDistortion = avgDistortion;
        }
      }
      System.out.println("\t" + header.get(i) + " -> " + bClusters.size());
    }
  }
}