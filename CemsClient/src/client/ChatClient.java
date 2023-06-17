// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import logic.Response;
import logic.StudentData;
import client.*;
import gui.ConnectToServerScreenController;
import gui.LecturerApprovalController;
import gui.LecturerPageController;
import gui.InExamController;
import gui.LoginScreenController;
import gui.ReviewExamController;
import gui.StartExamController;
import gui.writeQuestionController;
import gui.createExamController;
import gui.TimeRequestController;
import gui.StudentHistoryController;
import gui.StudentManualTestController;
import gui.TakeExamController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Exam;
import logic.LogInInfo;
import logic.Response;
import logic.AddedTime;
import logic.DownloadManualExaminController;
import logic.MyFile;
import logic.Test;
import logic.Users;
import logic.Question;
import java.io.*;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import ocsf.client.*;

public class ChatClient extends AbstractClient

{
	private StudentManualTestController stdManController;
	private LoginScreenController loginScreecontroller;
	private writeQuestionController writeQuestionController;
	private createExamController createExamController;
	private ReviewExamController reviewExamController;
	private TimeRequestController TimeRequestController;
	private StartExamController startExamController;
	private LecturerPageController lecturerPageController;
	private LecturerApprovalController lecturerApprovalController;
	private TakeExamController takeExamController;
	private InExamController inExamController;
	private StudentHistoryController studentHistoryController;
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
				case "getExamsByLecturer":
					ArrayList<Exam> examsArr = (ArrayList<Exam>) response.getResponseParam();
					startExamController.setExamsTable(examsArr);
					startExamController.donothing(examsArr);
					break;
				case "getOngoingExams":
					ArrayList<Exam> ongoingExamsArr = (ArrayList<Exam>) response.getResponseParam();
					lecturerPageController.setOngoingExamsTable(ongoingExamsArr);
					break;
				case "startExamSuccess":
					startExamController.startExamSuccess();
					break;
				case "startExamFailed":
					startExamController.startExamFailed();
					break;
				case "getGrades":
					ArrayList<Exam> gradesArr = (ArrayList<Exam>) response.getResponseParam();
					lecturerApprovalController.setGradesTable(gradesArr);
					break;
				case "approveGradeSuccess":
					lecturerApprovalController.approveGradeSuccess();
					break;
				case "changeGradeSuccess":
					lecturerApprovalController.changeGradeSuccess();
					break;
				case "GetExam":
					getExam((Test) response.getResponseParam());
					break;
				case "AddedTime":
					AddedTime added = (AddedTime) response.getResponseParam();
					inExamController.setAdded(added);
					break;
				case "GetStudentGrades":
					ArrayList<StudentData> data = (ArrayList<StudentData>) response.getResponseParam();
					sendStudentGrades(data);
					break;
				case "DownloadManualExam":
					MyFile file = (MyFile) response.getResponseParam();
					stdManController.setDownloadFile(file);
					break;
				case "closeExam":
					if (inExamController != null) {
						inExamController.LockExam((int) response.getResponseParam());
					}
					if (stdManController != null) {
						stdManController.LockExam((int) response.getResponseParam());
					}
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

	public void setController(LecturerApprovalController controller) {
		this.lecturerApprovalController = controller;
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

	public void setController(StartExamController controller) {
		this.startExamController = controller;
	}

	public void setController(LecturerPageController controller) {
		this.lecturerPageController = controller;
	}

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */

	private void sendStudentGrades(ArrayList<StudentData> data) {
		studentHistoryController.setStudentDataList(data);
	}

	private void getExam(Test test) {
		try {
			takeExamController.ShowStudentEnterIdScreen(test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setStudentManualTestController(StudentManualTestController controller) {
		this.stdManController = controller;
	}

	public void setTakeExamController(TakeExamController controller) {
		this.takeExamController = controller;
	}

	public void setInExamController(InExamController controller) {
		this.inExamController = controller;
	}

	public void setStudentHistoryController(StudentHistoryController controller) {
		this.studentHistoryController = controller;
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
// End of ChatClient class