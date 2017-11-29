package view;

//<editor-fold defaultstate="collapsed" desc="imports">
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.Globals;
import util.PropertiesEnum;
import util.PropertiesLoader;
//</editor-fold>

/**
 * MoleculeView 
 * Shows the information for one molecule
 * 
 * @author Felix Bänsch, 19.08.2015
 */
public class MoleculePane extends AnchorPane {
    
    //<editor-fold defaultstate="collapsed" desc="private variables">
    private BorderPane borderPane;
    private SplitPane splitPane; 
    private AnchorPane leftAnchorPane;
    private AnchorPane rightAnchorPane;
    private AnchorPane descriptorAnchorPane;
    private TitledPane structureTitledPane;
    private StackPane structurePane;
    private Accordion infoAccordion;
    private TitledPane infoTitledPane;
    private ListView infoList;
    private TitledPane propertiesTitledPane;
    private ListView propertiesList;
    private TitledPane descriptorTitledPane;
    private TabPane tabPane;
    private Tab atomicTab;
    private ListView atomicListView;
    private Tab atomPairTab;
    private ListView atomPairListView;
    private Tab bondTab;
    private ListView bondListView;
    private Tab molecularTab;
    private ListView molecularListView;
    private ToolBar toolbar;
    private Pane gapPane;
    private Button closeButton;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new MoleculePane instance
     */
    public MoleculePane(){
        super();
        //borderPane
        this.borderPane = new BorderPane();
        MoleculePane.setTopAnchor(this.borderPane, 0.0);
        MoleculePane.setLeftAnchor(this.borderPane, 0.0);
        MoleculePane.setRightAnchor(this.borderPane, 0.0);
        MoleculePane.setBottomAnchor(this.borderPane, 0.0);
        HBox.setHgrow(this.borderPane, Priority.ALWAYS);
        VBox.setVgrow(this.borderPane, Priority.ALWAYS);        
        
        //<editor-fold defaultstate="collapsed" desc="center">
        //splitPane
        this.splitPane = new SplitPane();
        this.leftAnchorPane = new AnchorPane();
        this.rightAnchorPane = new AnchorPane();
        this.splitPane.getItems().addAll(this.leftAnchorPane, this.rightAnchorPane);
        BorderPane.setMargin(this.splitPane, new Insets(Globals.mainPaneInsets));
        this.borderPane.setCenter(this.splitPane);
        
        //left
        this.structureTitledPane = new TitledPane();
        this.structureTitledPane.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MoleculePaneEnum.structureHeader.toString()));
        this.structureTitledPane.setCollapsible(false);
        this.structurePane = new StackPane();
        this.structurePane.setPadding(new Insets(Globals.mainPaneInsets));
        this.structureTitledPane.setContent(this.structurePane);
        this.leftAnchorPane.getChildren().add(this.structureTitledPane);
        this.leftAnchorPane.setTopAnchor(this.structureTitledPane, 0.0);
        this.leftAnchorPane.setLeftAnchor(this.structureTitledPane, 0.0);
        this.leftAnchorPane.setRightAnchor(this.structureTitledPane, 0.0);
        this.leftAnchorPane.setBottomAnchor(this.structureTitledPane, 0.0);
        
        //right
        //-info
        this.infoTitledPane = new TitledPane();
        this.infoTitledPane.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MoleculePaneEnum.infoHeader.toString()));       
        this.infoList = new ListView();
        this.infoList.setPadding(new Insets(Globals.mainPaneInsets));
        this.infoTitledPane.setContent(this.infoList);
        //-properties
        this.propertiesTitledPane = new TitledPane();
        this.propertiesTitledPane.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MoleculePaneEnum.propertiesHeader.toString()));
        this.propertiesList = new ListView();
        this.propertiesList.setPadding(new Insets(Globals.mainPaneInsets));
        this.propertiesTitledPane.setContent(this.propertiesList);
        //-descriptors
        this.descriptorTitledPane = new TitledPane();
        this.descriptorTitledPane.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MoleculePaneEnum.descriptorHeader.toString()));        
        this.descriptorAnchorPane = new AnchorPane();
        this.descriptorTitledPane.setContent(this.descriptorAnchorPane);
        //--tabPane
        this.tabPane = new TabPane();
//        this.descriptorAnchorPane.setPadding(new Insets(Globals.mainPaneInsets));
        this.descriptorAnchorPane.getChildren().add(this.tabPane);
        this.descriptorAnchorPane.setTopAnchor(this.tabPane, 0.0);
        this.descriptorAnchorPane.setLeftAnchor(this.tabPane, 0.0);
        this.descriptorAnchorPane.setRightAnchor(this.tabPane, 0.0);
        this.descriptorAnchorPane.setBottomAnchor(this.tabPane, 0.0);
        //---atomicTab
        this.atomicTab = new Tab(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.atomic.toString()));
        this.atomicTab.setClosable(false);
        this.atomicListView = new ListView();
        this.atomicListView.setPadding(new Insets(Globals.mainPaneInsets));
        this.atomicTab.setContent(this.atomicListView);
        //---atomPairTab
        this.atomPairTab = new Tab(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.atompair.toString()));
        this.atomPairTab.setClosable(false);
        this.atomPairListView = new ListView();
        this.atomPairListView.setPadding(new Insets(Globals.mainPaneInsets));
        this.atomPairTab.setContent(this.atomPairListView);
        //---bondTab
        this.bondTab = new Tab(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.bond.toString()));
        this.bondTab.setClosable(false);
        this.bondListView = new ListView();
        this.bondListView.setPadding(new Insets(Globals.mainPaneInsets));
        this.bondTab.setContent(this.bondListView);
        //---molecularTab
        this.molecularTab = new Tab(PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.molecular.toString()));
        this.molecularTab.setClosable(false);
        this.molecularListView = new ListView();
        this.molecularListView.setPadding(new Insets(Globals.mainPaneInsets));
        this.molecularTab.setContent(this.molecularListView);
        this.tabPane.getTabs().addAll(this.atomicTab, this.atomPairTab, this.bondTab, this.molecularTab);
        //-accordion
        this.infoAccordion = new Accordion();
        this.infoAccordion.getPanes().addAll(this.infoTitledPane, this.propertiesTitledPane, this.descriptorTitledPane);
        this.infoAccordion.setExpandedPane(this.descriptorTitledPane);
        this.rightAnchorPane.getChildren().add(this.infoAccordion);
        this.rightAnchorPane.setTopAnchor(this.infoAccordion, 0.0);
        this.rightAnchorPane.setLeftAnchor(this.infoAccordion, 0.0);
        this.rightAnchorPane.setRightAnchor(this.infoAccordion, 0.0);
        this.rightAnchorPane.setBottomAnchor(this.infoAccordion, 0.0);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="bottom">
        //toolBar
        this.toolbar = new ToolBar();
        this.borderPane.setBottom(this.toolbar);
        this.toolbar.setPrefHeight(Globals.toolContainerHeight);
        this.toolbar.setMaxHeight(Globals.toolContainerHeight);
        this.toolbar.setMinHeight(Globals.toolContainerHeight);
        this.toolbar.setPadding(new Insets(Globals.toolbarPadding));
        this.toolbar.setStyle("-fx-background-color: LIGHTGREY");
        //-gapPane
        this.gapPane = new Pane();
        HBox.setHgrow(this.gapPane, Priority.ALWAYS);
        this.toolbar.getItems().add(this.gapPane);
        //-closeButton
        this.closeButton = new Button();
        this.closeButton.setText(PropertiesLoader.getInstance().getValue(PropertiesEnum.MoleculePaneEnum.close.toString()));
        this.closeButton.setMinHeight(Globals.controlsHeight);
        this.closeButton.setPrefHeight(Globals.controlsHeight);
        this.closeButton.setMaxHeight(Globals.controlsHeight);
        this.toolbar.getItems().add(this.closeButton);
        //</editor-fold>      
        this.getChildren().add(this.borderPane);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="bindSize">
    /**
     * Binds the size of the structureTitledPane and the infoAccordion to the
     * left and rigth AnchorPane
     */
    public void bindSize(){
        //borderPane
        this.borderPane.prefHeightProperty().bind(this.heightProperty());
        this.borderPane.prefWidthProperty().bind(this.widthProperty());
        //-left
        this.structureTitledPane.prefHeightProperty().bind(this.leftAnchorPane.heightProperty());
        this.structureTitledPane.prefWidthProperty().bind(this.leftAnchorPane.widthProperty());
        this.structurePane.prefHeightProperty().bind(this.structureTitledPane.heightProperty());
        this.structurePane.prefWidthProperty().bind(this.structureTitledPane.widthProperty());
        //-right
        this.rightAnchorPane.maxWidthProperty().bind(this.splitPane.widthProperty().multiply(0.5));
        this.rightAnchorPane.minWidthProperty().bind(this.splitPane.widthProperty().multiply(0.5));
        //--accordion
        this.infoAccordion.prefHeightProperty().bind(this.rightAnchorPane.heightProperty());
        this.infoAccordion.prefWidthProperty().bind(this.rightAnchorPane.widthProperty());
        //---descriptorTitledPane                       
        this.descriptorTitledPane.maxWidthProperty().bind(this.rightAnchorPane.widthProperty());
        this.descriptorTitledPane.minWidthProperty().bind(this.rightAnchorPane.widthProperty());
        
//        this.descriptorAnchorPane.maxWidthProperty().bind(this.descriptorTitledPane.widthProperty());
//        this.descriptorAnchorPane.minWidthProperty().bind(this.descriptorTitledPane.widthProperty());
//        this.descriptorAnchorPane.maxHeightProperty().bind(this.descriptorTitledPane.heightProperty());
//        this.descriptorAnchorPane.minHeightProperty().bind(this.descriptorTitledPane.heightProperty());
//        this.tabPane.prefWidthProperty().bind(this.descriptorAnchorPane.widthProperty());
//        this.tabPane.prefHeightProperty().bind(this.descriptorAnchorPane.heightProperty());        
    }
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="public propeties">
    //<editor-fold defaultstate="collapsed" desc="getInfoListView">
    /**
     * Gets the infoListView
     * @return ListView
     */
    public ListView getInfoListView(){
        return this.infoList;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getPropertiesListView">
    /**
     * Gets the propertiesListView
     * @return ListView
     */
    public ListView getPropertiesListView(){
        return this.propertiesList;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getDescriptorListView">
    /**
     * Gets the descriptorListView
     * @return ListView
     */
//    public ListView getDescriptorListView(){
//        return this.descriptorList;
//    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getStructurePane">
    /**
     * Gets the structureAnchorPane
     * @return AnchorPane
     */
    public StackPane getStructurePane(){
        return this.structurePane;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCloseButton">
    /**
     * Gets the closeButton
     * @return Button
     */
    public Button getCloseButton(){
        return this.closeButton;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="????">
    public AnchorPane getDesAnchorPane(){
        return this.descriptorAnchorPane;
    }
    
    public TitledPane getDesTitledPane(){
        return this.descriptorTitledPane;
    }
    
    public  TabPane getTabPane(){
        return this.tabPane;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getAtomicListView">
    /**
     * Gets the ListView for the IAtomicDescriptors
     * @return ListView
     */
    public ListView getAtomicListView(){
        return this.atomicListView;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getAtomPairListView">
    /**
     * Gets the ListView for the IAtomPairDescriptors
     * @return ListView
     */
    public ListView getAtomPairListView(){
        return this.atomPairListView;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getBondListView">
    /**
     * Gets the ListView for the IBondDescriptors
     * @return ListView
     */
    public ListView getBondListView(){
        return this.bondListView;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getMolecularListView">
    /**
     * Gets the ListView for the IMolecularDescriptors
     * @return ListView
     */
    public ListView getMolecularListView(){
        return this.molecularListView;
    }
    //</editor-fold>
    //</editor-fold>
}
