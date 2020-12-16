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

public class Overlapping implements OverlappingStrategy{

    private int nrFeatures;
    private int nrFeatureOverlap;
    private int nrMatrices;

    private int[][] overlapMatrix;
    private int sizeResult;

    public Overlapping(int nrF, int nrO, int nrM){
        nrFeatures = nrF;
        nrFeatureOverlap = nrO;
        nrMatrices = nrM;

        overlapMatrix = new int[nrMatrices][nrFeatures];
    }

    public Overlapping(){}
    
    public int[][] compute(){
        int count = 0;
        for(int i = 0; i < nrMatrices; i++){
            for(int j = 0; j < nrFeatures; j++){
                overlapMatrix[i][j] = count+j;
            }
            count += nrFeatures - nrFeatureOverlap;
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
