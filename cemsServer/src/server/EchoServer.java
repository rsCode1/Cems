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
					writeQuestion((Question) request.getRequestParam(), client);
					break;
				case "getSubjects":
					getSubjects(client);
					break;
				case "getCourses":
					getCourses(client, (String) request.getRequestParam());
					break;
				case "getQuestions":
					getQuestions(client, (String) request.getRequestParam());
					break;
				case "saveExam":
					saveExam((Exam) request.getRequestParam(), client);
					break;
				case "getExamsByLecturer":
					getExamsByLecturer(client, (Users) request.getRequestParam());
					break;
				case "getOngoingExams":
					getOngoingExams(client, (Users) request.getRequestParam());
				case "startExam":
					startExam(client, (Exam) request.getRequestParam());
					// Add more case statements for other request types
			}
		}
	}

	private void startExam(ConnectionToClient client, Exam exam) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			String checkCodeCommand = "SELECT code FROM open_exams WHERE exam_id = ?";
			PreparedStatement stmt = conn.prepareStatement(checkCodeCommand);
			stmt.setInt(1, exam.getExamId());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()){
					Response response = new Response("startExamFailed", null);
					client.sendToClient(response);
					return;

			}
			String command = "INSERT INTO open_exams VALUES(?,?,?)";
			stmt = conn.prepareStatement(command);
			stmt.setInt(1, exam.getExamId());
			stmt.setInt(2, exam.getCode());
			stmt.setInt(3, exam.getTestTime());
			stmt.executeUpdate();
			Response response = new Response("startExamSuccess", null);
			client.sendToClient(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


		private void getOngoingExams(ConnectionToClient client, Users lecturer) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed Ongoing Exams!");
			// create Table, if exist skip
			String command1 = "SELECT open_exams.exam_id, open_exams.code, open_exams.test_time "
					+ "FROM open_exams "
					+ "JOIN exams ON open_exams.exam_id = exams.exam_id "
					+ "WHERE exams.lecturer_id = ?";
			PreparedStatement stmt = conn.prepareStatement(command1);
			stmt.setInt(1, lecturer.getId());

			ResultSet rs = stmt.executeQuery();
			ArrayList<Exam> exams = new ArrayList<>();
			while (rs.next()) {
				Exam exam = new Exam(rs.getInt("exam_id"), rs.getInt("code"), rs.getInt("test_time"));
				exams.add(exam);
			}
			Response response = new Response("getOngoingExams", exams);
			client.sendToClient(response);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	private void getExamsByLecturer(ConnectionToClient client, Users lecturer) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			Statement stmt = conn.createStatement();
			String command = "SELECT exam_id,course_name,lecturer_comments,student_comments,test_time FROM exams WHERE lecturer_id = '"
					+ lecturer.getId() + "'";
			ResultSet rs = stmt.executeQuery(command);
			ArrayList<Exam> exams = new ArrayList<>();
			while (rs.next()) {
				Exam exam = new Exam(rs.getInt("exam_id"), rs.getString("course_name"),
						rs.getString("lecturer_comments"), rs.getString("student_comments"), rs.getInt("test_time"));
				exams.add(exam);
			}
			Response response = new Response("getExamsByLecturer", exams);
			client.sendToClient(response);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}



	// save the exam to the database using the exam class
	private void saveExam(Exam exam, ConnectionToClient client) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			Statement stmt = conn.createStatement();
			String command = "INSERT INTO exams (course_name,lecturer_id,lecturer_comments,student_comments,test_time) VALUES ('"
					+ exam.getCourseName() + "','" + exam.getLecturer().getId() + "','" + exam.getLecturerComments()
					+ "','" + exam.getStudentComments() + "','" + exam.getTestTime() + "')";
			stmt.executeUpdate(command, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			int examId = rs.getInt(1);

			// just now remembered that i can use prepared statements ):
			// wasted ton of time on this

			PreparedStatement stmt2;
			// now, insert the exam's questions into the exam_questions table
			for (Question question : exam.getQuestions()) {
				command = "INSERT INTO exam_questions (exam_id, question_id, score) VALUES (?, ?, ?)";
				stmt2 = conn.prepareStatement(command);

				// set the parameters
				stmt2.setInt(1, examId);
				stmt2.setInt(2, question.getQuestionID());
				stmt2.setInt(3, question.getScore());
				// execute the INSERT statement
				stmt2.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			client.sendToClient(new Response("ExamSaved", null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getQuestions(ConnectionToClient client, String course) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			Statement stmt = conn.createStatement();
			String command = "SELECT course_id FROM courses WHERE course_name = '" + course + "'";
			ResultSet rs = stmt.executeQuery(command);
			rs.next();
			int courseId = rs.getInt("course_id");
			command = "SELECT * FROM questions WHERE course_id = '" + courseId + "'";
			rs = stmt.executeQuery(command);
			ArrayList<Question> questions = new ArrayList<Question>();
			while (rs.next()) {
				int questionId = rs.getInt("question_id");
				String questionText = rs.getString("question_text");
				String answer1 = rs.getString("answer1");
				String answer2 = rs.getString("answer2");
				String answer3 = rs.getString("answer3");
				String answer4 = rs.getString("answer4");
				int correctAnswer = rs.getInt("correct_answer");
				int authorId = rs.getInt("lecturer_id");
				String authorName = rs.getString("lecturer_name");
				Question question = new Question(questionId, questionText, answer1, answer2, answer3, answer4,
						correctAnswer, courseId, authorId, authorName);
				questions.add(question);
			}
			Response response = new Response("QuestionsArray", questions);
			client.sendToClient(response);
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}
	}

	// same as getSubjects but for courses
	private void getCourses(ConnectionToClient client, String subject) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			Statement stmt = conn.createStatement();
			String command = "SELECT subject_id FROM subjects WHERE subject_name = '" + subject + "'";
			ResultSet rs = stmt.executeQuery(command);
			rs.next();
			String subjectId = rs.getString("subject_id");
			command = "SELECT course_name FROM courses WHERE subject_id = '" + subjectId + "'";
			rs = stmt.executeQuery(command);
			ArrayList<String> courses = new ArrayList<String>();
			while (rs.next()) {
				courses.add(rs.getString("course_name"));
			}
			Response response = new Response("Courses", courses);
			client.sendToClient(response);
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}
	}

	private void getSubjects(ConnectionToClient client) {

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			Statement stmt = conn.createStatement();
			String command = "SELECT subject_name FROM subjects";
			ResultSet rs = stmt.executeQuery(command);
			ArrayList<String> subjects = new ArrayList<String>();
			while (rs.next()) {
				subjects.add(rs.getString("subject_name"));
			}
			for (String subject : subjects) {
				System.out.println(subject);
			}
			Response response = new Response("Subjects", subjects);
			client.sendToClient(response);
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}
	}

	// save the question in the DB
	// uses Question class from cemsShared

	private void writeQuestion(Question question, ConnectionToClient client) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			int courseId = getCourseId(question.getCourseName(), conn);
			Statement stmt = conn.createStatement();
			String command = "INSERT INTO questions (question_text,answer1,answer2,answer3,answer4,correct_answer,course_id,lecturer_id,lecturer_name) VALUES ('"
					+ question.getQuestionDescription() + "','" + question.getAnswer1() + "','" + question.getAnswer2()
					+ "','" + question.getAnswer3() + "','" + question.getAnswer4() + "','"
					+ question.getCorrectAnswer() + "','" + courseId + "','" + question.getAuthorID() + "','"
					+ question.getAuthor() + "')";
			stmt.executeUpdate(command);

		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
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

	private int getCourseId(String courseName, Connection conn) {
		Statement stmt;
		String command;
		ResultSet rs;
		int courseId = -1;
		try {
			stmt = conn.createStatement();
			command = String.format("SELECT course_id FROM courses WHERE course_name='%s'", courseName);
			rs = stmt.executeQuery(command);
			if (rs.next())
				return rs.getInt("course_id");
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}
		return courseId;

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
