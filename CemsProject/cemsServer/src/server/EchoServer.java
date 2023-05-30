package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import gui.ServerStartScreenController;
import logic.LogInInfo;
import logic.LoggedUsers;
import logic.Users;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	private ServerStartScreenController controller;

	final public static int DEFAULT_PORT = 5555;

	public EchoServer(int port) {
		super(port);
	}

	public void setController(ServerStartScreenController controller2) {
		this.controller = controller2;
	}

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

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/lab3db?serverTimezone=IST", "root",
					"123456");
			System.out.println("SQL connection succeed");
			if (msg instanceof LogInInfo && msg != null) {

				LogInInfo msg2 = ((LogInInfo) msg);
				Users user = getUserInfo(msg2, conn);
				Statement stmt = conn.createStatement();
				if (user == null) {
					client.sendToClient((Users)null);
					return;
				}

				ArrayList<LoggedUsers> LoggedUsersArray = new ArrayList<LoggedUsers>();

				Statement stmt2 = conn.createStatement();
				String command = "SELECT id,firstName,userName,LastName,role FROM users " + "WHERE islogged=1 ";
				ResultSet rs = stmt2.executeQuery(command);
				while (rs.next()) {
					Integer id = rs.getInt(1);
					String firstName = rs.getString(2);
					String lastName = rs.getString(3);
					String userName = rs.getString(4);
					int role = rs.getInt(5);

					LoggedUsers usr = new LoggedUsers(id, firstName, lastName, userName, role);
					LoggedUsersArray.add(usr);
				}

				controller.UpadteOnlineUsers(LoggedUsersArray);
				client.sendToClient(user);
			}
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}

	}

	private Users getUserInfo(LogInInfo login, Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(String.format("UPDATE users SET isLogged=1 WHERE userName='%s' AND password ='%s'",
				login.getUserName(), login.getPassword()));

		String command = String.format("SELECT * FROM users" + " WHERE userName='%s' AND password ='%s'",
				login.getUserName(), login.getPassword());
		ResultSet rs = stmt.executeQuery(command);
		if (rs.next() == false)
			return ((Users)null);
		return new Users(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6),
				rs.getInt(7));
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
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
