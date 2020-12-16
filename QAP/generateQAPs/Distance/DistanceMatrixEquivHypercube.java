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
public class DistanceMatrixEquivHypercube extends DistanceMatrixWithDistr{

    private int[][] distance;
    private static Random r = new Random();
    private int nrFacilities; //

    private double[][] pointsPlane;
    private int maxDistance;
    private int[] maxCoord;

    private PointsInPlane distrPoints;
    private DiscreteDistribution distribution;
    //private int typeDistr; // 0 - unif random; 1 - normal distr; 2 - determ

    private int dimCube;
    private int[][] neighbourhoods;
    private Stack<Integer> nrNeigh[];

    public DistanceMatrixEquivHypercube(int nrF, int dim, PointsInPlane distr, DiscreteDistribution dis){
        maxCoord = new int[2];

        distance = new int[nrF][nrF];
        nrFacilities = nrF;

        pointsPlane = new double[2][nrF];
        distrPoints = distr;

        neighbourhoods = new int[nrFacilities][nrFacilities];

        dimCube = dim;
        nrNeigh = new Stack[nrFacilities];
        for(int j = 0; j < nrFacilities; j++) {
            nrNeigh[j] = new Stack<Integer>();
        }

        distribution = dis;
    }

    //create the distance matrix
    @Override public int[][] generateDistanceMatrix(int[][] distance){
        double tempR;
        pointsPlane = distrPoints.generatePoints();
        int neighR;
        double[] tempD = new double[2];
        for(int currF = 0; currF < nrFacilities; currF++){
            //1.0 decide which one are the neighbours
            while(nrNeigh[currF].size() < dimCube){
                //chose at random one indiv to be the neigh
                neighR =r.nextInt(nrFacilities);
                while(nrNeigh[currF].contains(neighR) || nrNeigh[neighR].contains(currF) || neighR == currF || nrNeigh[neighR].size() >= dimCube){
                    neighR =r.nextInt(nrFacilities);
                }
                //insert the neighbour
                nrNeigh[currF].add(neighR);
                nrNeigh[neighR].add(currF);
            }
        }

        //2. generate the neighMatrix
        for(int currF = 0; currF < nrFacilities; currF++){
            for(int n = 0; n < dimCube; n++){
                    neighbourhoods[currF][nrNeigh[currF].get(n)] = 1;
                    neighbourhoods[nrNeigh[currF].get(n)][currF] = 1;
            }
        }

        for(int currF = 0; currF < this.nrFacilities; currF++){
                for(int d = 0; d < 2; d++){
                //generate a cycle with center in 0 and radius or 1
                if(r.nextDouble() < 0.5){
                    //negative value;
                    tempD[d] = -r.nextDouble();
                } else {
                    tempD[d] = r.nextDouble();
                }

            }
        }
        //compute the distance between each two points
        maxDistance = 0;
        maxCoord[0] = -1;
        maxCoord[1] = -1;
        for(int currF = 0; currF < nrFacilities; currF++) {
                for (int currE = currF; currE < nrFacilities; currE++) {
                    if (currE == currF) {
                        this.distance[currE][currF] = 0;
                    } else {
                        tempR = 0;
                        for (int d = 0; d < 2; d++) {
                            tempR += Math.pow(pointsPlane[d][currE] - pointsPlane[d][currF], 2);
                        }
                        if(neighbourhoods[currE][currF] == 1){
                            distance[currE][currF] = (int) Math.sqrt(tempR);
                            distance[currF][currE] = distance[currE][currF];
                            if (maxDistance < distance[currE][currF]) {
                                maxDistance = distance[currE][currF];
                                maxCoord[0] = currE;
                                maxCoord[1] = currF;
                            }
                        }else {
                        distance[currE][currF] = distribution.generate();
                        distance[currF][currE] = distance[currE][currF];
                    }
                    }
                }
            }

      return distance;
    }

}
