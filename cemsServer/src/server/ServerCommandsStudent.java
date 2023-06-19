package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import logic.AddedTime;
import logic.InTestQuestion;
import logic.MyFile;
import logic.Response;
import logic.StudentData;
import logic.StudentInTest;
import logic.Test;
import logic.TestApplyInfo;
import logic.TestCode;
import logic.TestSourceTime;
import logic.UploadFile;
import ocsf.server.ConnectionToClient;

public class ServerCommandsStudent {

	public void getStudentGrades(int studentId, ConnectionToClient client) {

		ArrayList<StudentData> studentGradesInfo = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			String query = "SELECT courseName ,grade, status FROM grades WHERE StudentID=" + studentId + " ;";
			Statement stmt = conn.prepareStatement(query);
			ResultSet resultSet = stmt.executeQuery(query);
			while (resultSet.next()) {
				String column2Value = resultSet.getString("courseName");
				String column3Value = String.valueOf(resultSet.getInt("grade"));
				String stat = resultSet.getString("status");
				if (!stat.equals("approved"))
					column3Value = "---";
				studentGradesInfo.add(new StudentData(column2Value, column3Value));
			}
			Response response = new Response("GetStudentGrades", studentGradesInfo);
			resultSet.close();
			try {
				client.sendToClient(response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addStudentToInExamList(TestApplyInfo testApplyInfo, ConnectionToClient client) {
		Statement stmt;
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			stmt = conn.createStatement();
			String str = "INSERT INTO `cems`.`student_inexam` (`student_id`, `exam_id`) VALUES ('"
					+ testApplyInfo.getStudentId() + "', '" + testApplyInfo.getTestId() + "');";
			stmt.executeUpdate(str);
			str = "";
			System.out.println("Added student to student_inexam list");
		}

		catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void submitManualExam(UploadFile answersFile, ConnectionToClient client) {
		try {

			boolean Submitted = true;
			String status = "Not Submitted- manualy";
			if (answersFile.getMyfile() == null)
				Submitted = false;
			if (Submitted) {
				String LocalfilePath = "Submitted_Manual_Exams_Files\\" + answersFile.getMyfile().getFileName();
				File newFile = new File(LocalfilePath);
				FileOutputStream fis = new FileOutputStream(newFile);
				BufferedOutputStream bis = new BufferedOutputStream(fis);
				bis.write(answersFile.getMyfile().getMybytearray(), 0, answersFile.getMyfile().getSize());
				bis.close();
				status = "Submitted- Manualy";
			}//answersFile.getStudentInTest()
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			Statement stmt2, stmt3, stmt4, stmt5,stmt6,stmt7,stmt8;
			String str2 = "INSERT INTO `cems`.`grades` (`examId`, `studentId`, `courseID`, `grade`, `lecturerID`, `courseName`, `status`)";
			str2 += " VALUES ('" + answersFile.getStudentInTest().getTestId() + "', '" + answersFile.getStudentInTest().getStudentId() + "', '"
					+ answersFile.getStudentInTest().getCourseId() + "', '" + answersFile.getStudentInTest().getScore();
			str2 += "', '" + answersFile.getStudentInTest().getLecturerId() + "', '" + answersFile.getStudentInTest().getCourseName() + "','" + "pending"
					+ "');";
			stmt2 = conn.createStatement();
			stmt2.executeUpdate(str2);
			String str3 = "UPDATE `cems`.`student_inexam` SET `submitted` = '1' WHERE (`student_id` ="
					+ answersFile.getStudentId() + ") AND (`exam_id` = " + answersFile.getTestId() + ");";
			stmt3 = conn.createStatement();
			stmt3.executeUpdate(str3);
			String testId = '"' + String.valueOf(answersFile.getTestId()) + '"';
			String str4 = "SELECT (SELECT COUNT(*) FROM cems.student_inexam WHERE exam_id =" + testId
					+ ") AS total_rows,";
			str4 += "(SELECT COUNT(*) FROM cems.student_inexam WHERE exam_id =" + testId
					+ "AND submitted = 1) AS submitted_rows;";
			stmt4 = conn.createStatement();
			ResultSet rs,rs2,rs3;
			rs = stmt4.executeQuery(str4);
			if (rs.next()) {
				if (rs.getInt("total_rows") == rs.getInt("submitted_rows")) {
					int studentsnumber= rs.getInt("total_rows"); //number of students who did the exam
					String str6= "SELECT test_time,code FROM cems.open_exams where exam_id=" +testId   +" ;";
					stmt6= conn.createStatement();
					rs2=stmt6.executeQuery(str6);
					rs2.next();
					int duration=rs2.getInt("test_time");//test duration
					int code=rs2.getInt("code");//test code
					rs2.close();
					String str5 = "DELETE FROM cems.open_exams where exam_id =" + answersFile.getTestId() + " ;";
					stmt5 = conn.createStatement();
					stmt5.executeUpdate(str5);
					LocalDateTime now = LocalDateTime.now();
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm");
			        String DateExamEnded = now.format(formatter);//date end
			        LocalDateTime before = LocalDateTime.now().minusMinutes(duration);
			        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm");
			        String DateExamStarted = before.format(formatter2); //date start
			       //this query returns the number of ques id's in test where 70% of students who did the exam 
			       //answered the same wrong answer, so if there is at least one ques id 
			       //there's a big chance of cheating 
			        String query = "SELECT COUNT(*) AS numRowsReturned " +
			        	    "FROM ( " +
			        	    "    SELECT quesId, answerNum, COUNT(*) AS answerCount, (SELECT COUNT(DISTINCT studentId) FROM students_answers WHERE examId ="+ testId +") AS totalStudents " +
			        	    "    FROM students_answers " +
			        	    "    WHERE examId ="+ testId +"AND answerNum <> correctAnswer " +
			        	    "    GROUP BY quesId, answerNum " +
			        	    "    HAVING answerCount >= 0.7 * totalStudents " +
			        	    ") AS subquery;";
			        stmt7=conn.createStatement();
			        rs3=stmt7.executeQuery(query);
			        int quesCheated =0;
			        if (rs3.next()) {
			        	quesCheated=rs3.getInt("numRowsReturned");
			        }
			        rs3.close();
			        int cheated=0;
			        //if 20% of questions are "Cheated" then cheated is assured
			        double percentage = (double) quesCheated / answersFile.getStudentInTest().getQesSize() * 100;
			        if (percentage >= 35 & studentsnumber>1 & answersFile.getStudentInTest().getQesSize() >2) {
			        	cheated=1; //if cheated=1 then there is cheating in the exam
			        }
			        String insertQuery = "INSERT INTO `cems`.`closed_exams` " +
		                     "(`exam_id`, `code`, `test_time`, `date_start`, `date_end`, `cheat`, `students_number`, `lecturer_id`) " +
		                     "VALUES ("+ testId +", '"+ code +"', '"+ duration + "', '"+ DateExamStarted +"', '"+DateExamEnded +"', '"+ cheated +"', '"+ studentsnumber + "', '" + answersFile.getStudentInTest().getLecturerId() +"');";
			        stmt8 = conn.createStatement();
					stmt8.executeUpdate(insertQuery);
				}
			}
			rs.close();

		} catch (Exception e) {
			System.out.println("Error sending files to Server");
			e.printStackTrace();
		}

	}

	// finished
	public void downloadManuelExam(int testid, ConnectionToClient client) {
		MyFile msg2 = new MyFile(testid + ".docx");
		String LocalfilePath = "Manual_Exams_Files\\" + testid + ".docx";

		try {

			File newFile = new File(LocalfilePath);

			byte[] mybytearray2 = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);

			msg2.initArray(mybytearray2.length);
			msg2.setSize(mybytearray2.length);

			bis.read(msg2.getMybytearray(), 0, mybytearray2.length);
			Response response = new Response("DownloadManualExam", msg2);
			client.sendToClient(response);
		} catch (Exception e) {
			System.out.println("Error sending exam file to Server");
		}

	}

	// if applying info is correct the function gets requested Test from data base
	// and sends it to Client
	public void getExam(TestCode t, ConnectionToClient client) {
		int testid;
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			testid = ApplyingInfoExisted(conn, t);
			if (testid != -1) {
				Test test = getTest(conn, testid);
				if (test != null) {
					ArrayList<InTestQuestion> qLst = getTestQuestions(conn, testid);
					if (qLst != null) {
						test.setQLst(qLst);
						test.setquesSize(qLst.size());
						try {
							Response response = new Response("GetExam", test);
							client.sendToClient(response);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.out.println("Questions not Found!!! ");
						try {
							client.sendToClient((Response) null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("Test code not Found!!! ");
					try {
						client.sendToClient((Response) null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Test not Found!!! ");
				try {
					client.sendToClient((Response) null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// returns Question list if testid Existed in DataBase else null
	public ArrayList<InTestQuestion> getTestQuestions(Connection conn, int testid) {
		ArrayList<InTestQuestion> qLst = new ArrayList<InTestQuestion>();
		boolean f = false;
		Statement stmt;
		try {
			String id = '"' + String.valueOf(testid) + '"';
			String str = "SELECT * FROM cems.exam_questions eq, cems.questions q where eq.exam_id=" + testid
					+ " And eq.question_id = q.question_id ;";
			// str= str+ " AND t.idTest = " + id + ";";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(str);
			int i = 0;
			while (rs.next()) {
				String[] ansArr = new String[4];
				ansArr[0] = rs.getString("answer1");
				ansArr[1] = rs.getString("answer2");
				ansArr[2] = rs.getString("answer3");
				ansArr[3] = rs.getString("answer4");
				qLst.add(new InTestQuestion(rs.getString("question_text"), ansArr, rs.getInt("score"),
						rs.getInt("question_id"), rs.getInt("correct_answer")));
				i++;
				f = true;
			}
			rs.close();
			if (f) {
				return qLst;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void submitTest(StudentInTest studentInTest, ConnectionToClient client) {
		Statement stmt, stmt2, stmt3, stmt4, stmt5,stmt6,stmt7,stmt8;
		String str;
		String studentId = '"' + String.valueOf(studentInTest.getStudentId()) + '"';
		String testId = '"' + String.valueOf(studentInTest.getTestId()) + '"';
		String courseName = '"' + String.valueOf(studentInTest.getCourseName()) + '"';
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			stmt = conn.createStatement();
			for (int i = 0; i < studentInTest.getQesSize(); i++) {
				String answer = '"' + String.valueOf(studentInTest.getAnswer(i)) + '"';
				String quesId = '"' + String.valueOf(studentInTest.getQuesIdArr()[i]) + '"';
				String correctAns = '"' + String.valueOf(studentInTest.getTest().getqLst().get(i).getcAns()) + '"';
				str = "INSERT INTO `cems`.`students_answers` (`studentId`, `examId`, `courseName`,`quesId`, `answerNum`, `correctAnswer`) ";
				str += "VALUES (" + studentId + ',' + testId + ',' + courseName + ',' + quesId + ',' + answer + ',' + correctAns + ");";
				stmt.executeUpdate(str);
				str = "";
			}
			String str2 = "INSERT INTO `cems`.`grades` (`examId`, `studentId`, `courseID`, `grade`, `lecturerID`, `courseName`, `status`)";
			str2 += " VALUES ('" + studentInTest.getTestId() + "', '" + studentInTest.getStudentId() + "', '"
					+ studentInTest.getCourseId() + "', '" + studentInTest.getScore();
			str2 += "', '" + studentInTest.getLecturerId() + "', '" + studentInTest.getCourseName() + "','" + "pending"
					+ "');";
			stmt2 = conn.createStatement();
			stmt2.executeUpdate(str2);
			String str3 = "UPDATE `cems`.`student_inexam` SET `submitted` = '1' WHERE (`student_id` =" + studentId
					+ ") AND (`exam_id` = " + testId + ");";
			stmt3 = conn.createStatement();
			stmt3.executeUpdate(str3);
			String str4 = "SELECT (SELECT COUNT(*) FROM cems.student_inexam WHERE exam_id =" + testId
					+ ") AS total_rows,";
			str4 += "(SELECT COUNT(*) FROM cems.student_inexam WHERE exam_id =" + testId
					+ "AND submitted = 1) AS submitted_rows;";
			stmt4 = conn.createStatement();
			ResultSet rs,rs2,rs3;
			rs = stmt4.executeQuery(str4);
			if (rs.next()) {
				if (rs.getInt("total_rows") == rs.getInt("submitted_rows")) {
					int studentsnumber= rs.getInt("total_rows"); //number of students who did the exam
					String str6= "SELECT test_time,code FROM cems.open_exams where exam_id=" +testId   +" ;";
					stmt6= conn.createStatement();
					rs2=stmt6.executeQuery(str6);
					rs2.next();
					int duration=rs2.getInt("test_time");//test duration
					int code=rs2.getInt("code");//test code
					rs2.close();
					String str5 = "DELETE FROM cems.open_exams where exam_id =" + testId + ";";
					stmt5 = conn.createStatement();
					stmt5.executeUpdate(str5);
					LocalDateTime now = LocalDateTime.now();
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm");
			        String DateExamEnded = now.format(formatter);//date end
			        LocalDateTime before = LocalDateTime.now().minusMinutes(duration);
			        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm");
			        String DateExamStarted = before.format(formatter2); //date start
			       //this query returns the number of ques id's in test where 70% of students who did the exam 
			       //answered the same wrong answer, so if there is at least one ques id 
			       //there's a big chance of cheating 
			        String query = "SELECT COUNT(*) AS numRowsReturned " +
			        	    "FROM ( " +
			        	    "    SELECT quesId, answerNum, COUNT(*) AS answerCount, (SELECT COUNT(DISTINCT studentId) FROM students_answers WHERE examId ="+ testId +") AS totalStudents " +
			        	    "    FROM students_answers " +
			        	    "    WHERE examId ="+ testId +"AND answerNum <> correctAnswer " +
			        	    "    GROUP BY quesId, answerNum " +
			        	    "    HAVING answerCount >= 0.7 * totalStudents " +
			        	    ") AS subquery;";
			        stmt7=conn.createStatement();
			        rs3=stmt7.executeQuery(query);
			        int quesCheated =0;
			        if (rs3.next()) {
			        	quesCheated=rs3.getInt("numRowsReturned");
			        }
			        rs3.close();
			        int cheated=0;
			        //if 20% of questions are "Cheated" then cheated is assured
			        double percentage = (double) quesCheated / studentInTest.getQesSize() * 100;
			        if (percentage >= 35 & studentsnumber>1 & studentInTest.getQesSize() >2) {
			        	cheated=1; //if cheated=1 then there is cheating in the exam
			        }
			        String insertQuery = "INSERT INTO `cems`.`closed_exams` " +
		                     "(`exam_id`, `code`, `test_time`, `date_start`, `date_end`, `cheat`, `students_number` , `lecturer_id`) " +
		                     "VALUES ("+ testId +", '"+ code +"', '"+ duration + "', '"+ DateExamStarted +"', '"+DateExamEnded +"', '"+ cheated +"', '"+ studentsnumber + "' , '" + studentInTest.getLecturerId() + "');";
			        stmt8 = conn.createStatement();
					stmt8.executeUpdate(insertQuery);
				}
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void checkIfDurationChanged(TestSourceTime testSourcetime, ConnectionToClient client) {
		String str;
		int currentD = testSourcetime.getSourceTime();
		AddedTime added = new AddedTime();
		added.setAdded(0);
		str = "SELECT test_time FROM cems.open_exams WHERE exam_id=" + testSourcetime.getTestId() +" ;";
		Statement stmt;
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(str);
			if (rs.next()) {
				currentD = rs.getInt(1);
			}
			rs.close();
			if (currentD != testSourcetime.getSourceTime()) {
				added.setAdded(currentD - testSourcetime.getSourceTime());
				try {
					Response response = new Response("AddedTime", added);
					client.sendToClient(response);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				try {
					Response response = new Response("AddedTime", added);
					client.sendToClient(response);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Response response = new Response("AddedTime", added);
			client.sendToClient(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// gets test Data from DataBase and puts it in Test Object and returns it;
	public Test getTest(Connection conn, int testid) {
		ArrayList<Integer> studentsIdForTest = new ArrayList<>();
		Test test = null;
		Statement stmt;
		try {
			String id1 = String.valueOf(testid);
			String str = "SELECT * FROM exams e, courses c WHERE e.exam_id=" + id1
					+ " AND e.course_name = c.course_name;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(str);
			if (rs.next()) {
				test = new Test(rs.getString("course_name"), rs.getInt("test_time"), rs.getString("student_comments"),
						testid, rs.getInt("lecturer_id"), rs.getInt("course_id"));
			}
			rs.close();
			ResultSet rs2;
			str = "SELECT u.* FROM users u LEFT JOIN student_inexam s ON u.id = s.student_id AND s.exam_id =" + testid + " WHERE u.role = 0 AND s.student_id IS NULL;";
			Statement stmt2 = conn.createStatement();
			rs2 = stmt2.executeQuery(str);
			while (rs2.next()) {
				studentsIdForTest.add(rs2.getInt("id"));
			}
			rs2.close();
			if (test != null)
				test.setStudentsIdForTest(studentsIdForTest);
			return test;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return test;
	}

	// Checks if Test Applying info is existed in Database in Student Applying list
	// returns test Id if it does Exist, else -1
	public int ApplyingInfoExisted(Connection conn, TestCode t) {
		Statement stmt;
		int testid = -1;
		try {
			String code1 = '"' + String.valueOf(t.getCode()) + '"';
			String str = "SELECT * FROM open_exams list Where list.code=" + code1 + ";";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(str);
			if (rs.next())
				testid = rs.getInt("exam_id");
			rs.close();
			return testid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return testid;
	}

}
