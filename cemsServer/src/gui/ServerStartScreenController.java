package gui;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import logic.LoggedUsers;
import server.EchoServer;

public class ServerStartScreenController implements Initializable {

	private EchoServer server;

	@FXML
	private Button startBtn;
	@FXML
	private Button exitBtn;
	@FXML
	private TableColumn<LoggedUsers, Integer> id;
	@FXML
	private TableColumn<LoggedUsers, String> firstName;
	@FXML
	private TableColumn<LoggedUsers, String> lastName;
	@FXML
	private TableColumn<LoggedUsers, String> userName;
	@FXML
	private TableColumn<LoggedUsers, Integer> role;
	@FXML
	private TableView<LoggedUsers> table;

	@FXML
	private TextField iPAddress;
	@FXML
	private TextField dbUserNameField;
	@FXML
	private TextField portField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextField textField;
	@FXML
	private CheckBox showPasswordCheckBox;
	@FXML
	private Circle colorCircle;
	 @FXML
    private Text passwordText;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
		role.setCellValueFactory(new PropertyValueFactory<>("role"));
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			iPAddress.setText(localHost.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		passwordField.textProperty().bindBidirectional(passwordText.textProperty());

		showPasswordCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				passwordField.setVisible(false);
				passwordText.setVisible(true);
			} else {
				passwordField.setVisible(true);
				passwordText.setVisible(false);
			}
		});

	}

	public void UpadteOnlineUsers(ArrayList<LoggedUsers> usersArray) {
		table.getItems().clear();
		table.getItems().addAll(usersArray);

	}

	@FXML
	public void startServer() {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST",
					dbUserNameField.getText(),
					passwordField.getText());
			if (server != null && conn != null) {
				server.listen(); // Start listening for connections
				colorCircle.setFill(Color.web("#21ff25"));;
			} else {
				System.out.println("ERROR - Server is not initialized!");
				 colorCircle.setFill(Color.RED);
			}
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			 colorCircle.setFill(Color.RED);
		}
	}

	public void setServer(EchoServer server) {
		this.server = server;
	}

	@FXML
	public void exitApplication() {
		System.exit(0);
	}

}
