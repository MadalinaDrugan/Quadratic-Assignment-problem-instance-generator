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

package QAP.generateQAPs.Flows;

import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Distributions.*;
import QAP.generateQAPs.Distributions.WithHoles.DiscreteDistributionWithHoles;
import java.io.*;
import java.util.*;

public class FlowMatrixChebsev extends FlowMatrixWithDistr{

    private int[][] flow;
    private int nrFacilities;
    private int nrObjectives;

    private int maxFlow;

    private DiscreteDistributionWithHoles distribution;

    private DistanceMatrixWithDistr distance;

    private Integer[] tempFlow;
    private int[] tempDistance;
    private TreeMap<Integer,int[][]> tempDStack;
    private int[][] indexes;
    private int[][] tempD;

    private static java.util.Random r = new java.util.Random();

    public FlowMatrixChebsev(int nrF, DiscreteDistributionWithHoles myDistr){
        nrFacilities = nrF;
        nrObjectives = 1;

        distribution = myDistr;

        flow = new int[nrFacilities][nrFacilities];

    }

    public FlowMatrixChebsev(){
    }

    public void setDistanceMatrix(DistanceMatrixWithDistr dist){
        distance = dist;

        tempFlow = new Integer[nrFacilities*(nrFacilities-1)/2];
        tempDistance = new int[nrFacilities*(nrFacilities-1)/2];

        tempD = distance.getDistance();

        indexes = new int[nrFacilities*(nrFacilities-1)/2][2];
        tempDStack = new TreeMap<Integer,int[][]>();
        int count = 0;
        for(int i = 0; i < nrFacilities; i++){
            for(int j = i+1; j < nrFacilities; j++){
                tempDistance[count] =tempD[i][j];
                if(!tempDStack.containsKey(tempD[i][j])){
                    int[][] temp = new int[1][2];
                    temp[0][0] = i;
                    temp[0][1] = j;
                    tempDStack.put(tempD[i][j], temp);
                } else {
                    int[][] temp = tempDStack.get(tempD[i][j]);
                    int[][] temp1 = new int[temp.length+1][2];
                    for(int t = 0; t < temp.length; t++){
                        temp1[t] = Arrays.copyOf(temp[t], temp[t].length);
                    }
                    temp1[temp.length][0] = i;
                    temp1[temp.length][1] = j;
                    tempDStack.put(tempD[i][j], temp1);
                }
                count++;
            }
        }
        Arrays.sort(tempDistance);

        count = 0;
        while(!tempDStack.isEmpty()){
            int value = tempDStack.firstKey();
            int[][] temp = tempDStack.get(value);
            for(int i = 0; i < temp.length; i++){
                indexes[count][0] = temp[i][0]; //, temp[0].length);
                indexes[count][1] = temp[i][1];
                count++;
            }
            tempDStack.remove(value);
        }
        if(count != tempDistance.length){
            System.err.print("count " + count + " distance "+ tempDistance.length);
        }
    }

    public void setDistanceMatrix(DistanceMatrixWithDistr dist, int[][] allocated, int countA){
        distance = dist;

        tempFlow = new Integer[nrFacilities*nrFacilities/2 - countA/2];
        tempDistance = new int[nrFacilities*nrFacilities/2 - countA/2];

        tempD = distance.getDistance();


        indexes = new int[nrFacilities*nrFacilities/2 - countA/2][2];
        tempDStack = new TreeMap<Integer,int[][]>();
        int count = 0;
        for(int i = 0; i < nrFacilities; i++){
            for(int j = i+1; j < nrFacilities; j++){
                if(allocated[i][j] == 1){
                    continue;
                }

                tempDistance[count] =tempD[i][j];
                if(!tempDStack.containsKey(tempD[i][j])){
                    int[][] temp = new int[1][2];
                    temp[0][0] = i;
                    temp[0][1] = j;
                    tempDStack.put(tempD[i][j], temp);
                } else {
                    int[][] temp = tempDStack.get(tempD[i][j]);
                    int[][] temp1 = new int[temp.length+1][2];
                    for(int t = 0; t < temp.length; t++){
                        temp1[t] = Arrays.copyOf(temp[t], temp[t].length);
                    }
                    temp1[temp.length][0] = i;
                    temp1[temp.length][1] = j;
                    tempDStack.put(tempD[i][j], temp1);
                }
                
                count++;
            }
        }
        Arrays.sort(tempDistance);

        count = 0;
        while(!tempDStack.isEmpty()){
            int value = tempDStack.firstKey();
            int[][] temp = tempDStack.get(value);
            for(int i = 0; i < temp.length; i++){
                indexes[count][0] = temp[i][0]; //, temp[0].length);
                indexes[count][1] = temp[i][1];
                count++;
            }
            tempDStack.remove(value);
        }
        if(count != tempDistance.length){
            System.err.print("count " + count + " distance "+ tempDistance.length + "\n");
        }
    }

    public void setDistanceMatrix(DistanceMatrixWithDistr dist, Stack<Integer> freeS){
        distance = dist;

        tempFlow = new Integer[freeS.size()];
        tempDistance = new int[freeS.size()];

        tempD = distance.getDistance();

        indexes = new int[freeS.size()][2];
        tempDStack = new TreeMap<Integer,int[][]>();
        int count = 0;
        for(int i = 0; i < freeS.size(); i++){
            int[] tempI = new int[2];
            tempI[0] = freeS.get(i)/1000;
            tempI[1] = freeS.get(i)%1000;
                   
            tempDistance[count] =tempD[tempI[0]][tempI[1]];
            if(!tempDStack.containsKey(tempD[tempI[0]][tempI[1]])){
                int[][] temp = new int[1][2];
                temp[0][0] = tempI[0];
                temp[0][1] = tempI[1];
                tempDStack.put(tempD[tempI[0]][tempI[1]], temp);
            } else {
                int[][] temp = tempDStack.get(tempD[tempI[0]][tempI[1]]);
                int[][] temp1 = new int[temp.length+1][2];
                for(int t = 0; t < temp.length; t++){
                     temp1[t] = Arrays.copyOf(temp[t], temp[t].length);
                }
                temp1[temp.length][0] = tempI[0];
                temp1[temp.length][1] = tempI[1];
                    tempDStack.put(tempD[tempI[0]][tempI[1]], temp1);
                }
                
                count++;
        }
        Arrays.sort(tempDistance);

        count = 0;
        while(!tempDStack.isEmpty()){
            int value = tempDStack.firstKey();
            int[][] temp = tempDStack.get(value);
            for(int i = 0; i < temp.length; i++){
                indexes[count][0] = temp[i][0]; //, temp[0].length);
                indexes[count][1] = temp[i][1];
                count++;
            }
            tempDStack.remove(value);
        }
        if(count != tempDistance.length){
            System.err.print("count " + count + " distance "+ tempDistance.length + "\n");
        }
    }

    private class compareLess implements Comparator<Integer>{
        public int compare(Integer o1, Integer o2){
            if(o1 < o2){
                return -1;
            }
            if(o1 > o2){
                return 1;
            }
            return 0;
        }
    }
    private compareLess myCompare;

    private TreeSet<Integer> indexI;
    public int[][] createFlowMatrices(DistanceMatrixWithDistr distr, int[] zeroP, int countUp){

        indexI = new TreeSet<Integer>();

        for(int i = 0; i < zeroP[0]; i++){
                tempFlow[i] = 0;
        }

        // the next largest
        int countT = countUp - zeroP[0];
        for(int i = zeroP[0]; i < tempFlow.length; i++){
               if(countT > 0){
                    tempFlow[i] = distribution.generateLow();
                    countT--;
               } else {
                    tempFlow[i] = distribution.generateHigh();
               }
               indexI.add(tempFlow[i]);
        }

        Arrays.sort(tempFlow);

        for(int count = 0; count < tempFlow.length; count++) {
           flow[indexes[count][0]][indexes[count][1]] = tempFlow[tempFlow.length -1 - count];

           flow[indexes[count][1]][indexes[count][0]] = flow[indexes[count][0]][indexes[count][1]];
        }

        indexI.clear();

       return flow;
    }

    @Override public boolean setElement(int[] index, int value){
       flow[index[0]][index[1]] = value;
       return true;
    }

    @Override public int getElement(int[] index){
       return flow[index[0]][index[1]];
    }

    @Override public FlowMatrixChebsev clone(){
        FlowMatrixChebsev thisFlow = new FlowMatrixChebsev(nrFacilities,distribution);
        return thisFlow;
    }

    @Override public void swapsInFlow(int i, int j, int[][] flow){
        //swaps rows
            for(int k = 0; k < flow[0].length; k++){
                int temp = flow[j][k];
                flow[j][k] = flow[i][k];
                flow[i][k] = temp;
            }

            //swaps columns
            for(int k = 0; k < flow[0].length; k++){
                int temp = flow[k][j];
                flow[k][j] = flow[k][i];
                flow[k][i] = temp;
            }
    }

    public void swapsInFlow(int i, int j){
        swapsInFlow(i,j,flow);
    }

    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < flow.length; i++){
            for(int j = 0; j < flow[0].length; j++){
                sb.append(flow[i][j]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");
       return sb.toString();
    }

    @Override
    public int getNrFacilities(){
        return nrFacilities;
    }

    @Override
    public int getNrObjectives(){
        return nrObjectives;
    }

    @Override
    public int[][] getFlow(){
        return flow;
    }

    @Override
    public DiscreteDistribution getDistribution(){
        return distribution;
    }

    public void writeQAP(FlowMatrixChebsev flow, String nameF){
        int[][] currFlow = flow.getFlow();
        try{
            File myFile = new File(nameF);
            FileWriter fileStream = new FileWriter(myFile,true);
            myFile.createNewFile();
            fileStream = new FileWriter(myFile);
            BufferedWriter fDistI = new BufferedWriter(fileStream);

            //write the header
            String line = "facilities = " + String.valueOf(nrFacilities) + " objectives = " + String.valueOf(nrObjectives) + "  max flows = " + String.valueOf(flow.maxFlow) ;
            fDistI.write(line + " \n ");


            //write the flow - first dimension
            for(int currE = 0; currE < nrFacilities; currE++){
                for(int currF = 0; currF < nrFacilities; currF++) {
                    if (currF < nrFacilities - 1) {
                        fDistI.write(currFlow[currE][currF] + "\t");
                    } else {
                        fDistI.write(currFlow[currE][currF] + "\n");
                    }
                }
            }
            fDistI.write("\n");

            fDistI.write("\n");

            fDistI.flush();
            fDistI.close();
        } catch (Exception e){
			System.out.println("Write not possible:"+ nameF);
			e.printStackTrace(System.out);
	}
    }

 }
