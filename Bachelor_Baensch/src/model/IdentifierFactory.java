package model;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.UUID;
//</editor-fold>

/**
 * Creates new identifiers
 * 
 * @author Felix Bänsch, 17.04.2016
 */
public class IdentifierFactory {
    
    //<editor-fold defaultstate="collapsed" desc="private static variables">
    private static int atomContainerCount = 0; 
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * empty
     */
    public IdentifierFactory(){
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="createIdentifier">
    /**
     * Creates a new Identifier object, consists of a counter and an unique ID
     * @return tmpIdentifier
     */
    public synchronized static Identifier createIdentifier(){
        IdentifierFactory.atomContainerCount++;
        Identifier tmpIdentifier = new Identifier(String.valueOf(IdentifierFactory.atomContainerCount),
                UUID.randomUUID().toString().replaceAll("-", ""));
        return tmpIdentifier;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="clearCounter">
    /**
     * Sets the atomContainerCount to null
     */
    public static void clearCounter(){
        atomContainerCount = 0;
    }
    //</editor-fold>
//</editor-fold>
}
