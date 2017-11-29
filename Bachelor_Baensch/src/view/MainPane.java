package view;

//<editor-fold defaultstate="collapsed" desc="imports">
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import util.Globals;
import util.PropertiesEnum;
import util.PropertiesLoader;
//</editor-fold>

/**
 * MainPane
 *
 * @author Felix Bänsch, 12.05.2016
 */
public class MainPane extends AnchorPane {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private BorderPane borderPane;
    private Pane mainCenterPane;
    private MenuBar menuBar;
    private Menu fileMenu;
    private Menu editMenu;
    private Menu toolMenu;
    private MenuItem loadSdfMenuItem;
    private MenuItem loadMolMenuItem;
    private MenuItem loadSmileMenuItem;
    private MenuItem closeMenuItem;
    private MenuItem saveSdfMenuItem;
    private MenuItem saveSmileMenuItem;
    private MenuItem saveSmileHMenuItem;
    private MenuItem deleteMenuItem;
    private MenuItem deleteSelectionMenuItem;
    private MenuItem calculationMenuItem;
    private MenuItem saveDescriptorsAll;
    private MenuItem saveDescriptorsSel;
    private MenuItem saveDescriptorAllAscii;
    private MenuItem saveDescriptorSelAscii;
    private CheckMenuItem checkAllMenuItem;
    private ToolBar toolBar;
    private Label statusLabel;
    private Pane fillGapPane;
    private HBox moleculesPerPageBox;
    private Label moleculesPerPageLabel;
    private TextField moleculePerPageTextField;
    private HBox navigationBox;
    private Button firstButton;
    private Button prevButton;
    private TextField pageTextField;
    private Button nextButton;
    private Button lastButton;
    private Tooltip moleculesPerPageTip;
    private Tooltip firstButtonTip;
    private Tooltip prevButtonTip;
    private Tooltip pageTip;
    private Tooltip nextButtonTip;
    private Tooltip lastButtonTip;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new mainPane
     */
    public MainPane() {
        super();
        //<editor-fold defaultstate="collapsed" desc="borderPane">
        this.borderPane = new BorderPane();
        MainPane.setTopAnchor(this.borderPane, 0.0);
        MainPane.setLeftAnchor(this.borderPane, 0.0);
        MainPane.setRightAnchor(this.borderPane, 0.0);
        MainPane.setBottomAnchor(this.borderPane, 0.0);
        HBox.setHgrow(this.borderPane, Priority.ALWAYS);
        VBox.setVgrow(this.borderPane, Priority.ALWAYS);
        this.mainCenterPane = new Pane();
        this.mainCenterPane.setStyle("-fx-background-color: LIGHTGREY");
        this.borderPane.setMargin(this.mainCenterPane, new Insets(Globals.mainPaneInsets));
//        this.borderPane.setStyle("-fx-background-color: LIGHTBLUE");
        this.borderPane.setCenter(this.mainCenterPane);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="menuBar">
        this.menuBar = new MenuBar();
//        this.setSize(this.menuBar, USE_COMPUTED_SIZE, Globals.toolContainerHeight);
        this.borderPane.setTop(this.menuBar);
        HBox.setHgrow(this.menuBar, Priority.ALWAYS);

        //<editor-fold defaultstate="collapsed" desc="fileMenu">
        this.fileMenu = new Menu();
        this.fileMenu.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.fileMenu.toString()));
        this.menuBar.getMenus().add(this.fileMenu);
        //-loadSdfMenuItem
        this.loadSdfMenuItem = new MenuItem();
        this.loadSdfMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.loadSdf.toString()));
        this.fileMenu.getItems().add(this.loadSdfMenuItem);
        //-loadMolMenuItem
        this.loadMolMenuItem = new MenuItem();
        this.loadMolMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.loadMol.toString()));
        this.fileMenu.getItems().add(this.loadMolMenuItem);
        //-loadSmileMenuItem
        this.loadSmileMenuItem = new MenuItem();
        this.loadSmileMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.loadSmile.toString()));
        this.fileMenu.getItems().add(this.loadSmileMenuItem);
        //-seperator
        this.fileMenu.getItems().add(new SeparatorMenuItem());
        //-saveSDF
        this.saveSdfMenuItem = new MenuItem();
        this.saveSdfMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.saveSdf.toString()));
        this.saveSdfMenuItem.setDisable(true);
        this.fileMenu.getItems().add(this.saveSdfMenuItem);
        //-saveSmile
        this.saveSmileMenuItem = new MenuItem();
        this.saveSmileMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.saveSmile.toString()));
        this.saveSmileMenuItem.setDisable(true);
        this.fileMenu.getItems().add(this.saveSmileMenuItem);
        //-saveSmileH
        this.saveSmileHMenuItem = new MenuItem();
        this.saveSmileHMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.saveSmileH.toString()));
        this.saveSmileHMenuItem.setDisable(true);
        this.fileMenu.getItems().add(this.saveSmileHMenuItem);
        //-seperator
        this.fileMenu.getItems().add(new SeparatorMenuItem());
        //-saveDescriptorsAll
        this.saveDescriptorsAll = new MenuItem();
        this.saveDescriptorsAll.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.saveDescriptorsAll.toString()));
        this.saveDescriptorsAll.setDisable(true);
        this.fileMenu.getItems().add(this.saveDescriptorsAll);
        //-saveDescriptrosSel
        this.saveDescriptorsSel = new MenuItem();
        this.saveDescriptorsSel.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.saveDescriptorsSel.toString()));
        this.saveDescriptorsSel.setDisable(true);
        this.fileMenu.getItems().add(this.saveDescriptorsSel);
        //-saveDescriptroAllAscii
        this.saveDescriptorAllAscii = new MenuItem();
        this.saveDescriptorAllAscii.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.saveDescriptorAllAscii.toString()));
        this.saveDescriptorAllAscii.setDisable(true);
        this.fileMenu.getItems().add(this.saveDescriptorAllAscii);
        //-saveDescriptroSelAscii
        this.saveDescriptorSelAscii = new MenuItem();
        this.saveDescriptorSelAscii.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.saveDescriptorSelAscii.toString()));
        this.saveDescriptorSelAscii.setDisable(true);
        this.fileMenu.getItems().add(this.saveDescriptorSelAscii);
        //-seperator
        this.fileMenu.getItems().add(new SeparatorMenuItem());
        //-close
        this.closeMenuItem = new MenuItem();
        this.closeMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.close.toString()));
        this.fileMenu.getItems().add(this.closeMenuItem);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="editMenu">
        this.editMenu = new Menu();
        this.editMenu.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.editMenu.toString()));
        this.menuBar.getMenus().add(this.editMenu);
        //-checkAll
        this.checkAllMenuItem = new CheckMenuItem();
        this.checkAllMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.checkAll.toString()));
        this.checkAllMenuItem.setDisable(true);
        this.editMenu.getItems().add(this.checkAllMenuItem);
        //-seperator
        this.editMenu.getItems().add(new SeparatorMenuItem());
        //-delete
        this.deleteMenuItem = new MenuItem();
        this.deleteMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.delete.toString()));
        this.deleteMenuItem.setDisable(true);
        this.editMenu.getItems().add(this.deleteMenuItem);
        //-deleteSelection
        this.deleteSelectionMenuItem = new MenuItem();
        this.deleteSelectionMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.deleteSelection.toString()));
        this.deleteSelectionMenuItem.setDisable(true);
        this.editMenu.getItems().add(this.deleteSelectionMenuItem);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="toolMenu">
        this.toolMenu = new Menu();
        this.toolMenu.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.toolMenu.toString()));
        this.menuBar.getMenus().add(this.toolMenu);
        //-calculation
        this.calculationMenuItem = new MenuItem();
        this.calculationMenuItem.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.calculation.toString()));
        this.calculationMenuItem.setDisable(true);
        this.toolMenu.getItems().add(this.calculationMenuItem);
        //</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="toolBar">
        this.toolBar = new ToolBar();
        this.borderPane.setBottom(this.toolBar);
        this.setSize(this.toolBar, USE_COMPUTED_SIZE, Globals.toolContainerHeight);
        this.toolBar.setPadding(new Insets(Globals.toolbarPadding));
        this.toolBar.setStyle("-fx-background-color: LIGHTGREY");
        //-statusLabel
        this.statusLabel = new Label();
        HBox.setHgrow(this.statusLabel, Priority.ALWAYS);
        this.toolBar.getItems().add(this.statusLabel);
        //-fillGapPane
        this.fillGapPane = new Pane();
        HBox.setHgrow(this.fillGapPane, Priority.ALWAYS);
        this.toolBar.getItems().add(this.fillGapPane);

        //<editor-fold defaultstate="collapsed" desc="moleculesPerPageBox">
        this.moleculesPerPageBox = new HBox();
        this.moleculesPerPageBox.setSpacing(Globals.containerSpacing);
        this.toolBar.getItems().add(this.moleculesPerPageBox);
        //-label
        this.moleculesPerPageLabel = new Label();
        this.setSize(this.moleculesPerPageLabel, USE_COMPUTED_SIZE, Globals.controlsHeight);
        this.moleculesPerPageLabel.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.moleculesPerPage.toString()));
        this.moleculesPerPageBox.getChildren().add(this.moleculesPerPageLabel);
        //-textField
        this.moleculePerPageTextField = new TextField();
        this.setSize(this.moleculePerPageTextField, Globals.moleculesPerPageTextFieldWidth, Globals.controlsHeight);
        this.moleculePerPageTextField.setAlignment(Pos.CENTER);
        this.moleculePerPageTextField.setEditable(false);
        this.moleculesPerPageTip = new Tooltip(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.moleculesPerPageTextFieldTip.toString()));
        this.moleculePerPageTextField.setTooltip(this.moleculesPerPageTip);
        this.moleculesPerPageBox.getChildren().add(this.moleculePerPageTextField);
        //-pane for spacing
        Pane tmpPane = new Pane();
        tmpPane.setPrefSize(Globals.toolbarPadding, Globals.controlsHeight);
        this.moleculesPerPageBox.getChildren().add(tmpPane);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="navigationBox">
        this.navigationBox = new HBox();
        this.navigationBox.setSpacing(Globals.containerSpacing);
        this.toolBar.getItems().add(this.navigationBox);
        //-firstButton
        this.firstButton = new Button();
        this.firstButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.firstButton.toString()));
        this.setSize(this.firstButton, USE_COMPUTED_SIZE, Globals.controlsHeight); //TODO: evtl width ändern
        this.firstButton.setDisable(true);
        this.firstButtonTip = new Tooltip(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.firstButtonTip.toString()));
        this.firstButton.setTooltip(this.firstButtonTip);
        this.navigationBox.getChildren().add(this.firstButton);
        //-prevButton
        this.prevButton = new Button();
        this.prevButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.prevButton.toString()));
        this.setSize(this.prevButton, USE_COMPUTED_SIZE, Globals.controlsHeight);
        this.prevButton.setDisable(true);
        this.prevButtonTip = new Tooltip(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.prevButtonTip.toString()));
        this.prevButton.setTooltip(this.prevButtonTip);
        this.navigationBox.getChildren().add(this.prevButton);
        //-textField
        this.pageTextField = new TextField();
        this.setSize(this.pageTextField, Globals.pageTextFieldWidth, Globals.controlsHeight);
        this.pageTextField.setAlignment(Pos.CENTER);
        this.pageTextField.setEditable(false);
        this.pageTip = new Tooltip(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.pageNumberTextFieldTip.toString()));
        this.pageTextField.setTooltip(this.pageTip);
        this.navigationBox.getChildren().add(this.pageTextField);
        //-nextButton
        this.nextButton = new Button();
        this.nextButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.nextButton.toString()));
        this.setSize(this.nextButton, USE_COMPUTED_SIZE, Globals.controlsHeight); //TODO: evtl width ändern
        this.nextButton.setDisable(true);
        this.nextButtonTip = new Tooltip(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.nextButtonTip.toString()));
        this.nextButton.setTooltip(this.nextButtonTip);
        this.navigationBox.getChildren().add(this.nextButton);
        //-lastButton
        this.lastButton = new Button();
        this.lastButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.lastButton.toString()));
        this.setSize(this.lastButton, USE_COMPUTED_SIZE, Globals.controlsHeight); //TODO: evtl width ändern
        this.lastButton.setDisable(true);
        this.lastButtonTip = new Tooltip(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.lastButtonTip.toString()));
        this.lastButton.setTooltip(this.lastButtonTip);
        this.navigationBox.getChildren().add(this.lastButton);
        //</editor-fold>
        //</editor-fold>
        this.getChildren().add(this.borderPane);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="menuItems">
    //<editor-fold defaultstate="collapsed" desc="getCloseMenuItem">
    /**
     * Gets the closeMenuItem
     *
     * @return MenuItem
     */
    public MenuItem getCloseMenuItem() {
        return this.closeMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCalculationMenuItem">
    /**
     * Gets the calculationMenuItem
     *
     * @return MenuItem
     */
    public MenuItem getCalculationMenuItem() {
        return this.calculationMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getDeleteMenuItem">
    /**
     * Gets the deleteMenuItem
     *
     * @return MenuItem
     */
    public MenuItem getDeleteMenuItem() {
        return this.deleteMenuItem;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getDeleteSelectionMenuItem">
    /**
     * Gets the deleteSelectionMenuItem
     * @return MenuItem
     */
    public MenuItem getDeleteSelectionMenuItem(){
        return this.deleteSelectionMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getLoadSdfMenuItem">
    /**
     * Gets theloadSdfMenuItem
     *
     * @return MenuItem
     */
    public MenuItem getLoadSdfMenuItem() {
        return this.loadSdfMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getLoadMolMenuItem">
    /**
     * Gets the loadMolMenuItem
     *
     * @return MenuItem
     */
    public MenuItem getLoadMolMenuItem() {
        return this.loadMolMenuItem;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getLoadSmileMenuItem">
    /**
     * Gets the loadSmileMenuItem
     * @return MenuItem
     */
    public MenuItem getLoadSmileMenuItem(){
        return this.loadSmileMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getSaveSdfMenuItem">
    /**
     * Gets the saveSdfMenuItem
     *
     * @return MenuItem
     */
    public MenuItem getSaveSdfMenuItem() {
        return this.saveSdfMenuItem;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getSaveSmileMenuItem">
    /**
     * Gets the saveSmileMenuItem
     * @return MenuItem
     */
    public MenuItem getSaveSmileMenuItem(){
        return this.saveSmileMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getSaveSmileMenuItem">
    /**
     * Gets the saveSmileMenuItem
     * @return MenuItem
     */
    public MenuItem getSaveSmileHMenuItem(){
        return this.saveSmileHMenuItem;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getCheckAllMenuItem">
    /**
     * Gets the checkAllMenuItem
     *
     * @return CheckMenuItem
     */
    public CheckMenuItem getCheckAllMenuItem() {
        return this.checkAllMenuItem;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getSaveDescriptorsAll">
    /**
     * Gets the saveDescriptorsAll MenuItem
     * @return MenuItem
     */
    public MenuItem getSaveDescriptorsAll(){
        return this.saveDescriptorsAll;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getSaveDescriptorsSel">
    /**
     * Gets the saveDescriptorsSel MenuItem
     * @return MenuItem
     */
    public MenuItem getSaveDescriptorsSel(){
        return this.saveDescriptorsSel;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getSaveDescriptorAllAscii">
    /**
     * Gets the saveDescriptorAllAscii MenuItem
     * @return MenuItem
     */
    public MenuItem getSaveDescriptorAllAscii(){
        return this.saveDescriptorAllAscii;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getSaveDescriptorSelAscii">
    /**
     * Gets the saveDescriptorSelAscii MenuItem
     * @return MenuItem
     */
    public MenuItem getSaveDescriptorSelAscii(){
        return this.saveDescriptorSelAscii;
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="buttons">
    //<editor-fold defaultstate="collapsed" desc="getLastButton">
    /**
     * Gets the lastButton
     *
     * @return Button
     */
    public Button getLastButton() {
        return this.lastButton;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getFirstButton">
    /**
     * Gets the firstButton
     *
     * @return Button
     */
    public Button getFirstButton() {
        return this.firstButton;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getNextButton">
    /**
     * Gets the nextButton
     *
     * @return Button
     */
    public Button getNextButton() {
        return this.nextButton;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getPrevButton">
    /**
     * Gets the prevButton
     * @return Button
     */
    public Button getPrevButton() {
        return this.prevButton;
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getPageTextField">
    /**
     * Gets the pageTextField
     *
     * @return TextField
     */
    public TextField getPageTextField() {
        return this.pageTextField;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMoleculePerPageTextField">
    /**
     * Gets the moleculePerPageTextField
     *
     * @return TextField
     */
    public TextField getMoleculePerPageTextField() {
        return this.moleculePerPageTextField;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getStatusPanel">
    /**
     * Gets the statusLabel
     *
     * @return Lanel
     */
    public Label getStatusLabel() {
        return this.statusLabel;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMainCenterPane">
    /**
     * Gets the mainPane
     *
     * @return Pane
     */
    public Pane getMainCenterPane() {
        return this.mainCenterPane;
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private properties">
    //<editor-fold defaultstate="collapsed" desc="setSize">
    /**
     * Sets the min, pref and max size of the control
     *
     * @param tmpControl
     * @param aWidth
     * @param aHeight
     */
    private void setSize(Control tmpControl, double aWidth, double aHeight) {
        tmpControl.setMinSize(aWidth, aHeight);
        tmpControl.setPrefSize(aWidth, aHeight);
        tmpControl.setMaxSize(aWidth, aHeight);
    }
    //</editor-fold>
    //</editor-fold>
}
