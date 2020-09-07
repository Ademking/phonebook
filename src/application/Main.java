package application;

import com.gn.decorator.GNDecorator; /* window border / fullscreen / etc... */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		System.setProperty("prism.lcdtext", "false"); // make text smooth
		Parent root = FXMLLoader.load(getClass().getResource("/views/app.fxml"));
		GNDecorator window = new GNDecorator();
		window.setTitle("PhoneBook");
		window.setResizable(false);
		window.fullBody();
		window.centralizeTitle();
		window.setContent(root);
		window.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
