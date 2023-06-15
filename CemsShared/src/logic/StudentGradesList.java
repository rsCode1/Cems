package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentGradesList  implements Serializable{
	private ArrayList<StudentData> gradesList;

	public ArrayList<StudentData> getGradeList() {
		return gradesList;
	}

	public void setGrad(ArrayList<StudentData> gradeList) {
		this.gradesList = gradeList;
	}

	public StudentGradesList(ArrayList<StudentData> gradeList) {
		super();
		this.gradesList = gradeList;
	} ;

}
