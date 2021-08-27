package mainPages;

import offlineMessages.getMessageThread;
import offlineMessages.getMessagesConnection;
import offlineSetting.getSettingConnection;
import offlineSetting.setSettingThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import launch.view;
import loadTwittes.jsonDecoder;
import loadTwittes.loadConnection;
import login.loginConnection;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.Menu;
import twitteControl.TwitteController;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public  class Feed {

    ArrayList<String> usernames ;
    ArrayList<String> text ;
    ArrayList<String> date ;
    ArrayList<String> serials ;
    ArrayList<Integer> likes ;
    ArrayList<Integer> comments ;
    ArrayList<Integer> retwittes ;
    ArrayList<Boolean> isSaved ;
    ArrayList<Boolean> isLiked ;
    ArrayList<Boolean> isRetwitted ;
    public static double vValueFeed;
    public static Timeline updateChecker;


    public Feed() {
    }
    Menu menu = new Menu();
    int counter = 1;
    public static String username;
    String[] mapper = new String[10000];

    private static final Logger logger = LogManager.getLogger(Feed.class);

    public Feed(String AuthKey) throws Exception {

        new getSettingConnection(AuthKey);
        new getMessagesConnection(AuthKey);

        getMessageThread getMessageThread = new getMessageThread();
        if (!getMessageThread.isAlive())
        getMessageThread.start();

        setSettingThread setSettingThread = new setSettingThread();
        if (!setSettingThread.isAlive())
            setSettingThread.start();

        Feed.username =AuthKey;
        logger.info("System: user went to mainPages.Feed");


            try {
                view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            view.scene = new Scene(view.root);
            view.stage.setScene(view.scene);
            view.stage.setTitle("Feed");
            view.stage.show();
            Label Header = (Label) view.scene.lookup("#header");
            Header.setText("Feed");

            LocalDateTime now = LocalDateTime.now();
            ImageView Exit = (ImageView) view.root.lookup("#exit");
            Exit.setCursor(Cursor.HAND);
            Exit.setOnMouseClicked(event -> {
                int response = JOptionPane.showConfirmDialog(null,
                        "Do you want to exit the app and stay online or revoke page?");
                if (response == 0) {
                    try {
                        Feed.vValueFeed = TwitteController.scrollPane.getVvalue();
                        new Feed(AuthKey);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else if (response == 1) {
                    loginConnection loginConnection = new loginConnection();
                    loginConnection.connection(AuthKey,"-");
                    System.exit(1);
                }
            });


            TwitteController.likeList = new ArrayList<>();
            TwitteController.likeAction = new ArrayList<>();
            TwitteController.commentList = new ArrayList<>();
            TwitteController.commentAction = new ArrayList<>();
            TwitteController.retwitteList = new ArrayList<>();
            TwitteController.retwitteAction = new ArrayList<>();
            TwitteController.Share = new ArrayList<>();
            TwitteController.Save = new ArrayList<>();
            TwitteController.Report = new ArrayList<>();
            TwitteController.twitteImages = new ArrayList<>();
        TwitteController.Link = new ArrayList<>();

            loadConnection loadConnection = new loadConnection();
            loadConnection.get("feed", AuthKey);

        if (jsonDecoder.usernames==null){
            new Messenger(AuthKey);
            return;
        }
            usernames = jsonDecoder.usernames;
            text = jsonDecoder.text;
            date = jsonDecoder.date;
            serials = jsonDecoder.serials;
            likes = jsonDecoder.likes;
            comments = jsonDecoder.comments;
            retwittes = jsonDecoder.retwittes;
            isSaved = jsonDecoder.isSaved;
            isLiked = jsonDecoder.isLiked;
            isRetwitted = jsonDecoder.isRetwitted;


         updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                loadConnection.get("feed", AuthKey);
                                if (!serials.equals(jsonDecoder.serials)
                                        || !likes.equals(jsonDecoder.likes)
                                        || !comments.equals(jsonDecoder.comments)
                                        || !retwittes.equals(jsonDecoder.retwittes)
                                        || !isSaved.equals(jsonDecoder.isSaved)) {
                                    try {
                                        updateChecker.stop();
                                        Feed.vValueFeed = TwitteController.scrollPane.getVvalue();
                                        new Feed(AuthKey);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();

        TwitteController.scrollPane = (ScrollPane) view.scene.lookup("#scobar");
        TwitteController.twitteList = (VBox) TwitteController.scrollPane.lookup("#twittelist");
        TwitteController.twitteList.getChildren().removeAll();

        for (int i = 0; i < serials.size(); i++) {
            try {
                new TwitteController(counter,
                        usernames.get(i),
                        text.get(i),
                        date.get(i),
                        serials.get(i),
                        likes.get(i),
                        comments.get(i),
                        retwittes.get(i),
                        isSaved.get(i),
                        isLiked.get(i),
                        isRetwitted.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
            mapper[counter] = serials.get(i);
        }


        menu.Menu_command(AuthKey);
        menu.twitteButtons(AuthKey, mapper, counter);


        Platform.runLater(()->{
            TwitteController.scrollPane.setVvalue(Feed.vValueFeed);
        });


    }



}
