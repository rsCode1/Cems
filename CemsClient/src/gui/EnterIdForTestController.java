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
import logic.InTestQuestion;
import logic.Request;
import logic.Test;
import logic.TestApplyInfo;
import logic.TestCode;
import logic.Users;

public class EnterIdForTestController {
	private int DigOrMan;
	
	/**
	 * return if the test is manual or digital
	 * @return
	 */
	public int getDigOrMan() {
		return DigOrMan;
	}
	/**
	 * sets the exam to be manual or digital
	 * @param digOrMan
	 */
	public void setDigOrMan(int digOrMan) {
		DigOrMan = digOrMan;
	}
	private ChatClient client;
	private Users student;
	private Test test;
	/**
	 * sets the student and client that currently using the form
	 * @param Student
	 * @param client
	 */
	public void setStudentAndClient(Users Student,ChatClient client) {
    	this.student=Student;
    	this.client=client;
    }

	/**
	 * sets the test that we are going to initialize
	 * @param test
	 */
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

	/**
	 * sends to server the ID of the student, checks if he's eligiable for the test.
	 * if yes, procceed to test form.
	 * @param event
	 */
    void StartTestClicked(ActionEvent event) {
    	String id = idField.getText();
    	if(CheckApplyingInfo(id)) {
    	try{
    		int studentId = Integer.parseInt(id);
    		TestApplyInfo testApplyInfo= new TestApplyInfo(student.getId(),test.getTestId());
    		if (test.getStudentsIdForTest().contains(studentId)) {
    			errTxt.setText("Correct Id");
    			client.sendToServer(new Request("AddStudentToInExamList", testApplyInfo));
    			ShowStudentTestScreen(test);	
    		}
			else{
				errTxt.setText("Already submitted!");
			}
    	}
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    	}
    		else {
    			errTxt.setText("Wrong Id, please try again");
    		}
    		
    		
    	

    }
	/**
	 * the method the checks if the given ID is valid and matches the id of the logged user
	 * @param code
	 * @return
	 */
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
    	else if(!code.equals(String.valueOf(student.getId()))) {
    		errTxt.setText("Wrong Id, please try again");  
    		ret=false;
    	}
    	
    	else {
    		ret = true;
        }
        return ret;
    }

	/**
	 * depends on whether the test is digital or manual, opens the Corresponding test form 
	 * to the student
	 * @param test
	 */
    public void ShowStudentTestScreen(Test test) {
			if (test == null) {
				errTxt.setText("Id or Code is wrong!, please try again!"); } 
			
			else if (DigOrMan == 0){
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InExam.fxml"));
			errTxt.setText("");
			Parent root;
			try {
			
				root = loader.load();
				Stage window = (Stage) starttestBtn.getScene().getWindow();
				InExamController inExamController=loader.getController();
				inExamController.setTest(test);
				inExamController.setStudentAndClient(student ,client,inExamController);
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
			else if (DigOrMan == 1){
			  try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Take_manual_exam.fxml"));
	            Parent root = loader.load();
	            Stage window = (Stage) starttestBtn.getScene().getWindow();
	            StudentManualTestController controller=loader.getController();
	            controller.setStudentAndClient(student, client,controller);
	            controller.setTest(test);
	            controller.setStudentInTest();
	            controller.setWelcomeLabel();
	            window.setScene(new Scene(root));
				window.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	    }
				
			}
		
    }
	/**
	 * adding the lecturer notes in the test form
	 */
    public void SetLectureNotes() {
    	lecnotesArea.setText(test.getStudentNotes());
    	testLbl.setText("Welcome to " + test.getCourseName() + " Test");
    }
    
    
    
    
    
    
    
    
    /**
	 * returns the controller used in that form
	 * @return
	 */
    public EnterIdForTestController getController() {
		return this;
	}
    

}
