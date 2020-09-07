package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.gn.decorator.GNDecorator;
import com.gn.decorator.options.ButtonType;

import helpers.DbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Pair;

public class appController implements Initializable {

	@FXML
	private TextField usernameInput;

	@FXML
	private PasswordField passwordInput;

	@FXML
	void handleLogin(MouseEvent event) throws Exception {
		this.login();
	}

	/**
	 * Show Home Page
	 * 
	 * @throws Exception
	 */
	void showHomePage(int user_id) throws Exception {

		// System.setProperty("prism.lcdtext", "false"); // make text smooth
		// Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml"));

		/*
		 * FXMLLoader fxmlLoader = new
		 * FXMLLoader(getClass().getResource("/views/home.fxml")); Parent root =
		 * (Parent)fxmlLoader.load(); homeController homeController =
		 * fxmlLoader.<homeController>getController();
		 * homeController.set_connected_user_id(user_id);
		 */

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));

		// Create a controller instance
		homeController controller = new homeController(user_id);
		// Set it in the FXMLLoader
		loader.setController(controller);
		Parent root = loader.load();
		GNDecorator window = new GNDecorator();
		window.setTitle("PhoneBook");

		window.fullBody();
		window.addButton(ButtonType.FULL_EFFECT);
		window.centralizeTitle();
		window.setContent(root);
		window.show();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	/**
	 * Close actual window
	 */
	void closeActualWindow() {
		Window stage = usernameInput.getScene().getWindow(); // get any node from the window
		stage.hide();
	}

	/**
	 * Login test
	 * 
	 * @param user
	 * @param pass
	 * @return
	 */
	void login() {
		try {

			String user = this.usernameInput.getText();
			String pass = this.passwordInput.getText();
			boolean isLogged = false;
			int user_id = 100;

			Connection connection = DbConnect.getInstance().getConnection();

			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(
					"select * from users where username" + " = '" + user + "' and password = '" + pass + "'");

			// if ok
			if (resultSet.next()) {
				isLogged = true;
				user_id = resultSet.getInt("id");

			} else {
				isLogged = false;
			}
			connection.close();
			if (isLogged) {

				this.showHomePage(user_id); // show Home page
				this.closeActualWindow(); // close actual window
			} else {
				this.showDialog(AlertType.ERROR, "Login Failed", "Your username or password is incorrect!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void showDialog(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);

		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	public void onEnter(ActionEvent ae) {
		this.login();
	}

	@FXML
	void signupClick(MouseEvent event) throws IOException {
		System.setProperty("prism.lcdtext", "false"); // make text smooth

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/signup.fxml"));

		Parent parent = fxmlLoader.load();
		signupController signupontroller = fxmlLoader.<signupController>getController();

		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.setIconified(false);
		stage.setTitle("Create New Account");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();
	}

}
