/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package QAP.Archives;

//import localoptimization.Solution;
//import general.Genetic.PerturbatorStrategies;
//import general.Scalarization.Scalarizer;
//import general.Reduction.Reduction;
import java.util.*;
//import general.*;

/**PartialSolution with the constructor from QAPs
 *
 * @author madalina
 */
public class Solution_QAPs extends Solution{
    
    public TreeSet<PartialSolution_QAPs> set;

    //private QAPs problem;

    static final int ITEMS_SHOWN=0;
    static Random r = new Random();
    protected long identificationNumber = r.nextLong();

    ///////////////////////
    // additional functionality
    //private PerturbatorStrategies epsilon;

    private Vector<int[]> cyclePosition = new Vector<int[]>();
    private Vector<Integer> tempArray = new Vector<Integer>();
    
    private int flagOperation = 0;

    public boolean minimization = true;

    public Scalarizer scal;
    //public Reduction red;

    public Solution_QAPs(){
        //epsilon = 0;
    }
    
    //public Solution_QAPs(PerturbatorStrategies e){
    //    epsilon = e;
    //}
	
    public Solution_QAPs(long[] obj, int[] rep){//, PerturbatorStrategies e){
        items = Arrays.copyOf(rep, rep.length);
        objectives = Arrays.copyOf(obj, obj.length);
        cycles = new int[items.length];
        identArray = new int[items.length];
        combArray = new int[items.length];
    }
    
    public Solution_QAPs(long[] obj, Vector<Integer> rep){//, PerturbatorStrategies e){
        items = new int[rep.size()];
        for(int i = 0; i < rep.size(); i++) {
            items[i] = rep.get(i);
        }
        objectives = new long[obj.length];
        objectives = Arrays.copyOf(obj, obj.length);
        cycles = new int[items.length];
        identArray = new int[items.length];
        combArray = new int[items.length];
    }

    public Solution_QAPs(Solution_QAPs s){
        items = Arrays.copyOf(s.items, s.items.length);
        objectives = Arrays.copyOf(s.objectives, s.objectives.length);
        cycles = new int[items.length];
        identArray = new int[items.length];
        combArray = new int[items.length];
        //epsilon = s.epsilon;
    }

    @Override
    public String toString()
	{
		StringBuilder sb = new StringBuilder();
        sb.append("Permutation: \t");
		for(int j=0;j<items.length;j++){
			sb.append(items[j]+"\t");
		}
        sb.append('\n');

        sb.append("value of solution");
		for(int j=0;j<objectives.length;j++){
			sb.append(objectives[j]+"\t");
		}
        sb.append('\n');
		if (set != null && !set.isEmpty() ){
            sb.append("Near values ");

            Iterator<PartialSolution_QAPs> iter = set.iterator();
            while(iter.hasNext()){
                PartialSolution_QAPs iterSol = iter.next();

                for(int j=0;j<iterSol.items.length;j++){
                    sb.append(iterSol.items[j]+",\t");
                }
		        sb.append('\n');
            }
		}
 		//sb.append(" space:"+spaceUsed);
		return sb.toString();
	}

    @Override public void setTypeProblem(boolean type){
        this.minimization = type;
    }
    
    @Override public void setScalarizer(Scalarizer scal){
        this.scal = scal;
    }

    ///@Override public void setReduction(Reduction sc){
    //    this.red = sc;
    //}

    //////////////////////////
    // dominating relationships for one solution
    ///////////////////////////
    /*@Override
    public boolean incomparable(Solution ps, PerturbatorStrategies epsilon2){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        boolean tooClose = true;
        for(int i=0;i<objectives.length;i++){
            if(objectives[i] < ps.objectives[i]){
                oneBiggerB=true;
                equalB = false;
            }
            if (objectives[i] > ps.objectives[i]){
                oneSmaller = true;
                equalB = false;
            }

            if(Math.abs(objectives[i] - ps.objectives[i]) > epsilon2.getPerturbator()) {
                tooClose = false;
            }
        }
        if(oneBiggerB && oneSmaller && !tooClose) {
            return true;
        }
        if(equalB && !tooClose) {
            return true;
        }
        return false;
    }*/

    @Override
    public boolean isDominated(Solution ps){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<objectives.length;i++){
            if (objectives[i] < ps.objectives[i]){
                oneBiggerB=true;
            }
            if (objectives[i] > ps.objectives[i]){
                oneSmaller = true;
            }
	}
        if(!oneBiggerB && oneSmaller && minimization) {
            return true;
        }
        if(oneBiggerB && !oneSmaller && !minimization) {
            return true;
        }
        return false;
    }

    @Override
    public boolean incomparable(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0 = scal.scalarize(objectives);
        double scal1 = scal.scalarize(ps.objectives);
        if(scal0 == scal1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDominated(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0 = scal.scalarize(objectives);
        double scal1 = scal.scalarize(ps.objectives);
        if(scal0 > scal1 && minimization) {
            return true;
        }
        if(scal0 < scal1 && !minimization) {
            return true;
        }
        return false;
    }


    @Override
    public boolean dominates(Solution ps){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<objectives.length;i++){
            if (objectives[i] < ps.objectives[i]){
                oneBiggerB=true;
            }
            if (objectives[i] > ps.objectives[i]){
                oneSmaller = true;
            }
        }
        if(oneBiggerB && !oneSmaller && minimization) {
            return true;
        }
        if(!oneBiggerB && oneSmaller && !minimization) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominates(long[] obj){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<obj.length;i++){
            if (objectives[i] < obj[i]){
                oneBiggerB=true;
            }
            if (objectives[i] > obj[i]){
                oneSmaller = true;
            }
        }
        if(oneBiggerB && !oneSmaller && minimization) {
            return true;
        }
        if(!oneBiggerB && oneSmaller && !minimization) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominates(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0 = scal.scalarize(objectives);
        double scal1 = scal.scalarize(ps.objectives);
        if(scal0 < scal1 && minimization) {
            return true;
        }
        if(scal0 > scal1 && !minimization) {
            return true;
        }
        return false;
    }


    ////////////////////////////
    // dominating relationship of one solution against a front
    ////////////////////////////
    @Override
    public boolean dominates(ArchiveSolutions ps){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        //
        boolean incomp = true;
        boolean domin = false;
        boolean isDomin = false;
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<objectives.length;i++) {
                if (objectives[i] > s1.objectives[i]) {
                    oneSmaller = true;
                    equalB = false;
                } else if (objectives[i] < s1.objectives[i]) {
                    oneBiggerB = true;
                    equalB = false;
                }
            }
            if((oneBiggerB && oneSmaller) || equalB) {
                incomp = true;
            }
            else if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        if(domin && !isDomin && minimization) {
            return true;
        }
        if(!domin && isDomin && !minimization) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominates(ArchiveSolutions ps, Scalarizer scal){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        //
        double scal0 = scal.scalarize(objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            double scal1 = scal.scalarize(s1.objectives);

            //this dominates one of the parents
            if(scal0 < scal1 && minimization) {
                oneBiggerB = true;
            }
            if(scal0 > scal1 && minimization) {
                oneSmaller = true;
            }
            if(scal0 > scal1 && !minimization) {
                oneBiggerB = true;
            }
            if(scal0 < scal1 && !minimization) {
                oneSmaller = true;
            }
        }
        if(oneBiggerB && !oneSmaller) {
            return true;
        }
        return false;
    }


    @Override
    public boolean isDominated(long[] obj){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<obj.length;i++){
            if(objectives[i] < obj[i]){
                oneBiggerB=true;
            }
            if (objectives[i] > obj[i]){
                oneSmaller = true;
            }
        }
        if(!oneBiggerB && oneSmaller && minimization) {
            return true;
        }
        if(oneBiggerB && !oneSmaller && !minimization) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDominated(ArchiveSolutions ps, Scalarizer scal){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        //
        double scal0 = scal.scalarize(objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            double scal1 = scal.scalarize(s1.objectives);

            //this dominates one of the parents
            if(scal0 < scal1 && minimization) {
                oneBiggerB = true;
            }
            if(scal0 > scal1 && minimization) {
                oneSmaller = true;
            }
            if(scal0 > scal1 && !minimization) {
                oneBiggerB = true;
            }
            if(scal0 < scal1 && !minimization) {
                oneSmaller = true;
            }
        }
        if(!oneBiggerB && oneSmaller) {
            return true;
        }
        return false;
    }


    @Override
    public boolean incomparable(ArchiveSolutions ps, Scalarizer scal){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean oneEqual_1 = true;
        //
        double scal0 = scal.scalarize(objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            double scal1 = scal.scalarize(s1.objectives);

            //this dominates one of the parents
            if(scal0 < scal1 && minimization) {
                oneBiggerB = true;
            }
            if(scal0 > scal1 && minimization) {
                oneSmaller = true;
            }
            if(scal0 > scal1 && !minimization) {
                oneBiggerB = true;
            }
            if(scal0 < scal1 && !minimization) {
                oneSmaller = true;
            }
            if(scal0 != scal1) {
                oneEqual_1 = false;
            }
        }
        if((oneBiggerB && oneSmaller) || oneEqual_1) {
            return true;
        }
        return false;
    }


    ////////////////////////////
    // dominating relationship of one solution against a front
    ////////////////////////////
    public boolean isDominated(ArchiveSolutions ps){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        //
        boolean incomp = true;
        boolean domin = false;
        boolean isDomin = false;
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<objectives.length;i++) {
                if (objectives[i] > s1.objectives[i]){
                    oneSmaller = true;
                    equalB = false;
                }
                else if(objectives[i] < s1.objectives[i]){
                    oneBiggerB=true;
                    equalB = false;
                }
            }
            if((oneBiggerB && oneSmaller) || equalB) {
                incomp = true;
            }
            else if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        if(!domin && isDomin && minimization)
            return true;
        if(domin && !isDomin && !minimization)
            return true;
        return false;
    }

    ///////////////////////////
    //////////////////////////////
    // check also the compare to solution
    int oneBigger = 0;
    int oneEqual = 0;
    @Override public int compareTo(Solution o){
        oneBigger = 0;
        oneEqual = 0;
        if(o == this) {
            return 0;
        }
        //we assume that is not dominant 
        if(flagOperation == 2){
          for(int i=0;i<objectives.length;i++){
              if (Math.abs(objectives[i] - o.objectives[i]) == 0) {
                    return -1;
                }
          }
        }
      
        for(int i=0;i<this.objectives.length;i++){
                if(o.objectives[i] < this.objectives[i]) {
                oneBigger++;
            }
                else if (Math.abs(o.objectives[i]-this.objectives[i]) == 0) {
                oneEqual++;
            }
        }

        if(flagOperation == 2 & oneBigger > 0 & oneBigger+oneEqual < objectives.length){
             /*   if(set == null)
                    set = new TreeSet<PartialSolution_QAPs>();
                if(set.contains((Solution_QAPs) o))
                    set.add(new PartialSolution_QAPs((Solution_QAPs)o));*/
        } else if(flagOperation == 3 | flagOperation == 4){

            if(oneBigger == 0 & oneEqual != objectives.length) {
                return 1;
            }
            if(oneBigger > 0 & oneBigger+oneEqual == objectives.length) {
                return -1;
            }
            //if(flagOperation == 3 & oneBigger > 0 & oneBigger+oneEqual < problem.numberOfObjectives)
             //   return -1;
            //else return 1;
        } else if(flagOperation == 1){
            if(oneBigger > 0 & oneBigger+oneEqual < objectives.length) {
                return 1;
            }
        }
        return 0;
    }

    /*public boolean dominates(PartialSolution_QAPs ps){
	boolean oneD=false;
	for(int i=0;i<objectives.length;i++){
		if (objectives[i] < ps.objectives[i]) oneD=true;
		else if (objectives[i] > ps.objectives[i]) return false;
	}
	return oneD;
   }*/

    //a kind of dominates only in real number
    // 1 - o dominates o1
    // - 1 - o is dominated by o1
    // 0 - o and o1 are non-dominated
    
    @Override public int compare(Solution o, Solution o1){
        if(o == this) {
            return 0;
        }
        //we assume that is not dominant 
        if(flagOperation == 1 | flagOperation == 2){
          for(int i=0;i<objectives.length;i++){
              if (Math.abs(objectives[i] - ((Solution_QAPs)o).objectives[i]) ==0) {
                    return 1;
                }
          }            
        } else if(flagOperation == 3 | flagOperation == 4){
            int lengthO = o.objectives.length;
            oneBigger=0;
            oneEqual = 0;
            for(int i=0;i<lengthO;i++){
                if(o.objectives[i] < this.objectives[i]) {
                    oneBigger++;
                }
                else if (Math.abs(o.objectives[i]-this.objectives[i]) == 0) {
                    oneEqual++;
                }
            }

            if(oneBigger == 0 & oneEqual != lengthO) {
                return -1;
            }
            if(oneBigger > 0 & oneBigger+oneEqual == lengthO) {
                return 1;
            }
        }
        return 0;
    }
    

    @Override public int hashCode(){
        return (int) this.identificationNumber;
    }

    public long getIdentifNumber(){
        double temp = 0;
        for(int i = 0; i < objectives.length; i++){
            temp += Math.pow(10, 8*i)*objectives[i];
        }
        return (long)temp;
    }

    //minimum number of interchanges between two solutions
    public int getDistance(Solution s){
       //test the difference between the two strings
       int dist = 0;
       cyclePosition.clear();
       if(cycles == null){
           cycles = new int[items.length];
       }

       Arrays.fill(cycles,-1);

       //find the cycles in the two
       for(int j = 0; j < this.items.length;j++)
           if(cycles[j] == -1){
                    int tempJ = j;
                    cycles[j] = tempJ;
                    if(items[tempJ] == s.items[tempJ]){
                        int[] tempA = new int[1];
                        tempA[0] = items[tempJ];
                        cyclePosition.add(tempA);
                        continue;
                    }
                    tempArray.clear();
                    tempArray.add(Integer.valueOf(tempJ));
                    while(true){
                        boolean flagContinue = false;
                        for(int k = j+1; k< this.items.length; k++)
                            if(tempJ != k & items[k] == s.items[tempJ]){
                                cycles[k] = k;
                                tempJ = k;
                                tempArray.add(Integer.valueOf(tempJ));
                                flagContinue = true;
                                break;
                            }
                        if(!flagContinue | items[j] == s.items[tempJ])
                            break;
                    }
                    //find
                    int[] tempArray1 = new int[tempArray.size()];
                    int i = 0;
                    Iterator<Integer> thisIter = tempArray.iterator();
                    while(thisIter.hasNext()){
                        tempArray1[i++] = thisIter.next();
                    }
                    dist += tempArray1.length - 1;
                    Arrays.sort(tempArray1);
                    cyclePosition.add(tempArray1);
             }

         return dist; //items.length - cyclePosition.size();
        }

    //minimum number of interchanges between two solutions
    public int getDistance(int[] temp){
       //test the difference between the two strings
       int dist = 0;
       cyclePosition.clear();
       if(cycles == null){
           cycles = new int[items.length];
       }

       Arrays.fill(cycles,-1);

       //find the cycles in the two
       for(int j = 0; j < this.items.length;j++)
           if(cycles[j] == -1){
                    int tempJ = j;
                    cycles[j] = tempJ;
                    if(items[tempJ] == temp[tempJ]){
                        int[] tempA = new int[1];
                        tempA[0] = items[tempJ];
                        cyclePosition.add(tempA);
                        continue;
                    }
                    tempArray.clear();
                    tempArray.add(Integer.valueOf(tempJ));
                    while(true){
                        boolean flagContinue = false;
                        for(int k = j+1; k< this.items.length; k++)
                            if(tempJ != k & items[k] == temp[tempJ]){
                                cycles[k] = k;
                                tempJ = k;
                                tempArray.add(Integer.valueOf(tempJ));
                                flagContinue = true;
                                break;
                            }
                        if(!flagContinue | items[j] == temp[tempJ])
                            break;
                    }
                    //find
                    int[] tempArray1 = new int[tempArray.size()];
                    int i = 0;
                    Iterator<Integer> thisIter = tempArray.iterator();
                    while(thisIter.hasNext()){
                        tempArray1[i++] = thisIter.next();
                    }
                    Arrays.sort(tempArray1);
                    dist += tempArray1.length - 1;
                    cyclePosition.add(tempArray1);
             }

         return dist; //items.length - cyclePosition.size();
        }

    //minimum number of interchanges between two solutions
    public int getDistance(int[] temp, int[] temp2){
       //test the difference between the two strings
       cyclePosition.clear();
       if(cycles == null){
           cycles = new int[temp2.length];
       }
       int dist = 0;

       Arrays.fill(cycles,-1);

       //find the cycles in the two
       for(int j = 0; j < temp2.length;j++)
           if(cycles[j] == -1){
                    int tempJ = j;
                    cycles[j] = tempJ;
                    if(temp2[tempJ] == temp[tempJ]){
                        int[] tempA = new int[1];
                        tempA[0] = temp2[tempJ];
                        cyclePosition.add(tempA);
                        continue;
                    }
                    tempArray.clear();
                    tempArray.add(Integer.valueOf(tempJ));
                    while(true){
                        boolean flagContinue = false;
                        for(int k = j+1; k< temp2.length; k++)
                            if(tempJ != k & temp2[k] == temp[tempJ]){
                                cycles[k] = k;
                                tempJ = k;
                                tempArray.add(Integer.valueOf(tempJ));
                                flagContinue = true;
                                break;
                            }
                        if(!flagContinue | temp2[j] == temp[tempJ])
                            break;
                    }
                    //find
                    int[] tempArray1 = new int[tempArray.size()];
                    int i = 0;
                    Iterator<Integer> thisIter = tempArray.iterator();
                    while(thisIter.hasNext()){
                        tempArray1[i++] = thisIter.next();
                    }
                    Arrays.sort(tempArray1);
                    dist+= tempArray1.length - 1;
                    cyclePosition.add(tempArray1);
             }

         return dist; //temp2.length - cyclePosition.size();
        }

    public Solution_QAPs getASolution(){
       if(set == null)
           return this.clone();
       
       int N = r.nextInt(set.size()+1);
       if(N == 0)
           return clone();

       int i =0;
       Iterator<PartialSolution_QAPs> iter = set.iterator();
       while(iter.hasNext() & i++ < N-1)
           iter.next();

       return clone(iter.next());
    }

    @Override public Solution_QAPs clone(){
        Solution_QAPs newS = new Solution_QAPs(); //epsilon);
        newS.items = Arrays.copyOf(items, this.items.length);
        newS.objectives = Arrays.copyOf(objectives, this.objectives.length);
        //newS.problem = problem;
        //flag is not copied
        newS.flagVisited = flagVisited;
        return newS;
    }

    public Solution_QAPs clone(PartialSolution_QAPs original){
        Solution_QAPs sol = new Solution_QAPs(); //epsilon);
        sol.items = Arrays.copyOf(original.items, original.items.length);
        sol.objectives = Arrays.copyOf(original.objectives, original.objectives.length);
        sol.flagVisited = flagVisited;
        //sol.problem = problem;
        return sol;
   }

    public boolean setVisited(){
        flagVisited = true;
        return flagVisited;
    }

    public void setFlagOperation(int f){
        this.flagOperation = f;
    }
    public int getFlagOperation(){
        return flagOperation;
    }
    /*public void setEpsilon(PerturbatorStrategies e){
        this.epsilon = e;
    }
    public PerturbatorStrategies getEpsilon(){
        return epsilon;
    }*/



    //////////////////////////
    //aditionale; specifice
    public long[] dominatingObj(long[] obj){
        for(int j=0;j<obj.length;j++){
            if(this.objectives[j]<obj[j] && minimization)
                obj[j] = objectives[j];
            if(this.objectives[j]>obj[j] && !minimization)
                obj[j] = objectives[j];
		}
        return obj;
    }

}
