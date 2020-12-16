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

package QAP.generateQAPs.CombineMQAPs;

import java.util.*;

public class AlignPermutations {
    private Stack<int[]> cyclePosition = new Stack<int[]>();
    private int[] cycles;
    private Stack<Integer> tempArray = new Stack<Integer>();

    public AlignPermutations(){

    }
    
    //minimum number of interchanges between two solutions
    public Stack<int[]> getDistance(Stack<Integer> temp1, Stack<Integer> temp2){
       //test the difference between the two strings
       cyclePosition.clear();
       if(cycles == null){
           cycles = new int[2];
       }

       //find the cycles in the two
       for(int j = 0; j < temp2.size();j++) {
           int temp = temp1.get(j);
           if(temp != j){
              cycles = new int[2];
              cycles[0] = j;
              cycles[1] = temp;
              cyclePosition.add(cycles);

              //swich in temp2
              int tempPP = temp1.indexOf(j);
              int tempP = temp1.get(tempPP);
              temp1.set(tempPP, temp1.get(j));
              temp1.set(j, tempP);
           }
        }

       return cyclePosition;
    }

}
