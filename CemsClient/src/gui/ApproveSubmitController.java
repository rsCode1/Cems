package gui;

import java.io.IOException;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Request;
import logic.StudentInTest;
import logic.Test;
import logic.Users;

public class ApproveSubmitController {
	private Test test;
	private StudentInTest studentInTest;
	private ChatClient client;
	private Users student;
	private InExamController inExamController; 
	private StudentManualTestController studentManualTest; 
	private int DigOrMan;
	public int getDigOrMan() {
		return DigOrMan;
	}
	public void setDigOrMan(int digOrMan) {
		DigOrMan = digOrMan;
	}

	public void setStudentAndClient(Users Student,ChatClient client,StudentInTest studentInTest,InExamController inExamController,Test test) {
    	this.student=Student;
    	this.client=client;
    	this.studentInTest=studentInTest;
    	this.inExamController=inExamController;
		this.test=test;
    	}
	public void setStudentAndClient2(Users Student,ChatClient client,StudentManualTestController Controller) {
    	this.student=Student;
    	this.client=client;
    	this.studentManualTest=Controller;
    	}
	 @FXML
	    private Button clsBtn;

	@FXML
    private Label lbl;

	@FXML
    private Text mesTxt;
    @FXML
    private Button noBtn;

    @FXML
    private Button yesBtn;

    @FXML
    void noBtnclicked(ActionEvent event) {
    	 Stage currentStage = (Stage) noBtn.getScene().getWindow();
         currentStage.close();
    }
    @FXML
    void clsBtnClicked(ActionEvent event) {
    	Stage currentStage = (Stage) clsBtn.getScene().getWindow();
        currentStage.close();

    }

    @FXML
    void yesBtnClicked(ActionEvent event) {
    	if(DigOrMan==0) {
    	try {
		int score=0;
		for(int i=0; i<test.getQuesSize();i++){
            if(test.getqLst().get(i).getcAns() == studentInTest.getAnswer(i))
			score+=test.getqLst().get(i).getScore();	
			}
		client.sendToServer(new Request("SubmitExam", studentInTest));
		System.out.println("Submitted");
		//Stage currentStage = (Stage) noBtn.getScene().getWindow();
        //currentStage.close();
		yesBtn.setDisable(true);
		noBtn.setDisable(true);
		lbl.setText("The Test is Submitted Succesfully!");
		mesTxt.setText("Good Luck!");
        inExamController.stopTimer();
        inExamController.CloseWindow();
		
		}

	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    	}
    	
    	else {
    		//send new request to server "Submit Manual exam"
    		yesBtn.setDisable(true);
    		noBtn.setDisable(true);
    		lbl.setText("The Test is Submitted Succesfully!");
    		mesTxt.setText("Good Luck!");
    		studentManualTest.stopTimer();
    		studentManualTest.CloseWindow();
    	}

    }
    public ApproveSubmitController getController() {
		return this;
	}
    

}
