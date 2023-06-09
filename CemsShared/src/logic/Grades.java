package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Grades implements Serializable  {
	String examID;
	String studentID;
	String courseID;
	int grade;
	int dataOf;//student=0,lectuer=1,course=2

	
	public String getExamID() {
		return examID;
	}
	public void setExamID(String examID) {
		this.examID = examID;
	}
	public String getStudentID() {
		return studentID;
	}
	
	public String getCourseID() {
		return courseID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public int getDataOf() {
		return dataOf;
	}
	public void setDataOf(int dataOf) {
		this.dataOf = dataOf;
	}
	public int getGrade() {
		return grade;
	}
	public Grades(String examID, String studentID, String courseID, int grade,int dataOf) {
		super();
		this.examID = examID;
		this.studentID = studentID;
		this.courseID = courseID;
		this.grade = grade;
		this.dataOf=dataOf;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	
	public String getCourseName() {
		return courseID;
	}
	public void setCourseName(String courseName) {
		this.courseID = courseName;
	}
	@Override
	public String toString() {
		return "Grades [examID=" + examID + ", studentID=" + studentID + ", courseID=" + courseID + ", grade=" + grade
				+ "]";
	}
	


}
