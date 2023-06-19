/**
 * this Controller is for Digital Exam screen.
 */
package gui;

import java.io.IOException;
import client.ChatClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.AddedTime;
import logic.InTestQuestion;
import logic.Request;
import logic.StudentInTest;
import logic.Test;
import logic.TestSourceTime;
import logic.Users;
import java.util.Timer;
import java.util.TimerTask;

public class InExamController {
	private int remainingTime;
	private StudentInTest studentInTest;
	private ChatClient client;
	private Users student;
	private int questionIndex = 0;
	private Test test;
	private int selectedAns;
	private int answeredNum = 0;
	private boolean locked = false;
	private AddedTime added = new AddedTime();

	public AddedTime getAdded() {
		return added;
	}

	public void setAdded(AddedTime added) {
		this.added = added;
	}

	public void setTest(Test test) {
		this.test = test;

	}

	/**
	 * setting studentInTest object, this object for saving student answers
	 * and quesId for each answer , and test Info, and student info .
	 */
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

	public void setStudentAndClient(Users Student, ChatClient client, InExamController controller) {
		this.student = Student;
		this.client = client;
		this.client.setInExamController(controller);
	}

	@FXML
	private Label timeLabel;
	@FXML
	private Text ansNum;
	@FXML
	private ToggleGroup AnsNumber;

	@FXML
	private Text ans1Txt;

	@FXML
	private Text ans2Txt;

	@FXML
	private Text ans3Txt;

	@FXML
	private Text ans4Txt;

	@FXML
	private RadioButton ansBtn1;

	@FXML
	private RadioButton ansBtn2;

	@FXML
	private RadioButton ansBtn3;

	@FXML
	private RadioButton ansBtn4;

	@FXML
	private Label crsName;

	@FXML
	private Button nextBtn;

	@FXML
	private Button prevBtn;

	@FXML
	private Text qNum;

	@FXML
	private Text qSize;

	@FXML
	private Text qtxt;

	@FXML
	private Text scoreTxt;

	@FXML
	private Button subBtn;

	@FXML
	void GoToNextQuestion(ActionEvent event) {
		int answer;
		questionIndex++;
		if (questionIndex >= test.getQuesSize()) {
			questionIndex = 0;
		}
		if (selectedAns == 1) {
			ansBtn1.setSelected(false);
		}
		if (selectedAns == 2) {
			ansBtn2.setSelected(false);
		}
		if (selectedAns == 3) {
			ansBtn3.setSelected(false);
		}
		if (selectedAns == 4) {
			ansBtn4.setSelected(false);
		}
		answer = studentInTest.getAnswer(questionIndex);
		if (answer != 0) {
			if (answer == 1) {
				ansBtn1.setSelected(true);
			}
			if (answer == 2) {
				ansBtn2.setSelected(true);
			}
			if (answer == 3) {
				ansBtn3.setSelected(true);
			}
			if (answer == 4) {
				ansBtn4.setSelected(true);
			}
		}
		qtxt.setText(test.getqLst().get(questionIndex).getqTxt());
		ans1Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[0]);
		ans2Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[1]);
		ans3Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[2]);
		ans4Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[3]);
		qNum.setText((questionIndex + 1) + "");
		scoreTxt.setText("Points Number : " + test.getqLst().get(questionIndex).getScore());

	}

	@FXML
	void goToPreviousQuestion(ActionEvent event) {
		int answer;
		questionIndex--;
		if (questionIndex < 0) {
			questionIndex = test.getQuesSize() - 1;
		}
		if (selectedAns == 1) {
			ansBtn1.setSelected(false);
		}
		if (selectedAns == 2) {
			ansBtn2.setSelected(false);
		}
		if (selectedAns == 3) {
			ansBtn3.setSelected(false);
		}
		if (selectedAns == 4) {
			ansBtn4.setSelected(false);
		}
		answer = studentInTest.getAnswer(questionIndex);
		if (answer != 0) {
			if (answer == 1) {
				ansBtn1.setSelected(true);
			}
			if (answer == 2) {
				ansBtn2.setSelected(true);
			}
			if (answer == 3) {
				ansBtn3.setSelected(true);
			}
			if (answer == 4) {
				ansBtn4.setSelected(true);
			}
		}
		qtxt.setText(test.getqLst().get(questionIndex).getqTxt());
		ans1Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[0]);
		ans2Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[1]);
		ans3Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[2]);
		ans4Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[3]);
		qNum.setText((questionIndex + 1) + "");
		scoreTxt.setText("Points Number : " + test.getqLst().get(questionIndex).getScore());
	}

	@FXML
	void ansBtn1Clicked(ActionEvent event) {
		if (studentInTest.getAnswer(questionIndex) == 0) {
			answeredNum++;
		}
		studentInTest.SetAnswer(questionIndex, 1);
		selectedAns = 1;
		ansNum.setText(answeredNum + "");

	}

	@FXML
	void ansBtn2Clicked(ActionEvent event) {
		if (studentInTest.getAnswer(questionIndex) == 0) {
			answeredNum++;
		}
		studentInTest.SetAnswer(questionIndex, 2);
		selectedAns = 2;
		ansNum.setText(answeredNum + "");

	}

	@FXML
	void ansBtn3Clicked(ActionEvent event) {
		if (studentInTest.getAnswer(questionIndex) == 0) {
			answeredNum++;
		}
		studentInTest.SetAnswer(questionIndex, 3);
		selectedAns = 3;
		ansNum.setText(answeredNum + "");

	}

	@FXML
	void ansBtn4Clicked(ActionEvent event) {
		if (studentInTest.getAnswer(questionIndex) == 0) {
			answeredNum++;
		}
		studentInTest.SetAnswer(questionIndex, 4);
		selectedAns = 4;
		ansNum.setText(answeredNum + "");
	}

	@FXML
	void submit(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ApproveSubmit.fxml"));
		Parent root;
		try {

			root = loader.load();
			// Stage window = (Stage) subBtn.getScene().getWindow();
			Stage window = new Stage();
			ApproveSubmitController controller = loader.getController();
			// controller.setTest(test);
			controller.setStudentAndClient(student, client, studentInTest, this.getController(), test);
			controller.setDigOrMan(0);
			window.setScene(new Scene(root));
			window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * for closing the window
	 */
	public void CloseWindow() {
		Platform.runLater(() -> {
			Stage currentStage = (Stage) nextBtn.getScene().getWindow();
			currentStage.close();
		});
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
				controller.setStudentAndClient(student, client, studentInTest, this.getController(), test);
				controller.setDigOrMan(0);
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

	public InExamController getController() {
		return this;
	}

	public void setFirstPage() {
		// int timeInSeconds = 10;
		// startTimer(timeInSeconds);
		startTimer(test.getDuration() * 60, test.getDuration());
		questionIndex = 0;
		qtxt.setText(test.getqLst().get(questionIndex).getqTxt());
		ans1Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[0]);
		ans2Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[1]);
		ans3Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[2]);
		ans4Txt.setText(test.getqLst().get(questionIndex).getAnsTxt()[3]);
		qNum.setText((questionIndex + 1) + "");
		qSize.setText(test.getQuesSize() + "");
		scoreTxt.setText("Points Number : " + test.getqLst().get(questionIndex).getScore());
		crsName.setText(test.getCourseName() + " Test");
		// timeTxt.setText("Remaining Time: "+test.getDuration()+" M");
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

	private void startTimer(int timeInSeconds, int duration) {
		TestSourceTime testSourceTime = new TestSourceTime(test.getDuration(), test.getTestId());
		stopThread = false; // Reset stopThread flag
		timeThread = new Thread(() -> {
			try {
				remainingTime = timeInSeconds;
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

				// Timer has finished/*
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

	/** if test time ended and student didn't submit this function well be called */
	private void forceSubmit() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ApproveSubmit.fxml"));

		try {
			Parent root = loader.load();
			ApproveSubmitController controller = loader.getController();
			controller.setStudentAndClient(student, client, studentInTest, this.getController(), test);
			controller.setDigOrMan(0);
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

}
