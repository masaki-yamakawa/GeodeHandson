package geode.handson.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatGuiClient extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/chat.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/chat.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Geode Chat Client");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
