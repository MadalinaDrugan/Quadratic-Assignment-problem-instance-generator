/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package QAP.general.GenerateProblem;

import java.io.*;
/**
 *
 * @author madalina
 */
public interface GoodnessProblem {
    public void writeMeasurements(int[] perm);
    public void close();
    public void readMatrix();
    public void writeMeasurements(BufferedWriter fDistO, int[] perm);
    public void varianceComputation();
    public void open(String nameF);

}
