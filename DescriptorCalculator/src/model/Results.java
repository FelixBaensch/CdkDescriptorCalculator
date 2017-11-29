package model;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.LinkedList;
import java.util.List;
import org.openscience.cdk.qsar.result.IDescriptorResult;
//</editor-fold>

/**
 *
 * @author Felix Bänsch, 06.06.2016
 */
public class Results {
    
    //<editor-fold defaultstate="collapsed" desc="private variables">
    List<IDescriptorResult> resultList; //List which contains the IDescriptorResults
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new Results instance
     */
    public Results(){
        this.resultList = new LinkedList<>();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="addResult">
    /**
     * Adds a descriptor result to the result list
     * @param aResult IDescriptorResult
     */
    public void addResult(IDescriptorResult aResult){
        if(this.resultList == null){
            this.resultList = new LinkedList<>();
        }
        this.resultList.add(aResult);
    }
    //</editor-fold>
    
    public List<IDescriptorResult> getResults(){
        return this.resultList;
    }
}
