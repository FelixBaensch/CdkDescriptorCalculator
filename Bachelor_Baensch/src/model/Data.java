package model;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import util.Globals;
import util.UtilityMethods;
//</editor-fold>

/**
 * Data class, administres the molecules
 *
 * @author Felix Bänsch, 17.05.2016
 */
public class Data {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private int moleculeCount;                                  //int for the number of imported molecules
    private File sourceFile;                                    //File which contains the molecule data
    private File[] molFileArray;                                //FileArray for the MolFiles, is generated out of the List
    private List<File> molFileList;                             //List for the MolFiles
    private List<String> smilesList;                            //List for the SMILES
    private String[] smilesArray;                               //StringArray for the SMILES, is generated out of the list
    private List<String> smilesNamesList;                       //List for the SMILES names    
    private String[] smilesNamesArray;                          //StringArray for the SMILES names, is generated out of the list
    private IteratingSDFReader sdfReader;                       //IteratingSDFReader for reading the SDFiles
    private MDLV2000Reader mol2000Reader;                       //MDLV2000Reader for reading the MolFiles
    private SmilesParser smilesParser;                          //SmilesParser for reading the SMILES
    private ConcurrentHashMap<Integer, Molecule> moleculesMap;  //ConcurrentHashMap for the molecules and the complying index; threadsafe
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new Data instance from the SDFile oder MolFile
     *
     * @param aFile File
     */
    public Data(File aFile) {
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aFile == null) {
            throw new IllegalArgumentException("aFile must not be null");
        }
        //</editor-fold>
        this.sourceFile = aFile;
        if (Globals.isMolFile) {
            this.molFileList = this.listFileDir(this.sourceFile, "mol");
        }
        if (Globals.isSmileFile) {
            this.createSmilesList(this.sourceFile);
        }
        this.countFile();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="createMoleculeArray">
    /**
     * Creates a new MoleculeArray
     */
    public void createMoleculeHashMap() {
        this.moleculesMap = new ConcurrentHashMap<>();
        //SDFile
        if (Globals.isSdfile) {
            try {
                this.sdfReader = new IteratingSDFReader(new FileInputStream(this.sourceFile),
                        DefaultChemObjectBuilder.getInstance());
                while (this.sdfReader.hasNext()) {
                    IAtomContainer tmpContainer = this.addImplicitHydrogens(this.sdfReader.next());
                    Identifier tmpIdentifier = IdentifierFactory.createIdentifier();
                    Molecule tmpMolecule = new Molecule(tmpIdentifier, tmpContainer);
                    int tmpKey = Integer.parseInt(tmpIdentifier.toString("Counter"));
                    this.moleculesMap.put(tmpKey, tmpMolecule);
                }
                System.out.println(this.moleculesMap.size());
            } catch (FileNotFoundException | NumberFormatException ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    this.sdfReader.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } //MolFile
        else if (Globals.isMolFile) {
            try {
                this.molFileArray = this.molFileList.toArray(new File[this.molFileList.size()]);
                for (int i = 0; i < this.moleculeCount; i++) {
                    this.mol2000Reader = new MDLV2000Reader(new FileInputStream(this.molFileArray[i]),
                            IChemObjectReader.Mode.RELAXED);
                    IAtomContainer tmpContainer = this.mol2000Reader.read(new AtomContainer());
                    tmpContainer = this.addImplicitHydrogens(tmpContainer);
                    tmpContainer.setProperty("NAME", this.molFileArray[i].getName().split("\\.")[0]);
                    Identifier tmpIdentifier = IdentifierFactory.createIdentifier();
                    Molecule tmpMolecule = new Molecule(tmpIdentifier, tmpContainer);
                    int tmpKey = Integer.parseInt(tmpIdentifier.toString("Counter"));
                    this.moleculesMap.put(tmpKey, tmpMolecule);
                }
            } catch (FileNotFoundException | CDKException ex) {
                throw new RuntimeException(ex);
            }
        } //SmileFile
        else if (Globals.isSmileFile) {
            try {
                this.smilesArray = this.smilesList.toArray(new String[this.smilesList.size()]);
                this.smilesNamesArray = this.smilesNamesList.toArray(new String[this.smilesNamesList.size()]);
                IChemObjectBuilder tmpBuilder = SilentChemObjectBuilder.getInstance();
                this.smilesParser = new SmilesParser(tmpBuilder);
                for (int i = 0; i < this.moleculeCount; i++) {
                    IAtomContainer tmpContainer = this.addImplicitHydrogens(this.smilesParser.parseSmiles(this.smilesArray[i]));
                    tmpContainer.setProperty("NAME", this.smilesNamesArray[i]);
                    Identifier tmpIdentifier = IdentifierFactory.createIdentifier();
                    Molecule tmpMolecule = new Molecule(tmpIdentifier, tmpContainer);
                    int tmpKey = Integer.parseInt(tmpIdentifier.toString("Counter"));
                    this.moleculesMap.put(tmpKey, tmpMolecule);
                }
            } catch (InvalidSmilesException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="clearAll">
    /**
     * Sets all variables to null
     */
    public void clearAll() {
        this.moleculeCount = 0;
        this.sourceFile = null;
        this.molFileArray = null;
        this.molFileList = null;
        this.moleculesMap = null;
        IdentifierFactory.clearCounter();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deleteOne">
    /**
     * Deletes the molecule with the given Key (int)
     *
     * @param aKey int
     */
    public void deleteOne(int aKey) {
        if (this.moleculesMap.containsKey(aKey)) {
            this.moleculesMap.remove(aKey);
            this.moleculeCount--;
            System.out.println("One Deleted");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setCheckAll">
    /**
     * Check or unchecks each molecule in the HashMap
     * @param aBoolean boolean
     */
    public void setCheckAll(boolean aBoolean) {
//        if(aBoolean){
//            for(Entry<Integer, Molecule> tmpEntry : moleculesMap.entrySet()){
//                tmpEntry.getValue().setCheck(aBoolean);
//            }
//        } else if (!aBoolean){
//            for(Entry<Integer, Molecule> tmpEntry : moleculesMap.entrySet()){
//                tmpEntry.getValue().setCheck(aBoolean);
//            }
//        }
        for (Entry<Integer, Molecule> tmpEntry : moleculesMap.entrySet()) {
            tmpEntry.getValue().setCheck(aBoolean);
        }
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">   
    //<editor-fold defaultstate="collapsed" desc="addImplicitHydrogens">
    /**
     * Adds implicit hydrogens to the given AtomContainer, if the globalvariable
     * "isAdditionImplicitHydrogensOnImport" is set on true
     *
     * @param anAtomContainer IAtomContainer
     * @return anAtomContainer IAtomContainer
     */
    private IAtomContainer addImplicitHydrogens(IAtomContainer anAtomContainer) {
        try {
            if (Globals.isAdditionImplicitHydrogensOnImport) {
                CDKAtomTypeMatcher tmpMatcher = CDKAtomTypeMatcher.getInstance(anAtomContainer.getBuilder());
                for (IAtom tmpAtom : anAtomContainer.atoms()) {
                    IAtomType tmpAtomType = tmpMatcher.findMatchingAtomType(anAtomContainer, tmpAtom);
                    AtomTypeManipulator.configure(tmpAtom, tmpAtomType);
                }
                CDKHydrogenAdder tmpAdder = CDKHydrogenAdder.getInstance(anAtomContainer.getBuilder());
                tmpAdder.addImplicitHydrogens(anAtomContainer);
            }
            return anAtomContainer;
        } catch (CDKException ex) {
            throw new RuntimeException(ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="countFile">
    /**
     * Counts the molcules in the sourceFile
     */
    private void countFile() {
        if (Globals.isSdfile) {
            try {
                this.sdfReader = new IteratingSDFReader(new FileInputStream(this.sourceFile),
                        DefaultChemObjectBuilder.getInstance());
                while (this.sdfReader.hasNext()) {
                    this.moleculeCount++;
                    this.sdfReader.next();
                }
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    this.sdfReader.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (Globals.isMolFile) {
            this.moleculeCount = this.molFileList.size();
        } else if (Globals.isSmileFile) {
            this.moleculeCount = this.smilesList.size();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="listFileDir">
    /**
     * Lists the directoryfile from the molFileChooser to an List<File>
     *
     * @param aFile directoryFile from molFileChooser
     * @return List<File>
     */
    private List<File> listFileDir(File aFile, String anExtension) {
        File[] tmpArray = aFile.listFiles();
        List<File> tmpList = new ArrayList<>();
        if (tmpArray != null) {
            for (File tmpFile : tmpArray) {
                if (this.getExtension(tmpFile.getName()).equalsIgnoreCase(anExtension)) {
                    tmpList.add(tmpFile);
                }
                if (tmpFile.isDirectory()) {
                    tmpList.addAll(this.listFileDir(tmpFile, anExtension));
                }
            }
        }
        return tmpList;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getExtension">
    /**
     * Gets the file extension of the given file name
     *
     * @param aFileName String
     * @return file extension String
     */
    private String getExtension(String aFileName) {
        if (aFileName != null) {
            if (aFileName.lastIndexOf(".") > 0) {
                return aFileName.substring(aFileName.lastIndexOf(".") + 1);
            }
        }
        return "";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createSmilesList">
    /**
     * Reads the textfile to a list
     *
     * @param aFile File
     * @return List
     */
    private void createSmilesList(File aFile) {
        this.smilesList = new ArrayList<>();
        this.smilesNamesList = new ArrayList<>();
        try {
            BufferedReader tmpReader = new BufferedReader(new FileReader(aFile));
            String tmpLine;
            while ((tmpLine = tmpReader.readLine()) != null) {
                String tmpName;
                String tmpSmile;
                if (tmpLine.contains(":")) {
                    tmpName = tmpLine.split(":")[0]; //TODO: Abfragen ob überhaupt name drin ist und verschiedene Trennzeichen
                    tmpName = UtilityMethods.getOnlyAscii(tmpName);
                    tmpSmile = tmpLine.split(":")[1];
                } else {
                    tmpSmile = tmpLine;
                    tmpName = aFile.getName();
                }
                this.smilesList.add(tmpSmile.trim());
                this.smilesNamesList.add(tmpName.trim());
                System.out.println(tmpName);
            }
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="getMoleculeCount">
    /**
     * Gets the moleculeCount
     *
     * @return int moleculeCount
     */
    public int getMoleculeCount() {
        return this.moleculeCount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMolecule">
    /**
     * Gets the molecule at given index
     *
     * @param aKey int
     * @return molecule
     */
    public Molecule getMolecule(int aKey) {
        return this.moleculesMap.get(aKey);
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="hasKey">
    /**
     * Checks if the moleculesMap contais the given key (int) and returns the
     * corresponding boolean
     *
     * @param aKey int
     * @return boolean
     */
    public boolean hasKey(int aKey) {
        return this.moleculesMap.containsKey(aKey);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="isOneChecked">
    /**
     * Checks that at least one molecule is checked and returns true or false
     *
     * @return boolean
     */
    public boolean isOneChecked() {
        int tmpLoop = this.moleculeCount;
        for (int i = 1; i <= tmpLoop; i++) {
            if (this.hasKey(i)) {
                if (this.getMolecule(i).isChecked()) {
                    return true;
                }
            } else {
                tmpLoop++;
            }
        }
        return false;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getMoleculesMap">
    /**
     * Gets the ConcurrentHashMap moleculesMap
     * @return  ConcurrentHashMap
     */
    public ConcurrentHashMap getMoleculesMap(){
        return this.moleculesMap;
    }
    //</editor-fold>
    //</editor-fold>
}
