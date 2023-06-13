package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import client.ChatClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Question;
import logic.Request;
import logic.Users;

public class writeQuestionController {
    private ChatClient client;
    private Users lecturer;

    @FXML
    private TextField answer1;

    @FXML
    private TextField answer2;

    @FXML
    private TextField answer3;

    @FXML
    private TextField answer4;

    @FXML
    private Button backBtn;

    @FXML
    private ToggleGroup correctAnswer;

    @FXML
    private ToggleGroup correctAnswer1;

    @FXML
    private ComboBox<String> coursesComboBox;

    @FXML
    private Pane createQuestionForm;

    @FXML
    private ComboBox<String> professionsComboBox;

    @FXML
    private TextArea questionDescriptionField;

    @FXML
    private RadioButton radio1;

    @FXML
    private RadioButton radio2;

    @FXML
    private RadioButton radio3;

    @FXML
    private RadioButton radio4;

    @FXML
    private Button saveButton;

    @FXML
    private Text toolCreateExams;

    @FXML
    private Text toolGrade;

    @FXML
    private Text toolStatistics;

    @FXML
    private Text toolTimeRequest;

    @FXML
    private Text toolWriteQuestion;

    @FXML
    private Label saveLabelBtn;

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

    public void setClientAndLecturer(ChatClient client, Users lecturer) {
        this.client = client;
        this.lecturer = lecturer;
    }

    // sends the question to the server
    // uses the Question class from CemsShared
    // creates a Request object with the question and sends it to the server
    @FXML
    public void btnSaveQuestion(ActionEvent event) {
        String questionDescription = questionDescriptionField.getText();
        String answer1 = this.answer1.getText();
        String answer2 = this.answer2.getText();
        String answer3 = this.answer3.getText();
        String answer4 = this.answer4.getText();
        int correctAnswer = 0;
        if (radio1.isSelected()) {
            correctAnswer = 1;
        } else if (radio2.isSelected()) {
            correctAnswer = 2;
        } else if (radio3.isSelected()) {
            correctAnswer = 3;
        } else if (radio4.isSelected()) {
            correctAnswer = 4;
        }
        
        String course = coursesComboBox.getValue();
        String subject = professionsComboBox.getValue();
        if (questionDescription.equals("") || answer1.equals("") || answer2.equals("") || answer3.equals("")
            || answer4.equals("") || correctAnswer == 0 || course == null || subject == null) {
                saveLabelBtn.setText("Please fill all the fields");
                return;
            }

        

        String author = lecturer.getFirstName()+" "+lecturer.getLastName();
        int authorID = lecturer.getId();
        // create a Question object
        Question question = new Question(questionDescription, answer1, answer2, answer3, answer4, correctAnswer,
                course, authorID,author);
        // create a Request object
        Request request = new Request("writeQuestion", question);
        // send the request to the server
        try {
            client.sendToServer(request);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        saveLabelBtn.setText("Question saved successfully");

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
    // @FXML
    // public void btnSelectCourse(ActionEvent event) {
    // // add some courses to the comboBox depending on the subject currently
    // selected
    // String subject = professionsComboBox.getValue();
    // if (subject.equals("Select Subject"))
    // return;
    // Request request = new Request("getCourses", subject);
    // try {
    // client.sendToServer(request);
    // }catch(Exception e) {
    // e.printStackTrace();
    // }

    // }

    // @FXML
    // public void btnSelectProfession(MouseDragEvent event) {
    // System.out.println("btnSelectProfession");
    // // add some subjects to the comboBox
    // Request request = new Request("getSubjects", null);
    // try {
    // client.sendToServer(request);
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    @FXML
    public void initialize() {
        radio1.setToggleGroup(correctAnswer);
        radio2.setToggleGroup(correctAnswer);
        radio3.setToggleGroup(correctAnswer);
        radio4.setToggleGroup(correctAnswer);

        professionsComboBox.setOnMouseClicked(event -> {
            System.out.println(professionsComboBox.getValue());
            // add some subjects to the comboBox
            Request request = new Request("getSubjects", null);
            try {
                client.sendToServer(request);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        coursesComboBox.setOnMouseClicked(event -> {
            // add some courses to the comboBox depending on the subject currently selected
            String subject = professionsComboBox.getValue();
            if (!subject.equals("Select Course")) {
                Request request = new Request("getCourses", subject);
                try {
                    client.sendToServer(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

}
