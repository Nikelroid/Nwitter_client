package lists;

import connection.sender;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jsonContoller.jsonUsers;
import mainPages.Feed;
import objects.objUsers;
import operation.jsonCreatorOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import submit.listJson;
import twitteControl.TwitteController;
import twitteControl.deathPages;
import userControl.userFinder;
import userControl.userProfile;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Lists {
    int counter;
    public static String type;
    private static final Logger logger = LogManager.getLogger(Lists.class);
    connection.sender sender = new sender();
    submit.listJson listJson = new listJson();
    public static Timeline updateChecker;
    public static double vValueFeed;
    public Lists(String username, String AuthKey, int response) throws Exception {

        listPrinter.followButton = new ArrayList<>();
        logger.info("System: user went to lists.Lists");
        counter = 1;
        var get_j = new jsonUsers();
        List<objUsers> users = get_j.get();
        userFinder us = new userFinder();

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));

        launch.view.stage.show();

        userFinder uf = new userFinder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) launch.view.root.lookup("#exit");
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int res = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (res == 0) {
                try {
                    new Lists(username,AuthKey,response);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (res == 1) {
                users.get(uf.UserFinder(username)).setLastseen(dtf.format(now));
                new jsonUsers(users);
                System.exit(1);
            }
        });

        Label Header = (Label) launch.view.scene.lookup("#header");
        Button Back = (Button) launch.view.scene.lookup("#back");
        var lp = new listPrinter();
        lp.difiner();
        Back.setText("Back to user page");
        Back.setOnAction(event -> {
            new deathPages();
            try {
                new userProfile(AuthKey, username);
            } catch (Exception e) {
                logger.error("Error in back to user page");
            }
        });

        switch (response) {
            case 1:
                Header.setText(username + "'s Followings");
                type = "followings";
                break;
            case 2:
                Header.setText(username + "'s Followers");
                type = "followers";
                break;
            case 3:

                Header.setText(username + "s Block list:");
                type = "blocks";
                break;
            case 4:
                    Header.setText(username + "s Mutes list:");
                    type = "mutes";
                    break;

        }

        listPrinter.scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        listPrinter.twitteList = (VBox) listPrinter.scrollPane.lookup("#twittelist");
        listPrinter.twitteList.getChildren().removeAll();

        JSONObject likeRequest = listJson.get(type,username);
        JSONObject outputJson = sender.send(likeRequest);
        new jsonListDecoder(outputJson);

        ArrayList<String> Username = jsonListDecoder.username;
        ArrayList<String> Name = jsonListDecoder.name;
        ArrayList<String> Pic = jsonListDecoder.pic;

        for (int i = 0; i < jsonListDecoder.username.size(); i++) {
            new listPrinter(jsonListDecoder.username.get(i),jsonListDecoder.name.get(i),Pic.get(i));
            final int finalI = i;
            listPrinter.followButton.get(i+1).setText("Profile");
            listPrinter.followButton.get(i+1).setOnAction(event -> {
                if(userProfile.updateChecker!=null)
                userProfile.updateChecker.stop();
                if(Lists.updateChecker!=null)
                Lists.updateChecker.stop();
                try {
                    new userProfile(AuthKey,jsonListDecoder.username.get(finalI));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }
        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                String var;


                                JSONObject likeRequest = listJson.get(type,username);
                                JSONObject outputJson = sender.send(likeRequest);
                                new jsonListDecoder(outputJson);

                                if (!Username.equals(jsonListDecoder.username)
                                        || !Name.equals(jsonListDecoder.name)
                                        || !Pic.equals(jsonListDecoder.pic)) {
                                    try {
                                        updateChecker.stop();
                                        Lists.vValueFeed = listPrinter.scrollPane.getVvalue();
                                        new Lists(username,AuthKey,response);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();
        Platform.runLater(()->{
            listPrinter.scrollPane.setVvalue(Lists.vValueFeed);
        });
    }
}

