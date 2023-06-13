package logic;

import java.io.InputStream;
import java.io.Serializable;



public class DownloadManualExaminController implements Serializable {
	private InputStream inputStream ;

	public DownloadManualExaminController(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
