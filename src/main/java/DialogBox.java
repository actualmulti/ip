import java.io.IOException;
import java.util.Collections;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);

        applyCircularClip();
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    public static DialogBox getJakeDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    // ChatGPT-5 was consulted to come up with such a feature
    private void applyCircularClip() {
        // Use the fit sizes you set in FXML (99x99) rather than actual image width (safer if image is large)
        double diameter = Math.min(displayPicture.getFitWidth(), displayPicture.getFitHeight());
        if (diameter <= 0) {
            // Defer until layout pass if sizes not resolved yet
            displayPicture.layoutBoundsProperty().addListener(new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends javafx.geometry.Bounds> obs,
                                    javafx.geometry.Bounds oldB, javafx.geometry.Bounds newB) {
                    displayPicture.layoutBoundsProperty().removeListener(this);
                    applyCircularClip(); // retry
                }
            });
            return;
        }
        double radius = diameter / 2.0;
        Circle clip = new Circle(radius, radius, radius);
        displayPicture.setClip(clip);

        displayPicture.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.25)));
    }
}
