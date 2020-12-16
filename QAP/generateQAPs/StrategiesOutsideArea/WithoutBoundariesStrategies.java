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

package QAP.generateQAPs.StrategiesOutsideArea;

import QAP.generateQAPs.Correlation.GenerateRanCorrVar;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Distributions.WithHoles.DiscreteDistributionWithHoles;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import java.util.*;

public class WithoutBoundariesStrategies implements StrategiesOutside{
    
    private Stack<Integer> freeSockets; 
    private double percentLow; 
    private double[] percentZeros;
    
    private double[][] correlation;
    
    private int[][] distanceMatrix;
    private int[][] flowMatrix;
    
    public WithoutBoundariesStrategies(double[][] corr, int[] tempI){ 
        correlation = corr;
    }

    private int countDown; 
    //private double remainingLow;
    private double newPercentLow;
    private int lowNumber;
    private int[] countZ1;
    
    private Random r = new Random();
    public void getPercents(){
        ///////////////////////////////////
        //solutions that need to be filled in
        // indexI -> indexes of zeros 
        // how much from these solutions are 0s ?
        countDown = (int)Math.floor(freeSockets.size() *percentLow*percentZeros[0]);
        double remainingLow = Math.floor(percentLow * freeSockets.size() ) - countDown;
        newPercentLow = remainingLow/(freeSockets.size() - countDown);
        lowNumber = (int)Math.floor(freeSockets.size()*newPercentLow);
        
        ////////////////////
        // flow calculations
        countZ1 = new int[percentZeros.length - 1];
        for(int i = 0; i < percentZeros.length - 1; i++){
                countZ1[i] = (int) Math.floor(freeSockets.size()*percentLow* percentZeros[i+1]);
        }
    }
    
    @Override
    public int[] getCountZ(){
        return countZ1;
    }
    
    @Override
    public int getLow(){
        return (int)Math.floor(percentLow * freeSockets.size());
    }
    
    @Override
    public void setParameters(Stack<Integer> freeS, double percentL, double[] percentZ){
        freeSockets = freeS;
        percentLow = percentL;
        percentZeros = percentZ;
        
        getPercents();
    }
    
    private DistanceMatrixWithDistr newDistance;
    private DiscreteDistributionWithHoles fillDistance;
    private FlowMatrixWithDistr newFlow;
    private DiscreteDistributionWithHoles fillFlow;
    
    @Override
    public void arrangeDistance(DistanceMatrixWithDistr newD, DiscreteDistributionWithHoles fillD){
        newDistance = newD;

        fillDistance = fillD;
        fillDistance.reset();
        fillDistance.setPercentDown(newPercentLow);
    }

    @Override
    public void arrangeFLow(FlowMatrixWithDistr newF, DiscreteDistributionWithHoles fillF){
        newFlow = newF;
        fillFlow = fillF;
        GenerateRanCorrVar corrDistr = new GenerateRanCorrVar(correlation); //, typeDistr);
        
        /////////////////////////////
        //randomly generate basic numbers and correlated them
        int initSize = freeSockets.size();
        double[][] tempValue = new double[2][initSize];
        for(int i = 0; i < initSize; i++){
            double[] tempValueI = new double[2];
            boolean check = false;
            tempValueI[0] = fillDistance.generateBasic();
            for(int j = 0; j < tempValueI.length-1; j++){
                tempValueI[j + 1] = fillFlow.generateBasic();
            }
            
            while(!check){
                //correlate these values
                tempValueI = corrDistr.generateNumbers(tempValueI);
            
                check = newDistance.getDistribution().inLimits(tempValueI[0]);
                if(!check){
                     tempValueI = newDistance.getDistribution().checkLimits(0, tempValueI);
                     check = false;
                     continue;
                }
                        
                for(int ti = 0; ti < tempValueI.length - 1; ti++){
                      check = newFlow.getDistribution().inLimits(tempValueI[ti+1]);
                      if(!check){
                           tempValueI = newFlow.getDistribution().checkLimits(ti+1, tempValueI);
                           check = false;
                           break;
                      }
                } 
                
           }
                    
            for(int j = 0; j < tempValueI.length; j++){
                tempValue[j][i] = tempValueI[j];
            }
        }
        
        /////////////////////////////////
        //sort the distance matrix and split in low and high
        double[] temporar = new double[initSize];
        temporar= Arrays.copyOf(tempValue[0], initSize);
        Arrays.sort(temporar);
        double splitValueZero = temporar[countDown];
        double splitValueLow = temporar[lowNumber + countDown];
        double[] splitFlows = new double[1];
        for(int i = 0; i < 1; i++){
            splitFlows[i] = temporar[initSize - countZ1[i] -1];
        }
        
        distanceMatrix = newDistance.getDistance();
        flowMatrix = newFlow.getFlow();
                
        for(int i = 0; i < initSize; i++){
            int[] index = new int[2];
            int tempI = freeSockets.get(i);
            index[0] = tempI / 1000;
            index[1] = tempI % 1000;
              
            int value = 0;
            if(tempValue[0][i] < splitValueZero){  
                value = 0;
            } else if(tempValue[0][i] < splitValueLow){
               value = fillDistance.setInLowBounds(tempValue[0][i]); 
            } else {
                value = fillDistance.setInHighBounds(tempValue[0][i]);
            }
            
            distanceMatrix[index[0]][index[1]] = value;
            distanceMatrix[index[1]][index[0]] = value;
                
            for(int j = 0; j < tempValue.length - 1; j++){
                int valueI = 0;
                if(tempValue[0][i] < splitValueLow){    
                    valueI = fillFlow.setInHighBounds(tempValue[j+1][i]);
                } else if(tempValue[0][i] < splitFlows[j]){
                    valueI = fillFlow.setInLowBounds(tempValue[j+1][i]);
                } else {
                    valueI = 0;
                }
                
                flowMatrix[index[0]][index[1]] = valueI;
                flowMatrix[index[1]][index[0]] = valueI;
            }
            
        }

        newDistance.setDistance(distanceMatrix);
        newFlow.setFlow(flowMatrix);
    }
}
