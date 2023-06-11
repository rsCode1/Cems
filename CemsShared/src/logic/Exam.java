package logic;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class Exam implements Serializable {

    private String courseName;
    private String subject;
    ArrayList<Question>  questions;
    private Users lecturer;
    private int testTime;
    private String lecturerComments;
    private String studentComments;


    public Exam( String courseName, ObservableList<Question> questions, Users lecturer, int testTime, String subject) {
        this.courseName = courseName;
        this.questions = new ArrayList<>(questions);;
        this.lecturer = lecturer;
        this.testTime = testTime;
        this.subject = subject;
    }

    public String getCourseName() {
        return courseName;
    }
    public ArrayList<Question> getQuestions() {
        return questions;
    }
    public Users getLecturer() {
        return lecturer;
    }
    public int getTestTime() {
        return testTime;
    }
    public String getSubject() {
        return subject;
    }

    public void setLecturerComments(String lecturerComments) {
        this.lecturerComments = lecturerComments;
    }
    public void setStudentComments(String studentComments) {
        this.studentComments = studentComments;
    }
    public String getLecturerComments() {
        return lecturerComments;
    }
    public String getStudentComments() {
        return studentComments;
    }

}
