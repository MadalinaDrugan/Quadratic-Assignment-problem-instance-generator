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
package QAP.generateQAPs.CombineMQAPs.ChooseOptimum;

import QAP.Archives.*;
//import general.*;
import java.util.*;

public class ChooseScalarization implements ChoseOptimSolution{
    
    private double[] scal;
    
    @Override
    public void selectScal(double[] s){
        scal = new double[s.length];
        scal = Arrays.copyOf(s, s.length);
    }
    
    @Override
    public Solution chose(ArchiveSolutions nda){
        Iterator<Solution> iter = nda.iterator();
        Solution temp = null;
        double valueSol = -Double.MAX_VALUE;
        while(iter.hasNext()){
            Solution s = iter.next();
            if(temp == null){
                temp = s;
                valueSol = 0;
                for(int i = 0; i < scal.length; i++){
                    valueSol += scal[i] * ((Solution_QAPs)s).objectives[i];
                }
                continue;
            }
            double thisV = 0;
            for(int i = 0; i < scal.length; i++){
                thisV += scal[i] * ((Solution_QAPs)s).objectives[i];
            }
            if(thisV < valueSol){
                valueSol = thisV;
                temp = s;
            }
        }
        return temp;
    }
}
