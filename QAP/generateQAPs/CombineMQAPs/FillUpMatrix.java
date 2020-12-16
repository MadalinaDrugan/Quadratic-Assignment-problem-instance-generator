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

package QAP.generateQAPs.CombineMQAPs;

import QAP.general.GenerateProblem.GenerateMQAPs.GenerateQAPs;
import QAP.general.GenerateProblem.GenerateProblems;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Distributions.DiscreteDistribution;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import QAP.generateQAPs.GenerateMQAPs;


public class FillUpMatrix {

    private FlowMatrixWithDistr newFlow;
    private DistanceMatrixWithDistr newDistance;
    private GenerateQAPs newQAP;

    private DiscreteDistribution fillFlow;
    private DiscreteDistribution fillDistance;

    private int nrFacilities;

    private int nrMatrices;

    public FillUpMatrix(DiscreteDistribution fillD, DiscreteDistribution fillF){
        fillFlow = fillF;
        fillDistance = fillD;
    }

    public FillUpMatrix(){}


    public void setNrFacilities(int nrF){
        nrFacilities = nrF;
    }

    public void setNrObjectives(int nrO){
        //nrObjectives = nrO;
    }

    public void setNrMatrices(int nrM){
        nrMatrices = nrM;
    }

    private int[] index;
    private int[] indexF;
    public GenerateQAPs combine(GenerateProblems[] listP, int[][] tableOver){
       newFlow = new FlowMatrixWithDistr(nrFacilities,fillFlow);
       newDistance = new DistanceMatrixWithDistr(nrFacilities,fillDistance);

       index = new int[2];
       indexF = new int[2];

       for(int i = 0; i < nrFacilities; i++){
            for(int j = 0; j < nrFacilities; j++){

               boolean nonZero = false;

               //
               for(int m = 0; m < nrMatrices; m++){
                if(i < tableOver[1][m] && i >= tableOver[0][m] && j < tableOver[1][m] && j >= tableOver[0][m]){
                    index[0] = i;
                    index[1] = j;

                    newDistance.setElement(index, newDistance.getElement(index)+ ((GenerateQAPs)listP[m]).getDistance().getDistance()[i - tableOver[0][m]][j- tableOver[0][m]]);

                    indexF[0] = i;
                    indexF[1] = j;
                    newFlow.setElement(indexF, newFlow.getElement(indexF) +((GenerateQAPs)listP[m]).getFlow().getFlow()[i- tableOver[0][m]][j- tableOver[0][m]]);
                    nonZero = true;
                }
              }

              //
              if(!nonZero){
                  if(i < j){
                      index[0] = i;
                      index[1] = j;
                      newDistance.setElement(index, fillDistance.generate());

                      indexF[0] = i;
                      indexF[1] = j;
                      newFlow.setElement(indexF, fillFlow.generate());
                  } else {
                      index[0] = i;
                      index[1] = j;
                      newDistance.setElement(index, newDistance.getDistance()[j][i]);
                      indexF[0] = i;
                      indexF[1] = j;
                      newFlow.setElement(indexF, newFlow.getFlow()[j][i]);
                  }
             }
           }
        }

 
       newQAP = new GenerateMQAPs(newDistance,newFlow);
       return newQAP;
     }
}
