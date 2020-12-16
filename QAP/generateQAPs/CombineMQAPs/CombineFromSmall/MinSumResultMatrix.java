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

package QAP.generateQAPs.CombineMQAPs.CombineFromSmall;

import QAP.general.GenerateProblem.GenerateMQAPs.CombineProblemsMQAPs;
import QAP.general.GenerateProblem.GenerateMQAPs.GenerateQAPs;
import QAP.general.GenerateProblem.GenerateMQAPs.GoodnessProblemMQAPs;
import QAP.general.GenerateProblem.GenerateMQAPs.OverlappingStrategy;
import QAP.general.GenerateProblem.GenerateProblems;
import QAP.generateQAPs.CombineMQAPs.*;
import QAP.generateQAPs.CombineMQAPs.ChooseOptimum.ChoseOptimSolution;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import QAP.generateQAPs.Goodness.ExactSolution;
import java.util.*;
/**
 *
 * @author madalina
 */
public class MinSumResultMatrix implements CombineProblemsMQAPs{

    private GenerateQAPs result;
    private AlignGenerateMatrices align = new AlignGenerateMatrices();

    // other data from the initialization
    private FlowMatrixWithDistr newFlow;
    private DistanceMatrixWithDistr newDistance;
    private GoodnessProblemMQAPs newMeasure;

    //private GenerateQAPs currentQAP;
    private AlignPermutations util;

    private ExactSolution alternate = new ExactSolution();

    private static Random r = new Random();

    private OverlappingStrategy strategy;

    //flow maxim
    //private int maxFlow;
    //private int radMaxFlow;

    private FillUpRandomMatrix fill;
    
    public MinSumResultMatrix(){}
    
    public MinSumResultMatrix(OverlappingStrategy str, FillUpRandomMatrix f){
        strategy = str;
        util = new AlignPermutations();
        //tableOver = str.compute();
        //sizeResult = str.sizeResultMatrix();

        //maxFlow = maxF;
        //radMaxFlow = radF;

        fill = f;
    }

    @Override public OverlappingStrategy getStrategy(){
        return strategy;
    }
    
    @Override public void setStrategy(OverlappingStrategy str){
        strategy = str;
    }

    private GenerateProblems[] tempList;
    @Override public GenerateProblems[] generateKnownOptim(GenerateProblems[] listP, ChoseOptimSolution chose){

        tempList = new GenerateProblems[listP.length];


        Stack<Integer> identity = new Stack<Integer>();
            for(int j = 0; j < ((GenerateQAPs)listP[0]).getDistance().getNrFacilities(); j++){
                identity.add(j);
            }
        for(int i = 0; i < nrMatrices; i++){

            tempList[i] = align.align(listP[i],identity, chose);
                    
        }

        return tempList;
    }


    @Override public GenerateQAPs combine(GenerateProblems[] listP, ChoseOptimSolution chose){
        return combine(generateKnownOptim(listP, chose),strategy,fill);
    }

    @Override public int getSizeResult(){
        return strategy.sizeResultMatrix();
    }

    private int sizeResult;
    private int[][] tableOver;
    public GenerateQAPs combine(GenerateProblems[] listP, OverlappingStrategy str, FillUpRandomMatrix fill){
        tableOver = str.compute();
        sizeResult = str.sizeResultMatrix();

        fill.setNrFacilities(sizeResult);
        fill.setNrObjectives(((GenerateQAPs)listP[0]).getFlow().getNrObjectives());
        fill.setNrMatrices(strategy.nrOverMatrices());
        //currentQAP = (GenerateQAPs)listP[matrixCount];
        
        result = fill.combine(listP,tableOver);

        newMeasure = ((GenerateQAPs)listP[0]).getMeasure();
        result.setMeasure(newMeasure);

        return result;
    }

    private int nrMatrices;
    public void setNrMatrices(int nrM){
        nrMatrices = nrM;
    }
}
