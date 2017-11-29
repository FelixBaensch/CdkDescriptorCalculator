package util;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javafx.scene.control.Alert;
import model.Data;
import org.openscience.cdk.dict.Dictionary;
import org.openscience.cdk.dict.DictionaryDatabase;
import org.openscience.cdk.dict.Entry;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.IAtomPairDescriptor;
import org.openscience.cdk.qsar.IAtomicDescriptor;
import org.openscience.cdk.qsar.IBondDescriptor;
import org.openscience.cdk.qsar.IDescriptor;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.IDescriptorResult;
//</editor-fold>

/**
 * Class for utilityMethods
 *
 * @author Felix Bänsch, 23.05.2016
 */
public class UtilityMethods {

    //<editor-fold defaultstate="collapsed" desc="private static variables">
    private static Dictionary dictionary;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private static final variables">
    private static String seperator;
    private final static Charset encoding = StandardCharsets.UTF_8;
    private final static Charset ascii = StandardCharsets.US_ASCII;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getOnlyAscii">
    /**
     * Checks every char of the given index and returns only ASCII-Code
     *
     * @param aString String
     * @return String
     */
    public static String getOnlyAscii(String aString) {
        if (aString == null) {
            return "";
        } else {
            Matcher tmpMatcher;
            StringBuilder tmpStringBuilder = new StringBuilder();
            char[] tmpArray = aString.toCharArray();
            for (char tmpChar : tmpArray) {
                tmpMatcher = Globals.asciiPattern.matcher(Character.toString(tmpChar));
                if (tmpMatcher.matches()) {
                    tmpStringBuilder.append(tmpChar);
                } else {
                    tmpStringBuilder.append("?");
                }
            }
            return tmpStringBuilder.toString();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="matchKeyName">
    /**
     * Checks if the Key matches to "(?i)Name(.*)"
     *
     * @param aNameKey String
     * @return true if match boolean
     */
    public static Boolean matchKeyName(String aNameKey) {
        Matcher tmpMatcher = Globals.namePattern.matcher(aNameKey);
        return tmpMatcher.find();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="instantiateDescriptors">
    /**
     * Instantiates descriptors from a list of descriptor class names.
     *
     * @see org.openscience.cdk.qsar.DescriptorEngine
     * @param tmpDescriptorClassNames lst od descriptor class names
     * @return list of the descriptors from the class names
     */
    public static List<IDescriptor> instantiateDescriptors(List<String> tmpDescriptorClassNames) {
        List<IDescriptor> tmpDescriptors;
        tmpDescriptors = new ArrayList<>();
        for (String tmpDescriptorName : tmpDescriptorClassNames) {
            try {
                IDescriptor tmpDescriptor = (IDescriptor) UtilityMethods.class.getClassLoader().loadClass(tmpDescriptorName).newInstance();
                tmpDescriptors.add(tmpDescriptor);
            } catch (NoClassDefFoundError exception) {
                Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, "Following Descriptor could not be found: ", tmpDescriptorName);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
                Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, "Following Descriptor could not be loaded: ", tmpDescriptorName);
            }
        }
        return tmpDescriptors;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getDescriptorName">
    /**
     * Returns the name of a descriptor as String.
     *
     * @param tmpDescriptor descriptor
     * @return descriptor name
     */
    public static String getDescriptorName(IDescriptor tmpDescriptor) {
        String tmpName = tmpDescriptor.getSpecification().getImplementationTitle();
        if (tmpDescriptor instanceof IMolecularDescriptor) {
            tmpName = tmpName.replace("org.openscience.cdk.qsar.descriptors.molecular.", new String());
        } else if (tmpDescriptor instanceof IAtomicDescriptor) {
            tmpName = tmpName.replace("org.openscience.cdk.qsar.descriptors.atomic.", new String());
        } else if (tmpDescriptor instanceof IAtomPairDescriptor) {
            tmpName = tmpName.replace("org.openscience.cdk.qsar.descriptors.atompair.", new String());
        } else if (tmpDescriptor instanceof IBondDescriptor) {
            tmpName = tmpName.replace("org.openscience.cdk.qsar.descriptors.bond.", new String());
        } else {
            Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, "Unsupported Descriptor", tmpDescriptor.getClass());
        }
        tmpName = tmpName.replace("Descriptor", new String());
        return tmpName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getDescriptorDefinition">
    /**
     * Returns the definition if a descriptor from a dictionary.
     *
     * @param aSpecification DescriptorSpecification
     * @see org.openscience.cdk.qsar.DescriptorEngine
     * @return descriptor's definition
     */
    public static synchronized String getDescriptorDefinition(DescriptorSpecification aSpecification) {
        if (UtilityMethods.dictionary == null) {
            DictionaryDatabase tmpDatabase = new DictionaryDatabase();
            UtilityMethods.dictionary = tmpDatabase.getDictionary("descriptor-algorithms");
        }
        Entry[] tmpEntries = UtilityMethods.dictionary.getEntries();
        String tmpDefinition = PropertiesLoader.getInstance().getValue(PropertiesEnum.CalculationPaneEnum.noDescription.toString());
        String tmpReference = aSpecification.getSpecificationReference().toLowerCase().split("#")[1];
        for (Entry tmpEntry : tmpEntries) {
            if (!tmpEntry.getClassName().equals("Descriptor")) {
                continue;
            }
            if (tmpEntry.getID().equals(tmpReference.toLowerCase())) {
                tmpDefinition = UtilityMethods.getOnlyAscii(tmpEntry.getDefinition());
                break;
            }
        }
        return tmpDefinition.trim();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hasParamters">
    /**
     * Checks if the given IDescriptor has parameters
     *
     * @param aDescriptor IDescriptor
     * @return boolean
     */
    public static boolean hasParameters(IDescriptor aDescriptor) {
        return (aDescriptor.getParameters() != null && aDescriptor.getParameters().length != 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="isDescriptorTypeOf">
    /**
     * Checks if a descriptor is of a special descriptor type.
     *
     * @param tmpDescriptor descriptors
     * @param tmpType descriptorUtil.ATOMIC, DescriptorUtil.ATOMPAIR,
     * DescriptorUtil.BOND or case DescriptorUtil.MOLECULAR
     * @return true if descriptor is of the specified type
     */
    public static boolean isDescriptorTypeOf(IDescriptor tmpDescriptor, String tmpType) {
        String tmpName = tmpDescriptor.getSpecification().getImplementationTitle();
        switch (tmpType) {
            case Globals.atomic:
                return tmpName.startsWith("org.openscience.cdk.qsar.descriptors.atomic");
            case Globals.atomPair:
                return tmpName.startsWith("org.openscience.cdk.qsar.descriptors.atompair");
            case Globals.bond:
                return tmpName.startsWith("org.openscience.cdk.qsar.descriptors.bond");
            case Globals.molecular:
                return tmpName.startsWith("org.openscience.cdk.qsar.descriptors.molecular");
            default:
                Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, "Unknown Descriptor Type: {0}", tmpDescriptor.getClass());//TODO
                return false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="writeDescriptors">
    /**
     * Writes the given descriptors to an CSV file. When it gets an Exception an
     * ErrorDialog will open
     *
     * @param aDescriptors List
     */
    public static void writeDescriptors(List<IDescriptor> aDescriptors) {
        Path tmpDescriptorPath = Paths.get(System.getProperty("user.dir") + "/" + "descriptors.dsf"); //dsf = descriptor selection file
        BufferedWriter tmpWriter = null;
        try {
            //write descriptors
            tmpWriter = Files.newBufferedWriter(tmpDescriptorPath, UtilityMethods.encoding);
            tmpWriter.write(Globals.uuid + " " + Globals.versionNumber);
            tmpWriter.newLine();
            for (IDescriptor tmpDescriptor : aDescriptors) {
                String tmpLine = new String();
                tmpLine += tmpDescriptor.getSpecification().getImplementationTitle();
                if (UtilityMethods.hasParameters(tmpDescriptor)) {
                    for (Object tmpParameter : tmpDescriptor.getParameters()) {
                        tmpLine += UtilityMethods.seperator + tmpParameter.toString();
                    }
                }
                tmpWriter.write(tmpLine);
                tmpWriter.newLine();
            }
        } catch (IOException ex) {
            Dialog tmpDialog = new Dialog(PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.error.toString()),
                    null, String.format(PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.errormessage.toString()), "Descriptors"), Alert.AlertType.ERROR);
            tmpDialog.showAndWait();
        } finally {
            if (tmpWriter != null) {
                try {
                    tmpWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="readDescriptors">
    /**
     * Reads out the file and if applicable it adds the descriptors and
     * parameters to the returned list When it comes to an Exception an
     * ErrorDialog will open
     *
     * @param aDescriptors List
     * @return List
     */
    public static List<IDescriptor> readDescriptors(List<IDescriptor> aDescriptors) {
        List<IDescriptor> tmpList = new ArrayList<>();
//        Path tmpPath = Paths.get(aFile.getPath());
        Path tmpDescriptorPath = Paths.get(System.getProperty("user.dir") + "/" + "descriptors.dsf"); //dsf = descriptor selection file
        String tmpCheckLine = Globals.uuid + " " + Globals.versionNumber;
        try {
            Scanner tmpScanner = new Scanner(tmpDescriptorPath, UtilityMethods.encoding.name());
            if (tmpScanner.nextLine().equals(tmpCheckLine)) {
                while (tmpScanner.hasNextLine()) {
                    String tmpLine = tmpScanner.nextLine();
                    String[] tmpValues = tmpLine.split(UtilityMethods.seperator);
                    IDescriptor tmpReadDescriptor = null;
                    for (IDescriptor tmpDescriptor : aDescriptors) {
                        if (tmpDescriptor.getSpecification().getImplementationTitle().equals(tmpValues[0])) {
                            tmpReadDescriptor = tmpDescriptor;
                            break;
                        }
                    }
                    if (UtilityMethods.hasParameters(tmpReadDescriptor)) {
                        Object[] tmpParameters = tmpReadDescriptor.getParameters();
                        for (int i = 0; i < tmpParameters.length; i++) {
                            if (tmpParameters[i] instanceof Boolean) {
                                tmpParameters[i] = Boolean.parseBoolean(tmpValues[i + 1]);
                            } else if (tmpParameters[i] instanceof Integer) {
                                tmpParameters[i] = Integer.parseInt(tmpValues[i + 1]);
                            } else if (tmpParameters[i] instanceof Double) {
                                tmpParameters[i] = Double.parseDouble(tmpValues[i + 1]);
                            } else if (tmpParameters[i] instanceof String) {
                                tmpParameters[i] = tmpValues[i + 1];
                            } else {
                                Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, "Unknown Parameter Type in Descriptor " + tmpValues[0] + ": " + tmpParameters[i].getClass().getName());
                            }
                        }
                        try {
                            tmpReadDescriptor.setParameters(tmpParameters);
                        } catch (CDKException ex) {
                            Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, "Could not set Parameters for Descriptor: " + tmpValues[0], ex);
                        }
                    }
                    tmpList.add(tmpReadDescriptor);
                }
                return tmpList;
            } else {
                Dialog tmpDialog = new Dialog("Error", null, "Could not read descriptors", Alert.AlertType.ERROR);
                tmpDialog.showAndWait();
            }
        } catch (IOException ex) {
            Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, null, ex);
            Dialog tmpDialog = new Dialog("Error", null, "Could not read file", Alert.AlertType.ERROR);
            tmpDialog.showAndWait();
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="writeAllDescriptorValues">
    /**
     * Writes descriptorvalues for all molecules to an CSV file
     *
     * @param aFile File
     * @param aData Data
     */
    public static void writeAllDescriptorValues(File aFile, Data aData) {
        Path tmpPath = Paths.get(aFile.getPath());
        try {
            BufferedWriter tmpWriter = Files.newBufferedWriter(tmpPath, UtilityMethods.encoding);
            String tmpLine;
            ConcurrentHashMap<IDescriptor, IDescriptorResult> tmpMap = null;
            int tmpLoop = aData.getMoleculeCount();
            for (int i = 1; i <= tmpLoop; i++) {
                if (aData.hasKey(i)) {
                    //first line contains calculated descriptor titles
                    tmpMap = aData.getMolecule(i).getResultsMap();
                    tmpLine = "Molecule";
                    for (Object tmpKey : tmpMap.keySet()) {
                        tmpLine += UtilityMethods.seperator + UtilityMethods.getDescriptorName((IDescriptor) tmpKey);
                    }
                    tmpWriter.write(tmpLine);
                    tmpWriter.newLine();
                    break;
                } else {
                    tmpLoop++;
                }
            }
            tmpLoop = aData.getMoleculeCount();
            for (int i = 1; i <= tmpLoop; i++) {
                if (aData.hasKey(i)) {
                    //next line contains molecule name and descriptor values
                    tmpMap = aData.getMolecule(i).getResultsMap();
                    tmpLine = aData.getMolecule(i).getName();
                    for (Object tmpValue : tmpMap.values()) {
                        if (tmpValue == null) {
                            tmpLine += UtilityMethods.seperator + "NaN";
                        } else {
                            tmpLine += UtilityMethods.seperator + tmpValue.toString();
                        }
                    }
                    tmpWriter.write(tmpLine);
                    tmpWriter.newLine();
                } else {
                    tmpLoop++;
                }
            }
            tmpWriter.close();
        } catch (Exception ex) {
            Dialog tmpDialog = new Dialog("Error", "Not able to save desciptors", ex.toString(), Alert.AlertType.ERROR);
            tmpDialog.showAndWait();
//            Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="writeSelectedDescriptorValues">
    /**
     * Writes descriptorvalues for selected molecules to a CSV file
     *
     * @param aFile File
     * @param aData Data
     */
    public static void writeSelectedDescriptorValues(File aFile, Data aData) {
        Path tmpPath = Paths.get(aFile.getPath());
        try {
            BufferedWriter tmpWriter = Files.newBufferedWriter(tmpPath, UtilityMethods.encoding);
            String tmpLine;
            ConcurrentHashMap<IDescriptor, IDescriptorResult> tmpMap = null;
            int tmpLoop = aData.getMoleculeCount();
            for (int i = 1; i <= tmpLoop; i++) {
                if (aData.hasKey(i)) {
                    //first line contains calculated descriptor titles
                    tmpMap = aData.getMolecule(i).getResultsMap();
                    tmpLine = "Molecule";
                    for (Object tmpKey : tmpMap.keySet()) {
                        tmpLine += UtilityMethods.seperator + UtilityMethods.getDescriptorName((IDescriptor) tmpKey);
                    }
                    tmpWriter.write(tmpLine);
                    tmpWriter.newLine();
                    break;
                } else {
                    tmpLoop++;
                }
            }
            tmpLoop = aData.getMoleculeCount();
            for (int i = 1; i <= tmpLoop; i++) {
                if (aData.hasKey(i)) {
                    if (aData.getMolecule(i).isChecked()) {
                        //next line contains molecule name and descriptor values
                        tmpLine = aData.getMolecule(i).getName();
                        tmpMap = aData.getMolecule(i).getResultsMap();
                        for (Object tmpValue : tmpMap.values()) {
                            if (tmpValue == null) {
                                tmpLine += UtilityMethods.seperator + "NaN";
                            } else {
                                tmpLine += UtilityMethods.seperator + tmpValue.toString();
                            }
                        }
                        tmpWriter.write(tmpLine);
                        tmpWriter.newLine();
                    }
                } else {
                    tmpLoop++;
                }
            }
            tmpWriter.close();
        } catch (Exception ex) {
            Dialog tmpDialog = new Dialog("Error", "Not able to save desciptors", ex.toString(), Alert.AlertType.ERROR);
            tmpDialog.showAndWait();
//            Logger.getLogger(UtilityMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    public static void writeDescriptorValues(File aFile, Data aData, String aType, String anExtension) {
        Path tmpPath = Paths.get(aFile.getPath());
        try {
            BufferedWriter tmpWriter;
            String tmpLine = "";
            if (anExtension.equals(Globals.ascii)) {
                tmpWriter = Files.newBufferedWriter(tmpPath, UtilityMethods.ascii);
                UtilityMethods.seperator = "\t";
            } else {
                tmpWriter = Files.newBufferedWriter(tmpPath, UtilityMethods.encoding);
                UtilityMethods.seperator = ";";            
                tmpLine = "sep=" + UtilityMethods.seperator;
                tmpWriter.write(tmpLine);
                tmpWriter.newLine();
            }
            ConcurrentHashMap<IDescriptor, IDescriptorResult> tmpMap = null;
            int tmpLoop = aData.getMoleculeCount();
            for (int i = 1; i <= tmpLoop; i++) {
                if (aData.hasKey(i)) {
                    //first line contains calculated descriptor titles
                    tmpMap = aData.getMolecule(i).getResultsMap();
                    tmpLine = "Molecule";
                    for (Object tmpKey : tmpMap.keySet()) {
                        String tmpDescriptorName = UtilityMethods.getDescriptorName((IDescriptor) tmpKey);
                        tmpLine += UtilityMethods.seperator + tmpDescriptorName ;
                        System.out.println(tmpDescriptorName);
                        if(tmpDescriptorName.equals("Weight")){
                            tmpLine += "[g/mol]";
                        }
                        else if (tmpDescriptorName.equals("VABC")) {
                            tmpLine += "[A]";
                        }
                    }
                    tmpWriter.write(tmpLine);
                    tmpWriter.newLine();
                    break;
                }
            }
            tmpLoop = aData.getMoleculeCount();
            for (int i = 1; i <= tmpLoop; i++) {
                if (aData.hasKey(i)) {
                    if (aType.equals(Globals.all)) {
                        //next line contains molecule name and descriptor values
                        tmpLine = aData.getMolecule(i).getName();
                        tmpMap = aData.getMolecule(i).getResultsMap();
                        for (Object tmpValue : tmpMap.values()) {
                            if (tmpValue == null) {
                                tmpLine += UtilityMethods.seperator + "NaN";
                            } else {
                                tmpLine += UtilityMethods.seperator + tmpValue.toString();
                            }
                        }
                    } else {
                        if (aData.getMolecule(i).isChecked()) {
                            //next line contains molecule name and descriptor values
                            tmpLine = aData.getMolecule(i).getName();
                            tmpMap = aData.getMolecule(i).getResultsMap();
                            for (Object tmpValue : tmpMap.values()) {
                                if (tmpValue == null) {
                                    tmpLine += UtilityMethods.seperator + "NaN";
                                } else {
                                    tmpLine += UtilityMethods.seperator + tmpValue.toString();
                                }
                            }
                        }
                    }
                }
                tmpWriter.write(tmpLine);
                tmpWriter.newLine();
            }
            tmpWriter.close();
        } catch (Exception ex) {
            Dialog tmpDialog = new Dialog("Error", "Not able to save desciptors", ex.toString(), Alert.AlertType.ERROR);
            tmpDialog.showAndWait();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="cutOfStringValue">
    /**
     * Cuts the given String when it contains a dot
     *
     * @param aValue String
     * @return String
     */
    public static String cutOfStringValue(String aValue) {
        String tmpValue = aValue;
        if (aValue.contains(".")) {
            int tmpPosition = aValue.indexOf(".") + 3;
            tmpValue = aValue.substring(0, tmpPosition);
        }
        return tmpValue;
    }
    //</editor-fold>

}
