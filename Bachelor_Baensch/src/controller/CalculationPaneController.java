package controller;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Calculator;
import model.Descriptors;
import model.Data;
import model.DescriptorModel;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.IDescriptor;
import util.Dialog;
import util.Globals;
import util.PropertiesEnum;
import util.PropertiesLoader;
import util.UtilityMethods;
import view.CalculationPane;
//</editor-fold>

/**
 * Controller class for the CalculationPane
 *
 * @author Felix Bänsch, 29.05.2016
 */
public class CalculationPaneController {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private CalculationPane calculationPane;    //Pane for the descriptor calculation
    private Stage calculationPaneStage;         //Stage for the calculationPane
    private Descriptors descriptors;            //Administres the descriptors
    private Node[] parameterEditorNodes;        //Node for the descriptorparameters 
    private Data data;                          //Instance for the molecule data
    private Stage mainStage;                    //Main stage
    private Scene scene;                        //Scene for the calculation pane
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new controller for the CalculationPane
     *
     * @param aStage Stage
     * @param aCalculationPane CalculationPane
     * @param aData Data
     * @param aDescriptors Descriptors
     */
    public CalculationPaneController(Stage aStage, CalculationPane aCalculationPane, Data aData, Descriptors aDescriptors) {
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aDescriptors == null) {
            throw new IllegalArgumentException("aCalculator must not be null");
        }
        if (aData == null) {
            throw new IllegalArgumentException("aData must not be null");
        }
        //</editor-fold>
        this.mainStage = aStage;
        this.data = aData;
        this.descriptors = aDescriptors;
        this.calculationPane = aCalculationPane;
        this.calculationPane.setMinSize(Globals.moleculePaneWidth, Globals.moleculePaneHeight);
        this.calculationPane.setPrefSize(Globals.moleculePaneWidth, Globals.moleculePaneHeight);
//        this.calculationPane.setMaxSize(Globals.moleculePaneWidth, Globals.moleculePaneHeight);
        this.calculationPane.bindSize();
        this.scene = new Scene(this.calculationPane, Globals.moleculePaneWidth, Globals.moleculePaneHeight);
        //TODO: minsize
        this.calculationPaneStage = new Stage();
        this.calculationPaneStage.setScene(this.scene);
        this.calculationPaneStage.setTitle(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.calculationTitle.toString()));
        this.calculationPaneStage.initModality(Modality.WINDOW_MODAL);
        this.calculationPaneStage.initOwner(this.mainStage);
        this.calculationPaneStage.show();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="setContent">
    /**
     * Sets the content of the calculationPane
     */
    public void setContent() {
        this.addListener();
        this.setItemsToLists();
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">
    //<editor-fold defaultstate="collapsed" desc="addListener">
    /**
     * Adds Listeners and EventHandlers to the CalculationPane
     */
    private void addListener() {
        //<editor-fold defaultstate="collapsed" desc="closeButton">
        /**
         * Adds an EventHandler to the CloseButton which closes this stage
         */
        this.calculationPane.getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CalculationPaneController.this.calculationPaneStage.close();
                for (IDescriptor tmpDescriptor : CalculationPaneController.this.descriptors.getDescriptorList()) {
                    CalculationPaneController.this.descriptors.setCheck(tmpDescriptor, CalculationPaneController.this.calculationPane.getCheckAll().isSelected());
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="calculationButton">
        /**
         * Adds an EventHandler to the CalculationButton which starts the
         * descriptor calculation
         */
        this.calculationPane.getCalculationButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (CalculationPaneController.this.descriptors.getSelectedDescriptors().size() > 0) {                    
                    Task tmpTask = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            CalculationPaneController.this.calculationPane.getProgressBar().setVisible(true);
                            CalculationPaneController.this.calculationPaneStage.getScene().setCursor(Cursor.WAIT);
                            CalculationPaneController.this.calculationPane.getCloseButton().setDisable(true);
                            CalculationPaneController.this.calculationPane.getCalculationButton().setDisable(true);
                            Calculator tmpCalculator = new Calculator(CalculationPaneController.this.data);
                            tmpCalculator.calculateDescriptors(CalculationPaneController.this.descriptors.getSelectedDescriptors());
                            CalculationPaneController.this.calculationPane.getCalculationButton().setDisable(false);
                            CalculationPaneController.this.calculationPane.getCloseButton().setDisable(false);
                            CalculationPaneController.this.calculationPane.getProgressBar().setVisible(false);
                            CalculationPaneController.this.calculationPaneStage.getScene().setCursor(Cursor.DEFAULT);
                            return null;
                        }
                    };
                    CalculationPaneController.this.calculationPane.getProgressBar().progressProperty().
                            bind(tmpTask.progressProperty());
                    Thread tmpThread = new Thread(tmpTask);
                    tmpThread.start();
                } else {
                    Dialog tmpDialog = new Dialog("Error", null, "You must at least check one descriptor", Alert.AlertType.ERROR);
                    tmpDialog.showAndWait();
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="defaultButton">
        /**
         * Empty
         */
        this.calculationPane.getDefaultButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO
//                for (IDescriptor tmpDescriptor : CalculationPaneController.this.descriptors.getDescriptorList()) {

//                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="saveDescriptorsButton">
        /**
         * Opens a FileChooser and writes the selected descriptors to the
         * selected file
         */
        this.calculationPane.getSaveDescriptorsButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                Chooser tmpChooser = new Chooser();
//                File tmpSaveFile = tmpChooser.saveFile(CalculationPaneController.this.calculationPaneStage, "csv");
                UtilityMethods.writeDescriptors(CalculationPaneController.this.descriptors.getSelectedDescriptors());
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="loadDescriptorsButton">
        /**
         * Opens a FileChooser, loads the selected CSV file and adds the
         * parameters
         */
        this.calculationPane.getLoadDescriptorsButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                Chooser tmpChooser = new Chooser();
//                File tmpLoadFile = tmpChooser.chooseCsvFile(CalculationPaneController.this.calculationPaneStage);
                List<IDescriptor> tmpList = UtilityMethods.readDescriptors(CalculationPaneController.this.descriptors.getDescriptorList());
                CalculationPaneController.this.descriptors.setLoadedDescriptors(tmpList);
                CalculationPaneController.this.setItemsToLists();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="checkAllCheckBox">
        /**
         * Checks or unchecks all descriptors
         */
        this.calculationPane.getCheckAll().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (IDescriptor tmpDescriptor : CalculationPaneController.this.descriptors.getDescriptorList()) {
                    CalculationPaneController.this.descriptors.setCheck(tmpDescriptor, CalculationPaneController.this.calculationPane.getCheckAll().isSelected());
                }
                CalculationPaneController.this.setItemsToLists();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="listViews">
        /**
         * ChangeListeners for the ListViews Shows the description of the
         * selected descriptor
         */
        //atomicListView
        this.calculationPane.getAtomicListView().getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<DescriptorModel>() {
                    @Override
                    public void changed(ObservableValue<? extends DescriptorModel> observable, DescriptorModel oldValue, DescriptorModel newValue) {
                        if (newValue != null) {
                            if (!newValue.equals(oldValue)) {
                                CalculationPaneController.this.setDescriptionText(newValue.getDescriptor());
                                CalculationPaneController.this.setParameterEditor(newValue.getDescriptor());
                            }
                        }
                    }
                });
        //atomPairListView
        this.calculationPane.getAtomPairListView().getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<DescriptorModel>() {
                    @Override
                    public void changed(ObservableValue<? extends DescriptorModel> observable, DescriptorModel oldValue, DescriptorModel newValue) {
                        if (newValue != null) {
                            if (!newValue.equals(oldValue)) {
                                CalculationPaneController.this.setDescriptionText(newValue.getDescriptor());
                                CalculationPaneController.this.setParameterEditor(newValue.getDescriptor());
                            }
                        }
                    }
                });
        //bondListView
        this.calculationPane.getBondListView().getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<DescriptorModel>() {
                    @Override
                    public void changed(ObservableValue<? extends DescriptorModel> observable, DescriptorModel oldValue, DescriptorModel newValue) {
                        if (newValue != null) {
                            if (!newValue.equals(oldValue)) {
                                CalculationPaneController.this.setDescriptionText(newValue.getDescriptor());
                                CalculationPaneController.this.setParameterEditor(newValue.getDescriptor());
                            }
                        }
                    }
                });
        //molecularListView
        this.calculationPane.getMolecularListView().getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<DescriptorModel>() {
                    @Override
                    public void changed(ObservableValue<? extends DescriptorModel> observable, DescriptorModel oldValue, DescriptorModel newValue) {
                        if (newValue != null) {
                            if (!newValue.equals(oldValue)) {
                                CalculationPaneController.this.setDescriptionText(newValue.getDescriptor());
                                CalculationPaneController.this.setParameterEditor(newValue.getDescriptor());
                            }
                        }
                    }
                });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="TabContextMenus">
        /**
         * EventHandlers for the TabContextMenuItems They check or uncheck all
         * descriptors of the their tab
         */
        //atomicTab
        this.calculationPane.getAtomicCheckMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (CalculationPaneController.this.calculationPane.getTabPane().getTabs().get(0).isSelected()) {
                    for (IDescriptor tmpDescriptor : CalculationPaneController.this.descriptors.getDescriptorList()) {
                        if (UtilityMethods.isDescriptorTypeOf(tmpDescriptor, CalculationPaneController.this.calculationPane.getTabPane().getSelectionModel().getSelectedItem().getText().toLowerCase())) {
                            CalculationPaneController.this.descriptors.setCheck(tmpDescriptor, CalculationPaneController.this.calculationPane.getAtomicCheckMenuItem().isSelected());
                        }
                    }
                    CalculationPaneController.this.setItemsToLists();
                } else {
                    CalculationPaneController.this.calculationPane.getAtomicCheckMenuItem().setSelected(false);
                }
            }
        });
        //atmoPairTab
        this.calculationPane.getAtomPairCheckMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (CalculationPaneController.this.calculationPane.getTabPane().getTabs().get(1).isSelected()) {
                    for (IDescriptor tmpDescriptor : CalculationPaneController.this.descriptors.getDescriptorList()) {
                        if (UtilityMethods.isDescriptorTypeOf(tmpDescriptor, CalculationPaneController.this.calculationPane.getTabPane().getSelectionModel().getSelectedItem().getText().toLowerCase())) {
                            CalculationPaneController.this.descriptors.setCheck(tmpDescriptor, CalculationPaneController.this.calculationPane.getAtomPairCheckMenuItem().isSelected());
                        }
                    }
                    CalculationPaneController.this.setItemsToLists();
                } else {
                    CalculationPaneController.this.calculationPane.getAtomPairCheckMenuItem().setSelected(false);
                }
            }
        });
        //bondTab
        this.calculationPane.getBondCheckMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (CalculationPaneController.this.calculationPane.getTabPane().getTabs().get(2).isSelected()) {
                    for (IDescriptor tmpDescriptor : CalculationPaneController.this.descriptors.getDescriptorList()) {
                        if (UtilityMethods.isDescriptorTypeOf(tmpDescriptor, CalculationPaneController.this.calculationPane.getTabPane().getSelectionModel().getSelectedItem().getText().toLowerCase())) {
                            CalculationPaneController.this.descriptors.setCheck(tmpDescriptor, CalculationPaneController.this.calculationPane.getBondCheckMenuItem().isSelected());
                        }
                    }
                    CalculationPaneController.this.setItemsToLists();
                } else {
                    CalculationPaneController.this.calculationPane.getBondCheckMenuItem().setSelected(false);
                }
            }
        });
        //molecularTab
        this.calculationPane.getMolecularCheckMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (CalculationPaneController.this.calculationPane.getTabPane().getTabs().get(3).isSelected()) {
                    for (IDescriptor tmpDescriptor : CalculationPaneController.this.descriptors.getDescriptorList()) {
                        if (UtilityMethods.isDescriptorTypeOf(tmpDescriptor, CalculationPaneController.this.calculationPane.getTabPane().getSelectionModel().getSelectedItem().getText().toLowerCase())) {
                            CalculationPaneController.this.descriptors.setCheck(tmpDescriptor, CalculationPaneController.this.calculationPane.getMolecularCheckMenuItem().isSelected());
                        }
                    }
                    CalculationPaneController.this.setItemsToLists();
                } else {
                    CalculationPaneController.this.calculationPane.getMolecularCheckMenuItem().setSelected(false);
                }
            }
        });
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setItemsToLists">
    /**
     * Adds items to the listViews of the calculationPane
     */
    private void setItemsToLists() {
        //atomic
        this.calculationPane.getAtomicListView().setItems(this.descriptors.createList(Globals.atomic));
        this.calculationPane.getAtomicListView().setCellFactory(CheckBoxListCell.forListView(new Callback<DescriptorModel, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(DescriptorModel param) {
                param.getCheck().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        CalculationPaneController.this.descriptors.setCheck(param.getDescriptor(), newValue);
                    }
                });
                return param.getCheck();
            }
        }));
        //atomPair
        this.calculationPane.getAtomPairListView().setItems(this.descriptors.createList(Globals.atomPair));
        this.calculationPane.getAtomPairListView().setCellFactory(CheckBoxListCell.forListView(new Callback<DescriptorModel, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(DescriptorModel param) {
                param.getCheck().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        CalculationPaneController.this.descriptors.setCheck(param.getDescriptor(), newValue);
                    }
                });
                return param.getCheck();
            }
        }));
        //bond
        this.calculationPane.getBondListView().setItems(this.descriptors.createList(Globals.bond));
        this.calculationPane.getBondListView().setCellFactory(CheckBoxListCell.forListView(new Callback<DescriptorModel, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(DescriptorModel param) {
                param.getCheck().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        CalculationPaneController.this.descriptors.setCheck(param.getDescriptor(), newValue);
                    }
                });
                return param.getCheck();
            }
        }));
        //molecular
        this.calculationPane.getMolecularListView().setItems(this.descriptors.createList(Globals.molecular));
        this.calculationPane.getMolecularListView().setCellFactory(CheckBoxListCell.forListView(new Callback<DescriptorModel, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(DescriptorModel param) {
                param.getCheck().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        CalculationPaneController.this.descriptors.setCheck(param.getDescriptor(), newValue);
                    }
                });
                return param.getCheck();
            }
        }));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setDescriptionText">
    /**
     * Sets the description text to the descriptionTextArea
     *
     * @param aDescriptor IDescriptor
     */
    private void setDescriptionText(IDescriptor aDescriptor) {
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aDescriptor == null) {
            throw new IllegalArgumentException("aDescriptor must not be null");
        }
        //</editor-fold>
        String tmpDefinition = UtilityMethods.getDescriptorDefinition((DescriptorSpecification) aDescriptor.getSpecification());
        CalculationPaneController.this.calculationPane.getDescriptionTextArea().setText(tmpDefinition);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setParameterEditor">
    /**
     * Sets the parameters to the CalculationPane
     *
     * @param aDescriptor IDescriptor
     */
    private void setParameterEditor(IDescriptor aDescriptor) {
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aDescriptor == null) {
            throw new IllegalArgumentException("aDescriptor must not be null");
        }
        this.calculationPane.getParameterPane().getChildren().clear();
        //</editor-fold>
        if (UtilityMethods.hasParameters(aDescriptor)) {
            VBox tmpVBox = new VBox();
            tmpVBox.setSpacing(Globals.containerSpacing);
//            HBox tmpHBox = new HBox();
//            tmpHBox.setSpacing(Globals.containerSpacing);
            final Object[] tmpDefaultParameters = aDescriptor.getParameters();
            for (int i = 0; i < tmpDefaultParameters.length; i++) {
                HBox tmpHBox = new HBox();
                tmpHBox.setSpacing(Globals.containerSpacing);
                String tmpParameterName = aDescriptor.getParameterNames()[i];
                Object tmpParameterType = aDescriptor.getParameterType(tmpParameterName);
                Object tmpDefault = aDescriptor.getParameters()[i];
                this.parameterEditorNodes = new Node[aDescriptor.getParameters().length];
                ComboBox tmpComboBox = new ComboBox();
                TextField tmpTextField = new TextField();
                tmpTextField.setPrefWidth(50.0);
                if (tmpParameterType instanceof Boolean) {
                    tmpComboBox.getItems().add(Boolean.FALSE);
                    tmpComboBox.getItems().add(Boolean.TRUE);
                    if ((Boolean) tmpDefault) {
                        tmpComboBox.getSelectionModel().select(Boolean.TRUE);
                    } else {
                        tmpComboBox.getSelectionModel().select(Boolean.FALSE);
                    }
                    int tmpIndex = i;
                    tmpComboBox.valueProperty().addListener(new ChangeListener() {
                        @Override
                        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                            Object[] tmpChangeParameters = tmpDefaultParameters;
                            tmpChangeParameters[tmpIndex] = (Boolean) newValue;
                            try {
                                aDescriptor.setParameters(tmpChangeParameters);
                            } catch (CDKException ex) {
                                System.err.println(ex);
//                                Logger.getLogger(CalculationPaneController.class.getName()).log(Level.SEVERE, null, ex);//TODO
                            }
                        }
                    });
                    this.parameterEditorNodes[i] = tmpComboBox;
                } else {
                    String tmpDefaultString = tmpDefault.toString();
                    tmpTextField.setText(tmpDefaultString);
                    int tmpIndex = i;
                    tmpTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent tmpEvent) {
                            if (tmpEvent.getCode() == KeyCode.ENTER) {
                                String tmpNewString = tmpTextField.getText();
                                Object tmpChangeParameter = null;
                                if (tmpParameterType instanceof Integer) {
                                    try {
                                        tmpChangeParameter = Integer.parseInt(tmpNewString);
                                    } catch (NumberFormatException ex) {
                                        tmpTextField.setText(tmpDefaultString);
                                        Dialog tmpDialog = new Dialog("Error", ex.toString(),
                                                "Could not parse input", Alert.AlertType.ERROR);
                                        tmpDialog.showAndWait();
                                        //TODO: ERROR Dialog
                                    }
                                } else if (tmpParameterType instanceof Double) {
                                    try {
                                        tmpChangeParameter = Double.parseDouble(tmpNewString);
                                    } catch (NumberFormatException ex) {
                                        tmpTextField.setText(tmpDefaultString);
                                        Dialog tmpDialog = new Dialog("Error", ex.toString(),
                                                "Could not parse input", Alert.AlertType.ERROR);
                                        tmpDialog.showAndWait();
                                    }
                                } else if (tmpParameterType instanceof String) {
                                    tmpChangeParameter = tmpNewString;
                                } else {
                                    throw new RuntimeException("Unsupported ParameterType: " + tmpParameterType);
                                }
                                if (tmpChangeParameter != null) {
                                    Object[] tmpChangedParameters = tmpDefaultParameters;
                                    tmpChangedParameters[tmpIndex] = tmpChangeParameter;
                                    try {
                                        aDescriptor.setParameters(tmpChangedParameters);
                                    } catch (CDKException ex) {
//                                        Logger.getLogger(CalculationPaneController.class.getName()).log(Level.SEVERE, null, ex);//TODO
                                        Dialog tmpDialog = new Dialog("Error", ex.toString(),
                                                "Could not set parameter", Alert.AlertType.ERROR);
                                        tmpDialog.showAndWait();
                                    }
                                }
                            }
                        }
                    });
                    this.parameterEditorNodes[i] = tmpTextField;
                }
                Pane tmpGapPane1 = new Pane();
                HBox.setHgrow(tmpGapPane1, Priority.ALWAYS);
                Pane tmpGapPane2 = new Pane();
                HBox.setHgrow(tmpGapPane2, Priority.ALWAYS);
                Button tmpDefaultButton = new Button();
                tmpDefaultButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.Default.toString()));
                Tooltip tmpTip = new Tooltip();
                tmpTip.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.DefaultButtonTip.toString()));
                tmpDefaultButton.setTooltip(tmpTip);
                tmpDefaultButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            aDescriptor.setParameters(tmpDefaultParameters);
                            if (tmpParameterType instanceof Boolean) {
                                tmpComboBox.getSelectionModel().select((Boolean) tmpDefault);
                            } else {
                                tmpTextField.setText(tmpDefault.toString());
                            }
                        } catch (CDKException ex) {
//                            Logger.getLogger(CalculationPaneController.class.getName()).log(Level.SEVERE, null, ex); //TODO
                            Dialog tmpDialog = new Dialog("Error", ex.toString(),
                                    "Could not set parameter to default", Alert.AlertType.ERROR);
                            tmpDialog.showAndWait();
                        }
                    }
                });
                tmpHBox.getChildren().add(new Label(tmpParameterName));
                tmpHBox.getChildren().add(tmpGapPane1);
                tmpHBox.getChildren().add(this.parameterEditorNodes[i]);
                tmpHBox.getChildren().add(tmpGapPane2);
                tmpHBox.getChildren().add(tmpDefaultButton);
                tmpVBox.getChildren().add(tmpHBox);
            }
            this.calculationPane.getParameterPane().getChildren().add(tmpVBox);
        }
    }
    //</editor-fold>
    //</editor-fold>
}
