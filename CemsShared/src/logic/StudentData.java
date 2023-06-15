package logic;

import java.io.Serializable;

public class StudentData implements Serializable {

	private String CourseName;
    private int StudentID;
    private String grade;
    private String status;
	
	public StudentData(String cname,int studentid, String grade) {
		super();
		this.CourseName=cname;
		this.StudentID=studentid;
		this.grade=grade;
	}




	public String getCourseName() {
		return CourseName;
	}

	public void setCourseName(String courseName) {
		CourseName = courseName;
	}


	public int getStudentID() {
		return StudentID;
	}

	public void setStudentID(int studentID) {
		StudentID = studentID;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
