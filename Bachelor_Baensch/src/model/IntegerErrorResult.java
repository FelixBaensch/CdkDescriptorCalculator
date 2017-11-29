package model;

import org.openscience.cdk.qsar.result.IntegerResultType;

/**
 * ErrorResult for IntegerResult
 * 
 * @author Felix Bänsch, 07.06.2016
 */
public class IntegerErrorResult extends IntegerResultType implements IErrorResult<Integer>{
    
    /**
     * Gets the value
     * @return value Integer
     */
    @Override
    public Integer getValue(){
        return -1;
    }
    
    @Override
    public String toString(){
        return "-1";
    }
}
