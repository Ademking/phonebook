package controllers;

import java.sql.Connection;
import java.sql.Statement;

import helpers.DbConnect;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class addContactPopup {

	private int connected_user_id;

	@FXML
	public TextField firstname;

	@FXML
	public TextField lastname;

	@FXML
	public TextField tel;

	@FXML
	public TextField email;

	@FXML
	public TextField org;

	@FXML
	public TextArea address;

	@FXML
	void ResetAllClick(MouseEvent event) {
		this.firstname.clear();
		this.lastname.clear();
		this.tel.clear();
		this.email.clear();
		this.org.clear();
		this.address.clear();
	}

	@FXML
	void createContactClick(MouseEvent event) {
		try {
			Connection c = DbConnect.getInstance().getConnection();

			String sql = "INSERT INTO \"contacts\"(\"firstname\",\"lastname\",\"tel\",\"email\",\"organization\",\"address\",\"created_by\") VALUES (\""
					+ firstname.getText() + "\",\"" + lastname.getText() + "\",\"" + tel.getText() + "\",\""
					+ email.getText() + "\",\"" + org.getText() + "\",\"" + address.getText() + "\",\""
					+ getConnected_user_id() + "\")";

			Statement stmt = c.createStatement();

			stmt.execute(sql);
			stmt.close();
			c.close();

			this.close_window(event);

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	void close_window(MouseEvent event) {
		// close window
		final Node source = (Node) event.getSource();
		final Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	public int getConnected_user_id() {
		return connected_user_id;
	}

	public void setConnected_user_id(int connected_user_id) {
		this.connected_user_id = connected_user_id;
	}
}
