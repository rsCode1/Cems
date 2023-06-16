package logic;

import java.io.Serializable;

public class InTestQuestion implements Serializable{
	
	private String qTxt;
	private String [] ansTxt = new String[4];
	private int score;
	private int quesId;
	private int cAns;
	public InTestQuestion(String qTxt, String[] ansTxt,int score,int quesId,int cAns) {
		this.qTxt = qTxt;
		this.ansTxt = ansTxt;
		this.score=score;
		this.quesId=quesId;
		this.cAns=cAns;
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
	public int getcAns() {
			return cAns;
		}
	
	
	
	

}
