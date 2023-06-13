// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import client.*;
import gui.ConnectToServerScreenController;
import gui.HDController;
import gui.LoginScreenController;
import gui.writeQuestionController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Grades;
import logic.LogInInfo;
import logic.RequestTime;
import logic.Response;
import logic.Users;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import ocsf.client.*;

public class ChatClient extends AbstractClient

{
    private LoginScreenController loginScreecontroller;
    private writeQuestionController writeQuestionController;
    private HDController hdcontroller;
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
    public void handleMessageFromServer(Object msg) {

        if (msg instanceof Response) {
            Response response = (Response) msg;
            switch (response.getResponseType()) {
                case "LOGIN_success":
                    login((Users) response.getResponseParam());
                    break;
                case "Subjects":
                    ArrayList<String> subjectsArr = (ArrayList<String>) response.getResponseParam();
                    writeQuestionController.updateSubjectsComboBox(subjectsArr);
                    System.out.println("hello");
                    break;

                case "Courses":
                    ArrayList<String> coursesArr = (ArrayList<String>) response.getResponseParam();
                    writeQuestionController.updateCoursesComboBox(coursesArr);
                    System.out.println("hello2");
                    break;
                case "Who Requested Extra Time":
                    hdcontroller.setTest((String) response.getResponseParam());
                    hdcontroller.refreshTable1();
                    hdcontroller.showpPopupApprove((String) response.getResponseParam());
                    break;
                case "RETURN-STUDENT-GRADES":
                    ArrayList<String> returnarr = (ArrayList<String>) response.getResponseParam();
                    for (String str : returnarr)
                        System.out.println(str.toString());
                    hdcontroller.setGeneralArray(returnarr);
                    break;
                case "ImportCourseData":
                    System.out.println("in import course data: " + (ArrayList<Grades>) response.getResponseParam());
                    ArrayList<Grades> grade = (ArrayList<Grades>) response.getResponseParam();
                    hdcontroller.ImportCourseGradeStatistics(grade);

                    break;

                case "ImportLectuerData":
                    ArrayList<Grades> grade1 = (ArrayList<Grades>) response.getResponseParam();
                    hdcontroller.ImportLectuerStatistics(grade1);

                    break;
                case "ImportStudentData":
                    ArrayList<Grades> grade2 = (ArrayList<Grades>) response.getResponseParam();
                    hdcontroller.ImportStudentGradeStatistics(grade2);
                    break;
                case "ImportCourseDatatenths":
                    System.out.println("in import course data: " + (ArrayList<Grades>) response.getResponseParam());
                    ArrayList<Grades> grade3 = (ArrayList<Grades>) response.getResponseParam();
                    hdcontroller.ImportCourseGradeStatisticstenths(grade3);

                    break;
                case "ImportLectuerDataTenths":
                    System.out.println("in import course data: " + (ArrayList<Grades>) response.getResponseParam());
                    ArrayList<Grades> grade4 = (ArrayList<Grades>) response.getResponseParam();
                    hdcontroller.ImportLectuerStatisticsTenths(grade4);

                    break;
                case "ImportStudentDataTenths":
                    System.out.println("in import course data: " + (ArrayList<Grades>) response.getResponseParam());
                    ArrayList<Grades> grade5 = (ArrayList<Grades>) response.getResponseParam();
                    hdcontroller.ImportStudentGradeStatisticsTenths(grade5);

                    break;
                case "EXTRA_TIME_INFO":
                    System.out.println("in chatClient");
                    ArrayList<RequestTime> requestList = (ArrayList<RequestTime>) response.getResponseParam();
                    hdcontroller.UpdateRequestTime((ArrayList<RequestTime>) response.getResponseParam());
                    System.out.println("in chatClient111");
                    for (RequestTime rt : (ArrayList<RequestTime>) response.getResponseParam()) {

                    }

                    // ************************************************ */
                    break;
                case "Get2IDSGradesStudent":
                    System.out.println("in chatCLient testing 2ids");
                    ArrayList<ArrayList<Grades>> gradeList = (ArrayList<ArrayList<Grades>>) response.getResponseParam();
                    ArrayList<Grades> grades = gradeList.get(0);
                    ArrayList<Grades> grades1 = gradeList.get(1);

                    // ArrayList<Grades> gradeList = (ArrayList<Grades>)
                    // response.getResponseParam();
                    // System.out.println(gradeList.toString());
                    // ArrayList<ArrayList<Grades>> gradeList = (ArrayList<ArrayList<Grades>>)
                    // response.getResponseParam();
                    for (Grades g1 : grades) {
                        System.out.println(g1.toString());

                        System.out.println("in for loop" + g1.toString());
                    }
                    for (Grades g2 : grades1) {
                        System.out.println(g2.toString());

                        System.out.println("in for loop" + g2.toString());

                    }

                    hdcontroller.compareTwoStudents(grades, grades1);
                    break;
                    case "Get2IDSGradesLectuer":
                    System.out.println("in chatCLient testing 2ids");
                    gradeList = (ArrayList<ArrayList<Grades>>) response.getResponseParam();
                    grades = gradeList.get(0);
                   grades1 = gradeList.get(1);

                    // ArrayList<Grades> gradeList = (ArrayList<Grades>)
                    // response.getResponseParam();
                    // System.out.println(gradeList.toString());
                    // ArrayList<ArrayList<Grades>> gradeList = (ArrayList<ArrayList<Grades>>)
                    // response.getResponseParam();
                    for (Grades g1 : grades) {
                        System.out.println(g1.toString());

                        System.out.println("in for loop" + g1.toString());
                    }
                    for (Grades g2 : grades1) {
                        System.out.println(g2.toString());

                        System.out.println("in for loop" + g2.toString());

                    }

                    hdcontroller.compareTwoLectuers(grades, grades1);
                    break;
                    case "Get2IDSGradesCourse":
                    System.out.println("in chatCLient testing 2ids");
                    gradeList = (ArrayList<ArrayList<Grades>>) response.getResponseParam();
                    grades = gradeList.get(0);
                    grades1 = gradeList.get(1);

                    for (Grades g1 : grades) {
                        System.out.println(g1.toString());

                        System.out.println("in for loop" + g1.toString());
                    }
                    for (Grades g2 : grades1) {
                        System.out.println(g2.toString());

                        System.out.println("in for loop" + g2.toString());

                    }

                    hdcontroller.compareTwoCourses(grades, grades1);
                    break;

            }
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

    public void setController(LoginScreenController controller) {
        this.loginScreecontroller = controller;
    }

    public void setController(writeQuestionController controller) {
        this.writeQuestionController = controller;
    }

    public void setController(HDController controller) {
        this.hdcontroller = controller;
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