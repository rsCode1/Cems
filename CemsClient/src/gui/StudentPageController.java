
package gui;

	import java.net.URL;
	import java.util.ResourceBundle;

import client.ChatClient;
import javafx.fxml.FXML;
	
	public class StudentPageController {
		private ChatClient client;
	    @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    void initialize() {

	    }
	    public void setClient(ChatClient client) {
		    this.client = client;
		}
	}


