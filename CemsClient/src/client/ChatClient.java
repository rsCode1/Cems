// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;



import client.*;
import gui.ConnectToServerScreenController;
import gui.HDController;
import gui.LoginScreenController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Users;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import ocsf.client.*;

 
public class ChatClient extends AbstractClient

{
	private LoginScreenController loginScreecontroller;
  private HDController hdController;
	private String ip="";
	private int portServer;
  //Instance variables **********************************************
  
  public ChatClient(String host, int port, Object clientUI) 
    throws IOException 
  {
	  super(host, port); 
	  ip=host;
	  this.portServer=port;
  }

  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    
      if (msg instanceof ArrayList<?>){
           hdController. ApproveRequestTime( msg);
      }
    
    
  if(msg instanceof Users || msg==null) {
          Users user = (Users) msg;
          System.out.println(user);
          try {
			loginScreecontroller.ShowUserWelcomeScreen(user);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          System.out.println(user.getRole());
      } else {
          System.out.println("Received message of type: " + msg.getClass());
      }
    }
  

	public void setController(LoginScreenController controller) {
	    this.loginScreecontroller = controller;
	}
  /**
   * This method terminates the client.
   */
  public void quit()
  {

    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

public String getIp() {
	return ip;
}

public int getPortServer() {
	return portServer;
}
}

//End of ChatClient class
