package logic;

import java.io.Serializable;

public class TestCode implements Serializable {
	private int code;
	private int type;

	public int getType() {
		return type;
	}

	public int getCode() {
		return code;
	}

	public TestCode(int code,int type) {
		super();
		this.code = code;
		this.type=type;
	}
    
}


