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

import QAP.Archives.*;
import QAP.general.GenerateProblem.GenerateMQAPs.CombineProblemsMQAPs;
import QAP.general.GenerateProblem.GenerateMQAPs.GenerateQAPs;
import QAP.general.GenerateProblem.GenerateMQAPs.GoodnessProblemMQAPs;
import QAP.general.GenerateProblem.GenerateMQAPs.OverlappingStrategy;
import QAP.general.GenerateProblem.GenerateProblems;
import QAP.generateQAPs.CombineMQAPs.ChooseOptimum.ChoseOptimSolution;
import QAP.generateQAPs.CombineMQAPs.CombineFromSmall.FillUpRandomMatrix;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import QAP.generateQAPs.Goodness.ExactSolution;
import java.util.*;

public class FillResultMatrix implements CombineProblemsMQAPs{

    private GenerateQAPs result;

    // other data from the initialization
    private FlowMatrixWithDistr newFlow;
    private DistanceMatrixWithDistr newDistance;
    private GoodnessProblemMQAPs newMeasure;

    private AlignPermutations util;

    private ExactSolution alternate = new ExactSolution();

    private static Random r = new Random();

    private OverlappingStrategy strategy;

    private FillUpMatrix fill;
    
    public FillResultMatrix(){}
    
    public FillResultMatrix(OverlappingStrategy str, FillUpMatrix f){
        strategy = str;
        util = new AlignPermutations();

        fill = f;
    }

    @Override public OverlappingStrategy getStrategy(){
        return strategy;
    }
    
    @Override public void setStrategy(OverlappingStrategy str){
        strategy = str;
    }

    private GenerateProblems[] tempList;
    private Stack<int[]> swaps = new Stack<int[]>();
    private NonDominatedArchive checkNon;
    @Override public GenerateProblems[] generateKnownOptim(GenerateProblems[] listP, ChoseOptimSolution chose){

        tempList = new GenerateProblems[listP.length];

        Stack<Integer> identity = new Stack<Integer>();
        for(int j = 0; j < ((GenerateQAPs)listP[0]).getDistance().getNrFacilities(); j++){
                identity.add(j);
        }

        for(int i = 0; i < nrMatrices; i++){

            tempList[i] = align(listP[i],identity);
                    
        }

        return tempList;
    }

    public GenerateProblems align(GenerateProblems tempP, Stack<Integer> identity){
            GenerateProblems tempL;

            alternate.setParameters(((GenerateQAPs)tempP).getDistance(), ((GenerateQAPs)tempP).getFlow());
            alternate.generateSolution(((GenerateQAPs)tempP).getDistance(), ((GenerateQAPs) tempP).getFlow());
            checkNon = alternate.returnNDA();

            int tempSol = (int)Math.floor(r.nextDouble() * checkNon.size());
            Solution_QAPs solution = (Solution_QAPs)checkNon.getNthSolution(tempSol);

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
                System.err.print(" Wrong Solution !");
        }
    }

    @Override public GenerateQAPs combine(GenerateProblems[] listP, ChoseOptimSolution chose){
        return combine(generateKnownOptim(listP,chose),strategy,fill);
    }

    @Override public int getSizeResult(){
        return strategy.sizeResultMatrix();
    }

    private int sizeResult;
    private int[][] tableOver;
    @Override
    public GenerateQAPs combine(GenerateProblems[] listP, OverlappingStrategy str, FillUpRandomMatrix fill){
        tableOver = str.compute();
        sizeResult = str.sizeResultMatrix();

        fill.setNrFacilities(sizeResult);
        fill.setNrObjectives(((GenerateQAPs)listP[0]).getFlow().getNrObjectives());
        fill.setNrMatrices(strategy.nrOverMatrices());
        
        result = fill.combine(listP,tableOver);

        newMeasure = ((GenerateQAPs)listP[0]).getMeasure();
        result.setMeasure(newMeasure);

        return result;
    }

     public GenerateQAPs combine(GenerateProblems[] listP, OverlappingStrategy str, FillUpMatrix fill){
        tableOver = str.compute();
        sizeResult = str.sizeResultMatrix();

        fill.setNrFacilities(sizeResult);
        fill.setNrObjectives(((GenerateQAPs)listP[0]).getFlow().getNrObjectives());
        fill.setNrMatrices(strategy.nrOverMatrices());
        
        result = fill.combine(listP,tableOver);

        newMeasure = ((GenerateQAPs)listP[0]).getMeasure();
        result.setMeasure(newMeasure);

        return result;
    }

   private int nrMatrices;
    @Override
    public void setNrMatrices(int nrM){
        nrMatrices = nrM;
    }
}
