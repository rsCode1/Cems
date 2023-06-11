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
import logic.LoggedUsers;
import logic.Question;
import logic.Request;
import logic.Users;


public class createExamController implements Initializable {
	private ChatClient client;
	private Users lecturer;

    @FXML
    private Button addQuestionBtn;

    @FXML
    private Button backBtn;

    @FXML
    private ComboBox<String> coursesComboBox;

    @FXML
    private Pane createExamForm;

    @FXML
    private TableColumn<?, ?> examViewIdColumn;

    @FXML
    private TableColumn<?, ?> examViewQuestioColumn;

    @FXML
    private ComboBox<String> professionsComboBox;

    @FXML
    private TableColumn<Question, Integer> questionIdViewColumn;

    @FXML
    private TableColumn<Question, String> questionTextColumn;

    @FXML
    private TableView<Question> questionViewTABLE;

	@FXML
    private TableView<?> examViewTable;

    @FXML
    private Button removeQuestionBtn;

    @FXML
    private Button reviewExamBtn;

    @FXML
    private TextField scoreTextField;

    @FXML
    private Button setScoreBtn;

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

	public void setClientAndLecturer(ChatClient client, Users lecturer) {
		this.client = client;
		this.lecturer = lecturer;
	}

	@FXML
	void backToMainScreen(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/FirstPage.fxml")); // specify the path to the
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
		FirstPageController controller = loader.getController();
		controller.setLecturerAndClient(lecturer, client);

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
		String course = coursesComboBox.getValue().toString();
		if (course==null) {
			errLabel.setText("Please select a subject and course");
			return;
		}
		Request request = new Request("getQuestions", course);
		try {
			client.sendToServer(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
