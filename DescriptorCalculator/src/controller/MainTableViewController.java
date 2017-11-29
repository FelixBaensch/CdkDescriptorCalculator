package controller;

//<editor-fold defaultstate="collapsed" desc="imports">
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Data;
import model.ModelTableView;
import util.Dialog;
import util.Globals;
import util.PropertiesEnum;
import util.PropertiesLoader;
import view.MainPane;
import view.MainTableView;
import view.MoleculePane;
//</editor-fold>

/**
 * Controller class for the TableView
 *
 * @author Felix Bänsch, 18.05.2016
 */
public class MainTableViewController {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private Data data;                              //Instance for the molecule data
    private MainPane mainPane;                      //Instance for the mainPane
    private MainTableView mainTable;                //Instance for the mainTable
    private int rowCount;                           //int for the count of the tablerows for the height of the cells
    private int internalPageNumber;                 //int for the internal page number
    private int internalMaxPage;                    //int for the internal maximum page number
    private int moleculeIndex;                      //int for the molecule index for naviagtion
    private int imageHeight;                        //int for the height of the structure image
    private int imageWidth;                         //int for the width of the structure image
    private int compensationValue;                  //int for the compensation of molecule index for the last page number
    private double imageViewHeight;                 //double for the height of the imageview which contains the structure image
    private double imageViewWidth;                  //double for the width of the imageview which contains the structure image
    private Stage primaryStage;                     //Stage for the mainPane
    private ObservableList<ModelTableView> list;    //ObservableList which contains the data for the table of the mainPane
    private MainPaneController mainPaneController;  //controller for the mainPane
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new instance on MainTableViewController
     *
     * @param aData Data
     * @param aStage Stage
     * @param aMainPane MainPane
     * @param aController MainPaneController
     */
    public MainTableViewController(Data aData, Stage aStage, MainPane aMainPane,
            MainPaneController aController) {
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aData == null) {
            throw new IllegalArgumentException("aData must not be null");
        }
        //</editor-fold>
        this.mainPaneController = aController;
        this.data = aData;
        this.primaryStage = aStage;
        this.mainPane = aMainPane;
        this.mainTable = new MainTableView();
        this.mainPane.getMainCenterPane().getChildren().add(this.mainTable);
        this.mainTable.setPrefSize(this.mainPane.getMainCenterPane().getWidth(), this.mainPane.getMainCenterPane().getHeight());
        this.addListener();
//        this.compensationValue = 1; 
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="setRowCount">
    /**
     * Sets the rowCount and the size for the imageView and image
     *
     * @param aRowCount int
     * @param aMaxPage int 
     */
    public void setRowCount(int aRowCount, int aMaxPage) {
        this.rowCount = aRowCount;
        this.internalPageNumber = Integer.valueOf(this.mainPane.getPageTextField().getText()) - 1;
        this.internalMaxPage = aMaxPage - 1;
        this.imageViewHeight = ((this.mainPane.getMainCenterPane().getHeight() - Globals.headerHeight) / this.rowCount);
        this.imageViewWidth = (this.mainTable.getStructureColumn().getWidth());
        this.imageHeight = (int) (this.imageViewHeight - Globals.imageMargin);
        this.imageWidth = (int) (this.imageViewWidth - Globals.imageMargin);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setContent">
    /**
     * Creates content for the TableView and sets it
     */
    public void setContent() {
        this.list = FXCollections.observableArrayList(new Callback<ModelTableView, Observable[]>() {
            @Override
            public Observable[] call(ModelTableView param) {
                return new Observable[]{param.checkProperty()};
            }
        });
        this.moleculeIndex = this.internalPageNumber * this.rowCount + 1;
        //firstPage
        if (this.internalPageNumber == 0) {
            int tmpLoop = this.rowCount;
            int tmpKey = 1;
            for (int i = 1; i <= tmpLoop; i++) {
                if (this.data.hasKey(tmpKey)) {
                    this.list.add(new ModelTableView(this.data.getMolecule(tmpKey).getIdentifier(),
                            this.data.getMolecule(tmpKey).getName(), this.data.getMolecule(tmpKey).createImage(this.imageWidth, this.imageHeight),
                            this.data.getMolecule(tmpKey).isChecked()));
                    tmpKey++;
                } else {
                    tmpKey++;
                    tmpLoop++;
                }
            }
        } //lastPage
        else if (this.internalPageNumber == this.internalMaxPage) {
            int tmpCount;
            if ((this.data.getMoleculeCount() % this.rowCount) != 0) {
                tmpCount = (this.data.getMoleculeCount() % this.rowCount) + this.moleculeIndex + this.compensationValue;
            } else {
                tmpCount = this.rowCount + this.moleculeIndex;
            }
            int tmpKey = this.moleculeIndex;
            for (int j = 1; j < tmpKey; j++) {
                if (!this.data.hasKey(j)) {
                    tmpKey++;
                    tmpCount++;
                }
            }
            for (int i = this.moleculeIndex; i < tmpCount; i++) {
                if (this.data.hasKey(tmpKey)) {
                    this.list.add(new ModelTableView(this.data.getMolecule(tmpKey).getIdentifier(),
                            this.data.getMolecule(tmpKey).getName(), this.data.getMolecule(tmpKey).createImage(this.imageWidth, this.imageHeight),
                            this.data.getMolecule(tmpKey).isChecked()));
                    tmpKey++;

                } else {
                    tmpKey++;
                }
            }
        } //else
        else {
            int tmpLoop = this.moleculeIndex + this.rowCount;
            int tmpKey = this.moleculeIndex;
            for (int j = 1; j < tmpKey; j++) {
                if (!this.data.hasKey(j)) {
                    tmpKey++;
                    tmpLoop++;
                }
            }
            for (int i = tmpKey; i < tmpLoop; i++) {
                if (this.data.hasKey(tmpKey)) {
                    this.list.add(new ModelTableView(this.data.getMolecule(tmpKey).getIdentifier(),
                            this.data.getMolecule(tmpKey).getName(), this.data.getMolecule(tmpKey).createImage(this.imageWidth, this.imageHeight),
                            this.data.getMolecule(tmpKey).isChecked()));
                    tmpKey++;
                } else {
                    tmpKey++;
                    tmpLoop++;
                }
            }
        }
        this.mainTable.setItems(this.list);
        //structureColumn
        this.mainTable.getStructureColumn().setCellValueFactory(new PropertyValueFactory("imageView"));
        this.mainTable.getStructureColumn().setStyle("-fx-alignment: CENTER");
        //checkColumn
        this.mainTable.getCheckColumn().setCellValueFactory(new PropertyValueFactory("check"));
        //-Callback for checkColumn
        final Callback<TableColumn<ModelTableView, Boolean>, TableCell<ModelTableView, Boolean>> tmpCellFactory = CheckBoxTableCell.forTableColumn(this.mainTable.getCheckColumn());
        this.mainTable.getCheckColumn().setCellFactory(new Callback<TableColumn<ModelTableView, Boolean>, TableCell<ModelTableView, Boolean>>() {
            @Override
            public TableCell<ModelTableView, Boolean> call(TableColumn<ModelTableView, Boolean> param) {
                TableCell<ModelTableView, Boolean> tmpCell = tmpCellFactory.call(param);
                tmpCell.setAlignment(Pos.CENTER);
                tmpCell.setEditable(true);
                return tmpCell;
            }
        });
        this.mainTable.getCheckColumn().setCellFactory(tmpCellFactory);
        this.mainTable.getCheckColumn().setStyle("-fx-alignment: CENTER");
        this.mainTable.getCheckColumn().setEditable(true);
        //counterColumn
        this.mainTable.getCounterColumn().setCellValueFactory(new PropertyValueFactory("counter"));
        this.mainTable.getCounterColumn().setStyle("-fx-alignment: CENTER");
        //nameColumn
        this.mainTable.getNameColumn().setCellValueFactory(new PropertyValueFactory("name"));
        this.mainTable.getNameColumn().setStyle("-fx-alignment: CENTER");
        this.addListListener();
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">
    //<editor-fold defaultstate="collapsed" desc="addListener">
    /**
     * Adds width and height listener to resize the mainTable (MainTableView)
     * and update the content when parent is resized. And adds a width listener
     * for the structurecolumn which updates the MainTableView
     */
    private void addListener() {
        //<editor-fold defaultstate="collapsed" desc="widthListener">
        /**
         * Width listener
         */
        this.mainPane.getMainCenterPane().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                MainTableViewController.this.mainTable.setPrefWidth(MainTableViewController.this.mainPane.getMainCenterPane().getWidth());
                MainTableViewController.this.updateImageSize();
                MainTableViewController.this.setContent(); // TODO: nicht schön, besser nur das Image updaten, aber funktioniert
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="heightListener">
        /**
         * Height listener
         */
        this.mainPane.getMainCenterPane().heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                MainTableViewController.this.mainTable.setPrefHeight(MainTableViewController.this.mainPane.getMainCenterPane().getHeight());
                MainTableViewController.this.updateImageSize();
                MainTableViewController.this.setContent();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="structureColumnWidthListener">
        /**
         * StructureColumn width listener
         */
        this.mainTable.getStructureColumn().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                MainTableViewController.this.updateImageSize();
                MainTableViewController.this.setContent(); //TODO: auch hier besser nur das Column updaten
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="contextMenu">
        /**
         * Listener for the showMenuItem and the deleteMenuItem of the
         * ContextMenu Shows a MoleculePane for the selected Molecule or deletes
         * the selected Molecule and fires an event
         */
        this.mainTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent tmpMouseEvent) {
                try {
                    ModelTableView tmpModel = (ModelTableView) MainTableViewController.this.mainTable.getSelectionModel().getSelectedItem();
                    if (tmpMouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        MainTableViewController.this.mainTable.getShowMenuItem().setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent tmpActionEvent) {
                                MoleculePane tmpPane = new MoleculePane();
                                MoleculePaneController tmpController
                                        = new MoleculePaneController(MainTableViewController.this.primaryStage,
                                                MainTableViewController.this.data, tmpModel.getIdentifier(), tmpPane);
                                tmpController.setContent();
                            }
                        });
                        MainTableViewController.this.mainTable.getDeleteMenuItem().setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Dialog tmpDialog = new util.Dialog(String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.deleteOneTitle.toString()), tmpModel.getName()),
                                        String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.deleteOneHeader.toString()), tmpModel.getName()), null, Alert.AlertType.CONFIRMATION);
                                if (tmpDialog.getResult() == ButtonType.OK) {
                                    if (MainTableViewController.this.data.getMoleculeCount() == 1) {
                                        MainTableViewController.this.mainPaneController.deleteAll();
                                    } else {
                                        int tmpIndex = Integer.parseInt(tmpModel.getIdentifier().toString("Counter"));
                                        MainTableViewController.this.data.deleteOne(tmpIndex);
                                        MainTableViewController.this.compensationValue++;
                                        Event.fireEvent(MainTableViewController.this.mainTable, event);
                                    }
                                } else {
                                    tmpDialog.close();
                                }
                            }
                        });
                    }
                } catch (ClassCastException ex) {
//                    throw new RuntimeException(ex); //TODO
                    System.err.println(ex);
                }
            }
        });
        //</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="addListListener">
    /**
     * Adds a ListChangeListener to the ObservableList list and sets the check
     * (boolean) of the molecule
     */
    private void addListListener() {
        this.list.addListener(new ListChangeListener<ModelTableView>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ModelTableView> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        int tmpIndex = Integer.parseInt(MainTableViewController.this.list.get(c.getFrom()).getCounter());
                        MainTableViewController.this.data.getMolecule(tmpIndex).setCheck(MainTableViewController.this.list.get(c.getFrom()).getCheck());
                    }
                }
            }
        });
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="updateSize">
    /**
     * Updates the imageView size and the image image
     */
    private void updateImageSize() {
        this.imageViewHeight = ((this.mainPane.getMainCenterPane().getHeight() - Globals.headerHeight) / this.rowCount);
        this.imageViewWidth = (this.mainTable.getStructureColumn().getWidth());
        this.imageHeight = (int) (this.imageViewHeight - Globals.imageMargin);
        this.imageWidth = (int) (this.imageViewWidth - Globals.imageMargin);
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="getMainTable">
    /**
     * Gets the mainTable
     *
     * @return MainTableView
     */
    public MainTableView getMainTable() {
        return this.mainTable;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getCompensationValue">
    /**
     * Gets the compensationValue
     * @return int
     */
    public int getCompensationValue() {
        return this.compensationValue;
    }
    //</editor-fold>
    //</editor-fold>
}
