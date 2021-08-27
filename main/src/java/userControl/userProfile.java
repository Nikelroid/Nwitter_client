package userControl;

import chat.chatPage;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import launch.authKey;
import launch.view;
import lists.Lists;
import login.loginConnection;
import mainPages.Expelorer;
import mainPages.Info;
import objects.objUsers;
import operation.jsonCreatorOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import profiles.getImageFile;
import twitteControl.Menu;
import twitteControl.TwitteController;
import twitteControl.deathPages;
import twitteControl.userTwittes;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class userProfile {

    String Username;
    String Name;
    String Email;
    String Phonenumber;
    String Birthday;
    String Bio;
    boolean account;
    boolean enable;
    String lastseen;
    boolean isFollowing;
    boolean isMuted;
    boolean isblockd;
    boolean followedBy;
    boolean mutedBy;
    boolean blockdBy;
    int followers;
    int followings;
    int blocks;
    int mutes;
    boolean isRequested;
    String pic;

    int target = 0;
    private static final Logger logger = LogManager.getLogger(userProfile.class);
    Label followsYou, blocksYou, mutesYou;
    public Button followerCount, followingCount, blockCount, muteCount, followAction, blockAction, muteAction, removeAction, sendMessage;
    List<objUsers> users;
    Menu menu = new Menu();
    JSONObject userInfo = null;
    sender sender = new sender();
    public static double vValueFeed;
    public static Timeline updateChecker;
    jsonCreatorOperator operarator = new jsonCreatorOperator();


    public void resetCounts() {
        followerCount.setText(this.followers + "");
        followingCount.setText(this.followings + "");
        blockCount.setText(this.blocks + "");
        muteCount.setText(this.mutes + "");
    }

    public userProfile(String myAuthKey, String username) throws Exception {

        userJsonDecoder.Username = "";
        jsonGeneratorUser userJson = new jsonGeneratorUser();
        userInfo = sender.send(userJson.generate(myAuthKey, username));
        new userJsonDecoder(userInfo);

        if (userJsonDecoder.Username.equals("-")){
            JOptionPane.showMessageDialog(null, "Username or name not found");
            return;
        }
//        if (userJsonDecoder.AuthKey.equals(myAuthKey)) {
//            new Info(myAuthKey);
//            return;
//        }

        this.Username = userJsonDecoder.Username;
        this.Name = userJsonDecoder.Name;
        this.Email = userJsonDecoder.Email;
        this.Phonenumber = userJsonDecoder.Phonenumber;
        this.Birthday = userJsonDecoder.Birthday;
        this.Bio = userJsonDecoder.Bio;
        this.account = userJsonDecoder.account;
        this.enable = userJsonDecoder.enable;
        this.lastseen = userJsonDecoder.lastseen;
        this.isFollowing = userJsonDecoder.isFollowing;
        this.isMuted = userJsonDecoder.isMuted;
        this.isblockd = userJsonDecoder.isblockd;
        this.followedBy = userJsonDecoder.followedBy;
        this.mutedBy = userJsonDecoder.mutedBy;
        this.blockdBy = userJsonDecoder.blockdBy;
        this.followers = userJsonDecoder.followers;
        this.followings = userJsonDecoder.followings;
        this.blocks = userJsonDecoder.blocks;
        this.mutes = userJsonDecoder.mutes;
        this.isRequested = userJsonDecoder.isRequested;
        this.pic = userJsonDecoder.pic;

        if(blockdBy||!enable){
            JOptionPane.showMessageDialog(null,"You cant see this page.");
            new deathPages();
            new Expelorer(myAuthKey);
            return;
        }

        logger.info("System: user went to userControl.userProfile");

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/user_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.show();

        userFinder uf = new userFinder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) launch.view.root.lookup("#exit");
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online or revoke?");
            if (response == 0) {
                try {
                    userProfile.vValueFeed = TwitteController.scrollPane.getVvalue();
                    new userProfile(myAuthKey,username);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (response == 1) {
                loginConnection loginConnection = new loginConnection();
                loginConnection.connection(myAuthKey,"-");
                System.exit(1);
            }
        });

        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText("  @" + this.Username);
        TwitteController.likeList = new ArrayList<>();
        TwitteController.likeAction = new ArrayList<>();
        TwitteController.commentList = new ArrayList<>();
        TwitteController.commentAction = new ArrayList<>();
        TwitteController.retwitteList = new ArrayList<>();
        TwitteController.retwitteAction = new ArrayList<>();
        TwitteController.Share = new ArrayList<>();
        TwitteController.Save = new ArrayList<>();
        TwitteController.Link = new ArrayList<>();

        TwitteController.twitteImages = new ArrayList<>();

        followerCount = (Button) launch.view.root.lookup("#follower_count");
        followingCount = (Button) launch.view.root.lookup("#following_count");
        blockCount = (Button) launch.view.root.lookup("#block_count");
        muteCount = (Button) launch.view.root.lookup("#mute_count");

        followAction = (Button) launch.view.root.lookup("#follow_action");
        blockAction = (Button) launch.view.root.lookup("#block_action");
        muteAction = (Button) launch.view.root.lookup("#mute_action");
        removeAction = (Button) launch.view.root.lookup("#remove_action");
        sendMessage = (Button) launch.view.root.lookup("#send_message");

        followsYou = (Label) launch.view.root.lookup("#follows_you");
        mutesYou = (Label) launch.view.root.lookup("#mutes_you");
        blocksYou = (Label) launch.view.root.lookup("#blocks_you");




        getImageFile getProfileFile= new getImageFile();
        File file = getProfileFile.profile(Username);
        Image image = new Image(file.toURI().toString());



        ImageView Icon = (ImageView) launch.view.root.lookup("#picture");
        try {
            Icon.setImage(image);
        } catch (NullPointerException ignored) {
        }



        if (!userJsonDecoder.pic.isEmpty()) {
            Icon.setOnMouseClicked(mouseEvent -> {
                try {
                    new selectIcon(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Error in open select image");
                }
            });
        }

        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                userInfo = sender.send(userJson.generate(myAuthKey, Username));
                                new userJsonDecoder(userInfo);
                                if (!Username.equals(userJsonDecoder.Username)
                                        || !Name.equals(userJsonDecoder.Name)
                                        || !Email.equals(userJsonDecoder.Email)
                                        || !Phonenumber.equals(userJsonDecoder.Phonenumber)
                                        || !Birthday.equals(userJsonDecoder.Birthday)
                                        || !Bio.equals(userJsonDecoder.Bio)
                                        || account != userJsonDecoder.account
                                        || enable != userJsonDecoder.enable
                                        || !lastseen.equals(userJsonDecoder.lastseen)
                                        || isFollowing != userJsonDecoder.isFollowing
                                        || isMuted != userJsonDecoder.isMuted
                                        || isblockd != userJsonDecoder.isblockd
                                        || followedBy != userJsonDecoder.followedBy
                                        || mutedBy != userJsonDecoder.mutedBy
                                        || blockdBy != userJsonDecoder.blockdBy
                                        || followers != userJsonDecoder.followers
                                        || followings != userJsonDecoder.followings
                                        || blocks != userJsonDecoder.blocks
                                        || mutes != userJsonDecoder.mutes
                                        || isRequested != userJsonDecoder.isRequested
                                        || !pic.equals(userJsonDecoder.pic)) {
                                    try {
                                        updateChecker.stop();
                                        if (userTwittes.updateChecker!=null)
                                        userTwittes.updateChecker.stop();
                                        userProfile.vValueFeed = TwitteController.scrollPane.getVvalue();
                                        new userProfile(myAuthKey, Username);
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

        new userController(Username);


        resetCounts();
        buttonsSetter();

        menu.Menu_command(myAuthKey);

        followAction.setOnAction(event -> {
            try {
                if (sender.send(operarator.generate("follow", myAuthKey, Username))
                        .get("result").equals("0"))
                    JOptionPane.showMessageDialog(null, "error in connection. try again!");

            }catch (JSONException ignored){
                ignored.printStackTrace();
            }

        });


        blockAction.setOnAction(event -> {
            try {
                if (sender.send(operarator.generate("block", myAuthKey, Username))
                        .get("result").equals("0"))
                    JOptionPane.showMessageDialog(null, "error in connection. try again!");

            }catch (JSONException ignored){
                ignored.printStackTrace();
            }

        });
        muteAction.setOnAction(event -> {

            try {
                if (sender.send(operarator.generate("mute", myAuthKey, Username))
                        .get("result").equals("0"))
                    JOptionPane.showMessageDialog(null, "error in connection. try again!");

            }catch (JSONException ignored){
                ignored.printStackTrace();
            }
       });

        removeAction.setOnAction(event -> {
            try {
                if (sender.send(operarator.generate("remove", myAuthKey, Username))
                        .get("result").equals("0"))
                    JOptionPane.showMessageDialog(null, "error in connection. try again!");

            }catch (JSONException ignored){
                ignored.printStackTrace();
            }

        });

        sendMessage.setOnAction(event -> {

                try {
                    new chatPage(myAuthKey,launch.authKey.username, Username);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            buttonsSetter();
            resetCounts();
        });

        followingCount.setOnAction(event -> {
            try {
                if (account || isFollowing)
                    if (followings == 0) {
                        JOptionPane.showMessageDialog(null, Username+" haven't any following");
                    } else {
                    new deathPages();
                    userTwittes.updateChecker.stop();
                    new Lists(Username, myAuthKey, 1);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        followerCount.setOnAction(event -> {

                try {
                    if (account || isFollowing)
                        if (followers == 0) {
                            JOptionPane.showMessageDialog(null, Username+" haven't any followers :(");
                        } else{
                            new deathPages();
                        userTwittes.updateChecker.stop();
                        new Lists(Username, myAuthKey, 2);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

        });
        blockCount.setOnAction(event -> {
            try {
                if (account || isFollowing)
                    if (blocks == 0) {
                    JOptionPane.showMessageDialog(null, Username+" haven't block anybody");
                } else {
                        new deathPages();
                    userTwittes.updateChecker.stop();
                    new Lists(Username, myAuthKey, 3);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        muteCount.setOnAction(event -> {
             try {
                if (account || isFollowing)
                    if (mutes == 0) {
                        JOptionPane.showMessageDialog(null, Username+" haven't mute anybody");
                    } else{
                        new deathPages();
                    userTwittes.updateChecker.stop();
                    new Lists(Username, myAuthKey, 4);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        Platform.runLater(() -> {
            TwitteController.scrollPane.setVvalue(userProfile.vValueFeed);
        });
        if (account || isFollowing)
            new userTwittes(Username, myAuthKey,false);
        else
            new userTwittes(true);
    }


    public void buttonsSetter() {

        if (!isFollowing) {
            if (this.account) {
                followAction.setText("Follow");
                followAction.getStyleClass().remove("logined");
            } else {
                if (isRequested) {
                    followAction.setText("UnRequest");
                    if (!followAction.getStyleClass().contains("logined"))
                        followAction.getStyleClass().add("logined");
                } else {
                    followAction.setText("Request");
                    followAction.getStyleClass().remove("logined");
                }
            }
        } else {
            blockAction.setText("Block");
            blockAction.getStyleClass().remove("logined");
            followAction.setText("Unfollow");
            if (!followAction.getStyleClass().contains("logined"))
                followAction.getStyleClass().add("logined");
        }

        if (this.isblockd) {
            if (this.account) {
                followAction.setText("Follow");
                followAction.getStyleClass().remove("logined");
            } else {
                followAction.setText("Request");
                followAction.getStyleClass().remove("logined");
            }
            blockAction.setText("Unblock");
            if (!blockAction.getStyleClass().contains("logined"))
                blockAction.getStyleClass().add("logined");
        } else {
            blockAction.setText("Block");
            blockAction.getStyleClass().remove("logined");
        }


        if (this.isMuted) {
            muteAction.setText("Unmute");
            if (!muteAction.getStyleClass().contains("logined"))
                muteAction.getStyleClass().add("logined");
        } else {
            muteAction.setText("Mute");
            muteAction.getStyleClass().remove("logined");
        }

        if (!isFollowing)
            sendMessage.setVisible(false);
        if (!followedBy) removeAction.setVisible(false);
        followsYou.setVisible(followedBy);
        mutesYou.setVisible(mutedBy);
        blocksYou.setVisible(blockdBy);
    }
}



