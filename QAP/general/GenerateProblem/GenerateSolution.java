/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package QAP.general.GenerateProblem;

import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import java.util.Stack;
/**
 *
 * @author madalina
 */
public interface GenerateSolution {
    public Stack<Integer> generateSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow);
    public long[] computeSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow, int[] items); //Stack<Integer> permutation);

    //public long[] computeSolution(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow, Solution_QAPs sol);

}
