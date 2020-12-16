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
public class CorrelationFlows implements CorrelationGroups {   
    
    double[][] corrNum;
    CorrelationInterface corrInterf;
    
    public CorrelationFlows(double[][] corr, CorrelationInterface c){
        corrNum = corr;
        corrInterf = c;
    }

    @Override
    public double[] correlation(double[] inputs, double[][] correlation, DiscreteDistribution distr){
        double[] output = new double[correlation.length-1];
        
        output[0] = corrInterf.correlation(inputs[0], correlation[0][1], distr);
        //for(int i = 1; i < output.length; i++){
            if(corrInterf.typeCorrelation() == 0){
                if(correlation[0][1] > 0){
                    output[1] = corrInterf.correlation(output[0], correlation[0][1], distr);
                } else {
                    output[1] = 1.0 - corrInterf.correlation(output[0], -correlation[0][1], distr);                
                }
            } else {
                    output[1] = corrInterf.correlation(output[0], correlation[0][1], distr);
           }
            
        //}
        return output;
        
    }
    
    public double[] correlation(double[] inputs, DiscreteDistribution distr){
        return correlation(inputs,corrNum,distr);

    }

    @Override
    public double[] normValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[][] getCorrMatrix() {
       return corrNum;
    }

    @Override
    public double[][] getTransfMatrix() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
