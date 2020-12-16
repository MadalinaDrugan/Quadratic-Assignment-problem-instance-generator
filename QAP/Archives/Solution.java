/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package QAP.Archives;

//import localoptimization.Solution;
//import general.Genetic.PerturbatorStrategies;
//import Scalarizer;
import java.util.*;

/**PartialSolution with the constructor from QAPs
 *
 * @author madalina
 */
public abstract class Solution implements Comparable<Solution>, java.io.Serializable, Comparator<Solution>{
    //public int[] rep;
    //public QAPs problem;
    public int[] cycles;
    public int[] identArray;
    public int[] combArray;
    
    public int[] items;
    public long[] objectives;
    public boolean flagVisited = false;

    private int flagOperation = 0;
    
    //relationships
    public abstract boolean dominates(Solution ps);
    //public abstract boolean dominates(Solution ps, PerturbatorStrategies epsilon2);
    public abstract boolean dominates(long[] obj);
    public abstract boolean dominates(ArchiveSolutions ps);
    public abstract long[] dominatingObj(long[] obj);

    public abstract boolean isDominated(Solution ps);
    public abstract boolean isDominated(long[] obj);
    public abstract boolean isDominated(ArchiveSolutions ps);

    //public abstract boolean incomparable(Solution ps, PerturbatorStrategies epsilon2);
    //public abstract boolean incomparable(long[] obj, PerturbatorStrategies epsilon2);
    //public abstract boolean incomparable(ArchiveSolutions ps, PerturbatorStrategies epsilon2);
    
    public abstract long getIdentifNumber();

    //minimum number of interchanges between two solutions
    public abstract int getDistance(Solution s);
    public abstract int getDistance(int[] temp);
    public abstract int getDistance(int[] temp, int[] temp2);

    public abstract Solution getASolution();

    public abstract boolean setVisited();

    public abstract void setTypeProblem(boolean type);

    /////////////////////
    // set aditional 
    public abstract void setFlagOperation(int f);
    public abstract int getFlagOperation();

    //public abstract void setEpsilon(PerturbatorStrategies e);
    //public abstract PerturbatorStrategies getEpsilon();

    @Override public boolean equals(Object o){
          if (this==o) {
            return true;
        }
          for(int i=0;i<objectives.length;i++){
              if (objectives[i] != ((Solution)o).objectives[i]) {
                  return false;
              }
          }

          for(int i = 0; i < items.length; i++){
              if(((Solution)o).items[i] != items[i]){
                  return false;
              }
          }
          return true;
	}

    @Override public abstract Solution clone();

    //public abstract boolean dominates(ArchiveSolutions ps, PerturbatorStrategies epsilon2);
    public abstract boolean dominates(Solution ps, Scalarizer scal);
    public abstract boolean dominates(ArchiveSolutions ps, Scalarizer scal);
    public abstract boolean isDominated(Solution ps, Scalarizer scal);
    public abstract boolean isDominated(ArchiveSolutions ps, Scalarizer scal);
    public abstract boolean incomparable(Solution ps, Scalarizer scal);
    public abstract boolean incomparable(ArchiveSolutions ps, Scalarizer scal);
    public abstract void setScalarizer(Scalarizer scal);

    //public abstract boolean dominates(ArchiveSolutions ps, PerturbatorStrategies epsilon2);
    //public abstract boolean dominates(Solution ps, Reduction scal);
    //public abstract boolean dominates(ArchiveSolutions ps, Reduction scal);
    //public abstract boolean isDominated(Solution ps, Reduction scal);
    //public abstract boolean isDominated(ArchiveSolutions ps, Reduction scal);
    /// abstract boolean incomparable(Solution ps, Reduction scal);
    //public abstract boolean incomparable(ArchiveSolutions ps, Reduction scal);
    //public abstract void setReduction(Reduction scal);
}
