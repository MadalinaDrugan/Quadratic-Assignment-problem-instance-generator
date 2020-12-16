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




import QAP.Util.PermutationGenerator;
import QAP.generateQAPs.CombineMQAPs.ChooseOptimum.ChooseAtRandom;
import QAP.generateQAPs.CombineMQAPs.CombineFromSmall.FillUpChebsevMatrix;
import QAP.generateQAPs.CombineMQAPs.CombineFromSmall.MinSumResultMatrix;
import QAP.generateQAPs.Correlation.Correlation2Normal;
import QAP.generateQAPs.Correlation.CorrelationDistanceFlows;
import QAP.generateQAPs.Correlation.CorrelationInterface;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Distributions.*;
import QAP.generateQAPs.Distributions.WithHoles.ComposedDistrHole;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import QAP.generateQAPs.GenerateCombinedQAPs;
import QAP.generateQAPs.Goodness.MeasureQAPs;
import QAP.generateQAPs.Overlapping.OverlappingCycle;
import QAP.generateQAPs.StrategiesOutsideArea.RandomOutsideStrategies;
import QAP.generateQAPs.StrategiesOutsideArea.StrategiesOutside;
import java.io.*;
import java.util.*;

public class GeneratorSizeU_QAP{

   
   
   public static void main(String[] args){

      int nrObjectives = 1;
      int nrFacilities = 8;
      int nrOverlap = 4;
      int nrMatrices = 10;
      double zeroP = 0.5; //percent of 0s
      double distrLow  = 0.5;
      String inputF = "/home/madalina/QAPgenerator/";
      int l = 1,  L = 10;
      int m1 = 20, M = 25; 
      int h = 90, H = 99;
      int nrInstances = 10;

      
      //int sizeM = 1;
      try{
         BufferedReader settingF = new BufferedReader(new FileReader("/home/madalina/QAPgenerator/Test_cQAP/setInput.param"));
         
         String nrObj = settingF.readLine();
         String[] nrObjToken = nrObj.split("[ ]+");
         if(nrObjToken.length > 0){
            nrObjectives = Integer.parseInt(nrObjToken[nrObjToken.length - 1]);
         }
         
         String nrFac = settingF.readLine();
         String[] nrFacToken = nrFac.split("[ ]+");
         if(nrFacToken.length > 0){
            nrFacilities = Integer.parseInt(nrFacToken[nrFacToken.length - 1]);
         }
         
         String nrOver = settingF.readLine();
         String[] nrOverToken = nrOver.split("[ ]+");
         if(nrOverToken.length > 0){
            nrOverlap = Integer.parseInt(nrOverToken[nrOverToken.length - 1]);
         }
         
         String nrMat = settingF.readLine();
         String[] nrMatToken = nrMat.split("[ ]+");
         if(nrMatToken.length > 0){
            nrMatrices = Integer.parseInt(nrMatToken[nrMatToken.length - 1]);
         }
         
         String zP = settingF.readLine();
         String[] zPToken = zP.split("[ ]+");
         if(zPToken.length > 0){
            zeroP = Double.parseDouble(zPToken[zPToken.length - 1]);
         }
         
         String distrL = settingF.readLine();
         String[] distrLToken = distrL.split("[ ]+");
         if(distrLToken.length > 0){
            distrLow = Double.parseDouble(distrLToken[distrLToken.length - 1]);
         }
         
         String lowBounds = settingF.readLine();
         String[] lowBoundsToken = lowBounds.split("[ ]+");
         if(lowBoundsToken.length > 1){
            l = Integer.parseInt(lowBoundsToken[lowBoundsToken.length - 2]);
            L = Integer.parseInt(lowBoundsToken[lowBoundsToken.length - 1]);
         }
         
         String bounds = settingF.readLine();
         String[] boundsToken = bounds.split("[ ]+");
         if(boundsToken.length > 1){
            m1 = Integer.parseInt(boundsToken[boundsToken.length - 2]);
            M = Integer.parseInt(boundsToken[boundsToken.length - 1]);
         }
         
         String highBounds = settingF.readLine();
         String[] highBoundsToken = highBounds.split("[ ]+");
         if(highBoundsToken.length > 1){
            h = Integer.parseInt(highBoundsToken[highBoundsToken.length - 2]);
            H = Integer.parseInt(highBoundsToken[highBoundsToken.length - 1]);
         }
    
         String nrInt = settingF.readLine();
         String[] nrRuns = nrInt.split("[ ]+");
         if(nrRuns.length > 0){
            nrInstances = Integer.parseInt(nrRuns[nrRuns.length - 1]);
         }
         
         String paramF = settingF.readLine();
         String[] paramFToken = paramF.split("[ ]+");
         if(paramFToken.length > 0){
            inputF = paramFToken[paramFToken.length - 1];
         }
         
           } catch (IOException | NumberFormatException e){
        System.out.println("Reading setting not possible: "+"/home/madalina/QAPgenerator/Test_cQAP/setInput.param");
	e.printStackTrace(System.out);
      }

      String nameF = inputF+"cQAP_instances/adqapSize_";

      double[] sumDomin  = new double[2];
      double[] sumQDomin = new double[2];
        
      double[] dominMax = new double[2];
      double[] dominMin = new double[2];

      double sumCorrCoef; 
      double sumQCorrCoeff; 

      double sumVar;
      double sumQVar;

      double[] sumMax = new double[2];
      double[] sumMaxQ = new double[2];

      double[] sumAver = new double[2];
      double[] sumAverQ = new double[2];

      double objective;
      double objStd;

      double objectiveI; 
      double objStdI; 

      double diffObj; 
      double diffObjStd; 

      for(int index = 0; index < nrInstances; index++){
        for(int i = 0; i < 2; i++){
             sumMax[i] = 0;
             sumMaxQ[i] = 0;
             sumAver[i] = 0;
             sumAverQ[i] = 0;
             sumDomin[i] = 0;
             sumQDomin[i] = 0;
                
             dominMax[i] = 0;
             dominMin[i] = Double.MAX_VALUE;
        }

        sumCorrCoef = 0;
        sumQCorrCoeff = 0;
                    
        sumVar = 0;
        sumQVar = 0;

        objective = 0;
        objStd = 0;
                    
        objectiveI = 0;
        objStdI = 0;

        diffObj = 0;
        diffObjStd = 0;

        //data
        //in the "good" area
        DiscreteDistribution normDist = new RandomDistribution(m1,M); 
        DistanceMatrixWithDistr distance = new DistanceMatrixWithDistr(nrFacilities, normDist);
        //Discontinous
        DiscreteDistribution normFlow = new RandomDistribution(m1,M); 
        FlowMatrixWithDistr flow = new FlowMatrixWithDistr(nrFacilities, normFlow);

        /////////////////
        // in the remaining positions
        DiscreteDistribution lowDistr = new RandomDistribution(l,L);
        DiscreteDistribution highDistr = new RandomDistribution(h,H);
        ComposedDistrHole distNorm = new ComposedDistrHole(lowDistr,highDistr,distrLow);
        ComposedDistrHole flowNorm = new ComposedDistrHole(lowDistr,highDistr,1.0-distrLow); 
                        
        StrategiesOutside ranStrategy = new RandomOutsideStrategies ();
        double[] zeroZ = new double[nrObjectives+1];
        zeroZ[0] = zeroP;
        zeroZ[1] = 0.01;
        FillUpChebsevMatrix fillUp = new FillUpChebsevMatrix(ranStrategy,distNorm,flowNorm,distrLow,zeroZ);
        MinSumResultMatrix resultM = new MinSumResultMatrix(new OverlappingCycle(nrFacilities, nrOverlap, nrMatrices),fillUp);

        int sizeO = resultM.getSizeResult();
        
        MeasureQAPs m = new MeasureQAPs();
        ChooseAtRandom ranChoose = new ChooseAtRandom();
        
        String multipleFiles_C = inputF+"cQAP_instances/adqap_"+ sizeO+"."+ nrOverlap +"."+distrLow + "." + zeroP + "." + index;
        GenerateCombinedQAPs QAP_1 = new GenerateCombinedQAPs(nrMatrices, distance, flow, fillUp, m, resultM, multipleFiles_C,ranChoose);
                
        double[][] corrMatrix = new double[nrObjectives+1][nrObjectives+1];
        corrMatrix[0][0] = 1; corrMatrix[1][1] = 1; 
        corrMatrix[0][1] = 0.0; corrMatrix[1][0] = corrMatrix[0][1];

        CorrelationInterface corrInter = new Correlation2Normal(0.0);
        CorrelationDistanceFlows corr = new CorrelationDistanceFlows(corrMatrix,corrInter);

        QAP_1.generateProblem(corr);
 
        /////////////////////////////
        // generate a permutation of the current QAP
        ///////////////////////////
        
        //////////////////////
        // 1. Identical solution
        //////////////////
        int[] items = new int[sizeO];
        for(int k1 = 0; k1 < sizeO; k1++) {
              items[k1] = k1;
        }

        QAP_1.writeMeasures(m, multipleFiles_C,items);

                       
        long obj = 0;
            int[][] myDist = QAP_1.getDistance().getDistance();
            int[][] myFlow = QAP_1.getFlow().getFlow();
            for(int j1 = 0; j1 < myDist.length; j1++) {
                for (int k1 = j1 + 1; k1 < myDist[0].length; k1++) {
                  if (myDist[j1][k1] == myDist[k1][j1] & myFlow[items[j1]][items[k1]] == myFlow[items[k1]][items[j1]]) {
                      obj += 2 * myDist[j1][k1] * myFlow[items[j1]][items[k1]];
                  } else {
                      System.out.println("Not symetrical matrices");
                  }
                }
            }

            objective += obj;
            objStd += Math.pow(obj, 2);

            /////////////////////////
            // 2. Inverse solution
            /////////////////////////
            for(int k1 = 0; k1 < sizeO; k1++) {
                items[k1] = sizeO - k1 - 1;
            }

            long objI = 0;
            for(int j1 = 0; j1 < myDist.length; j1++) {
             for (int k1 = j1 + 1; k1 < myDist[0].length; k1++) {
                  if (myDist[j1][k1] == myDist[k1][j1] & myFlow[items[j1]][items[k1]] == myFlow[items[k1]][items[j1]]) {
                                    objI += 2 * myDist[j1][k1] * myFlow[items[j1]][items[k1]];
                  } else {
                                    System.out.println("Not symetrical matrices");
                  }
             }
            }
        
            objectiveI += objI;
            objStdI += Math.pow(objI, 2);

        diffObj += (objI - obj);
        diffObjStd += Math.pow(objI - obj,2);

        //////////////////////
        // 3. A random solution
        /////////////////////
        //generate a permutated distance and flow matrices
        ArrayList<Integer> itemV = new ArrayList(sizeO);
        for(int i = 0; i < items.length; i++){
            itemV.add(i);
        }
        Collections.shuffle(itemV);
        int[] itemR = new int[sizeO];
        for(int i = 0; i < sizeO; i++){
            itemR[i] = itemV.get(i);
        }
            
        //transform itemV --> item
        for(int i = 0; i < items.length; i++){
            int switchI = itemV.indexOf(i);
            if(switchI < i){
                System.out.println(" Error in the permutation procedure ");
            } else if(switchI == i){
                
            } else {
                int tempI = itemV.get(i);
                itemV.set(i, i);
                itemV.set(switchI, tempI);
                
                for(int k = 0; k < sizeO; k++){
                    int tempP = myFlow[switchI][k];
                    myFlow[switchI][k] = myFlow[i][k];
                    myFlow[i][k] = tempP;
                }
                
                for(int k = 0; k < sizeO; k++){
                    int tempP = myFlow[k][switchI];
                    myFlow[k][switchI] = myFlow[k][i];
                    myFlow[k][i] = tempP;
                }
            }
        }
        
        //write the results
        String multipleFiles_V = inputF+"cQAP_instances/adqapR_"+ sizeO+"."+ nrOverlap +"."+distrLow + "." + zeroP + "." + index;
        try{
            BufferedWriter fDistV;
            File myFileV = new File(multipleFiles_V);
            FileWriter fileStream_V = new FileWriter(myFileV,true);
            if(!myFileV.exists()){
                 myFileV.createNewFile();
                 fileStream_V = new FileWriter(myFileV);
            }
            fDistV = new BufferedWriter(fileStream_V);
            
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            formatter.format("%d \n",sizeO);
            
            for(int d = 0; d < sizeO; d++){
                for(int k = 0; k < sizeO; k++){
                    formatter.format("%d \t",myDist[d][k]);
                }
                formatter.format("\n");
            }
            formatter.format("\n");
            
            for(int d = 0; d < sizeO; d++){
                for(int k = 0; k < sizeO; k++){
                    formatter.format("%d \t",myFlow[d][k]);
                }
                formatter.format("\n");
            }
            formatter.format("\n");
            
            fDistV.write(formatter.toString());
            fDistV.write("\n");            
            fDistV.close();
        } catch (Exception e){
            System.out.println("Write not possible:"+multipleFiles_V);
            e.printStackTrace(System.out);
        }

        try{
            BufferedWriter fDistS;
            File myFileS = new File(multipleFiles_V + ".sln");
            FileWriter fileStream_S = new FileWriter(myFileS,true);
            if(!myFileS.exists()){
                 myFileS.createNewFile();
                 fileStream_S = new FileWriter(myFileS);
            }
            fDistS = new BufferedWriter(fileStream_S);
            
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            formatter.format("%f \n",objective);
            
            for(int d = 0; d < sizeO; d++){
                formatter.format("%d \t",itemR[d]);
            }
            formatter.format("\n");

            fDistS.write(formatter.toString());
            fDistS.write("\n");            
            fDistS.close();
            
        } catch (Exception e){
            System.out.println("Write not possible:"+multipleFiles_V);
            e.printStackTrace(System.out);
        }
       
        
        //////////////////////
        // measurements
        /////////////////
        sumVar += ((MeasureQAPs)QAP_1.getMeasure()).autoCorr;
        sumQVar += Math.pow(((MeasureQAPs)QAP_1.getMeasure()).autoCorr, 2);

        sumCorrCoef += ((MeasureQAPs)QAP_1.getMeasure()).autoCorrCoef;
        sumQCorrCoeff += Math.pow(((MeasureQAPs)QAP_1.getMeasure()).autoCorrCoef, 2);

    for(int d = 0; d < ((MeasureQAPs)QAP_1.getMeasure()).maxMatrix.length; d++){
         sumMax[d] += ((MeasureQAPs)QAP_1.getMeasure()).maxMatrix[d];
         sumMaxQ[d] += Math.pow(((MeasureQAPs)QAP_1.getMeasure()).maxMatrix[d], 2);

         int nrF = ((MeasureQAPs)QAP_1.getMeasure()).nrMeasur;
         sumAver[d] += ((MeasureQAPs)QAP_1.getMeasure()).varMatrix[d]/nrF;
         sumAverQ[d] += Math.sqrt(((MeasureQAPs)QAP_1.getMeasure()).stdMatrix[d]/nrF - Math.pow( ((MeasureQAPs)QAP_1.getMeasure()).stdMatrix[d]/nrF, 2));

         double domin = ((MeasureQAPs)QAP_1.getMeasure()).dominance[d];
         sumDomin[d] += domin;
         sumQDomin[d] += Math.pow(domin, 2);
                    
         if(dominMax[d] < domin){
             dominMax[d] = domin;
         } 
         if(dominMin[d] > domin){
             dominMin[d] = domin;
         }
    }

    ///////////////////////
    //print the quality measures
    StringBuilder sb = new StringBuilder();
    Formatter formatter = new Formatter(sb, Locale.US);

    formatter.format("%.2f \t %.2f \n",distrLow,(zeroP*distrLow));
            
    for(int d = 0; d < 2; d++){
          formatter.format("%d \t %.2f \t %.2f \t ", d, sumDomin[d],Math.sqrt(sumQDomin[d] - Math.pow(sumDomin[d], 2)));
          formatter.format("%.2f \t %.2f \t ", sumMax[d], Math.sqrt(sumMaxQ[d] - Math.pow(sumMax[d], 2)));
          formatter.format("%.2f \t %.2f \t ", sumAver[d], sumAverQ[d]);

          double temp = 100 * (dominMax[d] - sumDomin[d])/(dominMax[d] - dominMin[d]);
          formatter.format("%.2f \t %.2f \t %.2f \n ", dominMax[d], dominMin[d], temp);
    }

    formatter.format("%.2f \t %.2f \t ", sumVar, Math.sqrt(sumQVar - Math.pow(sumVar, 2)));
    formatter.format("%.2f \t %.2f \n ", sumCorrCoef, Math.sqrt(sumQCorrCoeff - Math.pow(sumCorrCoef, 2)));
            
    double tempO = objective;
    double tempD = Math.sqrt(objStd - Math.pow(tempO, 2));
    formatter.format("%.2f \t %.2f \t ",tempO,tempD);
    

   ////////////////////////                 
   // write measures for the generated QAP instance  
   //////////////////////////                 
   try{
        BufferedWriter fDistO;
        File myFile1 = new File(nameF+".measure");
        FileWriter fileStream1 = new FileWriter(myFile1,true);
        if(!myFile1.exists()){
            myFile1.createNewFile();
            fileStream1 = new FileWriter(myFile1);
        }
        fDistO = new BufferedWriter(fileStream1);


        fDistO.write(formatter.toString());
        fDistO.write("\n");
        fDistO.write("\n");

        /////////////////////
        // generate the problem
        BufferedWriter fDistO1;
        File myFile2 = new File(nameF+".facil_v9");
        FileWriter fileStream2 = new FileWriter(myFile2,true);
        if(!myFile2.exists()){
             myFile2.createNewFile();
             fileStream2 = new FileWriter(myFile2);
        }
        fDistO1 = new BufferedWriter(fileStream2);
            
        sb = new StringBuilder();
        formatter = new Formatter(sb, Locale.US);

        formatter.format("%d \t %.1f \t %.1f \t ",nrFacilities, distrLow, (zeroP*distrLow));
        formatter.format("%.2f \t %.2f \t ", sumCorrCoef,Math.sqrt(sumQCorrCoeff - Math.pow(sumCorrCoef, 2)));
            
        for(int d = 0; d < 2; d++){
               formatter.format("%.0f \t %.0f \t ",sumDomin[d], Math.sqrt(sumQDomin[d] - Math.pow(sumDomin[d], 2)));
               double temp = 100 * (dominMax[d] - sumDomin[d])/(dominMax[d] - dominMin[d]);
               formatter.format("%.0f \t\t",temp);
        }

        tempO = objective;
        tempD = Math.sqrt(objStd - Math.pow(tempO, 2));
        formatter.format("%.1f \t %.1f \t",tempO, tempD);

        tempO = objectiveI;
        tempD = Math.sqrt(objStdI - Math.pow(tempO, 2));
        formatter.format("%.1f \t %.1f \t",tempO, tempD);
            //}

            //for(int d = 0; d < nrObjectives; d++){
        tempO = diffObj;
        tempD = Math.sqrt(diffObjStd - Math.pow(tempO, 2));
        formatter.format("%.1f \t %.1f \t",tempO, tempD);
            //}

        fDistO1.write(formatter.toString());
        fDistO1.write("\n");
        fDistO1.close();

        fDistO.close();
      } catch (Exception e){
        System.out.println("Write not possible:"+nameF);
	e.printStackTrace(System.out);
      }
        }
   }
   }
