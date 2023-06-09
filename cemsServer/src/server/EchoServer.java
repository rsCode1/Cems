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

import gui.ServerStartScreenController;
import logic.Grades;
import logic.LogInInfo;
import logic.LoggedUsers;
import logic.Request;
import logic.RequestTime;
import logic.Users;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	private ServerStartScreenController serverScreenController;

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

			case "GET-EXTRA-TIME-REQUEST":
				getRequestTimeInfo(null, client);
				break;
			case "SearchExam":

				searchExam((Request) request.getRequestParam(), client);
				break;
			case "SendLectuerID":
				System.out.println("sendID:" + request.getRequestParam());
				try {
					importID((String) request.getRequestParam(), client);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("sendID:" + request.getRequestParam());
				break;

			// Add more case statements for other request types
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
				client.sendToClient((Users) null);
				return;
			}
			updateUserLoggedIn(loginInfo, conn);
			addUserToLoggedTable(conn);
			client.sendToClient(user);

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

	private void searchExam(Request rq, ConnectionToClient client) {
		String examID = (String) rq.getRequestParam();
		String status = rq.getRequestType();// approve or reject

		try {
			// Update the time of the exam
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			Statement stmt = conn.createStatement();
			String command = String.format("SELECT * FROM requests WHERE examID = %s", examID);
			ResultSet rs = stmt.executeQuery(command);
			switch (status) {
			case "APPROVE":

				while (rs.next()) {
					int extraTime = rs.getInt("ExtraTime");
					String requestedBy = rs.getString("RequestedBy");

					Statement st = conn.createStatement();
					int updateStmt = st.executeUpdate(
							String.format("UPDATE exams SET time = time + %d WHERE examID = %s", extraTime, examID));
					Statement statment = conn.createStatement();
					int rowsAffected = statment
							.executeUpdate(String.format("DELETE FROM requests WHERE examID=%s", examID));

					Request request = new Request("Who Requested Extra Time", requestedBy);
					client.sendToClient(request);

				}
				break;
			case "REJECT":
				while (rs.next()) {
					Statement st = conn.createStatement();
					int rowsAffected = st.executeUpdate(String.format("DELETE FROM requests WHERE examID=%s", examID));

				}

			}

			// Close the resources
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void getRequestTimeInfo(RequestTime requestTime, ConnectionToClient client) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			ArrayList<RequestTime> requestList = new ArrayList<RequestTime>();
			Statement stmt = conn.createStatement();
			String command = String.format("SELECT * FROM cems.requests");
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				String examID = rs.getString(1);
				String IDRequest = rs.getString(2);
				String RequestBy = rs.getString(3);
				String reason = rs.getString(4);
				int extraTime = rs.getInt(5);
				RequestTime request = new RequestTime(examID, IDRequest, RequestBy, extraTime, reason);
				requestList.add(request);
			}
			client.sendToClient(requestList);
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}
	}

	private void logOut(LogInInfo login, ConnectionToClient client) {

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
				"Aa123456"); Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(String.format("UPDATE users SET isLogged=0 WHERE userName='%s' AND password ='%s'",
					login.getUserName(), login.getPassword()));
			addUserToLoggedTable(conn);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void importID(String gID, ConnectionToClient client) throws IOException {
		ArrayList<Grades> grades= new ArrayList<>();
		System.out.println("THE ID IS");
		//String ID = (String) gID.getRequestParam();
		System.out.println(gID.toString());
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
				"Aa123456"); 
				Statement stmt = conn.createStatement()) {
			String command = String.format("SELECT * FROM cems.users");
			ResultSet rs = stmt.executeQuery(command);
			if (rs.next()) {
				// get fields from resultSet
				Integer id = rs.getInt("id");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("LastName");
				String userName = rs.getString("userName");
				int role = rs.getInt("role");
				// RequestTime request = new RequestTime(IDRequest, CourseName, RequestBy,extraTime, reason);
				// requestList.add(request);
				if (role == 0) { // if its student
					 command = String.format("SELECT * FROM cems.grades WHERE studentID="+gID);
					ResultSet rs2 = stmt.executeQuery(command); //send student data to the client
					while(rs2.next()) {
						// get fields from resultSet
						String exemID1= rs2.getString("examID");
						String studentID1 = rs2.getString("studentID");
						String courseID1 = rs2.getString("courseID");
						int grade1 = rs2.getInt("grade");
						int dataof = rs2.getInt("dataOf");
						grades.add(new Grades(exemID1, studentID1,courseID1,grade1,dataof));
				
					}
					client.sendToClient(grades);
					}
				
				if (role == 1) { // if its lecture
					 command = String.format("SELECT *  FROM grades WHERE courseID="+gID);
					ResultSet rs2 = stmt.executeQuery(command);//send lecture to the client
					//add class for student data?
				}
				
			}
			// client.sendToClient(requestList);
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

	// Class methods *****************

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