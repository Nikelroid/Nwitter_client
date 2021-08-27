package category;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jsonContoller.jsonUsers;
import login.loginConnection;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import profiles.getImageFile;
import profiles.profileSaver;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class categoryMembers {
    public static double vValueCatMembers;
    public static Timeline updateChecker1;
    public static Timeline updateChecker2;
    AnchorPane card;
    ImageView Icon;
    public static ScrollPane scrollPane;
    public static VBox twitteList;
    categoryConnection connection = new categoryConnection();
    FXMLLoader fXMLLoader = new FXMLLoader();
    Label Username ,Name;
    ArrayList<Button> actionButton;
    jsonUsers get_j = new jsonUsers();
    int[] mapper = new int[10000];
    List<objUsers> users = get_j.get();
    userFinder us = new userFinder();
    int target=0;
    JSONObject resultJson =new JSONObject();
    private static final Logger logger = LogManager.getLogger(categoryMembers.class);
    private void definer() throws IOException {
        card = FXMLLoader.load(getClass().getResource("/layout/cards/category_members_card.fxml"));
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
        actionButton.add((Button) card.lookup("#delete"));
        Username.setText(username);
        Name.setText(name);
    }
    private void adder () {
        categoryMembers.twitteList.getChildren().add(card);
    }

    public categoryMembers(String AuthKey,String catName,int type) throws IOException {
        target = us.UserFinder(AuthKey);

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/minor_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.show();
        Button Back = (Button) launch.view.scene.lookup("#back");
        Back.setText("Back to categories");


        ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
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

        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText(catName + " Category");
        actionButton = new ArrayList<>();

        definer();






        if (type == 1) {

            JSONArray jsonArray1 = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();
            JSONArray jsonArray3 = new JSONArray();

            ArrayList<String> usernames = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> pics = new ArrayList<>();

            resultJson = connection.getter("getMembers",AuthKey,catName);
            if (resultJson.get("result").equals("0")) {
                JOptionPane.showMessageDialog(null, "Error in connection");
            } else {
                jsonArray1 = resultJson.getJSONArray("usernames");
                jsonArray2 = resultJson.getJSONArray("names");
                jsonArray3 = resultJson.getJSONArray("pic");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    usernames.add(jsonArray1.get(i).toString());
                    names.add(jsonArray2.get(i).toString());
                    pics.add(jsonArray3.get(i).toString());
                }
                for (int i = 0; i < pics.size(); i++) {
                    if (!pics.get(i).toString().isEmpty()){
                       new profileSaver(usernames.get(i),pics.get(i));
                    }
                }
            }

            final JSONArray finalJsonArray1 = jsonArray1;
            final JSONArray finalJsonArray2 = jsonArray2;
            final JSONArray finalJsonArray3 = jsonArray3;
            updateChecker1 = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    resultJson = connection.getter("getMembers",AuthKey,catName);
                                    if (resultJson.get("result").equals("0")) {
                                        JOptionPane.showMessageDialog(null, "Error in connection");
                                    } else if (!finalJsonArray1.toString().equals(resultJson.getJSONArray("usernames").toString())
                                            || !finalJsonArray2.toString().equals(resultJson.getJSONArray("names").toString())
                                            || !finalJsonArray3.toString().equals(resultJson.getJSONArray("pic").toString())) {
                                        new deathPages();
                                        categoryMembers.vValueCatMembers = scrollPane.getVvalue();
                                        try {
                                            new categoryMembers(AuthKey, catName,type);
                                        } catch (Exception exception) {
                                            logger.error("error in refresh category page");
                                            exception.printStackTrace();
                                        }
                                    }
                                }
                            }));
            updateChecker1.setCycleCount(Timeline.INDEFINITE);
            updateChecker1.play();

            scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().removeAll();

            actionButton.add((Button) card.lookup("#delete"));
            actionButton.get(0).setText("Add");
            actionButton.get(0).getStyleClass().remove("toggle_wrong");
            actionButton.get(0).getStyleClass().add("toggle_accept");
            Icon.setImage(new Image(getClass().getResourceAsStream("/img/plus.png")));
            Username.setText("For add member to this category press the button");
            Name.setText("");
            adder();

            Back.setOnAction(event -> {
                new deathPages();
                try {
                    new Category(AuthKey);
                } catch (Exception e) {
                    logger.error("Error in back to category page");
                }
            });
            actionButton.get(0).setOnAction(event -> {
                new deathPages();
                try {
                    new categoryMembers(AuthKey, catName,2);
                } catch (IOException e) {
                    logger.error("Error in load add member type of category");
                }
            });


            for (int i = 0; i < usernames.size(); i++) {
                    definer();
                    setter(usernames.get(i),names.get(i),pics.get(i));
                    adder();
            }
            for (int i = 1; i < actionButton.size(); i++) {
                final int finalI = i;
                actionButton.get(i).setOnAction(event -> {
                    try{
                        resultJson = connection.getter("deleteMembers",AuthKey,
                               catName, usernames.get(finalI-1));
                        if (resultJson.get("result").equals("0")) {
                            logger.error("Error in delete member from category");
                            JOptionPane.showMessageDialog(null,"error in connection");
                        }
                    } catch (Exception e) {
                        logger.error("Error in delete member from category");
                        JOptionPane.showMessageDialog(null,"error in connection");

                    }
                });
            }
        }else{


            JSONArray jsonArrayFollowings1 = new JSONArray();
            JSONArray jsonArrayFollowings2 = new JSONArray();
            JSONArray jsonArrayFollowings3 = new JSONArray();

            ArrayList<String> usernamesFollowings = new ArrayList<>();
            ArrayList<String> namesFollowings = new ArrayList<>();
            ArrayList<String> picsFollowings = new ArrayList<>();

            resultJson = connection.getter("getFollowingsCategory",AuthKey,catName);
            if (resultJson.get("result").equals("0")) {
                JOptionPane.showMessageDialog(null, "Error in connection");
            } else {
                jsonArrayFollowings1 = resultJson.getJSONArray("usernames");
                jsonArrayFollowings2 = resultJson.getJSONArray("names");
                jsonArrayFollowings3 = resultJson.getJSONArray("pic");
                for (int i = 0; i < jsonArrayFollowings1.length(); i++) {
                    usernamesFollowings.add(jsonArrayFollowings1.get(i).toString());
                    namesFollowings.add(jsonArrayFollowings2.get(i).toString());
                    picsFollowings.add(jsonArrayFollowings3.get(i).toString());
                }
                for (int i = 0; i < picsFollowings.size(); i++) {
                    if (!picsFollowings.get(i).toString().isEmpty()){
                        new profileSaver(usernamesFollowings.get(i),picsFollowings.get(i));
                    }
                }
            }

            final JSONArray finalJsonArrayFollowings1 = jsonArrayFollowings1;
            final JSONArray finalJsonArrayFollowings2 = jsonArrayFollowings2;
            final JSONArray finalJsonArrayFollowings3 = jsonArrayFollowings3;

            updateChecker2 = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    resultJson = connection.getter("getFollowingsCategory",AuthKey,catName);

                                    if (resultJson.get("result").equals("0")) {
                                        JOptionPane.showMessageDialog(null, "Error in connection");
                                    } else if (!finalJsonArrayFollowings1.toString().equals(resultJson.getJSONArray("usernames").toString())
                                            || !finalJsonArrayFollowings2.toString().equals(resultJson.getJSONArray("names").toString())
                                    || !finalJsonArrayFollowings3.toString().equals(resultJson.getJSONArray("pic").toString())) {
                                        new deathPages();
                                        categoryMembers.vValueCatMembers = scrollPane.getVvalue();
                                        try {
                                            new categoryMembers(AuthKey, catName,type);
                                        } catch (Exception exception) {
                                            logger.error("error in refresh category page");
                                            exception.printStackTrace();
                                        }
                                    }
                                }
                            }));
            updateChecker2.setCycleCount(Timeline.INDEFINITE);
            updateChecker2.play();

            scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().removeAll();

            Back.setOnAction(event -> {
                new deathPages();
                try {
                    new categoryMembers(AuthKey, catName,1);
                } catch (Exception e) {
                    logger.error("Error in back to category members page");
                }
            });

            for (int i = 0; i <usernamesFollowings.size(); i++) {

                definer();
                setter(usernamesFollowings.get(i),namesFollowings.get(i),picsFollowings.get(i));
                adder();
            }
            for (int i = 0; i < actionButton.size(); i++) {
                actionButton.get(i).setText("Add");
                actionButton.get(i).getStyleClass().remove("toggle_wrong");
                actionButton.get(i).getStyleClass().add("toggle_accept");
            }
            for (int i = 0; i < actionButton.size(); i++) {
                final int finalI = i;
                actionButton.get(i).setOnAction(event -> {
                    try{
                        resultJson = connection.getter("addMembers",AuthKey,
                                catName, usernamesFollowings.get(finalI));
                        if (resultJson.get("result").equals("0")) {
                            logger.error("Error in delete member from category");
                            JOptionPane.showMessageDialog(null,"error in connection");
                        }
                    } catch (Exception e) {
                        logger.error("Error in delete member from category");
                        JOptionPane.showMessageDialog(null,"error in connection");

                    }
                });
            }
        }
        Platform.runLater(()->{
            scrollPane.setVvalue(vValueCatMembers);
        });
    }
}
