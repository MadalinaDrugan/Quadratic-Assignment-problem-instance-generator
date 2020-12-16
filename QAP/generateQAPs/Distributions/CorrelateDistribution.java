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

package QAP.generateQAPs.Distributions;

public class CorrelateDistribution implements DiscreteDistribution{
    private static java.util.Random r = new java.util.Random();

    private int min = 0;
    private int max = 100;

    private double std = 10;
    private double mean = 10;

    //private double correlation = 0;

    //private GenerateRanCorrVar tempCorr;
    public CorrelateDistribution(){       
    }
    
    public CorrelateDistribution(int minM, int maxM, double[][] corrM){
        min = minM;
        max = maxM;

        std = maxM - minM;
        mean = minM;

        //correlation = corr;

        //tempCorr = new GenerateRanCorrVar(corrM, rD);
    }
    
    public void setBounds(int[] minMax){
        if(minMax.length == 2){
            min = minMax[0];
            max = minMax[1];
        } else {
            System.err.println("Min and max are 2 values. Error in number pf paramenters for the uniform random distribution \n");
        }
    }

    @Override
    public int[] getBounds(){
        int[] minMax = new int[2];
        minMax[0] = min;
        minMax[1] = max;
        return minMax;
    }

    private double variable;
    @Override
    public int generate(){
        double tempT = (generateNormal() + 2.0)*0.25;
        while(tempT < 0.0 || tempT > 1.0){
            tempT = (generateNormal() + 2.0)*0.25;
        }
        variable = std * tempT + mean;
        return (int)variable;
    }

    @Override
    public int generateFrom(double tempT){
        double tempI = tempT;
        variable = std * tempI + mean;
        //while(variable >= max && variable < min){
        //  variable = mean + std * r.nextGaussian();
        //}
        return (int)variable;        
    }
    
    @Override
    public double generateBasic(){
        double tempT = generateNormal(); //(generateNormal() + 3.0)*0.125;
        return tempT;
    }

    @Override
    public double[] checkLimits(int index, double[] in){
        double[] out = in;
        out[index] = (out[index] + 2.0)*0.125;
        
        if(out[index] > 1.0 || out[index] < 0.0){
            out[index] = generateBasic();
        }
        return out;
    }
    
    public boolean inLimits(double in){
        if(in > 1.0 || in < 0.0){
            return false;
        }
        return true;
    }
    

    public int getCount(){
        return -1;
    }

    public void reset(){

    }

    public int getMin(){
        return min;
    }
    
    public int getMax(){
        return max;
    }

    public double generateNormal(){
        double x1, x2, w, y1;        
        do{
            x1 = 2.0 * r.nextDouble() - 1.0;
            x2 = 2.0 * r.nextDouble() - 1.0;
            w = x1 * x1 + x2*x2;
        } while(w >= 1.0);
        w = Math.sqrt( (-2.0 * Math.log(w))/w);
        y1 = x1 * w;
        return y1;
    }

    @Override
    public int generate(double tempT) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double generateBasic(double tempI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
