package unitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gui.createExamController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.Exam;
import logic.Question;

//testing functionality of create exam controller on the client .
class CreateExamControllerTest {


	private Question qustion;
	private ObservableList<Question> ques = FXCollections.observableArrayList();
	private createExamController createExam;
	private ObservableList<Question> quesEmpty;

	//set up before each test , create question and add it to the list of questions,create createExamController
	@BeforeEach
	void setUp(){
		qustion = new Question("2+2?", "4", "2", "3", "0", 1, 1, 2, "Jane Smith");
		ques.add(qustion);
		createExam = new createExamController();
		quesEmpty = FXCollections.observableArrayList();
	}
	 
	// test gui of CreateExam screen and the logic of the controller with  no question 
	// input exam controller with no question
	// expected : status of exam to be created is "fail",no exam can be created with
	// no question
	   @Test
	    void CreateExamTest_NoQuestions() {
		  new JFXPanel();
	        createExam.setTestTime("60");
	        createExam.setErrLabel(new Label(""));
	        createExam.setQuestions(quesEmpty);
	        createExam.reviewExam(null);
	        assertEquals("fail", createExam.getStatus());
	    }

	// test gui of CreateExam screen and the logic of the controller with question and no time
	// input exam controller with question and time no time
	// expected : status of exam to be created is "fail",no exam can be created with
	// no time
	  @Test
	    void CreateExamTest_NoTime() {
		  new JFXPanel();
	        createExam.setQuestions(ques);
	        createExam.setErrLabel(new Label(""));
			createExam.setSetTimeTextField(new TextField(""));
	        createExam.reviewExam(null);
	        assertEquals("fail", createExam.getStatus());
	    }
	// test gui of CreateExam screen and the logic of the controller with no score
	// input exam controller with question and time no score
	// expected : status of exam to be created is "fail",no test can be created with
	// no score
	  @Test
	    void CreateExamTest_NoScore() {
		  new JFXPanel();
	        createExamController createExam = new createExamController();
	        createExam.setTestTime("60");
	        createExam.setErrLabel(new Label(""));
	        ObservableList<Question> ques = FXCollections.observableArrayList();
	        createExam.setQuestions(ques);
			createExam.setSetTimeTextField(new TextField("60"));
	        createExam.reviewExam(null);
	        assertEquals("fail", createExam.getStatus());
	    }


	// test gui of CreateExam screen and the logic of the controller with correct exam details
	// input exam controller with correct exam details
	// expected : status of exam to be created is "The test is ready",exam can be created.
	   @Test
	    void CreateExamTest_SuccssesTest() {
		  new JFXPanel();
	        createExam.setTestTime("60");
	        createExam.setErrLabel(new Label(""));
	        createExam.setScoreSum(100);
	        createExam.setQuestions(ques);
			createExam.setSetTimeTextField(new TextField("60"));
			createExam.setScoreSum(100);
	        createExam.reviewExam(null);
	        assertEquals("The test is ready", createExam.getStatus());
	    }
}
