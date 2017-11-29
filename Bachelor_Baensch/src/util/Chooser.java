package util;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//</editor-fold>

/**
 * Class for file dialog to choose SDFile or MolFileDirectory
 *
 * @author Felix Bänsch, 28.04.2016
 */
public class Chooser {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private String filePath;                //String for the filepath of the sourcefile
    private final String xmlFilePath;       //String for the filepath which is stored as XMLFile
    private final File xmlFile;             //File instance for the XMLFile
    private File xmlValueFile;              //File instance for the value of the XMLFile
    private Boolean isFilePathXml;          //Boolean if the xmlFilePAth exists
    private File sdfile;                    //File instance for the SDFile      
    private File molDirectoryFile;          //File instance for the MolFiel directory
    private File smileFile;                 //File instance for the SMILES file
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor reads the XMLFile path from the properties
     */
    public Chooser() {
        this.xmlFilePath = PropertiesLoader.getInstance().getValue("filePath");
        this.xmlFile = new File(System.getProperty("user.dir") + "/" + this.xmlFilePath);
        this.xmlValueFile = null;
        this.isFilePathXml = false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="chooseSdfile">
    /**
     * Filechooser for SDFiles, requires a stage and returns a file
     *
     * @param aStage Stage
     * @return sdfile File
     */
    public File chooseSdfile(Stage aStage) {
        try {
            FileChooser tmpChooser = new FileChooser();
            tmpChooser.setTitle("Open SDFile");
            tmpChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SDFiles", "*.sdf"));
            if (this.checkXmlFile()) {
                tmpChooser.setInitialDirectory(this.xmlValueFile);
            } else {
                tmpChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            }
            this.sdfile = tmpChooser.showOpenDialog(aStage);
            this.filePath = this.sdfile.getParent();
            if (!this.isFilePathXml || !this.xmlValueFile.getPath().equals(this.filePath)) {
                System.out.println("Path: " + this.filePath);
                this.serializeDirectoryPath();
            }
            return this.sdfile;
        } catch (NullPointerException ex) {
            throw new RuntimeException(ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="chooseMolFile">
    /**
     * Directorychooser for Molfile directorie, requiers a stage and returns a
     * file
     *
     * @param aStage Stage
     * @return molDirectoryFile
     */
    public File chooseMolFile(Stage aStage) {
        try {
            DirectoryChooser tmpChooser = new DirectoryChooser();
            tmpChooser.setTitle("Open Molfile directory");
            if (this.checkXmlFile()) {
                tmpChooser.setInitialDirectory(this.xmlValueFile);
            } else {
                tmpChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            }
            this.molDirectoryFile = tmpChooser.showDialog(aStage);
            this.filePath = this.molDirectoryFile.getParent();
            if (!this.isFilePathXml || !this.xmlValueFile.getPath().equals(this.filePath)) {
                System.out.println("Path: " + this.filePath);
                this.serializeDirectoryPath();
            }
            return this.molDirectoryFile;
        } catch (NullPointerException ex) {
            throw new RuntimeException("Chooser canceled");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="chooseSmileFile">
    /**
     * Filechooser for a Smile TextFile, requiers a stage and returns a file
     *
     * @param aStage Stage
     * @return smileFile File
     */
    public File chooseSmileFile(Stage aStage) {
        try {
            FileChooser tmpChooser = new FileChooser();
            tmpChooser.setTitle("Open SMILES Textfile");
            tmpChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "SMILES Textfile", "*.txt"));
            if (this.checkXmlFile()) {
                tmpChooser.setInitialDirectory(this.xmlValueFile);
            } else {
                tmpChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            }
            this.smileFile = tmpChooser.showOpenDialog(aStage);
            this.filePath = this.smileFile.getParent();
            if (!this.isFilePathXml || !this.xmlValueFile.getPath().equals(this.filePath)) {
                System.out.println("Path: " + this.filePath);
                this.serializeDirectoryPath();
            }
            return this.smileFile;
        } catch (NullPointerException ex) {
            throw new RuntimeException(ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="saveFile">
    /**
     * FileChooser for saving the molecule selection
     *
     * @param aStage Stage
     * @param anExtension String
     * @return File
     */
    public File saveFile(Stage aStage, String anExtension) {
        FileChooser tmpChooser = new FileChooser();
        tmpChooser.setTitle("Save");
        if (this.checkXmlFile()) {
            tmpChooser.setInitialDirectory(this.xmlValueFile);
        } else {
            tmpChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        }
        String tmpDescription = "";                                  
        if (anExtension.equals("sdf")) {
            tmpDescription = PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.sdf.toString());
        } else if (anExtension.equals("txt")) {
            tmpDescription = PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.txt.toString());
        } else if (anExtension.equals("csv")) {
            tmpDescription = PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.csv.toString());
        } else if (anExtension.equals("ascii")){
            tmpDescription = PropertiesLoader.getInstance().getValue(PropertiesEnum.ControllerEnum.ascii.toString());
            anExtension = Globals.saveTxt;
        }
        tmpChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(tmpDescription, "*." + anExtension));
        String tmpPath = tmpChooser.showSaveDialog(aStage).getPath();
        if (!tmpPath.endsWith("." + anExtension)) {
            tmpPath += "." + anExtension;
        }
        File tmpFile = new File(tmpPath);
        this.filePath = tmpFile.getParent();
        if (!this.isFilePathXml || !this.xmlValueFile.getPath().equals(this.filePath)) {
            System.out.println("Path: " + this.filePath);
            this.serializeDirectoryPath();
        }
        return tmpFile;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="chooseCsvFile">
    /**
     * FileChooser for a CSV File
     * @param aStage Stage
     * @return File
     */
    public File chooseCsvFile(Stage aStage) {
        try {
            FileChooser tmpChooser = new FileChooser();
            tmpChooser.setTitle("Open CSV File");
            tmpChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "CSV File", "*.csv"));
            if (this.checkXmlFile()) {
                tmpChooser.setInitialDirectory(this.xmlValueFile);
            } else {
                tmpChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            }
            return tmpChooser.showOpenDialog(aStage);
        } catch (NullPointerException ex){
            throw new RuntimeException(ex);
        }
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">
    //<editor-fold defaultstate="collapsed" desc="serializeDirectoryPath">
    /**
     * Serializes the current directory path
     */
    private void serializeDirectoryPath() {
        try {
            FileOutputStream tmpOutputStream = new FileOutputStream(System.getProperty("user.dir")
                    + "/" + this.xmlFilePath);
            try (XMLEncoder tmpEncoder = new XMLEncoder(tmpOutputStream)) {
                tmpEncoder.writeObject(this.filePath);
                tmpEncoder.flush();
                tmpEncoder.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Not able to serialize directory path");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deserializeDirectoryPath">
    /**
     * Deserializes the XML file with the directory path
     *
     * @return file
     */
    private File deserializeDirectoryPath() {
        File tmpPath = new File(System.getProperty("user.dir")
                + "/" + this.xmlFilePath);
        File tmpXmlPath = null;
        try {
            FileInputStream tmpInputStream = new FileInputStream(tmpPath);
            try (XMLDecoder tmpDecoder = new XMLDecoder(tmpInputStream)) {
                tmpXmlPath = new File(tmpDecoder.readObject().toString());
            } catch (NullPointerException ex) {
                tmpXmlPath = new File(System.getProperty("user.dir"));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("No path file found");
            tmpXmlPath = new File(System.getProperty("user.dir"));
        }
        return tmpXmlPath;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkXmlFile">
    /**
     * Checks if the XMLFile exists, reads it out and if the value of the
     * XMLFile exists and it is an directory it returns true
     *
     * @return Boolean
     */
    private Boolean checkXmlFile() {
        if (this.xmlFile.exists() && !this.xmlFile.isDirectory()) {
            this.xmlValueFile = this.deserializeDirectoryPath();
            if (this.xmlValueFile.exists() && this.xmlValueFile.isDirectory()) {
                this.isFilePathXml = true;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public properties">
    //<editor-fold defaultstate="collapsed" desc="getMolFileDirectoryName">
    /**
     * Gets the name of the mol file directory
     *
     * @return molFileDirectoryName String
     */
    public String getMolFileDirectoryName() {
        return this.molDirectoryFile.getName();
    }
    //</editor-fold>
    //</editor-fold>
}
