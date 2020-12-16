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

import java.util.Random;
/**
 *
 * @author madalina
 */
public class PointsInPlane {
     private double[][] pointsPlane;
     private int nrFacilities;
     private int min;
     private int max;
     private double[][] corrPoints;

     private static Random r = new Random();

     public PointsInPlane(int nrF, int minM, int maxM, double[][] corr){
            nrFacilities = nrF;
            min = minM;
            max = maxM;
            corrPoints = corr;
        }
        
    public double[][] generatePoints(){
        double tempR = 0, tempT = 0;
        double[] tempD = new double[2];
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

                tempT = 0;
            for(int d = 0; d < 2; d++) {
                    tempT += Math.pow(tempD[d], 2);
                }
            tempT = Math.sqrt(tempT);
            tempT = (max - min) * tempT + min;

            tempR = tempD[1]/tempD[0];

            pointsPlane[0][currF] = Math.signum(tempD[0]) * tempT/Math.sqrt(Math.pow(tempR,2)+1);
            pointsPlane[1][currF] = Math.signum(tempD[1]) * tempT * Math.abs(tempR)/Math.sqrt(Math.pow(tempR,2)+1);
            while(true){
                 tempT = r.nextDouble();
                 tempR = tempT - pointsPlane[1][currF];
                 if(tempR < 0) {
                        tempR *= -1;
                    }
                 tempR = Math.exp(-(Math.pow(tempR, 2))/(2.0*Math.pow(1.0-Math.pow(corrPoints[0][0],0.5),2)))/(1.0-Math.pow(corrPoints[0][0],0.5))*2.506;

                 if(tempR < r.nextDouble()*1.0/(1.0-Math.pow(corrPoints[0][0],0.5))*2.506) {
                         break;
                    }
            }

            pointsPlane[1][currF] = (int)(tempT*pointsPlane[1][currF]);
       }
       return pointsPlane;
    }
}
