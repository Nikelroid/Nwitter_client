package chat;

import checkExists.checkExists;
import connection.sender;
import graphics.selectIcon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import launch.authKey;
import launch.view;
import login.loginConnection;
import mainPages.Info;
import mainPages.Messenger;
import objects.objMessage;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import profiles.imageConnection;
import submit.submitMessage;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class chatPage {
    public static Timeline updateChecker;
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages;
    LocalDateTime now;
    checkExists checkExists = new checkExists();
    DateTimeFormatter dtf;
    submitMessage sb = new submitMessage();
    imageConnection imageConnection = new imageConnection();

    public static String AuthKey;
    public static String myUsername;
    public static String userUsername;

    sender sender = new sender();
    public static double vValueChatPage = 1;
    public static TextArea messageText;
    public static Button send, picture;
    int counter = 0;
    public static int[] mapper = new int[10000];
    private static final Logger logger = LogManager.getLogger(chatPage.class);

    public static DatePicker datePicker;
    public static TextField hour, minute, second;
    public static CheckBox sendNow;

    public chatPage(String AuthKey, String myUsername, String userUsername) throws Exception {

        chatPage.AuthKey = AuthKey;
        chatPage.myUsername = myUsername;
        chatPage.userUsername = userUsername;

        messages = get_mess.get();
        File file = new File("Message.json");
        if (!file.exists()) {
            messages = new ArrayList<>();
        }
        authKey.getter();
        logger.info("System: user went to ChatPage");

        for (int i = messages.size() - 1; i >= 0; i--)
            if (messages.get(i).getSender().equals(userUsername) &&
                    messages.get(i).getReceiver().equals(myUsername)
                    || (messages.get(i).getReceiver().equals(userUsername)
                    && userUsername.charAt(0) == '*' && !messages.get(i).getSender().equals(myUsername))) {
                if (!messages.get(i).getText().startsWith("/"))
                    messages.get(i).setSeen(true);
            }

        new jsonMessage(messages);
        messages = get_mess.get();
        if (!file.exists()) {
            messages = new ArrayList<>();
        }
        chatPage.mapper = new int[10000];
        view.root = FXMLLoader.load(getClass().getResource("/layout/page/chat_page.fxml"));
        view.scene = new Scene(view.root);
        view.stage.setScene(view.scene);
        view.stage.show();


        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        now = LocalDateTime.now();
        ImageView Exit = (ImageView) view.root.lookup("#exit");
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (response == 0) {
                new deathPages();
                System.exit(1);
            } else if (response == 1) {
                loginConnection loginConnection = new loginConnection();
                loginConnection.connection(AuthKey, "-");
                System.exit(1);
            }
        });

        Button Back = (Button) view.scene.lookup("#back");
        hour = (TextField) view.scene.lookup("#hour");
        minute = (TextField) view.scene.lookup("#minute");
        second = (TextField) view.scene.lookup("#second");
        datePicker = (DatePicker) view.scene.lookup("#date");
        sendNow = (CheckBox) view.scene.lookup("#now");


        Back.setText("Back");


        Label Header = (Label) view.scene.lookup("#header");
        if (myUsername.equals(userUsername)) {
            Header.setText("Saved messages");
        } else if (userUsername.charAt(0) == '*') {
            Header.setText(userUsername.substring(1) + " group");
        } else
            Header.setText(userUsername);

        chatPrinter.Forward = new ArrayList<>();
        chatPrinter.Save = new ArrayList<>();
        chatPrinter.Delete = new ArrayList<>();
        chatPrinter.Edit = new ArrayList<>();
        chatPrinter.messageImage = new ArrayList<>();

        chatPrinter.scrollPane = (ScrollPane) view.scene.lookup("#scobar");
        chatPrinter.chatList = (VBox) chatPrinter.scrollPane.lookup("#twittelist");

        messages = get_mess.get();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getSender().equals(myUsername) &&
                    messages.get(i).getReceiver().equals(userUsername)
                    && !messages.get(i).getText().startsWith("(From")) {
                messages = get_mess.get();
                try {
                    new chatPrinter(messages.get(i).getId(), 1);
                }catch (IndexOutOfBoundsException e){
                    new deathPages();
                    new chatPage(AuthKey,myUsername,userUsername);
                }

                mapper[counter] = i;
                counter++;
            } else if ((messages.get(i).getReceiver().equals(myUsername) &&
                    messages.get(i).getSender().equals(userUsername)) ||
                    (messages.get(i).getReceiver().equals(userUsername)
                            && messages.get(i).getReceiver().charAt(0) == '*')) {
                messages = get_mess.get();
                new chatPrinter(messages.get(i).getId(), 2);
                mapper[counter] = i;
                counter++;
            }

        }

        messageText = (TextArea) view.scene.lookup("#message_text");
        send = (Button) view.scene.lookup("#send");
        picture = (Button) view.scene.lookup("#pic");


        messages = get_mess.get();
        List<String> text = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        List<Boolean> sent = new ArrayList<>();
        List<Boolean> delivered = new ArrayList<>();
        List<Boolean> seen = new ArrayList<>();

        for (int i = 0; i < messages.size(); i++)
            if (messages.get(i).getReceiver().equals(userUsername)
                    || messages.get(i).getSender().equals(userUsername)) {
                text.add(messages.get(i).getText());
                id.add(messages.get(i).getId());
                sent.add(messages.get(i).isSent());
                delivered.add(messages.get(i).isDelivered());
                seen.add(messages.get(i).isSeen());
            }


        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                messages = get_mess.get();
                                List<String> tempText = new ArrayList<>();
                                List<Integer> tempId = new ArrayList<>();
                                List<Boolean> tempSent = new ArrayList<>();
                                List<Boolean> tempDelivered = new ArrayList<>();
                                List<Boolean> tempSeen = new ArrayList<>();
                                for (int i = 0; i < messages.size(); i++)
                                    if (messages.get(i).getReceiver().equals(userUsername)
                                            || messages.get(i).getSender().equals(userUsername)) {
                                        tempText.add(messages.get(i).getText());
                                        tempId.add(messages.get(i).getId());
                                        tempSent.add(messages.get(i).isSent());
                                        tempDelivered.add(messages.get(i).isDelivered());
                                        tempSeen.add(messages.get(i).isSeen());
                                    }
                                if (!text.toString().equals(tempText.toString())
                                        || !id.toString().equals(tempId.toString())
                                        || !sent.toString().equals(tempSent.toString())
                                        || !delivered.toString().equals(tempDelivered.toString())
                                        || (!seen.toString().equals(tempSeen.toString()) &&
                                        !myUsername.equals(userUsername))) {

                                    try {
                                        vValueChatPage = chatPrinter.scrollPane.getVvalue();
                                        new deathPages();
                                        new chatPage(AuthKey, myUsername, userUsername);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();


        picture.setOnAction(event -> {
            if (selectIcon.ImageIm == null) {
                try {
                    selectIcon si = new selectIcon();
                    si.SelectImage();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Error in open select image");
                }
            } else {
                try {
                    new deathPages();
                    new chatPage(AuthKey, myUsername, userUsername);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        send.setOnAction(event -> {
            new sendMessage(messageText.getText());
        });
        selectIcon.fileIm = null;
        selectIcon.ImageIm = null;

        view.scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                new sendMessage(messageText.getText());
            }
        });

        Back.setOnAction(event -> {
            new deathPages();
            try {
                vValueChatPage = 1;
                new Messenger(AuthKey);
            } catch (Exception e) {
                logger.error("Error in back to chat list page");
            }
        });
        new chatContoller(AuthKey, myUsername, userUsername);

        Platform.runLater(() -> {
            chatPrinter.scrollPane.setVvalue(vValueChatPage);
        });
    }


}
