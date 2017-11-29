package model;

//<editor-fold defaultstate="collapsed" desc="imports">
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//</editor-fold>

/**
 * Utility calss to set up the TableView
 *
 * @author Felix Bänsch, 02.05.2016
 */
public class ModelTableView {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private ImageView imageView;        //ImageView for the molecule structure
    private Identifier identifier;      //Instance for the identifier of the molecules
    private StringProperty counter;     //StringProperty for the counter (internal index) of the molecules
    private StringProperty name;        //StringProperty for the names of the molecules
    private BooleanProperty check;      //BooleanProperty for the CheckBoxes in the ListView
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructs a new Model object
     * @param anIdentifier Identifier
     * @param aName String
     * @param anImage Image
     * @param aBoolean boolean
     */
    public ModelTableView(Identifier anIdentifier, String aName, Image anImage,
            Boolean aBoolean) {
        this.identifier = anIdentifier;
        this.counter = new SimpleStringProperty();
        this.setCounter(this.identifier.toString("Counter"));
        this.name = new SimpleStringProperty();
        this.setName(aName);
        this.imageView = new ImageView(anImage);
        this.check =  new SimpleBooleanProperty();
        this.setCheck(aBoolean);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="getIdentifier">
    /**
     * Gets the counter
     *
     * @return Identifier
     */
    public Identifier getIdentifier() {
        return this.identifier;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCounter">
    /**
     * Gets the counter as a String
     *
     * @return String
     */
    public String getCounter() {
        return this.counter.get();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getCounterProperty">
    /**
     * Gets the counterProperty
     * @return StringProperty
     */
    public StringProperty counterProperty(){
        return this.counter;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getName">
    /**
     * Gets the name
     *
     * @return name
     */
    public String getName() {
        return this.name.get();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getNameProperty">
    /**
     * Gets the nameProperty
     * @return StringProperty
     */
    public StringProperty nameProperty(){
        return this.name;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getImageView">
    /**
     * Gets the imageView
     *
     * @return imageView
     */
    public ImageView getImageView() {
        return this.imageView;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCheck">
    /**
     * Gets the check value
     * @return Boolean
     */
    public Boolean getCheck() {
        return this.check.get();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkProperty">
    /**
     * Gets the check
     * @return SimpelBooleanProperty
     */
    public BooleanProperty checkProperty(){
        return this.check;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setIdentifier">
    /**
     * Sets the counter
     * @param anIdentifier Identifier
     */
    public void setIdentifier(Identifier anIdentifier) {
        this.identifier = anIdentifier;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setName">
    /**
     * Sets the name
     * @param aName String
     */
    public void setName(String aName) {
        this.name.set(aName);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setImageView">
    /**
     * Sets the imageView
     *
     * @param anImageView ImageView
     */
    public void setImageView(ImageView anImageView) {
        this.imageView = anImageView;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setCounter">
    /**
     * Set the counter as a String
     *
     * @param aCounter String
     */
    public void setCounter(String aCounter) {
        this.counter.set(aCounter);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setCheck">
    /**
     * Sets the check
     *
     * @param aBoolean Boolean
     */
    public void setCheck(Boolean aBoolean) {
        this.check.set(aBoolean);
    }
    //</editor-fold>
    //</editor-fold>  
}
