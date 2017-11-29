package view;

//<editor-fold defaultstate="collapsed" desc="imports">
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.ModelTableView;
import util.Globals;
import util.PropertiesEnum;
import util.PropertiesLoader;
//</editor-fold>
/**
 * Custom TableView
 *
 * @author Felix Bänsch, 18.05.2016
 */
public class MainTableView extends TableView {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private TableColumn<ModelTableView, Boolean> checkColumn;
    private TableColumn<ModelTableView, String> counterColumn;
    private TableColumn<ModelTableView, String> nameColumn;
    private TableColumn<ModelTableView, Image> structureColumn;
    private ContextMenu tableContextMenu;
    private MenuItem showMenuItem;
    private MenuItem deleteMenuItem;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new instance of MainTableView
     */
    public MainTableView() {
        super();
        this.setPlaceholder(new ImageView(new Image("file:bat_symbol.png")));
        //checkColumn
        this.checkColumn = new TableColumn<>(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainTableEnum.checkHeader.toString()));
        this.checkColumn.setMinWidth(Globals.checkColumnWidth);
        this.checkColumn.setPrefWidth(Globals.checkColumnWidth);
        this.checkColumn.setMaxWidth(Globals.checkColumnWidth);
        this.checkColumn.setResizable(false);
        this.checkColumn.setEditable(true);
        this.checkColumn.setSortable(false);
        //counterColumn
        this.counterColumn = new TableColumn<>(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainTableEnum.counterHeader.toString()));
        this.counterColumn.setMinWidth(Globals.counterColumnWidth);
        this.counterColumn.setPrefWidth(Globals.counterColumnWidth);
        this.counterColumn.setMaxWidth(Globals.counterColumnWidth);
        this.counterColumn.setResizable(false);
        this.counterColumn.setEditable(false);
        this.counterColumn.setSortable(false);
        //nameColumn
        this.nameColumn = new TableColumn<>(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainTableEnum.nameHeader.toString()));
        this.nameColumn.setMinWidth(150);
        this.nameColumn.setPrefWidth(USE_COMPUTED_SIZE);
        this.nameColumn.setMaxWidth(250);
        this.nameColumn.setResizable(true);
        this.nameColumn.setEditable(false);
        this.nameColumn.setSortable(false);
        //structureColumn
        this.structureColumn = new TableColumn<>(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainTableEnum.structureHeader.toString()));
        this.structureColumn.setMinWidth(100);
        this.structureColumn.setPrefWidth(485);
        this.structureColumn.setMaxWidth(5000);
        this.structureColumn.setResizable(true);
        this.structureColumn.setEditable(false);
        this.structureColumn.setSortable(false);
        //contextMenu
        this.tableContextMenu = new ContextMenu();
        this.showMenuItem = new MenuItem(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainTableEnum.showMenuItem.toString()));
        this.deleteMenuItem = new MenuItem(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainTableEnum.deleteMenuItem.toString()));
        this.tableContextMenu.getItems().addAll(this.showMenuItem, this.deleteMenuItem);
        //this
//        this.setStyle("-fx-background-color: RED");
        this.getStylesheets().add("/view/MainWinStyle.css");
        this.getColumns().addAll(this.checkColumn, this.counterColumn, this.nameColumn, this.structureColumn);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setContextMenu(this.tableContextMenu);
        this.setEditable(true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="getCheckColumn">
    /**
     * Gets the checkColumn
     *
     * @return TableColumn
     */
    public TableColumn getCheckColumn() {
        return this.checkColumn;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCounterColumn">
    /**
     * Gets the counterColumn
     *
     * @return TableColumn
     */
    public TableColumn getCounterColumn() {
        return this.counterColumn;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getNameColumn">
    /**
     * Gets the nameColumn
     *
     * @return TableColumn
     */
    public TableColumn getNameColumn() {
        return this.nameColumn;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getStructureColumn">
    /**
     * Gets the structureColumn
     *
     * @return Column
     */
    public TableColumn getStructureColumn() {
        return this.structureColumn;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getShowMenuItem">
    /**
     * Gets the showMenuItem
     * @return MenuItem
     */
    public MenuItem getShowMenuItem(){
        return this.showMenuItem;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getDeleteMenuItem">
    /**
     * Gets the deleteMenuItem
     * @return MenuItem
     */
    public MenuItem getDeleteMenuItem(){
        return this.deleteMenuItem;
    }
    //</editor-fold>
    //</editor-fold>
}
