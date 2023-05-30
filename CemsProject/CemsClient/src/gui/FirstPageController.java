package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class FirstPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane createQuestionForm;

    @FXML
    void initialize() {
        assert createQuestionForm != null : "fx:id=\"createQuestionForm\" was not injected: check your FXML file 'FirstPage.fxml'.";

    }
}
