import jake.Jake;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Jake jake;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/finnthehuman.jpg"));
    private Image jakeImage = new Image(this.getClass().getResourceAsStream("/images/jakethedog.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Jake instance */
    public void setJake(Jake j) {
        jake = j;
    }

    /**
     * Creates two dialog boxes, one echoing user input and other containing Jake's reply
     * Clears user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = jake.getResponse(input);

        if (response.startsWith("BYE_COMMAND:")) {
            String actualResponse = response.substring("BYE_COMMAND:".length());

            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getJakeDialog(actualResponse, jakeImage)
            );
            userInput.clear();

            userInput.setDisable(true);
            sendButton.setDisable(true);

            // Close application after 3 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        } else {
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getJakeDialog(response, jakeImage)
            );
            userInput.clear();
        }
    }
}
