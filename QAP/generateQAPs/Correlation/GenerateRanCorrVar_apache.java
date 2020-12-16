package QAP.generateQAPs.Correlation;

import QAP.generateQAPs.Distributions.DiscreteDistribution;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.NormalizedRandomGenerator;

public class GenerateRanCorrVar_apache implements CorrelationGroups{
    
    private double[] genRanNumbers;
    private int nrNumbers;
    
    private CorrelatedRandomVectorGenerator gener;
    private NormalizedRandomGenerator randomD;
    private BlockRealMatrix corrMatrix;
    
    private double[][] corrM;
    
    public GenerateRanCorrVar_apache(double[][] corrM, NormalizedRandomGenerator gen){
        nrNumbers = corrM.length;
        genRanNumbers = new double[nrNumbers];
        
        corrMatrix = new BlockRealMatrix(corrM);
        
        double[] mean = new double[corrM.length];
        for(int i = 0; i < mean.length; i++){
            mean[i] = 0.5;        
        }
        randomD = gen;
        gener = new CorrelatedRandomVectorGenerator(corrMatrix,0,randomD);
        
        this.corrM = corrM;
        //transMatrix = computeFactor(corrMatrix);
    }
    
    
    @Override
    public double[] correlation(double[] inputs, double[][] correlation, DiscreteDistribution distr){
        double[] tempout = gener.nextVector();
        while(true){
            boolean check = true;
            for(int i = 0; i < tempout.length; i++){
                tempout[i] = (tempout[i] + Math.sqrt(3))/(2 * Math.sqrt(3));
                if(tempout[i] > 1 || tempout[i] < 0){
                    check = false;
                    break;
                }
            }
            if(!check) {
                tempout = gener.nextVector();
            }
            else {
                break;
            }
        }
        return tempout;
    }
    
    @Override
    public double[] correlation(double[] inputs, DiscreteDistribution distr){
         return correlation(inputs,null,distr);
    }

    double[] normV;
    @Override
    public double[] normValues(){
        //??
        return null;
    }
    
    @Override
    public double[][] getCorrMatrix(){
        return corrM;
    }

    @Override
    public double[][] getTransfMatrix(){
        return null;
    }
}
