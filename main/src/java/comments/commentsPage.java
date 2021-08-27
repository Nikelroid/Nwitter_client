package comments;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import loadTwittes.jsonDecoderComments;
import loadTwittes.loadConnection;
import login.loginConnection;
import mainPages.Feed;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.commentsController;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class commentsPage {

    public static double vValueFeed;
    public static Timeline updateChecker;
    public static double vValueCat;

    commentButtons commentButtons = new commentButtons();
    String username;
    List<Integer> serial;
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    userFinder uf = new userFinder();
    jsonUsers Getu = new jsonUsers();
    List<objUsers> users = Getu.get();
    loadTwittes.loadConnection loadConnection = new loadConnection();
    int counter = 1;
    private static final Logger logger = LogManager.getLogger(commentsPage.class);

    String[] mapper = new String[10000];
    int target;

    public static Stage cStage;
    public static Parent cRoot;
    public static Scene cScene;

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

    public commentsPage(String serial, String AuthKey, boolean isRefresh) throws Exception {

        cRoot = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        cScene = new Scene(cRoot);
        cStage = new Stage();
        cStage.setScene(cScene);
        cStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));

        cStage.show();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) cRoot.lookup("#exit") ;
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int res = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online or revoke?");
            if (res==0){
                try {
                    cScene.getWindow().hide();
                    new commentsPage(serial,AuthKey,isRefresh);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }else if (res==1) {
                loginConnection loginConnection = new loginConnection();
                loginConnection.connection(AuthKey,"-");
                System.exit(1);
            }
        });

        usernames = new ArrayList<>();
        text = new ArrayList<>();
        date = new ArrayList<>();
         serials = new ArrayList<>();
         likes = new ArrayList<>();
         comments = new ArrayList<>();
         retwittes = new ArrayList<>();
         isSaved = new ArrayList<>();
         isLiked = new ArrayList<>();
         isRetwitted = new ArrayList<>();
        
        logger.info("System: user went to comments.commentsPage");

        userFinder userFinder = new userFinder();
        target = userFinder.UserFinder(username);

        Button Back = (Button) cScene.lookup("#back");
        Back.setText("Back");
        Label Header = (Label) cScene.lookup("#header");
        Header.setText("comments page");

        Back.setOnAction(event -> {
            new deathPages();
            Back.getScene().getWindow().hide();
        });


        commentsController.likeList = new ArrayList<>();
        commentsController.likeAction = new ArrayList<>();
        commentsController.commentList = new ArrayList<>();
        commentsController.commentAction = new ArrayList<>();
        commentsController.retwitteList = new ArrayList<>();
        commentsController.retwitteAction = new ArrayList<>();
        commentsController.Share = new ArrayList<>();
        commentsController.Save = new ArrayList<>();
        commentsController.Report = new ArrayList<>();
        commentsController.twitteImages = new ArrayList<>();
        commentsController.Link = new ArrayList<>();

        loadConnection.get("comments", AuthKey, serial,true);

        usernames = jsonDecoderComments.usernames;
        text = jsonDecoderComments.text;
        date = jsonDecoderComments.date;
        serials = jsonDecoderComments.serials;
        likes = jsonDecoderComments.likes;
        comments = jsonDecoderComments.comments;
        retwittes = jsonDecoderComments.retwittes;
        isSaved = jsonDecoderComments.isSaved;
        isLiked = jsonDecoderComments.isLiked;
        isRetwitted = jsonDecoderComments.isRetwitted;

        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                loadConnection.get("comments", AuthKey, serial,true);
                                if (!serials.equals(jsonDecoderComments.serials)
                                        || !likes.equals(jsonDecoderComments.likes)
                                        || !comments.equals(jsonDecoderComments.comments)
                                        || !retwittes.equals(jsonDecoderComments.retwittes)
                                        || !isSaved.equals(jsonDecoderComments.isSaved)) {
                                    try {
                                        updateChecker.stop();
                                        commentsPage.vValueFeed = commentsController.scrollPane.getVvalue();
                                        Back.getScene().getWindow().hide();
                                        new commentsPage(serial, AuthKey,true);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();

        commentsController.scrollPane = (ScrollPane) cScene.lookup("#scobar");
        commentsController.twitteList = (VBox) commentsController.scrollPane.lookup("#twittelist");
        commentsController.twitteList.getChildren().removeAll();

        for (int i = 0; i < serials.size(); i++) {
            try {
                new commentsController(counter,
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
            commentButtons.buttonsStyleSetter(counter,isLiked.get(i),isSaved.get(i),isRetwitted.get(i));
            counter++;
            mapper[counter] = serials.get(i);
        }
        Platform.runLater(() -> {
            commentsController.scrollPane.setVvalue(commentsPage.vValueFeed);
        });
        commentButtons.buttonCommandSetter(serial,AuthKey,Back, counter,mapper);
    }
}
