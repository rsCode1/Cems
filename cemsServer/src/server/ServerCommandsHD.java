package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import logic.Exam;
import logic.Grades;
import logic.LogInInfo;
import logic.LoggedUsers;
import logic.Request;
import logic.RequestTime;
import logic.Response;
import logic.Users;
import ocsf.server.ConnectionToClient;

public class ServerCommandsHD {
    	public void ImportTwoStudentsSGrades(ArrayList<String> requestParam, ConnectionToClient client) {
		ArrayList<Grades> firstIDGrades = new ArrayList<Grades>();
		ArrayList<Grades> secondIDGrades = new ArrayList<Grades>();
		ArrayList<ArrayList<Grades>> alldata = new ArrayList<ArrayList<Grades>>();
		System.out.println(
				"in echo server import2ids,first id: " + requestParam.get(0) + "second id: " + requestParam.get(1));

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");

			Statement stmt = conn.createStatement();
			String command = String.format("SELECT * FROM cems.grades WHERE studentID=%s", requestParam.get(0));
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				int examID = rs.getInt("examId");
				int studentID = rs.getInt("studentId");
				int courseID = rs.getInt("courseID");
				int grade = rs.getInt("grade");
				int lecturerID = rs.getInt("lecturerID");
				Grades grades = new Grades(examID, studentID, courseID, grade, lecturerID);
				firstIDGrades.add(grades);

			}
			Statement stmnt2 = conn.createStatement();
			String command1 = String.format("SELECT * FROM cems.grades WHERE studentID=%s", requestParam.get(1));
			ResultSet rs2 = stmnt2.executeQuery(command1);
			while (rs2.next()) {
				int examID = rs.getInt("examId");
				int studentID = rs.getInt("studentId");
				int courseID = rs.getInt("courseID");
				int grade = rs.getInt("grade");
				int lecturerID = rs.getInt("lecturerID");
				Grades grades2 = new Grades(examID, studentID, courseID, grade, lecturerID);
				secondIDGrades.add(grades2);

			}
			alldata.add(firstIDGrades);
			alldata.add(secondIDGrades);

			client.sendToClient(new Response("Get2IDSGradesStudent", alldata));
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}

	}

	public void ImportTwoCourseGrades(ArrayList<String> requestParam, ConnectionToClient client) {
		ArrayList<Grades> firstIDGrades = new ArrayList<Grades>();
		ArrayList<Grades> secondIDGrades = new ArrayList<Grades>();
		ArrayList<ArrayList<Grades>> alldata = new ArrayList<ArrayList<Grades>>();
		System.out.println(
				"in echo server import2ids,first id: " + requestParam.get(0) + "second id: " + requestParam.get(1));

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");

			Statement stmt = conn.createStatement();
			String command = String.format("SELECT * FROM cems.grades WHERE courseID=%s", requestParam.get(0));
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				int examID = rs.getInt("examId");
				int studentID = rs.getInt("studentId");
				int courseID = rs.getInt("courseID");
				int grade = rs.getInt("grade");
				int lecturerID = rs.getInt("lecturerID");
				Grades grades = new Grades(examID, studentID, courseID, grade, lecturerID);
				firstIDGrades.add(grades);

			}
			Statement stmnt2 = conn.createStatement();
			String command1 = String.format("SELECT * FROM cems.grades WHERE courseID=%s", requestParam.get(1));
			ResultSet rs2 = stmnt2.executeQuery(command1);
			while (rs2.next()) {
				int examID = rs.getInt("examId");
				int studentID = rs.getInt("studentId");
				int courseID = rs.getInt("courseID");
				int grade = rs.getInt("grade");
				int lecturerID = rs.getInt("lecturerID");
				Grades grades2 = new Grades(examID, studentID, courseID, grade, lecturerID);
				secondIDGrades.add(grades2);

			}
			alldata.add(firstIDGrades);
			alldata.add(secondIDGrades);

			client.sendToClient(new Response("Get2IDSGradesCourse", alldata));
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}

	}

	public void ImportTwoLectuerGrades(ArrayList<String> requestParam, ConnectionToClient client) {
		ArrayList<Grades> firstIDGrades = new ArrayList<Grades>();
		ArrayList<Grades> secondIDGrades = new ArrayList<Grades>();
		ArrayList<ArrayList<Grades>> alldata = new ArrayList<ArrayList<Grades>>();
		System.out.println(
				"in echo server import2ids,first id: " + requestParam.get(0) + "second id: " + requestParam.get(1));

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");

			Statement stmt = conn.createStatement();
			String command = String.format("SELECT * FROM cems.grades WHERE lecturerID=%s", requestParam.get(0));
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				int examID = rs.getInt("examId");
				int studentID = rs.getInt("studentId");
				int courseID = rs.getInt("courseID");
				int grade = rs.getInt("grade");
				int lecturerID = rs.getInt("lecturerID");
				Grades grades = new Grades(examID, studentID, courseID, grade, lecturerID);
				firstIDGrades.add(grades);

			}
			Statement stmnt2 = conn.createStatement();
			String command1 = String.format("SELECT * FROM cems.grades WHERE lecturerID=%s", requestParam.get(1));
			ResultSet rs2 = stmnt2.executeQuery(command1);
			while (rs2.next()) {
				int examID = rs.getInt("examId");
				int studentID = rs.getInt("studentId");
				int courseID = rs.getInt("courseID");
				int grade = rs.getInt("grade");
				int lecturerID = rs.getInt("lecturerID");
				Grades grades2 = new Grades(examID, studentID, courseID, grade, lecturerID);
				secondIDGrades.add(grades2);

			}
			alldata.add(firstIDGrades);
			alldata.add(secondIDGrades);

			client.sendToClient(new Response("Get2IDSGradesLectuer", alldata));
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}

	}

	public void getRequestTimeInfo(RequestTime requestTime, ConnectionToClient client) {
		System.out.println("in request time");

		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			ArrayList<RequestTime> requestList = new ArrayList<RequestTime>();
			Statement stmt = conn.createStatement();
			String command = String.format("SELECT * FROM cems.requests");
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				String examID = rs.getString("examID");
				String IDRequest = rs.getString("requestID");
				String RequestBy = rs.getString("Requestedby");
				String reason = rs.getString("Reason");
				int extraTime = rs.getInt("ExtraTime");

				RequestTime request = new RequestTime(examID, IDRequest, RequestBy, extraTime, reason);
				requestList.add(request);

			}
			client.sendToClient(new Response("EXTRA_TIME_INFO", requestList));
		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}
	}

	public void searchExam(Request rq, ConnectionToClient client) {
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
								String.format("UPDATE open_exams SET test_time = test_time + %d WHERE exam_id = %s",
										extraTime,
										examID));
						Statement statment = conn.createStatement();
						int rowsAffected = statment
								.executeUpdate(String.format("DELETE FROM requests WHERE examID=%s", examID));

						Response response = new Response("Who Requested Extra Time", requestedBy);
						client.sendToClient(response);

					}
					break;
				case "REJECT":
					while (rs.next()) {
						Statement st = conn.createStatement();
						int rowsAffected = st
								.executeUpdate(String.format("DELETE FROM requests WHERE examID=%s", examID));

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

	public void importStudentData(String gID, ConnectionToClient client) throws IOException, SQLException {
		ArrayList<Grades> grades = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			Statement stmt = conn.createStatement();

			if (gID == null) {
				String command = String.format("SELECT * FROM cems.grades");
				ResultSet rs = stmt.executeQuery(command); // send student data to the client

				while (rs.next()) {
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));

				}
			} else {
				String command = String.format("SELECT * FROM cems.grades WHERE studentID=" + gID);
				ResultSet rs = stmt.executeQuery(command); // send student data to the client

				while (rs.next()) {
					// get fields from resultSet
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));
				}
			}

			client.sendToClient(new Response("ImportStudentData", grades));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void importStudentDataTenths(String gID, ConnectionToClient client) throws IOException, SQLException {
		ArrayList<Grades> grades = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			Statement stmt = conn.createStatement();

			if (gID == null) {
				String command = String.format("SELECT * FROM cems.grades");
				ResultSet rs = stmt.executeQuery(command); // send student data to the client

				while (rs.next()) {
					// get fields from resultSet
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));

				}
			} else {
				String command = String.format("SELECT * FROM cems.grades WHERE studentID=" + gID);
				ResultSet rs = stmt.executeQuery(command); // send student data to the client

				while (rs.next()) {
					// get fields from resultSet
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));
				}
			}

			client.sendToClient(new Response("ImportStudentDataTenths", grades));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void importLectuerData(String gID, ConnectionToClient client) throws IOException, SQLException {
		ArrayList<Grades> grades = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			Statement stmt = conn.createStatement();
			if (gID == null) {
				String command = String.format("SELECT * FROM cems.grades");
				ResultSet rs = stmt.executeQuery(command); // send student data to the client

				while (rs.next()) {
					// get fields from resultSet
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));
				}
			} else {
				String command = String.format("SELECT * FROM grades WHERE lecturerID=" + gID);
				ResultSet rs = stmt.executeQuery(command);// send lecture to the client
				////
				while (rs.next()) {
					// get fields from resultSet
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));
				}
			}

			client.sendToClient(new Response("ImportLectuerData", grades));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void importLectuerDataTenths(String gID, ConnectionToClient client) throws IOException, SQLException {
		ArrayList<Grades> grades = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			Statement stmt = conn.createStatement();
			if (gID == null) {
				String command = String.format("SELECT * FROM cems.grades");
				ResultSet rs = stmt.executeQuery(command); // send student data to the client

				while (rs.next()) {
					// get fields from resultSet
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));

				}
			} else {
				String command = String.format("SELECT * FROM grades WHERE lecturerID=" + gID);
				ResultSet rs = stmt.executeQuery(command);// send lecture to the client
				////
				while (rs.next()) {
					// get fields from resultSet
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));
				}
			}

			client.sendToClient(new Response("ImportLectuerDataTenths", grades));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void importCourseData(String gID, ConnectionToClient client) throws IOException {

		ArrayList<Grades> grades = new ArrayList<>();
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			Statement stmt = conn.createStatement();
			if (gID == null) {
				String command = String.format("SELECT * FROM cems.grades");
				ResultSet rs = stmt.executeQuery(command); // send student data to the client

				while (rs.next()) {
					// get fields from resultSet
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));

				}
			} else {
				String command = String.format("SELECT *  FROM grades WHERE courseID=" + gID);
				ResultSet rs = stmt.executeQuery(command);// send lecture to the client
				while (rs.next()) {
					int examID1 = rs.getInt("examId");
					int studentID1 = rs.getInt("studentId");
					int courseID1 = rs.getInt("courseID");
					int grade1 = rs.getInt("grade");
					int lecturerID1 = rs.getInt("lecturerID");
					grades.add(new Grades(examID1, studentID1,
							courseID1, grade1, lecturerID1));

				}
			}

			client.sendToClient(new Response("ImportCourseData", grades));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void importCourseDatatenths(String gID, ConnectionToClient client) throws IOException {
		System.out.println("in importcourseid " + gID);
		ArrayList<Grades> grades = new ArrayList<>();

		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");

			Statement stmt = conn.createStatement();
			String command = String.format("SELECT *  FROM grades WHERE courseID=" + gID);
			ResultSet rs = stmt.executeQuery(command);// send lecture to the client
			while (rs.next()) {
				// get fields from resultSet
				int examID1 = rs.getInt("examId");
				int studentID1 = rs.getInt("studentId");
				int courseID1 = rs.getInt("courseID");
				int grade1 = rs.getInt("grade");
				int lecturerID1 = rs.getInt("lecturerID");
				grades.add(new Grades(examID1, studentID1,
						courseID1, grade1, lecturerID1));

				client.sendToClient(new Response("ImportCourseDatatenths", grades));
				System.out.println("sent to client");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	

	public int getCourseId(String courseName, Connection conn) {
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


	// update the user as logged in DB , based on its username and password


	// return user info from DB, based on its username and password
	public Users getUserInfo(LogInInfo login, Connection conn) throws SQLException {
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
}
