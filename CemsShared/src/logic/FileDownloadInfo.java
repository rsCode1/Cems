package logic;

import java.io.Serializable;

public class FileDownloadInfo implements Serializable {
	private String fileDownloadPath;
	private int courseId;
	
	public FileDownloadInfo(String fileDownloadPath, int courseId) {
		super();
		this.fileDownloadPath = fileDownloadPath;
		this.courseId = courseId;
	}
	public String getFileDownloadPath() {
		return fileDownloadPath;
	}
	public void setFileDownloadPath(String fileDownloadPath) {
		this.fileDownloadPath = fileDownloadPath;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	
	

}
