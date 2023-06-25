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

class LoginTest {
	LogInInfo loginStubSuccess;
	LogInInfo loginStubFailNull, loginStubFailNotInDB;
	EchoServer echoserver;
	ServerCommandsLecturer SCL;
	ChatClient client;
	Users successUser, failUser;
	private Users lecturer, lecturer2;
	private Connection connMock;
	ConnectionToClient connectionMock;


	@BeforeEach
	void setUp() throws Exception {

		echoserver = new EchoServer(5555);
		lecturer = new Users(496351, "Dina", "Barzilai", "dnb", "111", 0, 1);
		lecturer2 = new Users(2, "Jane", "Smith", "jsm", "456", 0, 1);

		SCL = new ServerCommandsLecturer();

		loginStubFailNull = new LogInInfo(null, null);
		loginStubSuccess = new LogInInfo(null, null);
		loginStubSuccess.setUserName("jsm");
		loginStubFailNotInDB = new LogInInfo("not_username", "worng_password");
		connectionMock = Mockito.mock(ConnectionToClient.class);
		loginStubSuccess.setPassword("456");
		loginStubFailNotInDB.setUserName("test");
		loginStubFailNotInDB.setPassword("111");
	}

	// test login of username and passwords which are not in DB
	// input loginStubFailNotInDB("not_username", "worng_password"),
	// expected : user is not found in DB and not logged in.
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

	// test login of username and passwords which are not in DB
	// input loginStubSuccess("jsm", "456"),
	// expected : user is found in DB and is able to log in.
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

	// test login of username and passwords which are not in DB,only username is in
	// DB
	// input loginStubFailNotInDB("jsm", "worng_password"),
	// expected : user is not found in DB and not logged in.
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

	// test login of username and passwords which are not in DB,only password is in
	// DB
	// input loginStubFailNotInDB("wrong username", "456"),
	// expected : user is not found in DB and not logged in.
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



}
