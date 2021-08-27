package messengerChilds;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import mainPages.Feed;
import mainPages.Info;
import mainPages.Messenger;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.Menu;
import twitteControl.TwitteController;

import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class savedTwittes {


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
    private static double vValueFeed;
    public static Timeline updateChecker;

    Menu menu = new Menu();
    int counter = 1;
    public static String username;
    String[] mapper = new String[10000];

    private static final Logger logger = LogManager.getLogger(Feed.class);

    public savedTwittes(String AuthKey) throws Exception {
        Feed.username =AuthKey;
        logger.info("System: user went to mainPages.Feed");
        int search = 0;
        File f = new File("Twittes.json");
        if (f.exists()) {
            int target = 0;
            userFinder userFinder = new userFinder();
            target = userFinder.UserFinder(AuthKey);


            launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));

            launch.view.scene = new Scene(launch.view.root);
            launch.view.stage.setScene(launch.view.scene);
            launch.view.stage.setTitle("Nwitter");
            launch.view.stage.show();

            userFinder uf = new userFinder();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
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
            Header.setText("Saved Twittes");
            Button Back = (Button) launch.view.scene.lookup("#back");
            Back.setText("Back to Messenger");
            Back.setOnAction(event -> {

                    new deathPages();
                    try {
                        new Messenger(AuthKey);
                    } catch (Exception e) {
                        logger.error("Error in back to chat list page");
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
            loadConnection.get("saved", AuthKey);

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
                                    loadConnection.get("saved", AuthKey);
                                    if (!serials.equals(jsonDecoder.serials)
                                            || !likes.equals(jsonDecoder.likes)
                                            || !comments.equals(jsonDecoder.comments)
                                            || !retwittes.equals(jsonDecoder.retwittes)
                                            || !isSaved.equals(jsonDecoder.isSaved)) {
                                        try {
                                            new deathPages();
                                            savedTwittes.vValueFeed = TwitteController.scrollPane.getVvalue();
                                            new savedTwittes(AuthKey);
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
        }

        menu.twitteButtons(AuthKey, mapper, counter);


        Platform.runLater(()->{
            TwitteController.scrollPane.setVvalue(savedTwittes.vValueFeed);
        });
    }

}
