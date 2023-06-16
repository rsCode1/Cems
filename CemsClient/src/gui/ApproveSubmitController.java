package gui;

import java.io.IOException;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.MyFile;
import logic.Request;
import logic.StudentInTest;
import logic.Test;
import logic.Users;

public class ApproveSubmitController {
	private MyFile answersFile;
	private Test test;
	private StudentInTest studentInTest;
	private ChatClient client;
	private Users student;
	private InExamController inExamController; 
	private StudentManualTestController studentManualTest; 
	private int DigOrMan;
	public void setAnswersFile(MyFile answersFile) {
		this.answersFile = answersFile;
	}
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
		studentInTest.setScore(score);
		client.sendToServer(new Request("SubmitExam", studentInTest));
		System.out.println("Submitted");
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
			try {
				client.sendToServer(new Request("SubmitManualExamExam", answersFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	public void forceSubmit(){
		yesBtn.setDisable(true);
        noBtn.setDisable(true);
		lbl.setText("Time out!");
		mesTxt.setText("your answers are submitted automatically, Good Luck!");
		if(DigOrMan==0) {
			try {
				client.sendToServer(new Request("SubmitExam", studentInTest));
				inExamController.CloseWindow();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			try {
				client.sendToServer(new Request("SubmitManualExamExam", answersFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	public void examIsLocked(){
		yesBtn.setDisable(true);
        noBtn.setDisable(true);
        lbl.setText("Sorry Exam is locked by luctuer!");
        mesTxt.setText("Your answers are submitted automatically, Good Luck!");
		if(DigOrMan==0) {
			try {
				client.sendToServer(new Request("SubmitExam", studentInTest));
				inExamController.CloseWindow();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			/*try {
				client.sendToServer(new Request("SubmitExamManualExam", FileUploadInfo));
				studentManualTest.CloseWindow();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		}
	}
    

}
