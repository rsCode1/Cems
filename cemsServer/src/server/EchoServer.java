package server;
// This file contains material supporting section 3.7 of the textbook:

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.Blob;

import gui.ServerStartScreenController;
import logic.AddedTime;
import logic.DownloadManualExaminController;
import logic.FileDownloadInfo;
import logic.LogInInfo;
import logic.LoggedUsers;
import logic.Question;
import logic.Request;
import logic.StudentInTest;
import logic.Test;
import logic.TestApplyInfo;
import logic.TestCode;
import logic.TestSourceTime;
import logic.Users;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	private ServerStartScreenController serverScreenController;

	final public static int DEFAULT_PORT = 5555;

	public EchoServer(int port) {
		super(port);
	}



	public void setController(ServerStartScreenController controller2) {
		this.serverScreenController = controller2;
	}

	@Override
	public void handleMessageFromClient//
	(Object msg, ConnectionToClient client)

	{

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}
		if (msg instanceof Request) {
			Request request = (Request) msg;

			switch (request.getRequestType()) {
				case "LOGIN":
					checkUserLogin((LogInInfo) request.getRequestParam(), client);
					break;
				case "LOGOUT":
					logOut((LogInInfo) request.getRequestParam(), client);
					break;
				case "GetExam" :
					getExam( (TestCode) request.getRequestParam(),client);
					break;
				case "SubmitExam" :
					submitTest( (StudentInTest) request.getRequestParam(),client);
					break;
				case "CheckIfDurationChanged":
					checkIfDurationChanged((TestSourceTime) request.getRequestParam(),client);
				case "DownloadManualExam":
				   downloadManuelExam((FileDownloadInfo) request.getRequestParam(),client);
				// Add more case statements for other request types
			}
		}
	}
	
	
	
	private void downloadManuelExam(FileDownloadInfo fileDownloadInfo, ConnectionToClient client) {
		String sql = "SELECT file_data FROM cems.manueltests WHERE testId = ?";
	    PreparedStatement statement;
	    
        try {
        	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root","Aa123456");
        	statement = conn.prepareStatement(sql);
        	statement.setInt(1, fileDownloadInfo.getCourseId());
	    ResultSet result = statement.executeQuery();
	    if (result.next()) {
	        Blob fileData = (Blob) result.getBlob("file_data");
	        InputStream inputStream = fileData.getBinaryStream();
	        Socket sock = new Socket("127.0.0.1", 4444);
	        byte[] mybytearray = new byte[1024];
	        File file = new File("C:\\123.docx");
	        String FileName= file.getName();
	        FileOutputStream fos = new FileOutputStream(fileDownloadInfo.getFileDownloadPath());
	        BufferedOutputStream bos = new BufferedOutputStream(fos);
	        int bytesRead = inputStream.read(mybytearray, 0, mybytearray.length);
	        bos.write(mybytearray, 0, bytesRead);
	        bos.close();
	        sock.close();
	        //DownloadManualExaminController DMIC = new DownloadManualExaminController(inputStream);
	       // client.sendToClient(DMIC);
	        
	     /*  File outputFile = new File(fileDownloadInfo.getFileDownloadPath());
	      //  OutputStream outputStream = new FileOutputStream(outputFile);
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }

	        System.out.println("File downloaded successfully!");
	        outputStream.close();*/
	    } else {
	        System.out.println("File not found!");
	    }
        
	} catch (Exception e) {
	    e.printStackTrace();
	}	
		
	}

	private void checkUserLogin(LogInInfo loginInfo, ConnectionToClient client) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			Users user = getUserInfo(loginInfo, conn);
			if (user == null) {
				client.sendToClient((Users) null);
				return;
			}
			updateUserLoggedIn(loginInfo, conn);
			addUserToLoggedTable(conn);
			client.sendToClient(user);

		} catch (Exception ex) {
			/* handle the error */
			ex.printStackTrace();
		}
	}
	//if applying info is correct the function gets requested Test from data base and sends it to Client
		private void getExam(TestCode t,ConnectionToClient client){
			int testid;
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root","Aa123456");
				testid=ApplyingInfoExisted(conn,t);
				if(testid != -1) {
					Test test = getTest(conn,testid);
						if (test!=null) {
							Question[] qLst= getTestQuestions(conn,testid,test.getQuesSize());
								if(qLst != null ) {
									test.setQLst(qLst);
									try {
										client.sendToClient(test);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								else {
									System.out.println("Questions not Found!!! ");
									try {
										client.sendToClient((Test)null);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
						}
						else {
							System.out.println("Test code not Found!!! ");
							try {
								client.sendToClient((Test)null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			    } 
				else {
					System.out.println("Test not Found!!! ");
					try {
						client.sendToClient((Test)null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}
			
		}
	
		//returns Question list if testid Existed in DataBase else null
		private  Question[] getTestQuestions(Connection conn,int testid,int Size) {
			Question[] qLst=new Question[Size];
			boolean f= false;
			Statement stmt;
			try {
				String id= '"' + String.valueOf(testid) +'"';
				String str = "SELECT  q.*,tq.score FROM test t, questions q , test_question tq Where t.idTest=tq.idTest AND q.idquestion=tq.idquestion";
				str= str+ " AND t.idTest = " + id + ";";
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(str);
				int i=0;
		 		while (rs.next()) {
		 			String [] ansArr= new String[4];
		 			ansArr[0]=rs.getString(3);
		 			ansArr[1]=rs.getString(4);
		 			ansArr[2]=rs.getString(5);
		 			ansArr[3]=rs.getString(6);
		 			qLst[i]= new Question(rs.getString(2),ansArr,rs.getInt(9),rs.getInt(1));
		 			i++;
		 			f=true;
				}
	 		rs.close();
	 		if(f) {
	 		return qLst;
	 		}
	 		return null;
			}
			catch (SQLException e) {e.printStackTrace();}
			return null;
		
			
		}
		public void submitTest(StudentInTest studentInTest,ConnectionToClient client) {
			Statement stmt,stmt2;
			String str2 ="UPDATE `cems`.`students_applying_for_test_list` SET `submitted` = '1' WHERE (`stdId` = '"+studentInTest.getStudentId() +" ') and (`testId` = '"+ studentInTest.getTestId() +"');";
			String str;
			String studentId= '"' + String.valueOf(studentInTest.getStudentId()) +'"';
			String testId= '"' + String.valueOf(studentInTest.getTestId()) +'"';
			String courseName= '"' + String.valueOf(studentInTest.getCourseName()) +'"';
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root","Aa123456");
				stmt = conn.createStatement();
				for(int i=0 ; i<studentInTest.getQesSize();i++ ) {
					String answer= '"' + String.valueOf(studentInTest.getAnswer(i)) +'"';
					String quesId= '"' + String.valueOf(studentInTest.getQuesIdArr()[i]) +'"';
				    str = "INSERT INTO `cems`.`studentintest` (`studentId`, `testId`,`answer`, `courseName`, `quesId`) " ;
				    str+="VALUES (" + studentId + ',' +testId + ',' + answer + ',' + courseName+ ',' + quesId + ");" ;
				    stmt.executeUpdate(str);
				    str="";
					}
				stmt2 = conn.createStatement();
				stmt2.executeUpdate(str2);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
		
		private void checkIfDurationChanged(TestSourceTime testSourcetime , ConnectionToClient client) {
			String str;
			int currentD=testSourcetime.getSourceTime();
			AddedTime added = new AddedTime();
			added.setAdded(0);
			str ="SELECT test.duration FROM cems.test test WHERE idTest=" +testSourcetime.getTestId() + ";";
			Statement stmt;
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root","Aa123456");
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(str);
				if(rs.next()) {
					currentD=rs.getInt(1);
				}
				rs.close();
				if(currentD != testSourcetime.getSourceTime()) {
					added.setAdded(currentD - testSourcetime.getSourceTime());
					try {
						client.sendToClient(added);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
					try {
						client.sendToClient(added);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				client.sendToClient(added);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//gets test Data from DataBase and puts it in Test Object and returns it;
		private Test getTest(Connection conn,int testid) {
			ArrayList<Integer> studentsIdForTest = new ArrayList<>();
			Test test=null;
			Statement stmt;
			try {
			String id1= '"' + String.valueOf(testid) +'"';
			String str = "SELECT * FROM cems.test Where idTest = " + id1 + ";";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(str);
	 		if (rs.next()) {
	 			test= new Test(rs.getString(7),rs.getInt(2),rs.getString(4),rs.getInt(3),rs.getInt(1));
			}
	 		rs.close();
	 		ResultSet rs2;
	 		str = "SELECT list.stdId FROM cems.students_applying_for_test_list list WHERE testId = " + testid + " AND submitted = 0;";
	 		Statement stmt2 = conn.createStatement();
	 		rs2 = stmt2.executeQuery(str);
	 		while (rs2.next()) {
	 			studentsIdForTest.add(rs2.getInt(1));
			}
	 		rs2.close();
	 		test.setStudentsIdForTest(studentsIdForTest);
	 		return test;
			}
			catch (SQLException e) {e.printStackTrace();}
			return test;
		}
		//Checks if Test Applying info is existed in Database in Student Applying list 
		//returns test Id if it does Exist, else -1
		private int ApplyingInfoExisted(Connection conn,TestCode t) {
			Statement stmt;
			int testid=-1;
			try {
			String code1= '"' + String.valueOf(t.getCode())+'"';
			String str = "SELECT * FROM cems.students_applying_for_test_list list Where " +  "list.code=" + code1 + ";";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(str);
	 		if (rs.next()) 
	 			testid= rs.getInt(2);
	 		rs.close();
	 		return testid;
			}
			catch (SQLException e) {e.printStackTrace();}
			return testid;
		}

	// function to add all the users that are logged in to the logged to the table
	// in server
	private void addUserToLoggedTable(Connection conn) throws SQLException {
		// arraylist to hold info about all currently logged in users
		ArrayList<LoggedUsers> LoggedUsersArray = new ArrayList<>();

		// select relevant informatiom from DB to display in table
		Statement stmt2 = conn.createStatement();
		String command = "SELECT id,firstName,LastName,userName,role FROM users " + "WHERE islogged=1 ";
		ResultSet rs = stmt2.executeQuery(command);
		while (rs.next()) {
			// get fields from resultSet
			Integer id = rs.getInt("id");
			String firstName = rs.getString("firstName");
			String lastName = rs.getString("LastName");
			String userName = rs.getString("userName");
			int role = rs.getInt("role");

			LoggedUsers loggedUser = new LoggedUsers(id, firstName, lastName, userName, role);

			LoggedUsersArray.add(loggedUser);
		}
		// add the user to the logged in table through with controller function
		// 'UpadteOnlineUsers'
		serverScreenController.UpadteOnlineUsers(LoggedUsersArray);
	}

	// update the user as logged in DB , based on its username and password
	private void updateUserLoggedIn(LogInInfo login, Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(String.format("UPDATE users SET isLogged=1 WHERE userName='%s' AND password ='%s'",
				login.getUserName(), login.getPassword()));
	}

	// return user info from DB, based on its username and password
	private Users getUserInfo(LogInInfo login, Connection conn) throws SQLException {
		// select user with username and password
		Statement stmt = conn.createStatement();
		String command = String.format("SELECT * FROM users" + " WHERE userName='%s' AND password ='%s'",
				login.getUserName(), login.getPassword());
		ResultSet rs = stmt.executeQuery(command);
		if (!rs.next())
			return (null);
		return new Users(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6),
				rs.getInt(7));
	}
	
	
	private void logOut(LogInInfo login, ConnectionToClient client) {
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456");
					Statement stmt = conn.createStatement();
				stmt.executeUpdate(String.format("UPDATE users SET isLogged=0 WHERE userName='%s' AND password ='%s'",
						login.getUserName(), login.getPassword()));
				addUserToLoggedTable(conn);
		

			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	@Override
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	@Override
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */

	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
// End of EchoServer class
