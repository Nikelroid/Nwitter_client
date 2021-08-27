package groups;

import chat.chatPage;
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
import jsonContoller.jsonMessage;
import launch.authKey;
import launch.view;
import login.loginConnection;
import mainPages.Messenger;
import objects.objMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitteControl.Menu;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class gpMain {
    private static double vValueGp;
    groupsConnection connection = new groupsConnection();
    int target;
    groupsConnection groupsConnection = new groupsConnection();
    private static final Logger logger = LogManager.getLogger(gpMain.class);
    ArrayList<String> retOrder = new ArrayList<String>();
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    public static Timeline updateChecker;
    AnchorPane creatCat;
    JSONObject resultJson;
    String username = null;
    public static ScrollPane scrollPane;
    public static VBox twitteList;

    public gpMain(String AuthKey) throws Exception {
        logger.info("System: user went to Groups");

        authKey.getter();
        username = authKey.username;

        userFinder userFinder = new userFinder();
        target = userFinder.UserFinder(AuthKey);


        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/general_page.fxml"));

        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Groups");
        launch.view.stage.show();

        userFinder uf = new userFinder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) launch.view.root.lookup("#exit");
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (response == 0) {
                new deathPages();
                System.exit(1);
            } else if (response == 1) {
                loginConnection loginConnection = new loginConnection();
                loginConnection.connection(AuthKey, "-");
                System.exit(1);
            }
        });

        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText("Groups");


        creatCat = FXMLLoader.load(getClass().getResource("/layout/cards/category_main_card.fxml"));
        TextField catName = (TextField) creatCat.lookup("#name");
        catName.setPromptText("Insert the Group name");
        Button createCat = (Button) creatCat.lookup("#create");
        scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().removeAll();
        twitteList.getChildren().add(creatCat);


        createCat.setOnAction(event -> {
            if (!catName.getText().isEmpty()) {
                groupsConnection.getter("addGps", AuthKey, catName.getText());
            }
        });
        launch.view.scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (!catName.getText().isEmpty()) {
                    groupsConnection.getter("addGps", AuthKey, catName.getText());
                }
            }
        });


        gpPrint.Members = new ArrayList<>();
        gpPrint.Messages = new ArrayList<>();
        gpPrint.Delete = new ArrayList<>();
        gpPrint.Link = new ArrayList<>();

        String gpName;

        JSONArray jsonArray1 = null;
        JSONArray jsonArray2 = null;
        ArrayList<String> groupsName = new ArrayList<>();
        ArrayList<String> groupsId = new ArrayList<>();

        resultJson = connection.getter("get", AuthKey);

        try {
            resultJson.get("result");
        }catch (JSONException e){
            JOptionPane.showMessageDialog(null,"This is online future");
            view.scene.getWindow().hide();
            new Messenger(AuthKey);
            return;
        }

        try{
        if (resultJson.get("result").equals("0")) {
            JOptionPane.showMessageDialog(null, "Error in connection");
        } else {
            jsonArray1 = resultJson.getJSONArray("names");
            jsonArray2 = resultJson.getJSONArray("id");
            for (int i = 0; i < jsonArray1.length(); i++) {
                groupsName.add(jsonArray1.get(i).toString());
                groupsId.add(jsonArray2.get(i).toString());
            }

            final JSONArray finalJsonArray1 = jsonArray1;
            final JSONArray finalJsonArray2 = jsonArray2;
            updateChecker = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    resultJson = connection.getter("get", AuthKey);

                                    if (resultJson.get("result").equals("0")) {
                                        JOptionPane.showMessageDialog(null, "Error in connection");
                                    } else if (!finalJsonArray1.toString().equals(resultJson.getJSONArray("names").toString())
                                            || !finalJsonArray2.toString().equals(resultJson.getJSONArray("id").toString())) {
                                        new deathPages();
                                        gpMain.vValueGp = gpMain.scrollPane.getVvalue();
                                        try {
                                            new gpMain(AuthKey);
                                        } catch (Exception exception) {
                                            logger.error("error in refresh category page");
                                            exception.printStackTrace();
                                        }
                                    }
                                }
                            }));
            updateChecker.setCycleCount(Timeline.INDEFINITE);
            updateChecker.play();




            for (int i = 0; i < groupsName.size(); i++) {
                new gpPrint(groupsName.get(i), groupsId.get(i));
            }


            for (int i = 0; i < gpPrint.Delete.size(); i++) {
                final int finalI = i;
                gpPrint.Delete.get(i).setText("Leave");
                gpPrint.Delete.get(i).setOnAction(event -> {
                    int input = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want leave this Group?");
                    if (input == 0) {
                        groupsConnection.getter("deleteGps", AuthKey, groupsName.get(finalI));
                    }
                });
                gpPrint.Messages.get(i).setOnAction(event -> {
                    new deathPages();
                    logger.info("User went to chat of " + finalI + 1 + "th group");
                    try {
                        new chatPage(AuthKey,username, "*"+groupsName.get(finalI));

                    } catch (IOException e) {
                        logger.error("Error in open groups members");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                gpPrint.Members.get(i).setOnAction(event -> {
                    new deathPages();
                    logger.info("User went to members of " + finalI + 1 + "th group");
                    try {
                        new gpMembers(AuthKey,groupsName.get(finalI),1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
                gpPrint.Link.get(i).setOnAction(event -> {
                    StringSelection stringSelection = new StringSelection("$" + groupsId.get(finalI));
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    JOptionPane.showMessageDialog(null, "Link: $" + groupsId.get(finalI) +
                            " Added to clipboard");
                });
            }
            var menu = new Menu();
            menu.Menu_command(AuthKey);
        }

        Platform.runLater(()->{
            gpMain.scrollPane.setVvalue(gpMain.vValueGp);
        });
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Check connection");
            new Messenger(AuthKey);
        }
    }
}
