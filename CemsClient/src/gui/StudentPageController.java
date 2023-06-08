
package gui;


import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.LogInInfo;
import logic.Request;
import logic.Users;

import java.io.IOException;
public class StudentPageController {
    
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
    
    @FXML
    public void TakeExamForm() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TakeExam.fxml"));
    		Parent root = loader.load();
    		Stage stage = new Stage();
    		stage.setScene(new Scene(root));
    		stage.show();
    		} 
    	catch (IOException e) {
        e.printStackTrace();
    	}
    
    }
   
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
	


    @FXML
    public void ShowResults() {
        // Implement the logic of the "Results" button.
    }

    @FXML
    public void takeManualExam() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Take_manual_exam.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public StudentPageController getController() {
		return this;
	}
}