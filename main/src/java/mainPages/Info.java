package mainPages;

import category.Category;
import chat.chatPage;
import connection.sender;
import edit.editProfile;
import graphics.selectIcon;
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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import launch.authKey;
import launch.view;
import lists.Lists;
import login.loginConnection;
import notifications.Notifications;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import profiles.getImageFile;
import profiles.imageConnection;
import twitteControl.*;
import userControl.*;


import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

public class Info {

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
    public static Button picture;
    profiles.imageConnection imageConnection = new imageConnection();
    Label followsYou, blocksYou, mutesYou;
    public Button followerCount, followingCount, blockCount, muteCount, followAction, blockAction, muteAction, removeAction, sendMessage;
    Menu menu = new Menu();
    JSONObject userInfo = null;
    connection.sender sender = new sender();
    public static double vValueFeed;
    public static Timeline updateChecker;
    public Button categories, savedTwittes, notifications, edit, savedMessages;

    public static ImageView Icon;

    AnchorPane newTwitte;


    public void resetCounts() {
        followerCount.setText(followers + "");
        followingCount.setText(followings + "");
        blockCount.setText(blocks + "");
        muteCount.setText(mutes + "");
    }

    private static final Logger logger = LogManager.getLogger(Info.class);

    public Info(String myAuthKey) throws Exception {

        logger.info("System: user went to userControl.userProfile");
        jsonGeneratorUser userJson = new jsonGeneratorUser();
        userInfo = sender.send(userJson.generate(myAuthKey, "1"));
        new userJsonDecoder(userInfo);

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

        logger.info("System: user went to userControl.userProfile");

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/user_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.show();

        ImageView Exit = (ImageView) launch.view.root.lookup("#exit");
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (response == 0) {
                try {
                    Info.vValueFeed = TwitteController.scrollPane.getVvalue();
                    new Info(myAuthKey);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (response == 1) {
                new deathPages();
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

        Icon = (ImageView) launch.view.root.lookup("#picture");

        categories = (Button) launch.view.root.lookup("#follow_action");
        savedTwittes = (Button) launch.view.root.lookup("#block_action");
        notifications = (Button) launch.view.root.lookup("#mute_action");
        edit = (Button) launch.view.root.lookup("#remove_action");
        savedMessages = (Button) launch.view.root.lookup("#send_message");

        followsYou = (Label) launch.view.root.lookup("#follows_you");
        mutesYou = (Label) launch.view.root.lookup("#mutes_you");
        blocksYou = (Label) launch.view.root.lookup("#blocks_you");

        if (Username==null){
            new Messenger(myAuthKey);
            return;
        }
        new userController(Username);


        resetCounts();
        categories.setText("Categories");
        savedTwittes.setText("Saved twittes");
        edit.setText("Edit profile");
        notifications.setText("Notifications");
        savedMessages.setText("Saved message");


        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(1000),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                userInfo = sender.send(userJson.generate(myAuthKey, "1"));
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
                                        || isRequested != userJsonDecoder.isRequested) {
                                    try {
                                        new deathPages();
                                        Info.vValueFeed = TwitteController.scrollPane.getVvalue();
                                        new Info(myAuthKey);
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



        getImageFile getProfileFile= new getImageFile();
        File file = getProfileFile.profile(Username);
        Image image = new Image(file.toURI().toString());

        try {
            Icon.setImage(image);
        } catch (NullPointerException ignored) {
        }

        menu.Menu_command(myAuthKey);

        Icon.setOnMouseEntered(mouseEvent -> {
            Icon.getStyleClass().add("focused");
        });
        Icon.setOnMouseExited(mouseEvent -> {
            Icon.getStyleClass().remove("username");
        });

        Icon.setOnMouseClicked(mouseEvent -> {
            authKey.getter();

            try {
                new selectIcon(authKey.username, 1);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Error in open select image");
            }
        });

        if (enable) {
            newTwitte = FXMLLoader.load(getClass().getResource("/layout/header/new_twitte.fxml"));
            TextArea twitteText = (TextArea) newTwitte.lookup("#twitte_text");

            Button send = (Button) newTwitte.lookup("#send");
            picture = (Button) newTwitte.lookup("#pic");

            ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().add(newTwitte);

            picture.setOnAction(event -> {
                if (selectIcon.ImageIm == null) {
                            try {
                                selectIcon si = new selectIcon();
                                si.SelectImage();
                            } catch (IOException e) {
                                e.printStackTrace();
                                logger.error("Error in open select image");
                            }
                            }else{
                    try {
                        new deathPages();
                        new Info(myAuthKey);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            });

            Newtwitte newTwitte = new Newtwitte();
            send.setOnAction(event -> {
                int ID = 0 ;
                if(selectIcon.ImageIm == null) {
                    if (!twitteText.getText().isEmpty()) {
                        try {
                            newTwitte.sendTwitte(twitteText.getText(), myAuthKey);
                        } catch (Exception e) {
                            logger.error("Error in creat a new nwitte");
                        }
                    }

                }else {
                            try {
                                ID = newTwitte.sendTwitte(twitteText.getText(), myAuthKey);

                            } catch (Exception e) {
                                logger.error("Error in creat a new nwitte");
                            }


                        byte[] fileContent = new byte[0];
                        String encodedString = null;
                        try {
                            fileContent = FileUtils.readFileToByteArray(selectIcon.fileIm);
                            encodedString = Base64.getEncoder().encodeToString(fileContent);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

//                        System.out.println(ID);
                        if (ID != 0) {
                        JSONObject outputJson = imageConnection.jsonCreator("twitte", ID + "",
                                encodedString);
                        sender.send(outputJson);
                        }

                        selectIcon.fileIm = null;
                        selectIcon.ImageIm = null;
                    }

            });

            selectIcon.fileIm = null;
            selectIcon.ImageIm = null;

            launch.view.scene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    int ID = 0 ;
                    if(selectIcon.ImageIm == null) {
                        if (!twitteText.getText().isEmpty()) {
                            try {
                                newTwitte.sendTwitte(twitteText.getText(), myAuthKey);
                            } catch (Exception e) {
                                logger.error("Error in creat a new nwitte");
                            }
                        }

                    }else {
                        try {
                            ID = newTwitte.sendTwitte(twitteText.getText(), myAuthKey);

                        } catch (Exception e) {
                            logger.error("Error in creat a new nwitte");
                        }


                        byte[] fileContent = new byte[0];
                        String encodedString = null;
                        try {
                            fileContent = FileUtils.readFileToByteArray(selectIcon.fileIm);
                            encodedString = Base64.getEncoder().encodeToString(fileContent);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

//                        System.out.println(ID);
                        if (ID != 0) {
                            JSONObject outputJson = imageConnection.jsonCreator("twitte", ID + "",
                                    encodedString);
                            sender.send(outputJson);
                        }

                        selectIcon.fileIm = null;
                        selectIcon.ImageIm = null;
                    }

                }
            });
        }

        categories.setOnAction(event -> {
            new deathPages();
            if (enable) {
                try {
                    updateChecker.stop();
                    userTwittes.updateChecker.stop();
                    new Category(myAuthKey);
                } catch (Exception e) {
                    logger.error("Error in open categories from mainPages.Info");
                }
            } else {
                JOptionPane.showMessageDialog(null, "You cant access categories" +
                        " when your account is disable");
            }

        });


        savedTwittes.setOnAction(event -> {
            new deathPages();
            try {
                updateChecker.stop();
                userTwittes.updateChecker.stop();
                new messengerChilds.savedTwittes(myAuthKey);
            } catch (Exception e) {
                logger.error("Error in open saved Twittes from mainPages.Info");
            }
        });


        notifications.setOnAction(event -> {
            new deathPages();
            if (enable) {
                try {
                    updateChecker.stop();
                    userTwittes.updateChecker.stop();
                    new Notifications(myAuthKey, 1);
                } catch (Exception e) {
                    logger.error("Error in open notification from mainPages.Info");
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "You cant access notifications" +
                        " when your account is disable");
            }

        });
        edit.setOnAction(event -> {
            new deathPages();
            try {
                updateChecker.stop();
                userTwittes.updateChecker.stop();
                new editProfile(myAuthKey);
            } catch (Exception e) {
                logger.error("Error in open edit page from mainPages.Info");
            }
        });

        authKey.getter();
        savedMessages.setOnAction(event -> {
            new deathPages();
            try {
                updateChecker.stop();
                userTwittes.updateChecker.stop();
                new chatPage(myAuthKey, launch.authKey.username, launch.authKey.username);
            } catch (Exception e) {
                logger.error("Error in open saved message from mainPages.Info");

            }
        });

        followingCount.setOnAction(event -> {

            if (followings == 0) {
                JOptionPane.showMessageDialog(null, "You haven't any following");
            } else try {
                updateChecker.stop();
                userTwittes.updateChecker.stop();
                new Lists(Username, myAuthKey, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        followerCount.setOnAction(event -> {
            if (followers == 0) {
                JOptionPane.showMessageDialog(null, "You haven't any followers :(");
            } else try {
                updateChecker.stop();
                userTwittes.updateChecker.stop();
                new Lists(Username, myAuthKey, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        blockCount.setOnAction(event -> {
            if (blocks == 0) {
                JOptionPane.showMessageDialog(null, "You haven't block anybody");
            } else try {
                updateChecker.stop();
                userTwittes.updateChecker.stop();
                new Lists(Username, myAuthKey, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        muteCount.setOnAction(event -> {
            if (mutes == 0) {
                JOptionPane.showMessageDialog(null, "You haven't mute anybody");
            } else try {
                updateChecker.stop();
                userTwittes.updateChecker.stop();
                new Lists(Username, myAuthKey, 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        new userTwittes(myAuthKey, myAuthKey,true);

    }
}
