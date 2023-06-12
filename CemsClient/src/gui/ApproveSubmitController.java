package gui;

import java.io.IOException;
//testing push xzzz 
import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Request;
import logic.StudentInTest;
import logic.Users;

public class ApproveSubmitController {
	private StudentInTest studentInTest;
	private ChatClient client;
	private Users student;
	private InExamController inExamController;

	public void setStudentAndClient(Users Student,ChatClient client,StudentInTest studentInTest,InExamController inExamController) {
    	this.student=Student;
    	this.client=client;
    	this.studentInTest=studentInTest;
    	this.inExamController=inExamController;
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
    //testing
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
    	try {
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
        System.out.println("arrived");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    }
    public ApproveSubmitController getController() {
		return this;
	}
    

}
