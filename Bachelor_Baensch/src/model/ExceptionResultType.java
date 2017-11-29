package model;

import org.openscience.cdk.qsar.result.IDescriptorResult;

/**
 *  IDescriptorResult type for Exceptions
 * 
 * @author Felix Bänsch, 23.06.2016
 */
public class ExceptionResultType implements IDescriptorResult{

    @Override
    public String toString(){
        return "ExceptionResultType";
    }
    
    @Override
    public int length() {
        return 1;
    }
    
}
