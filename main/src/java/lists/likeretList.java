package lists;

import connection.sender;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import login.loginConnection;
import mainPages.Info;
import objects.objUsers;
import operation.jsonCreatorOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import profiles.getImageFile;
import submit.listJson;
import submit.submitShare;
import twitteControl.TwitteController;
import twitteControl.deathPages;
import userControl.userProfile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class likeretList {


    connection.sender sender = new sender();
    jsonCreatorOperator jsonCreator = new jsonCreatorOperator();
    submit.listJson listJson = new listJson();

    jsonTwittes Get = new jsonTwittes();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> pics = new ArrayList<String>();
    ArrayList<String> serials = new ArrayList<String>();
    public static Timeline updateChecker;

    ScrollPane scrollPane;
    VBox twitteList;
    public static double vValueFeed;

    private static final Logger logger = LogManager.getLogger(submitShare.class);

    public likeretList() throws IOException {}
    public likeretList(String AuthKey,String twitteSerial,int type) throws IOException {

        usernames = jsonListDecoder.username;
        names = jsonListDecoder.name;
        pics = jsonListDecoder.pic;


        Parent ListRoot = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        Scene ListScene = new Scene(ListRoot);
        Stage ListStage = new Stage();
        ListStage.setScene(ListScene);
        ListStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));

        ListStage.show();


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) ListScene.lookup("#exit") ;
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (response==0){
          new deathPages();
                ListScene.getWindow().hide();
                try {
                    likeretList.vValueFeed = scrollPane.getVvalue();
                    new likeretList(AuthKey,twitteSerial,type);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }else if (response==1) {
                loginConnection loginConnection = new loginConnection();
                loginConnection.connection(AuthKey,"-");
                System.exit(1);
            }
        });

        Button Back = (Button) ListScene.lookup("#back");
        Back.setText("Back");
        Label Header = (Label) ListScene.lookup("#header");
        if (type==1)
        Header.setText("Likes");
        else
            Header.setText("Retwittes");


        ArrayList<Button> share = new ArrayList<>();


        for (int j = 0; j < usernames.size(); j++) {

            AnchorPane card = FXMLLoader.load(getClass().getResource("/layout/cards/category_members_card.fxml"));
            Label Username = (Label) card.lookup("#username");
            Label Name = (Label) card.lookup("#name");
            ImageView Icon = (ImageView) card.lookup("#icon");
            try {
                if (!pics.get(j).isEmpty()) {
                    getImageFile getProfileFile = new getImageFile();
                    File file = getProfileFile.profile(usernames.get(j));
                    Image image = new Image(file.toURI().toString());
                    Icon.setImage(image);
                }
            } catch (NullPointerException ignored) {
            }
            Username.setText(usernames.get(j));
            Name.setText(names.get(j));

            scrollPane = (ScrollPane) ListScene.lookup("#scobar");
            twitteList = (VBox) scrollPane.lookup("#twittelist");
            share.add((Button) card.lookup("#delete"));
            twitteList.getChildren().add(card);
        }

        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                String var;
                                if (type==1)var="likes";
                                else var="retwittes";

                                JSONObject likeRequest = listJson.get(var,twitteSerial);
                                JSONObject outputJson = sender.send(likeRequest);
                                new jsonListDecoder(outputJson);
                                if (!usernames.equals(jsonListDecoder.username)
                                        || !names.equals(jsonListDecoder.name)
                                        || !pics.equals(jsonListDecoder.pic) ) {
                                    try {
                                        new deathPages();
                                        likeretList.vValueFeed = scrollPane.getVvalue();
                                        Back.getScene().getWindow().hide();
                                        new likeretList(AuthKey,twitteSerial,type);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        likeretList.updateChecker.setCycleCount(Timeline.INDEFINITE);
        likeretList.updateChecker.play();


        for (int j = 0; j < share.size(); j++) {
            share.get(j).setText("Profile");
            share.get(j).getStyleClass().remove("toggle_wrong");
            share.get(j).getStyleClass().add("login");
        }
        Back.setOnAction(event -> {
            likeretList.updateChecker.stop();
            Back.getScene().getWindow().hide();
        });

        for (int j = 0; j < share.size(); j++) {
            final int finalJ = j;
            share.get(j).setOnAction(event -> {
                likeretList.updateChecker.stop();
                new deathPages();
                try {
                    new userProfile(AuthKey,usernames.get(finalJ));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error in loading user profile");
                }
                Back.getScene().getWindow().hide();
            });
            Platform.runLater(()->{
                TwitteController.scrollPane.setVvalue(likeretList.vValueFeed);
            });
        }
    }
}
