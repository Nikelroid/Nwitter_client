package notifications;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import jsonContoller.jsonNotifs;
import jsonContoller.jsonUsers;
import objects.objNotifs;
import org.apache.commons.io.FileUtils;
import profiles.getImageFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class notifsPrint {

    public static AnchorPane user;
    jsonNotifs Not = new jsonNotifs();
    List<objNotifs> notifs = Not.get();
    jsonUsers get_users = new jsonUsers();
    ImageView Icon;
    Label Text, Username;
    public static ArrayList<Button> Dismiss;

    private void difiner() throws IOException {

        user = FXMLLoader.load(getClass().getResource("/layout/cards/notifs_card.fxml"));
        Username = (Label) user.lookup("#username");
        Text = (Label) user.lookup("#text");
        Dismiss.add((Button) user.lookup("#dismiss"));
        Icon = (ImageView) user.lookup("#icon");

    }

    private void setter(int type,String user,int flag) {

        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.profile(user);
            Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            Icon.setImage(image);
        } catch (NullPointerException | FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        Username.setText(user);
        switch (type) {
            case 1 -> {
                Text.setText(" started following you ");
                Text.getStyleClass().add("accept");
            }
            case 2 -> {
                Text.setText(" unfollowed you ");
                Text.getStyleClass().add("wrong");
            }
            case 3 -> {
                Text.setText(" blocked you ");
                Text.getStyleClass().add("wrong");
            }
            case 4 -> {
                Text.setText(" unblocked you ");
                Text.getStyleClass().add("accept");
            }
            case 5 -> {
                Text.setText(" muted you ");
                Text.getStyleClass().add("wrong");
            }
            case 6 -> {
                Text.setText(" unmuted you ");
                Text.getStyleClass().add("accept");
            }
            case 7 -> {
                Text.setText(" deleted you ");
                Text.getStyleClass().add("wrong");
            }
            case 8 -> {
                Text.setText(" recieved your request and doesn't decided accept or reject you ");
                Text.getStyleClass().add("username");
                Dismiss.get(Dismiss.size()-1).setVisible(false);
            }
            case 9 -> {
                Text.setText(" accepted your follow request ");
                Text.getStyleClass().add("accept");
            }
            case 10 -> {
                Text.setText(" rejected your follow request ");
                Text.getStyleClass().add("wrong");
            }
        }

    }
    private void adder(){

        Notifications.twitteList.getChildren().add(user);
    }


    public notifsPrint(int type,String user,int flag) throws IOException {
        difiner();
        setter(type,user,flag);
        adder();
    }
}
