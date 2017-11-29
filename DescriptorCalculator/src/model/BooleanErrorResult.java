package model;

import org.openscience.cdk.qsar.result.BooleanResultType;

/**
 * ErrorResult for BooleanResult
 * 
 * @author Felix Bänsch, 07.06.2016
 */
public class BooleanErrorResult extends BooleanResultType implements IErrorResult<String>{
    
    /**
     * Gets the value
     * @return value String
     */
    @Override
    public String getValue() {
        return "error";
    }
    
    @Override
    public String toString(){
        return "error";
    }
    
}
