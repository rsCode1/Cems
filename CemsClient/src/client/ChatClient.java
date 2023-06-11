// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import client.*;
import gui.ConnectToServerScreenController;
import gui.LoginScreenController;
import gui.ReviewExamController;
import gui.writeQuestionController;
import gui.createExamController;
import gui.TimeRequestController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.LogInInfo;
import logic.Response;
import logic.Users;
import logic.Question;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import ocsf.client.*;

public class ChatClient extends AbstractClient

{
	private LoginScreenController loginScreecontroller;
	private writeQuestionController writeQuestionController;
	private createExamController createExamController;
	private ReviewExamController reviewExamController;
	private TimeRequestController TimeRequestController;

	private String ip = "";
	private int portServer;
	// Instance variables **********************************************

	public ChatClient(String host, int port, Object clientUI) throws IOException {
		super(host, port);
		ip = host;
		this.portServer = port;
	}
	// this is new line
	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {

		if (msg instanceof Response) {
			Response response = (Response) msg;
			switch (response.getResponseType()) {
				case "LOGIN":
					login((Users) response.getResponseParam());
					break;
				case "Subjects":
					ArrayList<String> subjectsArr = (ArrayList<String>) response.getResponseParam();
					if (writeQuestionController != null)
						writeQuestionController.updateSubjectsComboBox(subjectsArr);
					if (createExamController != null)
						createExamController.updateSubjectsComboBox(subjectsArr);

					System.out.println("hello");
					break;

				case "Courses":
					ArrayList<String> coursesArr = (ArrayList<String>) response.getResponseParam();
					if (writeQuestionController != null)
						writeQuestionController.updateCoursesComboBox(coursesArr);
					if (createExamController != null)
						createExamController.updateCoursesComboBox(coursesArr);
					System.out.println("hello2");
					break;

				case "QuestionsArray":
					ArrayList<Question> questionsArr = (ArrayList<Question>) response.getResponseParam();
					createExamController.upadteQuestionViewTable(questionsArr);
					System.out.println("hello3");
					break;

				case "ExamSaved":
					reviewExamController.setLabel();
					break;
			}

		}

	}

	private void login(Users user) {

		System.out.println(user);
		try {
			loginScreecontroller.ShowUserWelcomeScreen(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(user.getRole());

	}

	public void setController(LoginScreenController controller) {
		this.loginScreecontroller = controller;
	}

	public void setController(writeQuestionController controller) {
		this.writeQuestionController = controller;
	}

	public void setController(createExamController controller) {
		this.createExamController = controller;
	}

	public void setController(ReviewExamController controller) {
		this.reviewExamController = controller;
	}
	public void setController(TimeRequestController controller) {
		this.TimeRequestController = controller;
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {

		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	public String getIp() {
		return ip;
	}

	public int getPortServer() {
		return portServer;
	}
}
// End of ChatClient class
