package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Exam;
import logic.LoggedUsers;
import logic.Question;
import logic.Request;
import logic.Users;

public class createExamController implements Initializable {
	private ChatClient client;
	private Users lecturer;
	private ObservableList<Question> questions = FXCollections.observableArrayList();

	@FXML
	private Button addQuestionBtn;

	@FXML
	private Button backBtn;

	@FXML
	private ComboBox<String> coursesComboBox;

	@FXML
	private Pane createExamForm;

	@FXML
	private TableColumn<Question, Integer> examViewIdColumn;

	@FXML
	private TableColumn<Question, Integer> examViewQuestioColumn;

	@FXML
	private ComboBox<String> professionsComboBox;

	@FXML
	private TableColumn<Question, Integer> questionIdViewColumn;

	@FXML
	private TableColumn<Question, String> questionTextColumn;

	@FXML
	private TableView<Question> questionViewTABLE;

	@FXML
	private TableView<Question> examViewTable;

	@FXML
	private Button removeQuestionBtn;

	@FXML
	private Button reviewExamBtn;

	@FXML
	private TextField scoreTextField;

	@FXML
	private Button setScoreBtn;

	@FXML
	private TableColumn<Question, Integer> examViewScoreColumn;

	@FXML
	private TextField setTimeTextField;

	@FXML
	private Button showQuestionView;

	@FXML
	private Label testIdLabel;

	@FXML
	private Label errLabel;

	@FXML
	void btnSelectCourse(ActionEvent event) {

	}

	@FXML
	void btnSelectProfession(ActionEvent event) {

	}

	@FXML
	public void reviewExam(ActionEvent event) {
		Platform.runLater(() -> {
			// review the exam
			// check if the exam is valid
			if (questions.size() == 0) {
				errLabel.setText("Please add questions to the exam");
				return;
			}
			if (setTimeTextField.getText().isEmpty()) {
				errLabel.setText("Please set the time of the exam");
				return;
			}
			// check if the exam time is valid
			if (!setTimeTextField.getText().matches("[0-9]+")) {
				errLabel.setText("Please enter a valid time");
				return;
			}

			// check if the exam time is valid
			if (Integer.parseInt(setTimeTextField.getText()) < 1) {
				errLabel.setText("Please enter a valid time");
				return;
			}
			// check if the exam time is valid
			if (Integer.parseInt(setTimeTextField.getText()) < 1) {
				errLabel.setText("Please enter a valid time");
				return;
			}
			// check if the sum of question scores is 100
			int scoreSum = 0;
			for (Question question : questions) {
				scoreSum += question.getScore();
			}
			if (scoreSum != 100) {
				errLabel.setText("The sum of question scores must be 100");
				return;
			}
			Exam exam = new Exam(coursesComboBox.getValue(), questions, lecturer,
					Integer.parseInt(setTimeTextField.getText()), professionsComboBox.getValue());

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/review_exam.fxml")); // specify the path to
																									// the
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
			ReviewExamController controller = loader.getController();
			controller.setClient(client);
			controller.setExam(exam);
			controller.setDataTable();
			client.setController(controller);
			// Get the Stage information
			Stage window = (Stage) reviewExamBtn.getScene().getWindow();
			window.setScene(mainScene);
			window.show();

		});
	}

	@FXML
	public void setScoreQuestion(ActionEvent event) {
		Platform.runLater(() -> {
			// set the score of the selected question
			Question question = examViewTable.getSelectionModel().getSelectedItem();
			if (question == null) {
				errLabel.setText("Please select a question");
				return;
			}
			// update the question score column on table
			questions.remove(question);

			// set the score of the question
			question.setScore(Integer.parseInt(scoreTextField.getText()));
			questions.add(question);
			examViewTable.getItems().clear();
			examViewTable.getItems().addAll(questions);
		});

	}

	@FXML
	public void removeQuetsionFromExam(ActionEvent event) {

		Platform.runLater(() -> {
			// remove the selected question from the exam view table
			Question question = examViewTable.getSelectionModel().getSelectedItem();
			if (question == null) {
				errLabel.setText("Please select a question");
				return;
			}
			// remove the question from the exam view table
			question.setScore(0);
			questions.remove(question);
			examViewTable.getItems().clear();
			examViewTable.getItems().addAll(questions);
		});

	}

	@FXML
	public void addToExamView(ActionEvent event) {
		Platform.runLater(() -> {
			// add the selected question to the exam view table
			Question question = questionViewTABLE.getSelectionModel().getSelectedItem();
			if (question == null) {
				errLabel.setText("Please select a question");
				return;
			}
			// add the question to the exam view table
			questions.add(question);
			examViewTable.getItems().clear();
			examViewTable.getItems().addAll(questions);
		});

	}

	public void setClientAndLecturer(ChatClient client, Users lecturer) {
		this.client = client;
		this.lecturer = lecturer;
	}

	@FXML
	void backToMainScreen(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LecturerPage.fxml")); // specify the path to the
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
		LecturerPageController controller = loader.getController();
		controller.setLecturerAndClient(lecturer, client);
		controller.getOngoingExamsTable();
		client.setController(controller);

		// Get the Stage information
		Stage window = (Stage) backBtn.getScene().getWindow();
		window.setScene(mainScene);
		window.show();
	}

	@FXML
	public void showQuestionView(ActionEvent event) {
		// show the question view table
		try {
			client.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Platform.runLater(() -> {
			// add some subjects to the comboBox
			if (professionsComboBox.getValue() == null || coursesComboBox.getValue() == null) {
				errLabel.setText("Please select a subject and course");
				return;
			}
			errLabel.setText("");
			String subject = professionsComboBox.getValue().toString();
			String course = coursesComboBox.getValue().toString();

			Request request = new Request("getQuestions", course);
			try {
				client.sendToServer(request);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		professionsComboBox.setOnMouseClicked(event -> {
			System.out.println(professionsComboBox.getValue());
			// add some subjects to the comboBox
			Request request = new Request("getSubjects", null);
			try {
				client.openConnection();
				client.sendToServer(request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		coursesComboBox.setOnMouseClicked(event -> {
			// add some courses to the comboBox depending on the subject currently selected
			String subject = professionsComboBox.getValue();
			if (subject != null && !subject.equals("")) {
				Request request = new Request("getCourses", subject);
				try {
					client.openConnection();
					client.sendToServer(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		questionIdViewColumn.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		questionTextColumn.setCellValueFactory(new PropertyValueFactory<>("questionDescription"));
		examViewIdColumn.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		examViewQuestioColumn.setCellValueFactory(new PropertyValueFactory<>("questionDescription"));
		examViewScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

	}

	public void upadteQuestionViewTable(ArrayList<Question> questionsArray) {

		questionViewTABLE.getItems().clear();
		questionViewTABLE.getItems().addAll(questionsArray);

	}

	public void updateSubjectsComboBox(ArrayList<String> subjects) {
		Platform.runLater(() -> {
			ObservableList<String> list = FXCollections.observableArrayList(subjects);
			professionsComboBox.setItems(list);
		});
	}

	public void updateCoursesComboBox(ArrayList<String> courses) {
		Platform.runLater(() -> {
			ObservableList<String> list = FXCollections.observableArrayList(courses);
			coursesComboBox.setItems(list);
		});

	}

}
