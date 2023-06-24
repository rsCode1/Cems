package TestCases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




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
import logic.Users;
import ocsf.server.ConnectionToClient;
import server.EchoServer;
import server.IEchoServer;
import client.ChatClient;
import javafx.collections.ObservableList;

class EchoServerTest {
	LogInInfo loginStubSuccess;
	LogInInfo loginStubFail;
	EchoServer echoserver;
	ChatClient client;
	Users successUser, failUser;
	Exam exam;
	ObservableList<Question> questions;
	Users lecturer;
	Question question1,question2;
	 @Mock
	 private Connection connMock;
	ConnectionToClient connectionMock=Mockito.mock(ConnectionToClient.class);
	
	ArrayList<Users> loggedUsers;

	@BeforeEach
	void setUp() throws Exception {
		question1= new Question(123,"1+1=?","1","2","3","4",2,1,496351,"dnb");
			question2= new Question(124,"2+2=?","1","2","3","4",4,1,496351,"dnb");
		echoserver = new EchoServer(55555);
		lecturer = new Users(496351, "Dina", "Barzilai", "dnb", "111", 0, 1);
		questions.add(question1);
		questions.add(question2);
		// client=new ChatClient(null, 0, client);
		loggedUsers = new ArrayList<Users>();
		exam= new Exam("Calculus1",questions,lecturer,100,"Calculus1");

		loginStubSuccess = new LogInInfo(null, null);
		loginStubFail= new LogInInfo(null, null);
		loginStubSuccess.setUserName("jsm");
		loginStubFail = new LogInInfo("not_username", "worng_password");

		loginStubSuccess.setPassword("456");
		loginStubFail.setUserName("test");
		loginStubFail.setPassword("111");

	}

	private class LoginStub {

		private String usernamestub;
		private String passwordStub;
		private boolean isLogged;

		public LoginStub(String usernamestub, String passwordStub, boolean isLogged) {
			this.usernamestub = usernamestub;
			this.passwordStub = passwordStub;
			this.isLogged = isLogged;
		}

		public String getUsernamestub() {
			return usernamestub;
		}

		public void setUsernamestub(String usernamestub) {
			this.usernamestub = usernamestub;
		}

		public String getPasswordStub() {
			return passwordStub;
		}

		public void setPasswordStub(String passwordStub) {
			this.passwordStub = passwordStub;
		}

		public void setisLoggedStub(boolean isLogged) {
			this.isLogged = isLogged;
		}

		public boolean getisLoggedStub() {
			return this.isLogged;
		}

	}

	@Test
	void testUnsuccessfulLogin_username_and_password_not_in_DB() {
		Connection conn = connect2DB();
		boolean isLogged = false;
		try {
			isLogged = checkIfUserLoggedIn(loginStubFail, conn);
			assertFalse(isLogged);
			echoserver.checkUserLoginNoClient(loginStubFail);
			isLogged = checkIfUserLoggedIn(loginStubFail, conn);
			assertFalse(isLogged);
		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		
	}

	@Test
	void LoginSuccessTest_correct_logininfo()
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

	@Test
	void LoginFailTest_correct_username_and_incorrect_password() {

		Connection conn = connect2DB();// check th
		boolean isLogged = false;
		loginStubFail.setUserName("jsm");
		try {
			isLogged = checkIfUserLoggedIn(loginStubFail, conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(isLogged);
		echoserver.checkUserLoginNoClient(loginStubFail);
		try {

			isLogged = checkIfUserLoggedIn(loginStubFail, conn);
		} catch (SQLException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(isLogged);

	}
	@Test
	void LoginFailTest_incorrect_username_and_correct_password() {

		Connection conn = connect2DB();// check th
		boolean isLogged = false;
		loginStubFail.setPassword("456");
		try {
			isLogged = checkIfUserLoggedIn(loginStubFail, conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(isLogged);
		echoserver.checkUserLoginNoClient(loginStubFail);
		try {

			isLogged = checkIfUserLoggedIn(loginStubFail, conn);
		} catch (SQLException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(isLogged);

	}

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
			
		return (examID==null) ? false : true;

	}


public boolean AddExamtoDB( Exam exam,Connection conn) throws SQLException {
		boolean isExist = false;
		Integer examID=exam.getExamId();
		String command = String.format("SELECT * FROM cems.exams WHERE exam_id= '%d'",exam.getExamId());
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery(command);
		while (rs.next()) {
			// get fields from resultSet
			 examID = rs.getInt("exam_id");
			
				
		}
			
		return (examID==null) ? false : true;

	}



	@Test
	void CreateExam_SuccsesTest() {

		Connection conn = connect2DB();// check th
		boolean isExist;
		
		try {

			isExist = checkIfExamIsExist(exam,conn);
			assertFalse(isExist);

		} catch (SQLException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//create exam and insert to DB 
        



		//call to checkIfExamIsExist(conn)






		assertTrue(isExist);

	}

}
