package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Grades implements Serializable, Comparable<Grades> {
	String examID;
	String studentID;
	String courseID;
	int grade;
	String lectuerID;

	public String getLectuerID() {
		return lectuerID;
	}

	public void setLectuerID(String lectuerID) {
		this.lectuerID = lectuerID;
	}

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

	public int getGrade() {
		return grade;
	}

	public Grades(String examID, String studentID, String courseID, int grade, String lectuerID) {
		super();
		this.examID = examID;
		this.studentID = studentID;
		this.courseID = courseID;
		this.grade = grade;
		this.lectuerID = lectuerID;
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

	@Override
	public int compareTo(Grades o) {

		if (this.getGrade() == (o.getGrade()) && (this.getStudentID().equals(o.getStudentID()))
				&& (this.getCourseName().equals(o.getCourseName()))
				&& (this.getLectuerID().equals(o.getLectuerID()) && this.getExamID().equals(o.getExamID())))
			return 1;
		else
			return 0;
	}

}
