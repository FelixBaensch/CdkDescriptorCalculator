package controller;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.Map;
import java.util.Map.Entry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Data;
import model.Identifier;
import org.openscience.cdk.qsar.IDescriptor;
import util.Globals;
import util.UtilityMethods;
import view.MoleculePane;
//</editor-fold>

/**
 * Controller class for the MoleculePane
 *
 * @author Felix Bänsch, 19.05.2016
 */
public class MoleculePaneController {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private MoleculePane moleculePane;      //Pane for the molecule information window
    private Stage moleculePaneStage;        //Stage for the molecuelPane
    private Data data;                      //Instance for the molecule data
    private int index;                      //int for the internal index of the selected molecule
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new controller for the MoleculePane
     *
     * @param aStage Stage
     * @param aData Data
     * @param anIdentifier Identifier   
     * @param aMoleculePane MoleculePane
     */
    public MoleculePaneController(Stage aStage, Data aData, Identifier anIdentifier,
            MoleculePane aMoleculePane) {
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aData == null) {
            throw new IllegalArgumentException("aData must not be null");
        }
        //</editor-fold>
        this.data = aData;
        this.index = Integer.parseInt(anIdentifier.toString("Counter"));
        this.moleculePane = aMoleculePane;
//        this.moleculePane.setMinSize(Globals.moleculePaneWidth, Globals.moleculePaneHeight);
//        this.moleculePane.setPrefSize(Globals.moleculePaneWidth, Globals.moleculePaneHeight);
//        this.moleculePane.setMaxSize(Globals.moleculePaneWidth, Globals.moleculePaneHeight);
        this.moleculePane.bindSize();
        Scene tmpScene = new Scene(this.moleculePane, Globals.moleculePaneWidth, Globals.moleculePaneHeight);
        this.moleculePane.getStylesheets().add("/view/MoleculePaneStyle.css");
        this.moleculePaneStage = new Stage();
        this.moleculePaneStage.setScene(tmpScene);
        this.moleculePaneStage.setTitle(this.data.getMolecule(this.index).getName());
        this.moleculePaneStage.initModality(Modality.WINDOW_MODAL);
        this.moleculePaneStage.initOwner(aStage);
        this.moleculePaneStage.setMinWidth(Globals.moleculePaneWidth);
        this.moleculePaneStage.setMinHeight(Globals.moleculePaneHeight);
//        this.moleculePaneStage.setResizable(false);
        this.moleculePaneStage.show();
        
        moleculePane.getTabPane().setMaxHeight(moleculePane.getDesAnchorPane().getHeight());
        moleculePane.getTabPane().setMinHeight(moleculePane.getDesAnchorPane().getHeight());
        moleculePane.getDesAnchorPane().setMinHeight(moleculePane.getDesTitledPane().getHeight() - Globals.headerHeight);
        moleculePane.getDesAnchorPane().setMaxHeight(moleculePane.getDesTitledPane().getHeight() - Globals.headerHeight);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="setContent">
    /**
     * Sets content to the moleculePane
     */
    public void setContent() {
        this.setStructure();
        this.setInfoContent();
        this.setPropetiesContent();
        this.setDescriptorContent();
        this.addListener();
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">
    //<editor-fold defaultstate="collapsed" desc="setInfoContent">
    /**
     * Sets content to the infoListView
     */
    private void setInfoContent() {
        ObservableList<String> tmpInfoList = FXCollections.observableArrayList();
        double tmpMass = this.data.getMolecule(this.index).getMass();
        int tmpAtomCount = this.data.getMolecule(this.index).getAtomCount();
        int tmpBondCount = this.data.getMolecule(this.index).getBondCount();
        int tmpCharge = this.data.getMolecule(this.index).getCharge();
        tmpInfoList.add("ID:\t" + this.data.getMolecule(this.index).getIdentifier().toString("Counter"));
        tmpInfoList.add("Molecular Mass:\t" + String.valueOf(tmpMass));
        tmpInfoList.add("Atoms:\t" + String.valueOf(tmpAtomCount));
        tmpInfoList.add("Bonds:\t" + String.valueOf(tmpBondCount));
        tmpInfoList.add("Charge:\t" + String.valueOf(tmpCharge));
        tmpInfoList.add("Check:\t" + this.data.getMolecule(this.index).isChecked());
        this.moleculePane.getInfoListView().setItems(tmpInfoList);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setPropertiesContent">
    /**
     * Sets content to the propertiesListView
     */
    private void setPropetiesContent() {
        ObservableList<String> tmpPropertiesList = FXCollections.observableArrayList();
        Map<Object, Object> tmpPropertiesMap = this.data.getMolecule(this.index).getProperties();
        String tmpKey;
        String tmpValue;
        for (Entry<Object, Object> tmpEntry : tmpPropertiesMap.entrySet()) {
            tmpKey = tmpEntry.getKey().toString();
            if (tmpEntry.getValue() != null) {
                tmpValue = tmpEntry.getValue().toString();
            } else {
                tmpValue = "no value found";
            }
            tmpPropertiesList.add(UtilityMethods.getOnlyAscii(tmpKey + ":\t" + tmpValue));
        }
        this.moleculePane.getPropertiesListView().setItems(tmpPropertiesList);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setDescriptorContent">
    /**
     * Sets content to the descriptorListView
     */
    private void setDescriptorContent() {                
        Map<Object, Object> tmpResultMap = this.data.getMolecule(this.index).getResultsMap();
        String tmpName;
        String tmpValue;
        //atomic
        ObservableList<String> tmpAtomicList = FXCollections.observableArrayList();
        for(Entry<Object, Object> tmpEntry : tmpResultMap.entrySet()){
            if(UtilityMethods.isDescriptorTypeOf((IDescriptor) tmpEntry.getKey(), Globals.atomic)){
                tmpName = UtilityMethods.getDescriptorName((IDescriptor) tmpEntry.getKey());
                tmpName = UtilityMethods.getOnlyAscii(tmpName);
                if(tmpEntry.getValue() != null){
                    tmpValue = tmpEntry.getValue().toString();
                    tmpValue = UtilityMethods.getOnlyAscii(tmpValue);
//                    tmpValue = UtilityMethods.cutOfStringValue(tmpValue);
                } else {
                    tmpValue  = "NaN";
                }
                tmpAtomicList.add(tmpName + ": \t" + tmpValue);
            }            
        }
        if(tmpAtomicList.isEmpty()){
            tmpAtomicList.add("no descriptor value found");
        }
        this.moleculePane.getAtomicListView().setItems(tmpAtomicList);
        //atomPair
        ObservableList<String> tmpAtomPairList = FXCollections.observableArrayList();
        tmpAtomPairList.clear();
        for(Entry<Object, Object> tmpEntry : tmpResultMap.entrySet()){
            if(UtilityMethods.isDescriptorTypeOf((IDescriptor) tmpEntry.getKey(), Globals.atomPair)){
                tmpName = UtilityMethods.getDescriptorName((IDescriptor) tmpEntry.getKey());
                tmpName = UtilityMethods.getOnlyAscii(tmpName);
                if(tmpEntry.getValue() != null){
                    tmpValue = tmpEntry.getValue().toString();
                    tmpValue = UtilityMethods.getOnlyAscii(tmpValue);
//                    tmpValue = UtilityMethods.cutOfStringValue(tmpValue);
                } else {
                    tmpValue  = "Nan";
                }
                tmpAtomPairList.add(tmpName + ": \t" + tmpValue);
            }
        }
        if(tmpAtomPairList.isEmpty()){
            tmpAtomPairList.add("no descriptor value found");
        }
        this.moleculePane.getAtomPairListView().setItems(tmpAtomPairList);
        //bond
        ObservableList<String> tmpBondList = FXCollections.observableArrayList();
        for(Entry<Object, Object> tmpEntry : tmpResultMap.entrySet()){
            if(UtilityMethods.isDescriptorTypeOf((IDescriptor) tmpEntry.getKey(), Globals.bond)){
                tmpName = UtilityMethods.getDescriptorName((IDescriptor) tmpEntry.getKey());
                tmpName = UtilityMethods.getOnlyAscii(tmpName);
                if(tmpEntry.getValue() != null){
                    tmpValue = tmpEntry.getValue().toString();
                    tmpValue = UtilityMethods.getOnlyAscii(tmpValue);
//                    tmpValue = UtilityMethods.cutOfStringValue(tmpValue);
                } else {
                    tmpValue  = "NaN";
                }
                tmpBondList.add(tmpName + ": \t" + tmpValue);
            }
        }
        if(tmpBondList.isEmpty()){
            tmpBondList.add("no descriptor value found");
        }
        this.moleculePane.getBondListView().setItems(tmpBondList);
        //molecular
        ObservableList<String> tmpMolecularList = FXCollections.observableArrayList();
        for(Entry<Object, Object> tmpEntry : tmpResultMap.entrySet()){
            if(UtilityMethods.isDescriptorTypeOf((IDescriptor) tmpEntry.getKey(), Globals.molecular)){
                tmpName = UtilityMethods.getDescriptorName((IDescriptor) tmpEntry.getKey());
                tmpName = UtilityMethods.getOnlyAscii(tmpName);
                if(tmpEntry.getValue() != null){
                    tmpValue = tmpEntry.getValue().toString();
                    tmpValue = UtilityMethods.getOnlyAscii(tmpValue);
//                    tmpValue = UtilityMethods.cutOfStringValue(tmpValue);
                } else {
                    tmpValue  = "NaN";
                }
                tmpMolecularList.add(tmpName + ": \t" + tmpValue);
            }
        }
        if(tmpMolecularList.isEmpty()){
            tmpMolecularList.add("no descriptor value found");
        }
        this.moleculePane.getMolecularListView().setItems(tmpMolecularList);        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setStructure">
    /**
     * Sets an image to the structureAnchorPane
     */
    private void setStructure() {
        int tmpWidth = (int) (this.moleculePane.getStructurePane().getWidth() - 2 * Globals.mainPaneInsets);
        int tmpHeight = (int) (this.moleculePane.getStructurePane().getHeight() - 2 * Globals.mainPaneInsets);
        ImageView tmpImageView = new ImageView();
        tmpImageView.setImage(this.data.getMolecule(this.index).createImage(tmpWidth, tmpHeight));
        this.moleculePane.getStructurePane().getChildren().add(tmpImageView);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addListener">
    /**
     * Adds Listeners and EventHandlers to the MoleculePane
     */
    private void addListener() {
        //<editor-fold defaultstate="collapsed" desc="closeButton">
        /**
         * Adds an EventHandler to the CloseButton which closes this stage
         */
        this.moleculePane.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MoleculePaneController.this.moleculePaneStage.close();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="width ChangeListener">
        /**
         * ChangeListener for the width, sets new image
         */
        this.moleculePane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                MoleculePaneController.this.setStructure();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="height ChangeListener">
        /**
         * ChangeListener for the height, sets new image
         */
        this.moleculePane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                MoleculePaneController.this.setStructure();

                moleculePane.getTabPane().setMaxHeight(moleculePane.getDesAnchorPane().getHeight());
                moleculePane.getTabPane().setMinHeight(moleculePane.getDesAnchorPane().getHeight());
                moleculePane.getDesAnchorPane().setMinHeight(moleculePane.getDesTitledPane().getHeight() - Globals.headerHeight);
                moleculePane.getDesAnchorPane().setMaxHeight(moleculePane.getDesTitledPane().getHeight() - Globals.headerHeight);
            }
        });
        //</editor-fold>
    }
    //</editor-fold>
    //</editor-fold>
}
