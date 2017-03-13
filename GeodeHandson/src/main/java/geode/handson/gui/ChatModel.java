package geode.handson.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatModel {
	public final BooleanProperty connected = new SimpleBooleanProperty(false);
	public final BooleanProperty readyToChat = new SimpleBooleanProperty(false);
	public final ObservableList<String> chatHistory = FXCollections.observableArrayList();
	public final StringProperty currentMessage = new SimpleStringProperty();
	public final StringProperty userName = new SimpleStringProperty();
	public final StringProperty roomName = new SimpleStringProperty();
}
