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

import QAP.generateQAPs.Distributions.DiscreteDistribution;
import java.util.Arrays;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.UniformRandomGenerator;

public class GenerateUnifCorrVar_apache implements CorrelationGroups{
    
    private double[] genRanNumbers;
    private int nrNumbers;
    
    //private double[][] corrMatrix;
   // private int[] mixedDistr; // 1 - uniform; 0 - normal
    
    private CorrelatedRandomVectorGenerator gener;
    private UniformRandomGenerator randomD;
    private BlockRealMatrix corrMatrix;
    
    private double[][] transMatrix;
    private double[][] corrM;
    //private Random r = new Random();
    
    //private NormalCDF norm = new NormalCDF();
    
    public GenerateUnifCorrVar_apache(double[][] corrM, UniformRandomGenerator gen){
        nrNumbers = corrM.length;
        genRanNumbers = new double[nrNumbers];
        
        transMatrix = transformUniform(corrM);
        corrMatrix = new BlockRealMatrix(transMatrix);
        
        double[] mean = new double[corrM.length];
        for(int i = 0; i < mean.length; i++){
            mean[i] = 0;        
        }
        randomD = gen;
        gener = new CorrelatedRandomVectorGenerator(corrMatrix,0,randomD);
        
        this.corrM = corrM;
        //transMatrix = computeFactor(corrMatrix);
    }
    
    
     private double[][] transformUniform(double[][] corrM){
        transMatrix = new double[corrM.length][corrM.length];
        
        for(int i = 0; i < corrM.length; i++) {
            transMatrix[i] = Arrays.copyOf(corrM[i], corrM[i].length);
        }
        
        for(int i = 0; i < corrM.length; i++){
            transMatrix[i][i] = 1.0;
            for(int j = i+1; j < corrM.length; j++){
                transMatrix[i][j] = 2 * Math.sin(Math.PI * transMatrix[i][j]/6.0);
                transMatrix[j][i] = transMatrix[i][j];
            }
        }
        return transMatrix;
    }
    
    
    @Override
    public double[] correlation(double[] inputs, double[][] correlation, DiscreteDistribution distr){
        double[] result = gener.nextVector();
        double[] tempout = new double[result.length];
        
        for(int i = 0; i < corrM.length/2; i++){
            tempout[2 * i] = Math.exp(-0.5 * (Math.pow(result[2*i],2) + Math.pow(result[2*i+1],2))); 
            tempout[2 * i+1] = 1.0/(2 * Math.PI) * Math.acos(result[2*i]/(Math.pow(result[2*i],2) + Math.pow(result[2*i+1],2))); 
        }
        
        if(tempout.length % 2 == 1){
            tempout[tempout.length-1] = 1.0/(2 * Math.PI) * Math.acos(result[tempout.length-2]/(Math.pow(result[tempout.length-2],2) + Math.pow(result[tempout.length-1],2)));
        }
        return tempout;
    }
    
    @Override
    public double[] correlation(double[] inputs, DiscreteDistribution distr){
         return correlation(inputs,null,distr);
    }

    double[] normV;
    @Override
    public double[] normValues(){
        //??
        return null;
    }
    
    @Override
    public double[][] getCorrMatrix(){
        return corrM;
    }

    @Override
    public double[][] getTransfMatrix(){
        return null;
    }
}
