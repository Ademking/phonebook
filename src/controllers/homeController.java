package controllers;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.File;

import com.gn.decorator.GNDecorator;
import helpers.DbConnect;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import models.Contact;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class homeController implements Initializable {

	private int connected_user_id;

	List<Contact> contactlist = new ArrayList<>();

	@FXML
	private Button addBtn;

	@FXML
	private Button searchBtn;

	@FXML
	private Button exportBtn;

	@FXML
	private Button aboutBtn;

	@FXML
	private Button logoutBtn;

	@FXML
	private FlowPane flowpane;

	@FXML
	private ScrollPane scrollpane;

	@FXML
	private AnchorPane parentPane;

	@FXML
	private TextField searchinput;

	private boolean searchMode = false;

	@FXML
	void handleInfo(MouseEvent event) {
		this.showDialog(AlertType.INFORMATION, "About", "This app is made by Adem Kouki");
	}

	homeController(int user_id) {
		super();
		this.connected_user_id = user_id;
	}

	/*
	 * refresh flowpane
	 */
	void update_flowpane() throws IOException, SQLException {
		this.flowpane.getChildren().clear();
		this.contactlist.clear();

		Connection connection = DbConnect.getInstance().getConnection();
		Statement statement = connection.createStatement();
		String sql = "SELECT * FROM contacts where created_by = " + this.get_connected_user()
				+ " AND firstname like \"%" + searchinput.getText() + "%\" ORDER BY ID DESC";
		// System.out.println(sql);
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) {

			int contact_id = rs.getInt("id");
			String contact_firstname = rs.getString("firstname");
			String contact_lastname = rs.getString("lastname");
			String contact_tel = rs.getString("tel");
			String contact_email = rs.getString("email");
			String contact_organization = rs.getString("organization");
			String contact_address = rs.getString("address");
			int contact_created_by = rs.getInt("created_by");

			Contact contact = new Contact(contact_id, contact_firstname, contact_lastname, contact_organization,
					contact_email, contact_tel, contact_address, contact_created_by);

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/contact_card.fxml"));
			Parent cardLayout = (Parent) fxmlLoader.load();
			ContactCardController cardController = fxmlLoader.<ContactCardController>getController();

			contactlist.add(contact);
			cardController.setContact(contact);

			this.flowpane.getChildren().add(cardLayout);

		}
		statement.close();
		connection.close();

		if (contactlist.size() > 0) {
			this.unset_empty_bg();
		} else {
			this.set_empty_bg();
		}

	}

	void updateGUI() {
		// every 1 second
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						try {
							update_flowpane();
						} catch (IOException e) {

							e.printStackTrace();
						} catch (SQLException e) {

							e.printStackTrace();
						}
					}
				});
			}
		}, 500, 500);

	}

	void set_empty_bg() {
		this.scrollpane.setStyle(
				"-fx-background-image: url('assets/notfound.png'); -fx-background-position: center center; -fx-background-repeat: stretch;");
	}

	void unset_empty_bg() {
		this.scrollpane.setStyle("-fx-background-image: none");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.updateGUI();

		try {
			this.flowpane.getChildren().clear();
			this.update_flowpane();
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// add tooltips
		this.addToolTip(this.addBtn, "Add new contact");
		this.addToolTip(this.searchBtn, "Search for contact");
		this.addToolTip(this.exportBtn, "Export Contact to CSV");
		this.addToolTip(this.aboutBtn, "About this app");
	}

	/**
	 * Add tooltip to node
	 * 
	 * @param node
	 * @param message
	 */
	void addToolTip(Node node, String message) {
		Tooltip t = new Tooltip(message);
		Tooltip.install(node, t);
	}

	void addNewContactCardToFlowPane() throws IOException, SQLException {

		this.flowpane.getChildren().clear();
		this.update_flowpane();

		/*
		 * FXMLLoader fxmlLoader = new
		 * FXMLLoader(getClass().getResource("/views/contact_card.fxml")); Parent
		 * cardLayout = (Parent)fxmlLoader.load(); ContactCardController cardController
		 * = fxmlLoader.<ContactCardController>getController();
		 * 
		 * 
		 * Contact contact = new Contact("Foulen", "Fouleni", "Org", "foulen@gmail.com",
		 * "+21620202020", "Tunisia"); contactlist.add(contact);
		 * cardController.setContact(contact);
		 * this.flowpane.getChildren().add(cardLayout);
		 */

	}

	void showDialog(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	void handleAddContact(MouseEvent event) throws Exception {

		this.showAddContactPopup();
		this.addNewContactCardToFlowPane();
		this.scrollpane.setVvalue(1);
	}

	void showAddContactPopup() throws Exception {
		System.setProperty("prism.lcdtext", "false"); // make text smooth

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/add_contact_popup.fxml"));

		Parent parent = fxmlLoader.load();
		addContactPopup addcontroller = fxmlLoader.<addContactPopup>getController();
		addcontroller.setConnected_user_id(get_connected_user());
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.setIconified(false);
		stage.setTitle("Add New Contact");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();

	}

	void set_connected_user_id(int user_id) {
		this.connected_user_id = user_id;
	}

	int get_connected_user() {
		return this.connected_user_id;
	}

	private static final String CSV_SEPARATOR = ";";

	private static void writeToCSV(List<Contact> contactlist2, String path) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
			for (Contact contact : contactlist2) {
				StringBuffer oneLine = new StringBuffer();
				oneLine.append(contact.getId());
				oneLine.append(CSV_SEPARATOR);
				oneLine.append(contact.getFirstname());
				oneLine.append(CSV_SEPARATOR);
				oneLine.append(contact.getLastname());
				oneLine.append(CSV_SEPARATOR);
				oneLine.append(contact.getPhone());
				bw.write(oneLine.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (UnsupportedEncodingException e) {
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	@FXML
	void exportclick(MouseEvent event) {

		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			writeToCSV(this.contactlist, file.getPath());
			play_notif_sound();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Contact Exported Successfully");
			alert.setHeaderText(null);
			alert.setContentText("All Contacts are exported successfully");
			alert.showAndWait();
		}

	}

	void play_notif_sound() {
		String musicFile = "src/assets/notif.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	@FXML
	void searchClick(MouseEvent event) {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Search Contact");
		dialog.setHeaderText("Search contact");
		dialog.setContentText("Please enter contact name:");
		List<Contact> listClone = new ArrayList<>();
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			for (Contact c : contactlist) {
				if (c.getFirstname().matches("(?i)(" + result.get() + ").*")) {
					listClone.add(c);
				}
			}
			// System.out.println( listClone);
		}

	}

	@FXML
	void aboutClick(MouseEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Phonebook v1.0.0");
		alert.setHeaderText("PhoneBook v1.0.0: Contact Manager App for Desktop");
		alert.setContentText(
				"Made By: \n----------------------\nAdem Kouki\nHaythem Trabelsi\nEya Machfar\n----------------------\nCopyright © 2020-2021");

		alert.showAndWait();
	}

	@FXML
	void disconnectClick(MouseEvent event) throws IOException {
		System.setProperty("prism.lcdtext", "false"); // make text smooth
		Parent root = FXMLLoader.load(getClass().getResource("/views/app.fxml"));
		GNDecorator window = new GNDecorator();
		window.setTitle("PhoneBook");
		window.setResizable(false);
		window.fullBody();
		window.centralizeTitle();
		window.setContent(root);
		window.show();
		this.closeActualWindow();
	}

	void closeActualWindow() {
		Window stage = flowpane.getScene().getWindow(); // get any node from the window
		stage.hide();
	}

	@FXML
	void search(KeyEvent event) {
		// System.out.println(searchinput.getText());
	}

}
