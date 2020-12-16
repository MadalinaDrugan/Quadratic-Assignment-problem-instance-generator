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

import QAP.generateQAPs.Correlation.CorrelationGroups;
import QAP.generateQAPs.Distributions.DiscreteDistribution;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Stack;

public class FlowMatrixWithDistr{

    private int[][] flow;
    private int nrFacilities;
    
    private int maxFlow;

    private DiscreteDistribution distribution;

    public FlowMatrixWithDistr(int nrF, DiscreteDistribution myDistr){
        nrFacilities = nrF;
        
        distribution = myDistr;
        flow = new int[nrFacilities][nrFacilities];
    }

    public FlowMatrixWithDistr(){
    }

    public int[][] createFlowMatrices(){
        return createFlowMatrices(flow);
    }

    public int[][] createFlowMatrices(int[][] flow){
       for(int currF = 0; currF < nrFacilities; currF++) {
          for (int currE = currF; currE < nrFacilities; currE++) {
              if (currE == currF) {
                    flow[currE][currF] = 0;
              } else {
                    flow[currE][currF] = distribution.generate();
                    flow[currF][currE] = flow[currE][currF];
              }
          }
      }

      return flow;
    }

    public int[][] createFlowMatrices(int[][] distance, CorrelationGroups corr){
        newDistance = new double[nrFacilities][nrFacilities];
        for(int currF = 0; currF < nrFacilities; currF++) {
            for (int currE = currF; currE < nrFacilities; currE++) {
                if (currE == currF) {             
                    flow[currE][currF] = 0;
                } else {
                    double[] inputs = new double[1];
                    inputs[0] = distance[currF][currE];
                    
                    double[] outputs = corr.correlation(inputs, distribution);
                    
                    newDistance[currF][currE] = outputs[0];
                    newDistance[currE][currF] = newDistance[currF][currE];
                    
                    flow[currE][currF] = distribution.generateFrom(outputs[1]);
                    flow[currF][currE] = flow[currE][currF];
                }
            }
       }
 
       return flow;
    }

    public double[][] normalize(CorrelationGroups corr){
       double maxValNorm = -1; 
       corrValues = corr.getTransfMatrix();
       Stack<Integer> st = new Stack<Integer>();
       
       for(int i = 0; i < corrValues.length; i++){
           
           boolean corrD = false;
           for(int j = 0; j < corrValues[0].length; j++){
               if(i != j && corrValues[i][j] != 0){
                   corrD = true;
               }
           }
                      
           if(corrD && i != 0){
               st.add(i);
               
               for(int j = 0; j < nrFacilities; j++){
                   for(int k = 0; k < nrFacilities; k++){
                      if(maxValNorm == -1){
                        maxValNorm = basicFlow[j][k];
                      } else if(maxValNorm < basicFlow[j][k]){
                        maxValNorm = basicFlow[j][k];
                      }
                   }
               }
              
           } else if(corrD){
               st.add(i);
               
               for(int j = 0; j < nrFacilities; j++){
                   for(int k = 0; k < nrFacilities; k++){
                      if(maxValNorm == -1){
                        maxValNorm = newDistance[j][k];
                      } else if(maxValNorm < newDistance[j][k]){
                        maxValNorm = newDistance[j][k];
                      }
                   }
               }
               
           } 
       }
       
       if(maxValNorm > 1.0){
        for(int i = 0; i < st.size(); i++){
           int indI = st.get(i);
           
            for(int j = 0; j < nrFacilities; j++){
               for(int k = 0; k < nrFacilities; k++){
                    if(indI == 0){
                        newDistance[j][k] /=  maxValNorm;
                    } else {
                        basicFlow[j][k] /= maxValNorm;
                    }
                }
           }
        }
       }
       return basicFlow;
    }
            
    private double[][] newDistance;  // in case the distance values are modified
    private double[][] corrValues;
    private double[][] basicFlow;
        
    public double[][] createBasicFlowMatrix(double[][] distance, CorrelationGroups corr){
        basicFlow = new double[nrFacilities][nrFacilities];
        newDistance = new double[nrFacilities][nrFacilities];

        for(int currF = 0; currF < nrFacilities; currF++) {
            for (int currE = currF; currE < nrFacilities; currE++) {
                if (currE == currF) {             
                    basicFlow[currE][currF] = 0.0;
                } else {
                    double[] inputs = new double[1];
                    inputs[0] = distance[currF][currE];
                    if(corr!=null){
                        double[] outputs = corr.correlation(inputs, distribution);

                        newDistance[currF][currE] = outputs[0];
                        newDistance[currE][currF] = newDistance[currF][currE];
                        
                        basicFlow[currE][currF] = outputs[1];
                        basicFlow[currF][currE] = basicFlow[currE][currF];
                    } else {
                        
                    }
                }
            }
       }
        
       return basicFlow;
    }
    
    public double[][] getNewDistance(){
        return newDistance;
    }
    
    public int[][] accomodateBasicMatrix(double[][] f){
        for(int currF = 0; currF < nrFacilities; currF++) {
            for (int currE = currF; currE < nrFacilities; currE++) {
                if (currE == currF) {
                    flow[currE][currF] = 0;
                } else {
                    flow[currE][currF] = distribution.generateFrom(f[currE][currF]);
                    flow[currF][currE] = flow[currE][currF];
                }
            }
        }
       return flow;
    }

    public boolean setElement(int[] index, int value){
       flow[index[0]][index[1]] = value;
       return true;
    }

    public int getElement(int[] index){
       return flow[index[0]][index[1]];
    }

    @Override public FlowMatrixWithDistr clone(){
        FlowMatrixWithDistr thisFlow = new FlowMatrixWithDistr(nrFacilities,distribution);
        return thisFlow;
    }

    public void swapsInFlow(int i, int j, int[][] flow){
       for(int k = 0; k < flow.length; k++){
            int temp = flow[j][k];
            flow[j][k] = flow[i][k];
            flow[i][k] = temp;
        }

        for(int k = 0; k < flow.length; k++){
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
       sb.append("dimension ").append("\n");

       for(int i = 0; i < flow.length; i++){
           for(int j = 0; j < flow[0].length; j++){
                sb.append(flow[i][j]).append(" ");
           }
           sb.append("\n");
       }
       sb.append("\n");
       return sb.toString();
    }

    public int getNrFacilities(){
        return nrFacilities;
    }

    public int getNrObjectives(){
        return 1;
    }

    public int[][] getFlow(){
        return flow;
    }

    public void setFlow(int[][] f){
        flow  = f;
    }

    public void setOneFlow(int[][] f){
        for(int i = 0; i < f.length; i++){
            for(int j = 0; j < f[0].length; j++){
               flow[i][j]  = f[i][j];
            }
        }
    }

    public DiscreteDistribution getDistribution(){
        return distribution;
    }

    public void writeQAP(FlowMatrixWithDistr flow, String nameF){
        int[][] currFlow = flow.getFlow();
        try{
            File myFile = new File(nameF);
            FileWriter fileStream = new FileWriter(myFile,true);
            myFile.createNewFile();
            fileStream = new FileWriter(myFile);
            BufferedWriter fDistI = new BufferedWriter(fileStream);


            //write the header
            String line = "facilities = " + String.valueOf(nrFacilities) + "  max flows = " + String.valueOf(flow.maxFlow) ;
            fDistI.write(line + " \n ");


            //write the flow - first dimension
            for(int currE = 0; currE < nrFacilities; currE++) {
                for(int currF = 0; currF < nrFacilities; currF++) {
                    if (currF < nrFacilities - 1) {
                        fDistI.write(currFlow[currE][currF] + "\t");
                    } else {
                        fDistI.write(currFlow[currE][currF] + "\n");
                    }
                }
            }
            fDistI.write("\n");

            //write the flow - second dimension
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

            fDistI.flush();
            fDistI.close();
        } catch (Exception e){
			System.out.println("Write not possible:"+ nameF);
			e.printStackTrace(System.out);
	}
    }

 }
