package gui;

import java.io.IOException;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.LogInInfo;
import logic.Request;
import logic.Users;

public class LoginScreenController {
	private ChatClient client;
	private Users user;
	public void setUser(Users user) {
		this.user=user;
	}
	@FXML
	private TextField userName;
	@FXML
	private TextField password;
	@FXML
	private Button loginBtn;
	@FXML
	private Label loginStatus;
	@FXML
	private ImageView image;
	@FXML
	private Pane pane;

	public void setClient(ChatClient client, LoginScreenController controller) {
		this.client = client;
		this.client.setController(controller); // also set the controller on the client
	}

	@FXML
	public void sendLoginDataToServer(ActionEvent event) {
		LogInInfo loginData = new LogInInfo(userName.getText(), password.getText());
		try {
			client.openConnection();
			if (client.isConnected()) {
				client.sendToServer(new Request("LOGIN", loginData));
			} else {
				System.out.println("Not connected to server.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ShowUserWelcomeScreen(Users user) throws IOException {
		Platform.runLater(() -> {
			String Studentpath = "/gui/StudentPage.fxml";
			String Lacturertpath = "/gui/FirstPage.fxml";
			String HDpath = "/gui/HDPage.fxml";

			if (user == null) {
				// show error text

				loginStatus.setText("Username or password is incorrect, please try again!");
			} else {
				FXMLLoader loader = null;
				loginStatus.setText("");

				if (user.getRole() == 0) {
					loader = new FXMLLoader(getClass().getResource(Studentpath));
				}
				if (user.getRole() == 1) {
					loader = new FXMLLoader(getClass().getResource(Lacturertpath));
				}
				if (user.getRole() == 2) {
					loader = new FXMLLoader(getClass().getResource(HDpath));
				}

				Parent root;
				try {
					root = loader.load();
					Stage window = (Stage) getLoginBtn().getScene().getWindow();
					FirstPageController lecturerController=null;
					StudentPageController studentController = null;
					if (user.getRole() == 1) {
						lecturerController = loader.getController();
						lecturerController.setLecturerAndClient(user, client);
					}
					if (user.getRole() == 0) {
						String welcomeMsg= "Welcome" +" "+ user.getFirstName() + " " + user.getLastName();
						studentController = loader.getController();
						studentController.setStudentAndClient(user, client);
						studentController.setWelcomeLabel(welcomeMsg);
					}
					window.setScene(new Scene(root));
					window.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public LoginScreenController getController() {
		return this;
	}

	public ChatClient getClient() {
		return client;
	}

	public TextField getUserName() {
		return userName;
	}

	public TextField getPassword() {
		return password;
	}

	public Button getLoginBtn() {
		return loginBtn;
	}

	public Label getLoginStatus() {
		return loginStatus;
	}

}
