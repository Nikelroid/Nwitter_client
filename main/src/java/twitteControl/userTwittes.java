package twitteControl;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import launch.view;
import loadTwittes.jsonDecoder;
import loadTwittes.loadConnection;
import mainPages.Feed;
import mainPages.Info;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;
import userControl.userProfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class userTwittes {

    ArrayList<String> usernames;
    ArrayList<String> text;
    ArrayList<String> date;
    ArrayList<String> serials;
    ArrayList<Integer> likes;
    ArrayList<Integer> comments;
    ArrayList<Integer> retwittes;
    ArrayList<Boolean> isSaved;
    ArrayList<Boolean> isLiked;
    ArrayList<Boolean> isRetwitted;
    public static double vValueFeed;
    public static Timeline updateChecker;
    Menu menu = new Menu();
    int counter = 1;
    String[] mapper = new String[10000];
    jsonTwittes Get = new jsonTwittes();
    private static final Logger logger = LogManager.getLogger(userTwittes.class);

    public userTwittes(String Username, String AuthKey,boolean self) throws Exception {

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
        if(self)
            loadConnection.get("myTwittes", AuthKey);
        else{
            loadConnection.get("userTwittes", Username,AuthKey,false);
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
                                if(self)
                                    loadConnection.get("myTwittes", AuthKey);
                                else
                                    loadConnection.get("userTwittes", Username,AuthKey,false);

                                if (!serials.equals(jsonDecoder.serials)
                                        || !likes.equals(jsonDecoder.likes)
                                        || !comments.equals(jsonDecoder.comments)
                                        || !retwittes.equals(jsonDecoder.retwittes)
                                        || !isSaved.equals(jsonDecoder.isSaved)) {
                                    try {
                                        if (Info.updateChecker!=null)
                                        Info.updateChecker.stop();
                                        if (userProfile.updateChecker!=null)
                                            userProfile.updateChecker.stop();
                                        updateChecker.stop();
                                        userTwittes.vValueFeed = TwitteController.scrollPane.getVvalue();
                                        if (self)new Info(AuthKey);
                                        else new userProfile(AuthKey,Username);
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

        if (serials.size()==0) new userTwittes(false);
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


        Platform.runLater(() -> {
            TwitteController.scrollPane.setVvalue(userTwittes.vValueFeed);
        });
    }

    public userTwittes(boolean exists) throws IOException {
        if (exists)
               new TwitteController(-1);
        else
            new TwitteController();
    }
}

