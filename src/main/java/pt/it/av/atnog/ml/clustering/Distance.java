package pt.it.av.atnog.ml.clustering;

/**
 * This interface allows to compute the distance between two objects.
 * <p>It is strongly recommended (though not required) that when objects are equals the distance between them be 0.
 * This interaface is currently used in the clustering framework.</p>
 * @param <T> the type of objects that this object may be compared to
 */
public interface Distance<T> {
    /**
     * Returns the distance between two objects.
     * @param d another object
     * @return distance between two objects
     */
    double distance(T d);
}
