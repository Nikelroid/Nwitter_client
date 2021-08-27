package twitteHyperlink;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import loadTwittes.jsonDecoder;
import loadTwittes.loadConnection;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import profiles.getImageFile;
import twitteControl.deathPages;
import twitteControl.twitteModifier;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class showHyperlink {
    public static Button likeList,likeAction,commentList,commentAction,retwitteList,
            retwitteAction, Share,Save,Report,Link;
    AnchorPane Text;
    Label Sender,Date;


    public static ImageView twitteImages;
    public static Timeline updateChecker;
    String username;
    String text;
    String date;
    String serial;
    int likes;
    int comments;
    int retwittes;
    boolean isSaved;
    boolean isLiked;
    boolean isRetwitted;


    private static final Logger logger = LogManager.getLogger(showHyperlink.class);


    public showHyperlink(String serial) throws Exception {

        String AuthKey = launch.authKey.getter().substring(2);

        this.serial = serial;
        logger.info("System: user went to messengerChilds.massMessenger");

        Parent ListRoot = FXMLLoader.load(getClass().getResource("/layout/cards/twitte.fxml"));
        Scene ListScene = new Scene(ListRoot);
        Stage ListStage = new Stage();
        ListStage.setScene(ListScene);


        twitteImages = (ImageView) ListScene.lookup("#image");
        Text = (AnchorPane) ListScene.lookup("#twitte_text");
        Sender = (Label) ListScene.lookup("#sender");
        Date = (Label) ListScene.lookup("#date");
        likeList = (Button) ListScene.lookup("#like_l");
        likeAction= (Button) ListScene.lookup("#like_a");
        commentList= (Button) ListScene.lookup("#comment_l");
        commentAction=(Button) ListScene.lookup("#comment_a");
        retwitteList=(Button) ListScene.lookup("#ret_l");
        retwitteAction=(Button) ListScene.lookup("#ret_a");
        Share=(Button) ListScene.lookup("#share");
        Save=(Button) ListScene.lookup("#save");
        Report=(Button) ListScene.lookup("#report");
        Link=(Button) ListScene.lookup("#link");





        loadConnection loadConnection = new loadConnection();
        if (loadConnection.get("hyperlink", AuthKey,serial,false)==0){
            JOptionPane.showMessageDialog(null,"Sorry, " +
                    "we can't show this twitte to you.");
            ListScene.getWindow().hide();
            return;
        }

        username = jsonDecoder.usernames.get(0);
        text = jsonDecoder.text.get(0);
        date = jsonDecoder.date.get(0);
        serial = jsonDecoder.serials.get(0);
        likes = jsonDecoder.likes.get(0);
        comments = jsonDecoder.comments.get(0);
        retwittes = jsonDecoder.retwittes.get(0);
        isSaved = jsonDecoder.isSaved.get(0);
        isLiked = jsonDecoder.isLiked.get(0);
        isRetwitted = jsonDecoder.isRetwitted.get(0);


        final String finalSerial = serial;
        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                loadConnection.get("hyperlink", AuthKey,finalSerial,false);
                                if (!finalSerial.equals(jsonDecoder.serials.get(0))
                                        || likes!=jsonDecoder.likes.get(0)
                                        || comments!=jsonDecoder.comments.get(0)
                                        || retwittes!=jsonDecoder.retwittes.get(0)
                                        || isSaved!=jsonDecoder.isSaved.get(0)) {
                                    try {
                                        new deathPages();
                                        ListScene.getWindow().hide();
                                        new showHyperlink(jsonDecoder.serials.get(0));
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();



        hyperlinkButtons hyperlinkButtons = new hyperlinkButtons(finalSerial,AuthKey,username,text,
                isLiked,isRetwitted,isSaved);
        twitteHyperlink.hyperlinkButtons.setter(Date, Sender, Text, serial,username,text,date,likes,comments,retwittes);
        ListStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));
        ListStage.show();
        ListScene.getWindow().setOnHidden(windowEvent -> {
            updateChecker.stop();
        });
    }
}
