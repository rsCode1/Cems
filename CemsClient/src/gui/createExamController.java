package gui;

import java.io.IOException;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Users;


public class createExamController {
	private ChatClient client;
	private Users lecturer;

	@FXML
	private Button backBtn;

	@FXML
	private ComboBox<?> coursesComboBox;

	@FXML
	private Pane createQuestionForm;

	@FXML
	private ComboBox<?> professionsComboBox;

	@FXML
	private Label testIdLabel;

	@FXML
	void btnSelectCourse(ActionEvent event) {

	}

	@FXML
	void btnSelectProfession(ActionEvent event) {

	}

	public void setClientAndLecturer(ChatClient client, Users lecturer) {
		this.client = client;
		this.lecturer = lecturer;
	}

	@FXML
	void backToMainScreen(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/FirstPage.fxml")); // specify the path to the
																							// main screen FXML file
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene mainScene = new Scene(parent);

		// Get the main screen's controller and pass the ChatClient and lecturer
		// instances to it
		FirstPageController controller = loader.getController();
		controller.setLecturerAndClient(lecturer, client);

		// Get the Stage information
		Stage window = (Stage) backBtn.getScene().getWindow();
		window.setScene(mainScene);
		window.show();
	}

}
