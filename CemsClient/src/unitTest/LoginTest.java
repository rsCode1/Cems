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
