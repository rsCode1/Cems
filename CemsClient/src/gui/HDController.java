package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeSet;

import client.ChatClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Exam;
import logic.Grades;
import logic.LogInInfo;
import logic.Request;
import logic.RequestTime;
import logic.Users;
import ocsf.client.*;

public class HDController implements Initializable {
	private ChatClient client;
	private RequestTime requestTime;
	private Users HOD;
	private TreeSet<String> infoSelecttionArray = new TreeSet<String>();
	private String whoToShow;
	private String test;
	private ArrayList<String> generalGradesArray = new ArrayList<String>();

	public void SetHeadOfDepartment(Users Hod, ChatClient client) {
		this.HOD = Hod;
		this.client = client;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public void setGeneralArray(ArrayList<String> generalGradesArray) {
		this.generalGradesArray = generalGradesArray;
	}

	@FXML
	private Button btn;

	@FXML
	private Label checkedWholbl;
	@FXML
	private Label wholbl1;
	@FXML
	private RadioButton medianrb;
	@FXML
	private Label tt11;
	@FXML
	private RadioButton gparb;
	@FXML
	private RadioButton examrb;
	@FXML
	private RadioButton deistributionrb;
	@FXML
	private RadioButton studentrb;

	@FXML
	private RadioButton lecturerrb;
	@FXML
	private Label errlbl;

	@FXML
	private RadioButton courserb1;

	@FXML
	private ResourceBundle resources;
	@FXML
	private Label testlbl;
	@FXML
	private Label gPAlbl;
	@FXML
	private Label medianlbl;
	@FXML
	private Label selectionlbl;
	@FXML
	private Label checkinfolLabel;
	@FXML
	private TextField gpaTxtField;
	@FXML
	private TextField medianTxtField;
	@FXML
	private TextField selectionNameTxtField;// use for the name of the selected parameter

	@FXML
	private URL location;

	@FXML
	private Button LogOutBTN;

	public Button getLogOutBTN() {
		return LogOutBTN;
	}

	@FXML
	private TableView<RequestTime> requestTable;

	@FXML
	private Button ApproveBTN;
	@FXML
	private TextField RequestExamIDText;////////////////////

	@FXML
	private Button refreshButton;

	@FXML
	private Button rejectBTN;

	public Button getRejectBTN() {
		return rejectBTN;
	}

	@FXML
	private Button ShowStudentBTN;

	@FXML
	private TextField GPA_LECtextArea;
	@FXML
	private TextField StudentName;
	@FXML
	private TextField LectuerName;

	@FXML
	private TextField LectuerID;
	@FXML
	private TextField Median_LECTextArea;

	@FXML
	private Button ShowLectuerBTN;
	@FXML
	private Button ShowCourseBTN;

	@FXML
	private TextField GPA_STUtextArea;

	@FXML
	private TextField median_STUtextArea;

	@FXML
	private TextField ID_STUtextArea;

	@FXML
	private TextField GPA_GradestextArea;

	@FXML
	private TextField median_GradesTextArea;

	@FXML
	private TextField ID_GradetextArea;
	@FXML
	private TextField EnterLectuerName;
	@FXML
	private TextField CourseName;

	@FXML
	private TableColumn<RequestTime, String> IDColumn;
	@FXML
	private TableColumn<RequestTime, String> ExamIdColumn;
	@FXML
	private TableColumn<RequestTime, String> RequestedByColumn;
	@FXML
	private TableColumn<RequestTime, Integer> ExtraTimeColumn;
	@FXML
	private TableColumn<RequestTime, String> ReasonColumn;
	@FXML
	private BarChart<String, Number> barChartLec;
	@FXML
	private BarChart<String, Number> barChartStud;
	@FXML
	private BarChart<String, Number> barChartCourse;
	@FXML
	private BarChart<String, Number> generalScreenBarChart;
	private ToggleGroup toggleGroup;
	@FXML
	private Label tt22;
	@FXML
	private AnchorPane anchorpane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		toggleGroup = new ToggleGroup();
		studentrb.setToggleGroup(toggleGroup);
		lecturerrb.setToggleGroup(toggleGroup);
		courserb1.setToggleGroup(toggleGroup);
		examrb.setToggleGroup(toggleGroup);

		// Create bar chart

		// Set chart title and axis labels
		generalScreenBarChart.setTitle("Sample Bar Chart");
		CategoryAxis xAxis = (CategoryAxis) generalScreenBarChart.getXAxis();
		xAxis.setLabel("Category");
		NumberAxis yAxis = (NumberAxis) generalScreenBarChart.getYAxis();
		yAxis.setLabel("Value");

		// TableView<RequestTime> requests = new TableView<>();
		// TableColumn<RequestTime, String> idColumn = new TableColumn<>("Request ID");
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
		// TableColumn<RequestTime, String> examIdColumn = new TableColumn<>("exam ID");
		ExamIdColumn.setCellValueFactory(new PropertyValueFactory<>("examID"));
		// TableColumn<RequestTime, String> requestedByColumn = new
		// TableColumn<>("Requested By");
		RequestedByColumn.setCellValueFactory(new PropertyValueFactory<>("requestedBy"));
		// TableColumn<RequestTime, Integer> extraTimeColumn = new TableColumn<>("Extra
		// Time");
		ExtraTimeColumn.setCellValueFactory(new PropertyValueFactory<>("extraTime"));
		// TableColumn<RequestTime, String> reasonColumn = new TableColumn<>("Reason");
		ReasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
		/*
		 * requests.getColumns().add(idColumn); requests.getColumns().add(examIdColumn);
		 * requests.getColumns().add(requestedByColumn);
		 * requests.getColumns().add(extraTimeColumn);
		 * requests.getColumns().add(reasonColumn);
		 */

		try {
			client = new ChatClient("localhost", 5555, this);
			client.openConnection();
			if (client.isConnected()) {
				testlbl.setText("connection succeeded");

			} else {
				System.out.println("Not connected to server.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setClient(ChatClient client, HDController controller) {
		this.client = client;
		this.client.setController(controller); // also set the controller on the client
	}

	@FXML
	public void ToLogOut(ActionEvent event) {
		Platform.runLater(() -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScreen.fxml"));
			Parent root;
			try {
				root = loader.load();
				Stage window = (Stage) getLogOutBTN().getScene().getWindow();
				window.setScene(new Scene(root));
				window.show();
				LogInInfo loginData = new LogInInfo(HOD.getUserName(), HOD.getPassword());
				try {
					client.openConnection();
					if (client.isConnected()) {
						client.sendToServer(new Request("LOGOUT", loginData));
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.out.println("Not connected to server.");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	@FXML
	public void UpdateRequestTime(ArrayList<RequestTime> requestList) {
		Platform.runLater(() -> {
			requestTable.getItems().clear();
			requestTable.getItems().addAll(requestList);
		});

	}

	@FXML
	public void ApproveRequestTime(ActionEvent event) {
		Request request = new Request("APPROVE", RequestExamIDText.getText());
		try {
			client.openConnection();
			if (client.isConnected()) {
				client.sendToServer(new Request("SearchExam", request));
				refreshTable(event);
				System.out.println("after approve refresh");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		refreshTable1();
		Platform.runLater(() -> {
			if (requestTable.getItems().size() == 1)
				requestTable.getItems().clear();
			refreshTable1();

		});

	}

	@FXML
	public void RejectRequestTime(ActionEvent event) {
		Request request = new Request("REJECT", RequestExamIDText.getText());
		try {
			client.openConnection();
			client.sendToServer(new Request("SearchExam", request));
			refreshTable(event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Platform.runLater(() -> {
			if (requestTable.getItems().size() == 1)
				requestTable.getItems().clear();
			refreshTable1();
			showpPopupReject();

		});
	}
	/*
	 * Show window with email\phone of the le cturer-the request rejected
	 */

	@FXML
	public void refreshTable(ActionEvent event) {

		refreshTable1();
	}

	public void refreshTable1() {

		try {

			client.openConnection();
			if (client.isConnected()) {
				client.sendToServer(new Request("GET-EXTRA-TIME-REQUEST", null));

			} else {
				System.out.println("Not connected to server.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showpPopupApprove(String username) {

		System.out.println("in popupApprove");
		String lblstring = "Request Approve! " + username + "@braude.ac.il";
		Platform.runLater(() -> {

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/approvePopup.fxml"));
				Parent popupContent = loader.load();
				ApprovePopupController popupController = loader.getController(); // Get the controller instance

				Popup popup = new Popup();
				popupController.setPopup(popup);
				popupController.setStr(lblstring);
				popupController.initialize(null, null);

				popup.getContent().add(popupContent);
				popup.show(ApproveBTN.getScene().getWindow());

			} catch (IOException e) {
				e.printStackTrace();
			}

			/*
			 * Stage popupStage = new Stage(); popupStage.setTitle("Pop-up Window");
			 * popupStage.setWidth(200); popupStage.setHeight(200);
			 * popupStage.initModality(Modality.APPLICATION_MODAL); VBox popupContent = new
			 * VBox(); popupContent.setAlignment(Pos.CENTER); Label messageLabel = new
			 * Label("Request Approve! " + username + "@braude.ac.il");
			 * messageLabel.setAlignment(Pos.TOP_CENTER); messageLabel.setPadding(new
			 * Insets(10)); Button closeButton = new Button("Close");
			 * closeButton.setAlignment(Pos.CENTER); closeButton.setOnAction(e ->
			 * popupStage.close()); popupContent.getChildren().addAll(messageLabel,
			 * closeButton); Scene popupScene = new Scene(popupContent);
			 * popupStage.setScene(popupScene); popupStage.show();
			 */
		});
	}

	public void showpPopupReject() {
		Platform.runLater(() -> {

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/rejectpopup.fxml"));
				RejectPopUpController popupController = new RejectPopUpController();

				loader.setController(popupController);

				Parent popupContent = loader.load();
				Popup popup = new Popup();
				popupController.setPopup(popup);
				popup.getContent().add(popupContent);

				popup.show(rejectBTN.getScene().getWindow());
			} catch (IOException e) {
				e.printStackTrace();
			}

			/*
			 * Stage popupStage = new Stage(); popupStage.setTitle("Pop-up Window");
			 * popupStage.setWidth(200); popupStage.setHeight(200);
			 * popupStage.initModality(Modality.APPLICATION_MODAL); VBox popupContent = new
			 * VBox(); popupContent.setAlignment(Pos.CENTER); Label messageLabel = new
			 * Label("Request Rejected!! " + username + "@braude.ac.il");
			 * messageLabel.setAlignment(Pos.TOP_CENTER); messageLabel.setPadding(new
			 * Insets(10)); Button closeButton = new Button("Close");
			 * closeButton.setAlignment(Pos.CENTER); closeButton.setOnAction(e ->
			 * popupStage.close()); popupContent.getChildren().addAll(messageLabel,
			 * closeButton); Scene popupScene = new Scene(popupContent);
			 * popupStage.setScene(popupScene); popupStage.show();
			 */
		});
	}

	public void ShowGradeStatistics(ActionEvent event) {

		try {
			client.openConnection();
			if (client.isConnected()) {
				client.sendToServer(new Request("SendID", LectuerID.getText()));
				client.sendToServer(new Request("SendID", LectuerID.getText()));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Not connected to server.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void ImportLectueGradeStatistics(ArrayList<Grades> msg) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		// Create data series
		for (Grades grade : msg) {
			int i = 0;
			series.getData().add(new XYChart.Data<>("", msg.get(i).getGrade()));
			i++;
		}
		LectuerName.setText(msg.get(0).getCourseName());// show course name
		GPA_LECtextArea.setText(String.valueOf(calcAVG(msg)));// show the avarge
		Median_LECTextArea.setText(String.valueOf(calcMedian(msg)));// shoe median
		// Add series to BarChart
		barChartLec.getData().add(series);

	}

	public void ImportStudentGradeStatistics(ArrayList<Grades> msg) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		for (Grades grade : msg) {
			int i = 0;
			series.getData().add(new XYChart.Data<>("", msg.get(i).getGrade()));
			i++;
		}
		StudentName.setText(msg.get(0).getCourseName());// show course name
		GPA_STUtextArea.setText(String.valueOf(calcAVG(msg)));// show the avarge
		median_STUtextArea.setText(String.valueOf(calcMedian(msg)));// shoe median
		// Add series to BarChart
		barChartStud.getData().add(series);

	}

	public void ImportCourseGradeStatistics(ArrayList<Grades> msg) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		for (Grades grade : msg) {
			int i = 0;

			series.getData().add(new XYChart.Data<>("exam" + i + 1, msg.get(i).getGrade()));
			i++;
		}
		CourseName.setText(msg.get(0).getCourseName());// show course name
		GPA_GradestextArea.setText(String.valueOf(calcAVG(msg)));// show the avarge
		median_GradesTextArea.setText(String.valueOf(calcMedian(msg)));// shoe median
		// Add series to BarChart
		barChartCourse.getData().add(series);

	}

	public double calcAVG(ArrayList<Grades> msg) {
		int sum = 0;
		for (Grades number : msg) {
			sum += number.getGrade();
		}
		double avg = sum / msg.size();
		return avg;
	}

	public double calcAVG1(ArrayList<String> msg) {
		int sum = 0;
		int temp;
		int avg=0;
		try {
		for (String number : msg) {
			temp = Integer.valueOf(number);
			sum = sum + temp;
		}
		avg = sum / msg.size();}
		catch (Exception e) {
			
		}
		return avg;
	}

	public double calcMedian(ArrayList<Grades> grades) {
		double median;
		int medianIndex = grades.size() / 2;

		if (grades.size() % 2 == 0) {
			int medianValue1 = grades.get(medianIndex - 1).getGrade();
			int medianValue2 = grades.get(medianIndex).getGrade();
			median = (medianValue1 + medianValue2) / 2.0;
		} else {
			median = grades.get(medianIndex).getGrade();
		}
		return median;

	}

	@FXML
	public void pressInfoRadioButton(ActionEvent event) {
		if (medianrb.isSelected()) {// if median radio button is selected we add to the array the word median to
									// search in the sql
			infoSelecttionArray.add("median");

		} else {// if not or unselectedd we remove it
			infoSelecttionArray.remove("median");
		}
		if (gparb.isSelected()) {
			infoSelecttionArray.add("GPA");
		} else {
			infoSelecttionArray.remove("GPA");
		}
		if (deistributionrb.isSelected()) {
			infoSelecttionArray.add("Distribution");
		} else {
			infoSelecttionArray.remove("Distribution");
		}
		checkinfolLabel.setText(infoSelecttionArray.toString());
	}

	@FXML
	public void pressfromRadioButtonLecturer(ActionEvent event) {
		tt11.setText("Lecturer");
		whoToShow = "lecturer";
		wholbl1.setText("enter Lecturer name:");

	}

	@FXML
	public void pressfromRadioButtonExam(ActionEvent event) {
		tt11.setText("Exam");
		whoToShow = "examID";
		wholbl1.setText("enter Exam ID:");

	}

	@FXML
	public void pressfromRadioButtonStudent(ActionEvent event) {
		tt11.setText("student");
		whoToShow = "Student";
		wholbl1.setText("enter student name:");
	}

	@FXML
	public void pressfromRadioButtonCourse(ActionEvent event) {
		tt11.setText("course");
		whoToShow = "course";
		wholbl1.setText("enter course name:");
	}

	@FXML
	public void pressShowResultButton(ActionEvent event) {
		Platform.runLater(() -> {
			String sqlQueryString = "SELECT";
			String currentStyle = selectionNameTxtField.getStyle();

			// check if any info was selected:
			if (gparb.isSelected() == false && medianrb.isSelected() == false
					&& deistributionrb.isSelected() == false) {
				errlbl.setStyle("-fx-text-fill: white;-fx-background-color: red;-fx-background-radius:10px;");
				errlbl.setText("please select atleast 1 value!");
				errlbl.setAlignment(Pos.CENTER);
				errlbl.setVisible(true);

			} else {
				errlbl.setVisible(false);

			}
			// check if any info was selected:
			if (lecturerrb.isSelected() == false && studentrb.isSelected() == false && courserb1.isSelected() == false
					&& examrb.isSelected() == false) {
				checkedWholbl.setStyle("-fx-text-fill: white;-fx-background-color: red;-fx-background-radius:10px;");
				checkedWholbl.setText("please select atleast 1 value!");
				checkedWholbl.setAlignment(Pos.CENTER);
				checkedWholbl.setVisible(true);

			} else {
				checkedWholbl.setVisible(false);

			}
			if (selectionNameTxtField.getText().isBlank()) {
				selectionNameTxtField.setStyle("-fx-border-width:2px;-fx-border-color:red;");
				selectionNameTxtField.setPromptText("please enter a value!");
			} else {
				selectionNameTxtField.setStyle("-fx-border-width:0px;-fx-border-color:white");
			}

			for (String info : infoSelecttionArray) {
				sqlQueryString = sqlQueryString + " " + info + " ,";
			}
			sqlQueryString = sqlQueryString.substring(0, sqlQueryString.length() - 1);
			sqlQueryString = sqlQueryString + "where " + whoToShow + " = " + selectionNameTxtField.getText();

			tt22.setText(sqlQueryString);
			// end of error handeling

			// start of logic

			/**
			 * i want to get the query and send it to the server and get in return a grades
			 * array
			 * 
			 * 
			 * 
			 * 
			 */

			/// getting exam sql query for student
			/*
			 * if (lecturerrb.isSelected()) {
			 * 
			 * 
			 * 
			 * 
			 * 
			 * try { client.openConnection(); if (client.isConnected()) {
			 * client.sendToServer(new Request("SearchExam", request));
			 * 
			 * } } catch (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 * 
			 * }
			 */
			if (studentrb.isSelected()) {
				String studentid = selectionNameTxtField.getText();// student id
				String querytosend = "SELECT grade FROM cems.studentsData where sID=" + studentid + "";

				try {
					client.openConnection();
					if (client.isConnected()) {
						client.sendToServer(new Request("SEARCH-STUDENT-BY-ID", studentid));

					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				tt11.setText(generalGradesArray.toString());
				if (medianrb.isSelected()) {
					double median = calcAVG1(generalGradesArray);
					String medianstr = String.valueOf(median);
					medianTxtField.setText(medianstr);

				}
				if (deistributionrb.isSelected()) {

				}
			}

			/*
			 * if (courserb1.isSelected()) {
			 * 
			 * try { client.openConnection(); if (client.isConnected()) {
			 * client.sendToServer(new Request("SearchExam", ));
			 * 
			 * 
			 * } } catch (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 * 
			 * } if (examrb.isSelected()) {
			 * 
			 * String examid=selectionNameTxtField.getText();//exam id
			 * 
			 * try { client.openConnection(); if (client.isConnected()) {
			 * client.sendToServer(new Request("SearchExam", ));
			 * 
			 * 
			 * } } catch (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 * 
			 * }
			 */});

	}

	@FXML
	private void handleAddDataButton() {
		// Create data series
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Series");

		// Add data points to series
		series.getData().add(new XYChart.Data<>("Category 1", 10));
		series.getData().add(new XYChart.Data<>("Category 2", 30));
		series.getData().add(new XYChart.Data<>("Category 3", 30));
		series.getData().add(new XYChart.Data<>("Category 4", 40));
		series.getData().add(new XYChart.Data<>("Category 5", 50));
		series.getData().add(new XYChart.Data<>("Category 6", 60));

		// Add series to the bar chart
		ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();
		data.add(series);
		generalScreenBarChart.setData(data);
	}
}
