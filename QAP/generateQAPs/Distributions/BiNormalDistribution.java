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

public class BiNormalDistribution implements DiscreteDistribution{
    private static java.util.Random r = new java.util.Random();

    private int min = 0;
    private int max = 100;

    private double[][] corrObj;
   
    public BiNormalDistribution(){      
    }
    
    public BiNormalDistribution(int minM, int maxM, double[][] corrObjM){
        min = minM;
        max = maxM;

        corrObj = corrObjM;
    }

    @Override
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

    public void setCorr(double[][] corr){
        corrObj = corr;
    }

    @Override
    public int generate(){
        double tempP = generateBasic();
        while(tempP < 0.0 || tempP > 1.0){
            tempP = generateBasic();
        }

        return min + (int) (tempP*(max-min));
    }

    @Override
    public int generate(double tempT1){
        double tempP = generateBasic(tempT1);
        
        while(tempP < 0.0 || tempP > 1.0){
            tempP = generateBasic();
        }

        return min + (int) (tempP*(max-min));
    }

    @Override
    public double generateBasic(){
        double tempT = r.nextDouble();
        return generateBasic(tempT);
    }
      
    @Override
    public double generateBasic(double tempT){
        double tempP, tempR1, tempR2;
        while (true) {
            tempP = r.nextDouble();
            tempR1 = tempP - tempT;
            if (tempR1 < 0) {
                tempR1 *= -1;
            }

            tempP = r.nextDouble();
            tempR2 = tempP - tempT;
            if (tempR2 < 0) {
                tempR2 *= -1;
            }

            double s11, s22, s12;
            
            s11 = 1.0 - Math.pow(Math.abs(corrObj[0][0]), 0.5);
            s22 = 1.0 - Math.pow(Math.abs(corrObj[1][1]), 0.5);
            s12 = 1.0 - Math.pow(Math.abs(corrObj[1][0]), 0.5);
            
            double s1 = Math.sqrt(s12*s12 + s11 * s11);
            double s2 = Math.sqrt(s12*s12 + s22 * s22);
            double rho = (s11*s12 + s22*s12)/(s1 * s2);
            
            double z = Math.pow(tempR1/s1,2) + Math.pow(tempR2/s2,2) - 2 * rho * tempR1/s1 * tempR2/s2;
            
            double tempR = Math.exp(-z / (2.0 * (1.0 - Math.pow(rho, 2)))) / (Math.pow(1.0 - Math.pow(rho, 2), 0.5) * s1 * s2 * 2.506);
            double tempW = r.nextDouble() * 1.0 / (Math.pow(1.0 - Math.pow(rho, 2), 0.5) * s2 * s1 * 2.506);
            if (tempR >= tempW) {
                 break;
            }
        }
        return tempP;
    }
    
    @Override
    public int generateFrom(double tempT){
        return (int)(min + tempT*(max-min));
    }

    @Override
    public int getMin(){
        return min;
    }

    public int getMax(){
        return max;
    }

   public int getCount(){
        return -1;
    }

    public void reset(){

    }

    @Override
    public double[] checkLimits(int index, double[] in){
        double[] out = in;
        
        if(out[index] > 1.0 || out[index] < 0.0){
            out[index] = generateBasic();
        }
        return out;
    }
    
    @Override
    public boolean inLimits(double in){
        if(in > 1.0 || in < 0.0){
            return false;
        }
        return true;
    }
    
}
