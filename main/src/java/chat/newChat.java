package chat;

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
import launch.authKey;
import lists.jsonListDecoder;
import login.loginConnection;
import mainPages.Messenger;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import profiles.getImageFile;
import submit.listJson;
import twitteControl.deathPages;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class newChat {
    private static double vValueUserSelect;
    submit.listJson listJson = new listJson();
    sender sender = new sender();
    String me;
    public static ScrollPane scrollPane ;
    public static VBox twitteList ;
    public static Timeline updateChecker;
    int target=0,counter = 0;
    int[] mapper = new int[10000];
    private static final Logger logger = LogManager.getLogger(newChat.class);
    public newChat(String AuthKey) throws Exception {
        authKey.getter();
        me = authKey.username;
        logger.info("System: user went to chat.newChat");

        Parent ListRoot = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        Scene ListScene = new Scene(ListRoot);
        Stage ListStage = new Stage();
        ListStage.setScene(ListScene);
        ListStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));

        ListStage.show();
        Button Back = (Button) ListScene.lookup("#back");
        Back.setText("Cancel");
        Label Header = (Label) ListScene.lookup("#header");
        Header.setText("New chat with:");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) ListScene.lookup("#exit") ;
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

        ArrayList<Button> share  = new ArrayList<>();

        ArrayList<String> username = new ArrayList<String>();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> pic = new ArrayList<String>();

        JSONObject likeRequest = listJson.get("followings",me);
        JSONObject outputJson = sender.send(likeRequest);
        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });
        try {
            outputJson.get("result");
        }catch (JSONException e){
            JOptionPane.showMessageDialog(null,"This is online future");
            Back.getScene().getWindow().hide();
            new Messenger(AuthKey);
            return;
        }
        new jsonListDecoder(outputJson);

        username = jsonListDecoder.username;
        name = jsonListDecoder.name;
        pic = jsonListDecoder.pic;

        final ArrayList<String> finalUsername = username;
        final ArrayList<String> finalName = name;
        final ArrayList<String> finalPic = pic;
        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                String var;


                                JSONObject likeRequest = listJson.get("followings",me);
                                JSONObject outputJson = sender.send(likeRequest);
                                new jsonListDecoder(outputJson);

                                if (!finalUsername.equals(jsonListDecoder.username)
                                        || !finalName.equals(jsonListDecoder.name)
                                        || !finalPic.equals(jsonListDecoder.pic)) {
                                    try {
                                        new deathPages();
                                        Back.getScene().getWindow().hide();
                                        newChat.vValueUserSelect = newChat.scrollPane.getVvalue();
                                        new newChat(AuthKey);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();

        scrollPane = (ScrollPane) ListScene.lookup("#scobar");
        twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().removeAll();
        for (int j = 0; j < username.size(); j++){

                AnchorPane card = FXMLLoader.load(getClass().getResource("/layout/cards/category_members_card.fxml"));
                Label Username = (Label) card.lookup("#username");
                Label Name = (Label) card.lookup("#name");
                ImageView Icon = (ImageView) card.lookup("#icon");
                Username.setText(username.get(j));
                Name.setText(name.get(j));

            try {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.profile(username.get(j));
                Image image = new Image(file.toURI().toString());
                FileUtils.readFileToByteArray(file);
                Icon.setImage(image);
            } catch (NullPointerException| FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }

                share.add((Button) card.lookup("#delete"));
                twitteList.getChildren().add(card);

                counter++;

            }



        for (int j = 0; j < share.size(); j++) {
            share.get(j).setText("Start");
            share.get(j).getStyleClass().remove("toggle_wrong");
            share.get(j).getStyleClass().add("toggle_accept");
            final int finalJ = j;
            share.get(j).setOnAction(event -> {
                authKey authKey = new authKey();
                launch.authKey.getter();
                try {
                    new chatPage(AuthKey,me,finalUsername.get(finalJ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("System: New chat created");
                Back.getScene().getWindow().hide();
            });
        }
        Platform.runLater(()->{
            scrollPane.setVvalue(newChat.vValueUserSelect);
        });

    }
}
