package gui;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import client.ChatClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Request;
import logic.StudentData;
import logic.StudentGradesList;
import logic.Users;



public class StudentHistoryController {

    private  StudentGradesList studentdata;
	private Users student;
	private ChatClient client;
    
	public void setStudentDataList(StudentGradesList  data) {
		this.studentdata=data;
	   }
	
    @FXML
    private TableView<StudentData> TestTableView;

    @FXML
    private Button btnBacktoHome;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblGPA;

    @FXML
    private TableColumn<StudentData, String> tbcCourse;

    @FXML
    private TableColumn<StudentData, String> tbcGrade;

    
    
    @FXML
    void ReturnBack(ActionEvent event) {
    	Stage currentStage = (Stage) btnBacktoHome.getScene().getWindow();
        currentStage.close();
    }

    
    public void setStudentAndClient(Users Student,ChatClient client,StudentHistoryController SH) {
    	this.student=Student;
    	this.client=client;
    	this.client.setStudentHistoryController(SH);
    }
    
  
   


    public void viewPage() {
    	
    	  ObservableList<StudentData> odata= FXCollections.observableArrayList();
    	  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");  
          LocalDateTime now = LocalDateTime.now();  
          lblDate.setText("Date : " + dtf.format(now));
		  /*TableView<StudentData> tableView = new TableView<>();
          TableColumn<StudentData, String> courseNameColumn = new TableColumn<>("Course");
          courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
          TableColumn<StudentData, String> gradeColumn = new TableColumn<>("Grade");
		  gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
		  tableView.getColumns().addAll(courseNameColumn, gradeColumn);*/
          if(studentdata==null)
				System.out.println("data is null");
			else
			for(int i=0 ; i<studentdata.getGradeList().size();i++) {
				odata.add(studentdata.getGradeList().get(0));}
    	  TestTableView.setItems(odata);
    	  CalculateGPA();
    	     
    }
    
    
    
    
    public void CalculateGPA()
    {
    	 ObservableList<StudentData> data=TestTableView.getItems();
    	 double total=0;
    	 int count=0;
    	 int tempGrade=0;
    	 
    	 for (StudentData sd : data)
    	 {
    		 if (sd.getGrade()!="-/-")
    		 {
    		     tempGrade=Integer.parseInt(sd.getGrade());
    			 total+=tempGrade;
    			 count++;
    		 }
    	 }
    	 double average = (count > 0) ? total / count : 0.0;
    	    lblGPA.setText("Average GPA: " + average);
    }
    
    public StudentHistoryController getController() {
		return this;
	}
    
}
