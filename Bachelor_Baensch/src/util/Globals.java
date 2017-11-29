package util;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Class for the global variables and constants
 *
 * @author Felix Bänsch, 17.05.2016
 */
public class Globals {

    //<editor-fold defaultstate="collapsed" desc="view">
    /**
     * Height for all containers (toolbars e.g.)
     */
    public static final double toolContainerHeight = 35.0; //old 40

    /**
     * Padding for toolbars
     */
    public static final double toolbarPadding = 10.0;

    /**
     * Spacing for containers
     */
    public static final double containerSpacing = 3.5;

    /**
     * Height for all controlelements
     */
    public static final double controlsHeight = 25.0;

    /**
     * Width for the moleculesPerPageTextField
     */
    public static final double moleculesPerPageTextFieldWidth = 30;

    /**
     * Width for the pageTextField
     */
    public static final double pageTextFieldWidth = 80.0;

    /**
     * Insets for the mainPane
     */
    public static final double mainPaneInsets = 10.0;

    /**
     * Height for the header of the MainTable
     */
    public static final double headerHeight = 25.0; //Ausgelesen ist 24

    /**
     * Margin for the structure images
     */
    public static final double imageMargin = 10.0;

    /**
     * Width for the checkColumn
     */
    public static final double checkColumnWidth = 40.0;

    /**
     * Width for the counterColum
     */
    public static final double counterColumnWidth = 55.0;

    /**
     * Width for the MoleculePane
     */
    public static final double moleculePaneWidth = 800.0;

    /**
     * Height for the MoleculePane
     */
    public static final double moleculePaneHeight = 600.0;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="controller">
    /**
     * Boolean for the additon of implicit hydrogens on import
     */
    public static boolean isAdditionImplicitHydrogensOnImport = true; //true

    /**
     * Boolean for the decision if it is a SDFile; if true it is an SDFile
     */
    public static boolean isSdfile = false;

    /**
     * Boolean for the decision if it is a MolFile; if true it is an MolFile
     */
    public static boolean isMolFile = false;

    /**
     * Boolean for the decision if it is a SmileFile; if true it is an SmileFile
     */
    public static boolean isSmileFile = false;

    /**
     * initialvalue (int) for the molecules which where shown per page
     */
    public static int moleculesPerPage = 3;

    /**
     * Strings used for file type to differ the input files
     */
    public static final String sdf = "SDF";
    public static final String mol = "Mol";
    public static final String smile = "SMILE";

    /**
     * Strings used for type to create the appropriate list
     */
    public static final String atomic = "atomic";
    public static final String atomPair = "atompair";
    public static final String bond = "bond";
    public static final String molecular = "molecular";

    /**
     * String for the version number
     */
    public static final String versionNumber = "0.1";

    /**
     * Unique ID for internal files check
     */
    public static final byte[] name = "Felix".getBytes();
    public static final String uuid = UUID.nameUUIDFromBytes(name).toString();

    /**
     * int for the height and width of the scene for the mainPane
     */
    public static final int mainPaneHeight = 800;
    public static final int mainPaneWidth = 800;

    /**
     * Strings used for decision wether save SMILES with or without hydrogens
     */
    public static final String withHydrogens = "withHydrogens";
    public static final String withoutHydrogens = "withoutHydrogens";

    /**
     * Strings for Descriptorsaving
     */
    public static final String all = "all";
    public static final String selected = "selected";
    public static final String csv = "csv";
    public static final String ascii = "ascii";
    public static final String saveSdf = "sdf";
    public static final String saveTxt = "txt";    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="model">
    /**
     * Pattern for String check
     */
    public static final Pattern asciiPattern = Pattern.compile("\\p{ASCII}*$");

    /**
     * Pattern for name check
     */
    public static final Pattern namePattern = Pattern.compile("(?i)Name(.*)");

    /**
     * Pattern for smiles check
     */
//    public static final Pattern smileNamePattern = Pattern.compile(":");
    //</editor-fold>
}
