package messengerChilds;

import category.Category;
import category.categoryConnection;
import com.google.gson.JsonObject;
import connection.sender;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import launch.authKey;
import lists.jsonListDecoder;
import lists.userSelect;
import mainPages.Messenger;
import objects.objMessage;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import submit.listJson;
import submit.submitMessage;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class massMessenger {

    listJson listJson = new listJson();
    sender sender = new sender();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    submitMessage sb = new submitMessage();
    int target,sounter=1;
    int counter=1;
    int[] mapper = new int [10000];
    int[] chater = new int [10000];
    public static String catName="";
    public static Button Back;
    private static final Logger logger = LogManager.getLogger(massMessenger.class);
    public massMessenger(String username,String catName) throws Exception {
        massMessenger.catName=catName;
        new massMessenger(username);
    }
    authKey authKey= new authKey();
    public massMessenger(String username) throws Exception {
        String AuthKey = authKey.getter().substring(2);

        logger.info("System: user went to messengerChilds.massMessenger");

        Parent ListRoot = FXMLLoader.load(getClass().getResource("/layout/mini_page/mass_message.fxml"));
        Scene ListScene = new Scene(ListRoot);
        Stage ListStage = new Stage();
        ListStage.setScene(ListScene);
        ListStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));

        ListStage.show();
        TextArea Text = (TextArea) ListScene.lookup("#text");
        Back = (Button) ListScene.lookup("#back");
        Button All = (Button) ListScene.lookup("#all");
        Button Cat = (Button) ListScene.lookup("#cat");
        Button Selected = (Button) ListScene.lookup("#selected");
        Button Pic = (Button) ListScene.lookup("#pic");
        Back.setText("Cancel");
        if(!catName.isEmpty()) {
            All.setVisible(false);
            Selected.setVisible(false);
            Cat.setMaxWidth(Region.USE_COMPUTED_SIZE);
            Cat.setMinWidth(Region.USE_COMPUTED_SIZE);
            Cat.setPrefWidth(Region.USE_COMPUTED_SIZE);
            Cat.setText("Send to "+ catName +" category");
        }
        All.setOnAction(event -> {
            if (!Text.getText().isEmpty()) {

                LocalDateTime now = LocalDateTime.now();
                ArrayList<String> usernames = new ArrayList<String>();

                JSONObject likeRequest = listJson.get("followings",username);
                JSONObject outputJson = sender.send(likeRequest);
                try {
                    outputJson.get("result");
                }catch (JSONException e){
                    JOptionPane.showMessageDialog(null,"This is online future");
                    try {
                        Back.getScene().getWindow().hide();
                        new Messenger(AuthKey);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return;
                }
                new jsonListDecoder(outputJson);

                usernames = jsonListDecoder.username;
                if (usernames.size()>0) {
                    for (int j = 0; j < usernames.size(); j++) {
                        sb.SubMess(Text.getText(), username, usernames.get(j),dtf.format(now));
                    }
                    JOptionPane.showMessageDialog(null, "Messages sent to all followings!");
                    try {
                        Back.getScene().getWindow().hide();
                        new Messenger(AuthKey);
                    } catch (Exception e) {
                        logger.error("Error in go to message after message to all");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "You dont follow anybody to send message");
                }
            }
        });

        Selected.setOnAction(event -> {
            if (!Text.getText().isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                ArrayList<String> usernames = new ArrayList<String>();

                JSONObject likeRequest = listJson.get("followings",username);
                JSONObject outputJson = sender.send(likeRequest);

                try {
                    outputJson.get("result");
                }catch (JSONException e){
                    JOptionPane.showMessageDialog(null,"This is online future");
                    try {
                        Back.getScene().getWindow().hide();
                        new Messenger(AuthKey);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return;
                }

                new jsonListDecoder(outputJson);

                usernames = jsonListDecoder.username;

                if (usernames.size()>1) {
                    userSelect.select = new ArrayList<>();
                    try {
                        new userSelect(AuthKey,username);
                    } catch (IOException e) {
                        logger.error("Error in loading user select page from mass messenger");
                    }
                    boolean sent = false;
                    for (int i = 0; i < userSelect.select.size(); i++) {
                        if (userSelect.select.get(i).isSelected()) {
                            sb.SubMess(Text.getText(), username, usernames.get(i),dtf.format(now));
                            sent = true;
                        }
                    }
                    if (sent)
                    JOptionPane.showMessageDialog(null, "Messages sent to selected followings!");

                    try {
                        Back.getScene().getWindow().hide();
                        new Messenger(AuthKey);
                    } catch (Exception e) {
                        logger.error("Error in go to message after message to all");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "You dont follow anybody");
                }
            }
        });

        ListScene.setOnKeyPressed(keyEvent -> {

            if (keyEvent.getCode() == KeyCode.ENTER && !Text.getText().isEmpty()  &&  !catName.isEmpty() ) {
                LocalDateTime now = LocalDateTime.now();
                List<String> usernames = new ArrayList<>();
                JSONArray jsonArray1;
                categoryConnection connection = new categoryConnection();
                JSONObject resultJson = connection.getter("getMembers", AuthKey, catName);

                try {
                    resultJson.get("result");
                }catch (JSONException e){
                    JOptionPane.showMessageDialog(null,"This is online future");
                    try {
                        Back.getScene().getWindow().hide();
                        new Messenger(AuthKey);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return;
                }

                if (resultJson.get("result").equals("0")) {
                    JOptionPane.showMessageDialog(null, "Error in connection");
                } else {
                    jsonArray1 = resultJson.getJSONArray("usernames");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        usernames.add(jsonArray1.get(i).toString());
                    }
                }


                for (int j = 0; j < usernames.size(); j++) {
                    var sb = new submitMessage();
                    sb.SubMess(Text.getText(), username, usernames.get(j),dtf.format(now));
                }
                JOptionPane.showMessageDialog(null, "Message sent to members of " +
                        catName + " category");
                try {
                    catName = "";
                    Back.getScene().getWindow().hide();
                    new Category(AuthKey);
                } catch (Exception e) {
                    logger.error("error in reloading mass messenger after sending message");
                }
            }
        });
        Cat.setOnAction(event -> {
            if (!Text.getText().isEmpty()) {
                    if (catName.isEmpty()) {
                        try {
                            new Category(AuthKey, Text.getText());
                        } catch (Exception e) {
                            logger.error("Error in go to category for sending message");
                        }
                    } else {
                        LocalDateTime now = LocalDateTime.now();
                        List<String> usernames = new ArrayList<>();
                        JSONArray jsonArray1 ;
                        categoryConnection connection = new categoryConnection();
                        JSONObject resultJson = connection.getter("getMembers",AuthKey,catName);

                        try {
                            resultJson.get("result");
                        }catch (JSONException e){
                            JOptionPane.showMessageDialog(null,"This is online future");
                            try {
                                Back.getScene().getWindow().hide();
                                new Messenger(AuthKey);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            return;
                        }

                        if (resultJson.get("result").equals("0")) {
                            JOptionPane.showMessageDialog(null, "Error in connection");
                        } else {
                            jsonArray1 = resultJson.getJSONArray("usernames");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                usernames.add(jsonArray1.get(i).toString());
                            }
                        }

                        var sb = new submitMessage();
                        for (int j = 0; j < usernames.size(); j++) {
                            sb.SubMess(Text.getText(), username, usernames.get(j),dtf.format(now));
                        }
                        JOptionPane.showMessageDialog(null, "Message sent to members of " +
                                catName + " category");
                        try {
                            catName = "";
                            Back.getScene().getWindow().hide();
                            new Category(AuthKey);
                        } catch (Exception e) {
                            logger.error("error in reloading mass messenger after sending message");
                        }
                    }
                }else{
            JOptionPane.showMessageDialog(null, "You dont have any categories");
                Back.getScene().getWindow().hide();
                try {
                    new Messenger(AuthKey);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });

    }
}
