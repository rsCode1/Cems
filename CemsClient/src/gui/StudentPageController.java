
package gui;


import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Exam;
import logic.LogInInfo;
import logic.Request;

import logic.Users;

import java.io.IOException;

public class StudentPageController {
	/**the student page controller is for students home page in our cems app ,
	 * once a student is logged in, it welcome the student to the cems app
	 * displaye
	 */
	private ChatClient client;
	private Users student;
	
	@FXML
    private Button btnLogOut;
    
    @FXML
    private Text dateLabel;
    
    @FXML
    private Button btnTakeExam;
    
  
    
    @FXML
    private Button btnResults;
    
    @FXML
    private Button btnResults1;
    
    @FXML
    private Label WelcomeLabel;

    @FXML
    private Label dataLabel;
    
    public void setStudentAndClient(Users Student,ChatClient client) {
    	this.student=Student;
    	this.client=client;
    }

    public void setWelcomeLabel(String txt) {
      WelcomeLabel.setText(txt);
    }
    @FXML
    public void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");  
        LocalDateTime now = LocalDateTime.now();  
        dateLabel.setText("Date : " + dtf.format(now));  
		
    }
    /**
	 * when student clicks on start Digita Test button
	 */
    @FXML
    public void TakeDigitalExamForm() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TakeExam.fxml"));
    		Parent root = loader.load();
    		Stage stage = new Stage();
    		TakeExamController takeExamController = loader.getController();
    		takeExamController.setStudentAndClient(student, client,takeExamController);
    		takeExamController.setDigOrMan(0);
    		takeExamController.setLabel("Welcome to Take Digital Exam Form");
    		stage.setScene(new Scene(root));
    		stage.show();
    		} 
    	catch (IOException e) {
        e.printStackTrace();
    	}
    
    }
   
	/**
	 * when student clicks on logout button
	 */
    @FXML
    void LogOut(ActionEvent event) {
    	LogInInfo loginData = new LogInInfo(student.getUserName(), student.getPassword());
		try {
			client.openConnection();
			if (client.isConnected()) {
				client.sendToServer(new Request("LOGOUT", loginData));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				System.out.println("Not connected to server.");
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScreen.fxml")); // specify the path to the new fxml file
			Parent parent=null;
			try {
				parent = loader.load();
				Scene nextScene = new Scene(parent);
			    
			    // Get the new scene's controller and pass the ChatClient instance to it
			    LoginScreenController controller = loader.getController();
			    controller.setClient(this.client,controller);
			    // Get the Stage information
			    Stage window = (Stage) btnLogOut.getScene().getWindow();
			    window.setScene(nextScene);
			    window.show();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		   
		}
		catch (IOException e) {
			e.printStackTrace();
			}
		}
	

   /**
	 * when student clicks on show results button
	 */
    @FXML
    public void ShowResults(ActionEvent event) {
        try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/StudentHistory.fxml"));
    		Parent root = loader.load();
    		Stage stage = new Stage();
    		StudentHistoryController controller = loader.getController();
    		controller.setStudentAndClient(student, client,controller);
    		if (client.isConnected()) {
			client.sendToServer(new Request("GetStudentGrades", student.getId()));}
			try {
				Thread.sleep(150 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		controller.viewPage();
    		stage.setScene(new Scene(root));
    		stage.show();
    		} 
    	catch (IOException e) {
        e.printStackTrace();
    	}
    	
    }
	/**
	 * this methood will be activated when new grade is approved from lecturer
	 * it shos popout window with the grade details
	 * @param exam
	 */
    public void showNewApprovedgrade(Exam exam){
		Platform.runLater(() -> {
		if (student.getId() == exam.getStudentId()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/NewGradePopout.fxml"));
        
        try {
            Parent root = loader.load();
            NewGradePopController controller = loader.getController();
            String Mes="Your grade in "+ exam.getCourseName() + " is: " + exam.getGrade();
            controller.setMes(Mes);
            
                Stage window = new Stage();
                window.setScene(new Scene(root));
                window.show();
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	 });

		
	}
    
	/**
	 * when student clicks on start Manual Test button
	 */
    @FXML
    public void takeManualExam() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TakeExam.fxml"));
    		Parent root = loader.load();
    		Stage stage = new Stage();
    		TakeExamController takeExamController = loader.getController();
    		takeExamController.setStudentAndClient(student, client,takeExamController);
    		takeExamController.setDigOrMan(1);
    		takeExamController.setLabel("Welcome to Take Manuel Exam Form");
    		stage.setScene(new Scene(root));
    		stage.show();
    		} 
    	catch (IOException e) {
        e.printStackTrace();
    	}
    }
    public StudentPageController getController() {
		return this;
	}
}