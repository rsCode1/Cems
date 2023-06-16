package gui;

import java.io.IOException;

import javafx.scene.control.TextArea;

import client.ChatClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.LogInInfo;
import logic.InTestQuestion;
import logic.Request;
import logic.StudentInTest;
import logic.Test;
import logic.TestApplyInfo;
import logic.TestCode;
import logic.Users;

public class TakeExamController {
	private int DigOrMan;
	public int getDigOrMan() {
		return DigOrMan;
	}

	public void setDigOrMan(int digOrMan) {
		DigOrMan = digOrMan;
	}

	private ChatClient client;
	private Users student;
	private TestCode testCode;
	private Test test;
	private InTestQuestion[] qLst;
	public void setStudentAndClient(Users Student,ChatClient client,TakeExamController controller) {
    	this.student=Student;
    	this.client=client;
    	this.client.setTakeExamController(controller);
    }
	@FXML
    private Text lbl;
	 
	 @FXML
	    private Text errMes1;

	    @FXML
	    private Text errMes2;

	    @FXML
	    private Text errMes3;
    @FXML
    private CheckBox ConfirmCheckB;

    @FXML
    private Button backBtn;

    @FXML
    private TextField codeTxt;

  

    @FXML
    private Button startBtn;
    
    private boolean checked=false;
    
    @FXML
    void ConfirmCheckBChecked(ActionEvent event) {
    if(!checked)
    checked=true;
    else checked=false;
    }

    @FXML
    void backBtnClicked(ActionEvent event){
    	  Stage currentStage = (Stage) backBtn.getScene().getWindow();
          currentStage.close();
    }

    @FXML
    void startBtnClicked(ActionEvent event) {
    	String code = codeTxt.getText() ;
    	if ( CheckApplyingInfo(code)) {
    		TestCode testCode = new TestCode(Integer.valueOf(code),DigOrMan);
    		if(DigOrMan == 0) {
    		try {
    			client.openConnection();
    			if (client.isConnected()) {
    				client.sendToServer(new Request("GetExam", testCode));
    				errMes1.setText("Code is wrong!, please try again!");
    			}
    			else {
    				System.out.println("Not connected to server.");
    			}
    		}
    		 catch (IOException e) {
    			e.printStackTrace();
    		 }
    		}
    		else {
    			try {
        			client.openConnection();
        			if (client.isConnected()) {
        				client.sendToServer(new Request("GetExam", testCode));
        				errMes1.setText("Code is wrong!, please try again!");
        			} else {
        				System.out.println("Not connected to server.");
        			}
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
    			
    		}
    		}
    		/*FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InExam.fxml")); // specify the path to the new fxml file
    		Parent root;
			try {
				root = loader.load();
				Stage window = (Stage) getstartBtn().getScene().getWindow();
			    InExamController controller = loader.getController();
			    //controller.setClient(this.client,controller);
			    // Get the Stage information
			    controller.setTest();
			    controller.setStudentAndClient(student, client);
			    controller.setStudentInTest();
			    controller.setFirstPage();
			    window.setScene(new Scene(root));
				window.show();
			}
			catch (Exception e) {
				e.printStackTrace();
			}*/
    	}
    	
    	
    	
    		

    
    public void setLabel(String str) {
    	lbl.setText(str);
    	
    }
    
    public void ShowStudentEnterIdScreen(Test test) throws IOException {
    	//System.out.println("I arruved at show student test");
    	Platform.runLater(() -> {
			if (test == null) {
				// show error text
				errMes1.setText("Code is wrong!, please try again!"); } 
			
			else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/EnterIdForTest.fxml")); // specify the path to the new fxml file
	    		Parent root;
				try {
					root = loader.load();
					Stage window = (Stage) getstartBtn().getScene().getWindow();
				    EnterIdForTestController controller = loader.getController();
				   // controller.setClient(this.client,controller);
				    // Get the Stage information
				    controller.setTest(test);
				    controller.setStudentAndClient(student, client);
				    controller.setDigOrMan(DigOrMan);
				   // controller.setStudentInTest();
				    //controller.setFirstPage();
				    controller.SetLectureNotes();
				    window.setScene(new Scene(root));
					window.show();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			
    	    }
		});
    }
    
    //Checks if id and code not empty and contains number only , 
    //checks also if CheckBox is checked
    //returns True if yes
    public boolean CheckApplyingInfo(String code ){
    	boolean ret=false;
    	String mes1 = "Please Click on Check Box!";
    	String mes3 = "Please Enter Code!";
    	errMes1.setText(" ");
    	errMes2.setText(" ");
    	if(code.isEmpty() || !checked ) {
    		if(!checked)
    		errMes1.setText(mes1);
    		if(code.isEmpty()) 
    	    errMes2.setText(mes3);
    		ret=false;
    	}
    	else if ( !code.matches("[0-9]+")) {
	    	errMes1.setText("Please Enter Numbers only for code.");  
	    	ret=false;
	    }
    	else {
    		ret = true;
        }
        return ret;
    }
    public TakeExamController getController() {
		return this;
	}
    public Button getstartBtn() {
		return startBtn;
	}

}
