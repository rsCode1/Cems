package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.AddedTime;


import logic.MyFile;
import logic.Request;
import logic.StudentInTest;
import logic.Test;
import logic.TestSourceTime;
import logic.UploadFile;
import logic.Users;

import java.util.Timer;
import java.util.TimerTask;

import client.ChatClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.sql.Blob;

/**
 * this controller is for Manual exam screen
 */
public class StudentManualTestController {
	private StudentInTest studentInTest;

	private MyFile downloadFile;
	private UploadFile uploadFile;

	public void setDownloadFile(MyFile downloadFile) {
		this.downloadFile = downloadFile;
	}

	private String downloadPath;
	private String ToUploadPath;
	private int remainingTime;
	private boolean StartedTime = false;
	private Test test;
	private ChatClient client;
	private Users student;
	private boolean locked = false;
	private AddedTime added = new AddedTime();

	public void setStudentInTest() {
		this.studentInTest = new StudentInTest(student.getId(), test.getCourseName(), test.getQuesSize(),
				test.getTestId(), test.getLecturerId(), test.getCourseId());
		int[] quesId = new int[test.getQuesSize()];
		for (int i = 0; i < test.getQuesSize(); i++) {
			quesId[i] = test.getqLst().get(i).getQuesId();
		}
		this.studentInTest.setQuesId(quesId);
		this.studentInTest.setTest(test);
	}

	public AddedTime getAdded() {
		return added;
	}

	public void setStudentAndClient(Users Student, ChatClient client, StudentManualTestController controller) {
		this.student = Student;
		this.client = client;
		this.client.setStudentManualTestController(controller);
	}

	public void setAdded(AddedTime added) {
		this.added = added;
	}

	public void setTest(Test test) {
		this.test = test;

	}

	@FXML
	private Text selectedUpFileLbl;
	@FXML
	private Text uploadMes;

	@FXML
	private Label downloadMes;
	@FXML
	private Button subBtn;
	@FXML
	private Label crsName;
	@FXML
	private Button download;
	@FXML
	private Button upload;
	@FXML
	private Label timeLabel;
	@FXML
	private Label selectedFileLabel;

	private File selectedFile;

	/**
	 * when student clicks on submit button this func will be called,
	 * by the upload path he choose the methood will convert the file to Myfile
	 * object
	 * and sends it to ApproveSubmit screen
	 */
	@FXML
	void submit(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ApproveSubmit.fxml"));
		Parent root;
		try {
			root = loader.load();
			File newFile = new File(ToUploadPath);
			uploadFile = new UploadFile(new MyFile(newFile.getName()), student.getId(), test.getTestId());
			uploadFile.setStudentInTest(studentInTest);
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			uploadFile.getMyfile().initArray(mybytearray.length);
			uploadFile.getMyfile().setSize(mybytearray.length);
			bis.read(uploadFile.getMyfile().getMybytearray(), 0, mybytearray.length);
			Stage window = new Stage();
			ApproveSubmitController controller = loader.getController();
			controller.setStudentAndClient2(student, client, this.getController());
			controller.setDigOrMan(1);
			controller.setAnswersFile(uploadFile);
			window.setScene(new Scene(root));
			window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void CloseWindow() {
		Platform.runLater(() -> {
			Stage currentStage = (Stage) upload.getScene().getWindow();
			currentStage.close();
		});
	}

	public void setWelcomeLabel() {
		crsName.setText(test.getCourseName() + " Test");
		subBtn.setDisable(true);
		selectedFileLabel.setText("Selected Directory: not selected yet");
		downloadMes.setText("Download status: not yet");
		uploadMes.setText("Upload status: not yet");
		selectedUpFileLbl.setText("Selected File: not selected yet");

	}

	/**
	 * this method will activate when student clicks on downlaod exam button,
	 * he will choose the path of the directory he want to download,
	 */
	@FXML
	void click_download(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Download Directory");
		File selectedDirectory = directoryChooser.showDialog(null);

		if (selectedDirectory != null) {
			String selectedDirectoryPath = selectedDirectory.getAbsolutePath();
			selectedFileLabel.setText("Selected Directory: " + selectedDirectoryPath);
			String LocalfilePath = selectedDirectory.getPath();
			if (LocalfilePath.equals("C:\\") | LocalfilePath.equals("D:\\") | LocalfilePath.equals("F:\\")) {
				downloadMes.setText("Download status:Error Please choose folder path!");
			} else {
				LocalfilePath += "\\" + "TestId_" + test.getTestId() + "-StId_" + student.getId() + ".docx";
				downloadPath = LocalfilePath;

				// }
				try {
					client.openConnection();
					if (client.isConnected()) {
						client.sendToServer(new Request("DownloadManualExam", test.getTestId()));
						try {
							Thread.sleep(70);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (downloadFile != null) {
							try {
								File newFile = new File(LocalfilePath);
								FileOutputStream fis = new FileOutputStream(newFile);
								BufferedOutputStream bis = new BufferedOutputStream(fis);
								bis.write((downloadFile).getMybytearray(), 0, (downloadFile).getSize());
								bis.close();
								System.out.println("File downloaded successfully!");
								downloadMes.setText("Download status: downloaded successfully!");
								if (StartedTime == false) {
									startTimer(test.getDuration() * 60, test.getDuration());
									StartedTime = true;
								}
							} catch (Exception e) {
								System.out.println("Error getting file from Server");
								downloadMes.setText("Download status: Unexpected Error please contact the lectuer");
							}
						} else {
							System.out.println("Error getting file from Server");
							downloadMes.setText("Download status: Unexpected Error please contact the lectuer");
						}

					}

				} // endof try
				catch (IOException e) {
					e.printStackTrace();
				}

			} // endofif
		}

	}

	/**
	 * Before submitting this method is for choosing the answers file path for
	 * submitting
	 */
	@FXML
	void click_upload(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Modified File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Documents", "*.doc", "*.docx"));
		selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			ToUploadPath = selectedFile.getPath();
			selectedFileLabel.setText("Selected File: " + ToUploadPath);
			uploadMes.setText(selectedFile.getName() + " uploaded successfully!");
			subBtn.setDisable(false);
		}
	}

	private void updateTimerLabel(int timeInSeconds) {
		// Convert seconds to minutes and seconds
		int minutes = timeInSeconds / 60;
		int seconds = timeInSeconds % 60;

		String timeText = String.format("%02d:%02d", minutes, seconds);
		// Update timeLabel on the FX application thread
		Platform.runLater(() -> timeLabel.setText(timeText));
	}

	private volatile boolean stopThread = false;
	private Thread timeThread;
	boolean addedTime = false;

	/**
	 * this methood is for starting thread timer of the exam
	 * it recieves the exam duration in minutes and the exam time in seconds
	 * 
	 * @param timeInSeconds
	 * @param duration
	 */
	private void startTimer(int timeInSeconds, int duration) {
		TestSourceTime testSourceTime = new TestSourceTime(test.getDuration(), test.getTestId());
		stopThread = false; // Reset stopThread flag
		timeThread = new Thread(() -> {
			try {
				remainingTime = timeInSeconds + 60;
				while (remainingTime >= 0 && !stopThread) {
					if (remainingTime <= 5 && addedTime == false) {
						// checkIfDurationChanged
						// if yes then
						// addedTime=true;
						try {
							client.openConnection();
							if (client.isConnected()) {
								client.sendToServer(new Request("CheckIfDurationChanged", testSourceTime));
								if (added.getAdded() != 0) {
									remainingTime += added.getAdded() * 60;
									addedTime = true;
								}
							} else {
								System.out.println("Not connected to server.");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					updateTimerLabel(remainingTime);
					Thread.sleep(1000);
					remainingTime--;
				}

				// Timer has finished
				if (!stopThread) {
					subBtn.setDisable(true);
					forceSubmit();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		timeThread.start();
	}

	public void stopTimer() {
		stopThread = true;
		remainingTime = 0;
		updateTimerLabel(remainingTime);
	}

	/**
	 * when the exam time ended , and the student didnt submit this function will be
	 * called
	 */
	private void forceSubmit() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ApproveSubmit.fxml"));

		try {
			Parent root = loader.load();
			UploadFile forceUpload = new UploadFile(null, student.getId(), test.getTestId());
			forceUpload.setStudentInTest(studentInTest);
			ApproveSubmitController controller = loader.getController();
			controller.setStudentAndClient2(student, client, this.getController());
			controller.setDigOrMan(1);
			controller.setAnswersFile(forceUpload);
			controller.forceSubmit();
			Platform.runLater(() -> {
				Stage window = new Stage();
				window.setScene(new Scene(root));
				window.show();
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this function is userd when the lecturer request to lock the exam
	 * 
	 * @param testid
	 */
	public void LockExam(int testid) {
		if (testid == test.getTestId()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ApproveSubmit.fxml"));
			try {
				Parent root = loader.load();
				ApproveSubmitController controller = loader.getController();
				controller.setStudentAndClient2(student, client, this.getController());
				controller.setDigOrMan(1);
				controller.examIsLocked();
				Platform.runLater(() -> {
					Stage window = new Stage();
					window.setScene(new Scene(root));
					window.show();
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public StudentManualTestController getController() {
		return this;
	}

}
