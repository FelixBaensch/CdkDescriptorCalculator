package controller;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import model.Descriptors;
import model.Data;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import util.Chooser;
import util.Dialog;
import util.Globals;
import util.PropertiesEnum;
import util.PropertiesLoader;
import util.UtilityMethods;
import view.CalculationPane;
import view.MainPane;
//</editor-fold>

/**
 * Controllerclass for the mainPane
 *
 * @author Felix Bänsch, 13.05.2016
 */
public class MainPaneController {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private MainPane mainPane;                                  //Pane for the main window
    private Chooser chooser;                                    //Instance for the file choosers
    private File dataFile;                                      //File for the molecule data
    private File saveFile;                                      //File for saving data
    private Stage primaryStage;                                 //Stage for the mainPane
    private Data data;                                          //Instance for the molecule data
    private int pageNumber;                                     //int for the page number
    private int moleculesPerPage;                               //int for the molecules per page
    private int maxPage;                                        //int for the maximum page number
    private int firstPage;                                      //int for the first page number (=1)
    private MainTableViewController mainTableViewController;    //controller for the main table 
    private Descriptors descriptors;                            //Instance for the descriptors
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new controller for the MainPane class
     *
     * @param aMainPane MainPane
     * @param aStage Stage
     */
    public MainPaneController(MainPane aMainPane, Stage aStage) {
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aMainPane == null) {
            throw new IllegalArgumentException("aMainPane must not be null");
        }
        //</editor-fold>
        this.mainPane = aMainPane;
        this.primaryStage = aStage;
        Scene tmpScene = new Scene(aMainPane, Globals.mainPaneWidth, Globals.mainPaneHeight);
        this.primaryStage.setTitle("Descriptor Calculator");
        this.primaryStage.setScene(tmpScene);
        this.primaryStage.show();
        this.primaryStage.setMinHeight(Globals.mainPaneHeight);
        this.primaryStage.setMinWidth(Globals.mainPaneWidth);
        System.out.println(aMainPane.getMainCenterPane().getWidth());
        this.firstPage = 1;
        this.addListener();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="deleteAll">
    /**
     * Sets all variables to null
     */
    public void deleteAll() {
        this.data.clearAll();
        this.data = null;
        this.dataFile = null;
        this.mainPane.getMoleculePerPageTextField().setText(null);
        this.mainPane.getMoleculePerPageTextField().setEditable(false);
        this.mainPane.getPageTextField().setText(null);
        this.mainPane.getPageTextField().setEditable(false);
        this.mainPane.getStatusLabel().setText(null);
        this.mainPane.getFirstButton().setDisable(true);
        this.mainPane.getPrevButton().setDisable(true);
        this.mainPane.getNextButton().setDisable(true);
        this.mainPane.getLastButton().setDisable(true);
        this.mainPane.getDeleteMenuItem().setDisable(true);
        this.mainPane.getDeleteSelectionMenuItem().setDisable(true);
        this.mainPane.getSaveSdfMenuItem().setDisable(true);
        this.mainPane.getSaveSmileMenuItem().setDisable(true);
        this.mainPane.getSaveSmileHMenuItem().setDisable(true);
        this.mainPane.getCheckAllMenuItem().setDisable(true);
        this.mainPane.getCalculationMenuItem().setDisable(true);
        this.mainTableViewController.getMainTable().setItems(null);
        this.mainTableViewController = null;
        this.mainPane.getMainCenterPane().getChildren().clear();
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">
    //<editor-fold defaultstate="collapsed" desc="addListener">
    /**
     * Adds listeners and EventHandlers and Events to the mainPane controls
     */
    private void addListener() {
        //<editor-fold defaultstate="collapsed" desc="load SDFile">
        /**
         * Load SDFile and sets up GUI
         */
        this.mainPane.getLoadSdfMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    if (data != null) {
                        MainPaneController.this.deleteAll();
                    }
                    MainPaneController.this.chooser = new Chooser();
                    MainPaneController.this.dataFile = chooser.chooseSdfile(primaryStage);
                    MainPaneController.this.setFileType(Globals.sdf);
                    MainPaneController.this.data = new Data(dataFile);
                    MainPaneController.this.data.createMoleculeHashMap();
                    MainPaneController.this.mainTableViewController = new MainTableViewController(MainPaneController.this.data,
                            MainPaneController.this.primaryStage, MainPaneController.this.mainPane, MainPaneController.this);
                    MainPaneController.this.setupGui();
                    MainPaneController.this.addTableListener();
                } catch (RuntimeException ex) {
                    System.err.println(ex);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="load Mol File">
        /**
         * Load MolFileDirectory
         */
        this.mainPane.getLoadMolMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    if (data != null) {
                        MainPaneController.this.deleteAll();
                    }
                    MainPaneController.this.chooser = new Chooser();
                    MainPaneController.this.dataFile = MainPaneController.this.chooser.chooseMolFile(primaryStage);
                    MainPaneController.this.setFileType(Globals.mol);
                    MainPaneController.this.data = new Data(dataFile);
                    MainPaneController.this.data.createMoleculeHashMap();
                    MainPaneController.this.mainTableViewController = new MainTableViewController(MainPaneController.this.data,
                            MainPaneController.this.primaryStage, MainPaneController.this.mainPane, MainPaneController.this);
                    System.out.println(data.getMoleculeCount() + " molecules found");
                    MainPaneController.this.setupGui();
                    MainPaneController.this.addTableListener();
                } catch (RuntimeException ex) {
                    System.err.println(ex);
//                    throw new RuntimeException(ex);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="load SMILEFile">
        /**
         * Load textfile with smiles
         */
        this.mainPane.getLoadSmileMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    if (data != null) {
                        MainPaneController.this.deleteAll();
                    }
                    MainPaneController.this.chooser = new Chooser();
                    MainPaneController.this.dataFile = MainPaneController.this.chooser.chooseSmileFile(primaryStage);
                    MainPaneController.this.setFileType(Globals.smile);
                    MainPaneController.this.data = new Data(dataFile);
                    MainPaneController.this.data.createMoleculeHashMap();
                    MainPaneController.this.mainTableViewController = new MainTableViewController(MainPaneController.this.data,
                            MainPaneController.this.primaryStage, MainPaneController.this.mainPane, MainPaneController.this);
                    System.out.println(data.getMoleculeCount() + " molecules found");
                    MainPaneController.this.setupGui();
                    MainPaneController.this.addTableListener();
                } catch (RuntimeException ex) {
//                    throw new RuntimeException(ex);
                    System.err.println(ex);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="save SDFile">
        /**
         * Saves the checked Molecules as a SDFile
         */
        this.mainPane.getSaveSdfMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    if (MainPaneController.this.data.isOneChecked()) {
                        MainPaneController.this.chooser = new Chooser();
                        MainPaneController.this.saveFile = MainPaneController.this.chooser.saveFile(MainPaneController.this.primaryStage, "sdf");
                        try (SDFWriter tmpWriter = new SDFWriter(new FileOutputStream(MainPaneController.this.saveFile))) {
                            int tmpLoop = MainPaneController.this.data.getMoleculeCount();
                            for (int i = 1; i <= tmpLoop; i++) {
                                if (MainPaneController.this.data.hasKey(i)) {
                                    if (MainPaneController.this.data.getMolecule(i).isChecked()) {
                                        tmpWriter.write(MainPaneController.this.data.getMolecule(i).getIAtomContainer());
                                    }
                                } else {
                                    tmpLoop++;
                                }
                            }
                        }
                    } else {
                        Dialog tmpDialog = new Dialog("Error", null, "You must at least check one molecule", AlertType.ERROR);
                        tmpDialog.showAndWait();
                    }
                } catch (IOException | CDKException ex) {
//                    throw new RuntimeException(ex);
                    System.err.println(ex);
                    Dialog tmpDialog = new Dialog("Error", ex.toString(), "Could not save file", AlertType.ERROR);
                    tmpDialog.showAndWait();
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="save SmilesFile">
        /**
         * Saves the checked molecules as SMILES
         */
        this.mainPane.getSaveSmileMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                MainPaneController.this.saveSmiles(Globals.withoutHydrogens);
//                String tmpError = "null";
//                try {
//                    if (MainPaneController.this.data.isOneChecked()) {
//                        MainPaneController.this.chooser = new Chooser();
//                        MainPaneController.this.saveFile = MainPaneController.this.chooser.saveFile(MainPaneController.this.primaryStage, "txt");
//                        BufferedWriter tmpWriter = new BufferedWriter(new FileWriter(MainPaneController.this.saveFile));
//                        SmilesGenerator tmpGenerator = SmilesGenerator.generic(); //.absolute().aromatic() ????                       
//                        int tmpLoop = MainPaneController.this.data.getMoleculeCount();
//                        for (int i = 1; i <= tmpLoop; i++) {
//                            if (MainPaneController.this.data.hasKey(i)) {
//                                if (MainPaneController.this.data.getMolecule(i).isChecked()) {
//                                    String tmpName = MainPaneController.this.data.getMolecule(i).getName();
//                                    tmpError = tmpName;
//                                    IAtomContainer tmpContainer = MainPaneController.this.data.getMolecule(i).getIAtomContainer();
//                                    String tmpSmile = tmpGenerator.create(AtomContainerManipulator.removeHydrogens(tmpContainer)); //does not know phenol
//                                    if (tmpLoop == 1) {
//                                        tmpWriter.write(tmpSmile);
//                                    } else {
//                                        tmpWriter.write(tmpName + ":" + "\t" + tmpSmile);
//                                    }
//                                    tmpWriter.newLine();
//                                }
//                            } else {
//                                tmpLoop++;
//                            }
//                        }
//                        tmpWriter.close();
//                    } else {
//                        Dialog tmpDialog = new Dialog("Error", null, "You must at least check one molecule", AlertType.ERROR);
//                        tmpDialog.showAndWait();
//                    }
//                } catch (IOException | CDKException ex) {
////                    throw new RuntimeException(ex);
//                    System.err.println(ex);
//                    System.err.println(tmpError);
//                    Dialog tmpDialog = new Dialog("Error", ex.toString(), "Could not save file", AlertType.ERROR);
//                    tmpDialog.showAndWait();
//                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="save SmilesHFile">
        /**
         * saves the checked molecules as SMILES with hydrogens
         */
        this.mainPane.getSaveSmileHMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                MainPaneController.this.saveSmiles(Globals.withHydrogens);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="save Descriptors for all">
        /**
         * Opens a FileChooser and saves the descriptorvalues for all molecules
         * to the selected file
         */
        this.mainPane.getSaveDescriptorsAll().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
//                MainPaneController.this.chooser = new Chooser();
//                MainPaneController.this.saveFile = MainPaneController.this.chooser.saveFile(MainPaneController.this.primaryStage, "csv");
//                UtilityMethods.writeAllDescriptorValues(MainPaneController.this.saveFile,
//                        MainPaneController.this.data);
                MainPaneController.this.saveDescriptorValues(Globals.csv, Globals.all);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="save Descriptors for selected">
        /**
         * Opens a FileChooser and saves the descriptorvalues for selected
         * molecules to the selected file
         */
        this.mainPane.getSaveDescriptorsSel().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (MainPaneController.this.data.isOneChecked()) {
//                    MainPaneController.this.chooser = new Chooser();
//                    MainPaneController.this.saveFile = MainPaneController.this.chooser.saveFile(MainPaneController.this.primaryStage, "csv");
//                    UtilityMethods.writeSelectedDescriptorValues(MainPaneController.this.saveFile,
//                            MainPaneController.this.data);
                    MainPaneController.this.saveDescriptorValues(Globals.csv, Globals.selected);
                } else {
                    Dialog tmpDialog = new Dialog("Error", null, "You must at least check one molecule", AlertType.ERROR);
                    tmpDialog.showAndWait();
                }
            }
        });
        //</editor-fold>

        this.mainPane.getSaveDescriptorAllAscii().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
//                MainPaneController.this.chooser = new Chooser();
//                MainPaneController.this.saveFile = MainPaneController.this.chooser.saveFile(MainPaneController.this.primaryStage, "ascii");
//                UtilityMethods.writeAllDescriptorValues(MainPaneController.this.saveFile,
//                        MainPaneController.this.data);
                MainPaneController.this.saveDescriptorValues(Globals.ascii, Globals.all);
            }
        });
        
        this.mainPane.getSaveDescriptorSelAscii().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (MainPaneController.this.data.isOneChecked()) {
                    MainPaneController.this.saveDescriptorValues(Globals.ascii, Globals.selected);
                } else {
                    Dialog tmpDialog = new Dialog("Error", null, "You must at least check one molecule", AlertType.ERROR);
                    tmpDialog.showAndWait();
                }
            }
        });

        //<editor-fold defaultstate="collapsed" desc="close">
        /**
         * Close
         */
        this.mainPane.getCloseMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                MainPaneController.this.primaryStage.close();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="check all">
        /**
         * EventHandler for the checkAllMenuItem, which checks or unchecks each
         * molecule
         */
        this.mainPane.getCheckAllMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                MainPaneController.this.data.setCheckAll(MainPaneController.this.mainPane.getCheckAllMenuItem().isSelected());
                MainPaneController.this.updateGui();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="deleteMenuItem">
        /**
         * EventHandler for the delete menuItem Opens a Dialog and whether
         * DialogResult is ButtonType.OK it deletes all or else it closes the
         * Dialog
         */
        this.mainPane.getDeleteMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Dialog tmpDialog = new util.Dialog(PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.deleteAllTitle.toString()),
                        PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.deleteAllHeader.toString()), "", AlertType.CONFIRMATION);
                if (tmpDialog.getResult() == ButtonType.OK) {
                    MainPaneController.this.deleteAll();
                } else {
                    tmpDialog.close();
                }
            }
        });
        //</editor-fold>        

        //<editor-fold defaultstate="collapsed" desc="deleteSelectionMenuItem">
        /**
         * EventHandler for the deleteSelectionMenuItem Opens a Dialog and
         * whether DialogResult is ButtonType.OK it deletes all selected
         * molecules or else it closes the Dialog
         */
        this.mainPane.getDeleteSelectionMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (MainPaneController.this.data.isOneChecked()) {
                    Dialog tmpDialog = new Dialog(String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.deleteOneTitle.toString()),
                            PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.selectionDialog.toString())),
                            String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.deleteOneHeader.toString()),
                                    PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.selectionDialog.toString())), "", AlertType.CONFIRMATION);
                    if (tmpDialog.getResult() == ButtonType.OK) {
                        int tmpLoop = MainPaneController.this.data.getMoleculeCount();
                        for (int i = 1; i <= tmpLoop; i++) {
                            if (MainPaneController.this.data.hasKey(i)) {
                                if (MainPaneController.this.data.getMolecule(i).isChecked()) {
                                    if (MainPaneController.this.data.getMoleculeCount() == 1) {
                                        MainPaneController.this.deleteAll();
                                        break;
                                    } else {
                                        MainPaneController.this.data.deleteOne(i);
                                    }
                                    MainPaneController.this.updateGui();
                                }
                            } else {
                                tmpLoop++;
                            }
                        }
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="calculationMenuItem">
        /**
         * EventHandler for the CalculationMenuItem Creates an new
         * CalculationPane and its Controller
         */
        this.mainPane.getCalculationMenuItem().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (MainPaneController.this.descriptors == null) {
                    MainPaneController.this.descriptors = new Descriptors();
                }
                CalculationPane tmpCalculationPane = new CalculationPane();
                CalculationPaneController tmpController = new CalculationPaneController(
                        MainPaneController.this.primaryStage, tmpCalculationPane, MainPaneController.this.data,
                        MainPaneController.this.descriptors);
                tmpController.setContent();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="pageTextField">
        /**
         * Sets pageNumber to typed number
         */
        this.mainPane.getPageTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent tmpEvent) {
                if (tmpEvent.getCode() == KeyCode.ENTER) {
                    String tmpText = mainPane.getPageTextField().getText();
                    if (tmpText.matches("\\d*")) { //TODO: compile?
                        int tmpValue = Integer.parseInt(tmpText);
                        if ((tmpValue > 0) && (tmpValue <= maxPage)) {
                            MainPaneController.this.setPageNumber(tmpValue);
                        }
                    }
                    MainPaneController.this.updateGui();
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="moleculesPerPagetextField">
        /**
         * Sets moleculesPerPage to typed number
         */
        this.mainPane.getMoleculePerPageTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent tmpEvent) {
                if (tmpEvent.getCode() == KeyCode.ENTER) {
                    String tmpText = mainPane.getMoleculePerPageTextField().getText();
                    if (tmpText.matches("\\d*")) { //TODO: compile?
                        int tmpValue = Integer.parseInt(tmpText);
                        if (tmpValue > data.getMoleculeCount()) {
                            MainPaneController.this.setMoleculesPerPage(data.getMoleculeCount());
                        } else if (tmpValue < 1) {
                            MainPaneController.this.setMoleculesPerPage(1);
                        } else {
                            MainPaneController.this.setMoleculesPerPage(tmpValue);
                        }
                        MainPaneController.this.updateGui();
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="firstButton">
        /**
         * Set pageNumber to firstPage
         */
        this.mainPane.getFirstButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPaneController.this.setPageNumber(firstPage);
                MainPaneController.this.updateGui();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="prevButton">
        /**
         * Decrements the pageNumber
         */
        this.mainPane.getPrevButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPaneController.this.decrementPageNumber();
                MainPaneController.this.updateGui();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="nextButton">
        /**
         * Increments the pageNumber
         */
        this.mainPane.getNextButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPaneController.this.incrementPageNumber();
                MainPaneController.this.updateGui();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="lastButton">
        /**
         * Sets pageNumber to maxPage
         */
        this.mainPane.getLastButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPaneController.this.setPageNumber(maxPage);
                MainPaneController.this.updateGui();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="mousewheel naviagtion">
        /**
         * EventHandler for ScrollEvents, in- or decrements the pageNumber and
         * updates the GUI
         */
        this.mainPane.addEventHandler(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent tmpEvent) {
                int tmpValue = (int) tmpEvent.getDeltaY();
                if (tmpValue == 40) {
                    if (!MainPaneController.this.mainPane.getPrevButton().isDisabled()) {
                        MainPaneController.this.decrementPageNumber();
                        MainPaneController.this.updateGui();
                    }
                } else if (tmpValue == -40) {
                    if (!MainPaneController.this.mainPane.getNextButton().isDisabled()) {
                        MainPaneController.this.incrementPageNumber();
                        MainPaneController.this.updateGui();
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="keyboard navigation">
        /**
         * EventHandler for KeyEvent, allows keyboard navigation
         */
        this.mainPane.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent tmpEvent) {
                if (!MainPaneController.this.mainPane.getMoleculePerPageTextField().isDisabled()) {
                    if ((tmpEvent.getCode() == KeyCode.PLUS) || (tmpEvent.getCode() == KeyCode.ADD)) {
                        if ((MainPaneController.this.moleculesPerPage < MainPaneController.this.data.getMoleculeCount())) {
                            MainPaneController.this.moleculesPerPage++;
                            MainPaneController.this.updateGui();
                        }
                    }
                    if ((tmpEvent.getCode() == KeyCode.MINUS) || (tmpEvent.getCode() == KeyCode.SUBTRACT)) {
                        MainPaneController.this.moleculesPerPage--;
                        MainPaneController.this.updateGui();
                    }
                }
            }
        });
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addTableListener">
    /**
     * Adds Listeners/EventHandlers to the table for page keyboardnavigation
     */
    private void addTableListener() {
        //<editor-fold defaultstate="collapsed" desc="keyboard navigation">
        /**
         * Adds an EventHandler for the keyboardnavigation
         */
        this.mainTableViewController.getMainTable().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent tmpEvent) {
                if (!MainPaneController.this.mainPane.getNextButton().isDisabled()) {
                    if (tmpEvent.getCode() == KeyCode.RIGHT) {
                        MainPaneController.this.incrementPageNumber();
                        MainPaneController.this.updateGui();
                    }
                    if (tmpEvent.getCode() == KeyCode.PAGE_UP) {
                        MainPaneController.this.incrementPageNumber();
                        MainPaneController.this.updateGui();
                    }
                }
                if (!MainPaneController.this.mainPane.getPrevButton().isDisabled()) {
                    if (tmpEvent.getCode() == KeyCode.LEFT) {
                        MainPaneController.this.decrementPageNumber();
                        MainPaneController.this.updateGui();
                    }
                    if (tmpEvent.getCode() == KeyCode.PAGE_DOWN) {
                        MainPaneController.this.decrementPageNumber();
                        MainPaneController.this.updateGui();
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="updateGui after delete one molecule">
        /**
         * Adds an EventHandler, which updates the TableView when event was
         * fired during deletion of one molecule
         */
        this.mainTableViewController.getMainTable().addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainPaneController.this.updateGui();
            }
        });
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setupGui">
    /**
     * Setup for the GUI
     */
    private void setupGui() {
        this.setPageNumber(this.firstPage);
        this.setPageTextField();
        this.setMoleculesPerPage(Globals.moleculesPerPage);
        this.setMoleculesPerPageTextField();
        this.setMaxPage();
        this.setStatusLabel();
        this.mainTableViewController.setRowCount(this.moleculesPerPage, this.maxPage);
        this.mainTableViewController.setContent();
        //mainPane setup
        this.mainPane.getMoleculePerPageTextField().setEditable(true);
        this.mainPane.getDeleteMenuItem().setDisable(false);
        this.mainPane.getDeleteSelectionMenuItem().setDisable(false);
        this.mainPane.getSaveSdfMenuItem().setDisable(false);
        this.mainPane.getSaveSmileMenuItem().setDisable(false);
        this.mainPane.getSaveSmileHMenuItem().setDisable(false);
        this.mainPane.getCheckAllMenuItem().setDisable(false);
        this.mainPane.getCalculationMenuItem().setDisable(false);
        this.mainPane.getSaveDescriptorsAll().setDisable(false);//TODO: erst wenn berechnung durchgeführt
        this.mainPane.getSaveDescriptorsSel().setDisable(false);//TODO: erst wenn berechnung durchgeführt
        this.mainPane.getSaveDescriptorAllAscii().setDisable(false);//TODO: erst wenn berechnung durchgeführt
        this.mainPane.getSaveDescriptorSelAscii().setDisable(false);//TODO: erst wenn berechnung durchgeführt
        if (this.maxPage > 1) {
            this.mainPane.getPageTextField().setEditable(true);
            this.mainPane.getNextButton().setDisable(false);
            this.mainPane.getLastButton().setDisable(false);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="updateGui">
    /**
     * Updates the Gui
     */
    private void updateGui() {
        if (this.moleculesPerPage <= 0) {
            this.setMoleculesPerPage(1);
        }
        if (this.moleculesPerPage > this.data.getMoleculeCount()) {
            this.moleculesPerPage = this.data.getMoleculeCount();
        }
        this.setMaxPage();
        if (this.pageNumber > this.maxPage) {
            this.setPageNumber(this.maxPage);
        }
        if (this.pageNumber < this.firstPage) {
            this.setPageNumber(this.firstPage);
        }
        this.setPageTextField();
        this.setMoleculesPerPageTextField();
        this.mainTableViewController.setRowCount(this.moleculesPerPage, this.maxPage);
        if (data != null) {
            this.mainTableViewController.setContent();
        }
        if (this.pageNumber == this.maxPage) {
            this.mainPane.getNextButton().setDisable(true);
            this.mainPane.getLastButton().setDisable(true);
        }
        if (this.pageNumber != this.maxPage) {
            this.mainPane.getNextButton().setDisable(false);
            this.mainPane.getLastButton().setDisable(false);
        }
        if (this.pageNumber <= 1) {
            this.mainPane.getPrevButton().setDisable(true);
            this.mainPane.getFirstButton().setDisable(true);
        }
        if (this.pageNumber > 1) {
            this.mainPane.getPrevButton().setDisable(false);
            this.mainPane.getFirstButton().setDisable(false);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setFileType">
    /**
     * Sets the Globals variables to differ the input files
     *
     * @param aType
     */
    private void setFileType(String aType) {
        switch (aType) {
            case Globals.sdf:
                Globals.isSdfile = true;
                Globals.isMolFile = false;
                Globals.isSmileFile = false;
                break;
            case Globals.mol:
                Globals.isSdfile = false;
                Globals.isMolFile = true;
                Globals.isSmileFile = false;
                break;
            case Globals.smile:
                Globals.isSdfile = false;
                Globals.isMolFile = false;
                Globals.isSmileFile = true;
                break;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="saveSmiles">
    /**
     * class internal method to save the checked molecules as SMILES, with or
     * without hydrogens
     *
     * @param String aCase
     */
    private void saveSmiles(String aCase) {
        String tmpError = "null";
        try {
            if (MainPaneController.this.data.isOneChecked()) {
                MainPaneController.this.chooser = new Chooser();
                MainPaneController.this.saveFile = MainPaneController.this.chooser.saveFile(MainPaneController.this.primaryStage, "txt");
                BufferedWriter tmpWriter = new BufferedWriter(new FileWriter(MainPaneController.this.saveFile));
                SmilesGenerator tmpGenerator = SmilesGenerator.generic(); //.absolute().aromatic() ????
                int tmpLoop = MainPaneController.this.data.getMoleculeCount();
                for (int i = 1; i <= tmpLoop; i++) {
                    if (MainPaneController.this.data.hasKey(i)) {
                        if (MainPaneController.this.data.getMolecule(i).isChecked()) {
                            String tmpName = MainPaneController.this.data.getMolecule(i).getName();
                            tmpError = tmpName;
                            String tmpSmile;
                            IAtomContainer tmpContainer = MainPaneController.this.data.getMolecule(i).getIAtomContainer();
                            if (aCase == Globals.withoutHydrogens) {
                                tmpSmile = tmpGenerator.create(AtomContainerManipulator.removeHydrogens(tmpContainer)); //does not know phenol
                            } else {
                                tmpSmile = tmpGenerator.create(tmpContainer);
                            }
                            if (tmpLoop == 1) {
                                tmpWriter.write(tmpSmile);
                            } else {
                                tmpWriter.write(tmpName + ":" + "\t" + tmpSmile);
                            }
                            tmpWriter.newLine();
                        }
                    } else {
                        tmpLoop++;
                    }
                }
                tmpWriter.close();
            } else {
                Dialog tmpDialog = new Dialog("Error", null, "You must at least check one molecule", AlertType.ERROR);
                tmpDialog.showAndWait();
            }
        } catch (IOException | CDKException ex) {
//                    throw new RuntimeException(ex);
            System.err.println(ex);
            System.err.println(tmpError);
            Dialog tmpDialog = new Dialog("Error", ex.toString(), "Could not save file", AlertType.ERROR);
            tmpDialog.showAndWait();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="saveDescriptorValues">
    /**
     * Internal method to save descriptor values
     *
     * @param String aFileExtension
     * @param String aType
     */
    private void saveDescriptorValues(String aFileExtension, String aType) {
        MainPaneController.this.chooser = new Chooser();
        MainPaneController.this.saveFile = MainPaneController.this.chooser.saveFile(MainPaneController.this.primaryStage, aFileExtension);
        UtilityMethods.writeDescriptorValues(MainPaneController.this.saveFile, MainPaneController.this.data, aType, aFileExtension);
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private properties">
    //<editor-fold defaultstate="collapsed" desc="setPageNumber">
    /**
     * Sets the pageNumber to given Value
     *
     * @param aValue int
     */
    private void setPageNumber(int aValue) {
        this.pageNumber = aValue;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setMoleculesPerPage">
    /**
     * Sets the moleculePerPage to given value
     *
     * @param aValue int
     */
    private void setMoleculesPerPage(int aValue) {
        if (aValue > this.data.getMoleculeCount()) {
            this.moleculesPerPage = this.data.getMoleculeCount();
        } else {
            this.moleculesPerPage = aValue;
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setMaxPage">
    /**
     * Sets the maxPage value
     */
    private void setMaxPage() {
        int tmpCount = this.data.getMoleculeCount();
        if ((tmpCount % this.moleculesPerPage) != 0) {
            this.maxPage = (tmpCount / this.moleculesPerPage) + 1;
        } else {
            this.maxPage = (tmpCount / this.moleculesPerPage);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setStatusLabel">
    /**
     * Sets text to the statusLabel
     */
    private void setStatusLabel() {
        this.mainPane.getStatusLabel().setText(this.dataFile.getName());
    }
    //</editor-fold> //TODO: evtl public

    //<editor-fold defaultstate="collapsed" desc="setMoleculesPerPageTextField">
    /**
     * Sets text to moleculesPerPageTextField
     */
    private void setMoleculesPerPageTextField() {
        this.mainPane.getMoleculePerPageTextField().setText(String.valueOf(this.moleculesPerPage));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setPageTextField">
    /**
     * Sets text to the pageTextField
     */
    private void setPageTextField() {
        this.mainPane.getPageTextField().setText(String.valueOf(this.pageNumber));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="incrementPageNumber">
    /**
     * Raises the page number by one
     */
    private void incrementPageNumber() {
        this.pageNumber++;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="decrementPageNumber">
    /**
     * Lowers the page number by one
     */
    private void decrementPageNumber() {
        this.pageNumber--;
    }
    //</editor-fold>
    //</editor-fold>
}
