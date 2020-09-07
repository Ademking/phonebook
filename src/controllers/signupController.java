package controllers;

import java.sql.Connection;
import java.sql.Statement;

import helpers.DbConnect;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class signupController {

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private PasswordField confirmpass;

	@FXML
	private Button signupbtn;

	@FXML
	void signupClick(MouseEvent event) {

		// empty check
		if (username.getText().isEmpty() || password.getText().isEmpty() || confirmpass.getText().isEmpty()) {
			showDialog(AlertType.ERROR, "Error", "Username / password cannot be empty");
			return;
		}

		// password check
		if (!password.getText().equals(confirmpass.getText())) {
			showDialog(AlertType.ERROR, "Error", "Password does not match confirmation");
			return;
		}

		try {
			Connection c = DbConnect.getInstance().getConnection();

			String sql = "INSERT INTO \"users\"(\"username\",\"password\") VALUES (\"" + username.getText() + "\",\""
					+ password.getText() + "\")";

			Statement stmt = c.createStatement();

			stmt.execute(sql);
			stmt.close();
			c.close();

			this.close_window(event);

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	void showDialog(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	void close_window(MouseEvent event) {
		// close window
		final Node source = (Node) event.getSource();
		final Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

}
