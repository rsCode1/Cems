package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.Exam;
import logic.Request;
import logic.Users;

public class StartExamController implements Initializable {
    private ChatClient client;
    private Users lecturer;
    private ArrayList<Exam> exams;
    @FXML
    private TableColumn<Exam, String> courseName;

    @FXML
    private TableColumn<Exam, Integer> examId;

    @FXML
    private TableView<Exam> examsTable;

    @FXML
    private TableColumn<Exam, String> lComments;

    @FXML
    private Label label;

    @FXML
    private TableColumn<Exam, String> sComments;

    @FXML
    private TableColumn<Exam, Integer> testTime;

    @FXML
    void startExamBtn(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        examId.setCellValueFactory(new PropertyValueFactory<Exam, Integer>("examId"));
        courseName.setCellValueFactory(new PropertyValueFactory<Exam, String>("courseName"));
        lComments.setCellValueFactory(new PropertyValueFactory<Exam, String>("lecturerComments"));
        sComments.setCellValueFactory(new PropertyValueFactory<Exam, String>("studentComments"));
        testTime.setCellValueFactory(new PropertyValueFactory<Exam, Integer>("testTime"));

    }
    public void setExamsTable(ArrayList<Exam> exam) {
        examsTable.getItems().clear();
        examsTable.getItems().addAll(exam);
    }
    public void getExamsTable() {
        Request request = new Request("getExamsByLecturer",lecturer);
        try {
            client.sendToServer(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setClientAndLecturer(ChatClient client, Users lecturer) {
        this.client = client;
        this.lecturer = lecturer;
    }
    public void setExams(ArrayList<Exam> exams) {
        this.exams = exams;
    }
    public ArrayList<Exam> getExams() {
        return exams;
    }
    public void setLabel(String label) {
        this.label.setText(label);
    }
}
