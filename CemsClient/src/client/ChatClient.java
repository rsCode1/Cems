// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import logic.Response;
import client.*;
import gui.ConnectToServerScreenController;
import gui.InExamController;
import gui.LoginScreenController;
import gui.StudentHistoryController;
import gui.StudentManualTestController;
import gui.TakeExamController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.AddedTime;
import logic.DownloadManualExaminController;
import logic.Test;
import logic.Users;

import java.io.*;
import java.util.List;
import ocsf.client.*;

public class ChatClient extends AbstractClient

{
  private StudentManualTestController stdManController;
  private LoginScreenController loginScreecontroller;
  private TakeExamController takeExamController;
  private InExamController inExamController;
  private StudentHistoryController StudentHistoryController;
  private String ip = "";
  private int portServer;
  // Instance variables **********************************************


  public ChatClient(String host, int port, Object clientUI)
      throws IOException {
    super(host, port);
    ip = host;
    this.portServer = port;
  }
  // this is new line
  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
	 if (msg instanceof Response) {
       Response response = (Response) msg;
    	 switch (response.getResponseType()) {
				  case "LOGIN":
				  	login((Users) response.getResponseParam());
					  break; 
          case "GetExam":
           getExam((Test) response.getResponseParam());
           break;
          case "AddedTime":
            AddedTime added= (AddedTime) response.getResponseParam();
            inExamController.setAdded(added);
            break;
          }
         }
        }
   
       
       
  
 /*  else if(msg instanceof DownloadManualExaminController ) {
	  DownloadManualExaminController finfo=   ( DownloadManualExaminController) msg;
	  stdManController.setDownloadFile(finfo);
  }*/
  

  private void getExam(Test test){
    try {
		  takeExamController.ShowStudentEnterIdScreen(test);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  private void login(Users user) {

		System.out.println(user);
		try {
			loginScreecontroller.ShowUserWelcomeScreen(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(user.getRole());

	}


  public void setStudentManualTestController(StudentManualTestController controller) {
	  this.stdManController=controller;
  }
  public void setController(LoginScreenController controller) {
    this.loginScreecontroller = controller;
  }
  public void setTakeExamController(TakeExamController controller) {
	    this.takeExamController = controller;
	  }
  public void setInExamController(InExamController controller) {
	    this.inExamController = controller;
	  }
  public void setStudentHistoryController(StudentHistoryController controller) {
	    this.StudentHistoryController = controller;
	  }

  /**
   * This method terminates the client.
   */
  public void quit() {

    try {
      closeConnection();
    } catch (IOException e) {
    }
    System.exit(0);
  }

  public String getIp() {
    return ip;
  }

  public int getPortServer() {
    return portServer;
  }
}
// End of ChatClient class