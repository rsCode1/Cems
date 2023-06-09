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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Exam;
import logic.LogInInfo;
import logic.Request;
import logic.RequestTime;
import logic.Users;
import ocsf.client.*;

public class HDController implements Initializable {
	private ChatClient client;
	private RequestTime requestTime;
	private Users HOD;

	public void SetHeadOfDepartment(Users Hod, ChatClient client) {
		this.HOD = Hod;
		this.client = client;
	}

	@FXML
	private ResourceBundle resources;
	@FXML
	private Label testlbl;

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
	private Button ShowStatisticsBTN;

	@FXML
	private TextField GPA_LECtextArea;

	@FXML
	private TextField LectuerID;
	@FXML
	private TextField Median_LECTextArea;

	@FXML
	private Button ShoeBTN;

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
	private TableColumn<RequestTime, String> IDColumn;
	@FXML
	private TableColumn<RequestTime, String> ExamIdColumn;
	@FXML
	private TableColumn<RequestTime, String> RequestedByColumn;
	@FXML
	private TableColumn<RequestTime, Integer> ExtraTimeColumn;
	@FXML
	private TableColumn<RequestTime, String> ReasonColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
	}

	@FXML
	public void RejectRequestTime(ActionEvent event) {
		Request request = new Request("REJECT", RequestExamIDText.getText());
		try {
			client.openConnection();
			client.sendToServer(new Request("SearchExam", request));
			refreshTable1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Platform.runLater(() -> {
			// popup massage
			Stage popupStage = new Stage();
			popupStage.setTitle("Pop-up Window");
			popupStage.setWidth(400);
			popupStage.setHeight(200);
			popupStage.initModality(Modality.APPLICATION_MODAL);
			VBox popupContent = new VBox();
			Label messageLabel = new Label("Request denied!");
			Button closeButton = new Button("Close");
			closeButton.setOnAction(e -> popupStage.close());
			popupContent.getChildren().addAll(messageLabel, closeButton);
			Scene popupScene = new Scene(popupContent);
			popupStage.setScene(popupScene);
			popupStage.show();

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
		Platform.runLater(() -> {
			Stage popupStage = new Stage();
			popupStage.setTitle("Pop-up Window");
			popupStage.setWidth(200);
			popupStage.setHeight(200);
			popupStage.initModality(Modality.APPLICATION_MODAL);
			VBox popupContent = new VBox();
			popupContent.setAlignment(Pos.CENTER);
			Label messageLabel = new Label("Request Approve! " + username + "@braude.ac.il");
			messageLabel.setAlignment(Pos.TOP_CENTER);
			messageLabel.setPadding(new Insets(10));
			Button closeButton = new Button("Close");
			closeButton.setAlignment(Pos.CENTER);
			closeButton.setOnAction(e -> popupStage.close());
			popupContent.getChildren().addAll(messageLabel, closeButton);
			Scene popupScene = new Scene(popupContent);
			popupStage.setScene(popupScene);
			popupStage.show();
		});
	}

	public void showpPopupReject(String username) {
		Platform.runLater(() -> {
			Stage popupStage = new Stage();
			popupStage.setTitle("Pop-up Window");
			popupStage.setWidth(200);
			popupStage.setHeight(200);
			popupStage.initModality(Modality.APPLICATION_MODAL);
			VBox popupContent = new VBox();
			popupContent.setAlignment(Pos.CENTER);
			Label messageLabel = new Label("Request Rejected!! " + username + "@braude.ac.il");
			messageLabel.setAlignment(Pos.TOP_CENTER);
			messageLabel.setPadding(new Insets(10));
			Button closeButton = new Button("Close");
			closeButton.setAlignment(Pos.CENTER);
			closeButton.setOnAction(e -> popupStage.close());
			popupContent.getChildren().addAll(messageLabel, closeButton);
			Scene popupScene = new Scene(popupContent);
			popupStage.setScene(popupScene);
			popupStage.show();
		});
	}
	public void ImportLectuerGradeStatistics(){
		try {
			//send lecturer ID to the sever
			client.sendToServer(new Request("SendLectuerID", LectuerID.getText()));			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	

}