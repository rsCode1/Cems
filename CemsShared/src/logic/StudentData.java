package logic;

import java.io.Serializable;

public class StudentData implements Serializable {

	private String CourseName;
	private String TestName;
    private int StudentID;
    private String grade;
    private String status;
	
	public StudentData(String cname,String tname,int studentid, String column3Value) {
		super();
		this.CourseName=cname;
		this.TestName=tname;
		this.StudentID=studentid;
		this.grade=column3Value;
	}




	public String getCourseName() {
		return CourseName;
	}

	public void setCourseName(String courseName) {
		CourseName = courseName;
	}


	public String getTestName() {
		return TestName;
	}

	public void setTestName(String testName) {
		TestName = testName;
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
