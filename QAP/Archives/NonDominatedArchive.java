/*
 * NonDominatedArchive for one or more objectives
 * There are only two types of problems: minimization and maximization problems
 * minimization: true is a minimization problem, false otherwise
 * objectives: internal variable to spead up the search contains sorted lists for each separate objective
 * larger, smaller lists for comparision of the current NDA with the new candidate solutions
 *
 * Date or work: September 2010
 */
package QAP.Archives;

//import general.Scalarization.Scalarizer;
//import general.Genetic.PerturbatorStrategies;
//import general.*;
import java.util.*;
//import general.Scalarization.*;
//import general.Reduction.*;
//import MOInstance_QAPs.Solution_QAPs;

public class NonDominatedArchive implements ArchiveSolutions{

    // the effective nda
    private TreeMap<Long,Solution> avl = new TreeMap<Long,Solution>();
    //type of problem: minimization or maximization
    public boolean minimization = true;

    ///////////////////////
    //with memory or no
    private boolean memory = false;
    // list with the past dominating and non-identical solutions
    private Vector<Solution> dominatedList = new Vector<Solution>();
    //the times an solutions is meet
    //Vector<Long[]> dominatedList_Times = new Vector<Long[]>();
    private int sizeDominatedList = 200;

    private final static Random r = new Random();

    //public PerturbatorStrategies epsilon;
    
    public NonDominatedArchive(){
        //epsilon = e;
    }

    //////////////////////
    //internal structures
    //////////////////////
    //the relationship of this identifier with the current solution
    //vectors of identifies; identifier present if in a relationship in at least one dimension
    //private Vector<Long> larger = new Vector<Long>();
    //private Vector<Long> smaller = new Vector<Long>();
    //private Vector<Long> equal = new Vector<Long>();

    //private TreeMap<Long,Long>[] objectives; //sorted tree: value and identifier
    private int nrObjectives;

    ///////////////////////
    // scalarization is null
    public boolean add(Solution s){
        s.setFlagOperation(4);

        if(!avl.isEmpty() && contains(s)) {
            return false;
        }

        //if(dominates(s)){
        //    return false;
        //}
        
        //smaller.clear();
        //larger.clear();
        //equal.clear();
        // init the objectives
        if(avl.size() == 0){
          //  objectives = new TreeMap[s.objectives.length];
          //  for(int i = 0; i < s.objectives.length; i++) {
          //      objectives[i] = new TreeMap<Long, Long>();
          //  }

        }

        // empty list; add and
        if(avl.isEmpty()){
            nrObjectives = s.objectives.length;
            s.setFlagOperation(1);
            long identif = 0;
            for(int i = 0; i < nrObjectives; i++) {
                identif += s.objectives[i] * (long) Math.pow(10, 6*i);
            }
            avl.put(identif,s);
            //for(int i = 0; i < nrObjectives; i++) {
            //    objectives[i].put(new Long(s.objectives[i]), identif);
            //}

             return true;
        }

        //check consistency in some cases
        //if(objectives[0].size() != avl.size()) {
        //    checkConsistency(s);
        //}

       // iterate avl.
       Iterator<Map.Entry<Long,Solution>> inter = avl.entrySet().iterator();
       Vector<Long> removeIdentif = new Vector<Long>();
       while(inter.hasNext()){
            Map.Entry<Long,Solution> compSol = inter.next();
            Solution sol = compSol.getValue();
            if(sol.dominates(s)) {
                return false;
            }
            if(s.dominates(sol)) {
                removeIdentif.add(compSol.getKey());
            }
       }
       while(!removeIdentif.isEmpty()){
           avl.remove(removeIdentif.remove(0));
       }


       /* //check which identifier is larger and which is not
        for(int i = 0; i < s.objectives.length; i++){
            if(objectives[i].containsKey(s.objectives[i])){
                equal.add(objectives[i].get(s.objectives[i]));
            } else {
                //identif of solutions that are larger than a value
                Collection<Long> tempI = objectives[i].headMap(s.objectives[i]).values();
                Iterator<Long> iterTemp = tempI.iterator();
                while(iterTemp.hasNext()){
                    Long tempL = iterTemp.next();
                    if(!larger.contains(tempL.longValue())) {
                        larger.add(tempL);
                    }
                }
                // solutions that are smaller than a value
                tempI = objectives[i].tailMap(s.objectives[i]).values();
                iterTemp = tempI.iterator();
                while(iterTemp.hasNext()){
                    Long tempL = iterTemp.next();
                    if(!smaller.contains(tempL.longValue())) {
                        smaller.add(tempL);
                    }
                }
            }
        }

        for(int i = 0; i < equal.size(); i++){
            Long identif = equal.get(i);
            if(minimization){
                if(larger.contains(identif.longValue())) {
                    remove(avl.get(identif));
                }
                else {
                    return false;
                }
            }
            if(!minimization){
                if(smaller.contains(identif.longValue())) {
                    remove(avl.get(identif));
                }
                else {
                    return false;
                }
            }
        }

        //if an identifier is only in one list than dominated or dominating depending
        for(int i = 0; i < larger.size(); i++){
            Long identif = larger.get(i);
            if(!smaller.contains(identif)){
                // is minimization problem: --> dominated, return false
                if(minimization) {
                    return false;
                }
                else {
                    //maximization problem: remove dominated
                    remove(avl.get(identif));
                }
            } else {
                //non-dominated solution; premise is to add
            }
        }

        for(int i = 0; i < smaller.size(); i++){
            Long identif = smaller.get(i);
            if(!larger.contains(identif)){
                // is NOT minimization problem: --> dominated, return false
                if(!minimization) {
                    return false;
                }
                else {
                    //NOT maximization problem: remove dominated
                    Solution s1 = avl.get(identif);
                    if(s1 != null) {
                        remove(avl.get(identif));
                    }
                    else {
                        checkConsistency(s);
                    }
                    //remove(larger.remove(identif));
                }
            } else {
                //non-dominated solution; premise is to add
            }
        }*/

        ////////////////////////
        // add the solution s
        //checkConsistency(s);
        s.setFlagOperation(1);
        long identif = this.generateIdentif(s);
        //for(int i = 0; i < objectives.length; i++)
        //    identif += s.objectives[i] * (long)Math.pow(10, 6*i);
        while(avl.containsKey(identif)){
            // check if it is the same solution
            // if not change the identifi method
            identif += r.nextLong();
        }
        avl.put(identif,s);
        //for(int i = 0; i < nrObjectives; i++) {
        //    objectives[i].put(new Long(s.objectives[i]), identif);
        //}
        //checkConsistency(s);
        ///////////////////
        return true;
   }
    
    public long generateIdentif(Solution s){
        long identif = 0;
        for(int i = 0; i < nrObjectives; i++) {
            identif += s.objectives[i] * (long) Math.pow(10, 6*i);
        }
        
        return identif;
    }
    
    /*public long generateIdentif(long[] obj){
        long identif = 0;
        for(int i = 0; i < obj.length; i++) {
            identif += obj[i] * (long) Math.pow(10, 6*i);
        }
        return identif;
    }*/


    @Override
    public void add(ArchiveSolutions newNDA){
        Iterator<Solution> i = newNDA.iterator();
	    while(i.hasNext()){
            Solution s = i.next();
            //if(!
            add(s);//)
            //   this.dominatedListAdd(s);
        }
	}

    public Solution getNthSolution(int N){
        if(N < avl.size()){
             Iterator<Map.Entry<Long,Solution>> newI = avl.entrySet().iterator();
             for(int counter = 0; counter < N; counter++)
                    newI.next();
             Map.Entry<Long,Solution> result = newI.next();
             return result.getValue();
        }
        if(memory && N-avl.size() < dominatedList.size()){
            return this.dominatedList.get(N-avl.size());
        }
        return null;
    }
        
   public Solution getRandomNotVisitedSolution(){
       //vector with not visited solutioatedns
       Stack<Solution> notVisited = new Stack<Solution>();
       Iterator<Map.Entry<Long,Solution>> newI = avl.entrySet().iterator();
       for(int counter = 0; counter < avl.size(); counter++){
             Solution tempS = newI.next().getValue();
             if(!tempS.flagVisited)
                 notVisited.add(tempS);
       }
       if(notVisited.isEmpty())
           return null;
       // randominzed part
       int sizeT = notVisited.size();
       double rTemp = r.nextDouble();
       int t = (int)Math.round(rTemp*sizeT);
       if(t == sizeT)
           t--;
       return notVisited.get(t);
    }

   ////////////////////////////
   // get solutions from avl that are at some distance in parameter space
   /////////////////////////////
   public Stack<Solution[]> getDistanceSol(int mut_rate){
        if(avl.size() < 2)
            return null;
        int N = avl.size();
        Stack<Solution[]> tempList = new Stack<Solution[]>();
        Iterator<Map.Entry<Long,Solution>> newI = avl.entrySet().iterator();
        for(int counter = 0; counter < N; counter++){
             Solution result = newI.next().getValue();
             Iterator<Map.Entry<Long,Solution>> newJ = avl.entrySet().iterator();
             for(int counter1 = 0; counter1 < N; counter1++){
                 Solution result1 = newJ.next().getValue();
                 if(result1.getDistance(result) >= mut_rate+2){
                     Solution[] newS = new Solution[2];
                     newS[0] = result1;
                     newS[1] = result;
                     tempList.add(newS);
                 }
             }
        }
        return tempList;
   }

   ///////////////////////
   // works
   public boolean contains(Solution s){
        s.setFlagOperation(2);
        if(avl == null || avl.isEmpty() || s == null || s.objectives == null) {
            return false;
        }

        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
 	while(i.hasNext()){
            Solution v = i.next().getValue();
            if(s.equals(v)){
                return true;
            }
	}
        return false;

       /* for(int i = 0; i < this.nrObjectives; i++){
             if(!objectives[i].containsKey(s.objectives[i])){
                   return false;

               }
           }
        return true;*/
   }


   /////////////////////
   // nda contains at least one solutions from the current NDA
   ///////////////////////
   public boolean contains(ArchiveSolutions nda){
       Iterator<Solution> iterNDA = nda.iterator();
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();
           s.setFlagOperation(2);

           if(contains(s))
               return true;
           /*boolean cont = false;
           for(int i = 0; i < this.nrObjectives; i++){
               if(!objectives[i].containsKey(s.objectives[i])){
                   cont = true;
                   break;
               }
           }
           if(cont)
               continue;
           return true;*/
       }
       return false;
    }


   //////////////////////////////
   // revise the definition of domination
   //true -- avl dominated s
   //false -- avl do not dominates s
    @Override
   public boolean dominates (Solution s){
     if(avl == null || avl.isEmpty()) {
            return false;
        }

     boolean domin = false;
     boolean isDomin = false;
     //SortedMap<Long,Long> n1 = firstObjective.headMap(new Long(s.objectives[0]));
     Iterator<Solution> iterator = iterator();
     while(iterator.hasNext()){
           Solution s1 = iterator.next();
           if(s1.dominates(s)) {
                domin = true;
            }
           if(s.dominates(s1)) {
                isDomin = true;
            }
              //else incomparable
              //if(avl.get(s1).objectives[0] < s.objectives[0] && avl.get(s1).objectives[1] < s.objectives[1]){
              //    return true;
              //}
     }
     if(domin && !isDomin) {
            return true;
        }
     return false;
   }

    @Override
   public boolean dominates (ArchiveSolutions nda){
       Iterator<Solution> iterNDA = nda.iterator();
       //when it is not dominated
       boolean domin = false;
       boolean isDomin = false;
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();

           if(dominates(s)) {
                domin = true;
            }
           if(s.dominates(this)) {
                isDomin = true;
            }
       }
       if(domin && !isDomin) {
            return true;
        }
       return false;
    }

   ////////////////////////////
   // exists one solutions for which this is true
   public boolean dominatesAtLeastOne (Solution s){
     if(avl.isEmpty()) {
            return false;
        }

     Iterator<Solution> iterator = iterator();
     while(iterator.hasNext()){
              Solution s1 = iterator.next();
              if(s1.dominates(s)) {
                return true;
            }
     }
     return false;
   }

   public boolean dominatesAtLeastOne (ArchiveSolutions nda){
       Iterator<Solution> iterNDA = nda.iterator();
       //when it is not dominated
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();

           if(dominatesAtLeastOne(s)) {
                return true;
            }
       }
       return false;
    }



   //true -- avl is dominated by s
   // false -- avl is not dominated by s
   public boolean isDominated(Solution s){
     if(avl.isEmpty()) {
            return false;
        }

     boolean domin = false;
     boolean isDomin = false;
     Iterator<Solution> iterNDA = iterator();
       //when it is not dominated
       while(iterNDA.hasNext()){
           Solution s1 = iterNDA.next();
           if(s1.dominates(s)) {
                domin = true;
            }
           if(s.dominates(s1)) {
                isDomin = true;
            }
       }
     if(!domin && isDomin) {
            return true;
        }
       //     if(firstObjective.floorKey(s.objectives[0]) == null && secondObjective.floorEntry(s.objectives[1]) == null)
       //         return true;
       return false;
   }

   public boolean isDominated (ArchiveSolutions nda){
       Iterator<Solution> iterNDA = nda.iterator();
       //when it is not dominated
       boolean domin = false;
       boolean isDomin = false;
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();

           if(dominates(s)) {
                domin = true;
            }
           if(s.dominates(this)) {
                isDomin = true;
            }
       }
       if(!domin && isDomin) {
            return true;
        }
       return false;
    }

   /////////////////////////////
   // remove a solution s that is found as beening sominated
   ////////////////////////////
   public void remove(Solution s){
        s.setFlagOperation(3);

        if(!this.contains(s)){
            System.err.println("why it is not contained ?" + s.toString() + " into " + avl.toString());
            contains(s);
            return;
        }
        Long identif = generateIdentif(s);
        Solution remove = avl.remove(identif);
        if(remove == null){
            System.out.println(" Remove inexistent solution !" + s.toString());
            System.out.println(" Existent solutions "+ avl.toString());
        }
        //for(int i = 0; i < this.nrObjectives; i++)
        //    objectives[i].remove(s.objectives[i]);
        //add in the other list
    }


    //check the consistency of the tree
    public void checkConsistency(Solution s){
        if(avl== null || avl.size() == 0) {
            return;
        }

        System.out.println("Check avl consistency absolite ");
        //first case of inconsistency: firstObjective and secondObjective have different size
        /*if(objectives[0].size() != avl.size()){
            //rebuid first and second obje
           System.err.println("Before " + avl.size() + ", f = " + objectives[0].size() );
           System.err.println("Avl" + avl.toString());
           for(int i = 0; i < this.nrObjectives; i++){
                System.err.println("obj " + i+ " " + objectives[i].toString());
                objectives[i].clear();
           }
           Iterator<Map.Entry<Long,Solution>> newI = avl.entrySet().iterator();
           while(newI.hasNext()){
                Solution sol = newI.next().getValue();
                for(int i = 0; i < this.nrObjectives; i++)
                    objectives[i].put(sol.objectives[i], sol.getIdentifNumber());
            }
            //sysncronize
            System.err.println("Sysncronized " + avl.size() + ", f = " + objectives[0].size());
            return;
        }

        //2. first and second objective have different identification values
        Iterator<Map.Entry<Long,Long>> m = entrySet().iterator();
        while(m.hasNext()){
            Map.Entry<Long,Long> tempL = m.next();
            if(!objectives[0].containsValue(tempL.getValue())){
                System.err.println("Inconsistency in the containt of the first and seconf objective");
                return;
            }
        }

        //3. avl has different objectes than it should
        m = objectives[0].entrySet().iterator();
        while(m.hasNext()){
            Map.Entry<Long,Long> tempL = m.next();
            if(!avl.containsKey(tempL.getValue().longValue())){
                System.err.println("Inconsistency in the containt of the first and seconf objective");
                return;
            } else {
                Solution s1 = avl.get(tempL.getValue().longValue());
                if(s1.objectives[0] != tempL.getKey().doubleValue()){
                    System.err.println("Inconsistency in the containt of the first and seconf objective");
                    return;
                }
                    
            }
         }*/
    }

    //constructs a new archive that is not dominated
        //optimize
    @Override public ArchiveSolutions  clone(){
            NonDominatedArchive newNDA = new NonDominatedArchive();
            Solution tempS;
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            while(i.hasNext()){
                tempS = i.next().getValue().clone();
                newNDA.add(tempS);                
            }
            //newNDA.setEpsilon();
            return newNDA;
    }
        
        //constructs two new archive: that is not dominated [0]
        //and [1] that is dominating
    public ArchiveSolutions[] getNonAndDominated(Solution s){
            NonDominatedArchive[] newNDA = new NonDominatedArchive[3]; // dominated
            newNDA[0] = new NonDominatedArchive(); // incomparable solutions
            newNDA[1] = new NonDominatedArchive(); // dominating
            newNDA[2] = new NonDominatedArchive();
            
            //newNDA[0].add(s);
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            while(i.hasNext()){
                Solution tempS = i.next().getValue().clone();
                if(tempS.dominates(s)) {
                    newNDA[1].add(tempS);
                } else if(!s.dominates(tempS)){
                    newNDA[0].add(tempS);
                }
                if(!s.dominates(tempS) && !tempS.dominates(s)){
                    newNDA[2].add(tempS);
                }
            }            
            return newNDA;
        }
	
    public ArchiveSolutions[] getNonAndDominated(ArchiveSolutions s){
            NonDominatedArchive[] newNDA = new NonDominatedArchive[3]; // dominated
            newNDA[0] = new NonDominatedArchive(); // incomparable solutions
            newNDA[1] = new NonDominatedArchive(); // dominating
            newNDA[2] = new NonDominatedArchive();

            Iterator<Solution> iter = s.iterator();
            while(iter.hasNext()){
                Solution tempS = iter.next();
                ArchiveSolutions[] tempNDA = getNonAndDominated(tempS);
                newNDA[0].add(tempNDA[0]);
                newNDA[1].add(tempNDA[1]);
                newNDA[2].add(tempNDA[2]);
            }
            return newNDA;
    }

    public ArchiveSolutions overlap(ArchiveSolutions  nda){
        ArchiveSolutions tempA = new NonDominatedArchive();
        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
	while(i.hasNext()){
            Solution v = i.next().getValue();
            if(nda.contains(v)){
                tempA.add(v);
            }
	}
	return tempA;
    }

    public ArchiveSolutions overlapInValue(ArchiveSolutions  nda){
        ArchiveSolutions tempA = new NonDominatedArchive();
        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
	while(i.hasNext()){
            Solution v = i.next().getValue();
            if(nda.contains(v)){
                tempA.add(v);
            }
            if(!nda.dominates(v) && !v.dominates(nda)){
                tempA.add(v);
            }
	}
	return tempA;
    }

    public TreeMap<Long,Solution> getNDA(){
        return avl;
    }

    public void setNDA(ArchiveSolutions nda){
        avl = nda.getNDA();
        dominatedList.clear();
    }
    
    @Override public String toString(){
            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            while(i.hasNext()){
                //sb.append("Component individual");
                Solution sol = i.next().getValue();
                for(int j = 0 ; j < sol.objectives.length; j++){
                    sb.append(sol.objectives[j]).append(" ");
                }
                sb.append("\n");
            }
            return sb.toString();
    }

    public String ndaToString(){
            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            while(i.hasNext()){
                //sb.append("Component individual");
                Solution sol = i.next().getValue();
                for(int j = 0 ; j < sol.objectives.length; j++){
                    sb.append(sol.objectives[j]).append(" ");
                }
                for(int j = 0 ; j < sol.items.length; j++){
                    sb.append(sol.items[j]).append(" ");
                }
                sb.append("\n");
            }
            return sb.toString();

    }

    public Solution[] ndaToArray(){
        Solution[] temp = new Solution[size()];
        int count = 0;
        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
        while(i.hasNext()){
                //sb.append("Component individual");
                temp[count] = i.next().getValue();
                count++;
        }
            
        return temp;
    }

    public void reset(){
            avl.clear();
            if(solutions != null){
                for(int i = 0; i < solutions.size(); i++){
                    solutions.set(i,null);
                }
            }
            //for(int i = 0; i < this.nrObjectives; i++)
            //    objectives[i].clear();
            //dominatedList.clear();
        }
        
    public Iterator<Solution> iterator(){
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            Stack<Solution> v = new Stack<Solution>();
            while(i.hasNext()){
                v.add(i.next().getValue());
            }
            return v.iterator();
        }
        
    public int size(){
            return avl.size();
        }

        ////////////////////////////
        // set characteristics
    public void setTypeProblem(boolean minimization){
            this.minimization = minimization;
        }

    //////////////////////////
    // the memory part of this
    // the solutions that are now in NDA are included in dominatedListSolutions
    //////////////////////////////
    public void update(ArchiveSolutions newNDA){
        if(this.sizeDominatedList == 0 || memory == false)
            return;

        Iterator<Solution> iter = newNDA.iterator();
        while(iter.hasNext()){
            Solution s = iter.next();
            if(dominatedList.contains(s))
                continue;
            while(sizeDominatedList <= this.dominatedList.size()){
                // remore the first vector which is also the last one
                dominatedList.remove(0);
                //dominatedList_Times.remove(0);
            }
            dominatedList.add(s);
            //this.dominatedList_Times(time);
        }
    }

    public int sizeDominated(){
        return dominatedList.size();
    }

   public int typeArchive(){
       return 0;
   }
   /* @Override public void setEpsilon(PerturbatorStrategies epsilon){
        this.epsilon = epsilon;
    }

    @Override public PerturbatorStrategies getEpsilon(){
        return epsilon;
    }*/
   
   //////////////////////////////
   // scalarization
   /////////////////////////////////
      //////////////////////
    // scalarizer and their best solutions
    private Stack<Solution> solutions = new Stack<Solution>();
    private SetScalarizeres scal;

    //public SingleValuedArchive(){
    //}
    
    NonDominatedArchive(SetScalarizeres sc){
        //solutions.add(s1);
        scal = sc;
        if(solutions == null) {
            solutions = new Stack<Solution>();
        }
        for(int i = 0; i < scal.size(); i++) {
            solutions.add(null);
        }
        //currentScal = sc.chooseScalarizer();
    }

    //private boolean tempR;

    @Override public boolean add(Solution s, Scalarizer sc){
        
        add(s);
        
        if(scal != null && scal.contains(sc)){
            int index = scal.indexOf(sc);
            Solution thisS = solutions.get(index);
            if(thisS == null || s.dominates(thisS,sc)){
                //solutions.remove(index);
                solutions.set(index, s);
                return true;
            }
            return false;
        }
        solutions.add(s);
        if(scal == null){
            //scal = new 
        }
        scal.add(sc);
        return true;
    }

    public boolean addSolution(Scalarizer sc){
        if(scal != null && scal.contains(sc)){
            int index = scal.indexOf(sc);
            Solution thisS = solutions.get(index);
            if(thisS != null){
                add(thisS);
                return true;
            } 
        }
        return false;
    }
   
    public boolean add(Solution s, int indentif){
       Solution thisS = solutions.get(indentif);
       Scalarizer sc = this.scal.getScalarizer(indentif);
       if(s.dominates(thisS,sc)){
            //solutions.remove(indentif);
            solutions.set(indentif, s);
            return true;
       }
       return false;
   }

   public boolean add(Scalarizer sc){
       if(!scal.contains(sc)){
            scal.add(sc);
            solutions.add(null);
            return true;
       }
       return false;
   }
   
    @Override
   public void setScalarizer(int indentif){
       //currentScal = indentif;
       scal.setScalarizer(indentif);
   }

    @Override
   public void setScalarizer(Scalarizer sc){
       if(scal.contains(sc)){
           scal.setScalarizer(sc);
           //currentScal = scal.getCurrentIndex();
       }
   }

   /* */
    @Override
   public Solution get(int indentif){
        return solutions.get(indentif);
   }

    @Override
   public Solution get(Scalarizer sc){
        int index = scal.indexOf(sc);
        if(index > -1) {
            return solutions.get(index);
        }
        return null;
   }

    @Override
   public boolean isDominated(Solution s, Scalarizer sc){
       if(solutions.isEmpty()) {
            return false;
        }
       int index = scal.indexOf(sc);
       if(index > -1) {
            return solutions.get(index).isDominated(s, sc);
        }
       return false;
   }

    @Override
   public boolean dominates (Solution s, Scalarizer sc){
       if(solutions.isEmpty()) {
            return false;
        }
       int index = scal.indexOf(sc);
       if(index > -1 && solutions.get(index) != null) {
            return solutions.get(index).dominates(s, sc);
        }
       return false;
   }

    @Override
   public boolean incomparable(Solution s, Scalarizer sc){
       if(solutions.isEmpty()) {
            return false;
        }
       int index = scal.indexOf(sc);
       if(index > -1) {
            return solutions.get(index).incomparable(s, sc);
        }
       return false;
  }


}
