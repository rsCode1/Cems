package logic;

import java.io.Serializable;

public class TestSourceTime implements Serializable {
	private int sourceTime;
	public TestSourceTime(int sourceTime, int testId) {
		super();
		this.sourceTime = sourceTime;
		this.testId = testId;
	}
	private int testId;
	
	public int getSourceTime() {
		return sourceTime;
	}
	public void setSourceTime(int sourceTime) {
		this.sourceTime = sourceTime;
	}
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
	

	
}
