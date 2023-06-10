package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.naming.spi.InitialContextFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RejectPopUpController implements Initializable {
    @FXML
    private ImageView rejectImageView;;
    @FXML
    private Button exitbtn;
    @FXML
    private Label lbltst;
    private Popup popup;
    private String str;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setPopup(Popup popup) {
    	
        this.popup = popup;
    }

    @FXML
    public void exit(ActionEvent event) {
        popup.hide();

    }
}
