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

public class MinMaxResultMatrix implements CombineProblemsMQAPs{

    private GenerateQAPs result;

    // other data from the initialization
    private FlowMatrixWithDistr newFlow;
    private DistanceMatrixWithDistr newDistance;
    private GoodnessProblemMQAPs newMeasure;

    private AlignPermutations util;

    private ExactSolution alternate = new ExactSolution();

    private static Random r = new Random();
    
    private OverlappingStrategy strategy;
    private OverlappingStrategy strategyA;

    private FillUpMatrix fill;
    
    public MinMaxResultMatrix(OverlappingStrategy str, OverlappingStrategy strA, FillUpMatrix f){
        strategy = str;
        strategyA = strA;

        util = new AlignPermutations();

        fill = f;
    }

    private GenerateProblems[] resultTemp;
    private GenerateProblems[] tempList;
    private Stack<int[]> swaps = new Stack<int[]>();
    private NonDominatedArchive checkNon, checkOn;
    @Override public GenerateProblems[] generateKnownOptim(GenerateProblems[] listP, ChoseOptimSolution chose){
        tempList = new GenerateProblems[strategyA.nrOverMatrices()];
        resultTemp = new GenerateProblems[listP.length/strategyA.nrOverMatrices()];
        int count = 0;

        for(int i = 0; i < listP.length; i++){
            // find the maximum
            alternate.setParameters(((GenerateQAPs)listP[i]).getDistance(), ((GenerateQAPs)listP[i]).getFlow());
            alternate.generateSolution(((GenerateQAPs)listP[i]).getDistance(), ((GenerateQAPs)listP[i]).getFlow());
            checkNon = alternate.returnNDA();

            Solution_QAPs solution = (Solution_QAPs)chose.chose(checkOn);

            Stack<Integer> identity = new Stack<Integer>();
            for(int j = 0; j < ((GenerateQAPs)listP[i]).getDistance().getNrFacilities(); j++){
                if(i%2 == 0){
                    identity.add(j);
                } else {
                    identity.add(identity.size() - j);
                }
            }
            Stack<Integer> tempI = new Stack<Integer>();
            for(int j = 0; j < ((GenerateQAPs)listP[i]).getDistance().getNrFacilities(); j++){
                tempI.add(solution.items[j]);
            }

            swaps = util.getDistance(tempI, identity);

            tempList[i%strategyA.nrOverMatrices()] = ((GenerateQAPs)listP[i]).clone();
            for(int j = 0; j < swaps.size(); j++){
                for(int k = 1; k < swaps.get(j).length; k++){
                    ((GenerateQAPs)tempList[i%strategyA.nrOverMatrices()]).getFlow().swapsInFlow(swaps.get(j)[k-1], j);
                    ((GenerateQAPs)tempList[i%strategyA.nrOverMatrices()]).getDistance().swapsInDistance(swaps.get(j)[k-1], j);
                }
            }

            // from 2 matrices generated one matrix with known optima
            // return this composed matrice
            if(i%strategyA.nrOverMatrices() == strategyA.nrOverMatrices()-1){
                GenerateQAPs resultTemp_ = combineSmall(tempList,strategyA);
                alternate.setParameters(resultTemp_.getDistance(), resultTemp_.getFlow());
                alternate.generateSolution(resultTemp_.getDistance(), resultTemp_.getFlow());
                checkOn = alternate.returnNDA();

                solution = (Solution_QAPs)chose.chose(checkOn);

                identity.clear();
                for(int j = 0; j < resultTemp_.getDistance().getNrFacilities(); j++){
                    identity.add(j);
                 }

                tempI.clear();
                for(int j = 0; j < resultTemp_.getDistance().getNrFacilities(); j++){
                    tempI.add(solution.items[j]);
                }

                swaps = util.getDistance(tempI, identity);

                resultTemp[count] = resultTemp_.clone();
                for(int j = 0; j < swaps.size(); j++){
                    for(int k = 1; k < swaps.get(j).length; k++){
                        ((GenerateQAPs)resultTemp[count]).getFlow().swapsInFlow(swaps.get(j)[k-1], j);
                    }
                }
                count++;
            }
        }

        return resultTemp;
    }

    private int sizeResult;
    private int[][] tableOver;
    public GenerateQAPs combineSmall(GenerateProblems[] listP, OverlappingStrategy strategy){
        tableOver = strategy.compute();
        sizeResult = strategy.sizeResultMatrix();

        fill.setNrFacilities(sizeResult);
        fill.setNrObjectives(((GenerateQAPs)listP[0]).getFlow().getNrObjectives());

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

    @Override public GenerateQAPs combine(GenerateProblems[] listP, ChoseOptimSolution chose){
        return combine(generateKnownOptim(listP, chose),strategy,fill);
    }

    @Override public int getSizeResult(){
        return strategy.sizeResultMatrix();
    }

    private int nrMatrices;
    @Override
    public void setNrMatrices(int nrM){
        nrMatrices = nrM;
    }

   @Override public OverlappingStrategy getStrategy(){
        return strategy;
    }

    @Override
    public GenerateProblems combine(GenerateProblems[] listP, OverlappingStrategy str, FillUpRandomMatrix fill) {
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

    @Override
    public void setStrategy(OverlappingStrategy str) {
        strategy = str;
    }
}
