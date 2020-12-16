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

package QAP.generateQAPs.Correlation;

import QAP.generateQAPs.Distributions.*;

/**
 *
 * @author madalina
 */
public class Correlation2Uniform implements CorrelationInterface{
    
    private static java.util.Random r = new java.util.Random();
    //double[] limits;
    double corrNum;
    
    public Correlation2Uniform(double corr){
        corrNum = corr;
    }
    
    @Override
    public double correlation(double inputs, DiscreteDistribution distr){
        return correlation(inputs,corrNum,distr);
    }
    
    @Override
    public double correlation(double inputs, double correlation, DiscreteDistribution distr){
       double tempP, tempR, tempT, tempW;
       while (true) {
            tempP = distr.generateBasic();
            tempR = tempP - inputs;
            if (tempR < 0) {
                tempR *= -1;
            }
            tempW = Math.exp(-(Math.pow(tempR, 2)) / (2.0 * Math.pow(1.0 - Math.pow(correlation, 0.5), 2))) / ((1.0 - Math.pow(correlation, 0.5)) * 2.506);
            
            tempT = distr.generateBasic() / ((1.0 - Math.pow(correlation, 0.5)) * 2.506);
            
            if (tempW >= tempT) {
                 break;
            }
        }

        double output = tempP;
        return output;
    }
    
    public int typeCorrelation(){
        return 0;
    }
}
