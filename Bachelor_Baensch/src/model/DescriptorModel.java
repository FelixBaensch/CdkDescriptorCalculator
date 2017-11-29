package model;

//<editor-fold defaultstate="collapsed" desc="imports">
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.openscience.cdk.qsar.IDescriptor;
import util.UtilityMethods;
//</editor-fold>

/**
 * Modelclass for the ListViews of the CalculationPane
 *
 * @author Felix Bänsch, 31.05.2016
 */
public class DescriptorModel {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private BooleanProperty check = new SimpleBooleanProperty();        //BooleanProperty for the CheckBox of the ListView
    private StringProperty descriptorName = new SimpleStringProperty(); //StringProperty for the name of the descriptors
    private IDescriptor descriptor;                                     //Instance for the IDescriptor    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructs a new DescriptorModel
     * @param aDescriptor IDescriptor
     * @param aBoolean Boolean
     */
    public DescriptorModel(IDescriptor aDescriptor, Boolean aBoolean) {
        this.descriptor = aDescriptor;
        this.setCheck(aBoolean);
        this.setName(UtilityMethods.getDescriptorName(aDescriptor));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="getDescriptor">
    /**
     * Gets the descriptor
     *
     * @return IDescriptor
     */
    public IDescriptor getDescriptor() {
        return this.descriptor;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCheck">
    /**
     * Gets the check(boolean)
     * @return boolean
     */
    public BooleanProperty getCheck() {
        return this.check;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getName">
    /**
     * Gets the name
     *
     * @return String
     */
    public String getName() {
        return this.descriptorName.get();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setDescriptor">
    /**
     * Gets the descriptor
     * @param aDescriptor IDescriptor
     */
    public void setDescriptor(IDescriptor aDescriptor) {
        this.descriptor = aDescriptor;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setCheck">
    /**
     * Sets the check(boolean)
     *
     * @param aCheck boolean
     */
    public void setCheck(Boolean aCheck) {
        this.check.set(aCheck);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setName">
    /**
     * Sets the name
     *
     * @param aName String
     */
    public void setName(String aName) {
        this.descriptorName.set(aName);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="toString">
    /**
     * Gets the name as a String
     * @return String
     */
    @Override
    public String toString() {
        return this.getName();
    }
    //</editor-fold>
    //</editor-fold>
}
