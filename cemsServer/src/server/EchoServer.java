package server;
// This file contains material supporting section 3.7 of the textbook:

import java.io.IOException;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;

import gui.ServerStartScreenController;
import logic.Exam;
import logic.LogInInfo;
import logic.LoggedUsers;
import logic.Question;
import logic.Request;
import logic.Response;
import logic.Users;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	private ServerStartScreenController serverScreenController;
	private ServerCommandsLecturer lecturerCommands = new ServerCommandsLecturer();

	final public static int DEFAULT_PORT = 5555;

	public EchoServer(int port) {
		super(port);
	}

	public void setController(ServerStartScreenController controller2) {
		this.serverScreenController = controller2;
	}

	@Override
	public void handleMessageFromClient//
	(Object msg, ConnectionToClient client)

	{

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}
		if (msg instanceof Request) {
			Request request = (Request) msg;

			switch (request.getRequestType()) {
				case "LOGIN":
					checkUserLogin((LogInInfo) request.getRequestParam(), client);
					break;
				case "LOGOUT":
					logOut((LogInInfo) request.getRequestParam(), client);
					break;
				case "writeQuestion":
					lecturerCommands.writeQuestion((Question) request.getRequestParam(), client);
					break;
				case "getSubjects":
					lecturerCommands.getSubjects(client);
					break;
				case "getCourses":
					lecturerCommands.getCourses(client, (String) request.getRequestParam());
					break;
				case "getQuestions":
					lecturerCommands.getQuestions(client, (String) request.getRequestParam());
					break;
				case "saveExam":
					lecturerCommands.saveExam((Exam) request.getRequestParam(), client);
					break;
				case "getExamsByLecturer":
					lecturerCommands.getExamsByLecturer(client, (Users) request.getRequestParam());
					break;
				case "getOngoingExams":
					lecturerCommands.getOngoingExams(client, (Users) request.getRequestParam());
					break;
				case "startExam":
					lecturerCommands.startExam(client, (Exam) request.getRequestParam());
					break;
				case "getGrades":
					lecturerCommands.getGrades(client, (Users) request.getRequestParam());
					break;
				case "approveGrade":
					lecturerCommands.approveGrade(client, (Exam) request.getRequestParam());
					break;
				case "changeGrade":
					lecturerCommands.changeGrade(client, (Exam) request.getRequestParam());
					break;
			}
		}
	}



	private void checkUserLogin(LogInInfo loginInfo, ConnectionToClient client) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			Users user = getUserInfo(loginInfo, conn);
			if (user == null) {

				client.sendToClient(new Response("LOGIN", null));
				return;
			}
			updateUserLoggedIn(loginInfo, conn);
			addUserToLoggedTable(conn);
			Response response = new Response("LOGIN", user);
			client.sendToClient(response);

		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}
	}

	// function to add all the users that are logged in to the logged to the table
	// in server
	private void addUserToLoggedTable(Connection conn) throws SQLException {
		// arraylist to hold info about all currently logged in users
		ArrayList<LoggedUsers> LoggedUsersArray = new ArrayList<>();

		// select relevant informatiom from DB to display in table
		Statement stmt2 = conn.createStatement();
		String command = "SELECT id,firstName,LastName,userName,role FROM users " + "WHERE islogged=1 ";
		ResultSet rs = stmt2.executeQuery(command);
		while (rs.next()) {
			// get fields from resultSet
			Integer id = rs.getInt("id");
			String firstName = rs.getString("firstName");
			String lastName = rs.getString("LastName");
			String userName = rs.getString("userName");
			int role = rs.getInt("role");

			LoggedUsers loggedUser = new LoggedUsers(id, firstName, lastName, userName, role);

			LoggedUsersArray.add(loggedUser);
		}
		// add the user to the logged in table through with controller function
		// 'UpadteOnlineUsers'
		serverScreenController.UpadteOnlineUsers(LoggedUsersArray);
	}

	// update the user as logged in DB , based on its username and password
	private void updateUserLoggedIn(LogInInfo login, Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(String.format("UPDATE users SET isLogged=1 WHERE userName='%s' AND password ='%s'",
				login.getUserName(), login.getPassword()));
	}

	// return user info from DB, based on its username and password
	private Users getUserInfo(LogInInfo login, Connection conn) throws SQLException {
		// select user with username and password
		Statement stmt = conn.createStatement();
		String command = String.format("SELECT * FROM users" + " WHERE userName='%s' AND password ='%s'",
				login.getUserName(), login.getPassword());
		ResultSet rs = stmt.executeQuery(command);
		if (!rs.next())
			return (null);
		return new Users(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6),
				rs.getInt(7));
	}

	private void logOut(LogInInfo login, ConnectionToClient client) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(String.format("UPDATE users SET isLogged=0 WHERE userName='%s' AND password ='%s'",
					login.getUserName(), login.getPassword()));
			addUserToLoggedTable(conn);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	@Override
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	@Override
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */

	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
// End of EchoServer class
