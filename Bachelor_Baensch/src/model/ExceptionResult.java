package model;

/**
 * Exception Result 
 * 
 * @author Felix Bänsch. 23.06.2016
 */
public class ExceptionResult extends ExceptionResultType implements IErrorResult<String>{
    
    /**
     * Gets the value
     * @return value String
     */
    @Override
    public String getValue() {
        return "NaN";
    }
    
    @Override
    public String toString(){
        return "NaN";
    }
    
}
