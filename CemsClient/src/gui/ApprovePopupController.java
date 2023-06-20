package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Popup;

/**
 * this class is responsble for controlling the approve popout screen ' which will loads,
 * when the head department clicks on approve button for time requests.
 */
public class ApprovePopupController implements Initializable {
    @FXML
    private ImageView approveImageView;;
    @FXML
    private Button exitbtn;
    @FXML
    private Label lbltst;

    private Popup popup;
    private String str;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       lbltst.setText(str);
    }
    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    public void setStr(String str) {
    	this.str=str;

    }

  

    @FXML
    public void exit(ActionEvent event) {
        popup.hide();

    }

  
}
