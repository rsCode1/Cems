package gui;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import logic.Exam;
import logic.Question;

class createExamControllerTest {
	Exam expectedexam;
	Question qustion;
	 ObservableList<Question> ques = FXCollections.observableArrayList();
	
	   @Test
	    void CreateExamTest_NoQuestions() {
		  new JFXPanel();
		  
	        createExamController createExam = new createExamController();
	        createExam.setTestTime("60");
	      
	        ObservableList<Question> ques = FXCollections.observableArrayList();
	        createExam.setQuestions(ques);
	        createExam.reviewExam(null);
	        assertEquals("fail", createExam.getStatus());
	    }
	  @Test
	    void CreateExamTest_NoTime() {
		  new JFXPanel();
	        createExamController createExam = new createExamController();
	        ObservableList<Question> ques = FXCollections.observableArrayList();
	        ques.add(new Question("2+2?", "4", "2", "3", "0", 1, 1, 2, "Jane Smith"));
	        createExam.setQuestions(ques);
	        createExam.reviewExam(null);
	        assertEquals("fail", createExam.getStatus());
	    }
	  
	  @Test
	    void CreateExamTest_NoScore() {
		  new JFXPanel();
	        createExamController createExam = new createExamController();
	        createExam.setTestTime("60");
	        ObservableList<Question> ques = FXCollections.observableArrayList();
	        createExam.setQuestions(ques);
	        createExam.reviewExam(null);
	        assertEquals("fail", createExam.getStatus());
	    }
	   @Test
	    void CreateExamTest_SuccssesTest() {
		  new JFXPanel();
		  
	        createExamController createExam = new createExamController();
	        createExam.setTestTime("60");
	        createExam.setScoreSum(100);
	        ObservableList<Question> ques = FXCollections.observableArrayList();
	        ques.add(new Question("2+2?", "4", "2", "3", "0", 1, 1, 2, "Jane Smith"));
	        createExam.setQuestions(ques);
	        createExam.reviewExam(null);
	        assertEquals("The test is ready", createExam.getStatus());
	    }
	
	
	
}
