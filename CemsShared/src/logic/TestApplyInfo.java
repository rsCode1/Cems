package logic;

import java.io.Serializable;

public class TestApplyInfo  implements Serializable {
	private int id;
	private int code;

	public TestApplyInfo( int code ,int id) {
		super();
		this.code = code;
		this.id=id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}
	public int getCode() {
		return code;
	}

}
