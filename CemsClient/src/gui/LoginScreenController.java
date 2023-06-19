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
import javafx.stage.Screen;
import javafx.stage.Stage;
import logic.LogInInfo;
import logic.Request;
import logic.Users;

public class LoginScreenController {
	private ChatClient client;

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

	/**
	 * This function shows the welcome screen for a user based on their role and loads the appropriate
	 * FXML file.
	 * 
	 * @param user The user object represents the user who has just logged in and is being welcomed to the
	 * system. It contains information about the user such as their role, first name, and last name.
	 */
	public void ShowUserWelcomeScreen(Users user) throws IOException {
		Platform.runLater(() -> {
			String Studentpath = "/gui/StudentPage.fxml";
			String Lacturertpath = "/gui/LecturerPage.fxml";
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
					StudentPageController studentController = null;
					HDController hdccontroller = null;
					if (user.getRole() == 1) {
						// call lecturer conrtoller and use method
						LecturerPageController controller = loader.getController();
						controller.setLecturerAndClient(user, client);
						client.setController(controller);
						controller.getOngoingExamsTable();

					}
					if (user.getRole() == 0) {
						String welcomeMsg = "Welcome" + " " + user.getFirstName() + " " + user.getLastName();
						studentController = loader.getController();
						studentController.setStudentAndClient(user, client);
						studentController.setWelcomeLabel(welcomeMsg);
						client.setController(studentController);
					}
					if (user.getRole() == 2) {
						hdccontroller = loader.getController();
						hdccontroller.SetHeadOfDepartment(user, client);
						hdccontroller.refreshTable1();
						client.setController(hdccontroller);

					}
					window.setScene(new Scene(root));
					// Center the stage on the screen
					double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
					double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
					double stageWidth = window.getWidth();
					double stageHeight = window.getHeight();
					window.setX((screenWidth - stageWidth) / 2);
					window.setY((screenHeight - stageHeight) / 2);
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
