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

package QAP.generateQAPs.Goodness;
//import MOInstance_QAPs.QAPs;
import QAP.general.GenerateProblem.GenerateMQAPs.GoodnessProblemMQAPs;
import QAP.generateQAPs.Distance.DistanceMatrixWithDistr;
import QAP.generateQAPs.Flows.FlowMatrixWithDistr;
import java.io.*;
import java.util.*;

/**
 *
 * @author madalina
 */

public class MeasureQAPs extends GoodnessProblemMQAPs{
    private BufferedReader fDistI;
    private BufferedWriter fDistO;
    private BufferedReader fDistB;

    //internal data
    public int[][] distance;
    public int[][] flow;

    public int nrFacilites;
    public int nrObjectives;
    public final static java.util.Random r = new java.util.Random();
    //public int maxX, maxY;

    public int[] maxMatrix;
    public int[] minMatrix;
    public int[] varMatrix;
    public int[] stdMatrix;
    public int nrMeasur;

    public int[] items;
    public long[] obj;
    //
    public double[] dominance;

    public int[] itemsBest;
    public long[] objBest;

    public double autoCorr; 
    public double autoCorrCoef; 
    //measurements of how hard is the problem and what is a good solution

    //private FillUpMatrix fillUp;
    /////////////////////////////////////
    private double[] sumDomin;
    private double[] sumQDomin;
        
    private double[] dominMax;
    private double[] dominMin;

    private double sumCorrCoef;
    private double sumQCorrCoeff;

    private double sumVar;
    private double sumQVar;

    private double[] sumMax;
    private double[] sumMaxQ;

    private double[] sumAver;
    private double[] sumAverQ;

    private double[] sumStd;
    private double[] sumStdQ;

    private double[] objective;
    private double[] objStd;

    private double[] objectiveI;
    private double[] objStdI;

    private double[] diffObj;
    private double[] diffObjStd;

    private double[][] corrMatrix;

    //read the QAP from a file
    public MeasureQAPs(String nameF, double[][] corrM){

        if(corrM != null){
            corrMatrix = new double[corrM.length][corrM[0].length];
            for(int i = 0; i < corrM.length; i++){
                System.arraycopy(corrM[i], 0, corrMatrix[i], 0, corrM[0].length);
            }
        }
        
        try{
            File myFile = new File(nameF);
            FileReader fileStream = new FileReader(myFile);
            myFile.createNewFile();
            fileStream = new FileReader(myFile);
            fDistI = new BufferedReader(fileStream);

            File myFile1 = new File(nameF+".measure");
            FileWriter fileStream1 = new FileWriter(myFile1,true);
            myFile1.createNewFile();
            fileStream1 = new FileWriter(myFile1);
            fDistO = new BufferedWriter(fileStream1);
        } catch (Exception e){
			System.out.println("Write not possible:"+nameF);
			e.printStackTrace(System.out);
	}

        //readMatrix();
        //init internal data
       // r.setSeed(r.nextLong());
        
    }

    public MeasureQAPs(double[][] corrM){
            if(corrM != null){
            corrMatrix = new double[corrM.length][corrM[0].length];
            for(int i = 0; i < corrM.length; i++){
                System.arraycopy(corrM[i], 0, corrMatrix[i], 0, corrM[0].length);
            }
        }
    }
    
    public MeasureQAPs(){}
    
   //read the QAP from a file
    @Override
    public void open(String nameF){
        try{
            File myFile = new File(nameF);
            FileReader fileStream = new FileReader(myFile);
            myFile.createNewFile();
            fileStream = new FileReader(myFile);
            fDistI = new BufferedReader(fileStream);

            File myFile1 = new File(nameF+".measure");
            FileWriter fileStream1 = new FileWriter(myFile1,true);
            myFile1.createNewFile();
            fileStream1 = new FileWriter(myFile1);
            fDistO = new BufferedWriter(fileStream1);
        } catch (Exception e){
			System.out.println("Write not possible:"+nameF);
			e.printStackTrace(System.out);
	}

        //readMatrix();
        //init internal data
        r.setSeed(r.nextLong());
    }

    @Override
    public void setParamaters(DistanceMatrixWithDistr dist, FlowMatrixWithDistr f){
        distance = dist.getDistance();
        flow = f.getFlow();
        //fillUp = fill;
    }

    public void readBest(String nameB){
        
        try{
            File myFile = new File(nameB);
            FileReader fileStream = new FileReader(myFile);
            myFile.createNewFile();
            fileStream = new FileReader(myFile);
            fDistB = new BufferedReader(fileStream);

            String temp = fDistB.readLine();
            boolean setMinus = false;
            
            if(temp!=null){
              String[] entries = temp.split("\\s+");

              if(entries.length >= nrObjectives){
                  objBest = new long[nrObjectives];
              }
              for(int i = 0; i < nrObjectives; i++){
                  objBest[i] = Long.parseLong(entries[i]);
              }

              if(entries.length >= nrObjectives + nrFacilites){
                  itemsBest = new int[nrFacilites];
              }
              for(int i = 0; i < nrFacilites; i++){
                  itemsBest[i] = Integer.parseInt(entries[i+nrObjectives]);
                  if(itemsBest[i] == nrFacilites){
                      setMinus = true;
                  }
              }
              
              if(setMinus == true){
                  for(int i = 0; i < nrFacilites; i++){
                      itemsBest[i] = itemsBest[i]-1;
                  }
              }
            }

            fDistB.close();
        } catch (IOException e){
			System.out.println("Write not possible:"+nameB);
			e.printStackTrace(System.out);
	}
    }

    @Override
    public void readMatrix(){
        int[] limitA;
        int[][] limitB;
        int nrItemsFirstLine = 19;

        limitA = new int[2];
        limitA[0] = nrItemsFirstLine;
        limitB = new int[2][2];
      try{
        String temp = fDistI.readLine();
        //process the header of the finction
        if(temp!=null){
              String[] entries = temp.split("\\s+");

              //facilities: entries[2]
              nrFacilites = Integer.parseInt(entries[1]);
              distance = new int[nrFacilites][nrFacilites];
              limitA[1] = nrItemsFirstLine + nrFacilites * nrFacilites;

              //objectives: entries[5]
              nrObjectives = Integer.parseInt(entries[4]);
              flow = new int[nrFacilites][nrFacilites];

              limitB = new int[nrObjectives][2];
              for(int j = 0; j < nrObjectives; j++){
                   limitB[j][0] = nrItemsFirstLine + j + nrFacilites * nrFacilites*(j+1);
                   limitB[j][1] = nrItemsFirstLine + j + nrFacilites * nrFacilites*(j+2);
              }
        }

        temp = fDistI.readLine();
        int count = 0;
        int entry = 0;
        
        while(temp != null){
            if(temp.contentEquals("")){
                 temp = fDistI.readLine();
                 continue;
            }
            String[] entries = temp.split("\\s+");

             //read A
            if(count < nrFacilites){
               if(entries[0].contentEquals("")){
                  for(int i=1;i<entries.length;i++) {
                      distance[count][entry+i - 1] = Integer.parseInt(entries[i]);
                  }
                  entry+= entries.length - 1;
               } else {
                  for(int i=0;i<entries.length;i++) {
                      distance[count][entry+i] = Integer.parseInt(entries[i]);
                  }
                  entry+= entries.length;
               }
               
               if(entry < nrFacilites){
                    //count is the line number
                   
               } else {
                   count++;
                   entry = 0;
                }
            }else {
               //read one of B
               //compute in which matrix is
               //int thisObj = count/distance.length-1;
               int thisLine = count%distance.length;

               //System.out.println(Integer.toString(count)+ ": line "+Integer.toString(thisObj)+ ", colomn "+Integer.toString(thisLine) + " element "+ entries);
               if(entries[0].contentEquals("")){
                   for(int i = 1; i < entries.length; i++) {
                            flow[thisLine][entry + i - 1] = Integer.parseInt(entries[i]);
                   }
                   entry+= entries.length - 1;
               } else {
                   for(int i = 0; i < entries.length; i++) {
                            flow[thisLine][entry + i] = Integer.parseInt(entries[i]);
                   }
                   entry+= entries.length;
               }
               if(entry < nrFacilites){
                    //count is the line number
                   
               } else {
                   count++;
                   entry = 0;
                }
               //count++;
            }
            //count++;
            temp = fDistI.readLine();
        }

        ///////////////////
        //statistics
        maxMatrix = new int[2];
        minMatrix = new int[2];
        varMatrix = new int[2];
        stdMatrix = new int[2];

        this.nrMeasur = 0;
        for(int i = 0; i < 2; i++) {
                maxMatrix[i] = 0;
                minMatrix[i] = Integer.MAX_VALUE;
                varMatrix[i] = 0;
                stdMatrix[i] = 0;
        }

        for(int i = 0; i < distance.length; i++) {
                for (int j = i+1; j < distance.length; j++) {
                    if (maxMatrix[0] < distance[i][j]) {
                        maxMatrix[0] = distance[i][j];
                    }
                    //for (int d = 0; d < flow.length; d++) {
                        if (maxMatrix[1] < flow[i][j]) {
                            maxMatrix[1] = flow[i][j];
                        }
                    //}

                    if (minMatrix[0] > distance[i][j]) {
                        minMatrix[0] = distance[i][j];
                    }
                    //for (int d = 0; d < flow.length; d++) {
                        if (minMatrix[1] > flow[i][j]) {
                            minMatrix[1] = flow[i][j];
                        }
                    //}

                    varMatrix[0] += distance[i][j];
                    stdMatrix[0] += (int)Math.pow(distance[i][j],2);

                    //for (int d = 0; d < flow.length; d++) {
                        varMatrix[1] += flow[i][j];
                        stdMatrix[1] += (int)Math.pow(flow[i][j],2);
                    //}
                    nrMeasur++;
            }
        }
      }catch (FileNotFoundException e){
			System.out.println("Read not possible:");
			System.out.println(e);
			System.exit(0);
      }catch (IOException e){
			System.out.println("Read not possible:");
			System.out.println(e);
			System.exit(0);
      }
    }
    

    @Override
    public void varianceComputation(){
        double F1; // = new double[nrObjectives];
        double F2; // = new double[nrObjectives];
        double f1; // = new double[nrObjectives];
        double D1 = 0, D2 = 0, d1 = 0;
        double F; // = new double[nrObjectives];
        double Ff; // = new double[nrObjectives];
        double D = 0, Dd = 0;
        double FDc; // = new double[nrObjectives];
        double FDc2; // = new double[nrObjectives];

        //for(int i = 0; i < nrObjectives; i++){
            F1 = 0;
            F2 = 0;
            f1 = 0;
            for(int j = 0; j < nrFacilites; j++){
                double temp_f1 = 0;
                for(int k = 0; k < nrFacilites; k++){
                    F1 += flow[j][k];
                    F2 += Math.pow(flow[j][k],2);
                    temp_f1 += flow[j][k];
                }
                f1 += Math.pow(temp_f1, 2);
            }
            F = f1 - F2;
            Ff = Math.pow(F1, 2) + 2 * F2 - 4 * f1;
        //}

        for(int j = 0; j < nrFacilites; j++){
           double temp_f1 = 0;
           for(int k = 0; k < nrFacilites; k++){
                    D1 += distance[j][k];
                    D2 += Math.pow(distance[j][k],2);
                    temp_f1 += distance[j][k];
           }
           d1 += Math.pow(temp_f1, 2);
        }
        D = d1 - D2;
        Dd = Math.pow(D1, 2) + 2 * D2 - 4 * d1;

        //cost functions and
        //for(int i = 0; i < nrObjectives; i++){
            FDc = F1*D1/(2*nrFacilites*(nrFacilites-1));
            FDc2 = Ff * Dd/(4*nrFacilites*(nrFacilites-1)*(nrFacilites-2)*(nrFacilites-3));
            FDc2 += F * D /(nrFacilites*(nrFacilites-1)*(nrFacilites-2));
            FDc2 += F2 * D2 / (2*nrFacilites*(nrFacilites-1));
        //}

        double variance; // = new double[nrObjectives];
        double cost; // = new double[nrObjectives];
        //autoCorr = new double[1];
        //autoCorrCoef = new double[1];
        
        //for(int i = 0; i < nrObjectives; i++){

            variance = FDc2 - Math.pow(FDc, 2);
                    /*F2[i] * D2 * Math.pow(nrFacilites,4);
            variance[i] -= 2 * (F2[i] *(2*D2 + d1)+f1[i]*(D2-d1))*Math.pow(nrFacilites, 3);
            variance[i] += (Math.pow(F1[i], 2)*(D2 - 2*d1) +F2[i]*(Math.pow(D1,2)+5*D2+4*d1) -2*f1[i] *(Math.pow(D1,2) - 2 * D2))*Math.pow(nrFacilites,2);
            variance[i] += (Math.pow(F1[i], 2)*(2*Math.pow(D1, 2) -D2 + 2 *d1) - F2[i] * (Math.pow(D1, 2) + 2 * D2 + 2 * d1) + 2 * f1[i] * (Math.pow(D1, 2) - D2 -d1)) * nrFacilites;
            variance[i] -= 3 * Math.pow(F1[i], 2) * Math.pow(D1, 2);
            variance[i] /= 2*Math.pow(nrFacilites*(nrFacilites-1),2)*(nrFacilites-2)*(nrFacilites-3);*/

            cost = (nrFacilites-3.0)*F*D/(nrFacilites-2.0);
            cost += (nrFacilites-2.0)*F2*D2;
            cost += Ff*Dd / ((nrFacilites-2.0)*(nrFacilites-3.0));
            cost += F * D /(nrFacilites-2.0);
            cost -= F *Dd/(nrFacilites-2);
            cost -= F2 * D;
            cost -= F*D2;
            cost -= Ff * D / (nrFacilites-2.0);
            cost /= 0.25 * Math.pow(nrFacilites * (nrFacilites-1), 2);
                    /*F2[i] * D2 * Math.pow(nrFacilites,3);
            cost[i] -= (2 * F2[i] *(2*D2 + d1)+f1[i]*(2*D2-d1))*Math.pow(nrFacilites, 2);
            cost[i] += (Math.pow(F1[i], 2)*(D2 - d1) +F2[i] *(Math.pow(D1,2)+5*D2+4*d1) -f1[i] *(Math.pow(D1,2) - 4 * D2 - 3*d1))*nrFacilites;
            cost[i] += Math.pow(F1[i], 2)*(2*Math.pow(D1, 2) -D2 - d1) - (F2[i] + f1[i]) * (Math.pow(D1, 2) + 2 * D2 + 2 * d1);
            cost[i] /= 4*Math.pow(nrFacilites*(nrFacilites-1),2)*(nrFacilites-2)*(nrFacilites-3);*/

            autoCorr = 2 * variance/cost;
            autoCorrCoef = 100 - 400*(autoCorr-((double)nrFacilites)/4.0)/(double)(nrFacilites-2);
        //}

    }

    @Override
    public void writeMeasurements(int[] perm){
        writeMeasurements(fDistO,perm);
        try{
            if(corrMatrix != null){
                for(int i = 0; i < corrMatrix.length; i++){
                    for(int j = 0; j < corrMatrix[0].length; j++){
                        fDistO.write(corrMatrix[i][j] + ",\t");
                    }
                    fDistO.write("\n");
                }              
                fDistO.write("\n");
            }
        
            fDistO.close();
        } catch (Exception e){
			System.out.println("Write not possible:"+this.fDistO.toString());
			e.printStackTrace(System.out);
	}
    }

    @Override
    public void close(){
        try{
            //fDistO.
            fDistO.close();
        } catch (Exception e){
			System.out.println("Write not possible:"+this.fDistO.toString());
			e.printStackTrace(System.out);
	}
    }

        
    @Override
    public void writeMeasurements(BufferedWriter fDistO, int[] perm){
        dominance = new double[nrObjectives+1];
        
        double tempR = 0, tempP = 0;
        double tempRD = 0, tempPD = 0;
        try{
            //distance dominance
            fDistO.write("Distance dominance = ");
            for(int currE = 0; currE < nrFacilites; currE++){
                for(int currF = 0; currF < nrFacilites; currF++) {
                    tempRD += distance[currE][currF];
                }
            }
            tempRD /= Math.pow(nrFacilites,2);

            for(int currE = 0; currE < nrFacilites; currE++){
                for(int currF = 0; currF < nrFacilites; currF++) {
                    tempPD += Math.pow(distance[currE][currF] - tempRD, 2);
                }
            }
            tempPD = Math.sqrt(tempPD / Math.pow(nrFacilites,2));

            dominance[0] = 100* tempPD/tempRD;
            fDistO.write(dominance[0] + "\n");

            //flow dominance
            fDistO.write("Flow dominance = ");
            for(int currE = 0; currE < nrFacilites; currE++){
                for(int currF = 0; currF < nrFacilites; currF++) {
                   tempR += flow[currE][currF];
                }
            }
            tempR /= Math.pow(nrFacilites,2);

            for(int currE = 0; currE < nrFacilites; currE++){
                for(int currF = 0; currF < nrFacilites; currF++) {
                   tempP += Math.pow(flow[currE][currF] - tempR, 2);
                }
            }
            tempP = Math.sqrt(tempP / Math.pow(nrFacilites,2));

            dominance[1] = 100* tempP/tempR;
            fDistO.write(dominance[1] + "\t" + "dominance {" + Math.max(dominance[0], dominance[1]) +","+Math.min(dominance[0], dominance[1]) + "}\n");

            tempR = 0;
            tempP = 0;

          //variance and correlation
          fDistO.write("Variance and rugdness \n");
          varianceComputation();
          //for(int i = 0; i < nrObjectives; i++){
              fDistO.write(autoCorr + "\t" + autoCorrCoef +"\n");
          //}

          fDistO.flush();

          // best solution
          if(objBest == null || objBest.length != nrObjectives){
             fDistO.write("Compute the solution with 12..n \t");
             //QAPs thisProblem = new QAPs();
             items = new int[distance.length];
             for(int i = 0; i < distance.length; i++) {
                 items[i] = i;
             }
           
             
             obj = new long[1];
             obj[0] = 0;
             for(int j1 = 0; j1 < distance.length; j1++) {
                for (int k1 = j1 + 1; k1 < distance[0].length; k1++) {
                  if (distance[j1][k1] == distance[k1][j1] & flow[items[j1]][items[k1]] == flow[items[k1]][items[j1]]) {
                       obj[0] += 2 * distance[j1][k1] * flow[items[j1]][items[k1]];
                  } else {
                       System.out.println("Not symetrical matrices");
                  }
                }
             }

             //obj = thisProblem.computeSolution(distance, myFlow, items);
             for(int i = 0; i < obj.length; i++){
                fDistO.write(obj[i] + "\t");
             }
             fDistO.newLine();

            // given permutation
            if(perm != null){
               fDistO.write("Given solution "); 
               long[] obj_2 =  new long[1];
               for(int j1 = 0; j1 < distance.length; j1++) {
                for (int k1 = j1 + 1; k1 < distance[0].length; k1++) {
                  if (distance[j1][k1] == distance[k1][j1] & flow[perm[j1]][perm[k1]] == flow[perm[k1]][perm[j1]]) {
                       obj_2[0] += 2 * distance[j1][k1] * flow[perm[j1]][perm[k1]];
                  } else {
                       System.out.println("Not symetrical matrices");
                  }
                }
             }
               
             for(int i = 0; i < obj_2.length; i++){
                    fDistO.write(obj_2[i] + "\t");
             }
             fDistO.newLine();
           }
            
           for(int i = 0; i < distance.length; i++) {
                items[i] = nrFacilites - 1 - i;
           }
           
           long[] obj_1 = new long[1];
             for(int j1 = 0; j1 < distance.length; j1++) {
                for (int k1 = j1 + 1; k1 < distance[0].length; k1++) {
                  if (distance[j1][k1] == distance[k1][j1] & flow[items[j1]][items[k1]] == flow[items[k1]][items[j1]]) {
                       obj_1[0] += 2 * distance[j1][k1] * flow[items[j1]][items[k1]];
                  } else {
                       System.out.println("Mot symetrical matrices");
                  }
                }
             }
           
            fDistO.write("Inverse objective ");
            for(int i = 0; i < obj_1.length; i++){
                fDistO.write(obj_1[i] + "\t");
            }
            fDistO.newLine();

            fDistO.write("difference ");
            for(int i = 0; i < obj_1.length; i++){
                fDistO.write((obj_1[i] - obj[i]) + "\t");
            }
            fDistO.newLine();

          } else {
            fDistO.write("Compute the best solution: \t");
            for(int i = 0; i < nrObjectives; i++){
                fDistO.write(objBest[i] + "\t");
            }
            fDistO.write("\t\t");
            for(int i = 0; i < nrFacilites; i++){
                fDistO.write(itemsBest[i] + "\t");
            }
            fDistO.newLine();

            //QAPs thisProblem = new QAPs();
            items = new int[distance.length];
            for(int i = 0; i < distance.length; i++) {
                items[i] = itemsBest[nrFacilites - 1 - i];
            }
            
            //int[][][] flowMy = new int[1][flow.length][flow[0].length];
            //for(int i = 0; i < flow.length; i++){
            //      System.arraycopy(flow[i], 0, flowMy[0][i], 0, flow[i].length);
            //}

            obj = new long[1]; //thisProblem.computeSolution(distance, flowMy, items);
            for(int j1 = 0; j1 < distance.length; j1++) {
                for (int k1 = j1 + 1; k1 < distance[0].length; k1++) {
                  if (distance[j1][k1] == distance[k1][j1] & flow[items[j1]][items[k1]] == flow[items[k1]][items[j1]]) {
                     obj[0] += 2 * distance[j1][k1] * flow[items[j1]][items[k1]];
                  } else {
                       System.out.println("Not symetrical matrices");
                  }
               }
            }
            

            fDistO.write("Inverse objectives");
            for(int i = 0; i < flow.length; i++){
                fDistO.write(obj[i] + "\t");
            }

            fDistO.write("difference ");
            for(int i = 0; i < flow.length; i++){
                fDistO.write((obj[i] - objBest[i]) + "\t");
            }
            fDistO.newLine();

            fDistO.write("maxmin ");
            for(int i = 0; i < flow.length+1; i++){
                double temp = Math.sqrt(stdMatrix[i]/nrMeasur - Math.pow(varMatrix[i]/nrMeasur, 2));
                fDistO.write(maxMatrix[i] + "\t" + minMatrix[i] + "\t" + (varMatrix[i]/nrMeasur) + "\t" + temp + "\t");
            }
            fDistO.newLine();
          }

          fDistO.flush();
        } catch (Exception e){
			System.out.println("Write not possible:"+this.fDistO.toString());
			e.printStackTrace(System.out);
	}
    }

 
    @Override
    public void addMeasurements(){
        
        //QAPs thisProblem = new QAPs();
        items = new int[distance.length];
        for(int k1 = 0; k1 < distance.length; k1++) {
             items[k1] = k1;
        }

        //int[][][] flowMy = new int[1][flow.length][flow[0].length];
        //for(int i = 0; i < flow.length; i++){
        //    for(int j = 0; j < flow[i].length; j++){
        //       flowMy[0][i][j] = flow[i][j];
        //    }
        //}

        obj = new long[1];//thisProblem.computeSolution(distance, flowMy, items);
        for(int j1 = 0; j1 < distance.length; j1++) {
            for (int k1 = j1 + 1; k1 < distance[0].length; k1++) {
                  if (distance[j1][k1] == distance[k1][j1] & flow[items[j1]][items[k1]] == flow[items[k1]][items[j1]]) {
                     obj[0] += 2 * distance[j1][k1] * flow[items[j1]][items[k1]];
                  } else {
                       System.out.println("Not symetrical matrices");
                  }
               }
         }
            

        for(int k1 = 0; k1 < nrObjectives; k1++){
              objective[k1] += obj[k1];
              objStd[k1] += Math.pow(obj[k1], 2);
        }

        for(int k1 = 0; k1 < distance.length; k1++) {
              items[k1] = distance.length - k1 - 1;
        }

        long[] objI = new long[1]; //thisProblem.computeSolution(distance, flowMy, items);
        for(int j1 = 0; j1 < distance.length; j1++) {
            for (int k1 = j1 + 1; k1 < distance[0].length; k1++) {
                  if (distance[j1][k1] == distance[k1][j1] & flow[items[j1]][items[k1]] == flow[items[k1]][items[j1]]) {
                     objI[0] += 2 * distance[j1][k1] * flow[items[j1]][items[k1]];
                  } else {
                       System.out.println("Not symetrical matrices");
                  }
            }
         }
            
        
        for(int k1 = 0; k1 < nrObjectives; k1++){
              objectiveI[k1] += objI[k1];
              objStdI[k1] += Math.pow(objI[k1], 2);

              diffObj[k1] += (objI[k1] - obj[k1]);
              diffObjStd[k1] += Math.pow(objI[k1] - obj[k1],2);
        }

        //////////////////////
        // add measurements
        /////////////////
        //for(int d = 0; d < autoCorr.length; d++){
        sumVar += autoCorr;
        sumQVar += Math.pow(autoCorr, 2);

        sumCorrCoef += autoCorrCoef;
        sumQCorrCoeff += Math.pow(autoCorrCoef, 2);
        //}

        for(int d = 0; d < maxMatrix.length; d++){
            sumMax[d] += maxMatrix[d];
            sumMaxQ[d] += Math.pow(maxMatrix[d], 2);

            int nrF = nrMeasur;
            sumAver[d] += varMatrix[d]/nrF;
            sumAverQ[d] += Math.sqrt(stdMatrix[d]/nrF - Math.pow(stdMatrix[d]/nrF, 2));

            double domin = dominance[d];
            sumDomin[d] += domin;
            sumQDomin[d] += Math.pow(domin, 2);
                    
            if(dominMax[d] < domin){
                dominMax[d] = domin;
            } 
            if(dominMin[d] > domin){
                dominMin[d] = domin;
            }
        }
        
        countMeasures++;
    }

    private int countMeasures = 0;
    @Override
    public void resetMeasurements(){
        
       sumDomin  = new double[nrObjectives+1];
        sumQDomin = new double[nrObjectives+1];
        
        dominMax = new double[nrObjectives+1];
        dominMin = new double[nrObjectives+1];

        //sumCorrCoef = new double[nrObjectives+1];
        //sumQCorrCoeff = new double[nrObjectives+1];

        //sumVar = new double[nrObjectives+1];
        //sumQVar = new double[nrObjectives+1];

        sumMax = new double[nrObjectives+1];
        sumMaxQ = new double[nrObjectives+1];

        sumAver = new double[nrObjectives+1];
        sumAverQ = new double[nrObjectives+1];

        sumStd = new double[nrObjectives+1];
        sumStdQ = new double[nrObjectives+1];

        objective = new double[nrObjectives];
        objStd = new double[nrObjectives];

        objectiveI = new double[nrObjectives];
        objStdI = new double[nrObjectives];

        diffObj = new double[nrObjectives];
        diffObjStd = new double[nrObjectives];


        for(int i = 0; i < nrObjectives+1; i++){
                sumMax[i] = 0;
                sumMaxQ[i] = 0;
                sumAver[i] = 0;
                sumAverQ[i] = 0;
                sumStd[i] = 0;
                sumStdQ[i] = 0;
                sumDomin[i] = 0;
                sumQDomin[i] = 0;
                
                dominMax[i] = 0;
                dominMin[i] = Double.MAX_VALUE;
        }

        //for(int i = 0; i < sumMax.length; i++){
        //        sumCorrCoef[i] = 0;
        //        sumQCorrCoeff[i] = 0;
        //}

        for(int i = 0; i < nrObjectives; i++){
                objective[i] = 0;
                objStd[i] = 0;
        }

        for(int i = 0; i < nrObjectives; i++){
                objectiveI[i] = 0;
                objStdI[i] = 0;

                diffObj[i] = 0;
                diffObjStd[i] = 0;
        }

        countMeasures = 0;
    }
    
    @Override
    public Formatter writeAddedMeasurements(){
       StringBuilder sb = new StringBuilder();
       Formatter formatter = new Formatter(sb, Locale.US);

       //formatter.format("%d \t %.2f \t %.2f \n", size[N],distrZeroP[0],(zeroP[k]*(1 - distrZeroP[0])));
       if(countMeasures == 0){
           return formatter;
       }     
       
       formatter.format("%d \t %d \n",countMeasures,sumDomin.length);
       
       for(int d = 0; d < sumDomin.length; d++){
           double mean = sumDomin[d]/countMeasures;
           double std = Math.sqrt(sumQDomin[d]/countMeasures - Math.pow(sumDomin[d]/countMeasures, 2));
           formatter.format("%.2f \t %.2f \t ", mean,std);
       }
       formatter.format("\n");
       
       for(int d = 0; d < sumDomin.length; d++){
           double mean = sumMax[d]/countMeasures;
           double std = Math.sqrt(sumMaxQ[d]/countMeasures - Math.pow(sumMax[d]/countMeasures, 2));
           formatter.format("%.2f \t %.2f \t ", mean, std);
       }
       formatter.format("\n");
            
       for(int d = 0; d < sumDomin.length; d++){
           formatter.format("%.2f \t %.2f \t ", sumAver[d]/countMeasures, sumAverQ[d]/countMeasures);
       }
       formatter.format("\n");
       
       for(int d = 0; d < sumDomin.length; d++){
           double temp = 100 * (dominMax[d] - sumDomin[d]/countMeasures)/(dominMax[d] - dominMin[d]);
           formatter.format("%.2f \t %.2f \t %.2f \t ", dominMax[d]/countMeasures, dominMin[d]/countMeasures, temp);
       }
       formatter.format("\n");

       double mean = sumVar /countMeasures;
           double std = Math.sqrt(sumQVar /countMeasures - Math.pow(sumVar /countMeasures, 2));
           formatter.format("%.2f \t %.2f \t ", mean, std);
       //}
       formatter.format("\n");
       
       //for(int d = 1; d < sumVar.length; d++){
           mean = sumCorrCoef /countMeasures;
           std = Math.sqrt(sumQCorrCoeff /countMeasures - Math.pow(sumCorrCoef /countMeasures, 2));
           formatter.format("%.2f \t  %.2f \t ", mean, std);
      //}
      formatter.format("\n");
            
      for(int d = 0; d < nrObjectives; d++){
           double tempO = objective[d]/countMeasures;
           double tempD = Math.sqrt(objStd[d]/countMeasures - Math.pow(tempO, 2));
           formatter.format("%.2f \t %.2f \t ",tempO,tempD);
      }
      formatter.format("\n");
            
      return formatter;

    }
    
    @Override
    public void writeAdd2Measurements(String nameF){
       /////////////////////
            // generate the problem
       try{
            BufferedWriter fDistO1;
            File myFile2 = new File(nameF+".facil_v9");
            FileWriter fileStream2 = new FileWriter(myFile2,true);
            if(!myFile2.exists()){
                    myFile2.createNewFile();
                    fileStream2 = new FileWriter(myFile2);
                }
            fDistO1 = new BufferedWriter(fileStream2);
            
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            if(countMeasures == 0){
                fDistO1.close();
                return;
            }
            
            formatter.format("%d \t %d \n",countMeasures,sumDomin.length);
                    
            double mean = sumCorrCoef /countMeasures;
            double std = Math.sqrt(sumQCorrCoeff /countMeasures - Math.pow(sumCorrCoef /countMeasures, 2));
            formatter.format("%.2f \t  %.2f \n", mean,std);
            
            for(int d = 0; d < sumDomin.length; d++){
                mean = sumDomin[d]/countMeasures;
                std = Math.sqrt(sumQDomin[d]/countMeasures - Math.pow(sumDomin[d]/countMeasures, 2));
                formatter.format("%.0f \t  %.0f \t ",mean, std);
                double temp = 100 * (dominMax[d] - sumDomin[d]/countMeasures)/(dominMax[d] - dominMin[d]);
                formatter.format("%.0f \t",temp);
            }
            formatter.format("\n");
            
            for(int d = 0; d < nrObjectives; d++){
                double tempO = objective[d]/countMeasures;
                double tempD = Math.sqrt(objStd[d]/countMeasures - Math.pow(tempO, 2));
                formatter.format("%.1f \t  %.1f \t",tempO, tempD);
            }
            formatter.format("\n");

            for(int d = 0; d < nrObjectives; d++){
                double tempO = objectiveI[d]/countMeasures;
                double tempD = Math.sqrt(objStdI[d]/countMeasures - Math.pow(tempO, 2));
                formatter.format("%.1f \t  %.1f \t",tempO, tempD);
            }
            formatter.format("\n");
            
            for(int d = 0; d < nrObjectives; d++){
                double tempO = diffObj[d]/countMeasures;
                double tempD = Math.sqrt(diffObjStd[d]/countMeasures - Math.pow(tempO, 2));
                formatter.format("%.1f \t  %.1f \t",tempO, tempD);
            }
            formatter.format("\n");

            fDistO1.write(formatter.toString());
            fDistO1.write("\n");
            fDistO1.close();

        } catch (Exception e){
	System.out.println("Write not possible:"+this.fDistO.toString());
	e.printStackTrace(System.out);
        }
    }
}
