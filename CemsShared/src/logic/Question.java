package logic;

import java.io.Serializable;

public class Question implements Serializable{
	
	private String qTxt;
	private String [] ansTxt = new String[4];
	private int score;
	private int quesId;
	
	public Question(String qTxt, String[] ansTxt,int score,int quesId) {
		this.qTxt = qTxt;
		this.ansTxt = ansTxt;
		this.score=score;
		this.quesId=quesId;
	}
	

	public int getQuesId() {
		return quesId;
	}


	public String getqTxt() {
		return qTxt;
	}
	public String[] getAnsTxt() {
		return ansTxt;
	}
	public int getScore() {
		return score;
	}
	
	
	
	

}
