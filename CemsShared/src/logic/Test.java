package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Test implements Serializable{
	private Question[] qLst ;
	private String courseName;
	private int testId;
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
	public Question[] getqLst() {
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
	public String getStudentNotes() {
		return studentNotes;
	}
	private int duration;
	private String studentNotes;
	private int quesSize;
	public Test( String courseName, int duration, String studentNotes, int quesSize,int testId) {
		super();
		this.courseName = courseName;
		this.duration = duration;
		this.studentNotes = studentNotes;
		this.quesSize = quesSize;
		this.testId=testId;
	}
	public void setQLst(Question[] qLst) {
		this.qLst=qLst;
		
	}
	

}
