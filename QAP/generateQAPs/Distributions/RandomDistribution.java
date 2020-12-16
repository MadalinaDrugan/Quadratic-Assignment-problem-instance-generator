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

public class RandomDistribution implements DiscreteDistribution{
    private static java.util.Random r = new java.util.Random();

    private int min = 0;
    private int max = 100;

    public RandomDistribution(int minM, int maxM){
        min = minM;
        max = maxM;
    }

    public RandomDistribution(){}
    
    public void setBounds(int[] minMax){
        if(minMax.length == 2){
            min = minMax[0];
            max = minMax[1];
        } else {
            System.err.println("Min and max are 2 values. Error in number pf paramenters for the uniform random distribution \n");
        }
    }

    public int[] getBounds(){
        int[] minMax = new int[2];
        minMax[0] = min;
        minMax[1] = max;
        return minMax;
    }

    public int generate(){
        return min + (int) (r.nextDouble()*(max-min));
    }

     public int generate(double tempT){
        return min + (int) (r.nextDouble()*(max-min));
    }

    public int generateFrom(double tempT){
        return (int)(min + tempT*(max-min));
    }

    public double generateBasic(){
        return r.nextDouble();
    }
    
    public double generateBasic(double tempT){
        return r.nextDouble();
    }
 
    public double[] checkLimits(int index, double[] in){
        double[] out = in;
        
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

}
