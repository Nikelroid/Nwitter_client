package category;

import comments.commentsPage;
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
import jsonContoller.jsonUsers;
import launch.authKey;
import loadTwittes.jsonDecoderComments;
import login.loginConnection;
import mainPages.Feed;
import mainPages.Messenger;
import messengerChilds.massMessenger;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import submit.submitMessage;
import twitteControl.Menu;
import twitteControl.commentsController;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category {
    public static double vValueCat;
    public static ScrollPane scrollPane;
    public static VBox twitteList;
    categoryConnection connection = new categoryConnection();

    JSONObject resultJson = new JSONObject();
    int target;
    public static Timeline updateChecker;
    public static String textMessage = null;
    private static final Logger logger = LogManager.getLogger(Category.class);

    AnchorPane creatCat;

    int[] mapper = new int[10000];
    int[] chater = new int[10000];

    public Category(String AuthKey, String textMessage) throws Exception {
        Category.textMessage = textMessage;
        new Category(AuthKey);
    }

    public Category(String AuthKey) throws Exception {
        new catrgoryCfg();
        logger.info("System: user went to Category");

        userFinder userFinder = new userFinder();
        target = userFinder.UserFinder(AuthKey);

        launch.view.root = FXMLLoader.load(getClass().getResource(catrgoryCfg.fxml_general));

        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle(catrgoryCfg.header);
        launch.view.stage.show();

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
        JSONArray jsonArray1 = null;
        JSONArray jsonArray2 = null;
        ArrayList<String> categoriesName = new ArrayList<>();
        ArrayList<Integer> categoriesCount = new ArrayList<>();

        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText(catrgoryCfg.header);

        resultJson = connection.getter("get", AuthKey);
        try {
            resultJson.get("result");
        }catch (JSONException e){
            JOptionPane.showMessageDialog(null,"This is online future");
        new Messenger(AuthKey);
        return;
        }
        if (resultJson.get("result").equals("0")) {
            JOptionPane.showMessageDialog(null, "Error in connection");
        } else {
            jsonArray1 = resultJson.getJSONArray("names");
            jsonArray2 = resultJson.getJSONArray("counts");
            for (int i = 0; i < jsonArray1.length(); i++) {
                categoriesName.add(jsonArray1.get(i).toString());
                categoriesCount.add(Integer.parseInt(jsonArray2.get(i).toString()));
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
                                    || !finalJsonArray2.toString().equals(resultJson.getJSONArray("counts").toString())) {
                                        new deathPages();
                                        Category.vValueCat = Category.scrollPane.getVvalue();
                                        try {
                                            new Category(AuthKey, textMessage);
                                        } catch (Exception exception) {
                                            logger.error("error in refresh category page");
                                            exception.printStackTrace();
                                        }
                                    }
                                }
                            }));
            updateChecker.setCycleCount(Timeline.INDEFINITE);
            updateChecker.play();

            scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().removeAll();

            if (Category.textMessage == null) {
                creatCat = FXMLLoader.load(getClass().getResource(catrgoryCfg.fxml_main_card));
                TextField catName = (TextField) creatCat.lookup("#name");
                Button createCat = (Button) creatCat.lookup("#create");
                ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
                VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
                twitteList.getChildren().add(creatCat);

                createCat.setOnAction(event -> {
                    if (!catName.getText().isEmpty()) {
                        resultJson = connection.getter("addCat",AuthKey, catName.getText());
                        if (resultJson.get("result").equals("0")) {
                            logger.error("Error in adding cat by button from category");
                            JOptionPane.showMessageDialog(null,"error in connection");
                        }
                        logger.info("System: category.Category created!");
                    }
                });
                launch.view.scene.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (!catName.getText().isEmpty()) {
                            resultJson = connection.getter("addCat",AuthKey, catName.getText());
                            if (resultJson.get("result").equals("0")) {
                                logger.error("Error in adding cat by enter from category");
                                JOptionPane.showMessageDialog(null,"error in connection");
                            }
                        }
                    }
                });
            }

            categoryPrint.Members = new ArrayList<>();
            categoryPrint.Messages = new ArrayList<>();
            categoryPrint.Delete = new ArrayList<>();
            new categoryPrint(AuthKey, categoriesName, categoriesCount);

            for (int i = 0; i < categoriesName.size(); i++) {
                if (Category.textMessage != null) {
                    categoryPrint.Delete.get(i).setVisible(false);
                    categoryPrint.Members.get(i).setVisible(false);
                    categoryPrint.Messages.get(i).setText("Select");
                }
                final int finalI = i;
                categoryPrint.Delete.get(i).setOnAction(event -> {
                    int input = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want delete this category?");
                    if (input == 0) {
                        resultJson = connection.getter("deleteCat",AuthKey,categoriesName.get(finalI));

                        if (resultJson.get("result").equals("0")) {
                            logger.error("Error in adding cat by enter from category");
                            JOptionPane.showMessageDialog(null,"error in connection");
                        }
                    }
                });
                authKey.getter();
                String username = authKey.username;
                categoryPrint.Messages.get(i).setOnAction(event -> {
                    if (Category.textMessage == null) {
                        try {
                            new massMessenger(username, categoriesName.get(finalI));
                        } catch (Exception e) {
                            logger.error("Error in loading mass messenger from category");
                        }
                    } else {

                        List<String> usernames = new ArrayList<>();
                        JSONArray myJsonArray1 ;
                        categoryConnection connection = new categoryConnection();
                        JSONObject resultJson = connection.getter("getMembers",AuthKey,
                                categoriesName.get(finalI));
                        if (resultJson.get("result").equals("0")) {
                            JOptionPane.showMessageDialog(null, "Error in connection");
                        } else {
                            myJsonArray1 = resultJson.getJSONArray("usernames");
                            for (int m = 0; m < myJsonArray1.length(); m++) {
                                usernames.add(myJsonArray1.get(m).toString());
                            }
                        }
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(catrgoryCfg.date_format);
                        LocalDateTime now = LocalDateTime.now();

                        for (int j = 0; j < usernames.size(); j++) {
                            var sb = new submitMessage();
                            sb.SubMess(textMessage, username, usernames.get(j),dtf.format(now));
                        }
                        JOptionPane.showMessageDialog(null, "Message sent to members of " +
                                categoriesName.get(finalI) + " category");
                        try {
                            textMessage=null;
                            new deathPages();
                            new Messenger(AuthKey);
                        } catch (Exception e) {
                            logger.error("error in reloading mass messenger after sending message");
                        }

                    }
                });
                categoryPrint.Members.get(i).setOnAction(event -> {
                    new deathPages();
                    logger.info("User went to members of " + finalI + 1 + "th category");
                    try {
                        new categoryMembers(AuthKey, categoriesName.get(finalI), 1);
                    } catch (IOException e) {
                        logger.error("Error in open category members");
                    }
                });
            }
            var menu = new Menu();
            menu.Menu_command(AuthKey);
        }
        Platform.runLater(()->{
            Category.scrollPane.setVvalue(Category.vValueCat);
        });
    }
}
