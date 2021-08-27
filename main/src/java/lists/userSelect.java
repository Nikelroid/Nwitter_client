package lists;

import category.categoryMembers;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import login.loginConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import profiles.getImageFile;
import submit.listJson;
import twitteControl.deathPages;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class userSelect {
    AnchorPane card;
    ImageView Icon;
    Label Username ,Name;
    int target=0;
    Parent selectRoot;
    Scene selectScene;
    Stage selectStage;
    public static double vValueUserSelect;

    listJson listJson = new listJson();
    sender sender = new sender();
    public static Timeline updateChecker;
    public static ScrollPane scrollPane;

    public static ArrayList<CheckBox> select ;
    public static VBox twitteList;
    private static final Logger logger = LogManager.getLogger(categoryMembers.class);
    private void definer() throws IOException {
        card = FXMLLoader.load(getClass().getResource("/layout/cards/user_select.fxml"));
        Username = (Label) card.lookup("#username");
        Name = (Label) card.lookup("#name");
        Icon = (ImageView) card.lookup("#icon");

    }

    private void setter (String username,String name,String pic){

        try {
            if (!pic.isEmpty()) {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.profile(username);
                Image image = new Image(file.toURI().toString());
                Icon.setImage(image);
            }
        } catch (NullPointerException ignored) {
        }

        select.add((CheckBox) card.lookup("#select"));
        Username.setText(username);
        Name.setText(name);
    }
    private void adder () {

        twitteList.getChildren().add(card);
    }



    public userSelect(String AuthKey,String me) throws IOException {
         selectRoot = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
         selectScene = new Scene(selectRoot);
         selectStage = new Stage();
        selectStage.setScene(selectScene);
        selectStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));

        selectStage.show();

        scrollPane = (ScrollPane) selectScene.lookup("#scobar");
        twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().removeAll();

        ImageView Exit = (ImageView) selectScene.lookup("#exit") ;
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

        Button Back = (Button) selectScene.lookup("#back");
        Back.setText("Send");

        Label Header = (Label) selectScene.lookup("#header");
        Header.setText("Select followings");


            Back.setOnAction(event -> {
                new deathPages();
                try {
                    Back.getScene().getWindow().hide();
                } catch (Exception e) {
                    logger.error("Error in back to category page");
                }
            });
            ArrayList<String> username = new ArrayList<String>();
            ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> pic = new ArrayList<String>();

        JSONObject likeRequest = listJson.get("followings",me);
        JSONObject outputJson = sender.send(likeRequest);
        new jsonListDecoder(outputJson);

        username = jsonListDecoder.username;
        name = jsonListDecoder.name;
        pic =  jsonListDecoder.pic;

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
                                        Back.getScene().getWindow().hide();
                                        new deathPages();
                                        userSelect.vValueUserSelect = listPrinter.scrollPane.getVvalue();
                                        new userSelect(AuthKey,me);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();



            for (int i = 0; i < username.size(); i++) {

                     definer();
                     setter(username.get(i),name.get(i),pic.get(i));
                     adder();

            }
        selectStage.close();
        selectStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));

        selectStage.showAndWait();
        Platform.runLater(()->{
            scrollPane.setVvalue(userSelect.vValueUserSelect);
        });
    }
}
