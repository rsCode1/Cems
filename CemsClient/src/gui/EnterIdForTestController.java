package gui;

import java.io.IOException;



import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Question;
import logic.Request;
import logic.Test;
import logic.TestApplyInfo;
import logic.TestCode;
import logic.Users;

public class EnterIdForTestController {
	private ChatClient client;
	private Users student;
	private Test test;
	public void setStudentAndClient(Users Student,ChatClient client) {
    	this.student=Student;
    	this.client=client;
    }
	public void setTest(Test test){
		this.test=test;
		

	}
    @FXML
    private Text errTxt;

    @FXML
    private TextField idField;

    @FXML
    private TextArea lecnotesArea;

    @FXML
    private Button starttestBtn;

    @FXML
    private Label testLbl;
    @FXML
    void StartTestClicked(ActionEvent event) {
    	String id = idField.getText();
    	if(CheckApplyingInfo(id)) {
    		int studentId = Integer.parseInt(id);
    		if (test.getStudentsIdForTest().contains(studentId)) {
    			errTxt.setText("Correct Id");
    			ShowStudentTestScreen(test);
    			
    		}
    		
    	}

    }
    public boolean CheckApplyingInfo(String code ){
    	boolean ret=false;
    	errTxt.setText(" ");
    	if(code.isEmpty()) {
    		errTxt.setText("Please Enter id!");
    		ret=false;
    	}
    	else if ( !code.matches("[0-9]+")) {
    		errTxt.setText("Please Enter Numbers only for id.");  
	    	ret=false;
	    }
    	else if(code.length() != 9) {
    		errTxt.setText("Please Enter 9 Numbers for id.");  
    		ret=false;
    	}
    	
    	else {
    		ret = true;
        }
        return ret;
    }
    public void ShowStudentTestScreen(Test test) {
			if (test == null) {
				errTxt.setText("Id or Code is wrong!, please try again!"); } 
			
			else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InExam.fxml"));
			errTxt.setText("");
			Parent root;
			try {
			
				root = loader.load();
				Stage window = (Stage) starttestBtn.getScene().getWindow();
				InExamController inExamController=loader.getController();
				inExamController.setTest(test);
				inExamController.setStudentAndClient(student ,client);
				inExamController.setStudentInTest();
				inExamController.setFirstPage();
				window.setScene(new Scene(root));
				window.show();
			    }
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			
    	    }
		
    }
    public void SetLectureNotes() {
    	lecnotesArea.setText(test.getStudentNotes());
    	
    }
    
    
    
    
    
    
    
    
    
    public EnterIdForTestController getController() {
		return this;
	}
    

}
