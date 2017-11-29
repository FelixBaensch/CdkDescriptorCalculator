package model;

import org.openscience.cdk.qsar.result.DoubleResultType;

/**
 * ErrorResult for DoubleResult
 * 
 * @author Felix Bänsch, 07.06.2016
 */
public class DoubleErrorResult extends DoubleResultType implements IErrorResult<Double>{
    
    /**
     * Gets the value
     * @return value double
     */
    @Override
    public Double getValue(){
        return -1.0;
    }
    
    @Override
    public String toString(){
        return "-1.0";
    }
}
