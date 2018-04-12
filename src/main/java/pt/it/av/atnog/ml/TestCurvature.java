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
import pt.it.av.atnog.utils.PrintUtils;
import pt.it.av.atnog.utils.bla.Vector;
import pt.it.av.atnog.utils.structures.Distance;
import pt.it.av.atnog.utils.structures.Point2D;
import pt.it.av.atnog.utils.structures.Point4D;

import java.util.ArrayList;
import java.util.List;

public class TestCurvature {
  public static void main(String[] args) {
    System.out.println("Curvature Evaluation:");
    int reps = 100;
    Kmeans kmeans = new Kmeanspp();
    SLINK slink = new SLINK();

    // TODO:REMOVE
    double x[] = {-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        y[] = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 107, 122, 145, 176, 215, 262, 317, 380, 451, 530};

    ArrayUtils.replace(y, 0, 10);

    System.out.println(x.length + " " + y.length);

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
      int idx = curv.get(i).elbow(x, y);
      if (idx >= 0 && idx < x.length) {
        System.out.println("\t" + header.get(i) + " -> " + x[idx]);
      } else {
        System.out.println("\t" + header.get(i) + " -> NA");
      }
    }

    // IRIS
    System.out.println("Iris dataset (3)");
    Dataset<Point4D> iris = new Iris();

    List<Point4D> irisDps = iris.load();

    System.out.println("K-means++");
    elbowTest(kmeans, irisDps, (int) (Math.round(iris.classes() / 2.0)), iris.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, irisDps, (int) (Math.round(iris.classes() / 2.0)), iris.classes() * 2);
    //System.out.println();
    //System.out.println("DBSCAN");
    //dbscanElbowTest(irisDps,dmin,dmax);

    // Yeast Dataset
    System.out.println();
    System.out.println("Yeast dataset (10/5)");
    Dataset<Vector> yeast = new Yeast();

    List<Vector> yeastDps = yeast.load();

    System.out.println("K-means++");
    elbowTest(kmeans, yeastDps, (int) (Math.round(yeast.classes() / 2.0)), yeast.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, yeastDps, (int) (Math.round(yeast.classes() / 2.0)), yeast.classes() * 2);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(yeastDps,dmin,dmax);

    // Control Dataset
    System.out.println();
    System.out.println("Control dataset (6)");
    Dataset<Vector> control = new Control();

    List<Vector> controlDps = control.load();

    System.out.println("K-means++");
    elbowTest(kmeans, controlDps, (int) (Math.round(control.classes() / 2.0)), control.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, controlDps, (int) (Math.round(control.classes() / 2.0)), control.classes() * 2);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(controlDps,dmin,dmax);

    // Wine Dataset
    System.out.println();
    System.out.println("Wine dataset (3)");
    Dataset<Vector> wine = new Wine();

    List<Vector> wineDps = wine.load();

    System.out.println("K-means++");
    elbowTest(kmeans, wineDps, (int) (Math.round(wine.classes() / 2.0)), wine.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, wineDps, (int) (Math.round(wine.classes() / 2.0)), wine.classes() * 2);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(wineDps,dmin,dmax);

    // Vehicle Dataset
    System.out.println();
    System.out.println("Vehicle dataset (4)");
    Dataset<Vector> vehicle = new Vehicle();

    List<Vector> vehicleDps = vehicle.load();

    System.out.println("K-means++");
    elbowTest(kmeans, vehicleDps, (int) (Math.round(vehicle.classes() / 2.0)), vehicle.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, vehicleDps, (int) (Math.round(vehicle.classes() / 2.0)), vehicle.classes() * 2);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(vehicleDps,dmin,dmax);

    // A-Set A1 Dataset
    System.out.println();
    System.out.println("A-sets A1 dataset (20)");
    Dataset<Point2D> a1 = new ASetsA1();

    List<Point2D> a1Dps = a1.load();

    System.out.println("K-means++");
    elbowTest(kmeans, a1Dps, (int) (Math.round(a1.classes() / 2.0)), a1.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, a1Dps, (int) (Math.round(a1.classes() / 2.0)), a1.classes() * 2);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(a1Dps,dmin,dmax);

    // A-Set A2 Dataset
    System.out.println();
    System.out.println("A-sets A2 dataset (35)");
    Dataset<Point2D> a2 = new ASetsA2();

    List<Point2D> a2Dps = a2.load();

    System.out.println("K-means++");
    elbowTest(kmeans, a2Dps, (int) (Math.round(a2.classes() / 2.0)), a2.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, a2Dps, (int) (Math.round(a2.classes() / 2.0)), a2.classes() * 2);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(a2Dps,dmin,dmax);

    // A-Set A3 Dataset
    System.out.println();
    System.out.println("A-sets A3 dataset (50)");
    Dataset<Point2D> a3 = new ASetsA3();

    List<Point2D> a3Dps = a3.load();

    System.out.println("K-means++");
    elbowTest(kmeans, a3Dps, (int) (Math.round(a3.classes() / 2.0)), a3.classes() * 2, reps);
    System.out.println("SLINK");
    hiearchicalElbowTest(slink, a3Dps, (int) (Math.round(a3.classes() / 2.0)), a3.classes() * 2);
    //System.out.println("DBSCAN");
    //dbscanElbowTest(a3Dps,dmin,dmax);
  }

  public static <D extends Distance<D>> void elbowTest(final Kmeans alg, final List<D> dps,
                                                       final int min, final int max, final int reps) {
    final int kmax = (max < dps.size()) ? max : dps.size() - 1;
    double wsss[] = new double[(kmax - min) + 1],
        x[] = new double[(kmax - min) + 1];

    List<String> header = new ArrayList<>();
    //header.add("Kneedle");
    //header.add("L-method");
    //header.add("S-method");
    //header.add("Menger Curvature");
    header.add("DFDT");
    header.add("DSDT");
    header.add("R-Method");
    //header.add("DK-method");

    List<Curvature> curv = new ArrayList<>();
    //curv.add(new Kneedle());
    //curv.add(new Lmethod());
    //curv.add(new Smethod());
    //curv.add(new MengerCurvature());
    curv.add(new DFDT());
    curv.add(new DSDT());
    curv.add(new Rmethod());
    //curv.add(new DKmethod());

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
      if (idx >= 0) {
        System.out.println("\t" + header.get(i) + " -> " + x[idx]);
      } else {
        System.out.println("\t" + header.get(i) + " -> NA");
      }
    }
  }

  public static <D extends Distance<D>> void hiearchicalElbowTest(Hierarchical alg, final List<D> dps, final int min, final int max) {
    List<String> header = new ArrayList<>();
    //header.add("Kneedle");
    //header.add("L-method");
    //header.add("S-method");
    //header.add("Menger Curvature");
    header.add("DFDT");
    header.add("DSDT");
    header.add("R-Method");
    //header.add("DK-method");

    List<Curvature> curv = new ArrayList<>();
    //curv.add(new Kneedle());
    //curv.add(new Lmethod());
    //curv.add(new Smethod());
    //curv.add(new MengerCurvature());
    curv.add(new DFDT());
    curv.add(new DSDT());
    curv.add(new Rmethod());
    //curv.add(new DKmethod());

    int d[][] = alg.clustering(dps), size = max - min + 1;
    double x[] = new double[size], y[] = new double[size];

    // Create individual clusters
    List<Cluster<D>> clusters = new ArrayList<>(dps.size());
    for (int i = 0; i < dps.size(); i++) {
      clusters.add(new Cluster<D>(dps.get(i)));
    }

    System.out.println("Max = " + max + " Min = " + min);
    int i = 0, cSize = dps.size();
    for (; cSize > max; i++, cSize--) {
      clusters.get(d[i][1]).addAll(clusters.get(d[i][0]));
      clusters.set(d[i][0], null);
    }
    System.out.println("I = " + i);

    int j = 0;
    for (; cSize >= min; i++, cSize--) {
      clusters.get(d[i][1]).addAll(clusters.get(d[i][0]));
      clusters.set(d[i][0], null);
      x[size - j - 1] = dps.size() - i;
      y[size - j - 1] = ClusterUtils.avgDistortion(clusters);
      j++;
    }

    //clusters.get(d[0][1]).addAll(clusters.get(d[0][0]));
    //clusters.set(d[0][0], null);
    //x[dps.size() - 2] = dps.size() - 1;
    //y[dps.size() - 2] = ClusterUtils.avgDistortion(clusters);

    // Merge clusters
    /*for (int i = 1; i < d.length; i++) {
      clusters.get(d[i][1]).addAll(clusters.get(d[i][0]));
      clusters.set(d[i][0], null);
      x[dps.size() - i - 2] = dps.size() - i - 1;
      y[dps.size() - i - 2] = ClusterUtils.avgDistortion(clusters);
    }*/

    ArrayUtils.replace(y, 0, MathUtils.eps());
    System.out.println(PrintUtils.array(x));
    System.out.println(PrintUtils.array(y));

    for (i = 0; i < curv.size(); i++) {
      int idx = curv.get(i).elbow(x, y);
      if (idx >= 0) {
        System.out.println("\t" + header.get(i) + " -> " + x[idx]);
      } else {
        System.out.println("\t" + header.get(i) + " -> NA");
      }
    }
  }

  public static <D extends Distance<D>> void dbscanElbowTest(final List<D> dps, final int min,
                                                             final int max) {
    DBSCAN alg = new DBSCAN();

    List<String> header = new ArrayList<>();
    header.add("Kneedle");
    header.add("L-method");
    //header.add("S-method");
    header.add("Menger Curvature");
    header.add("DFDT");
    header.add("DSDT");
    header.add("R-Method");
    header.add("DK-method");

    List<Curvature> curv = new ArrayList<>();
    curv.add(new Kneedle());
    curv.add(new Lmethod());
    //curv.add(new Smethod());
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