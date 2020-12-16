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

package QAP.generateQAPs.CombineMQAPs.CombineBig;

import QAP.generateQAPs.CombineMQAPs.AlignPermutations;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import java.util.*;

public class AlignMatrix {

    private AlignPermutations util;
    private Stack<int[]> swaps = new Stack<int[]>();

    public FlowMatrixWithDistr align(FlowMatrixWithDistr flow, Stack<Integer> bestCurrent, Stack<Integer> identity){
       swaps = util.getDistance(bestCurrent, identity);

       for(int j = 0; j < swaps.size(); j++){
                int t1 = swaps.get(j)[0];
                int t2 = swaps.get(j)[1];
                if(t1 != t2) {
                    flow.swapsInFlow(t1, t2);
               }
        }
        return flow;
    }

}
