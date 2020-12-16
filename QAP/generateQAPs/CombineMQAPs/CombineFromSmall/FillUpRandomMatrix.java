/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package QAP.generateQAPs.CombineMQAPs.CombineFromSmall;

import QAP.general.GenerateProblem.GenerateMQAPs.GenerateQAPs;
import QAP.general.GenerateProblem.GenerateProblems;
import QAP.generateQAPs.Correlation.CorrelationInterface;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Distributions.*;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import QAP.generateQAPs.GenerateMQAPs;
/**
 *
 * @author madalina
 */
public class FillUpRandomMatrix{

    private FlowMatrixWithDistr newFlow;
    private DistanceMatrixWithDistr newDistance;
    private GenerateQAPs newQAP;

    private DiscreteDistribution fillFlow;
    private DiscreteDistribution fillDistance;

    private int nrFacilities;
    private int nrObjectives = 1;

    private int nrMatrices;

    private CorrelationInterface corr;
    public FillUpRandomMatrix(DiscreteDistribution fillD, DiscreteDistribution fillF, CorrelationInterface c){
        fillFlow = fillF;
        fillDistance = fillD;
        
        corr = c;
    }

    public FillUpRandomMatrix(){}


    public void setNrFacilities(int nrF){
        nrFacilities = nrF;
    }

    public void setNrObjectives(int nrO){
        nrObjectives = 1;
    }

    public void setNrMatrices(int nrM){
        nrMatrices = nrM;
    }

    private int[] index;
    private int[] indexF;
    private int[] index1;
    private int[] indexF1;
    private int minDist, maxDist;
    private int minFlow, maxFlow;
    private int[][] allocatedPos;
    private int[] limitsDist;
    private int[] limitsFlow;
    public GenerateQAPs combine(GenerateProblems[] listP, int[][] tableOver){
       newFlow = new FlowMatrixWithDistr(nrFacilities,fillFlow);
       newDistance = new DistanceMatrixWithDistr(nrFacilities,fillDistance);
       allocatedPos = new int[nrFacilities][nrFacilities];

       limitsDist = new int[4];
       limitsFlow = new int[4];
       //minFlow = new int[nrObjectives];
       //maxFlow = new int[nrObjectives];

       index = new int[2];
       indexF = new int[2];
       index1 = new int[2];
       indexF1 = new int[2];

       combineSmall(listP,tableOver);
       setLimits();
       fillUpMatrix();
 
       newQAP = new GenerateMQAPs(newDistance,newFlow);
       return newQAP;
     }

    public void combineSmall(GenerateProblems[] listP, int[][] tableOver){

        int nrFeatures = tableOver[0].length;
        for(int m = 0; m < nrMatrices; m++){
            for(int i = 0; i < nrFeatures; i++){
                index[0] = tableOver[m][i];
                index1[1] = tableOver[m][i];

                allocatedPos[index[0]][index[0]] = 1;

                for(int j = i+1; j < nrFeatures; j++){
                    //
                    index[1] = tableOver[m][j];
                    index1[0] = tableOver[m][j];

                    allocatedPos[index[0]][index[1]] = 1;

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
                      }
                  //}
           }
        }
    }
        
    

    public void setLimits(){
       //compute the maximum and the minimum
       boolean firstPos = true;
       for(int i = 0; i < nrFacilities; i++){
            for(int j = 0; j < nrFacilities; j++){
                if(allocatedPos[i][j] == 1){
                    index[0] = i;
                    index[1] = j;

                    if(firstPos){

                        minDist = newDistance.getElement(index);
                        maxDist = minDist;

                        //for(int k = 0; k < nrObjectives; k++){
                            //indexF[0] = k;
                            indexF[0] = i;
                            indexF[1] = j;

                            minFlow = newFlow.getElement(indexF);
                            maxFlow = minFlow;
                        //}

                        firstPos = false;
                    } else {
                        if(minDist > newDistance.getElement(index)){
                            minDist = newDistance.getElement(index);
                        }
                        if(maxDist < newDistance.getElement(index)){
                            maxDist = newDistance.getElement(index);
                        }

                        //for(int k = 0; k < nrObjectives; k++){
                            //indexF[0] = k;
                            indexF[0] = i;
                            indexF[1] = j;

                            if(minFlow > newFlow.getElement(indexF)){
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
       if(minDist < limitsDist[1]){
            limitsDist[1] = minDist;
        }
       if(maxDist > limitsDist[2]){
            limitsDist[2] = maxDist;
       }
       if(limitsDist[2] > limitsDist[3]){
           limitsDist[3] = limitsDist[2] + 100;
       }
       fillDistance.setBounds(limitsDist);
       //for(int k = 0; k < nrObjectives; k++){
            limitsFlow = fillFlow.getBounds();
            limitsFlow[1] = minFlow;
            limitsFlow[2] = maxFlow;
            fillFlow.setBounds(limitsFlow);
       // }
    }

    public void fillUpMatrix(){

        for(int i = 0; i < nrFacilities; i++){
            for(int j = 0; j < nrFacilities; j++){
                if(allocatedPos[i][j] == 1){
                    continue;
                }
                //
                index[0] = i;
                index[1] = j;

                if(i < j){
                    newDistance.setElement(index, fillDistance.generate());
                } else {
                    newDistance.setElement(index, newDistance.getDistance()[j][i]);
                }
            }
        }

        for(int i = 0; i < nrFacilities; i++){
            for(int j = 0; j < nrFacilities; j++){
                if(allocatedPos[i][j] == 1){
                    continue;
                }

                //for(int k = 0; k < nrObjectives; k++){
                    //indexF[0] = k;
                    indexF[0] = i;

                    indexF[1] = j;

                    if(i < j){
                        newFlow.setElement(indexF, fillFlow.generate());
                    } else {
                        newFlow.setElement(indexF, newFlow.getFlow()[j][i]);
                      }
                  //}
             }
           }
    }
}
