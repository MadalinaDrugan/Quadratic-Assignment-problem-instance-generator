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

package QAP.generateQAPs;

import QAP.general.GenerateProblem.GenerateMQAPs.GenerateQAPs;
import QAP.general.GenerateProblem.GenerateMQAPs.GoodnessProblemMQAPs;
import QAP.general.GenerateProblem.GenerateSolution;
import QAP.generateQAPs.CombineMQAPs.*;
import QAP.generateQAPs.CombineMQAPs.ChooseOptimum.ChoseOptimSolution;
import QAP.generateQAPs.Correlation.CorrelationGroups;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import QAP.generateQAPs.Goodness.ExactSolution;
import java.io.*;
import java.util.*;


public class GenerateSingleQAPs extends GenerateQAPs{

    private FlowMatrixWithDistr flow;
    private DistanceMatrixWithDistr distance;
    private AlignGenerateMatrices align = new AlignGenerateMatrices();

    private int[][] currFlow;
    private int[][] currDist;

    private GoodnessProblemMQAPs measurement;
    private ExactSolution currentNDA;

    private BufferedWriter fDistI;
    private String nameF;

    private BufferedWriter fDistO;

    private GenerateQAPs inputQAP;

    private static Random r = new Random();
    
    private ChoseOptimSolution chose;

    //private TransformsMultiObjectiveFlows transform;
    
    public GenerateSingleQAPs(){
    }
    
    public GenerateSingleQAPs(DistanceMatrixWithDistr dist, FlowMatrixWithDistr f, GoodnessProblemMQAPs m, String nF, ChoseOptimSolution cho){
        measurement = m;

        distance = dist;
        flow = f;

        inputQAP = new GenerateMQAPs();

        nameF = nF;

        r.setSeed(r.nextLong());

        currFlow = new int[flow.getNrFacilities()][flow.getNrFacilities()];
        currDist = new int[flow.getNrFacilities()][flow.getNrFacilities()];
        
        chose = cho;
        
        //transform = trans;
    }

    public void alignQAPs(Stack<Integer> identity){
        align.align(this, identity,chose);
    }

    public void checkQAPs(Stack<Integer> identity){
        align.checkResult(this, identity);
    }

    @Override public void generateProblem(CorrelationGroups corr){
        ////////////////////////////////////////
        //0. correlate distance and flow matrices
        // create correlation function
        nrObjectives = flow.getNrObjectives();
                    
        /////////////////////////////////////////
        // 1. generate nrM matrices and their optimal solutions
        DistanceMatrixWithDistr dist = distance.clone();
        double[][] distanceMatrix = dist.createBasicDistanceMatrix();
            
        //////////////////////////////
        // 2. correlate the values in the matrices
        FlowMatrixWithDistr f = flow.clone();
        double[][] flowMatrix =  f.createBasicFlowMatrix(distanceMatrix,corr);
        distanceMatrix = f.getNewDistance();
        
        ///////////////////////////////
        //4. update the matrices
        dist.accomodateBasicMatrix(distanceMatrix);
        f.accomodateBasicMatrix(flowMatrix);
                 
        inputQAP = new GenerateMQAPs(dist, f, measurement, nameF + "_" + 0);
        
        //5. Combine these matrices
        distance = inputQAP.getDistance();
        flow = inputQAP.getFlow();

        
        
        writeQAP(distance, flow, nameF);
    }


    @Override
    public DistanceMatrixWithDistr getDistance(){
        return distance;
    }

    @Override
    public FlowMatrixWithDistr getFlow(){
        return flow;
    }

    @Override
    public GoodnessProblemMQAPs getMeasure(){
        return measurement;
    }

    @Override
    public void setDistance(DistanceMatrixWithDistr dist){
        distance = dist;
    }

    @Override
    public void setFlow(FlowMatrixWithDistr f){
        flow = f;
    }

    public void setFlow(int index, int[][] f){
        flow.setOneFlow(f);
    }

    @Override
    public void setMeasure(GoodnessProblemMQAPs m){
        measurement = m;
        measurement.setParamaters(distance, flow);
    }

    @Override
    public GenerateSolution getSolution(){
        currentNDA = new ExactSolution(distance, flow);
        currentNDA.generateSolution(distance, flow);
        System.out.println("Exact solutions" + currentNDA.returnNDA().ndaToString());
        
        int[] identical = new int[distance.getDistance().length];
        for(int i = 0; i < identical.length; i++){
            identical[i] = i;
        }
        long[] obj = new long[flow.getFlow().length];
        obj = currentNDA.computeSolution(distance, flow, identical);
        
        System.out.println("123..n \t" +obj[0] + "\t" + obj[1]);
        return currentNDA;
    }

    @Override
    public void setSolution(GenerateSolution c){
        currentNDA = (ExactSolution)c;
    }

    private int nrFacilities;
    private int nrObjectives;

    @Override
    public void writeQAP(DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow, String nameF){
        currDist = distance.getDistance();
        currFlow = flow.getFlow();
        nrFacilities = currDist.length;
        nrObjectives = currFlow.length;

        try{
            File myFile = new File(nameF);
            FileWriter fileStream = new FileWriter(myFile,false);
            myFile.createNewFile();
            fileStream = new FileWriter(myFile);
           
            fDistI = new BufferedWriter(fileStream);


            //write the header
            String line = " " + String.valueOf(nrFacilities) + " objectives = " + String.valueOf(nrObjectives) ;
            fDistI.write(line + " \n ");

            //write the distance
            for(int currE = 0; currE < nrFacilities; currE++){
                for(int currF = 0; currF < nrFacilities; currF++) {
                    if (currF < nrFacilities - 1) {
                        fDistI.write(currDist[currE][currF] + "\t");
                    } else {
                        fDistI.write(currDist[currE][currF] + "\n");
                    }
                }
            }
            fDistI.write("\n");

            //write the flow - first dimension
            for(int currE = 0; currE < nrFacilities; currE++){
                for(int currF = 0; currF < nrFacilities; currF++) {
                    if (currF < nrFacilities - 1) {
                            fDistI.write(currFlow[currE][currF] + "\t");
                    } else {
                            fDistI.write(currFlow[currE][currF] + "\n");
                    }
                }
                fDistI.write("\n");
            }

            fDistI.flush();
            fDistI.close();
        } catch (Exception e){
	System.out.println("Write not possible:"+fDistI.toString());
	e.printStackTrace(System.out);
        }
    }

    public void writeMeasures(GoodnessProblemMQAPs measurement, String nameF, int[] perm){
            //measurements
            measurement.open(nameF);
            measurement.readMatrix();
            measurement.varianceComputation();
            measurement.writeMeasurements(perm);
            measurement.close();
    }

    @Override
    public void exactResult(ExactSolution currentNDA, DistanceMatrixWithDistr distance, FlowMatrixWithDistr flow){
        currentNDA.generateSolution(distance, flow);
    }

    @Override
    public void exactResult(String nameF){
        try{
            currentNDA.generateSolution(distance, flow);

            File myFile1 = new File(nameF+".measure");
            FileWriter fileStream1 = new FileWriter(myFile1,true);
            fDistO = new BufferedWriter(fileStream1);

            currentNDA.writeMeasurements(fDistO);

            fDistO.flush();
            fDistO.close();
        } catch (Exception e){
			System.out.println("Write not possible:"+nameF);
			e.printStackTrace(System.out);
	}
    }

    @Override
    public void swapInQAPs(int i, int j){
        flow.swapsInFlow(i, j,currFlow);
    }

    @Override public GenerateQAPs clone(){
        GenerateSingleQAPs thisQAP = new GenerateSingleQAPs(distance, flow, measurement, nameF,chose);
        return thisQAP;
    }
    
}
