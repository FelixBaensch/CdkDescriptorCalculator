package util;

/**
 * Enum class
 *
 * @author Felix Bänsch, 13.05.2016
 */
public class PropertiesEnum {

    //<editor-fold defaultstate="collapsed" desc="MainPaneEnum">
    /**
     * Enum for the mainPane
     */
    public enum MainPaneEnum {
        fileMenu, loadSdf, loadMol, loadSmile, close, editMenu,
        saveSdf, saveSmile, delete, checkAll, uncheckAll, toolMenu, calculation, 
        firstButton, prevButton, nextButton, lastButton, moleculesPerPage,
        deleteSelection, saveDescriptorsAll, saveDescriptorsSel, depictError,
        unnamed, saveSmileH, saveDescriptorAllAscii, saveDescriptorSelAscii
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MainTableEnum">
    /**
     * Enum for the mainTable
     */
    public enum MainTableEnum {
        checkHeader, counterHeader, structureHeader, nameHeader,
        showMenuItem, deleteMenuItem
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ControllerEnum">
    /**
     * Enum for the controllers
     */
    public enum ControllerEnum {
        deleteAllTitle, deleteAllHeader, deleteOneTitle, deleteOneHeader,
        selectionDialog, sdf, txt, csv, error, errormessage, ascii
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TooltipEnum">
    /**
     * Enum for the tooltips
     */
    public enum TooltipEnum {
        nextButtonTip, prevButtonTip, firstButtonTip, lastButtonTip,
        calculateButtonTip, cancelButtonTip, checkAllTip, pageNumberTextFieldTip,
        moleculesPerPageTextFieldTip, DefaultButtonTip, saveDescriptorsTip,
        loadDescriptorsTip
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MoleculePaneEnum">
    /**
     * Enum for the MoleculePane
     */
    public enum MoleculePaneEnum {
        structureHeader, infoHeader, propertiesHeader, descriptorHeader, 
        close
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="CalculationPaneEnum">
    /**
     * Enum for the CalculationPane
     */
    public enum CalculationPaneEnum{
        close, parameters, description, calculationTitle, atomic, atompair,
        bond, molecular, Default, checkDescriptors, calculate,
        saveDescriptors, loadDescriptors, noDescription
    }
    //</editor-fold>
}
