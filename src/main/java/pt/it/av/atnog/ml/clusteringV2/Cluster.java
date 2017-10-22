package pt.it.av.atnog.ml.clusteringV2;

import pt.it.av.atnog.utils.structures.Distance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Cluster<D extends Distance> extends ArrayList<D> {

  public Cluster() {  }

  public Cluster(D dp) {
    add(dp);
  }

  /**
   *
   * @return
   */
  public D center() {
    D rv = null;

    switch (size()) {
      case 0: rv = null; break;
      case 1: rv = get(0); break;
      case 2: rv = get(0); break;
      default: {
        D bestCenter = get(0);
        double bestDistCenter = 0.0;

        for(int j = 0; j < size(); j++) {
          bestDistCenter += bestCenter.distanceTo(get(j));
        }

        for(int i = 1; i < size(); i++) {
          D currentCenter = get(i);
          double avgDistCenter = 0.0;
          for(int j = 0; j < size(); j++) {
            if(i != j) {
              avgDistCenter += currentCenter.distanceTo(get(j));
            }
          }
          if(avgDistCenter < bestDistCenter) {
            bestCenter = currentCenter;
            bestDistCenter = avgDistCenter;
          }
        }
        rv = bestCenter;
        break;
      }
    }
    return rv;
  }

  /**
   * Returns the distiortion of the cluster.
   *
   * @return the distortion of the cluster.
   */
  public double distortion() {
    D dpc = center();
    double rv = 0.0;
    for (int i = 0; i < size(); i++)
      rv += Math.pow(dpc.distanceTo(get(i)), 2.0);
    return rv;
  }
}
