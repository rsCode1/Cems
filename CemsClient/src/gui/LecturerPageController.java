package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Exam;
import logic.LogInInfo;
import logic.Request;
import logic.Users;

public class LecturerPageController implements Initializable {
	private Users lecturer;
	private ChatClient client;
	private ArrayList<Exam> ongoingExams;

	@FXML
	private TableColumn<Exam, Integer> examId;

	@FXML
	private TableColumn<Exam, Integer> examCode;

	@FXML
	private TableColumn<Exam, Integer> timeRem;

	@FXML
	private TableView<Exam> ongoingExamsTable;

	@FXML
	private Button CreateExamBtn;

	@FXML
	private Button CloseExamBtn;

	@FXML
	private Button StartExamBtn;

	@FXML
	private Pane createQuestionForm;

	@FXML
	private Label lecturerName;

	@FXML
	private Button logOutBtn;

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
	private Button TimeRequestBtn;

	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		// toolTimeRequest.setOnMouseClicked(e -> { make tool time request-> clickable
		// showTimeRequestForm();
		// });

		examId.setCellValueFactory(new PropertyValueFactory<Exam, Integer>("examId"));
		examCode.setCellValueFactory(new PropertyValueFactory<Exam, Integer>("code"));
		timeRem.setCellValueFactory(new PropertyValueFactory<Exam, Integer>("testTime"));
		Platform.runLater(() -> {
			lecturerName.setText(lecturer.getFirstName() + " " + lecturer.getLastName());
		});

		toolGrade.setOnMouseClicked(e -> {
			Platform.runLater(() -> {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/lecturerApproval.fxml")); // specify
																											// the
																											// path
																											// to
				// the new fxml file
				Parent parent = null;
				try {
					parent = loader.load();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				Scene nextScene = new Scene(parent);

				// Get the new scene's controller and pass the ChatClient instance to it
				LecturerApprovalController controller = loader.getController();
				controller.setClientAndLecturer(this.client, lecturer);
				controller.getGrades();
				client.setController(controller);

				// Get the Stage information
				Stage window = (Stage) toolGrade.getScene().getWindow();
				window.setScene(nextScene);
				window.show();
			});
			

		});

	}

	public void setLecturerAndClient(Users lecturer, ChatClient client) {
		this.lecturer = lecturer;
		this.client = client;
	}

	@FXML
	public void getOngoingExamsTable() {

		Request request = new Request("getOngoingExams", lecturer);
		try {
			client.openConnection();
			client.sendToServer(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void setOngoingExamsTable(ArrayList<Exam> exam) {
		System.out.println("worked should update");
		ongoingExamsTable.getItems().clear();
		ongoingExamsTable.getItems().addAll(exam);
	}

	public void setExams(ArrayList<Exam> exams) {
		this.ongoingExams = exams;
	}

	public ArrayList<Exam> getExams() {
		return ongoingExams;
	}

	@FXML
	public void startExam(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/start_exam.fxml")); // specify the path to the
		// new fxml file
		Parent parent = null;
		try {
			parent = loader.load();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Scene nextScene = new Scene(parent);

		// Get the new scene's controller and pass the ChatClient instance to it
		StartExamController controller = loader.getController();
		controller.setClientAndLecturer(client, lecturer);
		controller.getExamsTable();
		client.setController(controller);

		// This line gets the Stage information
		Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		window.setScene(nextScene);
		window.show();
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

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScreen.fxml")); // specify the path to the
		// new fxml file
		Parent parent = null;
		try {
			parent = loader.load();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Scene nextScene = new Scene(parent);

		// Get the new scene's controller and pass the ChatClient instance to it
		LoginScreenController controller = loader.getController();
		controller.setClient(this.client, controller);

		// Get the Stage information
		Stage window = (Stage) logOutBtn.getScene().getWindow();
		window.setScene(nextScene);
		window.show();
	}

	@FXML
	public void showCreateQuestionForm(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/write_question.fxml")); // specify the path to
		// the new fxml file
		Parent parent = null;
		try {
			parent = loader.load();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Scene nextScene = new Scene(parent);

		// Get the new scene's controller and pass the ChatClient instance to it
		writeQuestionController controller = loader.getController();
		controller.setClientAndLecturer(this.client, lecturer);
		client.setController(controller);
		// Get the Stage information
		Stage window = (Stage) writeQuestionBtn.getScene().getWindow();
		window.setScene(nextScene);
		window.show();

	}

	@FXML
	public void showCreateExamForm(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/create_exam.fxml")); // specify the path to
		// the new fxml file
		Parent parent = null;

		try {
			parent = loader.load();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Scene nextScene = new Scene(parent);

		// Get the new scene's controller and pass the ChatClient instance to it
		createExamController controller = loader.getController();
		controller.setClientAndLecturer(this.client, lecturer);
		client.setController(controller);
		// Get the Stage information
		Stage window = (Stage) CreateExamBtn.getScene().getWindow();
		window.setScene(nextScene);
		window.show();

	}

	@FXML
	public void showTimeRequestForm(ActionEvent event) {
		Exam exam = ongoingExamsTable.getSelectionModel().getSelectedItem();
		if (exam == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No exam selected");
			alert.setContentText("Please select an exam from the table");
			alert.showAndWait();
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/time_request.fxml")); // specify the path to
		// the new fxml file
		Parent parent = null;

		try {
			parent = loader.load();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Scene nextScene = new Scene(parent);

		// Get the new scene's controller and pass the ChatClient instance to it
		TimeRequestController controller = loader.getController();
		controller.setClientAndLecturer(this.client, lecturer);
		client.setController(controller);
		// Get the Stage information
		Stage window = (Stage) TimeRequestBtn.getScene().getWindow();
		window.setScene(nextScene);
		window.show();
	}

}
