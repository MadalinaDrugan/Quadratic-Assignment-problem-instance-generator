package QAP.Archives;
import java.util.*;

public interface Scalarizer extends Comparable<Scalarizer>, java.io.Serializable, Comparator<Scalarizer> {

    double scalarize(long[] objectives);

    public void setWeights(double[] weights);
    public double[] getWeights();
    public void setWeights();

    public Scalarizer clone();
}
