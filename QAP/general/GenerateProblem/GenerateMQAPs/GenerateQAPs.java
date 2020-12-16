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

package QAP.general.GenerateProblem.GenerateMQAPs;

import QAP.general.GenerateProblem.GenerateProblems;
import QAP.general.GenerateProblem.GenerateSolution;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import QAP.generateQAPs.Goodness.ExactSolution;

public abstract class GenerateQAPs implements GenerateProblems{

    public abstract DistanceMatrixWithDistr getDistance();
    public abstract FlowMatrixWithDistr getFlow();
    public abstract GoodnessProblemMQAPs getMeasure();
    public abstract GenerateSolution getSolution();

    public abstract void setDistance(DistanceMatrixWithDistr dist);
    public abstract void setFlow(FlowMatrixWithDistr f);
    public abstract void setMeasure(GoodnessProblemMQAPs m);
    public abstract void setSolution(GenerateSolution m);

    public abstract void writeQAP(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow, String nameF);

    public abstract void exactResult(String nameF);
    public abstract void exactResult(ExactSolution m, DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow);

    public abstract void swapInQAPs(int i, int j);

    @Override public abstract GenerateQAPs clone();
}