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

package QAP.generateQAPs.Overlapping;

import QAP.general.GenerateProblem.GenerateMQAPs.OverlappingStrategy;
import java.util.*;

public class OverlappingDiscontinous implements OverlappingStrategy{

    private int nrFeatures;
    private int nrFeatureOverlap;
    private int nrMatrices;

    private int[][] overlapMatrix;
    private int sizeResult;

    private static java.util.Random r = new java.util.Random();

    public OverlappingDiscontinous(int nrF, int nrO, int nrM){
        nrFeatures = nrF;
        nrFeatureOverlap = nrO;
        nrMatrices = nrM;

        overlapMatrix = new int[nrMatrices][nrFeatures];
    }

    public OverlappingDiscontinous(){}

    private Stack<Integer> correspond;
    //private int[] countI;
    private Stack<Integer>[] overlap;
    private int[] countIndex;
    private int[] tempIndex;
    public int[][] compute(){

        correspond = new Stack<Integer>();
        //countI = new int[nrMatrices];
        overlap = new Stack[nrMatrices];
        for(int i = 0; i < nrMatrices; i++){
            overlap[i] = new Stack<Integer>();
        }
        countIndex = new int[sizeResult];

        while(correspond.size() < sizeResult){
            int tempI = (int)Math.floor(nrMatrices * r.nextDouble());
            while(overlap[tempI].size() >= nrFeatures){
                tempI = (int)Math.floor(nrMatrices * r.nextDouble());
            }
            //int[] tempF = new int[1];
            //tempF[0] = tempI;
            correspond.add(tempI);
            overlap[tempI].add(correspond.size()-1);
            //countI[tempI]++;
            countIndex[correspond.size()-1]++;
        }

        for(int i = 0; i < nrMatrices; i++){
            while(overlap[i].size() < nrFeatures){
                int tempI = (int)Math.floor(sizeResult * r.nextDouble());

                while(countIndex[tempI] >= nrFeatures/(nrFeatures - nrFeatureOverlap) || overlap[i].contains(tempI) || tempI == sizeResult){
                    tempI = (int)Math.floor(sizeResult * r.nextDouble());
                }
                overlap[i].add(tempI);
                countIndex[tempI]++;
            }
        }
        for(int i = 0; i < nrMatrices; i++){
            //
            tempIndex = new int[nrFeatures];
            for(int j = 0; j < nrFeatures; j++){
                tempIndex[j] = overlap[i].get(j);
            }
            Arrays.sort(tempIndex);

            //System.arraycopy(tempIndex, 0, overlapMatrix[i], 0, nrFeatures);
            for(int j = 0; j < tempIndex.length; j++){
                overlapMatrix[i][j] = tempIndex[j];
            }
         }
        //overlapMatrix
        for(int i = 0; i < overlapMatrix.length; i++){
            for(int j = 0; j < overlapMatrix[0].length; j++){
                if(overlapMatrix[i][j] == sizeResult){
                    System.out.print("oversize");
                }
            }
        }
        return overlapMatrix;
    }

    public int sizeResultMatrix(){
        sizeResult = nrFeatures + (nrMatrices - 1) * (nrFeatures - nrFeatureOverlap);
        return sizeResult;
    }

    public int nrOverMatrices(){
        return nrMatrices;
    }

    public int nrFeatureOverlap(){
        return nrFeatureOverlap;
    }
}
