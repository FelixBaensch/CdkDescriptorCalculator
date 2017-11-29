package util;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
//</editor-fold>

/**
 * Dialog class extends Alert class 
 * 
 * @author Felix Bänsch, 12.05.2016
 */
public class Dialog extends Alert{
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new dialog (alert);
     * when AlerType is CONFIRMATION the cancel button is default
     * @param aTitle String
     * @param aHeader String
     * @param aContent  String
     * @param anAlertType AlertType
     */
    public Dialog(String aTitle, String aHeader, String aContent, AlertType anAlertType) {
        super(anAlertType);
        this.setTitle(aTitle);
        this.setHeaderText(aHeader);
        this.setContentText(aContent);
        
        if(anAlertType == AlertType.CONFIRMATION){
            this.getButtonTypes().clear();
            this.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            Button tmpOkButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
            tmpOkButton.setDefaultButton(false);
            Button tmpCancelButton = (Button) this.getDialogPane().lookupButton(ButtonType.CANCEL);
            tmpCancelButton.setDefaultButton(true); 
            Optional<ButtonType> tmpResult = this.showAndWait();
        }
    }
    //</editor-fold>
}
