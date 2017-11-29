package model;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.DescriptorEngine;
import org.openscience.cdk.qsar.IAtomPairDescriptor;
import org.openscience.cdk.qsar.IAtomicDescriptor;
import org.openscience.cdk.qsar.IBondDescriptor;
import org.openscience.cdk.qsar.IDescriptor;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import util.Globals;
import util.UtilityMethods;
//</editor-fold>

/**
 * Administres the calculation and all depending data
 *
 * @author Felix Bänsch, 30.05.2016
 */
public class Descriptors {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private List<IDescriptor> descriptorList;  //List for the IDescriptors
    private Boolean[] isCheckedArray;          //BooleanArray for the check-status of the IDescriptors 
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new Calculator
     */
    public Descriptors() {
        List<String> tmpList = new ArrayList<>();     
        tmpList.addAll(DescriptorEngine.getDescriptorClassNameByPackage("org.openscience.cdk.qsar.descriptors.atomic", null));
        tmpList.addAll(DescriptorEngine.getDescriptorClassNameByPackage("org.openscience.cdk.qsar.descriptors.atompair", null));
        tmpList.addAll(DescriptorEngine.getDescriptorClassNameByPackage("org.openscience.cdk.qsar.descriptors.bond", null));
        tmpList.addAll(DescriptorEngine.getDescriptorClassNameByPackage("org.openscience.cdk.qsar.descriptors.molecular", null));        
        List<String> tmpAsciiList = new ArrayList<>();
        tmpList.forEach(tmpEntry -> {
            tmpAsciiList.add(UtilityMethods.getOnlyAscii(tmpEntry));
        });               
        this.descriptorList = UtilityMethods.instantiateDescriptors(tmpAsciiList);
        this.isCheckedArray = new Boolean[this.descriptorList.size()];
        Arrays.fill(this.isCheckedArray, Boolean.FALSE);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="createList">
    /**
     * Creates an ObservableList for the CalculationPane ListViews depending on
     * the given String String aType could be atomic, atomPair, bond or
     * molecular, if it is null or a wrong type null will be returned
     *
     * @param aType String
     * @return ObservableList
     */
    public ObservableList<DescriptorModel> createList(String aType) {
        ObservableList<DescriptorModel> tmpList = FXCollections.observableArrayList();
        switch (aType) {
            case Globals.atomic:
                for (IDescriptor tmpDescriptor : this.descriptorList) {
                    if (tmpDescriptor instanceof IAtomicDescriptor) {
                        tmpList.add(new DescriptorModel(tmpDescriptor, this.isCheckedArray[this.descriptorList.indexOf(tmpDescriptor)]));
                    }
                }
                return tmpList;
            case Globals.atomPair:
                for (IDescriptor tmpDescriptor : this.descriptorList) {
                    if (tmpDescriptor instanceof IAtomPairDescriptor) {
                        tmpList.add(new DescriptorModel(tmpDescriptor, this.isCheckedArray[this.descriptorList.indexOf(tmpDescriptor)]));
                    }
                }
                return tmpList;
            case Globals.bond:
                for (IDescriptor tmpDescriptor : this.descriptorList) {
                    if (tmpDescriptor instanceof IBondDescriptor) {
                        tmpList.add(new DescriptorModel(tmpDescriptor, this.isCheckedArray[this.descriptorList.indexOf(tmpDescriptor)]));
                    }
                }
                return tmpList;
            case Globals.molecular:
                for (IDescriptor tmpDescriptor : this.descriptorList) {
                    if (tmpDescriptor instanceof IMolecularDescriptor) {
                        tmpList.add(new DescriptorModel(tmpDescriptor, this.isCheckedArray[this.descriptorList.indexOf(tmpDescriptor)]));
                    }
                }
                return tmpList;
            default:
                System.out.println("Wrong Descriptor type");
                return null;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setCheck">
    /**
     * Sets the Boolean at the index of the IDescriptor
     *
     * @param aDescriptor IDescriptor
     * @param aBoolean Boolean
     */
    public void setCheck(IDescriptor aDescriptor, Boolean aBoolean) {
        this.isCheckedArray[this.descriptorList.indexOf(aDescriptor)] = aBoolean;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="setLoadedDescriptors">
    /**
     * Sets parameters from loaded descriptors
     * @param aDescriptors List
     */
    public void setLoadedDescriptors(List<IDescriptor> aDescriptors) {
        for (int i = 0; i < this.descriptorList.size(); i++) {
            IDescriptor tmpDescriptor = this.descriptorList.get(i);
            this.setCheck(tmpDescriptor, Boolean.FALSE);
            for (IDescriptor tmpReadDescriptor : aDescriptors) {
                if (tmpDescriptor.getSpecification().getImplementationTitle().equals(tmpReadDescriptor.getSpecification().getImplementationTitle())) {
                    this.setCheck(tmpDescriptor, Boolean.TRUE);
                    if(UtilityMethods.hasParameters(tmpDescriptor)){
                        try{
                            tmpDescriptor.setParameters(tmpReadDescriptor.getParameters());
                        } catch (CDKException ex){
                            Logger.getLogger(Descriptors.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="getDescriptoList">

    /**
     * Gets the descriptorList
     *
     * @return List (descriptorList)
     */
    public List<IDescriptor> getDescriptorList() {
        return this.descriptorList;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getSelectedDescriptors">
    /**
     * Gets a list of the selected descriptors
     *
     * @return List
     */
    public List<IDescriptor> getSelectedDescriptors() {
        List<IDescriptor> tmpList = new ArrayList<>();
        for (int i = 0; i < this.isCheckedArray.length; i++) {
            if (Objects.equals(this.isCheckedArray[i], Boolean.TRUE)) {
                tmpList.add(this.descriptorList.get(i));
            }
        }
        return tmpList;
    }
    //</editor-fold>
    //</editor-fold>
}
