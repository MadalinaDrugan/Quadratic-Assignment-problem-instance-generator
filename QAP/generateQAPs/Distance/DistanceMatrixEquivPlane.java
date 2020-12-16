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

import java.util.*;

public class DistanceMatrixEquivPlane extends DistanceMatrixWithDistr{

    private int[][] distance;
    private static Random r = new Random();
    private int nrFacilities; //

    private double[][] pointsPlane;
    private int maxDistance;
    private int[] maxCoord;

    private PointsInPlane distrPoints;
    
    public DistanceMatrixEquivPlane(int nrF, PointsInPlane distr){
        maxCoord = new int[2];

        distance = new int[nrF][nrF];
        nrFacilities = nrF;

        pointsPlane = new double[2][nrF];
        distrPoints = distr;
    }

    //create the distance matrix
    @Override public int[][] generateDistanceMatrix(int[][] distance){
        double tempR;
        pointsPlane = distrPoints.generatePoints();
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
                        distance[currE][currF] = (int) Math.sqrt(tempR);
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

}
