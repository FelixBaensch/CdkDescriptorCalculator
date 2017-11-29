package model;

/**
 * Identifier class containig local an unique ID
 * @author Felix Bänsch, 17.04.2016
 */
public class Identifier {
    
//    public static final String UniqueId = "Unique";
//    public static final String LocalId = "Local";
    
    //<editor-fold defaultstate="collapsed" desc="private variables">
    private String counter;     //String for the counter (internal index) of the molecule
    private String uniqueId;    //String for the uniqueId which every molecule gets
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructs a new identifier with a local and an unique ID
     * @param aCounter String
     * @param aUniqueId String
     */
    public Identifier(String aCounter, String aUniqueId){
        //<editor-fold defaultstate="collapsed" desc="check">
        if(aCounter == null){
            throw new NullPointerException("aCounter must not be null");
        }
        if(aUniqueId == null){
            throw new NullPointerException("aUniqueId must not be null");
        }
        //</editor-fold>
        this.counter = aCounter;
        this.uniqueId = aUniqueId;
    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="toString">
    /**
     * Returns the local or unique ID as String
     * @param aType String
     * @return ID String
     */
    public String toString(String aType){
        switch(aType){
            case "Unique":
                return this.uniqueId.toString();
            case "Counter":
                return this.counter.toString();
        }
        return "";
    }
    //</editor-fold>
}
