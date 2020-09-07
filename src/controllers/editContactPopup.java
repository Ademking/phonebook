package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import helpers.DbConnect;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Contact;

public class editContactPopup implements Initializable {

	private Contact contact;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@FXML
	private TextField firstname;

	@FXML
	private TextField lastname;

	public TextField getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname.setText(firstname);
	}

	public TextField getLastname() {
		return lastname;
	}

	public void setLastname(TextField lastname) {
		this.lastname = lastname;
	}

	public TextField getTel() {
		return tel;
	}

	public void setTel(TextField tel) {
		this.tel = tel;
	}

	public TextField getEmail() {
		return email;
	}

	public void setEmail(TextField email) {
		this.email = email;
	}

	public TextField getOrg() {
		return org;
	}

	public void setOrg(TextField org) {
		this.org = org;
	}

	public TextArea getAddress() {
		return address;
	}

	public void setAddress(TextArea address) {
		this.address = address;
	}

	@FXML
	private TextField tel;

	@FXML
	private TextField email;

	@FXML
	private TextField org;

	@FXML
	private TextArea address;

	private int created_by_id;

	public void setContact(Contact contact2) {
		this.contact = contact2;
	}

	public int getCreated_by_id() {
		return created_by_id;
	}

	public void setCreated_by_id(int created_by_id) {
		this.created_by_id = created_by_id;
	}

	public Contact getContact() {
		return contact;
	}

	public void setFirstname(TextField firstname) {
		this.firstname = firstname;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				firstname.setText(contact.getFirstname());
				lastname.setText(contact.getLastname());
				email.setText(contact.getEmail());
				tel.setText(contact.getPhone());
				org.setText(contact.getOrganization());
				address.setText(contact.getAddress());
				id = contact.getId();
				created_by_id = contact.getCreatedBy();
			}
		});

	}

	void close_window(MouseEvent event) {
		// close window
		final Node source = (Node) event.getSource();
		final Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	@FXML
	void modifyClick(MouseEvent event) throws SQLException, ClassNotFoundException {
		this.update(this.contact);
		this.close_window(event);
	}

	public void update(Contact contact) throws SQLException, ClassNotFoundException {

		try {
			Connection c = DbConnect.getInstance().getConnection();

			String sql = "UPDATE contacts SET \"firstname\" = \"" + this.firstname.getText() + "\""
					+ " , \"lastname\" = \"" + this.lastname.getText() + "\"" + " , \"tel\" = \"" + this.tel.getText()
					+ "\"" + " , \"email\" = \"" + this.email.getText() + "\"" + " , \"organization\" = \""
					+ this.org.getText() + "\"" + " , \"address\" = \"" + this.address.getText() + "\""
					+ " WHERE \"id\" = \"" + contact.getId() + "\"";

			// System.out.println(sql);
			Statement stmt = c.createStatement();

			stmt.execute(sql);
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	@FXML
	void CloseClick(MouseEvent event) {
		this.close_window(event);
	}

	void delete_contact(int id) throws SQLException {
		Connection c = DbConnect.getInstance().getConnection();
		String sql = "DELETE FROM contacts WHERE id = " + id;
		Statement stmt = c.createStatement();
		stmt.execute(sql);
		stmt.close();
		c.close();

	}

	@FXML
	void deleteClick(MouseEvent event) throws SQLException {
		Alert alert = new Alert(AlertType.CONFIRMATION);

		alert.setTitle("Delete Contact");
		alert.setHeaderText("Are you sure you want to delete this contact ?");
		alert.setContentText("This action is irreversible");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			this.delete_contact(contact.getId());
			this.close_window(event);
		} else {

		}
	}

}