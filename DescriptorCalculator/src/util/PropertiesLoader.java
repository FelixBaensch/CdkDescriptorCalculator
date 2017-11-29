package util;

//<editor-fold defaultstate="collapsed" desc="import">
import java.io.IOException;
import java.util.Properties;
//</editor-fold>

/**
 * Singletonclass for the properties
 * 
 * @author Felix Bänsch, 01.06.2016
 */
public class PropertiesLoader {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private static PropertiesLoader instance;
    private Properties properties;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructs an new PropertiesInstance
     */
    protected PropertiesLoader() {
        this.instance = null;
        this.properties = new Properties();
        try {
            this.properties.load(getClass().getResourceAsStream("bachelor.properties"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="getInstance">
    /**
     * Gets the instance
     * @return Properties
     */
    public static PropertiesLoader getInstance() {
        if (PropertiesLoader.instance == null) {
            PropertiesLoader.instance = new PropertiesLoader();
        }
        return PropertiesLoader.instance;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getValue">
    /**
     * Gets the value for the given key
     * @param aKey String
     * @return Value String
     */
    public String getValue(String aKey){
        return this.properties.getProperty(aKey);
    }
    //</editor-fold>
    //</editor-fold>
}
