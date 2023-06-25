package TestCases;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.internal.runners.statements.Fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatcher.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logic.Exam;
import logic.LogInInfo;
import logic.Question;
import logic.Response;
import logic.Users;
import ocsf.server.ConnectionToClient;
import server.EchoServer;
import server.IEchoServer;
import server.ServerCommandsLecturer;
import client.ChatClient;
import gui.LoginScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import junit.framework.Assert;

class CreatExamTest {
	EchoServer echoserver;
	ServerCommandsLecturer SCL;
	ChatClient client;
	Exam exam, exam2, exam3, exam4;
	ObservableList<Question> questions, noQuestions;
	private Users lecturer, lecturer2;
	Question question1, question2;
	private Connection connMock;
	ConnectionToClient connectionMock;

	ArrayList<Users> loggedUsers;

	@BeforeEach
	void setUp() throws Exception {
		question1 = new Question(2, "What is the capital of France?", "Paris", "London", "Berlin", "Madrid", 1, 2, 2,
				"Jane Smith");
		question2 = new Question(124, "2+2=?", "1", "2", "3", "4", 4, 1, 496351, "dnb");
		echoserver = new EchoServer(5555);
		lecturer = new Users(496351, "Dina", "Barzilai", "dnb", "111", 0, 1);
		lecturer2 = new Users(2, "Jane", "Smith", "jsm", "456", 0, 1);
		noQuestions = FXCollections.observableArrayList();
		// Initialize the questions list
		questions = FXCollections.observableArrayList();
		questions.add(question1);
		SCL = new ServerCommandsLecturer();
		loggedUsers = new ArrayList<>();
		exam = new Exam("Calculus1", questions, lecturer2, 100, "Calculus1");
		exam.setExamId(99);
		exam2 = new Exam("Calculus1", noQuestions, lecturer2, 100, "Calculus1");
		exam2.setExamId(999);
		exam3 = new Exam("Calculus1", questions, lecturer2, 0, "Calculus1");
		exam3.setExamId(9999);
		connectionMock = Mockito.mock(ConnectionToClient.class);

	}




	// test creat exam ,check if can successfuly add it the DB a valid Exam
	// input Exam("Calculus1", questions, lecturer2, 100, "Calculus1");
	// expected : Exam is successfuly added
	@Test
	void CreateExamTest_Succses() throws SQLException, InterruptedException {

		Connection conn = connect2DB();
		boolean isExist;

		try {
			isExist = checkIfExamIsExist(exam, conn);
			assertFalse(isExist);

			SCL.saveExam(exam, connectionMock);
			String command2 = String.format("SELECT * FROM exams WHERE exam_id = (SELECT MAX(exam_id) FROM exams)");
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(command2);
			int examID = 0;
			while (rs2.next()) {
				// get fields from resultSet
				examID = rs2.getInt("exam_id");

			}
			exam.setExamId(examID);
			isExist = CheckIfExamInserted(exam, conn);
			assertTrue(isExist);

		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
			fail("Exception thrown during test: " + e.getMessage());
		}
	}

	// test creat exam ,check if wrong exam with no questions is added
	// input Exam("Calculus1",noQuestions(empty array), lecturer2, 100,
	// "Calculus1");
	// expected : Exam is not successfuly added,due to not having question
	@Test
	void CreateExamTest_FailNoQuestions() throws SQLException, InterruptedException {

		Connection conn = connect2DB();
		boolean isExist;

		try {
			isExist = checkIfExamIsExist(exam2, conn);
			assertFalse(isExist);

			SCL.saveExam(exam2, connectionMock);
			isExist = CheckIfExamInserted(exam2, conn);
			System.out.println("the exam is " + isExist);
			assertFalse(isExist);

		} catch (Exception e) {
			assertEquals("Please add questions to the exam", e.getMessage());
			e.printStackTrace();
		}
	}

	// test creat exam ,check if exam without timer can be added
	// input Exam("Calculus1", ObservableList<Question> questions,, lecturer2, 0,
	// "Calculus1");
	// expected : Exam is not successfuly added,due to not having time to complete
	@Test
	void CreateExamTest_FailNoTimer() throws SQLException, InterruptedException {

		Connection conn = connect2DB();
		boolean isExist = false;

		try {
			isExist = checkIfExamIsExist(exam3, conn);
			assertFalse(isExist);

			SCL.saveExam(exam3, connectionMock);
			isExist = CheckIfExamInserted(exam3, conn);
			assertFalse(isExist);

		} catch (Exception e) {
			assertEquals("Please enter a valid time", e.getMessage());
			e.printStackTrace();

		}

	}

	// methode to handle connections
	public Connection connect2DB() {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * check if the user is connected
	 * we assume that the user is not connected
	 */
	public boolean checkIfUserLoggedIn(LogInInfo loginInfo, Connection conn) throws SQLException {
		int isLogged = 0;

		String command = String.format("SELECT * FROM cems.users WHERE userName= '%s' AND password = '%s'",
				loginInfo.getUserName(), loginInfo.getPassword());
		Statement stmt = conn.createStatement();
		// String command = "SELECT isLogged FROM users WHERE userName="+
		// loginInfo.getUserName()+" AND password="+ loginInfo.getPassword();
		ResultSet rs = stmt.executeQuery(command);
		while (rs.next()) {
			// get fields from resultSet
			isLogged = rs.getInt("isLogged");

		}
		return (isLogged == 1) ? true : false;
	}

	public boolean checkIfExamIsExist(Exam exam, Connection conn) throws SQLException {
		boolean isExist = false;

		Integer examID = 0;
		String command = String.format("SELECT * FROM cems.exams WHERE exam_id= '%d'", exam.getExamId());
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery(command);
		while (rs.next()) {
			// get fields from resultSet
			examID = rs.getInt("exam_id");

		}

		return (examID == exam.getExamId()) ? true : false;

	}

	public boolean CheckIfExamInserted(Exam exam, Connection conn) {
		boolean isExist = false;
		Integer examID = null;
		String command2 = String.format("SELECT * FROM exams WHERE exam_id = (SELECT MAX(exam_id) FROM exams)");
		try {
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(command2);
			while (rs2.next()) {
				// get fields from resultSet
				examID = rs2.getInt("exam_id");

			}
		} catch (Exception e1) {

		}

		return (examID == exam.getExamId()) ? true : false;

	}

}
