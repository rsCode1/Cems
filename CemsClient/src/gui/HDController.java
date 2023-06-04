package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.RequestTime;
import ocsf.client.*;

public class HDController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button LogOutBTN;

    public Button getLogOutBTN() {
        return LogOutBTN;
    }
    @FXML
    private TableView<RequestTime> requestTable;

    @FXML
    private Button ApproveBTN;

    @FXML
    private Button rejectBTN;

    @FXML
    private Button ShowStatisticsBTN;

    @FXML
    private TextField GPA_LECtextArea;

    @FXML
    private TextField Median_LECTextArea;

    @FXML
    private Button ShoeBTN;

    @FXML
    private TextField GPA_STUtextArea;

    @FXML
    private TextField median_STUtextArea;

    @FXML
    private TextField ID_STUtextArea;

    @FXML
    private TextField GPA_GradestextArea;

    @FXML
    private TextField median_GradesTextArea;

    @FXML
    private TextField ID_GradetextArea;
  
    @FXML
	private TableColumn IDColumn;
    @FXML
	private TableColumn CourseNameColumn;
    @FXML
	private TableColumn RequestedByColumn;
    @FXML
	private TableColumn ExtraTimeColumn;
     @FXML
	private TableColumn ReasonColumn;
  
    
    @FXML
    public void initialize() {
        TableView<RequestTime> requests = new TableView<>();
        TableColumn<RequestTime, String> iDColumn = new TableColumn<>("ID");
    	IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<RequestTime, String> courseNameColumn = new TableColumn<>("Course Name");
    	CourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("coursename"));
        TableColumn<RequestTime, String> requestedByColumn = new TableColumn<>("Requested By");
    	RequestedByColumn.setCellValueFactory(new PropertyValueFactory<>("requestedby"));
        TableColumn<RequestTime, String> extraTimeColumn = new TableColumn<>("Extra Time");
    	ExtraTimeColumn.setCellValueFactory(new PropertyValueFactory<>("extratime"));
        TableColumn<RequestTime, String> reasonColumn = new TableColumn<>("Reason");
        ReasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        requests.getColumns().add(IDColumn);
        requests.getColumns().add(courseNameColumn);
        requests.getColumns().add(requestedByColumn);
        requests.getColumns().add(extraTimeColumn);
        requests.getColumns().add(reasonColumn);
    
        ObservableList<RequestTime> data = FXCollections.observableArrayList(
            new RequestTime(123, "lll","ffff",10,"ssss"),
            new RequestTime(123, "lll","ffff",10,"ssss"),
            new RequestTime(123, "lll","ffff",10,"ssss")
    );
    requests.setItems(data);

    
    }
  
    @FXML
	public void ApproveRequestTime(Object requestTime) {
            
        

	
		
    }
    @FXML
	public void ToLogOut(ActionEvent event) {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScreen.fxml"));
            Parent root;
            try {
                root = loader.load();
                Stage window = (Stage) getLogOutBTN().getScene().getWindow();
                window.setScene(new Scene(root));
                window.show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
       
  
}
}
