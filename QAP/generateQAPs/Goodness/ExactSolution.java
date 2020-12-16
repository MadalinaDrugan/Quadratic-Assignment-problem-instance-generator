/**
 *
 * @author (C) Madalina M. Drugan 2009-2013
 * Artificial Intelligence Lab, Vrije Universiteit Brussels,
 * Pleinlaan 2, 1040 Brussels, Belgium
 * 
 
 * Please contact me - Madalina Drugan - if you have any comments, suggestions
 * or questions regarding this program or composite QAP problems. My email
 * address is mdrugan@vub.ac.be

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version. 

 * This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. 

 * The GNU General Public License is available at:
 *     http://www.gnu.org/copyleft/gpl.html
 *  or by writing to: 
 *       The Free Software Foundation, Inc., 
 *       675 Mass Ave, Cambridge, MA 02139, USA.  

 *
 */

package QAP.generateQAPs.Goodness;

import QAP.Archives.*;
import QAP.Util.PermutationGenerator;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import java.io.*;
import java.util.*;

/**
 *
 * @author madalina
 */
public class ExactSolution extends GreedySolution{

    //values of the greedy solution
    private Stack<int[]> thisStack = new Stack<int[]>();
    private TreeMap<Double,Stack<int[]>> matrixDistances = new TreeMap<Double,Stack<int[]>>();
    private int[] value = new int[2];

    private FlowMatrixWithDistr flow;
    private DistanceMatrixWithDistr distance;

    //private long totalDist = 0;
    private Stack<Integer> permutation;
    private long[] solution;
    private int[] items;

    private NonDominatedArchive nda = new NonDominatedArchive();

    private NonDominatedArchive ndaMin = new NonDominatedArchive();

    private Solution_QAPs sol = new Solution_QAPs();
    //private QAPs instance = new QAPs();

   private int[][] dist;
   private int[][][] flowMy;

   public ExactSolution(DistanceMatrixWithDistr dist, FlowMatrixWithDistr f){
        flow = f;
        distance = dist;
    }

    public ExactSolution(){}

    public void setParameters(DistanceMatrixWithDistr dist, FlowMatrixWithDistr f){
        flow = f;
        distance = dist;
    }

    public NonDominatedArchive returnNDA(){
        return nda;
    }

    public NonDominatedArchive returnNDAMin(){
        // randomly generate a solution
        return ndaMin;
    }

    @Override public long[] computeSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow, int[] items){
        //flowMy = new int[1][flow.getFlow().length][flow.getFlow()[0].length];
        //for(int i = 0; i < flow.getFlow().length; i++){
        //    for(int j = 0; j < flow.getFlow()[i].length; j++){
        //        flowMy[0][i][j] = flow.getFlow()[i][j];
        //    }
        //}
        long[] obj = new long[1];
        int[][] myDist = distance.getDistance();
        int[][] myFlow = flow.getFlow();
        for(int j1 = 0; j1 < myDist.length; j1++) {
             for (int k1 = j1 + 1; k1 < myDist[0].length; k1++) {
                  if (myDist[j1][k1] == myDist[k1][j1] & myFlow[items[j1]][items[k1]] == myFlow[items[k1]][items[j1]]) {
                                    obj[0] += 2 * myDist[j1][k1] * myFlow[items[j1]][items[k1]];
                  } else {
                                    System.out.println("Not symetrical matrices");
                  }
               }
        }

        return obj;
    }

    public long[] computeSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow, Stack<Integer> permutation){
        items = new int[permutation.size()];
        for(int i = 0; i < items.length; i++){
            items[i] = permutation.get(i);
        }
        long[] obj = new long[1];
        int[][] myDist = distance.getDistance();
        int[][] myFlow = flow.getFlow();
        for(int j1 = 0; j1 < myDist.length; j1++) {
             for (int k1 = j1 + 1; k1 < myDist[0].length; k1++) {
                  if (myDist[j1][k1] == myDist[k1][j1] & myFlow[items[j1]][items[k1]] == myFlow[items[k1]][items[j1]]) {
                                    obj[0] += 2 * myDist[j1][k1] * myFlow[items[j1]][items[k1]];
                  } else {
                                    System.out.println("Not symetrical matrices");
                  }
               }
        }
        return obj;
     }
    
    @Override public Stack<Integer> generateSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow){
        //sort the distances
        value[0] = 0;
        value[1] = 0;
        //permutation = new Vector<Integer>();
        solution = new long[flow.getNrObjectives()];
        //double key;
        matrixDistances.clear();
        thisStack.clear();
        
        nda = new NonDominatedArchive();
        ndaMin = new NonDominatedArchive();

        PermutationGenerator x = new PermutationGenerator(distance.getNrFacilities());
        int[] indices;
        //Stack<Integer> permutation_gen;
        while (x.hasMore ()) {
            permutation = new Stack<Integer>();
            indices = x.getNext();
            items = new int[indices.length];
            for (int i = 0; i < indices.length; i++) {
                permutation.add(indices[i]);
                items[i] = indices[i];
            }

            //evaluate the solution
            //fill the solution first

            int[][] myDist = distance.getDistance();
            int[][] myFlow = flow.getFlow();
            for(int j1 = 0; j1 < myDist.length; j1++) {
             for (int k1 = j1 + 1; k1 < myDist[0].length; k1++) {
                  if (myDist[j1][k1] == myDist[k1][j1] & myFlow[items[j1]][items[k1]] == myFlow[items[k1]][items[j1]]) {
                                    solution[0] += 2 * myDist[j1][k1] * myFlow[items[j1]][items[k1]];
                  } else {
                                    System.out.println("Not symetrical matrices");
                  }
               }
            }
            
            
            //solution = ; //instance.computeSolution(distance.getDistance(), flowMy, items);
            sol = new Solution_QAPs(solution,permutation);

            if(!nda.contains(sol)){//  && !nda.dominates(sol)){
                nda.add(sol);
            }
            /*if(!ndaMin.contains(sol) && sol.dominates(ndaMin)){
                ndaMin.add(sol);
            }*/
        //System.out.println (permutation.toString ());
        }
        return null;
    }

    public void writeMeasurements(BufferedWriter fDistO){
        try{
            int[] maxCoord = distance.getMaxCoord();
            //value of a greedy solutions (greedy solution already quite good)
           if(maxCoord[0] != -1 && maxCoord[1] != -1){
              //generateSolution(distance,flow);
              fDistO.write("Permutation from exchaustive search search \n");
              fDistO.write(nda.ndaToString());
           }
        } catch (Exception e){
			System.out.println("Write not possible:"+fDistO.toString());
			e.printStackTrace(System.out);
	}

    }


}
