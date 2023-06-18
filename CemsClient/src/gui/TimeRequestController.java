package gui;

import java.io.IOException;


import client.ChatClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Exam;
import logic.Request;
import logic.RequestTime;
import logic.Users;

public class TimeRequestController {
	private ChatClient client;
	private Users lecturer;
    private Exam exam;

    @FXML
    private Label errLabel;
    @FXML
    private TextField addTime;

    @FXML
    private Button backBtn;

    @FXML
    private TextField reasonText;

    @FXML
    private Button requestTimeBtn;

    @FXML
    private Pane timeRequestForm;

    @FXML
    private Text toolCreateExams;

    @FXML
    private Text toolGrade;

    @FXML
    private Text toolStatistics;

    @FXML
    private Text toolWriteQuestions;



    @FXML
    void requestTime(ActionEvent event) {
        int examID = exam.getExamId();
        String lecturerName= lecturer.getFirstName() + " " + lecturer.getLastName();
        String reason = reasonText.getText();
        String time= addTime.getText();
        if (time==null || reason==null || time.equals("") || reason.equals("")) {
            errLabel.setText("Please fill all fields");
            return;
        }
        RequestTime requestTime = new RequestTime(String.valueOf(examID), lecturerName, Integer.parseInt(time), reason);
        Request request = new Request ("RequestTime", requestTime);
        try {
            client.openConnection();
            client.sendToServer(request);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void setClientAndLecturer(ChatClient client, Users lecturer) {
        this.client = client;
        this.lecturer = lecturer;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
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


    public void requestTimeSuccess() {
        Platform.runLater(() -> {
            errLabel.setText("Time request sent successfully");
        });
    }


}
