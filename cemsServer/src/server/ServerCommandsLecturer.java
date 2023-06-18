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
import logic.Question;
import logic.RequestTime;
import logic.Response;
import logic.Users;
import ocsf.server.ConnectionToClient;

public class ServerCommandsLecturer {

    public void getPastExams(ConnectionToClient client, Users lecturer) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
                    "Aa123456");
            System.out.println("SQL connection succeed Ongoing Exams!");
            // create Table, if exist skip
            String command1 = "SELECT exam_id, code, test_time ,date_start,date_end,cheat,students_number "
                    + "FROM closed_exams "
                    + "WHERE closed_exams.lecturer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(command1);
            stmt.setInt(1, lecturer.getId());

            ResultSet rs = stmt.executeQuery();
            ArrayList<Exam> exams = new ArrayList<>();
            while (rs.next()) {
                int examId = rs.getInt("exam_id");
                int code = rs.getInt("code");
                int testTime = rs.getInt("test_time");
                String dateStart = rs.getString("date_start");
                String dateEnd = rs.getString("date_end");
                int cheat = rs.getInt("cheat");
                int studentsNumber = rs.getInt("students_number");
                Exam exam = new Exam(examId, code, testTime, dateStart, dateEnd, cheat, studentsNumber);
                exams.add(exam);
            }
            Response response = new Response("getPastExams", exams);
            client.sendToClient(response);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void requestTime(ConnectionToClient client, RequestTime requestTime) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
                    "Aa123456");
            System.out.println("SQL connection succeed");
            String command = "INSERT INTO requests (examID,RequestedBy,Reason,extraTime) VALUES (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(command);
            stmt.setString(1, requestTime.getExamID());
            stmt.setString(2, requestTime.getRequestedBy());
            stmt.setString(3, requestTime.getReason());
            stmt.setInt(4, requestTime.getExtraTime());
            stmt.executeUpdate();
            Response response = new Response("requestTimeSuccess", null);
            client.sendToClient(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeGrade(ConnectionToClient client, Exam exam) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
                    "Aa123456");
            System.out.println("SQL connection succeed");
            String command = "UPDATE grades SET status='approved',notes = ?,grade = ? WHERE examId = ? and studentId = ? and lecturerID = ?";
            PreparedStatement stmt = conn.prepareStatement(command);
            stmt.setString(1, exam.getNotesForChange());
            stmt.setInt(2, exam.getGrade());
            stmt.setInt(3, exam.getExamId());
            stmt.setInt(4, exam.getStudentId());
            stmt.setInt(5, exam.getLecturer().getId());
            stmt.executeUpdate();
            Response response = new Response("changeGradeSuccess", null);
            client.sendToClient(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void approveGrade(ConnectionToClient client, Exam exam) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
                    "Aa123456");
            System.out.println("SQL connection succeed");
            String command = "UPDATE grades SET status='approved' WHERE examId = ? and studentId = ? and lecturerID = ?";
            PreparedStatement stmt = conn.prepareStatement(command);
            stmt.setInt(1, exam.getExamId());
            stmt.setInt(2, exam.getStudentId());
            stmt.setInt(3, exam.getLecturer().getId());
            stmt.executeUpdate();
            Response response = new Response("approveGradeSuccess", null);
            client.sendToClient(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getGrades(ConnectionToClient client, Users lecturer) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
                    "Aa123456");
            System.out.println("SQL connection succeed");
            String command=null;
            if (lecturer.getFlag()==1){

                command = "SELECT examId,studentId,grade,courseName FROM grades WHERE status='approved' and lecturerID = ?";
            }else{
                command = "SELECT examId,studentId,grade,courseName FROM grades WHERE status!='approved' and lecturerID = ?";
            }
            
            PreparedStatement stmt = conn.prepareStatement(command);
            stmt.setInt(1, lecturer.getId());
            ResultSet rs = stmt.executeQuery();
            ArrayList<Exam> exams = new ArrayList<>();
            while (rs.next()) {
                Exam exam = new Exam(rs.getInt("examId"), rs.getInt("studentId"), rs.getString("courseName"),
                        rs.getInt("grade"), lecturer);
                exams.add(exam);
            }
            Response response = new Response("getGrades", exams);
            client.sendToClient(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startExam(ConnectionToClient client, Exam exam) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
                    "Aa123456");
            System.out.println("SQL connection succeed");
            String checkCodeCommand = "SELECT code FROM open_exams WHERE exam_id = ?";
            PreparedStatement stmt = conn.prepareStatement(checkCodeCommand);
            stmt.setInt(1, exam.getExamId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
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

    public void getOngoingExams(ConnectionToClient client, Users lecturer) {
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

    public void getExamsByLecturer(ConnectionToClient client, Users lecturer) {
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
    public void saveExam(Exam exam, ConnectionToClient client) {
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

    public void getQuestions(ConnectionToClient client, String course) {

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
    public void getCourses(ConnectionToClient client, String subject) {
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

    public void getSubjects(ConnectionToClient client) {

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

    public void writeQuestion(Question question, ConnectionToClient client) {
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
}
