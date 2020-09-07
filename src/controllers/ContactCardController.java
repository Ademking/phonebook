package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Contact;

public class ContactCardController {

	private Contact contact;

	@FXML
	private Label contactFullname;

	@FXML
	private Label phoneNumber;

	@FXML
	private Button details;

	@FXML
	void showDetails(MouseEvent event) throws Exception {
		System.setProperty("prism.lcdtext", "false"); // make text smooth

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/edit_contact_popup.fxml"));
		Parent parent = fxmlLoader.load();

		editContactPopup editController = fxmlLoader.<editContactPopup>getController();
		editController.setContact(contact);

		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.setIconified(false);
		stage.setTitle(contact.getFirstname() + " " + contact.getLastname() + " (Details)");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void setContact(Contact contact) {
		this.contact = contact;
		this.contactFullname.setWrapText(true);
		this.contactFullname.setText(this.contact.getFirstname() + " " + this.contact.getLastname());
		this.phoneNumber.setText(contact.getPhone());
	}
}
