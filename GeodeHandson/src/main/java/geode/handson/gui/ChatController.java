package geode.handson.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChatController implements Initializable {
	@FXML
	private ChoiceBox<String> roomSelection;
	@FXML
	private Button connectButton;
	@FXML
	private TextField userNameTextfield;
	@FXML
	private TextField messageTextField;
	@FXML
	private Button chatButton;
	@FXML
	private ListView<String> chatListView;

	private final ChatModel model = new ChatModel();
	private ChatClientEndpoint endpoint;

	@Override
	public void initialize(final URL url, final ResourceBundle bundle) {
		roomSelection.setItems(FXCollections.observableArrayList("General", "Random"));
		roomSelection.getSelectionModel().select(0);
		model.userName.bindBidirectional(userNameTextfield.textProperty());
		model.roomName.bind(roomSelection.getSelectionModel().selectedItemProperty());
		model.readyToChat.bind(model.userName.isNotEmpty().and(roomSelection.selectionModelProperty().isNotNull()));

		chatButton.disableProperty().bind(model.connected.not());
		messageTextField.disableProperty().bind(model.connected.not());
		messageTextField.textProperty().bindBidirectional(model.currentMessage);
		connectButton.disableProperty().bind(model.readyToChat.not());
		chatListView.setItems(model.chatHistory);

		userNameTextfield.setOnAction(event -> {
			connectButton.fire();
		});
		messageTextField.setOnAction(event -> {
			handleSendMessage();
		});
		chatButton.setOnAction(evt -> {
			handleSendMessage();
		});
		connectButton.setOnAction(evt -> {
			try {
				endpoint = new ChatClientEndpoint("localhost", 10334, model.userName.get(), model.roomName.get());
				endpoint.addMessageHandler(responseString -> {
					Platform.runLater(() -> {
						model.chatHistory.add(responseString);
					});
				});
				model.connected.set(true);
			} catch (Exception e) {
				showDialog("Error: " + e.getMessage());
			}
		});
	}

	private void handleSendMessage() {
		endpoint.sendMessage(model.currentMessage.get());
		model.currentMessage.set("");
		messageTextField.requestFocus();
	}

	private void showDialog(final String message) {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		VBox box = new VBox();
		box.getChildren().addAll(new Label(message));
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(5));
		dialogStage.setScene(new Scene(box));
		dialogStage.show();
	}
}