package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.LogInInfo;
import logic.Request;
import logic.Users;

public class FirstPageController {
	private Users lecturer;
	private ChatClient client;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane createQuestionForm;
    @FXML
    private Button logOutBtn;

    @FXML
    void initialize() {
        assert createQuestionForm != null : "fx:id=\"createQuestionForm\" was not injected: check your FXML file 'FirstPage.fxml'.";

    }
    public void setLecturerAndClient(Users lecturer,ChatClient client) {
    	this.lecturer=lecturer;
    	this.client=client;
    }
    
	@FXML
	public void logOut(ActionEvent event) {
		LogInInfo loginData = new LogInInfo(lecturer.getUserName(), lecturer.getPassword());
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScreen.fxml")); // specify the path to the new fxml file
	Parent parent=null;
	try {
		parent = loader.load();
		
	}catch (Exception e) {
		e.printStackTrace();
	}
    Scene nextScene = new Scene(parent);
    
    // Get the new scene's controller and pass the ChatClient instance to it
    LoginScreenController controller = loader.getController();
    controller.setClient(this.client,controller);
    // Get the Stage information
    Stage window = (Stage) logOutBtn.getScene().getWindow();
    window.setScene(nextScene);
    window.show();
}
    }

