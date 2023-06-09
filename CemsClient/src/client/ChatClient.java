// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import gui.HDController;
import gui.LoginScreenController;
import logic.Grades;
import logic.Request;
import logic.RequestTime;
import logic.Users;

import java.io.*;
import java.util.ArrayList;
import ocsf.client.*;

public class ChatClient extends AbstractClient

{
	private LoginScreenController loginScreecontroller;
	private HDController hdController;

	private String ip = "";
	private int portServer;
	// Instance variables **********************************************

	public ChatClient(String host, int port, Object clientUI) throws IOException {
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
	public void handleMessageFromServer(Object msg) {

		if (msg instanceof Users || msg == null) {
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
		if (msg instanceof ArrayList<?>) {
		
			if(	((ArrayList) msg).get(0)instanceof RequestTime) {
			hdController.UpdateRequestTime((ArrayList<RequestTime>) msg);
			for (RequestTime rt : (ArrayList<RequestTime>) msg) {
				System.out.println("111" + rt.toString());

			}
			}
			if(	((ArrayList) msg).get(0) instanceof Grades) {
				System.out.println("BEFOR");
				Grades grade=(Grades)msg;
				if(grade.getDataOf()==0) {
				hdController.ImportLectueGradeStatistics((ArrayList<Grades>) msg);
				System.out.println("AFTER");
			}
				if(grade.getDataOf()==1) {
					hdController.ImportStudentGradeStatistics((ArrayList<Grades>) msg);
				}
				if(grade.getDataOf()==2) {
					hdController.ImportCourseGradeStatistics((ArrayList<Grades>) msg);
				
				}
			}
		}
		if (msg instanceof Request) {
			Request rq = (Request) msg;
			switch (rq.getRequestType()) {
			case "Who Requested Extra Time":
				hdController.showpPopupApprove((String) rq.getRequestParam());
				break;
			}
		}
		
	}

	public void setController(LoginScreenController controller) {
		this.loginScreecontroller = controller;
	}

	public void setController(HDController controller) {
		this.hdController = controller;
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
// End of ChatClient class
