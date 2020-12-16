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
package QAP.generateQAPs.StrategiesOutsideArea;

import java.util.*;

public class NeighboursStrategies{
    
    int[] nrSizeClusters;
    Random r = new Random();
    
    Stack<Neighbours> thisNeighbourhood; 
            
    public Stack<Neighbours> getNeighList(){
        return thisNeighbourhood;
    }
    
    public void setParameters(int[] nrSC){
        nrSizeClusters = nrSC;
    }
    
    public Stack<int[]> arrangeList(Stack<Integer> list){
        Stack<Neighbours> tempL = setNeighList(list);
        return arrange(tempL);
    }
    
    public Stack<int[]> arrange(Stack<Neighbours> neigh){
        thisNeighbourhood = neigh;
        Stack<int[]> temp = new Stack<int[]>();
        // copy to a hash table 
        Stack<Integer> neighThis = new Stack<Integer>();
        for(int i = 0; i < thisNeighbourhood.size(); i++){
            //System.out.println(" COUnt " + i + "main (" + neigh.get(i).main[0] + ", " + neigh.get(i).main[1] + ") " + neigh.get(i));
            int newIndex = 1000*thisNeighbourhood.get(i).main[0] + thisNeighbourhood.get(i).main[1];
            neighThis.add(newIndex);
        }
        Stack<Neighbours> thisNeigh = new Stack<Neighbours>();
        Neighbours tempN;
        
        for(int i = 0; i < nrSizeClusters.length; i++){
            //chose the starting solution for clustering
            int countCluster = nrSizeClusters[i];
            thisNeigh.clear();
            
            while(countCluster > 0){
                
                if(thisNeigh.size() == 0){
                    //nothing in neighbourhood list
                    int start = (int)Math.floor(r.nextDouble() * thisNeighbourhood.size());
                    if(thisNeighbourhood.size() != 0){
                        tempN = thisNeighbourhood.get(start);   
                    } else {
                        tempN = null;
                    }
                    
                } else {
                    tempN = thisNeigh.pop();     
                }
                
                if(thisNeighbourhood.size() != 0){
                    int[] centerT = tempN.main;
                    temp.add(centerT);
                    countCluster--;
                    
                    int[][] neighT = tempN.randNeigh();
                    if(neighT != null){
                        
                        for(int j = 0; j < neighT.length; j++){
                            Integer newIndex = 1000*neighT[j][0] + neighT[j][1];
                            int indexR = neighThis.indexOf(newIndex);
                            if(indexR == -1){
                                //System.out.println(" Size stacks " + neighThis.size() + " ? " + thisNeighbourhood.size() + " member ? " + thisNeighbourhood.indexOf(indexR));
                            } else {
                                thisNeigh.push(thisNeighbourhood.get(indexR));     
                            }
                        }
                    }
                    
                    Integer inter = 1000*tempN.main[0]+ tempN.main[1];
                    if(!thisNeighbourhood.remove(tempN)){
                        System.out.println("Neighbour should exists ("+ tempN.main[0] + "," + tempN.main[1] + ")");
                    }
                    if(!neighThis.remove(inter)){
                       System.out.println("This Neighbour should exists ("+ tempN.main[0] + "," + tempN.main[1] + ")");                      
                    }
                } else {
                    countCluster--;
                }
            }
        }
        return temp;
    }
    
    public Stack<Neighbours> setNeighList(Stack<Integer> list){
        Stack<Neighbours> set = new Stack<Neighbours>();
        Stack<int[]> tempS = new Stack<int[]>();
        
        Iterator<Integer> iter = list.iterator();
        while(iter.hasNext()){
            Integer thisT = iter.next();
            
            // initialization temp
            Neighbours temp = new Neighbours();
            temp.main[0] = thisT/1000;
            temp.main[1] = thisT%1000;
            int[][] possibleT = temp.getNeighbors();
            
            //validate neighbours
            tempS.clear();
            if(possibleT != null){
                for(int i = 0; i < possibleT.length; i++){
                    // transforms in Integer
                    int tempInteger = 1000 * possibleT[i][0] + possibleT[i][1]; 
                    if(list.contains(tempInteger)){
                        tempS.add(possibleT[i]);
                    }
                }
                possibleT = new int[tempS.size()][possibleT[0].length];
                for(int i = 0; i < tempS.size(); i++){
                    possibleT[i] = Arrays.copyOf(tempS.get(i), tempS.get(i).length);
                }
                temp.setNeighbours(possibleT);
                set.add(temp);
            } else {
                temp.neigh = null;
                set.add(temp);
            }
        }
        return set;
    }
}
