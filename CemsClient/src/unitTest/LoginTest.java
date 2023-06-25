package unitTest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

//Assuming these are the correct imports for your classes

import org.junit.jupiter.api.Test;

import client.ChatClient;
import gui.LoginScreenController;
import javafx.embed.swing.JFXPanel;
import logic.Users;

public class LoginTest {
 
    // testing login() ,check if existed student succeded to log in the the student screen
    //if student succeded then loginScreenController.getStatus()=student , else loginScreenController.getStatus() = not found
	// input (Users) student .
	// expected : student logged in successfully and loginScreenController.getStatus()=student
    @Test
    public void testShowUserWelcomeScreen_student() {
        new JFXPanel();
        // Arrange
        String testIp = "localhost";
        int testPort = 5555;

        Users testUser = new Users(123, "Elon", "Mask", "El", "123", 0, 0);
        testUser.setRole(0); // Assume 0 is the role for students

        LoginScreenController loginScreenController = new LoginScreenController();
        // Act
        try {
            loginScreenController.ShowUserWelcomeScreen(testUser);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Assert
        assertEquals("student", loginScreenController.getStatus());
    }

    // testing login() ,check if existed Lecturer succeded to get the lecturer screen
    //if lecturer succeded then loginScreenController.getStatus()=lecturer, else loginScreenController.getStatus() = not found
	// input (Users) lecturer .
	// expected : lecturer logged in successfully and loginScreenController.getStatus()=lecturer
    @Test
    public void testShowUserWelcomeScreen_Lecturer() {
        new JFXPanel();
        // Arrange
        String testIp = "localhost";
        int testPort = 5555;

        Users testUser = new Users(123, "Elon", "Mask", "El", "123", 0, 1);

        LoginScreenController loginScreenController = new LoginScreenController();
        // Act
        try {
            loginScreenController.ShowUserWelcomeScreen(testUser);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Assert
        assertEquals("lecturer", loginScreenController.getStatus());
    }


    // testing login() ,check if existed head of department succeded to get the head of department  screen
    //if head of department  succeded then loginScreenController.getStatus()=head, else loginScreenController.getStatus() = not found
	// input (Users) head of department  .
	// expected : head of department  logged in successfully and loginScreenController.getStatus()=head
    @Test
    public void testShowUserWelcomeScreen_HeadOfDepartment() {
        new JFXPanel();
        // Arrange
        String testIp = "localhost";
        int testPort = 5555;

        Users testUser = new Users(123, "Elon", "Mask", "El", "123", 0, 2);

        LoginScreenController loginScreenController = new LoginScreenController();
        // Act
        try {
            loginScreenController.ShowUserWelcomeScreen(testUser);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Assert
        assertEquals("head", loginScreenController.getStatus());
    }

    // testing login() ,check if not existed user trys to login
    // loginScreenController.getStatus() should be equals to  not found 
	// input (Users) null.
	// expected :  logged failed and loginScreenController.getStatus()=head
    @Test
    public void testShowUserWelcomeScreen_NotFound() {
        new JFXPanel();
        // Arrange
        String testIp = "localhost";
        int testPort = 5555;

        Users testUser =null;
        LoginScreenController loginScreenController = new LoginScreenController();
        // Act
        try {
            loginScreenController.ShowUserWelcomeScreen(testUser);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Assert
        assertEquals("not found", loginScreenController.getStatus());
    }

}
