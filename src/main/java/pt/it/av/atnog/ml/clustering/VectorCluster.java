package pt.it.av.atnog.ml.clustering;

import pt.it.av.atnog.utils.bla.Vector;

import java.util.ArrayList;

/**
 * Created by mantunes on 1/23/17.
 */
public class VectorCluster<V extends Vector> extends ArrayList<V> implements Cluster<V> {
    /**
     * Constructs a new Vector based cluster with the first element.
     *
     * @param e the first element of the Vector based cluster
     */
    public VectorCluster(V e) {
        add(e);
    }

    @Override
    public double distortion() {
        return 0;
    }

    @Override
    public double distortion(V e) {
        return 0;
    }

    @Override
    public double distortion(Cluster<V> c) {
        return 0;
    }
}
