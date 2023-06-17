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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Exam;
import logic.Request;
import logic.Users;

public class LecturerApprovalController implements Initializable {

    private ChatClient client;
    private Users lecturer;
    private ArrayList<Exam> exams;

    @FXML
    private Label errLabel;

    @FXML
    private Button backBtn;
    @FXML
    private Button approveBtn;

    @FXML
    private Button changeBtn;

    @FXML
    private TableColumn<Exam, String> courseName;

    @FXML
    private TableColumn<Exam, Integer> examId;

    @FXML
    private TableView<Exam> examsTable;

    @FXML
    private TableColumn<Exam, Integer> grade;

    @FXML
    private TableColumn<Exam, Integer> studentId;

    @FXML
    private TextField newGrade;

    @FXML
    private TextArea changeGradeReason;

    @FXML
    void approveGrade(ActionEvent event) {
        Exam exam = examsTable.getSelectionModel().getSelectedItem();
        if (exam == null) {
            errLabel.setText("Please select an exam");
            return;
        }
        Request request = new Request("approveGrade", exam);
        try {
            client.openConnection();
            client.sendToServer(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        exams.remove(exam);

    }

    @FXML
    void changeGrade(ActionEvent event) {
        Exam exam = examsTable.getSelectionModel().getSelectedItem();
        String newGrade = this.newGrade.getText();
        if (exam == null) {
            errLabel.setText("Please select an exam");
            return;
        }
        if (newGrade == null || newGrade.isEmpty()) {
            errLabel.setText("Please enter a new grade");
            return;
        }
        if (newGrade.matches("[0-9]+") == false || Integer.parseInt(newGrade) > 100
                || Integer.parseInt(newGrade) < 0) {
            errLabel.setText("Please enter a valid grade");
            return;
        }
        if (changeGradeReason == null || changeGradeReason.getText().isEmpty()) {
            errLabel.setText("Please enter a reason for the change");
            return;
        }
        exam.setGrade(Integer.parseInt(newGrade));
        exam.setChangeGradeReason(changeGradeReason.getText());
        Request request = new Request("changeGrade", exam);
        try {
            client.openConnection();
            client.sendToServer(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        exams.remove(exam);

    }

    public void getGrades() {
        Request request = new Request("getGrades", lecturer);

        try {
            client.openConnection();
            client.sendToServer(request);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setGradesTable(ArrayList<Exam> exams) {
        Platform.runLater(() -> {
            this.exams = exams;
            examsTable.getItems().clear();
            examsTable.getItems().addAll(exams);
        });
    }

    public void setClientAndLecturer(ChatClient client, Users lecturer) {
        this.client = client;
        this.lecturer = lecturer;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        examId.setCellValueFactory(new PropertyValueFactory<>("examId"));
        courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        studentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        grade.setCellValueFactory(new PropertyValueFactory<>("grade"));

    }

    @FXML
    public void goBack(ActionEvent event) {
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

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public void setExams(ArrayList<Exam> exams) {
        this.exams = exams;
    }

    public void approveGradeSuccess() {
        Platform.runLater(() -> {
            errLabel.setText("Grade approved successfully");
            setGradesTable(exams);
        });

    }

    public void changeGradeSuccess() {
        Platform.runLater(() -> {
            errLabel.setText("Grade changed successfully");
            setGradesTable(exams);
        });

    }

}