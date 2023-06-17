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
import logic.UploadFile;
import logic.Users;

public class ApproveSubmitController {
	private UploadFile answersFile;
	private Test test;
	private StudentInTest studentInTest;
	private ChatClient client;
	private Users student;
	private InExamController inExamController; 
	private StudentManualTestController studentManualTest; 
	private int DigOrMan;

	/**
	 * sets the file that was uploaded
	 */
	public void setAnswersFile(UploadFile answersFile) {
		this.answersFile = answersFile;
	}
	/**
	 * returns if the test is digital or manual
	 */
	public int getDigOrMan() {
		return DigOrMan;
	}
	/**
	 * sets the test property to digital or manual
	 */
	public void setDigOrMan(int digOrMan) {
		DigOrMan = digOrMan;
	}

/**
 * sets which user is logged,what client,the test that is being proccessed, and the controller we"re using
 */
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
	/**
	 * cancels the submission
	 */
    void noBtnclicked(ActionEvent event) {
    	 Stage currentStage = (Stage) noBtn.getScene().getWindow();
         currentStage.close();
    }
    @FXML
	/**
	 * closes the submission windows
	 */
    void clsBtnClicked(ActionEvent event) {
    	Stage currentStage = (Stage) clsBtn.getScene().getWindow();
        currentStage.close();

    }

    @FXML
	/**
	 *approves the exam, sending to sever all the answers and closing
	 the exam
	 */
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
				client.sendToServer(new Request("SubmitManualExam", answersFile));
				studentManualTest.CloseWindow();
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
/**
 * returns the controller used for this form
 */
    public ApproveSubmitController getController() {
		return this;
	}

	/**
	 * submits the exam when the timer runs up or the lecturer closes  the exam.
	 */
	public void forceSubmit(){
		yesBtn.setDisable(true);
        noBtn.setDisable(true);
		lbl.setText("Time out!");
		
		if(DigOrMan==0) {
			try {
				mesTxt.setText("your answers are submitted automatically, Good Luck!");
				client.sendToServer(new Request("SubmitExam", studentInTest));
				inExamController.CloseWindow();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			try {
				client.openConnection();
    			if (client.isConnected()) {
				mesTxt.setText("Good luck next time!");
				client.sendToServer(new Request("SubmitManualExam", answersFile));
				studentManualTest.CloseWindow();}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	/**
	 * disables the option to submit the exam after it was locked
	 */

	 
	public void examIsLocked(){
		yesBtn.setDisable(true);
        noBtn.setDisable(true);
        lbl.setText("Sorry Exam is locked by luctuer!");
        
		if(DigOrMan==0) {
			try {
				mesTxt.setText("Your answers are submitted automatically, Good Luck!");
				forceSubmit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			try {
				mesTxt.setText("Good luck next time!");
				forceSubmit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
    

}
