package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import logic.Exam;
import logic.Question;

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
    void saveExam(ActionEvent event) {

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
