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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Exam;
import logic.Request;
import logic.Users;

public class pastExamsController implements Initializable {

    private ChatClient client;
    private Users lecturer;
    private ArrayList<Exam> exams;

    @FXML
    private Button backBtn;
    @FXML
    private TableColumn<Exam, Integer> cheat;

    @FXML
    private TableColumn<Exam, Integer> code;

    @FXML
    private TableColumn<Exam, String> dateEnd;

    @FXML
    private TableColumn<Exam, String> dateStart;

    @FXML
    private TableColumn<Exam, Integer> examId;

    @FXML
    private TableView<Exam> pastExamsTable;

    @FXML
    private TableColumn<Exam, Integer> studentsNumber;

    @FXML
    private TableColumn<Exam, Integer> testTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        examId.setCellValueFactory(new PropertyValueFactory<>("examId"));
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        testTime.setCellValueFactory(new PropertyValueFactory<>("testTime"));
        dateStart.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        dateEnd.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
        cheat.setCellValueFactory(new PropertyValueFactory<>("cheat"));
        studentsNumber.setCellValueFactory(new PropertyValueFactory<>("studentsNumber"));
    }


    public void setClientAndLecturer(ChatClient client, Users lecturer) {
        this.client = client;
        this.lecturer = lecturer;
    }

    public void setExamsTable(ArrayList<Exam> exams) {
        this.exams = exams;
        pastExamsTable.getItems().clear();
        pastExamsTable.getItems().setAll(exams);
    }
    public void getPastExams(){
        Request request = new Request("getPastExams", lecturer);
        try {
            client.openConnection();
            client.sendToServer(request);
        }catch(IOException e){
            e.printStackTrace();
        }
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

}
