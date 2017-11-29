package model;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.IDescriptor;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import util.PropertiesEnum;
import util.PropertiesLoader;
import util.UtilityMethods;
//</editor-fold>

/**
 * Class for the molecule data
 *
 * @author Felix Bänsch, 17.05.2016
 */
public class Molecule {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private Identifier identifier;                          //Instance for the identifier of the molecules
    private IAtomContainer container;                       //Instance for the IAtomContainer
    private Image image;                                    //Instance for the image of the molecule structure
//    private AtomContainerRenderer renderer;
    private Boolean check;                                  //Boolean if the molecule is checked or not
    private ConcurrentHashMap<IDescriptor, IDescriptorResult> resultsMap;   //ConcurrentHashMap for the IDescriptors and the corresponding value; threadsafe
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new object of the molecule class
     *
     * @param anIdentifier Identifier
     * @param aContainer AtomContainer
     */
    public Molecule(Identifier anIdentifier, IAtomContainer aContainer) {
        this.identifier = anIdentifier;
        this.container = aContainer;
        this.setCheck(false);
        this.resultsMap = new ConcurrentHashMap<>();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="createImage">
    /**
     * Creates an image of the atomContainer with given width and height
     *
     * @param aWidth int
     * @param aHeight int
     * @return Image
     */
    public Image createImage(int aWidth, int aHeight) {
        try {
            DepictionGenerator tmpGenerator = new DepictionGenerator();
            tmpGenerator = tmpGenerator.withAtomColors().withSize(aWidth, aHeight);
            BufferedImage tmpBufferedImage = tmpGenerator.depict(this.container).toImg();
            this.image = SwingFXUtils.toFXImage(tmpBufferedImage, null);
            return this.image;
        } catch (Exception ex) {
            return this.createErrorImage(ex.getMessage(), aWidth, aHeight);
        }
        //<editor-fold defaultstate="collapsed" desc="old">
//            try {
//                GraphicsConfiguration tmpGraphicsConfig = GraphicsEnvironment.getLocalGraphicsEnvironment()
//                        .getDefaultScreenDevice().getDefaultConfiguration();
//                List<IGenerator<IAtomContainer>> tmpGenerators = new ArrayList<>();
//                tmpGenerators.add(new BasicSceneGenerator());
//                tmpGenerators.add(new BasicBondGenerator());
//                tmpGenerators.add(new ExtendedAtomGenerator());
//                this.renderer = new AtomContainerRenderer(tmpGenerators, new AWTFontManager());
//                RendererModel tmpModel = this.renderer.getRenderer2DModel();
//                tmpModel.set(ExtendedAtomGenerator.ColorByType.class, Boolean.TRUE);
//                tmpModel.set(ExtendedAtomGenerator.ShowImplicitHydrogens.class, Boolean.TRUE);
//                BufferedImage tmpBufferedImage = tmpGraphicsConfig.createCompatibleImage(aWidth, aHeight);
//                Graphics2D tmpGraphic = (Graphics2D) tmpBufferedImage.getGraphics().create();
//                tmpGraphic.setColor(Color.WHITE);
//                tmpGraphic.fillRect(0, 0, aWidth, aHeight);
//                this.renderer.paint(this.container, new AWTDrawVisitor(tmpGraphic),
//                        new Rectangle(0, 0, aWidth, aHeight), true);
//                tmpGraphic.dispose();
//                this.image = SwingFXUtils.toFXImage(tmpBufferedImage, null);
//                return this.image;
//            } catch (Exception ex) {
//                return this.createErrorImage(ex.getMessage(), aWidth, aHeight);
//            }
        //</editor-fold>
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">
    //<editor-fold defaultstate="collapsed" desc="createErrorImage">
    /**
     * Creates an image of the given message
     *
     * @param aMessage
     * @param aWidth
     * @param aHeight
     */
    private Image createErrorImage(String aMessage, int aWidth, int aHeight) {
        String tmpMessage;
        if (aMessage == null) {
            tmpMessage = PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.depictError.toString());
        } else {
            tmpMessage = aMessage;
        }
        BufferedImage tmpBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tmpGraphic = tmpBufferedImage.createGraphics();
        Font tmpFont = new Font("Arial", Font.PLAIN, 20);
        tmpGraphic.setFont(tmpFont);
        tmpGraphic.dispose();
        tmpBufferedImage = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);
        tmpGraphic = tmpBufferedImage.createGraphics();
        tmpGraphic.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        tmpGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        tmpGraphic.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        tmpGraphic.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        tmpGraphic.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        tmpGraphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        tmpGraphic.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        tmpGraphic.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        tmpGraphic.setFont(tmpFont);
        FontMetrics tmpFontMetrics = tmpGraphic.getFontMetrics();
        tmpGraphic.setColor(Color.BLACK);
        tmpGraphic.drawString(tmpMessage, 0, tmpFontMetrics.getAscent());
        tmpGraphic.dispose();
        tmpGraphic.drawImage(tmpBufferedImage, 0, 0, null);
        this.image = SwingFXUtils.toFXImage(tmpBufferedImage, null);
        return this.image;
    }
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="getImage">
    /**
     * Gets the image
     *
     * @return image
     */
    public Image getImage() {
        return this.image;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getIdentifier">
    /**
     * Gets the identifier
     *
     * @return identifier
     */
    public Identifier getIdentifier() {
        return this.identifier;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getName">
    /**
     * Gets the name
     *
     * @return string
     */
    public String getName() {
        Map<Object, Object> tmpMap = this.container.getProperties();
        for (Map.Entry<Object, Object> tmpEntry : tmpMap.entrySet()) {
            if (UtilityMethods.matchKeyName(tmpEntry.getKey().toString())) {
                return UtilityMethods.getOnlyAscii(tmpEntry.getValue().toString());
            }
        }
        return PropertiesLoader.getInstance().getValue(PropertiesEnum.MainPaneEnum.unnamed.toString());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getProperties">
    /**
     * Gets the properties of the molecule at given index
     *
     * @return Map
     */
    public Map getProperties() {
        return this.container.getProperties();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMass">
    /**
     * Gets the mass of the molecule at given index
     *
     * @return double mass
     */
    public double getMass() {
        return Math.round(MolecularFormulaManipulator.getNaturalExactMass(
                MolecularFormulaManipulator.getMolecularFormula(
                        this.container)));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getAtomCount">
    /**
     * Gets the atomcount of the molecule at given index
     *
     * @return int
     */
    public int getAtomCount() {
        return this.container.getAtomCount();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getBondCount">
    /**
     * Gets the bondcount of the molecule at given index
     *
     * @return int
     */
    public int getBondCount() {
        return this.container.getBondCount();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCharge">
    /**
     * Gets the charge of the molecule at given index
     *
     * @return int
     */
    public int getCharge() {
        return MolecularFormulaManipulator.
                getMolecularFormula(this.container).getCharge();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getFormula">
    /**
     * Gets the formula
     *
     * @return string
     */
    public String getFormula() {
        return MolecularFormulaManipulator.getMolecularFormula(this.container).toString();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getCheck">
    /**
     * Gets the boolean if it is checked or unchecked
     *
     * @return boolean
     */
    public Boolean isChecked() {
        return this.check;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getImageView">
    /**
     * Gets the imageView
     *
     * @return ImageView
     */
//    public ImageView getImageView(){
//        return this.imageView = new ImageView(this.image);
//    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getIAtomContainer">
    /**
     * Gets the IAtomContainer
     *
     * @return IAtomContainer
     */
    public IAtomContainer getIAtomContainer() {
        return this.container;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setCheck">
    /**
     * Sets the check boolean if it is checked or unchecked
     *
     * @param aCheck boolean
     */
    public void setCheck(boolean aCheck) {
        if (!aCheck) {
            this.check = Boolean.FALSE;
        } else {
            this.check = Boolean.TRUE;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addResult">
    /**
     * Adds a key-value pair (IDescriptor - result) to the resultMap
     * @param aKey IDescriptor
     * @param aValue String
     */
    public synchronized void addResult(IDescriptor aKey, IDescriptorResult aValue){
        this.resultsMap.put(aKey, aValue); //TODO: raus weil überflüssig
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getResultsMap">
    /**
     * Gets the resultsMap
     * @return HashMap
     */
    public ConcurrentHashMap getResultsMap(){
        return this.resultsMap;
    }
    //</editor-fold>
    //</editor-fold>
}
