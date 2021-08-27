package mainPages;

import chat.chatPage;
import chat.newChat;
import offlineMessages.deleteChat;
import groups.gpMain;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jsonContoller.jsonMessage;
import login.loginConnection;
import messengerChilds.massMessenger;
import messengerChilds.savedTwittes;
import objects.objMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.Menu;
import twitteControl.deathPages;
import userControl.usersPrint;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class Messenger {


    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    ArrayList<String> retorder = new ArrayList<String>();
    String username;
    AnchorPane card;
    public static Timeline updateChecker;
    FXMLLoader fXMLLoader = new FXMLLoader();
    public static ArrayList<Button> actionButton;

    int target;
    int counter=0;
    int nonseen;
    int [] mapper = new int[10000];
    private static final Logger logger = LogManager.getLogger(Messenger.class);

    private void adder () {
        ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().add(card);
    }

    public Messenger(String AuthKey) throws Exception {

        launch.authKey.getter();
        username = launch.authKey.username;

        logger.info("System: user went to mainPages.Messenger");

        retorder = new ArrayList<String>();
        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Messenger");
        launch.view.stage.show();

        ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (response==0){
          new deathPages();
                System.exit(1);
            }else if (response==1) {
                loginConnection loginConnection = new loginConnection();
                loginConnection.connection(AuthKey,"-");
                System.exit(1);
            }
        });

        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText("Messenger");
        actionButton = new ArrayList<>();
        var menu = new Menu();
        menu.Menu_command(AuthKey);

        card = FXMLLoader.load(getClass().getResource("/layout/header/messenger_header.fxml"));
        Button New = (Button) card.lookup("#new");
        Button Saved = (Button) card.lookup("#saved");
        Button Mass = (Button) card.lookup("#mass");
        Button savedT = (Button) card.lookup("#saved_twittes");
        Button Gp = (Button) card.lookup("#gp");
        adder();
        New.setOnAction(event -> {
            new deathPages();
            try {
                new newChat(AuthKey);
            } catch (Exception e) {
                logger.error("Error in open new chat");
            }
        });
        Saved.setOnAction(event -> {
            new deathPages();
            try {
                new chatPage(AuthKey,username,username);
            } catch (Exception e) {
                e.printStackTrace();
               logger.error("Error in open saved message");
            }
        });
        Gp.setOnAction(event -> {
            new deathPages();
                try {
                    new gpMain(AuthKey);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error in open Group message");
                }
        });
        savedT.setOnAction(event -> {
            new deathPages();
                try {
                    new savedTwittes(AuthKey);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error in open saved twittes");
                }
        });
        Mass.setOnAction(event -> {
            new deathPages();
            try {
                new massMessenger(username);
            } catch (Exception e) {
                logger.error("Error in open mass messenger");
            }
        });


        userControl.usersPrint.Delete=new ArrayList<>();
        userControl.usersPrint.user = new ArrayList<>();
        String contact = null;


        File file = new File("Message.json");

        if (!file.exists()) {
        messages = new ArrayList<>();
        }else {
            messages = get_mess.get();
            while (messages == null) {
                messages = get_mess.get();
            }
        }


            for (int i = messages.size() - 1; i >= 0; i--)
                if (!messages.get(i).getReceiver().equals(username)
                        || !messages.get(i).getSender().equals(username)) {
                    if (messages.get(i).getReceiver().equals(username))
                        contact = messages.get(i).getSender();
                    else
                        contact = messages.get(i).getReceiver();
                    if (messages.get(i).getReceiver().equals(username)
                            && !retorder.contains(contact)
                            || (messages.get(i).getSender().equals(username)
                            && !retorder.contains(contact))) {

                        nonseen = 0;
                        for (int k = messages.size() - 1; k >= 0; k--) {
                            if (messages.get(k).getReceiver().equals(username) &&
                                    messages.get(k).getSender().equals(contact) &&
                                    !messages.get(k).isSeen()) nonseen++;
                        }

                        if (nonseen != 0) {
                            new usersPrint(contact, nonseen, counter);
                            retorder.add(contact);
                            mapper[counter] = i;
                            counter++;
                        }
                    }
                }

            for (int i = messages.size() - 1; i >= 0; i--) {
                if ((!messages.get(i).getReceiver().equals(username)
                        || !messages.get(i).getSender().equals(username))
                        && messages.get(i).getReceiver().charAt(0) != '*'
                        && messages.get(i).getSender().charAt(0) != '*') {
                    if (messages.get(i).getReceiver().equals(username))
                        contact = messages.get(i).getSender();
                    else
                        contact = messages.get(i).getReceiver();
                    if (!retorder.contains(contact)) {
                        new usersPrint(contact, 0, counter);
                        retorder.add(contact);
                        mapper[counter] = i;
                        counter++;
                    }
                } else if (messages.get(i).getReceiver().charAt(0) == '*'
                        || messages.get(i).getSender().charAt(0) == '*') {
                    if (!retorder.contains(messages.get(i).getReceiver())) {
                        if (messages.get(i).getReceiver().equals(username)) {
                            new usersPrint("Group " + messages.get(i).getSender().substring(1),
                                    0, counter);
                            retorder.add(messages.get(i).getSender());
                        } else {
                            new usersPrint("Group " + messages.get(i).getReceiver().substring(1),
                                    0, counter);
                            retorder.add(messages.get(i).getReceiver());
                        }

                        mapper[counter] = i;
                        counter++;
                    }
                }
            }


        List<String> text = new ArrayList<>();

            for (int i = 0; i < messages.size(); i++)
                text.add(messages.get(i).getText());






        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {


                                messages = get_mess.get();
                                File file = new File("Message.json");
                                if (!file.exists()){
                                    messages= new ArrayList<>();
                                }
                                List<String> tempText = new ArrayList<>();

                                for (int i = 0; i < messages.size(); i++)
                                    tempText.add(messages.get(i).getText());

                                if (!text.toString().equals(tempText.toString())) {
                                    try {

                                        new deathPages();
                                        new Messenger(AuthKey);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();


        for (int i = 0; i < usersPrint.Delete.size(); i++) {
            final int finalI=i;
            usersPrint.Delete.get(i).setOnAction(event -> {
            logger.info("User wants delete a chat");
                int input = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want delete your chat with " +
                                retorder.get(finalI)+
                        " ?");
                if (input==0){
                    jsonMessage.messageLock.lock();
                    try {
                        for (int k = 0; k < messages.size(); k++) {
                            for (int j = 0; j < messages.size(); j++) {
                                if (messages.get(j).getSender().equals(retorder.get(finalI))
                                        && messages.get(j).getReceiver().equals(AuthKey)
                                        || messages.get(j).getReceiver().equals(retorder.get(finalI))
                                        && messages.get(j).getSender().equals(AuthKey)) {
                                    messages.remove(j);
                                    k = 0;
                                    break;
                                }
                            }
                        }
                        new jsonMessage(messages);
                        new deleteChat(AuthKey, retorder.get(finalI));
                    }finally {
                        jsonMessage.messageLock.unlock();
                    }
                }
        });





            usersPrint.user.get(i).setOnMouseClicked(mouseEvent -> {
                new deathPages();
                if (messages.get(mapper[finalI]).getSender().equals(username))
                try {
                    new chatPage(AuthKey,username,retorder.get(finalI));
                } catch (Exception e) {
                   e.printStackTrace();
                }
                else
                    try {
                        new chatPage(AuthKey,username,retorder.get(finalI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            });

            usersPrint.user.get(i).setOnMouseEntered(mouseEvent -> {
                launch.view.scene.setCursor(Cursor.HAND);
                usersPrint.user.get(finalI).getStyleClass().remove("username");
                usersPrint.user.get(finalI).getStyleClass().add("focused");
            });
            usersPrint.user.get(i).setOnMouseExited(mouseEvent -> {
                launch.view.scene.setCursor(Cursor.DEFAULT);
                usersPrint.user.get(finalI).getStyleClass().add("username");
                usersPrint.user.get(finalI).getStyleClass().remove("focused");
            });
    }





        }
}
