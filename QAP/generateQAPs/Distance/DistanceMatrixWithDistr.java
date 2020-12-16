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

package QAP.generateQAPs.Distance;

import QAP.generateQAPs.Distributions.DiscreteDistribution;
import java.util.*;
/**
 *
 * @author madalina
 */
public class DistanceMatrixWithDistr{

    private int nrFacilities; //
    private int[][] distance;

    private int maxDistance;
    private int[] maxCoord;

    private static Random r = new Random();

    private DiscreteDistribution distribution;

    public DistanceMatrixWithDistr(int nrF, DiscreteDistribution myDistr){
        maxCoord = new int[2];

        distance = new int[nrF][nrF];
        nrFacilities = nrF;
        distribution = myDistr;
    }

    public DistanceMatrixWithDistr(){
    }

    //create the distance matrix
    public int[][] generateDistanceMatrix(int[][] distance){
        //compute the distance between each two points
        maxDistance = 0;
        maxCoord[0] = -1;
        maxCoord[1] = -1;
        for(int currF = 0; currF < nrFacilities; currF++) {
                for (int currE = currF; currE < nrFacilities; currE++) {
                    if (currE == currF) {
                        distance[currE][currF] = 0;
                    } else {
                        distance[currE][currF] = distribution.generate();
                        distance[currF][currE] = distance[currE][currF];
                        if (maxDistance < distance[currE][currF]) {
                            maxDistance = distance[currE][currF];
                            maxCoord[0] = currE;
                            maxCoord[1] = currF;
                        }
                    }
                }
            }

      return distance;
    }

    public int[][] createDistanceMatrix(){
        return generateDistanceMatrix(distance);
    }

    public double[][] createBasicDistanceMatrix(){
        double[][] basicDistance = new double[nrFacilities][nrFacilities];
        for(int i = 0; i < nrFacilities; i++){
            for(int j = i; j < nrFacilities; j++){
                if(i != j){
                    basicDistance[i][j] = distribution.generateBasic();
                    basicDistance[j][i] = basicDistance[i][j];
                } else {
                    basicDistance[i][i] = 0.0;
                }
            }
        }
        return basicDistance;
    }
    
    public int[][] accomodateBasicMatrix(double[][] basicM){
               //compute the distance between each two points
        maxDistance = 0;
        maxCoord[0] = -1;
        maxCoord[1] = -1;
        for(int currF = 0; currF < nrFacilities; currF++) {
                for (int currE = currF; currE < nrFacilities; currE++) {
                    if (currE == currF) {
                        distance[currE][currF] = 0;
                    } else {
                        distance[currE][currF] = distribution.generateFrom(basicM[currE][currF]);
                        distance[currF][currE] = distance[currE][currF];
                        if (maxDistance < distance[currE][currF]) {
                            maxDistance = distance[currE][currF];
                            maxCoord[0] = currE;
                            maxCoord[1] = currF;
                        }
                    }
                }
            }

      return distance;
    }
    
    @Override public DistanceMatrixWithDistr clone(){
        DistanceMatrixWithDistr thisDist = new DistanceMatrixWithDistr(nrFacilities,distribution);
        return thisDist;
    }

    public boolean setElement(int[] index, int value){
       distance[index[0]][index[1]] = value;
       return true;
    }

    public int getElement(int[] index){
       return distance[index[0]][index[1]];
    }

    public void swapsInDistance(int i, int j, int[][] distance){
        //swaps rows
        for(int k = 0; k < distance.length; k++){
            int temp = distance[j][k];
            distance[i][k] = temp;
            distance[j][k] = distance[i][k];
        }

        //swaps columns
        for(int k = 0; k < distance.length; k++){
            int temp = distance[k][j];
            distance[k][i] = temp;
            distance[k][j] = distance[k][i];
        }
    }

    public void swapsInDistance(int i, int j){
        swapsInDistance(i, j, distance);
    }

    @Override public String toString(){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < distance.length; i++){
                    for(int j = 0; j < distance[0].length; j++){
                        sb.append(distance[i][j]).append(" ");
                    }
                    sb.append("\n");
            }
            //sb.append("Component individual");
            sb.append("\n");
            return sb.toString();
    }

    public int getNrFacilities(){
        return nrFacilities;
    }

    public int[][] getDistance(){
        return distance;
    }

    public void setDistance(int[][] dist){
        distance = dist;
    }

    public int[] getMaxCoord(){
        return maxCoord;
    }

    public DiscreteDistribution getDistribution(){
        return distribution;
    }
}
