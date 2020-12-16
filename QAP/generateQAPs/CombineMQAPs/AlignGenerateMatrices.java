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

//import MOInstance_QAPs.*;
//import general.*;
import QAP.Archives.*;
import QAP.general.GenerateProblem.GenerateMQAPs.GenerateQAPs;
import QAP.general.GenerateProblem.GenerateProblems;
import QAP.generateQAPs.CombineMQAPs.ChooseOptimum.ChoseOptimSolution;
import QAP.generateQAPs.Goodness.ExactSolution;
import java.util.*;

public class AlignGenerateMatrices {

    private AlignPermutations util = new AlignPermutations();

    private ExactSolution alternate = new ExactSolution();

    private static Random r = new Random();

    private Stack<int[]> swaps = new Stack<int[]>();
    private ArchiveSolutions checkNon;
    public GenerateProblems align(GenerateProblems tempP, Stack<Integer> identity, ChoseOptimSolution chose){
            GenerateProblems tempL;

            alternate.setParameters(((GenerateQAPs)tempP).getDistance(), ((GenerateQAPs)tempP).getFlow());
            alternate.generateSolution(((GenerateQAPs)tempP).getDistance(), ((GenerateQAPs) tempP).getFlow());
            checkNon = alternate.returnNDA();

            Solution_QAPs solution = (Solution_QAPs)chose.chose(checkNon);

            Stack<Integer> tempI = new Stack<Integer>();
            for(int j = 0; j < ((GenerateQAPs)tempP).getDistance().getNrFacilities(); j++){
                tempI.add(solution.items[j]);
            }

            swaps = util.getDistance(tempI, identity);

            tempL = ((GenerateQAPs)tempP).clone();
            for(int j = 0; j < swaps.size(); j++){
                int t1 = swaps.get(j)[0];
                int t2 = swaps.get(j)[1];
                if(t1 != t2) {
                    ((GenerateQAPs) tempL).getFlow().swapsInFlow(t1, t2);
                }
            }

            return tempL;
    }

    public void checkResult(GenerateQAPs tempList, Stack<Integer> identity){

        alternate.setParameters(tempList.getDistance(), tempList.getFlow());
        alternate.generateSolution(tempList.getDistance(), tempList.getFlow());        
        checkNon = alternate.returnNDA();

        Iterator<Solution> iter = checkNon.iterator();
        boolean flag = false;
        while(iter.hasNext()){
                Solution sol = iter.next();

                boolean cont = true;
                for(int j = 0; j < sol.items.length; j++){
                    if(sol.items[j] != identity.get(j)){
                        cont = false;
                    }
                }
                if(cont == true){
                    flag = true;
                    break;
                }
        }
        if(!flag) {

                long[] obj = alternate.computeSolution(tempList.getDistance(), tempList.getFlow(), identity);
                //System.out.println(" Identiy value" + obj[0]);

                //System.err.println("NonDominated checked "+ checkNon.ndaToString());
        }
    }
}
