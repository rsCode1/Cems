package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
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
    private Button backBtn;

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
            client.openConnection();
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
    public void donothing(ArrayList<Exam> exam) {
    	System.out.println("worked should update start");

    }

}
