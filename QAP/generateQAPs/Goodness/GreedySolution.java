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

import QAP.general.GenerateProblem.GenerateSolution;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import java.io.*;
import java.util.*;

public class GreedySolution  implements GenerateSolution{

    private Stack<int[]> thisStack = new Stack<int[]>();
    private TreeMap<Double,Stack<int[]>> matrixDistances = new TreeMap<Double,Stack<int[]>>();
    private Stack<Integer> selected1 = new Stack<Integer>();
    private Stack<Integer> selected2 = new Stack<Integer>();
    private boolean existsPath = false;
    private boolean possiblePath = true;
    private int[] value = new int[2];

    private FlowMatrixWithDistr flow;
    private DistanceMatrixWithDistr distance;

    //private QAPs instance = new QAPs();

    private int[] items;

    public GreedySolution(){}
    
    public GreedySolution(DistanceMatrixWithDistr dist, FlowMatrixWithDistr f){
        flow = f;
        distance = dist;
    }

    public void existsPermutation(int[] value){
        if(existsPath || !possiblePath) {
            return;
        }
        if(selected1.size() >= distance.getNrFacilities()){
            existsPath = true;
            //possiblePath = true;
            return;
        }
        //if exists one of them twice in the lists --> return
        if((selected1.contains(value[0]) && selected2.contains(value[0])) || (selected1.contains(value[1]) && selected2.contains(value[1]))) {
            return;
        }
        //if not existing add
        if(!selected1.contains(value[0]) && !selected2.contains(value[1]) && !selected1.contains(value[1]) && !selected2.contains(value[0])){
             selected1.add(value[0]);
             selected2.add(value[1]);
             return;
        }
        //if only one exists
        if(!selected1.contains(value[0]) && !selected2.contains(value[0])){
            if(selected1.contains(value[1])){
                selected1.add(value[0]);
                selected2.add(value[1]);
            } else if(selected2.contains(value[1])){
                    selected2.add(value[0]);
                    selected1.add(value[1]);
            }
            return;
        }
        if(!selected1.contains(value[1]) && !selected2.contains(value[1])){
            if(selected1.contains(value[0])){
                selected1.add(value[1]);
                selected2.add(value[0]);
            } else if(selected2.contains(value[0])){
                    selected2.add(value[1]);
                    selected1.add(value[0]);
            }
            return;
        }

        if(selected1.contains(value[0])){
            if(selected1.indexOf(value[0]) != selected1.lastIndexOf(value[0])) {
                return;
            }
            if(selected2.contains(value[1])){
                if(selected2.indexOf(value[1]) != selected2.lastIndexOf(value[1])) {
                    return;
                }
                 selected2.add(value[0]);
                 selected1.add(value[1]);
                 return;
            }

            int rearange = -1;
            if(selected1.contains(value[1])) {
                rearange = selected1.indexOf(value[1]);
            }
            selected1.add(value[1]);
            selected2.add(value[0]);
            while(rearange != -1){
                int temp = selected2.remove(rearange);
                int temp1 = selected1.remove(rearange);
                if(selected2.contains(temp1)) {
                    rearange = selected2.indexOf(temp1);
                }
                else {
                    rearange = -1;
                }
                selected1.add(temp);
                selected2.add(temp1);
            }
            if(selected1.size() == distance.getNrFacilities()-1) {
                existsPath = true;;
            }
            return;
        }
        if(selected1.contains(value[1])){
            if(selected1.indexOf(value[1]) != selected1.lastIndexOf(value[1])) {
                return;
            }

            if(selected2.contains(value[0])){
                if(selected2.indexOf(value[0]) != selected2.lastIndexOf(value[0])) {
                    return;
                }
                selected2.add(value[1]);
                selected1.add(value[0]);
                return;
            }

            int rearange = -1;
            if(selected1.contains(value[0])) {
                rearange = selected1.indexOf(value[0]);
            }
            selected1.add(value[0]);
            selected2.add(value[1]);
            //if 2 selected1 value[0] -->
            //arrangePermutation(); ??
            while(rearange != -1){
                int temp = selected2.remove(rearange);
                int temp1 = selected1.remove(rearange);
                if(selected2.contains(temp1)) {
                    rearange = selected2.indexOf(temp1);
                }
                else {
                    rearange = -1;
                }
                selected1.add(temp);
                selected2.add(temp1);
            }
            if(selected1.size() == distance.getNrFacilities()-1) {
                existsPath = true;;
            }
            return;
        }
        //second raw
        if(selected2.contains(value[0])){
            if(selected2.indexOf(value[0]) != selected2.lastIndexOf(value[0])) {
                return;
            }
            if(selected1.contains(value[1])){
                if(selected1.indexOf(value[1]) != selected1.lastIndexOf(value[1])) {
                    return;
                }
                selected2.add(value[1]);
                selected1.add(value[0]);
                return;
            }

            //int temp = selected2.get(selected1.indexOf(value[0]));
            //if(selected1.contains(temp))
            //    return;
            //temp = selected1.get(selected2.indexOf(value[1]));
            //if(selected2.contains(temp))
            //    return;
            //arrange the permutation
            //arrangePermutation(); ??
            int rearange = -1;
            if(selected2.contains(value[1])) {
                rearange = selected2.indexOf(value[1]);
            }
            selected2.add(value[1]);
            selected1.add(value[0]);
            while(rearange != -1){
                int temp = selected1.remove(rearange);
                int temp1 = selected2.remove(rearange);
                if(selected1.contains(temp1)) {
                    rearange = selected1.indexOf(temp1);
                }
                else {
                    rearange = -1;
                }
                selected2.add(temp);
                selected1.add(temp1);
            }
            if(selected2.size() == distance.getNrFacilities()-1) {
                existsPath = true;;
            }
            return;
        }
        if(selected2.contains(value[1])){
            if(selected2.indexOf(value[1]) != selected2.lastIndexOf(value[1])) {
                return;
            }
            if(selected1.contains(value[0])){
                if(selected1.indexOf(value[0]) != selected1.lastIndexOf(value[0])) {
                    return;
                }
                selected2.add(value[0]);
                selected1.add(value[1]);
                return;
            }
            //int temp = selected2.get(selected1.indexOf(value[1]));
            //if(selected1.contains(temp))
            //    return;
            //temp = selected1.get(selected2.indexOf(value[0]));
            //if(selected2.contains(temp))
            //    return;
            int rearange = -1;
            if(selected2.contains(value[0])) {
                rearange = selected2.indexOf(value[0]);
            }
            selected2.add(value[0]);
            selected1.add(value[1]);
            //if 2 selected1 value[0] -->
            //arrangePermutation(); ??
            while(rearange != -1){
                int temp = selected1.remove(rearange);
                int temp1 = selected2.remove(rearange);
                if(selected1.contains(temp1)) {
                    rearange = selected1.indexOf(temp1);
                }
                else {
                    rearange = -1;
                }
                selected2.add(temp);
                selected1.add(temp1);
            }
            if(selected2.size() == distance.getNrFacilities()-1) {
                existsPath = true;
            }
            return;
        }

        System.err.print("Case which does not exists " + selected1.size()+" \t"+ selected1.toString() + " \nCase which does not exists " + selected2.size()+" \t"+ selected2.toString() +"\n");
        //this.possiblePath = false;
   }

   public Stack<Integer> getPermutation(){
       return permutation;
   }
   
   private long totalDist = 0;
   private Stack<Integer> permutation;
   private long[] solution;
   private int[][] dist;
   private int[][][] flowMy;
   public Stack<Integer> generateSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow){
        //sort the distances
        value[0] = 0;
        value[1] = 0;
        permutation = new Stack<Integer>();
        solution = new long[flow.getNrObjectives()];
        double key;
        matrixDistances.clear();
        thisStack.clear();

        dist = distance.getDistance();
        for(int i = 0; i < distance.getNrFacilities(); i++){
            for(int j = i+1; j < distance.getNrFacilities(); j++){
                 key = dist[i][j];
                 int[] newValue = new int[2];
                 newValue[0] = i;
                 newValue[1] = j;

                 if(!matrixDistances.isEmpty() & matrixDistances.containsKey(key)) {
                    thisStack = matrixDistances.get(key);
                }
                 else {
                    thisStack = new Stack<int[]>();
                }
                 thisStack.push(newValue);
                 matrixDistances.put(key, thisStack);
            }
        }

        //extract a good permutation
        while(matrixDistances.size() > 0 && !existsPath && possiblePath){
            key = matrixDistances.firstKey();
            thisStack = matrixDistances.get(key);
            while(!thisStack.empty()){
                value = thisStack.pop();
                this.existsPermutation(value);
                //arrange the permutation

                if(existsPath || !possiblePath){
                    break;
                }
              }
            matrixDistances.remove(key);
        }
        if(!possiblePath || !existsPath) {
            return null;
        }

        //make the permutation; compute the distance
        //find the margine; it appears one time in selected1 and not in selected2

        int i = 0, temp = 0, start = 0;
        for(int j = 0; j < distance.getNrFacilities(); j++) {
            if (!selected2.contains(selected1.get(i))) {
                start = j;
            }
        }
        temp = start;
        while(i < distance.getNrFacilities()){
            value[0] = selected1.get(temp);
            value[1] = selected2.get(temp);
            totalDist+=dist[value[0]][value[1]];
            if(!permutation.contains(value[0])){
                permutation.add(value[0]);
                i++;
                //find the next value
                temp = selected1.indexOf(value[1]);
            } else {
                temp = -1;
            }
            //it is a margine
            if(temp == -1){
                System.err.println("This "+ i+ " should not be here");
                if(!permutation.contains(value[1])){
                    permutation.add(value[1]);
                    i++;
                }
                //find another
                for(int j = 0; j < distance.getNrFacilities(); j++){
                    boolean exists = false;
                    for(int k = 0; k < i; k++) {
                        if (permutation.contains(selected1.get(j))) {
                            exists = true;
                            break;
                        }
                    }
                    if(!exists){
                        temp = j;
                        break;
                    }
                    exists = false;
                    for(int k = 0; k < i; k++) {
                        if (permutation.contains(selected2.get(j))) {
                            exists = true;
                            break;
                        }
                    }
                    if(!exists){
                        temp = j;
                        break;
                    }
                }
            }
        }

        items = new int[permutation.size()];
        for(int j = 0; j < items.length; j++){
            items[j] = permutation.get(j);
       }
        computeSolution(distance,flow,items);
        return permutation;
    }

    public long getTotalDist(){
        return totalDist;
    }

    public void writeMeasurements(BufferedWriter fDistO, GreedySolution sol){

        int[] maxCoord = distance.getMaxCoord();
        try{
          //value of a greedy solutions (greedy solution already quite good)
          if(maxCoord[0] != -1 && maxCoord[1] != -1){
              sol.generateSolution(distance,flow);
              fDistO.write("Permutation from greedy search \n");
              for(int i = 0; i < distance.getNrFacilities(); i++) {
                    fDistO.write(sol.getPermutation().get(i) + "\t");
                }
              fDistO.write("\n Solution" + sol.solution[0] + "\t" + sol.solution[1] + "\n Distance"+ sol.getTotalDist() + "\n");
          }
        } catch (Exception e){
			System.out.println("Write not possible:"+fDistO.toString());
			e.printStackTrace(System.out);
	}

    }

    /*public long[] computeSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow, Stack<Integer> permutation){
        Arrays.fill(solution, 0);
        dist = distance.getDistance();
        flowMy = flow.getFlow();
        for(int i = 0; i < flow.getNrObjectives(); i++){
           for(int j = 0; j < distance.getNrFacilities(); j++) {
                for (int k = j + 1; k < distance.getNrFacilities(); k++) {
                    if (dist[j][k] == dist[k][j] & flowMy[i][permutation.get(j)][permutation.get(k)] == flowMy[i][permutation.get(k)][permutation.get(j)]) {
                        solution[i] += 2 * dist[j][k] * flowMy[i][permutation.get(j)][permutation.get(k)];
                    } else {
                        System.out.println("Matrices are not symetrical");
                    }
                }
            }
        }
        return solution;
    }*/

     @Override public long[] computeSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow, int[] items){
        //flowMy = new int[1][flow.getFlow().length][flow.getFlow()[0].length];
        //for(int i = 0; i < flow.getFlow().length; i++){
        //    for(int j = 0; j < flow.getFlow()[i].length; j++){
        ///        flowMy[0][i][j] = flow.getFlow()[i][j];
        //    }
        //}
        solution[0] = 0;
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
            

        return solution; //instance.computeSolution(distance.getDistance(), flowMy, items);
        /*Arrays.fill(solution, 0);
        dist = distance.getDistance();
        flowMy = flow.getFlow();
        for(int i = 0; i < flow.getNrObjectives(); i++){
           for(int j = 0; j < distance.getNrFacilities(); j++) {
                for (int k = j + 1; k < distance.getNrFacilities(); k++) {
                    if (dist[j][k] == dist[k][j] & flowMy[i][permutation.get(j)][permutation.get(k)] == flowMy[i][items[k]][permutation.get(j)]) {
                        solution[i] += 2 * dist[j][k] * flowMy[i][permutation.get(j)][permutation.get(k)];
                    } else {
                        System.out.println("Matrices are not symetrical");
                    }
                }
            }
        }
        return solution;*/
    }
}
