package main;

import controller.MainPaneController;
import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;
import view.MainPane;

/**
 * Main Class
 *
 * @author Felix Bänsch, 12.05.2016
 */
public class Main extends Application {

    @Override
    public void start(Stage aPrimaryStage) {
        try {
            Locale tmpLocale = Locale.US;
            Locale.setDefault(tmpLocale);
            System.out.println(Locale.getDefault().toString());
            MainPane tmpFrame = new MainPane();
            MainPaneController tmpController = new MainPaneController(tmpFrame, aPrimaryStage);
        } catch (Exception ex){
            System.err.println(ex);
        }
    }

    /**
     * @param anArgs the command line arguments
     */
    public static void main(String[] anArgs) {
        launch(anArgs);
    }
}
