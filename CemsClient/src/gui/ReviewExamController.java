package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Exam;
import logic.Question;
import logic.Request;

public class ReviewExamController implements Initializable {

    private ChatClient client;
    private Exam exam;
    @FXML
    private TableView<Question> ExamQuestionTable;

    @FXML
    private TableColumn<Question, String> answer1;

    @FXML
    private TableColumn<Question, String> answer2;

    @FXML
    private TableColumn<Question, String> answer3;

    @FXML
    private TableColumn<Question, String> answer4;

    @FXML
    private Text course;

    @FXML
    private Pane createQuestionForm;

    @FXML
    private TextArea lecturetComments;

    @FXML
    private TableColumn<Question, Integer> questionId;

    @FXML
    private TableColumn<Question, String> questionText;

    @FXML
    private Button saveExamBtn;

    @FXML
    private TableColumn<Question, Integer> score;

    @FXML
    private TextArea studentComments;

    @FXML
    private Text subject;

    @FXML
    private Label testIdLabel;

    @FXML
    private Text testTiime;
    @FXML
    private Label errLabel;
    @FXML
    private Button backBtn;


    @FXML
    public void goBack(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/create_exam.fxml")); // specify the path to the
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
		createExamController controller = loader.getController();
		controller.setClientAndLecturer(client,exam.getLecturer());
        client.setController(controller);
		// Get the Stage information
		Stage window = (Stage) backBtn.getScene().getWindow();
		window.setScene(mainScene);
		window.show();

    }


    public void setLabel() {
        Platform.runLater(() -> {
        errLabel.setText("Exam saved successfully");
        });
    }

    @FXML
    void saveExam(ActionEvent event) {
        exam.setLecturerComments(lecturetComments.getText());
        exam.setStudentComments(studentComments.getText());
        try {
            client.sendToServer(new Request("saveExam", exam));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void setDataTable() {
        Platform.runLater(() -> {
        ExamQuestionTable.getItems().addAll(exam.getQuestions());
        course.setText(exam.getCourseName());
        testTiime.setText(String.valueOf(exam.getTestTime()));
        subject.setText(exam.getSubject());
        });

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        questionId.setCellValueFactory(new PropertyValueFactory<>("questionID"));
        questionText.setCellValueFactory(new PropertyValueFactory<>("questionDescription"));
        answer1.setCellValueFactory(new PropertyValueFactory<>("answer1"));
        answer2.setCellValueFactory(new PropertyValueFactory<>("answer2"));
        answer3.setCellValueFactory(new PropertyValueFactory<>("answer3"));
        answer4.setCellValueFactory(new PropertyValueFactory<>("answer4"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));

    }

    public void setClient(ChatClient client) {
        this.client = client;
    }
    public void  setExam(Exam exam) {
        this.exam = exam;
    }
}
