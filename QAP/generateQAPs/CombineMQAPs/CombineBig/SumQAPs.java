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

package QAP.generateQAPs.CombineMQAPs.CombineBig;

import QAP.general.GenerateProblem.GenerateMQAPs.GenerateQAPs;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import QAP.generateQAPs.GenerateMQAPs;

public class SumQAPs {

    private FlowMatrixWithDistr newFlow;
    private DistanceMatrixWithDistr newDistance;
    private GenerateQAPs newQAP;

    private int nrFacilities;
    //private int nrObjectives;

    private int nrMatrices;
    private int nrCurrentM;
    
    private FlowMatrixWithDistr[] sumFlow;
    private DistanceMatrixWithDistr[] sumDistance;
    private int[] sumCoef;
    
    public SumQAPs(int nrM, int[] addCoef){
        nrMatrices = nrM;
        sumCoef = new int[nrM];
        sumFlow = new FlowMatrixWithDistr[nrMatrices];
        sumDistance = new DistanceMatrixWithDistr[nrMatrices];
        nrCurrentM = 0;
    }
    
    public SumQAPs(DistanceMatrixWithDistr[] sumD, FlowMatrixWithDistr[] sumF, int[] addCoef){
        nrMatrices = sumD.length;
        
        sumFlow = sumF;
        sumDistance = sumD;

        nrCurrentM = sumD.length;

        sumCoef = addCoef;
    }

    public int add(DistanceMatrixWithDistr sumD, FlowMatrixWithDistr sumF, int coef){
        if(nrCurrentM >= nrMatrices) {
            return -1;
        }
        sumDistance[nrCurrentM] = sumD;
        sumFlow[nrCurrentM] = sumF;
        sumCoef[nrCurrentM] = coef;
        nrCurrentM++;
        return 0;
    }

    int[][] tempDistance;
    int[][] tempFlow;
    public GenerateQAPs addInResult(){
       nrFacilities = sumDistance[0].getNrFacilities();
       int[][] distance = new int[nrFacilities][nrFacilities];
       int[][] flow = new int[nrFacilities][nrFacilities];

       for(int i = 0; i < nrCurrentM; i++){
            tempDistance = sumDistance[i].getDistance();
            tempFlow = sumFlow[i].getFlow();

            for(int indexI = 0; indexI < nrFacilities; indexI++){
                for(int indexJ = 0; indexJ < nrFacilities; indexJ++){
                    distance[indexI][indexJ] += sumCoef[i] * tempDistance[indexI][indexJ];

                    flow[indexI][indexJ] += sumCoef[i] * tempFlow[indexI][indexJ];
                }
            }
       }

       newDistance.setDistance(distance);
       newFlow.setFlow(flow);

       newQAP = new GenerateMQAPs(newDistance,newFlow);
       return newQAP;
    }

    public GenerateQAPs addInResult(DistanceMatrixWithDistr sumD, FlowMatrixWithDistr sumF, int coef){
       nrFacilities = sumDistance[0].getNrFacilities();
       int[][] distance = new int[nrFacilities][nrFacilities];
       int[][] flow = new int[nrFacilities][nrFacilities];

       if(nrCurrentM >= nrMatrices){
            return null;
       }

       tempDistance = sumD.getDistance();
       tempFlow = sumF.getFlow();

       for(int indexI = 0; indexI < nrFacilities; indexI++){
             for(int indexJ = 0; indexJ < nrFacilities; indexJ++){
                 distance[indexI][indexJ] += coef * tempDistance[indexI][indexJ];

                 flow[indexI][indexJ] += coef * tempFlow[indexI][indexJ];
             }
       }

       newDistance.setDistance(distance);
       newFlow.setFlow(flow);

       newQAP = new GenerateMQAPs(newDistance,newFlow);
       return newQAP;
   
    }
}
