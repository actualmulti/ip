import java.io.IOException;

import jake.Jake;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Jake using FXML.
 */
public class Main extends Application {

    private Jake jake = new Jake("./data/jake.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setJake(jake); // inject the instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
