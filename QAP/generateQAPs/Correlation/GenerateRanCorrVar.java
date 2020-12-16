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

import QAP.Util.*;
import QAP.generateQAPs.Distributions.*;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.random.MersenneTwister;

public class GenerateRanCorrVar implements CorrelationGroups{
    
    private double[] genRanNumbers;
    private int nrNumbers;
    
    private double[][] corrMatrix;
    private double[][] transMatrix;
    
    private MersenneTwister r = new MersenneTwister();
    private EigenDecomposition cho;
    
    public GenerateRanCorrVar(double[][] corrM){
        nrNumbers = corrM.length;
        genRanNumbers = new double[nrNumbers];
        
        //mixedDistr = rD;
        corrMatrix = corrM;
        
        LMatrix = new double[corrMatrix.length][corrMatrix.length];
        DMatrix = new double[corrMatrix.length][corrMatrix.length];
        
        transMatrix = computeFactor(corrMatrix);
        
        
    }
         
    public double[] generateNumbers(){
        return generateNumbers(corrMatrix);
    }
    
    double[][] LMatrix;
    double[][] DMatrix;
    public double[][] computeFactor(double[][] corrM){
        
        //double[] result = new double[corrM.length];
        if(!Cholesky.isSymmetric(corrM)){
            return null;
        }
        
        BlockRealMatrix t1 = new BlockRealMatrix(corrM);
        cho = new EigenDecomposition(t1,0.1);
        transMatrix = cho.getVT().getData();
        //transMatrix = Cholesky_LDL.cholesky(transMatrix);
              
        return transMatrix;
    }
    
    public double[] generateNumbers(double[][] corrM){
        double[] result = new double[corrM.length];
                 
        for(int i = 0; i < genRanNumbers.length; i++){
            genRanNumbers[i] = r.nextDouble();
        }
        
        for(int i = 0; i < corrM.length; i++){
            result[i] = 0;
            for(int j = 0; j < corrM.length; j++){
                result[i] += genRanNumbers[j] * transMatrix[i][j];
            }
        }
        
        return result;
    }

    public double[] generateNumbers(double[] randNumbers){
        return generateNumbers(corrMatrix, randNumbers);
    }
    
    public double[] generateNumbers(double[][] corrM, double[] randNumbers){
       double[] result = new double[corrM.length];
       
       for(int i = 0; i < corrM.length; i++){
            result[i] = 0;
            for(int j = 0; j < corrM.length; j++){
                result[i] += randNumbers[j] * transMatrix[i][j];
            }
       }
        
        return result;
    }
    
    @Override
    public double[] correlation(double[] inputs, double[][] correlation, DiscreteDistribution distr){
        double[] tempout = new double[correlation.length];
        tempout[0] = inputs[0];
        //for(int i = 1; i < tempout.length; i++){
            tempout[1] = distr.generateBasic();
        //}
        tempout =  generateNumbers(tempout);
        
        for(int i = 0; i < tempout.length; i++){
            tempout[i] = (tempout[i] +1)/2.0;
        }
        return tempout;
    }
    
    @Override
    public double[] correlation(double[] inputs, DiscreteDistribution distr){
         return correlation(inputs,corrMatrix,distr);
    }

    double[] normV;
    @Override
    public double[] normValues(){
        normV = new double[transMatrix.length];
        
        for(int i = 0; i < transMatrix.length; i++){
            for(int j = 0; j < transMatrix[0].length;j++) {
                normV[i] += transMatrix[i][j];
            }
        }
        return normV;
    }
    
    @Override
    public double[][] getCorrMatrix(){
        return corrMatrix;
    }

    @Override
    public double[][] getTransfMatrix(){
        return transMatrix;
    }
}
