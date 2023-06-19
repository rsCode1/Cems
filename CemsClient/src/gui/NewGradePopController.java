package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class NewGradePopController {

    @FXML
    private Text gradeTxt;
    
    public void setMes(String string) {
 
    	gradeTxt.setText(string);
    

    }

    public NewGradePopController getController() {
		return this;
	}
}
