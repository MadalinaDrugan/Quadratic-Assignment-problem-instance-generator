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

package QAP.generateQAPs.Distributions.WithHoles;

import QAP.generateQAPs.Distributions.DiscreteDistribution;

public class ComposedDistrHole implements DiscreteDistributionWithHoles{
    private static java.util.Random r = new java.util.Random();

    private DiscreteDistribution lowDistr;
    private DiscreteDistribution highDistr;

    private double percentDown;
    private int countDown;
    private int countTotal;

    public ComposedDistrHole(){}
    
    public ComposedDistrHole(DiscreteDistribution l, DiscreteDistribution h, double percent){
        lowDistr = l;
        highDistr = h;
   
        percentDown = percent;
        countDown=0;
        countTotal = 0;
    }

    @Override
    public void setPercentDown(double perD){
        percentDown = perD;
    }
    
    @Override
    public void setBounds(int[] minMax){
        if(minMax.length == 4){
            int[] temp = new int[2];
            temp[0] = minMax[0];
            temp[1] = minMax[1];
            lowDistr.setBounds(temp);
            
            temp[0] = minMax[2];
            temp[1] = minMax[3];
            highDistr.setBounds(temp);
        } else {
            System.err.println("Min and max are 2 values. Error in number pf paramenters for the uniform random distribution \n");
        }
    }

    @Override
    public int[] getBounds(){
        int[] minMax = new int[4];
        int[] temp = new int[2];
        temp = lowDistr.getBounds();
        minMax[0] = temp[0];
        minMax[1] = temp[1];
        
        temp = highDistr.getBounds();
        minMax[2] = temp[0];
        minMax[3] = temp[1];
        
        return minMax;
    }

    @Override
    public int generateLow(){
        countDown++;
        countTotal++;
        return lowDistr.generate();
    }
    
    @Override
    public int generateHigh(){
        countTotal++;
        return highDistr.generate();
    }
    
    private boolean flagDistr;
    
    public void setDistribution(boolean flag){
        flagDistr = flag;
    }
    
    @Override
    public int generate(){
        if(r.nextDouble() < percentDown){
            countDown++;
            countTotal++;
            return lowDistr.generate();
        }
        countTotal++;
        return highDistr.generate();
    }

    @Override
    public int generate(double tempT1){
        if(r.nextDouble() < percentDown){
            countDown++;
            countTotal++;
            return lowDistr.generateFrom(tempT1); //.generate(tempT1);
        }
        countTotal++;
        return highDistr.generateFrom(tempT1);
        
    }

    @Override
    public int generateHighFrom(double tempT){
        countTotal++;
        return highDistr.generateFrom(tempT);        
    }
    
    @Override
    public int generateLowFrom(double tempT){
        countDown++;
        countTotal++;
        return lowDistr.generateFrom(tempT); 
    }
    
    @Override
    public int generateFrom(double tempT){      
        if(r.nextDouble() < percentDown){
            countDown++;
            countTotal++;
            return generateLowFrom(tempT);
        }
        countTotal++;
        return generateHighFrom(tempT);
    }
    
    @Override
    public double generateBasic(){
        if(r.nextDouble()< percentDown){
            return lowDistr.generateBasic();
        }
        return highDistr.generateBasic();
    }

    @Override
    public double generateBasic(double tempT){
        if(r.nextDouble()< percentDown){
            return lowDistr.generateBasic(tempT);
        }
        return highDistr.generateBasic(tempT);
    }
    
    @Override
    public int getCount(){
        return countDown;
    }
    
    public int countLarge(){
        return countTotal - countDown;
    }

    @Override
    public void reset(){
        countDown = 0;
        countTotal = 0;
    }

    @Override
    public int getMin(){
        return lowDistr.getMin();
    }

    @Override
    public int getMax(){
        return highDistr.getMax();
    }

    @Override
    public double[] checkLimits(int index, double[] in){
        if(flagDistr){
            return highDistr.checkLimits(index, in);
        }
        
        return lowDistr.checkLimits(index, in);
    }
    
    @Override
    public boolean inLimits(double in){
        if(in > 1.0 || in < 0.0){
            return false;
        }
        return true;
    }
    
    @Override
    public int setInHighBounds(double tempT){
      if(tempT < 1.0 && tempT >= 0.0){
      } else {
          //set in the limits
          System.out.println(" OUtside limits ");
      }
      // int[] temp = new int[2];
       temp = highDistr.getBounds();
       
       return (int)Math.floor(temp[0] + tempT*(temp[1] - temp[0]));
    }

    int[] temp = new int[2];
    @Override
    public int setInLowBounds(double tempT){
      if(tempT < 1.0 && tempT >= 0.0){
      } else {
          System.out.println(" OUtside limits ");          
      }
       
       temp = lowDistr.getBounds();
       
       return (int)Math.floor(temp[0] + tempT*(temp[1] - temp[0]));
    }
}
