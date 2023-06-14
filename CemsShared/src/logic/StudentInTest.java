package logic;

import java.io.Serializable;

public class StudentInTest implements Serializable {
	private int studentId;
	private int testId;
	private String CourseName;
	private int [] ansArr;
	private int score=0;
	private int qesSize;
	private int [] quesIdArr;
	private int lecturerId;
	
	public int[] getQuesIdArr() {
		return quesIdArr;
	}

	public void setQuesId(int[] quesIdArr) {
		this.quesIdArr = quesIdArr;
	}

	public StudentInTest( int studentId ,String CourseName,int qesSize,int testId ,int lecturerId) {
		this.studentId = studentId;
		this.CourseName = CourseName;
		this.qesSize = qesSize;
		this.testId= testId;
		this.lecturerId=lecturerId;
		ansArr= new int [qesSize];
		for(int i=0 ; i< qesSize; i++) {
			ansArr[i]=0;
		}
		quesIdArr = new int [qesSize];
	}
	public int getScore() {
			return score;
		}
	public void setScore(int score) {
		this.score=score;
	}
	public int getLecturerId() {
        return lecturerId;
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
