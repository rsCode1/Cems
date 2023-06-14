package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Test implements Serializable{
	private ArrayList<Question> qLst ;
	private String courseName;
	private int testId;
	private int lecturerId;
	private ArrayList<Integer> studentsIdForTest = new ArrayList<>();
	public ArrayList<Integer> getStudentsIdForTest() {
		return studentsIdForTest;
	}
	public void setStudentsIdForTest(ArrayList<Integer> studentsIdForTest) {
		this.studentsIdForTest = studentsIdForTest;
	}
	public int getTestId() {
		return testId;
	}
	public ArrayList<Question> getqLst() {
		return qLst;
	}
	public String getCourseName() {
		return courseName;
	}

	public int getDuration() {
		return duration;
	}
	public int getQuesSize() {
		return quesSize;
	}
	public int getLecturerId() {
        return lecturerId;
    }
	public String getStudentNotes() {
		return studentNotes;
	}
	private int duration;
	private String studentNotes;
	private int quesSize;
	public Test( String courseName, int duration, String studentNotes, int testId , int lecturerId) {
		super();
		this.courseName = courseName;
		this.duration = duration;
		this.studentNotes = studentNotes;
		this.testId=testId;
		this.lecturerId=lecturerId;
	}
	public void setQLst(ArrayList<Question> qLst) {
		this.qLst=qLst;
		
	}
	public void setquesSize(int quesSize) {
		this.quesSize=quesSize;
		
	}
	

}
