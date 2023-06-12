package gui;

import java.sql.*;

import client.ChatClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.StudentData;
import logic.Users;



public class StudentHistoryController {

	
	private Users student;
	private ChatClient client;

	
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
    private TableColumn<StudentData, Integer> tbcTest;
    
    
    public void setStudentAndClient(Users Student,ChatClient client) {
    	this.student=Student;
    	this.client=client;
    }
    
    ObservableList<StudentData> data= FXCollections.observableArrayList();

    public void initialize() {

    	 try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "username", "password")) {
            
    		 String query="SELECT course,testName,grade FROM testcompletebystudent WHERE status='completed' AND StudentID=?";
    		 PreparedStatement statement = connection.prepareStatement(query);
    	        statement.setInt(1, this.student.getId());
    	        ResultSet resultSet = statement.executeQuery();
    	        while (resultSet.next()) {
    	            String column1Value = resultSet.getString("testName");
    	            String column2Value = resultSet.getString("course");
    	            int column3Value = resultSet.getInt("grade");
    	            data.add(new StudentData(column2Value, column1Value,this.student.getId(),column3Value));
    	        }
    	        TestTableView.setItems(data);
    	 } catch (SQLException e) {
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
    		 tempGrade=sd.getGrade();
    		 if (tempGrade!=-1)
    		 {
    			 total+=tempGrade;
    			 count++;
    		 }
    	 }
    	 double average = (count > 0) ? total / count : 0.0;
    	    lblGPA.setText("Average GPA: " + average);
    }
    
}
