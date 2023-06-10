package gui;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.LogInInfo;
import logic.Request;
import logic.Users;

public class FirstPageController {
	private Users lecturer;
	private ChatClient client;

    @FXML
    private Button CreateExamBtn;

    @FXML
    private Button StartExamBtn;

    @FXML
    private Pane createQuestionForm;

    @FXML
    private Label lecturerName;

    @FXML
    private Button logOutBtn;

    @FXML
    private TableView<?> ongoingExamsTable;

    @FXML
    private TableColumn<?, ?> ongoingTableExam;

    @FXML
    private TableColumn<?, ?> ongoingTableSubject;

    @FXML
    private TableColumn<?, ?> ongoingTableTime;

    @FXML
    private Text toolCreateExams;

    @FXML
    private Text toolGrade;

    @FXML
    private Text toolStatistics;

    @FXML
    private Text toolTimeRequest;

    @FXML
    private Text toolWriteQuestions;

    @FXML
    private Button writeQuestionBtn;

    @FXML
    void initialize() {
        Platform.runLater(() -> {lecturerName.setText(lecturer.getFirstName()+" "+lecturer.getLastName());});

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

@FXML
public void showCreateQuestionForm(ActionEvent event) {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/write_question.fxml")); // specify the path to the new fxml file
	Parent parent=null;
	try {
		parent = loader.load();
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	Scene nextScene = new Scene(parent);
	
	// Get the new scene's controller and pass the ChatClient instance to it
	writeQuestionController controller = loader.getController();
	controller.setClientAndLecturer(this.client,lecturer);
	client.setController(controller);
	// Get the Stage information
	Stage window = (Stage) writeQuestionBtn.getScene().getWindow();
	window.setScene(nextScene);
	window.show();


}
}

