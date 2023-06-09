package logic;

import java.io.Serializable;

public class StudentInTest implements Serializable {
	private int studentId;
	private int testId;
	private String CourseName;
	private int [] ansArr;
	private int qesSize;
	private int [] quesIdArr;
	
	public int[] getQuesIdArr() {
		return quesIdArr;
	}

	public void setQuesId(int[] quesIdArr) {
		this.quesIdArr = quesIdArr;
	}

	public StudentInTest( int studentId ,String CourseName,int qesSize,int testId ) {
		this.studentId = studentId;
		this.CourseName = CourseName;
		this.qesSize = qesSize;
		this.testId= testId;
		ansArr= new int [qesSize];
		for(int i=0 ; i< qesSize; i++) {
			ansArr[i]=0;
		}
		quesIdArr = new int [qesSize];
	}

	public int getStudentId() {
		return studentId;
	}

	public int getTestId() {
		return testId;
	}

	public String getCourseName() {
		return CourseName;
	}

	public int[] getAnsArr() {
		return ansArr;
	}

	public int getQesSize() {
		return qesSize;
	}

	public void SetAnswer(int index, int answerNum) {
		this.ansArr[index]=answerNum;
		
	}
	public int getAnswer(int index) {
		return this.ansArr[index];
		
	}
	
}
