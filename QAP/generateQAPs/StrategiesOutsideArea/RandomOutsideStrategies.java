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

import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Distributions.WithHoles.DiscreteDistributionWithHoles;
import QAP.generateQAPs.Flows.FlowMatrixChebsev;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import java.util.*;

public class RandomOutsideStrategies implements StrategiesOutside{
    
    private Stack<Integer> freeSockets; 
    private Stack<Neighbours> freeNeighbours;
    private double percentLow; 
    private double[] percentZeros;
    
    public void RandomOutsideStrategies(){ 
    }

    private int countDown; 
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
    private int countL = 0; 
    private int countZ = 0; 
    private int countH = 0; 

    public void arrangeDistance(DistanceMatrixWithDistr newD, DiscreteDistributionWithHoles fillDistance){
        newDistance = newD;
        ////////////////////////////////////
        // 1. filling up the distance procedure
        //  indexes of non-zeros
        fillDistance.reset();
        fillDistance.setPercentDown(newPercentLow);
        int initSize = freeSockets.size();
        
        ///////////////////////
        // set zeros
        NeighboursStrategies neigh = new NeighboursStrategies();
        int[] clusters = new int[countDown];
        for(int i = 0; i < countDown; i++){
            clusters[i] = 1;
        }
        neigh.setParameters(clusters);
        Neighbours.size = 0;
        Stack<int[]> tempS = neigh.arrangeList(freeSockets);
        freeNeighbours = neigh.getNeighList();
        // check
        if(initSize != freeNeighbours.size() + tempS.size()){
            System.err.println("NOt correct 0: initial " + initSize + " now "+ freeNeighbours.size() + " allocated" + tempS.size());
        }
        for(int i = 0; i < tempS.size(); i++){
            newDistance.setElement(tempS.get(i),0);
            
            int[] index = Arrays.copyOf(tempS.get(i), tempS.get(i).length);
            int temp = index[0];
            index[0] = index[1];
            index[1] = temp;
            newDistance.setElement(index,0);
            
            countZ++;
        }
        int initSize1 = freeNeighbours.size();
        
        /////////////////////
        // different; low 
        //neigh = new NeighboursStrategies();
        clusters = new int[lowNumber];
        for(int i = 0; i < lowNumber; i++){
            clusters[i] = 1;
        }
        neigh.setParameters(clusters);
        Neighbours.size = 0;
        tempS = neigh.arrange(freeNeighbours);
        freeNeighbours = neigh.getNeighList();
        // check
        if(initSize1 != freeNeighbours.size() + tempS.size()){
            System.err.println("NOt correct low: initial " + initSize + " now "+ freeNeighbours.size() + " allocated" + tempS.size());
        }
        for(int i = 0; i < tempS.size(); i++){
            newDistance.setElement(tempS.get(i),fillDistance.generateLow());
            
            int[] index = Arrays.copyOf(tempS.get(i), tempS.get(i).length);
            int temp = index[0];
            index[0] = index[1];
            index[1] = temp;
            newDistance.setElement(index,newDistance.getElement(tempS.get(i)));
            
            countL++;
        }

        ///////////////////////////
        // high; the rest
        //neigh = new NeighboursStrategies();
        if(initSize - countDown - lowNumber > 0){
            clusters = new int[initSize - countDown - lowNumber];
            for(int i = 0; i < initSize - countDown - lowNumber; i++){
                clusters[i] = 1;
            }
            neigh.setParameters(clusters);
            Neighbours.size = 0;
            tempS = neigh.arrange(freeNeighbours);
            freeNeighbours = neigh.getNeighList();
            // check
            if(clusters.length != tempS.size()){
                System.err.println("NOt correct: initial high" + initSize + " now "+tempS.size() + " size "+ freeNeighbours.size());
            }
            for(int i = 0; i < tempS.size(); i++){
                newDistance.setElement(tempS.get(i),fillDistance.generateHigh());

                int[] index = Arrays.copyOf(tempS.get(i), tempS.get(i).length);
                int temp = index[0];
                index[0] = index[1];
                index[1] = temp;
                newDistance.setElement(index,newDistance.getElement(tempS.get(i)));
            
                countH++;
            }
        }
        
        System.out.println("Allocated (" + countH + "+ "+ countL + "+ "+ countZ + ") = initial " + initSize);
    }

    @Override
    public void arrangeFLow(FlowMatrixWithDistr newFlow, DiscreteDistributionWithHoles fillFlow){
        ((FlowMatrixChebsev) newFlow).setDistanceMatrix(newDistance, freeSockets);
        ((FlowMatrixChebsev) newFlow).createFlowMatrices(newDistance,getCountZ(),freeSockets.size() - getLow());
    }
}
