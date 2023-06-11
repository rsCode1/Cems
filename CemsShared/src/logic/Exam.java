package logic;

import javafx.collections.ObservableList;

public class Exam {

    private String courseName;
    private String subject;
    private ObservableList<Question> questions;
    private Users lecturer;
    private int testTime;


    public Exam( String courseName, ObservableList<Question> questions, Users lecturer, int testTime, String subject) {
        this.courseName = courseName;
        this.questions = questions;
        this.lecturer = lecturer;
        this.testTime = testTime;
        this.subject = subject;
    }

    public String getCourseName() {
        return courseName;
    }
    public ObservableList<Question> getQuestions() {
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

}
