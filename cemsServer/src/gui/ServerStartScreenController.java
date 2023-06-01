package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.LoggedUsers;
import server.EchoServer;

public class ServerStartScreenController implements Initializable   {

	private EchoServer server;


	@FXML
	private Button startBtn ;
	@FXML
	private Button exitBtn ;
	@FXML
	private TableColumn<LoggedUsers, Integer> id;
	@FXML
	private TableColumn<LoggedUsers, String> firstName;
	@FXML
	private TableColumn<LoggedUsers, String> lastName;
	@FXML
	private TableColumn<LoggedUsers, String> userName;
	@FXML
	private TableColumn<LoggedUsers, Integer> role;
	@FXML
    private TableView<LoggedUsers> table;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	id.setCellValueFactory(new PropertyValueFactory<>("id"));
    	firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    	lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    	userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
    	role.setCellValueFactory(new PropertyValueFactory<>("role"));
	}

	public void UpadteOnlineUsers(ArrayList<LoggedUsers> usersArray) {
		table.getItems().clear();
		table.getItems().addAll(usersArray);

	}

	@FXML
	public void startServer() {
	    try {
	        if (server != null) {
	            server.listen(); //Start listening for connections
	        } else {
	            System.out.println("ERROR - Server is not initialized!");
	        }
	    } catch (Exception ex) {
	        System.out.println("ERROR - Could not listen for clients!");
	    }
	}


	public void setServer(EchoServer server) {
		this.server = server;
	}



































}
