package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.sql.Blob;

// try push
public class StudentManualTest {

	@FXML
	private Button download;
	@FXML
	private Button upload;
	@FXML
	private Label timeLabel;
	@FXML
	private Label selectedFileLabel;

	private File selectedFile;

	private void updateTimerLabel(int timeInSeconds) {
		// Convert seconds to minutes and seconds
		int minutes = timeInSeconds / 60;
		int seconds = timeInSeconds % 60;

		String timeText = String.format("%02d:%02d", minutes, seconds);
		// Update timeLabel on the FX application thread
		Platform.runLater(() -> timeLabel.setText(timeText));
	}

	private volatile boolean stopThread = false;
	private Thread timeThread;

	private void startTimer(int timeInSeconds) {
		stopThread = false; // Reset stopThread flag
		timeThread = new Thread(() -> {
			try {
				int remainingTime = timeInSeconds;
				while (remainingTime >= 0 && !stopThread) {
					updateTimerLabel(remainingTime);
					Thread.sleep(1000);
					remainingTime--;
				}

				// Timer has finished
				if (!stopThread) {
					upload.setDisable(true);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		timeThread.start();
	}

	private void stopTimer() {
		stopThread = true;
		if (timeThread != null) {
			timeThread.interrupt();
			timeThread = null;
		}
	}

	@FXML
	void click_download(ActionEvent event) {
		int timeInSeconds = 10;
		startTimer(timeInSeconds);
		int fileId = 1; // Replace with the actual file ID
		String filePath = "C:\\Users\\nilad\\Desktop\\cems\\test3.docx"; // Replace with the desired save path

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
				"Aa123456")) {
			System.out.println("SQL connection succeeded");

			String sql = "SELECT file_data FROM files_table WHERE id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, fileId);

			ResultSet result = statement.executeQuery();
			if (result.next()) {
				Blob fileData = result.getBlob("file_data");
				InputStream inputStream = fileData.getBinaryStream();

				File outputFile = new File(filePath);
				OutputStream outputStream = new FileOutputStream(outputFile);

				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();

				System.out.println("File downloaded successfully!");
			} else {
				System.out.println("File not found!");
			}
		} catch (SQLException | IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	void click_upload(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Modified File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Documents", "*.doc", "*.docx"));
		selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			selectedFileLabel.setText("Selected File: " + selectedFile.getName());
		}
	}

	@FXML
	void click_submit(ActionEvent event) {
		if (selectedFile != null) {
			// Upload the file to the database
			int fileId = 1; // Replace with the actual file ID

			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/cems?serverTimezone=IST", "root",
					"Aa123456")) {
				System.out.println("SQL connection succeeded");

				// Upload the modified file
				InputStream uploadInputStream = new FileInputStream(selectedFile);

				String uploadSql = "UPDATE files_table SET file_data = ? WHERE id = ?";
				PreparedStatement uploadStatement = conn.prepareStatement(uploadSql);
				uploadStatement.setBinaryStream(1, uploadInputStream, (int) selectedFile.length());
				uploadStatement.setInt(2, fileId);
				int uploadResult = uploadStatement.executeUpdate();

				if (uploadResult > 0) {
					System.out.println("File uploaded successfully!");
					// Add your verification logic here
					// You can display a message, update UI, or perform any necessary action
				} else {
					System.out.println("File upload failed!");
				}

				uploadStatement.close();
				uploadInputStream.close();
			} catch (SQLException | IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
