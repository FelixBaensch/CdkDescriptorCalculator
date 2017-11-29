package model;

import org.openscience.cdk.qsar.result.IDescriptorResult;

/**
 * Interface for DescriptorErrorResult
 * 
 * @author Felix Bänsch, 07.06.2016
 * @param <T> type
 */
public interface IErrorResult<T> extends IDescriptorResult {
    
    /**
     * Gets the value
     * @return T
     */
    T getValue();
    
    @Override
    String toString();
}
