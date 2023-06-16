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

import logic.Users;



public class StudentHistoryController {

    private ArrayList<StudentData> studentGradesInfo = new ArrayList<>();
	private Users student;
	private ChatClient client;
    
	public void setStudentDataList(ArrayList<StudentData>  data) {
		this.studentGradesInfo=data ;
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
    	try {
    		client.openConnection();
		     //System.out.println("client is not connected");
				 ObservableList<StudentData> odata= FXCollections.observableArrayList();
		    	  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");  
		          LocalDateTime now = LocalDateTime.now();  
		          lblDate.setText("Date : " + dtf.format(now));
		          tbcCourse.setCellValueFactory(new PropertyValueFactory<>("CourseName"));
		          tbcGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
				  for(int i=0 ; i<studentGradesInfo.size();i++) {
						odata.add(studentGradesInfo.get(i));
				  }
		    	  TestTableView.setItems(odata);
		    	  CalculateGPA();
		    	     
			}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
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
    			 if( !sd.getGrade().matches("[0-9]+")) {
    				 tempGrade=0;
    			 }
    			 else {
    				 tempGrade=Integer.parseInt(sd.getGrade());
    			 }
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
