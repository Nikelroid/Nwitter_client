package mainPages;

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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import launch.view;
import loadTwittes.jsonDecoder;
import loadTwittes.loadConnection;
import login.loginConnection;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;
import twitteControl.Menu;
import twitteControl.TwitteController;

import twitteControl.deathPages;
import userControl.userFinder;
import userControl.userProfile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Expelorer {

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
    public static double vValueExpelorer;
    public static Timeline updateChecker;

    Menu menu = new Menu();
    AnchorPane SearchBar;
    String username;
    int counter = 1;
    String[] mapper = new String[10000];
    jsonUsers Users_get = new jsonUsers();
    List<objUsers> users = Users_get.get();
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    Feed feed = new Feed();
    ArrayList<Integer> retorder = new ArrayList<Integer>();
    int target = 0;
    int tar = 0;
    submitMessage sb = new submitMessage();
    int[] cheat = new int[10000];
    int sn = 1;
    private static final Logger logger = LogManager.getLogger(Expelorer.class);

    public Expelorer(String AuthKey) throws Exception {
        logger.info("System: user went to mainPages.Expelorer");
        this.username = AuthKey;

            launch.view.root = null;
            launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));

            launch.view.scene = new Scene(launch.view.root);
            launch.view.stage.setScene(launch.view.scene);
            launch.view.stage.setTitle("Explorer");
            launch.view.stage.show();

            userFinder uf = new userFinder();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
            Exit.setCursor(Cursor.HAND);
            Exit.setOnMouseClicked(event -> {
                int response = JOptionPane.showConfirmDialog(null,
                        "Do you want to exit the app and stay online or revoke?");
                if (response==0){
          new deathPages();
                    try {
                        Expelorer.vValueExpelorer = TwitteController.scrollPane.getVvalue();

                        new Expelorer(AuthKey);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }else if (response==1) {
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
            loadConnection.get("explorer", AuthKey);

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
                                    loadConnection.get("explorer", AuthKey);
                                    if (!serials.equals(jsonDecoder.serials)
                                            || !likes.equals(jsonDecoder.likes)
                                            || !comments.equals(jsonDecoder.comments)
                                            || !retwittes.equals(jsonDecoder.retwittes)
                                            || !isSaved.equals(jsonDecoder.isSaved)) {
                                        try {
                                            updateChecker.stop();
                                            Expelorer.vValueExpelorer = TwitteController.scrollPane.getVvalue();
                                            new Expelorer(AuthKey);
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

        SearchBar = FXMLLoader.load(getClass().getResource("/layout/header/search_bar.fxml"));
        TwitteController.twitteList.getChildren().add(SearchBar);

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
        searching(AuthKey);
        launch.view.scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        try {
                            searching(AuthKey);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            Platform.runLater(()->{
                TwitteController.scrollPane.setVvalue(Expelorer.vValueExpelorer);
            });


        Button searchButton = (Button) SearchBar.lookup("#search_button");
        searchButton.setOnAction(event -> {

            try {
                searching(AuthKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void searching(String AuthKey) {

        TextField searchField = (TextField) SearchBar.lookup("#search_field");
            String response = searchField.getText();
            if (!response.isEmpty()) {
                new deathPages();
                try {
                    new userProfile(AuthKey, response);
                } catch (Exception e) {
                    logger.error("error in load user profile");
                    e.printStackTrace();
                }
            }
            }
    }


