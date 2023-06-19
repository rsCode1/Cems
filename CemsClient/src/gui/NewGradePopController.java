package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
/**
 * this is popout widow it will show to the student the new grade recieved and course name
 */
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
