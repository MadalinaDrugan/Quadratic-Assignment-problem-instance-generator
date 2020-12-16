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
package QAP.generateQAPs.StrategiesOutsideArea;

import java.util.*;

public class Neighbours {
    public int[] main = new int[2];
    public int[][] neigh = new int[8][2];
    public int realNrNeighs = 0; 
    
    Random r = new Random();
    
    public static int size = 8;
    
    public int[][] getNeighbors(int[] m){
        //main = m;
        
        if(size == 8){
            if(neigh.length != 8){
                neigh  = new int[8][2];
            }
            neigh[0][0] = main[0] - 1; neigh[0][1] = main[1];
            neigh[1][0] = main[0];     neigh[1][1] = main[1] + 1;
            neigh[2][0] = main[0] + 1; neigh[2][1] = main[1];   
            neigh[3][0] = main[0];     neigh[3][1] = main[1] - 1;
        
            neigh[4][0] = main[0] - 1; neigh[4][1] = main[1] + 1;
            neigh[5][0] = main[0] + 1; neigh[5][1] = main[1] + 1;
            neigh[6][0] = main[0] + 1; neigh[6][1] = main[1] - 1;   
            neigh[7][0] = main[0] - 1; neigh[7][1] = main[1] - 1;
        } else if(size == 4){
            if(neigh.length != 4){
                neigh  = new int[4][2];
            }
            neigh[0][0] = main[0] - 1; neigh[0][1] = main[1];
            neigh[1][0] = main[0];     neigh[1][1] = main[1] + 1;
            neigh[2][0] = main[0] + 1; neigh[2][1] = main[1];   
            neigh[3][0] = main[0];     neigh[3][1] = main[1] - 1;            
        } else if(size == 0){
            neigh = null;
        } else {
            System.err.println("Not implemented");
        }
        return neigh;
    }
    
    public int[][] getNeighbors(){
        return getNeighbors(main);
    }

    public void setNeighbours(int[][] thisN){
        neigh = thisN;
        realNrNeighs = thisN.length;
    }   
    
    public int getNrNeighs(){
        return realNrNeighs;
    }
    
    public int[][] randNeigh(){
        if(neigh == null){
            return null;
        }
        
        Stack<Integer> sizeN = new Stack<Integer>();
        int[][] copyN = new int[neigh.length][neigh[0].length];
        for(int i = 0; i < neigh.length; i++){
            copyN[i] = Arrays.copyOf(neigh[i], neigh[i].length);
            //for(int j = 0; j < neigh[0].length; j++){
            //    copyN[i][j] = neigh[i][j];
            //}
        }
        for(int i = 0; i < neigh.length; i++){
            sizeN.add(i);
        }
        int[] tempR = new int[neigh.length];
        int indexN = 0;
        while(sizeN.size() > 0){
            int index = (int)Math.floor(r.nextDouble() * sizeN.size());
            tempR[indexN] = sizeN.get(index);
            sizeN.remove(index);
            indexN++;
        }
        
        for(int i = 0; i < neigh.length; i++){
            neigh[i]= Arrays.copyOf(copyN[tempR[i]], copyN[tempR[i]].length);
        }
        return neigh;
    }
}
