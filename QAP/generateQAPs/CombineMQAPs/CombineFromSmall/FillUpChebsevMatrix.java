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

package QAP.generateQAPs.CombineMQAPs.CombineFromSmall;

import QAP.general.GenerateProblem.GenerateMQAPs.GenerateQAPs;
import QAP.general.GenerateProblem.GenerateProblems;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Distributions.WithHoles.DiscreteDistributionWithHoles;
import QAP.generateQAPs.Flows.FlowMatrixChebsev;
import QAP.generateQAPs.GenerateMQAPs;
import QAP.generateQAPs.StrategiesOutsideArea.StrategiesOutside;
import java.util.*;

public class FillUpChebsevMatrix extends FillUpRandomMatrix{

    private FlowMatrixChebsev newFlow;
    private DistanceMatrixWithDistr newDistance;
    private GenerateQAPs newQAP;

    private DiscreteDistributionWithHoles fillFlow;
    private DiscreteDistributionWithHoles fillDistance;

    private int nrFacilities;

    private int nrMatrices;

    private double percentLow;
    private double[] percentZeros;
    
    private static java.util.Random r = new java.util.Random();
    
    private StrategiesOutside strategyFill;
    
    public FillUpChebsevMatrix(StrategiesOutside strategyF, DiscreteDistributionWithHoles fillD, DiscreteDistributionWithHoles fillF, double l, double[] z){ //, CorrelationGroups c){
        fillFlow = fillF;
        fillDistance = fillD;
        percentLow = l;
        percentZeros = z;
        strategyFill = strategyF;
    }

    public FillUpChebsevMatrix(){}


    @Override
    public void setNrFacilities(int nrF){
        nrFacilities = nrF;
    }

    @Override
    public void setNrObjectives(int nrO){
        //nrObjectives = 1;
    }

    @Override
    public void setNrMatrices(int nrM){
        nrMatrices = nrM;
    }

    private int[] index;
    private int[] index1;
    private int[] indexF;
    private int[] indexF1;
    private int minDist, maxDist;
    private int minFlow, maxFlow;
    private int[][] allocatedPos;
    private int[] limitsDist;
    private int[] limitsFlow;
    @Override public GenerateQAPs combine(GenerateProblems[] listP, int[][] tableOver){
       newFlow = new FlowMatrixChebsev(nrFacilities,fillFlow);
       newDistance = new DistanceMatrixWithDistr(nrFacilities,fillDistance);
       allocatedPos = new int[nrFacilities][nrFacilities];

       limitsDist = new int[4];
       limitsFlow = new int[4];

       index = new int[2];
       indexF = new int[2];
       index1 = new int[2];
       indexF1 = new int[2];

       combineSmall(listP,tableOver);
       
       // combine with the NEW QAP paper
       setLimits();
       fillUpMatrix();
 
       newQAP = new GenerateMQAPs(newDistance,newFlow);
       return newQAP;
     }

     private int countAlloc;
     @Override public void combineSmall(GenerateProblems[] listP, int[][] tableOver){

        int nrFeatures = tableOver[0].length;
        for(int m = 0; m < nrMatrices; m++){
            for(int i = 0; i < nrFeatures; i++){
                index[0] = tableOver[m][i];
                index1[1] = tableOver[m][i];

                allocatedPos[index[0]][index[0]] = 1;
                countAlloc++;
                for(int j = i+1; j < nrFeatures; j++){
                    //
                    index[1] = tableOver[m][j];
                    index1[0] = tableOver[m][j];

                    allocatedPos[index[0]][index[1]] = 1;
                    allocatedPos[index1[0]][index1[1]] = 1;

                    newDistance.setElement(index, newDistance.getElement(index)+ ((GenerateQAPs)listP[m]).getDistance().getDistance()[i][j]);
                    newDistance.setElement(index1, newDistance.getElement(index));

                    //for(int k = 0; k < nrObjectives; k++){
                        //indexF[0] = k;
                        indexF[0] =  tableOver[m][i];
                        indexF[1] =  tableOver[m][j];

                        //indexF1[0] = k;
                        indexF1[0] =  tableOver[m][j];
                        indexF1[1] =  tableOver[m][i];

                        newFlow.setElement(indexF, newFlow.getElement(indexF) +((GenerateQAPs)listP[m]).getFlow().getFlow()[i][j]);
                        newFlow.setElement(indexF1, newFlow.getElement(indexF));
                      //}
                  }
                }
              }
        //System.out.println("allocated " + countAlloc);
    }


    @Override
    public void setLimits(){
       //compute the maximum and the minimum
       boolean firstPos = true;
       for(int i = 0; i < nrFacilities; i++){
            for(int j = 0; j < nrFacilities; j++){
                if(allocatedPos[i][j] == 1){
                    index[0] = i;
                    index[1] = j;

                    if(firstPos){

                        if(newDistance.getElement(index) != 0){
                            minDist = newDistance.getElement(index);
                        } else {
                            minDist = Integer.MAX_VALUE;
                        }
                        maxDist = newDistance.getElement(index);

                        //for(int k = 0; k < nrObjectives; k++){
                            //indexF[0] = k;
                            indexF[0] = i;
                            indexF[1] = j;

                            if(newFlow.getElement(indexF) != 0){
                                minFlow = newFlow.getElement(indexF);
                            } else {
                                minFlow = Integer.MAX_VALUE;
                            }
                            maxFlow = newFlow.getElement(indexF);
                        //}

                        firstPos = false;
                    } else {
                        if(minDist > newDistance.getElement(index) && newDistance.getElement(index) != 0){
                            minDist = newDistance.getElement(index);
                        }
                        if(maxDist < newDistance.getElement(index)){
                            maxDist = newDistance.getElement(index);
                        }

                        //for(int k = 0; k < nrObjectives; k++){
                            //indexF[0] = k;
                            indexF[0] = i;
                            indexF[1] = j;

                            if(minFlow > newFlow.getElement(indexF) && newFlow.getElement(indexF) != 0){
                                minFlow = newFlow.getElement(indexF);
                            }
                            if(maxFlow < newFlow.getElement(indexF)){
                                maxFlow = newFlow.getElement(indexF);
                            }
                        //}
                    }
                }
            }
        }
       // fill the non-assigned positions with the elements outside the min, max range
       limitsDist = fillDistance.getBounds();
       boolean flag = false;
       if(minDist < limitsDist[1]){
            limitsDist[1] = minDist-1;
            flag = true;
        }
       if(maxDist > limitsDist[2]){
            limitsDist[2] = maxDist;
            flag = true;
       }
       if(limitsDist[2] > limitsDist[3]){
           limitsDist[3] = limitsDist[2] + 100;
           flag = true;
       }
       if(flag){
           System.out.println(" Change limits " + limitsDist[0] + "\t" + limitsDist[1] + "\t" + limitsDist[2] + "\t" + limitsDist[3]);

            fillDistance.setBounds(limitsDist);
            //for(int k = 0; k < nrObjectives; k++){
                limitsFlow = fillFlow.getBounds();

                if(minDist < limitsFlow[1]){
                    limitsFlow[1] = minDist-1;
                    flag = true;
                }

                if(maxDist > limitsFlow[2]){
                    limitsFlow[2] = maxDist;
                    flag = true;
                }

                if(limitsFlow[2] > limitsFlow[3]){
                    limitsFlow[3] = limitsFlow[2] + 100;
                    flag = true;
                }
            
                fillFlow.setBounds(limitsFlow);
           //}
        }
    }

    //private int sol;
    //private int countDown;
    private Stack<Integer> freeSockets;
    @Override
    public void fillUpMatrix(){
        freeSockets = new Stack<Integer>();
        ///////////////////////////
        // 0. preliminaries; nr of zeros; nr of non-zeros
        int count = 0;
        for(int i = 0; i < nrFacilities; i++){
            for(int j = i+1; j < nrFacilities; j++){
                if(allocatedPos[i][j] == 1){
                    count++;
                } else {
                    // table[0] * 1000 + table[1]
                    Integer tempI = 1000 * i + j;
                    freeSockets.add(tempI);
                }
            }
        }

        strategyFill.setParameters(freeSockets, percentLow, percentZeros);
        strategyFill.arrangeDistance(newDistance, fillDistance);
        strategyFill.arrangeFLow(newFlow, fillFlow);


    }
}
