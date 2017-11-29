package view;

//<editor-fold defaultstate="collapsed" desc="imports">
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import util.Globals;
import util.PropertiesEnum;
import util.PropertiesLoader;
//</editor-fold>

/**
 * CalculationPane for the calculation of descriptors
 *
 * @author Felix Bänsch, 27.05.2016
 */
public class CalculationPane extends AnchorPane {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private BorderPane borderPane;
    private ToolBar toolbar;
    private Pane gapPane;
    private Button closeButton;
    private Button calculateButton;
    private SplitPane mainSplitPane;
    private AnchorPane leftAnchorPane;
    private AnchorPane rightAnchorPane;
    private TabPane tabPane;
    private Tab atomicTab;
    private ContextMenu atomicContextMenu;
    private CheckMenuItem atomicCheckMenuItem;
    private ListView atomicListView;
    private Tab atomPairTab;
    private ContextMenu atomPairContextMenu;
    private CheckMenuItem atomPairCheckMenuItem;
    private ListView atomPairListView;
    private Tab bondTab;
    private ContextMenu bondContextMenu;
    private CheckMenuItem bondCheckMenuItem;
    private ListView bondListView;
    private Tab molecularTab;
    private ContextMenu molecularContextMenu;
    private CheckMenuItem molecularCheckMenuItem;
    private ListView molecularListView;
    private SplitPane rightSplitPane;
    private AnchorPane topAnchorPane;
    private AnchorPane downAnchorPane;
    private TitledPane topTitledPane;
    private TitledPane downTitledPane;
    private TextArea descriptionTextArea;
    private StackPane parameterPane;
    private ToolBar topToolbar;
    private CheckBox checkAll;
    private Button saveDescriptorsButton;
    private Button loadDescriptorButton;
    private Button defaultButton;
    private ProgressBar progressBar;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new CalculationPane instance
     */
    public CalculationPane() {
        super();
        //borderPane
        this.borderPane = new BorderPane();
        MoleculePane.setTopAnchor(this.borderPane, 0.0);
        MoleculePane.setLeftAnchor(this.borderPane, 0.0);
        MoleculePane.setRightAnchor(this.borderPane, 0.0);
        MoleculePane.setBottomAnchor(this.borderPane, 0.0);

        //<editor-fold defaultstate="collapsed" desc="top">
        //topToolbar
        this.topToolbar = new ToolBar();
        this.topToolbar.setPrefHeight(Globals.toolContainerHeight);
        this.topToolbar.setMaxHeight(Globals.toolContainerHeight);
        this.topToolbar.setMinHeight(Globals.toolContainerHeight);
        this.topToolbar.setPadding(new Insets(Globals.toolbarPadding));
        this.topToolbar.setStyle("-fx-background-color: LIGHTGREY");
        this.borderPane.setTop(this.topToolbar);
        //-checkBox
        this.checkAll = new CheckBox();
        this.checkAll.setText(String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.checkDescriptors.toString()), ""));
        this.checkAll.setMinHeight(Globals.controlsHeight);
        this.checkAll.setPrefHeight(Globals.controlsHeight);
        this.checkAll.setMaxHeight(Globals.controlsHeight);
        this.topToolbar.getItems().add(this.checkAll);
        //-gapPane
        Pane tmpGapPane = new Pane();
        HBox.setHgrow(tmpGapPane, Priority.ALWAYS);
        this.topToolbar.getItems().add(tmpGapPane);
        //-default
        this.defaultButton = new Button();
        this.defaultButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.Default.toString()));
        this.defaultButton.setMinHeight(Globals.controlsHeight);
        this.defaultButton.setPrefHeight(Globals.controlsHeight);
        this.defaultButton.setMaxHeight(Globals.controlsHeight);
        Tooltip tmpDefaultTip = new Tooltip();
        tmpDefaultTip.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.DefaultButtonTip.toString()));
        this.defaultButton.setTooltip(tmpDefaultTip);
        this.defaultButton.setDisable(true);//TODO: raus wenn funktion da
        this.topToolbar.getItems().add(this.defaultButton);
        //-saveDescriptors
        this.saveDescriptorsButton = new Button();
        this.saveDescriptorsButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.saveDescriptors.toString()));
        this.saveDescriptorsButton.setMinHeight(Globals.controlsHeight);
        this.saveDescriptorsButton.setPrefHeight(Globals.controlsHeight);
        this.saveDescriptorsButton.setMaxHeight(Globals.controlsHeight);
        Tooltip tmpSaveTip = new Tooltip();
        tmpSaveTip.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.saveDescriptorsTip.toString()));
        this.saveDescriptorsButton.setTooltip(tmpSaveTip);
        this.topToolbar.getItems().add(this.saveDescriptorsButton);
        //-loadDescriptors
        this.loadDescriptorButton = new Button();
        this.loadDescriptorButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.loadDescriptors.toString()));
        this.loadDescriptorButton.setMinHeight(Globals.controlsHeight);
        this.loadDescriptorButton.setPrefHeight(Globals.controlsHeight);
        this.loadDescriptorButton.setMaxHeight(Globals.controlsHeight);
        Tooltip tmpLoadTip = new Tooltip();
        tmpLoadTip.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.TooltipEnum.loadDescriptorsTip.toString()));
        this.loadDescriptorButton.setTooltip(tmpLoadTip);
        this.topToolbar.getItems().add(this.loadDescriptorButton);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="center">
        //mainSplitPane
        this.mainSplitPane = new SplitPane();
        this.mainSplitPane.setDividerPosition(0, 0.5);
        this.leftAnchorPane = new AnchorPane();
        this.rightAnchorPane = new AnchorPane();
        this.mainSplitPane.getItems().addAll(this.leftAnchorPane, this.rightAnchorPane);
        BorderPane.setMargin(this.mainSplitPane, new Insets(Globals.mainPaneInsets));
        this.borderPane.setCenter(this.mainSplitPane);

        //left (TabPane) //TODO: customized Tab with ContextMenu
        this.tabPane = new TabPane();
        //-tab1
        this.atomicTab = new Tab(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.atomic.toString()));
        this.atomicCheckMenuItem = new CheckMenuItem();
        this.atomicCheckMenuItem.setText(String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.checkDescriptors.toString()), PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.atomic.toString())));
        this.atomicContextMenu = new ContextMenu(this.atomicCheckMenuItem);
        this.atomicTab.setContextMenu(this.atomicContextMenu);
        this.atomicTab.setClosable(false);
        this.atomicListView = new ListView();
        this.atomicTab.setContent(this.atomicListView);
        //-tab2
        this.atomPairTab = new Tab(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.atompair.toString()));
        this.atomPairCheckMenuItem = new CheckMenuItem();
        this.atomPairCheckMenuItem.setText(String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.checkDescriptors.toString()), PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.atompair.toString())));
        this.atomPairContextMenu = new ContextMenu(this.atomPairCheckMenuItem);
        this.atomPairTab.setContextMenu(this.atomPairContextMenu);
        this.atomPairTab.setClosable(false);
        this.atomPairListView = new ListView();
        this.atomPairTab.setContent(this.atomPairListView);
        //-tab3
        this.bondTab = new Tab(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.bond.toString()));
        this.bondCheckMenuItem = new CheckMenuItem();
        this.bondCheckMenuItem.setText(String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.checkDescriptors.toString()), PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.bond.toString())));
        this.bondContextMenu = new ContextMenu(this.bondCheckMenuItem);
        this.bondTab.setContextMenu(this.bondContextMenu);
        this.bondTab.setClosable(false);
        this.bondListView = new ListView();
        this.bondTab.setContent(this.bondListView);
        //-tab4
        this.molecularTab = new Tab(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.molecular.toString()));
        this.molecularCheckMenuItem = new CheckMenuItem();
        this.molecularCheckMenuItem.setText(String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.checkDescriptors.toString()), PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.molecular.toString())));
        this.molecularContextMenu = new ContextMenu(this.molecularCheckMenuItem);
        this.molecularTab.setContextMenu(this.molecularContextMenu);
        this.molecularTab.setClosable(false);
        this.molecularListView = new ListView();
        this.molecularTab.setContent(this.molecularListView);
        this.tabPane.getTabs().addAll(this.atomicTab, this.atomPairTab, this.bondTab, this.molecularTab);
        this.leftAnchorPane.getChildren().add(this.tabPane);

        //right
        this.rightSplitPane = new SplitPane();
        this.rightSplitPane.setOrientation(Orientation.VERTICAL);
        this.topAnchorPane = new AnchorPane();
        this.downAnchorPane = new AnchorPane();
        this.rightSplitPane.getItems().addAll(this.topAnchorPane, this.downAnchorPane);
        this.rightAnchorPane.getChildren().add(this.rightSplitPane);
        //-top
        this.topTitledPane = new TitledPane();
        this.topTitledPane.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.parameters.toString()));
        this.topTitledPane.setCollapsible(false);
        this.parameterPane = new StackPane();
//        this.parameterPane.setAlignment(Pos.CENTER);
        this.topTitledPane.setContent(this.parameterPane);
        this.topAnchorPane.getChildren().add(this.topTitledPane);
        //-down
        this.downTitledPane = new TitledPane();
        this.downTitledPane.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.description.toString()));
        this.downTitledPane.setCollapsible(false);
        this.descriptionTextArea = new TextArea();
        this.descriptionTextArea.setEditable(false);
        this.descriptionTextArea.setWrapText(true);
        this.downTitledPane.setContent(this.descriptionTextArea);
        this.downTitledPane.setAlignment(Pos.CENTER);
        this.downAnchorPane.getChildren().add(this.downTitledPane);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="bottom">
        //ToolBar
        this.toolbar = new ToolBar();
        this.borderPane.setBottom(this.toolbar);
        this.toolbar.setPrefHeight(Globals.toolContainerHeight);
        this.toolbar.setMaxHeight(Globals.toolContainerHeight);
        this.toolbar.setMinHeight(Globals.toolContainerHeight);
        this.toolbar.setPadding(new Insets(Globals.toolbarPadding));
        this.toolbar.setStyle("-fx-background-color: LIGHTGREY");
        //-calculateButton
        this.calculateButton = new Button();
        this.calculateButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.calculate.toString()));
        this.calculateButton.setMinHeight(Globals.controlsHeight);
        this.calculateButton.setPrefHeight(Globals.controlsHeight);
        this.calculateButton.setMaxHeight(Globals.controlsHeight);
        this.toolbar.getItems().add(this.calculateButton);
        //-gapPane
        Pane tmpPane = new Pane();
        HBox.setHgrow(tmpPane, Priority.ALWAYS);
        this.toolbar.getItems().add(tmpPane);
        //-progressbar
        this.progressBar = new ProgressBar();
        this.progressBar.setVisible(false);
        this.toolbar.getItems().add(this.progressBar);
        //-gapPane
        this.gapPane = new Pane();
        HBox.setHgrow(this.gapPane, Priority.ALWAYS);
        this.toolbar.getItems().add(this.gapPane);
        //-closeButton
        this.closeButton = new Button();
        this.closeButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.close.toString()));
        this.closeButton.setMinHeight(Globals.controlsHeight);
        this.closeButton.setPrefHeight(Globals.controlsHeight);
        this.closeButton.setMaxHeight(Globals.controlsHeight);
        this.toolbar.getItems().add(this.closeButton);
        //</editor-fold>
        this.getChildren().add(this.borderPane);
        this.getStylesheets().add("/view/MoleculePaneStyle.css");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="bindSize">
    /**
     * Binds the size of the structureTitledPane and the infoAccordion to the
     * left and rigth AnchorPane
     */
    public void bindSize() {
        //borderPane
        this.borderPane.prefHeightProperty().bind(this.heightProperty());
        this.borderPane.prefWidthProperty().bind(this.widthProperty());
        //-center
        //--left
        this.leftAnchorPane.maxWidthProperty().bind(this.mainSplitPane.widthProperty().multiply(0.5));
        this.leftAnchorPane.minWidthProperty().bind(this.mainSplitPane.widthProperty().multiply(0.5));
        this.tabPane.prefHeightProperty().bind(this.leftAnchorPane.heightProperty());
        this.tabPane.prefWidthProperty().bind(this.leftAnchorPane.widthProperty());
        //--right
        this.rightSplitPane.prefHeightProperty().bind(this.rightAnchorPane.heightProperty());
        this.rightSplitPane.prefWidthProperty().bind(this.rightAnchorPane.widthProperty());
        //---top right
        this.topAnchorPane.maxHeightProperty().bind(this.rightSplitPane.heightProperty().multiply(0.5));
        this.topAnchorPane.minHeightProperty().bind(this.rightSplitPane.heightProperty().multiply(0.5));
        this.topAnchorPane.prefWidthProperty().bind(this.rightSplitPane.widthProperty());
        this.topTitledPane.prefWidthProperty().bind(this.topAnchorPane.widthProperty());
        this.topTitledPane.prefHeightProperty().bind(this.topAnchorPane.heightProperty());
        //---down right      
        this.downAnchorPane.prefWidthProperty().bind(this.rightSplitPane.widthProperty());
        this.downTitledPane.prefWidthProperty().bind(this.downAnchorPane.widthProperty());
        this.downTitledPane.prefHeightProperty().bind(this.downAnchorPane.heightProperty());
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="getCloseButton">
    /**
     * Gets the CloseButton
     *
     * @return Button
     */
    public Button getCloseButton() {
        return this.closeButton;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCalculationButton">
    /**
     * Gets the calculationButton
     *
     * @return Button
     */
    public Button getCalculationButton() {
        return this.calculateButton;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getDefaultButton">
    /**
     * Gets the defaultButton
     * @return Button
     */
    public Button getDefaultButton(){
        return this.defaultButton;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getSaveDescriptorsButton">
    /**
     * Gets the saveDescriptorsButton
     * @return Button
     */
    public Button getSaveDescriptorsButton(){
        return this.saveDescriptorsButton;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getLoadDescriptorsButton">
    /**
     * Gets the loadDescriptorsButton
     * @return Button
     */
    public Button getLoadDescriptorsButton(){
        return this.loadDescriptorButton;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCheckAll">
    /**
     * Gets the checkAll CheckBox
     * @return CheckBox
     */
    public CheckBox getCheckAll(){
        return this.checkAll;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getAtomicListView">
    /**
     * Gets the ListView of AtomicTab
     *
     * @return ListView
     */
    public ListView getAtomicListView() {
        return this.atomicListView;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getAtomPairListView">
    /**
     * Gets the ListView of atompairTab
     *
     * @return ListView
     */
    public ListView getAtomPairListView() {
        return this.atomPairListView;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getBondListView">
    /**
     * Gets the ListView of bondTab
     *
     * @return ListView
     */
    public ListView getBondListView() {
        return this.bondListView;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMolecularListView">
    /**
     * Gets the ListView of molecularTab
     *
     * @return ListView
     */
    public ListView getMolecularListView() {
        return this.molecularListView;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getDescriptionTextArea">
    /**
     * Gets the descriptionTextArea
     *
     * @return TextArea
     */
    public TextArea getDescriptionTextArea() {
        return this.descriptionTextArea;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getParameterPane">
    /**
     * Gets the parameterPane
     *
     * @return Pane
     */
    public StackPane getParameterPane() {
        return this.parameterPane;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getAtomicCheckMenuItem">
    /**
     * Gets the atomicCheckMenuItem
     *
     * @return CheckMenuItem
     */
    public CheckMenuItem getAtomicCheckMenuItem() {
        return this.atomicCheckMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getAtomPairCheckMenuItem">
    /**
     * Gets the atomPairCheckMenuItem
     *
     * @return CheckMenuItem
     */
    public CheckMenuItem getAtomPairCheckMenuItem() {
        return this.atomPairCheckMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getBondCheckMenuItem">
    /**
     * Gets the bondCheckMenuItem
     *
     * @return CheckMenuItem
     */
    public CheckMenuItem getBondCheckMenuItem() {
        return this.bondCheckMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMolecularCheckMenuItem">
    /**
     * Gets the molecularCheckMenuItem
     *
     * @return CheckMenuItem
     */
    public CheckMenuItem getMolecularCheckMenuItem() {
        return this.molecularCheckMenuItem;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getTabPane">
    /**
     * Gets the tabPane
     *
     * @return TabPane
     */
    public TabPane getTabPane() {
        return this.tabPane;
    }
    //</editor-fold>
    
    public ProgressBar getProgressBar(){
        return this.progressBar;
    }
    //</editor-fold>
}
