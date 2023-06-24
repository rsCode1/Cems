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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import junit.framework.Assert;

class EchoServerTest {
	LogInInfo loginStubSuccess;
	LogInInfo loginStubFailNull,loginStubFailNotInDB;
	EchoServer echoserver;
	ServerCommandsLecturer SCL;
	ChatClient client;
	Users successUser, failUser;
	Exam exam,exam2,exam3,exam4;
	ObservableList<Question> questions,noQuestions;
	private Users lecturer,lecturer2;
	Question question1,question2;
	 @Mock
	 private Connection connMock;
	ConnectionToClient connectionMock;
	
	
	ArrayList<Users> loggedUsers;

@BeforeEach
void setUp() throws Exception {
    question1 = new Question(2, "What is the capital of France?", "Paris", "London", "Berlin", "Madrid", 1, 2, 2, "Jane Smith");
    question2 = new Question(124, "2+2=?", "1", "2", "3", "4", 4, 1, 496351, "dnb");
    echoserver = new EchoServer(5555);
    lecturer = new Users(496351, "Dina", "Barzilai", "dnb", "111", 0, 1);
    lecturer2 = new Users(2, "Jane", "Smith", "jsm", "456", 0, 1);
    noQuestions= FXCollections.observableArrayList();
    // Initialize the questions list
    questions = FXCollections.observableArrayList();
    questions.add(question1);
    SCL = new ServerCommandsLecturer();
    loggedUsers = new ArrayList<>();
    exam = new Exam("Calculus1", questions, lecturer2, 100, "Calculus1");
	exam2 = new Exam("Calculus1", noQuestions, lecturer2, 100, "Calculus1");
	exam3 = new Exam("Calculus1", questions, lecturer2, 0, "Calculus1");

    loginStubFailNull = new LogInInfo(null, null);
    loginStubSuccess= new LogInInfo(null, null);
	loginStubSuccess.setUserName("jsm");
    loginStubFailNotInDB = new LogInInfo("not_username", "worng_password");
    connectionMock=Mockito.mock(ConnectionToClient.class);
    loginStubSuccess.setPassword("456");
    loginStubFailNotInDB.setUserName("test");
    loginStubFailNotInDB.setPassword("111");
}

	//test login of username and passwords which are not in DB
	//input loginStubFailNotInDB("not_username", "worng_password"),
	//expected : user is not found in DB and not logged in. 
	@Test
	void loginTest_username_and_password_not_in_DB() {
		Connection conn = connect2DB();
		boolean isLogged = false;
		try {
			isLogged = checkIfUserLoggedIn(loginStubFailNotInDB, conn);
			assertFalse(isLogged);
			echoserver.checkUserLoginNoClient(loginStubFailNotInDB);
			isLogged = checkIfUserLoggedIn(loginStubFailNotInDB, conn);
			assertFalse(isLogged);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}

		
	}



	//test login of username and passwords which are not in DB
	//input loginStubSuccess("jsm", "456"),
	//expected : user is  found in DB and is able to log in. 
	@Test
	void loginTest_correct_logininfo()
		throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Connection conn = connect2DB();
		boolean isLogged = checkIfUserLoggedIn(loginStubSuccess, conn);// check initial state for the isLogged field in
																		// db

		assertFalse(isLogged);// assert that the user isnt logged in yet
		echoserver.checkUserLoginNoClient(loginStubSuccess);
		isLogged = checkIfUserLoggedIn(loginStubSuccess, conn);// check that the user is logged in,we expect to get true
																// because the user is now logged

		assertTrue(isLogged);// assert value of isLogged

	}

	//test login of username and passwords which are not in DB,only username is in DB
	//input loginStubFailNotInDB("jsm", "worng_password"),
	//expected : user is not found in DB and not logged in. 
	@Test
	void loginTest_correct_username_and_incorrect_password() {

		Connection conn = connect2DB();// check th
		boolean isLogged = false;
		loginStubFailNotInDB.setUserName("jsm");
		try {
			isLogged = checkIfUserLoggedIn(loginStubFailNotInDB, conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(isLogged);
		echoserver.checkUserLoginNoClient(loginStubFailNotInDB);
		try {

			isLogged = checkIfUserLoggedIn(loginStubFailNotInDB, conn);
		} catch (SQLException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(isLogged);

	}


	//test login of username and passwords which are not in DB,only password is in DB
	//input loginStubFailNotInDB("wrong username", "456"),
	//expected : user is not found in DB and not logged in. 
	@Test
	void loginTest_incorrect_username_and_correct_password() {

		Connection conn = connect2DB();// check th
		boolean isLogged = false;
		loginStubFailNotInDB.setPassword("456");
		try {
			isLogged = checkIfUserLoggedIn(loginStubFailNotInDB, conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(isLogged);
		echoserver.checkUserLoginNoClient(loginStubFailNotInDB);
		try {

			isLogged = checkIfUserLoggedIn(loginStubFailNotInDB, conn);
		} catch (SQLException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(isLogged);

	}

	//methode to handle connections
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
		
		String command = String.format("SELECT * FROM cems.users WHERE userName= '%s' AND password = '%s'",loginInfo.getUserName(),loginInfo.getPassword());
		Statement stmt = conn.createStatement();
		//String command = "SELECT isLogged FROM users WHERE userName="+ loginInfo.getUserName()+" AND password="+ loginInfo.getPassword();
		ResultSet rs = stmt.executeQuery(command);
		while (rs.next()) {
			// get fields from resultSet
			isLogged = rs.getInt("isLogged");

		}
		return (isLogged == 1) ? true : false;
	}
		public boolean checkIfExamIsExist( Exam exam,Connection conn) throws SQLException {
		boolean isExist = false;
		
		Integer examID=exam.getExamId();
		String command = String.format("SELECT * FROM cems.exams WHERE exam_id= '%d'",exam.getExamId());
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery(command);
		while (rs.next()) {
			// get fields from resultSet
			 examID = rs.getInt("exam_id");
			
				
		}
			
		return (examID==exam.getExamId()) ? false : true;

	}


    
	public boolean CheckIfExamInserted( Exam exam,Connection conn) throws SQLException {
		boolean isExist = false;
		String command2 = String.format("SELECT * FROM exams WHERE exam_id = (SELECT MAX(exam_id) FROM exams)");
		Statement stmt2 = conn.createStatement();
		ResultSet rs2 = stmt2.executeQuery(command2);
        Integer examID=null;
        while (rs2.next()) {
			// get fields from resultSet
			 examID = rs2.getInt("exam_id");
			
				
		}

		
		return (examID==exam.getExamId()) ? false : true;

	}

	//test creat exam ,check if can successfuly add it the DB a valid Exam
	//input Exam("Calculus1", questions, lecturer2, 100, "Calculus1");
	//expected : Exam is successfuly added
@Test
void CreateExamTest_Succses() throws SQLException, InterruptedException {
  

    Connection conn = connect2DB();
    boolean isExist;

    try {
        isExist = checkIfExamIsExist(exam, conn);
        assertFalse(isExist);

        SCL.saveExam(exam, connectionMock);
        isExist = CheckIfExamInserted(exam, conn);
        assertTrue(isExist);

    } catch (SQLException | IllegalArgumentException e) {
        e.printStackTrace();
        fail("Exception thrown during test: " + e.getMessage());
	}
    }


	//test creat exam ,check if wrong exam with no questions is added
	//input Exam("Calculus1",noQuestions(empty array), lecturer2, 100, "Calculus1");
	//expected : Exam is not successfuly added,due to not having question
@Test
void CreateExamTest_FailNoQuestions() throws SQLException, InterruptedException {
  

    Connection conn = connect2DB();
    boolean isExist;

    try {
        isExist = checkIfExamIsExist(exam2, conn);
        assertFalse(isExist);

        SCL.saveExam(exam2, connectionMock);
        isExist = CheckIfExamInserted(exam2, conn);
        assertTrue(isExist);

    } catch (Exception e) {
        assertEquals("Please add questions to the exam",e.getMessage());
		e.printStackTrace();
	}
    }

     //test creat exam ,check if exam without timer can be added
	//input Exam("Calculus1", ObservableList<Question> questions,, lecturer2, 0, "Calculus1");
	//expected : Exam is not successfuly added,due to not having time to complete
@Test
void CreateExamTest_FailNoTimer() throws SQLException, InterruptedException {
  

    Connection conn = connect2DB();
    boolean isExist;

    try {
        isExist = checkIfExamIsExist(exam3, conn);
        assertFalse(isExist);

        SCL.saveExam(exam3, connectionMock);
        isExist = CheckIfExamInserted(exam3, conn);
        assertTrue(isExist);

    } catch (Exception e) {
        assertEquals("Please enter a valid time", e.getMessage());
		e.printStackTrace();
       
    }




}



		

	}


