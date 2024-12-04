package com.chatapp.client;

import com.chatapp.shared.Message;
import com.chatapp.shared.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.io.File;
import java.time.format.DateTimeFormatter;

public class ClientGUI extends Application {
    private ChatClient client;
    private TextArea chatArea;
    private TextField messageField;
    private ListView<User> userListView;
    private ObservableList<User> userList;
    private Label statusLabel;

    // for emojis yha added hai
    // private Button emojiButton;
    // private Button fileButton;

    @Override
    public void start(Stage primaryStage) {
        // ChatClient ka instance banate hain, jo server se connect karega
        client = new ChatClient("localhost", 5000);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));


        chatArea = new TextArea();

        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        VBox.setVgrow(chatArea, Priority.ALWAYS);


        messageField = new TextField();
        Button sendButton = new Button("Send");

        // idhar emoji and file buttons add krdiye hai screen pr output ke liye
        // emojiButton = new Button("😊");
        // fileButton = new Button("📎");

        sendButton.setDefaultButton(true);
        //HBox inputBox = new HBox(10, messageField, emojiButton, fileButton, sendButton);
        HBox inputBox = new HBox(10, messageField, sendButton);
        HBox.setHgrow(messageField, Priority.ALWAYS);


        VBox centerBox = new VBox(10, chatArea, inputBox);

        root.setCenter(centerBox);


        userList = FXCollections.observableArrayList();

        userListView = new ListView<>(userList);
        userListView.setCellFactory(param -> new UserListCell());
        userListView.setPrefWidth(150);

        root.setRight(userListView);

        statusLabel = new Label("Disconnected");

        statusLabel.setTextFill(Color.RED);

        root.setBottom(statusLabel);

        // Button click ya Enter press karne par message bhejne ka function call hota hai
        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());
        // Add event handlers for emoji and file button as well
        // emojiButton.setOnAction(e -> showEmojiPicker());
        // fileButton.setOnAction(e -> chooseAndSendFile());

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Application");
        primaryStage.show();

        try {
            // Client ko start karte hain aur message aur status handlers set karte hain
            client.start();

            client.setMessageHandler(this::handleMessage);
            client.setStatusHandler(this::updateStatus);
            promptForUsername(); // Username lene ka prompt dikhate hain
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Connection Error", "Failed to connect to the server.");
        }
    }

    private void sendMessage() {
        String content = messageField.getText().trim();
        if (!content.isEmpty()) {
            try {
                // Message bhejne ka function call karte hain
                client.sendMessage(new Message(client.getUsername(), content, null));
                messageField.clear(); // Message bhejne ke baad field clear karte hain
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Send Error", "Failed to send message.");
            }
        }
    }

    // emojis send krne ke liye aur add krna ho to add krdenge later
    // private void showEmojiPicker() {
    //     ContextMenu emojiMenu = new ContextMenu();
    //     String[] emojis = {"😊", "😂", "❤️", "👍", "🎉"};
    //     for (String emoji : emojis) {
    //         MenuItem item = new MenuItem(emoji);
    //         item.setOnAction(e -> messageField.appendText(emoji));
    //         emojiMenu.getItems().add(item);
    //     }
    //     emojiMenu.show(emojiButton, Side.BOTTOM, 0, 0);
    // }

    // private void chooseAndSendFile() {
    //     FileChooser fileChooser = new FileChooser();
    //     fileChooser.setTitle("Choose a file to send");
    //     File file = fileChooser.showOpenDialog(null);
    //     if (file != null) {
    //         try {
    //             client.sendFile(file);
    //             chatArea.appendText("File sent: " + file.getName() + "\n");
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //             showAlert("File Send Error", "Failed to send file.");
    //         }
    //     }
    // }


    private void handleMessage(Message message) {
        Platform.runLater(() -> {
            // Message ko format karke chat area mein dikhate hain
            String formattedMessage = String.format("[%s] %s: %s%n",
                    message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    message.getSender(),
                    message.getContent());
            chatArea.appendText(formattedMessage);
        });
    }

    private void updateStatus(boolean isConnected) {
        Platform.runLater(() -> {
            // Connection status update karte hain
            if (isConnected) {
                statusLabel.setText("Connected");
                statusLabel.setTextFill(Color.GREEN);
            } else {
                statusLabel.setText("Disconnected");
                statusLabel.setTextFill(Color.RED);
            }
        });
    }

    private void emojiSend(String emoji) {
        messageField.appendText(emoji);
    }

    private void promptForUsername() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Username");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter your username:");

        dialog.showAndWait().ifPresent(username -> {
            try {
                // Username set karte hain aur status update karte hain
                client.setUsername(username);
                updateStatus(true);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Connection Error", "Failed to set username.");
            }
        });
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static class UserListCell extends ListCell<User> {
        @Override
        protected void updateItem(User user, boolean empty) {
            super.updateItem(user, empty);
            if (empty || user == null) {
                setText(null);
                setGraphic(null);
            } else {
                // User ka username aur online status dikhate hain
                setText(user.getUsername());
                setTextFill(user.isOnline() ? Color.GREEN : Color.GRAY);
            }
        }
    }


    public static void main(String[] args) {
        launch(args); // Application ko launch karte hain
    }
}